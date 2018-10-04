package ir.tiroon.battleshipi;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import rpi.sensehat.api.dto.Color;


public class Point {

    public int x;
    public int y;
    public Color color = Color.BLUE;
    public Boolean blinking = false;

    @JsonCreator
    public Point(@JsonProperty("key") int x, @JsonProperty("key") int y, @JsonProperty("key") Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public void lightUp(Color color) {
        this.color = color;
        SenseHatUtil.senseHat.ledMatrix.setPixel(x, y, this.color);
    }

    public void lightUp() {
        SenseHatUtil.senseHat.ledMatrix.setPixel(x, y, this.color);
    }

    public void turnOff() {
        SenseHatUtil.senseHat.ledMatrix.setPixel(x, y, Color.of(0, 0, 0));
    }

    public void startBlinking() {
        blinking = true;
        blinkingThread.start();
    }

    public void stopBlinking(){
        synchronized (blinking) {
            blinking = false;
        }
    }

    private Thread blinkingThread = new Thread(
            new Runnable() {
                @Override
                public void run() {
                    while (blinking) {
                        SenseHatUtil.senseHat.ledMatrix.setPixel(x, y, Color.of(0, 0, 0));
                        SenseHatUtil.waitFor(150);
                        SenseHatUtil.senseHat.ledMatrix.setPixel(x, y, Color.BLUE);
                        SenseHatUtil.waitFor(100);
                    }
                }
            }
    );

}
