package ir.tiroon.battleshipi;

import rpi.sensehat.api.dto.Color;
import rpi.sensehat.api.dto.JoystickEvent;
import rpi.sensehat.api.dto.joystick.Direction;

public abstract class Screen implements Runnable {

    Point points[][] = new Point[8][8];

    Point globeSight = null;

    volatile boolean globeSightVisible = false;

    public Screen() {

        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                points[i][j] = new Point(i, j, Color.of(0, 0, 0));
    }


    public void showGlobeSight() {
        globeSight = points[0][0];
        globeSight.startBlinking();
        globeSightVisible = true;
        new Thread(this, "joystickEventThread").start();
    }

    public void vanishGlobeSight() {
        globeSightVisible = false;
        globeSight.stopBlinking();
    }

    void changeGlobeSightLocation(Direction direction) {

        Point tempPoint = new Point(globeSight);
        int tempX = tempPoint.x;
        int tempY = tempPoint.y;

        tempPoint.stopBlinking();

        switch (direction) {
            case UP: {

                if (tempY - 1 > -1)
                    //What if I write  like = new Point(tempX,tempX-1,temp.privousColor);
                    globeSight = points[tempX][tempY - 1];
                break;
            }
            case DOWN: {
                if (tempY + 1 < 8)
                    globeSight = points[tempX][tempY + 1];
                break;
            }
            case RIGHT: {
                if (tempX + 1 < 8)
                    globeSight = points[tempX + 1][tempY];
                break;
            }
            case LEFT: {
                if (tempX - 1 > -1)
                    globeSight = points[tempX - 1][tempY];
                break;
            }
        }

        globeSight.startBlinking();

    }


    void changeGlobeSightLocationToStart() {
        globeSight.stopBlinking();

        globeSight = points[0][0];

        globeSight.startBlinking();
    }


    @Override
    public void run() {
        while (globeSightVisible)
            applyJoystickEvent(SenseHatUtil.senseHat.joystick.waitForEvent(true));

        System.out.println("joyStick thread finished");

    }

    private void applyJoystickEvent(JoystickEvent event) {
        switch (event.getAction()) {
            case PRESSED: {
                if (event.getDirection().equals(Direction.MIDDLE))
                    pointSelected(globeSight);
                else changeGlobeSightLocation(event.getDirection());
                break;
            }
            case HELD: {
                break;
            }
            case RELEASED: {
                break;
            }
        }
        //Todo: is it necessary
        SenseHatUtil.waitFor(10);
    }


    public abstract void pointSelected(Point point);

    public abstract void reShowUp();
}
