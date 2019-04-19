package juniter.juniterriens.juniterriens.screens;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import juniter.juniterriens.include.*;
import juniter.juniterriens.juniterriens.Game;
import juniter.juniterriens.juniterriens.characters.Player;
import juniter.juniterriens.juniterriens.engine.Collectable;
import juniter.juniterriens.juniterriens.engine.Curiosity;
import juniter.juniterriens.juniterriens.engine.Gate;
import juniter.juniterriens.juniterriens.objects.Coins;

import java.io.IOException;
import java.util.ResourceBundle;


public class BBRoom extends Room {

    private Curiosity duniter;

    private Boolean txOpen = false;

    public BBRoom() {

    }

    @Override
    void roomSpecific() {

        var theFont = Font.font("Helvetica", FontWeight.BOLD, 24);
        gc().setFill(Color.valueOf("F4F4F4"));
        gc().setFont(theFont);
        gc().fillText(I18N.get("game.pi.rule5"), canvas.getWidth() / 2, canvas.getHeight() / 2);

        if (JuniterBindings.selectedTheme.getValue().equals(JuniterBindings.DARK_THEME)) {
            Game.foundRule5 = true;
        }

        for (Collectable moneybag : collectables)
            moneybag.render(gc());

        duniter.render(gc(), Player.get().intersects(duniter));

        collectables.forEach(x -> {

            if (duniter.intersects(x) && !txOpen) {
//                Parent root;
//
//                root = (Parent) load("/juniterriens/include/TxPanel.fxml");
//
//
//                Stage stage = new Stage();
//                stage.setTitle("Tx");
//                stage.setScene(new Scene( root, 700, 450));
//                stage.show();
                txOpen = true;


                Platform.runLater(()-> txOpen = TxBox.display("DUNITER", txOpen));

            }
        });

        collectables.removeIf(duniter::intersects);


    }

    protected Object load(String url) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(url));
            // loader.setControllerFactory(aClass -> applicationContext.getBean(aClass));

            loader.setResources(ResourceBundle.getBundle("Internationalization", I18N.getLocale()));

            return loader.load();
        } catch (Exception e) {

            throw new RuntimeException(String.format("Failed to load FXML file '%s' ", url) + getClass().getResource(url), e);
        }
    }

    @Override
    void preset() {
        gates.add(new Gate(new SavingMarcel(), canvas.getWidth() / 2, 0, canvas.getWidth() / 2, canvas.getHeight() - 100));
        gates.add(new Gate(new RoundTable(), canvas.getWidth() / 2, canvas.getHeight() - 50, canvas.getWidth() / 2, 100));
        gates.add(new Gate(new Neighbourhood(), 0, canvas.getHeight() / 2, canvas.getWidth() - 100, canvas.getHeight() / 2));

        duniter = new Curiosity("/juniterriens/game/img/duniter.png", I18N.get("game.trm"), 200, 200);

        collectables.clear();
        for (int i = 0; i < 15; i++) {
            Coins coin = new Coins();

            double px = (canvas.getWidth() - 50) * Math.random() + 25;
            double py = (canvas.getHeight() - 50) * Math.random() + 25;
            coin.setPosition(px, py);
            collectables.add(coin);
        }
    }


}
