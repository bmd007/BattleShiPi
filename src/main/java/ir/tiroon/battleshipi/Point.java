package ir.tiroon.battleshipi;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import rpi.sensehat.api.dto.Color;


public class Point {

    public int x;
    public int y;
    public Color color = Color.BLUE;
    static volatile boolean blinking = false;
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
        while (blinkingThread.isAlive()){}
        SenseHatUtil.senseHat.ledMatrix.setPixel(x, y, this.color);
    }

    public void turnOff() {
        setBlinking(false);
        SenseHatUtil.senseHat.ledMatrix.setPixel(x, y, Color.of(0, 0, 0));
    }

    public void startBlinking() {
        previousColor = color;
        color = Color.BLUE;
        setBlinking(true);
        blinkingThread = new Thread(blinkingRunnable);
        blinkingThread.start();
    }

    Thread blinkingThread = null;

    //The blinkingThread won't notice that blinking has set to false!:(
    public void stopBlinking() {
        setBlinking(false);
        lightUp(previousColor);
    }

    public void stopBlinkingAndLightUp(Color color) {
        setBlinking(false);
        lightUp(color);
    }

    public synchronized static boolean isBlinking() {
        return blinking;
    }

    public synchronized void setBlinking(boolean blinking) {
        this.blinking = blinking;
    }

    private Runnable blinkingRunnable = () -> {

        while (isBlinking()) {
            SenseHatUtil.waitFor(30);
            stateLessLightUp(Color.BLUE);
//            SenseHatUtil.senseHat.ledMatrix.setPixel(x, y, Color.of(0, 0, 0));
            SenseHatUtil.waitFor(30);
            stateLessLightUp(previousColor);
//            SenseHatUtil.senseHat.ledMatrix.setPixel(x, y, Color.BLUE);
        }

        System.out.println("Blinking thread finished");

    };

    public void stateLessLightUp(Color color){
        SenseHatUtil.senseHat.ledMatrix.setPixel(x, y,color);
    }
}
