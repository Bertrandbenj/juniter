package juniter.gui.business.page;

import javafx.concurrent.Worker.State;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import juniter.gui.technical.AbstractJuniterFX;
import juniter.service.web.GraphvizService;
import juniter.user.UserSettings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static juniter.gui.JuniterBindings.currenBlock;

@ConditionalOnExpression("${juniter.useJavaFX:false}")
@Component
public class GraphPanel extends AbstractJuniterFX implements Initializable {

    private static final Logger LOG = LogManager.getLogger(GraphPanel.class);

    @FXML
    private TabPane tabPane;

    @FXML
    private WebView SVGAnchor;

    @FXML
    private TextField uri;

    @Autowired
    private Optional<GraphvizService> graphvizService;

    private List<String> bookmarks = new UserSettings().getBookmarks();


    @Override
    public void start(Stage primaryStage) {
        LOG.info("Starting GraphPanel");

        uri.setOnAction(e -> go());

        primaryStage.setTitle("Juniter - Dot visualisation  ");
        primaryStage.show();
    }

    private boolean isGraphviz() {
        return uri.getText().startsWith("/graphviz");
    }

    private boolean isLocal() {
        return uri.getText().startsWith("/");
    }

    private boolean isWeb() {
        return uri.getText().startsWith("http");
    }

    @FXML
    public void go() {
        LOG.info("opening Page : " + uri.getText());
        var webEngine = SVGAnchor.getEngine();    // Get WebEngine via WebView


        if (isWeb()) {
            SVGAnchor.setZoom(1);
            webEngine.load(uri.getText());    // Load page

        } else if (isGraphviz() ) {
            // assume '/' starting urls relate to local juniter

            SVGAnchor.setZoom(0.60);

            var params = uri.getText().substring(1).split("/");
            var file = GraphvizService.FileOutputType.valueOf(params[1]);
            var out = GraphvizService.GraphOutput.valueOf(params[2]);
            String identifier = params[3];

            try {
                graphvizService.ifPresent(graphvizService1 ->
                        webEngine.loadContent(graphvizService1.build(file, out, identifier, new HashMap<>())));

            } catch (Exception e) {
                LOG.error("Problem calling graphviz service ", e);
            }
        }else if(isLocal()){
            SVGAnchor.setZoom(1);
            String url  ="https://"+hostAddress+":8443"+uri.getText();
            LOG.info("loading "+url);
            webEngine.load(url);
        }
    }

    private String hostAddress;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        uri.setText("/graphviz/svg/block/" + (currenBlock.get().getNumber() - 2));

        // Remote address
        hostAddress = InetAddress.getLoopbackAddress().getHostAddress();


        var webEngine = SVGAnchor.getEngine();    // Get WebEngine via WebView
        SVGAnchor.setZoom(0.60);

        webEngine.getLoadWorker().stateProperty().addListener((ov, oldState, newState) -> {
            if (newState == State.SUCCEEDED) {
                // note next classes are from org.w3c.dom domain
                EventListener listener = (Event ev) -> {
                    //var me = ((MouseEvent) ev);
                    var link = ((Element) ev.getTarget()).getAttribute("href");

                    if (link == null)
                        link = ((Element) ev.getTarget()).getAttribute("xlink:href");

                    //in case of an SVG document, we're likely going to click a child of the <a> tag, so we try the parent
                    var list = ((Element) ev.getTarget()).getParentNode().getAttributes();
                    for (var i = 0; i < list.getLength(); i++) {
                        if (list.item(i).getNodeName().endsWith("href")) {
                            link = list.item(i).getNodeValue();
                        }
                    }

                    if (link != null) {
                        uri.setText(link);
                        go();
                    } else {
                        LOG.error("no link found for event " + ev + " on " + ev.getTarget());
                    }

                };

                Document doc = webEngine.getDocument();
                LOG.debug("Doc : " + doc);
                if (doc != null) {
                    // Element el = Doc.getElementById("a");
                    NodeList aaa = doc.getElementsByTagName("a");
                    LOG.debug("list: " + aaa.getLength());

                    for (int i = 0; i < aaa.getLength(); i++) {

                        var target = ((EventTarget) aaa.item(i));
                        LOG.debug("target = " + target + " - ");
                        target.addEventListener("click", listener, false);
                    }
                }


            }
        });

        go();

        tabPane.getTabs().addAll(bookmarks.stream().map(b -> {
            Tab tab = new Tab(hostOf(b));
            var webView = new WebView();
            tab.setContent(webView);
            tab.selectedProperty().addListener((obs, old, newValue) -> {
                if (newValue)
                    webView.getEngine().load(b);
            });
            return tab;
        }).collect(Collectors.toList()));
    }

    private String hostOf(String url) {

        try {
            URL u = new URL(url);
            return u.getHost();
        } catch (MalformedURLException e) {
            LOG.info("Parsing url ", e);
        }
        return null;
    }
}
