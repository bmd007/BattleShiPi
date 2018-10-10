package ir.tiroon.battleshipi;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import rpi.sensehat.api.dto.Color;


public class Game{

    MapScreen mapScreen;

    AttackScreen attackScreen;

    static volatile int score;

    public Game() throws MqttException {


        MQTTUtil.mqttClient.subscribe(MQTTUtil.topic, (topic, message) -> {
            System.out.println("subscriber From " + topic + " to " + message.toString());

            MyMqttMessage receivedMessage = (MyMqttMessage) MQTTUtil.objectMapper.reader().readValue(message.getPayload());

            if (!receivedMessage.toSendOrToInformAbout)
                if (receivedMessage.bomb.isSuccessful) {
                    attackScreen.addSuccessfulBombPoint(
                            new Point(receivedMessage.bomb.targetX, receivedMessage.bomb.targetY, Color.RED));

                    score++;
                } else
                    attackScreen.addWaistedBombPoint(
                            new Point(receivedMessage.bomb.targetX, receivedMessage.bomb.targetY, Color.GREEN));


            else MQTTUtil.advertiseTheResultOfABomb(
                    new MyMqttMessage(
                            mapScreen.putABombOnMap(receivedMessage.bomb),
                    false));

        });



        cleanScreen();
        phase1();

        cleanScreen();
        phase2();

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