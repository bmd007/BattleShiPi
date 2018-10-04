package ir.tiroon.battleshipi;

import org.eclipse.paho.client.mqttv3.MqttMessage;

public class ScoreScreen extends Screen {

    public ScoreScreen() {
        super();
    }

    @Override
    public void pointSelected(Point point) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        System.out.println("Listened From "+getClass().getName()+" to "+message.toString() );
    }

//    ledMatrix.showLetter("V",
//            Color.of(255, 255, 0),
//            Color.of(0, 255, 255));
//    waitFor(5);

}

