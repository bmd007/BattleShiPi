package ir.tiroon.battleshipi;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.Random;

public class MQTTUtil2 {

    String topic = "areaBrain_sumProvider_Topic";
    int qos = 2;
    MemoryPersistence persistence = new MemoryPersistence();

    public String MQTTBrokerURL;

    MqttClient mqttClient;

    public MQTTUtil2() throws Exception {

        mqttClient = new MqttClient("tcp://"+MQTTBrokerURL+":1883",new Random().nextLong()+":", persistence);

        MqttConnectOptions connOpts = new MqttConnectOptions();
        mqttClient.connect(connOpts);

        sendToTopic(topic, "get up homes get up");

    }

    public void subscribeATopic(String topicName) throws Exception {
        mqttClient.subscribe(topicName, qos, (s, mqttMessage) -> {
            System.out.println("BMD::Message:" + s + ":::" + mqttMessage.toString());
        });
    }

    public void unSubscribeATopic(String topicName) throws Exception {
        mqttClient.unsubscribe(topicName);
    }

    public void sendToTopic(String topicName, String data) throws Exception {
        MqttMessage message = new MqttMessage(data.getBytes());
        message.setQos(qos);
        mqttClient.publish(topicName, message);
    }

    IMqttMessageListener mqttMessageListener = (topicName, mqttMessage) -> {

    };

}
