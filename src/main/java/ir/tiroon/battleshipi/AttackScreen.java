package ir.tiroon.battleshipi;

import rpi.sensehat.api.dto.Color;

import java.util.ArrayList;

public class AttackScreen extends Screen {

    public volatile ArrayList<Point> selectedLocationsToSendBomb = new ArrayList<>(2);
    //Show them in red
    public volatile ArrayList<Point> successfulBombsLocations = new ArrayList<>(2);
    //Show them in green
    public volatile ArrayList<Point> waistedBombsLocations = new ArrayList<>(2);

    public AttackScreen() {
        super();
    }

    public synchronized void addSuccessfulBombPoint(Point point) {
        //Todo Should I light up this point now????
        successfulBombsLocations.add(point);
    }

    public synchronized void addWaistedBombPoint(Point point) {
        //Todo Should I light up this point now????
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
        for (int i = 0; i < selectedLocationsToSendBomb.size(); i++)
            selectedLocationsToSendBomb.get(i).lightUp();

        for (int i = 0; i < successfulBombsLocations.size(); i++)
            successfulBombsLocations.get(i).lightUp();

        for (int i = 0; i < waistedBombsLocations.size(); i++)
            waistedBombsLocations.get(i).lightUp();
    }


}