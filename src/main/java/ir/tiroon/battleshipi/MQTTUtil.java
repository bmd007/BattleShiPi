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
    static int qos = 2;
    MemoryPersistence persistence = new MemoryPersistence();

    public String MQTTBrokerURL;

    static MqttClient mqttClient;

    public MQTTUtil() throws Exception {

        mqttClient = new MqttClient("tcp://" + MQTTBrokerURL + ":1883", new Random().nextLong() + ":", persistence);

        MqttConnectOptions connOpts = new MqttConnectOptions();
        mqttClient.connect(connOpts);
        mqttClient.subscribe(bombard_topic);
        mqttClient.subscribe(bomb_result_topic);
    }

    public static void unSubscribeATopic(String topicName) throws Exception {
        mqttClient.unsubscribe(topicName);
    }

    public static ObjectMapper objectMapper = new ObjectMapper();

    public static void sendBomb(Bomb bomb) {
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

}
