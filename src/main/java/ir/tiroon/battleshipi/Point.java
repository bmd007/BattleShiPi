package ir.tiroon.battleshipi;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import rpi.sensehat.api.dto.Color;


public class Point {

    public int x;
    public int y;
    public Color color = Color.BLUE;
    public volatile boolean blinking = false;
    private Color previousColor = Color.GREEN;

    @JsonCreator
    public Point(@JsonProperty("key") int x, @JsonProperty("key") int y, @JsonProperty("key") Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.previousColor = color;
//        lightUp();
    }

    public Point(Point point) {
        this.x = point.x;
        this.y = point.y;
        this.color = point.color;
        this.previousColor = point.getPreviousColor();
    }

    public Color getPreviousColor() {
        return previousColor;
    }

    public void lightUp(Color color) {
        this.color = color;
        previousColor = color;
        SenseHatUtil.senseHat.ledMatrix.setPixel(x, y, this.color);
    }

    public void lightUp() {
        SenseHatUtil.senseHat.ledMatrix.setPixel(x, y, this.color);
    }

    public void turnOff() {
        blinking = false;
        SenseHatUtil.senseHat.ledMatrix.setPixel(x, y, Color.of(0, 0, 0));
    }

    public void startBlinking() {
        previousColor = color;
        color = Color.BLUE;
        blinking = true;
        blinkingThread = new Thread(blinkingRunnable);
        blinkingThread.start();
    }

    Thread blinkingThread = null;

    public void stopBlinking() {
        blinking = false;
        lightUp(previousColor);
        SenseHatUtil.waitFor(500);

//        blinkingThread.stop();
    }

    private Runnable blinkingRunnable = () -> {

        while (blinking) {
            SenseHatUtil.senseHat.ledMatrix.setPixel(x, y, Color.of(0, 0, 0));
            SenseHatUtil.waitFor(30);
            SenseHatUtil.senseHat.ledMatrix.setPixel(x, y, Color.BLUE);
            SenseHatUtil.waitFor(30);
        }

        System.out.println("Blinking thread finished");

    };

}
