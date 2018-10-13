package ir.tiroon.battleshipi;

import rpi.sensehat.api.dto.Color;

import java.util.HashSet;
import java.util.Set;

public class MapScreen extends Screen {

    public Set<Point> selectedLocations = new HashSet<>();
    public Set<Point> receivedBombs = new HashSet<>();

    public MapScreen() {
        super();
    }

    @Override
    public void pointSelected(Point point) {
        point.stopBlinkingAndLightUp(Color.of(255, 255, 0));
        //The copy should be yellow
        Point temp = new Point(point);
        selectedLocations.add(temp);

        changeGlobeSightLocationToStart();
    }

    public Bomb putABombOnMap(Bomb bomb) {

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
