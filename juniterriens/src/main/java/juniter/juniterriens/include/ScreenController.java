package juniter.juniterriens.include;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

@Getter
@Setter
public class ScreenController {
    private HashMap<String, Pane> screenMap = new HashMap<>();
    private Scene main;
    private static final Logger LOG = LogManager.getLogger(ScreenController.class);


    public ScreenController() {

    }

    public void addScreen(String name, Pane pane) {
        screenMap.put(name, pane);
        LOG.info("addScreen " + name + "  " + pane + " " + main);
    }

    public void removeScreen(String name) {
        screenMap.remove(name);
    }

    public void removeScreens( ) {
        screenMap.clear();
    }

    public void activate(String name) {
        main.setRoot(screenMap.get(name));
    }

    public boolean hasScreen(String name) {
        return screenMap.containsKey(name);
    }
}