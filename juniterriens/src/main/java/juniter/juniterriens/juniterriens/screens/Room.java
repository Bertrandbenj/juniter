package juniter.juniterriens.juniterriens.screens;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.util.Duration;
import juniter.juniterriens.juniterriens.engine.Gate;
import lombok.Data;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
@Getter
public abstract class Room implements Initializable {

    protected static final Logger LOG = LogManager.getLogger(Room.class);

    protected static Canvas canvas;

    protected static Room singleton;

    protected List<Gate> gates;

    protected Timeline timeline = new Timeline();

    static public Room singleton(Class<? extends Room> x) {

        if (singleton == null) {
            try {
                singleton = (Room) x.getDeclaredConstructors()[0].newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
               LOG.error("Creating room ",e);
            }
        }

        return singleton;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        LOG.info("Entered Room " + this.getClass().getSimpleName());

        setGates();

        timeline.getKeyFrames().setAll(new KeyFrame(Duration.millis(16), event -> {
            canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());


        }));
    }

    abstract void setGates();

}
