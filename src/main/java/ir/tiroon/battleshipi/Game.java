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

    public static final int numberOfBombsToSend = 3;

    public static final int numberOfOwnPropertiesToSelect = 2;

    //Todo: change everything about story of "game finished" to "phase 2 finished"
    static volatile Boolean opponentsGameFinished = false;

    public static IMqttMessageListener bombReceiveListener = (topic, message) -> {

        System.out.println("Bomb received:" + topic + "::" +message);

        Bomb receivedBomb = new ObjectMapper().readValue(message.toString(), Bomb.class);

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

    public static IMqttMessageListener gameFinishedListener = (topic, message) -> {
        System.out.println(new ObjectMapper().readValue(message.toString(), Boolean.class)+"::Game finished?");
        setOpponentsGameFinished(true);
    };

    public synchronized static Boolean getOpponentsGameFinished() {
        return opponentsGameFinished;
    }

    public synchronized static void setOpponentsGameFinished(Boolean opponentsGameFinished) {
        Game.opponentsGameFinished = opponentsGameFinished;
    }

    ///////////////////////////////////////
    public Game() throws MqttException {


        ///////////////////////////
        cleanScreen();
        phase1();

        cleanScreen();
        phase2();

        SenseHatUtil.waitFor(4000);
        cleanScreen();
        phase3();


        //Waiting for opponents game to be finished
        //The only scenario that can make this waiting necessary is the time that one of players ask for bomb check and the checker is in phase 4
//        while (!getOpponentsGameFinished()) {
//            System.out.print("*");
//            SenseHatUtil.waitFor(3000);
//        }

        MQTTUtil.sendGameFinished();

        SenseHatUtil.waitFor(10000);
        cleanScreen();
        phase4();


        SenseHatUtil.waitFor(25000);
        cleanScreen();
        phase5();

        SenseHatUtil.waitFor(25000);
        cleanScreen();
        phase3();

    }

    void phase1() {
        mapScreen = new MapScreen();

        mapScreen.showGlobeSight();
        while (mapScreen.selectedLocations.size() != numberOfOwnPropertiesToSelect) SenseHatUtil.waitFor(4000);

        mapScreen.vanishGlobeSight();
        System.out.println("Own Property Point Selection Phase Finished");
    }

    void phase2() {
        attackScreen = new AttackScreen();
        attackScreen.showGlobeSight();

        while (attackScreen.selectedLocationsToSendBomb.size() != numberOfBombsToSend) SenseHatUtil.waitFor(4000);

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