package ir.tiroon.battleshipi;

import rpi.sensehat.api.SenseHat;

public class Main {

    public static void main(String[] args) {
        System.out.println(">>> Create project here <<<");

        SenseHat senseHat = new SenseHat();

        float humidity = senseHat.environmentalSensor.getHumidity();
        System.out.println("Current humidity: " + humidity);

        senseHat.ledMatrix.showMessage("my project");
        senseHat.ledMatrix.waitFor(5);
        senseHat.ledMatrix.clear();
    }
}
