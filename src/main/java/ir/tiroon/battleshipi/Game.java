package ir.tiroon.battleshipi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttException;
import rpi.sensehat.api.dto.Color;

public class Game {

    static MapScreen mapScreen;

    static AttackScreen attackScreen;

    static volatile int score;

    public static IMqttMessageListener bombReceiveListener = (topic, message) -> {
        System.out.println("Message received from:"+topic+"::"+message.toString());

        Bomb receivedBomb = MQTTUtil.objectMapper.reader().readValue(message.toString());

        MQTTUtil.advertiseTheResultOfABomb(mapScreen.putABombOnMap(receivedBomb));
    };

    public static IMqttMessageListener bombInfoReceiveListener = (topic, message) -> {
        System.out.println("Message received from:"+topic+"::"+message.toString());

        Bomb receivedBombInfo = MQTTUtil.objectMapper.reader().readValue(message.toString());

        if (receivedBombInfo.isSuccessful) {
            attackScreen.addSuccessfulBombPoint(
                    new Point(receivedBombInfo.targetX, receivedBombInfo.targetY, Color.RED));

            score = score + 1;
        } else
            attackScreen.addWaistedBombPoint(
                    new Point(receivedBombInfo.targetX, receivedBombInfo.targetY, Color.GREEN));

    };

    public Game() throws MqttException {

        MQTTUtil.mqttClient.subscribe(Main.playerNumber == 1 ? MQTTUtil.sendBombToPlayer1Topic : MQTTUtil
                .sendBombToPlayer2Topic, bombReceiveListener);

        MQTTUtil.mqttClient.subscribe(Main.playerNumber == 1 ? MQTTUtil.sendBombInfoToPlayer1Topic : MQTTUtil
                .sendBombInfoToPlayer2Topic, bombInfoReceiveListener);

        ///////////////////////////
        cleanScreen();
        phase1();

        cleanScreen();
        phase2();

        SenseHatUtil.waitFor(4000);
        cleanScreen();
        phase3();

        SenseHatUtil.waitFor(4000);
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