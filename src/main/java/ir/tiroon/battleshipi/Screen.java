package ir.tiroon.battleshipi;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import rpi.sensehat.api.dto.Color;
import rpi.sensehat.api.dto.JoystickEvent;
import rpi.sensehat.api.dto.joystick.Direction;

public abstract class Screen implements IMqttMessageListener {

    Point points[][] = new Point[8][8];

    Point globeSight = null;

    boolean globeSightVisible = false;

    Color colorOfTheLastPointTurnedIntoGlobeSight = Color.GREEN;

    public Screen() {

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                points[i][j] = new Point(i, j, Color.of(5, 5, 5));
            }
        }

    }

    void changeGlobeSightLocation(Direction direction) {

        Point tempPoint = new Point(globeSight);
        int tempX = tempPoint.x;
        int tempY = tempPoint.y;

        tempPoint.stopBlinking();

        tempPoint.lightUp(colorOfTheLastPointTurnedIntoGlobeSight);

        switch (direction) {
            case DOWN: {

                if (tempY - 1 > -1) {
                    System.out.println("X,Y Down" + points[tempX][tempY - 1].x + ":" + points[tempX][tempY - 1].y);
                    globeSight = points[tempX][tempY - 1];
                }
                break;
            }
            case UP: {
                if (tempY + 1 < 8) {
                    System.out.println("X,Y UP" + points[tempX][tempY + 1].x + ":" + points[tempX][tempY + 1].y);
                    globeSight = points[tempX][tempY + 1];
                }
                break;
            }
            //todo check inner ifs
            case RIGHT: {
                if (tempX + 1 < 8) {
                    System.out.println("X,Y Right" + points[tempX + 1][tempY].x + ":" + points[tempX + 1][tempY].y);
                    globeSight = points[tempX + 1][tempY];
                }
                break;
            }
            case LEFT: {
                if (tempX - 1 > -1) {
                    System.out.println("X,Y left" + points[tempX - 1][tempY].x + ":" + points[tempX - 1][tempY].y);
                    globeSight = points[tempX - 1][tempY];
                }
                break;
            }
        }

        colorOfTheLastPointTurnedIntoGlobeSight = globeSight.color;
        globeSight.startBlinking();

    }


    public void showGlobeSight() {
        colorOfTheLastPointTurnedIntoGlobeSight = points[5][5].color;
        globeSight = points[5][5];
        points[5][5].lightUp(Color.RED);
        globeSightVisible = true;
        globeSight.startBlinking();
        new Thread(joystickEventRunnable, "joystickEventThread").start();
    }

    public void vanishGlobeSight() {
        globeSightVisible = false;
    }


    private Runnable joystickEventRunnable = () -> {

        while (globeSightVisible)
            applyJoystickEvent(SenseHatUtil.senseHat.joystick.waitForEvent());


        System.out.println("joyStick thread finished");

    };

    public abstract void pointSelected(Point point);

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

    @Override
    public abstract void messageArrived(String topic, MqttMessage message);

//    Color[] colors = {
//            X, O, X, O, X, O, X, O,
//            O, X, O, X, O, X, O, X,
//            X, O, X, O, X, O, X, O,
//            O, X, O, X, O, X, O, X,
//            X, O, X, O, X, O, X, O,
//            O, X, O, X, O, X, O, X,
//            X, O, X, O, X, O, X, O,
//            O, X, O, X, O, X, O, X
//    };
//    waitFor(5);

}
