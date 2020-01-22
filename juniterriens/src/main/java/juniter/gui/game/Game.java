package juniter.gui.game;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import juniter.gui.game.characters.Player;
import juniter.gui.game.characters.WhiteRabbit;
import juniter.gui.game.screens.Room;
import juniter.gui.game.screens.TheBeginning;
import juniter.gui.technical.I18N;
import juniter.service.jpa.Index;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static juniter.gui.JuniterBindings.*;

@ConditionalOnExpression("${juniter.useJavaFX:false}")
@Component
public class Game implements Initializable {
    private static final Logger LOG = LogManager.getLogger(Game.class);
    public HBox container;

    @FXML
    private Canvas canvas;

    @FXML
    private TextField erg;

    private static MediaPlayer backgroundMusic;

    private WhiteRabbit bunny;

    private GraphicsContext gc;

    public static boolean foundRule5 = false;


    @Autowired
    public Index index;

    public static List<String> expiryList;


    @EventListener
    public void handleContextStart(ContextStartedEvent cse) {
        LOG.info("Handling context started event.");
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gc = canvas.getGraphicsContext2D();
        //canvas.heightProperty().bind(container.heightProperty());
        //canvas.widthProperty().bind(container.widthProperty());


        Platform.runLater(() -> playing.addListener(observable -> {
            if (playing.get()) {
                LOG.info("focus on game ");
                canvas.requestFocus();
            }
        }));

        backgroundMusic = new MediaPlayer(new Media(getClass().getResource("/gui/game/listen/save.mp3").toExternalForm()));
        backgroundMusic.setStartTime(Duration.ZERO);
        backgroundMusic.setVolume(0.2);   // from 0 to 1
        backgroundMusic.setAutoPlay(false);
        backgroundMusic.play();

        erg.setOnKeyPressed(e -> {
            String code = e.getCode().toString();
            //System.out.println("Game " + code);
            if (!keyboardInput.contains(code))
                keyboardInput.add(code);
        });

        erg.setOnKeyReleased(e -> {
            String code = e.getCode().toString();
            keyboardInput.remove(code);
        });

        Platform.runLater(() -> {
            long today = System.currentTimeMillis() / 1000L;
            long yesterday = today - 86400;

            expiryList = index.getMRepo().expiresBetween(yesterday, today);
            expiryList.add(0, I18N.get("game.moul.expire"));
            expiryList.add(0, I18N.get("game.moul.hi"));
        });

        Room.canvas = canvas;
        var r = new TheBeginning();

        isIndexing.addListener((observable, oldValue, newValue) -> {
            if (newValue)
                if (indexRatio.doubleValue() < .95)
                    r.getTimeline().stop();
                else {
                    r.getTimeline().play();
                }
        });

        Player.get().setPosition(canvas.getWidth() / 2, canvas.getHeight() / 2);
        r.run();
    }


}
