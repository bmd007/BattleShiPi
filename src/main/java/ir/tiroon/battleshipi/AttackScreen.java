package ir.tiroon.battleshipi;

import rpi.sensehat.api.dto.Color;

import java.util.HashSet;
import java.util.Set;

public class AttackScreen extends Screen {

    public volatile Set<Point> selectedLocationsToSendBomb = new HashSet<>();
    //Show them in red
    public volatile Set<Point> successfulBombsLocations = new HashSet<>();
    //Show them in green
    public volatile Set<Point> waistedBombsLocations = new HashSet<>();

    public AttackScreen() {
        super();
    }

    public synchronized void addSuccessfulBombPoint(Point point) {
        successfulBombsLocations.add(point);
    }

    public synchronized void addWaistedBombPoint(Point point) {
        waistedBombsLocations.add(point);
    }

    @Override
    public synchronized void pointSelected(Point point) {
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

        successfulBombsLocations.forEach(Point::lightUp);

        waistedBombsLocations.forEach(Point::lightUp);
    }
}