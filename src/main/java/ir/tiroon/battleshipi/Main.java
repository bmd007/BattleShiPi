package ir.tiroon.battleshipi;

import rpi.sensehat.api.SenseHat;
import rpi.sensehat.api.dto.Color;

public class Main {

    public static void main(String[] args) {
        Screen s = new ScoreScreen();
        s.showGlobeSight();
    }
}
