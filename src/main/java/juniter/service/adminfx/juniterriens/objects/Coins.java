package juniter.service.adminfx.juniterriens.objects;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import juniter.service.adminfx.juniterriens.engine.Collectable;

public class Coins implements Collectable {
    private static MediaPlayer mp3MusicFile;

    private static void getorSet() {
        mp3MusicFile = new MediaPlayer(new Media(Coins.class.getResource("/adminfx/game/Coins.mp3").toExternalForm()));
        mp3MusicFile.setAutoPlay(false);
        mp3MusicFile.setVolume(0.9);

        mp3MusicFile.setOnEndOfMedia(() -> {
            mp3MusicFile.seek(Duration.ZERO);
            mp3MusicFile.pause();
        });
    }

    private Image image;
    private double positionX;
    private double positionY;
    private double width;
    private double height;

    public Coins(String filename) {
        positionX = 0;
        positionY = 0;

        image = new Image(filename);
        width = 32;
        height = 32;
    }

    public static void playSound() {
        if (mp3MusicFile == null)
            getorSet();
        mp3MusicFile.play();
    }


    public void setPosition(double x, double y) {
        positionX = x;
        positionY = y;
    }

    public void render(GraphicsContext gc) {
        gc.drawImage(image, positionX, positionY, width, height);
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