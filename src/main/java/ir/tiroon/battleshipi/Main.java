package ir.tiroon.battleshipi;

import rpi.sensehat.api.SenseHat;
import rpi.sensehat.api.dto.Color;

public class Main {

    public static void main(String[] args) {
//        Screen s = new ScoreScreen();
//        s.showGlobeSight();

        Point p = new Point(5,5,Color.RED);
        p.startBlinking();
        Point p2  = p;
        SenseHatUtil.waitFor(8000);
        p2.stopBlinking();
        SenseHatUtil.waitFor(4000);
        p2.startBlinking();
        SenseHatUtil.waitFor(8000);
        Point p3 = new Point(6,7,Color.GREEN);
        p3.startBlinking();
        p2 = p3;
        p2.stopBlinking();
    }
}
