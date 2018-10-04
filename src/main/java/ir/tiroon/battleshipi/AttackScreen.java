package ir.tiroon.battleshipi;

import org.eclipse.paho.client.mqttv3.MqttMessage;

public class AttackScreen extends Screen {

    public AttackScreen() {
        super();
    }

    @Override
    public void pointSelected(Point point) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        System.out.println("Listened From "+getClass().getName()+" to "+message.toString() );
    }
}
