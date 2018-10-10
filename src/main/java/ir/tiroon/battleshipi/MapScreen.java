package ir.tiroon.battleshipi;

import rpi.sensehat.api.dto.Color;

import java.util.HashSet;
import java.util.Set;

public class MapScreen extends Screen {

    volatile Set<Point> selectedLocations = new HashSet<>();
    volatile Set<Point> receivedBombs = new HashSet<>();

    public MapScreen() {
        super();
    }

    @Override
    public synchronized void pointSelected(Point point) {
        point.stopBlinkingAndLightUp(Color.of(255, 255, 0));
        //The copy should be yellow
        selectedLocations.add(new Point(point));

        changeGlobeSightLocationToStart();
    }

    public synchronized Bomb putABombOnMap(Bomb bomb) {

        Point receivedBombPoint = new Point(bomb.targetX, bomb.targetY, Color.RED);

        receivedBombs.add(receivedBombPoint);

        bomb.isSuccessful = false;

        selectedLocations.forEach(point -> {
            if (receivedBombPoint.isOnTheSameLocationAs(point))
                bomb.isSuccessful = true;
        });


        return bomb;
    }


    @Override
    public void reShowUp() {
        selectedLocations.forEach(Point::lightUp);

        receivedBombs.forEach(Point::lightUp);

    }

}
