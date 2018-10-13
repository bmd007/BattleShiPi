package ir.tiroon.battleshipi;

import rpi.sensehat.api.dto.Color;

import java.util.HashSet;
import java.util.Set;

public class AttackScreen extends Screen {

    public Set<Point> selectedLocationsToSendBomb = new HashSet<>();
    //Show them in red
    public Set<Point> successfulBombsLocations = new HashSet<>();
    //Show them in green
    public Set<Point> waistedBombsLocations = new HashSet<>();

    public AttackScreen() {
        super();
    }

    public void addSuccessfulBombPoint(Point point) {
        successfulBombsLocations.add(point);
    }

    public void addWaistedBombPoint(Point point) {
        waistedBombsLocations.add(point);
    }

    @Override
    public void pointSelected(Point point) {

        point.stopBlinkingAndLightUp(Color.RED);

        Point bombLocation = new Point(point);

        Bomb bomb = new Bomb(bombLocation.x, bombLocation.y, false);

        MQTTUtil.sendBomb(bomb);

        selectedLocationsToSendBomb.add(bombLocation);

        changeGlobeSightLocationToStart();
    }


    @Override
    public void reShowUp() {
        selectedLocationsToSendBomb.forEach(Point::lightUp);

        SenseHatUtil.waitFor(2000);

        successfulBombsLocations.forEach(Point::lightUp);

        SenseHatUtil.waitFor(2000);

        waistedBombsLocations.forEach(Point::lightUp);
    }
}