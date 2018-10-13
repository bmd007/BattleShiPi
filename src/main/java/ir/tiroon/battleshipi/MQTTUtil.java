package ir.tiroon.battleshipi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Random;

public class MQTTUtil {

    //This class seems to need refurbishment

    public static String sendBombToPlayer1Topic = "sendBombToPlayer1Topic";
    public static String sendBombToPlayer2Topic = "sendBombToPlayer2Topic";
    public static String sendBombInfoToPlayer1Topic = "sendBombInfoToPlayer1Topic";
    public static String sendBombInfoToPlayer2Topic = "sendBombInfoToPlayer2Topic";
    public static String advertisePlayer1GameFinishedToPlayer2Topic = "advertisePlayer1GameFinished";
    public static String advertisePlayer2GameFinishedToPlayer1Topic = "advertisePlayer2GameFinished";

    public static ObjectMapper objectMapper = new ObjectMapper();
    static int qos = 1;
    static MemoryPersistence persistence = new MemoryPersistence();
    public static MqttClient mqttClient;
    static String brokerIPAddress;

    public static void MQTTUtilConnect(String brokerIPAddress) throws Exception {
        MQTTUtil.brokerIPAddress = brokerIPAddress;

        mqttClient = new MqttClient("tcp://" + brokerIPAddress + ":1883", new Random().nextLong() + ":", persistence);

        connect();
    }


    public static void unSubscribeATopic(String topicName) throws Exception {
        mqttClient.unsubscribe(topicName);
    }

    public static void connect() {
        try {

            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setAutomaticReconnect(true);
            mqttClient.connect(connOpts);

            MQTTUtil.mqttClient.subscribe(Main.playerNumber == 1 ? MQTTUtil.sendBombInfoToPlayer1Topic : MQTTUtil
                    .sendBombInfoToPlayer2Topic, Game.bombInfoReceiveListener);

            MQTTUtil.mqttClient.subscribe(Main.playerNumber == 1 ? MQTTUtil.sendBombToPlayer1Topic : MQTTUtil
                    .sendBombToPlayer2Topic, Game.bombReceiveListener);

            MQTTUtil.mqttClient.subscribe(Main.playerNumber == 1 ? MQTTUtil.advertisePlayer2GameFinishedToPlayer1Topic : MQTTUtil.advertisePlayer1GameFinishedToPlayer2Topic,
                    Game.gameFinishedListener);


        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public static void sendGameFinished(){

        try {
            MQTTUtil.mqttClient.publish(Main.playerNumber == 1 ? MQTTUtil.advertisePlayer1GameFinishedToPlayer2Topic : MQTTUtil.advertisePlayer2GameFinishedToPlayer1Topic
                    , new MqttMessage(("Game finished on "+Main.playerNumber+"'s side finished").getBytes(Charset.forName("UTF-8"))));
        } catch (MqttPersistenceException e) {
            e.printStackTrace();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public static void sendBomb(Bomb bomb) {
        try {
            if (!mqttClient.isConnected()) connect();


            String bombJson = objectMapper.writer().writeValueAsString(bomb);

            MqttMessage message = new MqttMessage(bombJson.getBytes(Charset.forName("UTF-8")));
            message.setQos(qos);
            mqttClient.publish(Main.playerNumber == 1 ? MQTTUtil.sendBombToPlayer2Topic : MQTTUtil.sendBombToPlayer1Topic, message);

            System.out.println("Bomb send to:"+ (Main.playerNumber == 1 ? MQTTUtil.sendBombToPlayer2Topic : MQTTUtil.sendBombInfoToPlayer1Topic) +"::"+ message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (MqttPersistenceException e) {
            e.printStackTrace();
        } catch (MqttException e) {
            e.printStackTrace();
        }

        //Todo send bomb sound

    }

    public static void advertiseTheResultOfABomb(Bomb bombToInformAbout) {
        try {
            if (!mqttClient.isConnected()) connect();

            String bombToTellAboutJson = objectMapper.writeValueAsString(bombToInformAbout);

            MqttMessage message = new MqttMessage(bombToTellAboutJson.getBytes(Charset.forName("UTF-8")));
            message.setQos(qos);
            mqttClient.publish(Main.playerNumber == 1 ? MQTTUtil.sendBombInfoToPlayer2Topic : MQTTUtil.sendBombInfoToPlayer1Topic,
                    message);

            System.out.println("I am advertised");


        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (MqttPersistenceException e) {
            e.printStackTrace();
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }
}
