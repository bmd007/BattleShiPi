package ir.tiroon.battleshipi;

import rpi.sensehat.api.SenseHat;
import rpi.sensehat.api.dto.Color;
import rpi.sensehat.api.dto.JoystickEvent;
import rpi.sensehat.api.dto.joystick.Direction;

public class Main {

    public static void main(String[] args) {
//        Screen s = new ScoreScreen();
//        s.showGlobeSight();
        SenseHatUtil.senseHat.ledMatrix.clear();

        Point p1 = new Point(5,5,Color.RED);
        Point p2 = new Point(5,6,Color.RED);
        Point p3 = new Point(5,7,Color.RED);
        Point p4 = new Point(0,0,Color.of(0,0,0));
        p1.lightUp();
        p2.lightUp();
        p3.lightUp();

        SenseHatUtil.waitFor(2000);
        Point gs  =  p1;
        gs.startBlinking();

        JoystickEvent event = SenseHatUtil.senseHat.joystick.waitForEvent();
            System.out.println("dir:"+event.getDirection());
            gs.stopBlinking();
            gs.lightUp(Color.RED);
            gs = p2;
            gs.startBlinking();
        SenseHatUtil.waitFor(8000);


    }
}
