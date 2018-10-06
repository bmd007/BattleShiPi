package ir.tiroon.battleshipi;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import rpi.sensehat.api.dto.Color;

public class AttackScreen extends Screen {

    public AttackScreen() {
        super();
    }

    @Override
    public void pointSelected(Point point) {
        point.stopBlinkingAndLightUp(Color.RED);

        changeGlobeSightLocationToStart();
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        System.out.println("Listened From "+getClass().getName()+" to "+message.toString() );
    }
}
