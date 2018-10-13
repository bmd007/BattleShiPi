package ir.tiroon.battleshipi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import rpi.sensehat.api.dto.Color;

public class Game {

    static MapScreen mapScreen;

    static AttackScreen attackScreen;

    static volatile int score;


    //Todo: change everything about story of "game finished" to "phase 2 finished"
    static volatile Boolean opponentsGameFinished = false;

    public static IMqttMessageListener bombReceiveListener = (topic, message) -> {

        System.out.println("BMD:" + message.toString() + "::" + message + "::" + new String(message.getPayload()));

        Bomb receivedBomb = new ObjectMapper().readValue(message.toString(), Bomb.class);

        System.out.println("Bomb info advertising game class" + receivedBomb.isSuccessful);

        MQTTUtil.advertiseTheResultOfABomb(mapScreen.putABombOnMap(receivedBomb));

    };

    public static IMqttMessageListener bombInfoReceiveListener = (topic, message) -> {

        System.out.println("Bomb info received from:" + topic + "::" + message.toString());

        Bomb receivedBombInfo = new ObjectMapper().readValue(message.toString(), Bomb.class);

        if (receivedBombInfo.isSuccessful) {
            attackScreen.addSuccessfulBombPoint(
                    new Point(receivedBombInfo.targetX, receivedBombInfo.targetY, Color.RED));

            score = score + 1;
        } else
            attackScreen.addWaistedBombPoint(
                    new Point(receivedBombInfo.targetX, receivedBombInfo.targetY, Color.GREEN));

    };

    public static IMqttMessageListener gameFinishedListener = (topic, message) ->
            opponentsGameFinished = true;
                    //new ObjectMapper().readValue(message.toString(), Boolean.class);

    public Game() throws MqttException {

        MQTTUtil.mqttClient.subscribe(Main.playerNumber == 1 ? MQTTUtil.sendBombInfoToPlayer1Topic : MQTTUtil
                .sendBombInfoToPlayer2Topic, bombInfoReceiveListener);

        MQTTUtil.mqttClient.subscribe(Main.playerNumber == 1 ? MQTTUtil.sendBombToPlayer1Topic : MQTTUtil
                .sendBombToPlayer2Topic, bombReceiveListener);

        MQTTUtil.mqttClient.subscribe(Main.playerNumber == 1 ? MQTTUtil.advertisePlayer2GameFinishedToPlayer1Topic : MQTTUtil.advertisePlayer1GameFinishedToPlayer2Topic);

        ///////////////////////////
        cleanScreen();
        phase1();

        cleanScreen();
        phase2();


        MQTTUtil.sendGameFinished(opponentsGameFinished);

        //Waiting for opponents game to be finished
        while (!opponentsGameFinished) {
            SenseHatUtil.waitFor(3000);
            //Todo Maybe showing some message here or nice sound about waiting for others
        }
        SenseHatUtil.waitFor(4000);
        cleanScreen();
        phase3();

        SenseHatUtil.waitFor(3000);
        cleanScreen();
        phase4();


        SenseHatUtil.waitFor(8000);
        cleanScreen();
        phase5();

        SenseHatUtil.waitFor(8000);
        cleanScreen();
        phase3();

    }

    void phase1() {
        mapScreen = new MapScreen();

        mapScreen.showGlobeSight();
        while (mapScreen.selectedLocations.size() != 1) SenseHatUtil.waitFor(4000);

        mapScreen.vanishGlobeSight();
        System.out.println("Own Property Point Selection Phase Finished");
    }

    void phase2() {
        attackScreen = new AttackScreen();
        attackScreen.showGlobeSight();

        while (attackScreen.selectedLocationsToSendBomb.size() != 1) SenseHatUtil.waitFor(4000);

        attackScreen.vanishGlobeSight();
        System.out.println("Bomb to Send Point Selection Phase Finished");
    }

    void phase3() {
        System.out.println("Score is" + score);
        SenseHatUtil.senseHat.ledMatrix.showMessage("" + score, (float) 0.5, Color.RED, Color.BLUE);
    }

    void phase4() {
        mapScreen.reShowUp();
    }

    void phase5() {
        attackScreen.reShowUp();
    }


    void cleanScreen() {
        SenseHatUtil.senseHat.ledMatrix.clear();
    }
}