package ir.tiroon.battleshipi;

import rpi.sensehat.api.dto.Color;

import java.util.ArrayList;

public class MapScreen extends Screen {

    public volatile ArrayList<Point> selectedLocations = new ArrayList<>(3);
    public volatile ArrayList<Point> receivedBombs = new ArrayList<>(4);

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

    public Bomb putABombOnMap(Bomb bomb) {

        Point receivedBombPoint = new Point(bomb.targetX, bomb.targetY, Color.RED);

        receivedBombs.add(receivedBombPoint);

        //is it really necessary?
        bomb.isSuccessful = false;

        for (Point p : selectedLocations)
            if (receivedBombPoint.isOnTheSameLocationAs(p)) {
                bomb.isSuccessful = true;
                break;
            }


        return bomb;
    }


    @Override
    public void reShowUp() {
        for (int i = 0; i < selectedLocations.size(); i++)
            selectedLocations.get(i).lightUp();

        for (int i = 0; i < receivedBombs.size(); i++)
            receivedBombs.get(i).lightUp();

    }

}
