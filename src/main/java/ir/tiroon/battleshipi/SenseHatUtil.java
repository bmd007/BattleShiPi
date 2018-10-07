package ir.tiroon.battleshipi;

import rpi.sensehat.api.SenseHat;

public class SenseHatUtil {

    public static SenseHat senseHat = new SenseHat();

    public static void waitFor(Integer milliSeconds) {


        try {
            Thread.sleep(milliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
