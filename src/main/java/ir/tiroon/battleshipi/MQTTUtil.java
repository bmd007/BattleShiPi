package ir.tiroon.battleshipi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.Random;

public class MQTTUtil {

    //This class seems to need refurbishment
    public static String topic = "BattleShiPi";
    public static ObjectMapper objectMapper = new ObjectMapper();
    static int qos = 2;
    static MemoryPersistence persistence = new MemoryPersistence();
    public static MqttClient mqttClient;
    static String brokerIPAddress;

    public static void MQTTUtilConnect(String brokerIPAddress) throws Exception {
        MQTTUtil.brokerIPAddress = brokerIPAddress;
        mqttClient = new MqttClient("tcp://" + brokerIPAddress + ":1883", new Random().nextLong() + ":", persistence);

        MqttConnectOptions connOpts = new MqttConnectOptions();
        mqttClient.connect(connOpts);
    }

    public static void unSubscribeATopic(String topicName) throws Exception {
        mqttClient.unsubscribe(topicName);
    }

    public static void sendBomb(Bomb bomb) {
        try {
            if (!mqttClient.isConnected())
                mqttClient.connect();

            String bombMessageJson = objectMapper.writer().
                    writeValueAsString(new MyMqttMessage(bomb, true));

            MqttMessage message = new MqttMessage(bombMessageJson.getBytes());
            message.setQos(qos);
            mqttClient.publish(topic, message);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (MqttPersistenceException e) {
            e.printStackTrace();
        } catch (MqttException e) {
            e.printStackTrace();
        }

        //Todo send bomb sound

    }

    public static void advertiseTheResultOfABomb(MyMqttMessage bombToInformAboutMessage) {
        try {
            if (!mqttClient.isConnected())
                mqttClient.connect();

            System.out.println("Advertising result of bomb:"+
                objectMapper.writer().writeValueAsString(bombToInformAboutMessage));

            String bombToTellAboutJson = objectMapper.writer().writeValueAsString(bombToInformAboutMessage);

            MqttMessage message = new MqttMessage(bombToTellAboutJson.getBytes());
            message.setQos(qos);
            mqttClient.publish(topic, message);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (MqttPersistenceException e) {
            e.printStackTrace();
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }
}
