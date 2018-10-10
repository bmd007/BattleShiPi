package ir.tiroon.battleshipi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.Random;

public class MQTTUtil {

    //This class seems to need refurbishment
    public static String bombard_topic = "bombard_topic";
    public static String bomb_result_topic = "bomb_result_topic";
    public static ObjectMapper objectMapper = new ObjectMapper();
    static int qos = 2;
    static MemoryPersistence persistence = new MemoryPersistence();
    public static MqttClient mqttClient;

    public static void MQTTUtilConnect(String brokerIPAddress) throws Exception {
        System.out.println("tcp://" + brokerIPAddress + ":1883");
        mqttClient = new MqttClient("tcp://" + brokerIPAddress + ":1883", new Random().nextLong() + ":", persistence);

        MqttConnectOptions connOpts = new MqttConnectOptions();
        mqttClient.connect(connOpts);
    }

    public static void unSubscribeATopic(String topicName) throws Exception {
        mqttClient.unsubscribe(topicName);
    }

    public static void sendBomb(Bomb bomb) {
        if (!mqttClient.isConnected()) {
            try {
                mqttClient.reconnect();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

        try {
            String bombJson = objectMapper.writer().writeValueAsString(bomb);

            MqttMessage message = new MqttMessage(bombJson.getBytes());
            message.setQos(qos);
            mqttClient.publish(bombard_topic, message);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (MqttPersistenceException e) {
            e.printStackTrace();
        } catch (MqttException e) {
            e.printStackTrace();
        }

        //Todo send bomb sound

    }

    public static void advertiseTheResultOfABomb(Bomb bomb) {
        if (!mqttClient.isConnected()) {
            try {
                mqttClient.reconnect();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

        try {
            String bombToTellAboutJson = objectMapper.writer().writeValueAsString(bomb);

            MqttMessage message = new MqttMessage(bombToTellAboutJson.getBytes());
            message.setQos(qos);
            mqttClient.publish(bomb_result_topic, message);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (MqttPersistenceException e) {
            e.printStackTrace();
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }
}
