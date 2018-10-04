package ir.tiroon.battleshipi;

import rpi.sensehat.api.SenseHat;
import rpi.sensehat.api.dto.Color;
import rpi.sensehat.api.dto.JoystickEvent;
import rpi.sensehat.api.dto.joystick.Direction;

public class Main {

    public static void main(String[] args) {
        SenseHatUtil.senseHat.ledMatrix.clear();
        Screen s = new ScoreScreen();
        s.showGlobeSight();


    }
}
