package ir.tiroon.battleshipi;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public abstract class Screen implements IMqttMessageListener {

    Borad borad;

    public Screen(){
        borad = new Borad();
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {

    }


}
