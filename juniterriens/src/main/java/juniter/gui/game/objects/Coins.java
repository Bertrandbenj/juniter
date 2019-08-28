package juniter.gui.game.objects;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import juniter.gui.game.engine.Collectable;
import juniter.gui.game.screens.Room;

public class Coins implements Collectable {

    private static MediaPlayer mp3MusicFile;

    public static void playSound() {
        if (mp3MusicFile == null){
            mp3MusicFile = new MediaPlayer(new Media(Coins.class.getResource("/gui/game/listen/Coins.mp3").toExternalForm()));
            mp3MusicFile.setAutoPlay(false);
            mp3MusicFile.setVolume(0.9);

            mp3MusicFile.setOnEndOfMedia(() -> {
                mp3MusicFile.seek(Duration.ZERO);
                mp3MusicFile.pause();
            });
        }

        mp3MusicFile.play();
    }


    private Image image;
    private double positionX;
    private double positionY;
    private double width;
    private double height;

    public Coins( ) {
        positionX = 0;
        positionY = 0;

        image = new Image("/gui/game/img/coin.png");
        width = 32;
        height = 32;
    }



    public void setPosition(double x, double y) {
        positionX = x;
        positionY = y;
    }

    public void render(GraphicsContext gc) {
        gc.drawImage(image, positionX, positionY, width, height);
    }

    @Override
    public void render() {
        render(Room.canvas.getGraphicsContext2D());
    }

    public Rectangle2D getBoundary() {
        return new Rectangle2D(positionX, positionY, width, height);
    }

    //public boolean intersects(Coins s) {
    //    return s.getBoundary().intersects(this.getBoundary());
    // }

    public String toString() {
        return " Position: [" + positionX + "," + positionY + "]"
                ;
    }
}