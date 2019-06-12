package juniter.gui.game;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import juniter.gui.include.I18N;
import juniter.gui.include.JuniterBindings;
import juniter.gui.game.characters.Player;
import juniter.gui.game.characters.WhiteRabbit;
import juniter.gui.game.screens.TheBeginning;
import juniter.gui.game.screens.Room;
import juniter.repository.jpa.index.MINDEXRepository;
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

@ConditionalOnExpression("${juniter.useJavaFX:false}")
@Component
public class Game implements Initializable {
    private static final Logger LOG = LogManager.getLogger(Game.class);

    @FXML
    private Canvas canvas;

    @FXML
    private TextField erg;

    private static MediaPlayer backgroundMusic;

    private WhiteRabbit bunny;

    private GraphicsContext gc;

    public static boolean foundRule5 = false;


    @Autowired
    public MINDEXRepository mRepo;

    public static List<String> expiryList;


    @EventListener
    public void handleContextStart(ContextStartedEvent cse) {
        LOG.info("Handling context started event.");
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gc = canvas.getGraphicsContext2D();

        Platform.runLater(() -> JuniterBindings.playing.addListener(observable -> {
            if (JuniterBindings.playing.get()) {
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
            if (!JuniterBindings.input.contains(code))
                JuniterBindings.input.add(code);
        });

        erg.setOnKeyReleased(e -> {
            String code = e.getCode().toString();
            JuniterBindings.input.remove(code);
        });

        Platform.runLater(() -> {
            long today = System.currentTimeMillis() / 1000L;
            long yesterday = today - 86400;

            expiryList = mRepo.expiresBetween(yesterday, today);
            expiryList.add(0, I18N.get("game.moul.expire"));
            expiryList.add(0, I18N.get("game.moul.hi"));
        });

        Room.canvas = canvas;
        var r = new TheBeginning();

        Player.get().setPosition(canvas.getWidth() / 2, canvas.getHeight() / 2);
        r.run();
    }


}
