package ir.tiroon.battleshipi;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import rpi.sensehat.api.dto.JoystickEvent;
import rpi.sensehat.api.dto.joystick.Direction;


public class Game implements IMqttMessageListener {

    MapScreen mapScreen = new MapScreen();

    AttackScreen attackScreen = new AttackScreen();

    ScoreScreen scoreScreen = new ScoreScreen();


    public Game() {

    }

    public void initializeGame() {

    }


    private Thread joystickEventThread = new Thread(new Runnable() {
        @Override
        public void run() {
            JoystickEvent event = SenseHatUtil.senseHat.joystick.waitForEvent();
            applyJoystickEvent(event);
        }
    });

    private void applyJoystickEvent(JoystickEvent event) {
        switch (event.getAction()) {
            case PRESSED: { break; }
            case HELD: {
                if (event.getDirection().equals(Direction.MIDDLE)) {
                    //#Todo
                }
                break;
            }
            case RELEASED: { break; }
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println("Listened From "+getClass().getName()+" to "+message.toString() );
    }
}