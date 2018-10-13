package ir.tiroon.battleshipi;

import rpi.sensehat.api.dto.Color;

import java.util.HashSet;
import java.util.Set;

public class MapScreen extends Screen {

    //may only this one should be volite
    Set<Point> selectedLocations = new HashSet<>();
    Set<Point> receivedBombs = new HashSet<>();

    public MapScreen() {
        super();
    }

    @Override
    public void pointSelected(Point point) {
        point.stopBlinkingAndLightUp(Color.of(255, 255, 0));
        //The copy should be yellow

        selectedLocations.add(new Point(point));
        System.out.println("New Point added to Map selected points>"+point.x+":"+point.y);

        changeGlobeSightLocationToStart();
    }

    public Bomb putABombOnMap(Bomb bomb) {

        Point receivedBombPoint = new Point(bomb.targetX, bomb.targetY, Color.RED);

        System.out.println("BMD::putOnMapCheck>"+selectedLocations.size());

        receivedBombs.add(receivedBombPoint);
        System.out.println("New Point added to Map received bombs>"+receivedBombPoint.x+":"+receivedBombPoint.y);

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
