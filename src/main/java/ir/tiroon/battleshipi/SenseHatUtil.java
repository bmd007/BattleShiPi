package ir.tiroon.battleshipi;

import rpi.sensehat.api.SenseHat;
import rpi.sensehat.api.dto.JoystickEvent;

public class SenseHatUtil {

    public static SenseHat senseHat = new SenseHat();

    public static void waitFor(Integer milliSeconds) {


        try {
            Thread.sleep(milliSeconds);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
