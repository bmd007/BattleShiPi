package ir.tiroon.battleshipi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttException;
import rpi.sensehat.api.dto.Color;

public class Game {

    public static final int numberOfBombsToSend = 3;
    public static final int numberOfOwnPropertiesToSelect = 2;
    static MapScreen mapScreen;
    static AttackScreen attackScreen;
    static volatile int score;
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
    //Todo: change everything about story of "game finished" to "phase 2 finished"
    static volatile Boolean opponentsGameFinished = false;
    public static IMqttMessageListener gameFinishedListener = (topic, message) -> {
        System.out.println(new ObjectMapper().readValue(message.toString(), Boolean.class) + "::Game finished?");
        setOpponentsGameFinished(true);
    };
    public static IMqttMessageListener bombReceiveListener = (topic, message) -> {

        System.out.println("Bomb received:" + topic + "::" + message);

        Bomb receivedBomb = new ObjectMapper().readValue(message.toString(), Bomb.class);

        MQTTUtil.advertiseTheResultOfABomb(mapScreen.putABombOnMap(receivedBomb));

    };

    ///////////////////////////////////////
    public Game() throws MqttException {

        //Todo start background music
        ///////////////////////////
        cleanScreen();
        phase1();

        //Todo do not allow any one send bomb before phase one finished for both.
        //      So if one of players is still choosing yellow points, the other player is not allowed to send bomb
        //          There are some different way to make it possible. iT seems that The easiest way is changing names in the GameFinished procedure
        //              and wait for other players phase 1 end before starting the current player phase2. Also  it seems that this method is not working,
        //                  at least didn't worked last time  as GameFinished procedure ( deadlock happens probably ;( ?)
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

        //MQTTUtil.sendGameFinished();


        //Todo show text about phase 4 screen
        SenseHatUtil.waitFor(10000);
        cleanScreen();
        phase4();

        //Todo show text about phase 5 screen
        SenseHatUtil.waitFor(25000);
        cleanScreen();
        phase5();

        SenseHatUtil.waitFor(25000);
        cleanScreen();
        phase3();

        //Todo finish background music

    }

    public synchronized static Boolean getOpponentsGameFinished() {
        return opponentsGameFinished;
    }

    public synchronized static void setOpponentsGameFinished(Boolean opponentsGameFinished) {
        Game.opponentsGameFinished = opponentsGameFinished;
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