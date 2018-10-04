package ir.tiroon.battleshipi;

import rpi.sensehat.api.SenseHat;

public class SenseHatUtil {

    public static SenseHat senseHat = new SenseHat();

    public static void waitFor(Integer seconds) {
        try {
            Thread.sleep(seconds * 1000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
