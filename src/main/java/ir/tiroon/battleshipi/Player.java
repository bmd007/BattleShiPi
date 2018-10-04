package ir.tiroon.battleshipi;

import java.util.ArrayList;

public class Player {
    public int number;
    public boolean isLocalOrRemote = true;
    public int score;
    public ArrayList<Point> locatedPoints = new ArrayList<Point>(5);
    public ArrayList<Bomb> sentBombs = new ArrayList<>(6);
}
