package juniter.gui.include;

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

    public enum PanelName {
        MAIN, GRAPHS, NOTARY, NETWORK, DATABASE, SPARK, SETTINGS
    }

    private static final Logger LOG = LogManager.getLogger(ScreenController.class);

    private HashMap<PanelName, Pane> screenMap = new HashMap<>();

    private Scene main;


    public ScreenController() {

    }

    public void addScreen(PanelName name, Pane pane) {
        screenMap.put(name, pane);
        LOG.info("addScreen " + name + "  " + pane + " " + main);

    }

    public void removeScreen(String name) {
        screenMap.remove(name);
    }

    public void removeScreens( ) {
        screenMap.clear();
        main = null;
    }

    public void activate(PanelName name) {
        main.setRoot(screenMap.get(name));
    }

    public boolean hasScreen(PanelName name) {
        return screenMap.containsKey(name);
    }
}