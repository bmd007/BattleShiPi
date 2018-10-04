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

    public Screen() {

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                points[i][j] = new Point(i, j, Color.of(0, 0, 0));
            }
        }

        globeSight = points[0][0];

    }

    void changeGlobeSightLocation(Direction direction){

        int tempX = globeSight.x;
        int tempY = globeSight.y;

        globeSight.stopBlinking();

        switch (direction){
            case UP:{
                if (tempY + 1 < 8)
                    globeSight = points[tempY+1][tempX];
                break;
            }case DOWN:{
                if (tempY - 1 > -1)
                    globeSight = points[tempY-1][tempX];
                break;
            }case LEFT:{
                if (tempX + 1 < 8)
                break;
            }case RIGHT:{
                if (tempX - 1 > -1)
                    globeSight = points[tempY][tempX-1];
                break;
            }
        }

        globeSight.startBlinking();
    }



    public void showGlobeSight() {
        globeSightVisible = true;
        joystickEventThread.start();
    }

    public void vanishGlobeSight() {
        globeSightVisible = false;
    }


    private Thread joystickEventThread = new Thread(new Runnable() {
        @Override
        public void run() {

            while (globeSightVisible) {
                JoystickEvent event = SenseHatUtil.senseHat.joystick.waitForEvent();
                applyJoystickEvent(event);
            }
        }
    });

    public abstract void pointSelected(Point point);

    private void applyJoystickEvent(JoystickEvent event){
        switch (event.getAction()){
            case PRESSED: {
                if (event.getDirection().equals(Direction.MIDDLE))
                    pointSelected(globeSight);
                else changeGlobeSightLocation(event.getDirection());
                break;
            }
            case HELD: { break; }
            case RELEASED: { break; }
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
