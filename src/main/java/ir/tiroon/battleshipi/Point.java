package ir.tiroon.battleshipi;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import rpi.sensehat.api.dto.Color;


public class Point implements Runnable {

    static volatile boolean blinking = false;
    public int x;
    public int y;
    public Color color = Color.of(0, 0, 0);
    Thread blinkingThread = null;
    private Color previousColor = Color.of(0, 0, 0);

    @JsonCreator
    public Point(@JsonProperty("x") int x, @JsonProperty("y") int y, @JsonProperty("color") Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.previousColor = color;
    }

    public Point(Point point) {
        this.x = point.x;
        this.y = point.y;
        this.color = point.color;
        this.previousColor = point.getPreviousColor();
    }

    public synchronized static boolean isBlinking() {
        return blinking;
    }

    public synchronized void setBlinking(boolean blinking) {
        this.blinking = blinking;
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
        while (blinkingThread.isAlive()) {
        }
        SenseHatUtil.senseHat.ledMatrix.setPixel(x, y, this.color);
    }

    public void turnOff() {
        setBlinking(false);
        SenseHatUtil.senseHat.ledMatrix.setPixel(x, y, Color.of(0, 0, 0));
    }

    public void startBlinking() {
        previousColor = color;
        blinkingThread = new Thread(this);
        blinkingThread.start();
    }

    //The blinkingThread won't notice that blinking has set to false!:(
    public void stopBlinking() {
        setBlinking(false);
        lightUp(previousColor);
    }

    public void stopBlinkingAndLightUp(Color color) {
        setBlinking(false);
        lightUp(color);
    }

    public void stateLessLightUp(Color color) {
        SenseHatUtil.senseHat.ledMatrix.setPixel(x, y, color);
    }

    @Override
    public void run() {
        setBlinking(true);

        while (isBlinking()) {
            SenseHatUtil.waitFor(30);
            stateLessLightUp(Color.BLUE);
            SenseHatUtil.waitFor(30);
            stateLessLightUp(previousColor);
        }

        System.out.println("Blinking thread finished");
    }

    public boolean isOnTheSameLocationAs(Point other) {
        System.out.println("Checking equality of "+this.x +"=="+ other.x +" AND "+ this.y +"=="+ other.y);
        return (this.x == other.x && this.y == other.y);
    }
}