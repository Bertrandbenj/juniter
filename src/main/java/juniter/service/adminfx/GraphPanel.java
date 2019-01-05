package juniter.service.adminfx;

import javafx.concurrent.Worker.State;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import juniter.repository.jpa.BlockRepository;
import juniter.service.adminfx.include.AbstractJuniterFX;
import juniter.service.web.GraphvizService;
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
import org.w3c.dom.events.MouseEvent;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

@ConditionalOnExpression("${juniter.useJavaFX:false}")
@Component
public class GraphPanel extends AbstractJuniterFX implements Initializable {

	private static final Logger LOG = LogManager.getLogger();

	@FXML WebView SVGAnchor;

	@FXML WebView  CesiumAnchor;

	@FXML WebView  WotMapAnchor;

	@FXML TextField uri;

	@Autowired
	GraphvizService graphvizService;

	@Autowired
	BlockRepository blockRepo;

	@Override
	public void start(Stage primaryStage) {
		LOG.info("Starting GraphPanel");



		primaryStage.setTitle("Juniter - Dot visualisation  ");
		primaryStage.show();
	}

    @FXML
    public void openInBrowser(){

	    if(isWeb()){
            getHostServices().showDocument(uri.getText());
        }else if(isGraphviz()){
            getHostServices().showDocument("https://localhost:8443" + uri.getText());
        }
    }




	private boolean isGraphviz(){
	    return uri.getText().startsWith("/graphviz");
    }

    private boolean isWeb(){
        return uri.getText().startsWith("http");
    }



	@FXML
	public void go() {
		LOG.info("opening Page : " + uri.getText() );
		var webEngine = SVGAnchor.getEngine(); 	// Get WebEngine via WebView


		if(isWeb()){
            SVGAnchor.setZoom(1);
			webEngine.load(uri.getText()); 	// Load page

		}else if(isGraphviz()) {
			// assume '/' starting urls relate to local juniter

            SVGAnchor.setZoom(0.60);

			var params = uri.getText().substring(1).split("/");
			var file = GraphvizService.FileOutputType.valueOf(params[1]);
			var out = GraphvizService.GraphOutput.valueOf(params[2]);
			String identifier = params[3];

			try{
				webEngine.loadContent(graphvizService.build(file,out,identifier, new HashMap<>()));

			}catch (Exception e){
				LOG.error("Problem calling graphviz service " , e);
			}
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		uri.setText("/graphviz/svg/block/"+(blockRepo.currentBlockNumber()-2));

		var webEngine = SVGAnchor.getEngine(); 	// Get WebEngine via WebView
		SVGAnchor.setZoom(0.60);

		webEngine.getLoadWorker().stateProperty().addListener((ov, oldState, newState) -> {
			if (newState == State.SUCCEEDED) {
				// note next classes are from org.w3c.dom domain
				EventListener listener = (Event ev) ->  {
					var me = ((MouseEvent) ev);
					var link = ((Element)ev.getTarget()).getAttribute("href");

					if(link == null)
						link = ((Element)ev.getTarget()).getAttribute("xlink:href");

					//in case of an SVG document, we're likely going to click a child of the <a> tag, so we try the parent
					var list = ((Element)ev.getTarget()).getParentNode().getAttributes();
					for(var i = 0; i < list.getLength(); i++ ){
						if(list.item(i).getNodeName().endsWith("href")){
							link = list.item(i).getNodeValue();
						}
					}

					if(link != null ){
						uri.setText(link);
						go();
					}else{
						LOG.error("no link found for event " + ev + " on " + ev.getTarget());
					}

				};

				Document doc = webEngine.getDocument();
				LOG.debug("doc : "+ doc);
				if(doc != null) {
					Element el = doc.getElementById("a");
					NodeList aaa = doc.getElementsByTagName("a");
					LOG.debug("list: "+ aaa.getLength());

					for (int i=0; i<aaa.getLength(); i++) {

						var target = ((EventTarget) aaa.item(i));
						LOG.debug("target = " + target + " - " );
						target.addEventListener("click", listener, false);
					}
				}


			}
		});

		go();
		WotMapAnchor.getEngine().load("https://duniter.normandie-libre.fr/wotmap/");
		CesiumAnchor.getEngine().load("https://g1.le-sou.org/#/app/currency/lg");
	}
}
