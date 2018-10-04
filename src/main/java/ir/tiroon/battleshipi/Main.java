package ir.tiroon.battleshipi;

import rpi.sensehat.api.SenseHat;
import rpi.sensehat.api.dto.Color;

public class Main {

    public static void main(String[] args) {
        System.out.println(">>> Create project here <<<");

        Point p = new Point(1,2,Color.BLUE);
        SenseHatUtil.waitFor(2);
        p.startBlinking();
    }
}
