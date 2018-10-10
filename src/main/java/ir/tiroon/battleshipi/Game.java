package ir.tiroon.battleshipi;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import rpi.sensehat.api.dto.Color;


public class Game implements IMqttMessageListener {

    MapScreen mapScreen;

    AttackScreen attackScreen;

    static volatile int score;

    public Game() throws MqttException {

        MQTTUtil.mqttClient.subscribe(MQTTUtil.bombard_topic,this);
        MQTTUtil.mqttClient.subscribe(MQTTUtil.bomb_result_topic,this);


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
        while (mapScreen.selectedLocations.size() != 2) SenseHatUtil.waitFor(4000);

        mapScreen.vanishGlobeSight();
        System.out.println("Own Property Point Selection Phase Finished");
    }

    void phase2() {
        attackScreen = new AttackScreen();
        attackScreen.showGlobeSight();

        while (attackScreen.selectedLocationsToSendBomb.size() != 2) SenseHatUtil.waitFor(4000);

        attackScreen.vanishGlobeSight();
        System.out.println("Bomb to Send Point Selection Phase Finished");
    }

    void phase3() {
        System.out.println("Score is"+score);
        SenseHatUtil.senseHat.ledMatrix.showMessage(""+score, (float) 0.4,Color.RED,Color.BLUE);
    }

    void phase4() {
        mapScreen.reShowUp();
    }

    void phase5() {
        attackScreen.reShowUp();
    }


    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println("Listened From " + topic + " to " + message.toString());

        //ToDO
        //receive a bomb, check if it was good or bad, publish the result
        Bomb receivedBomb = (Bomb) MQTTUtil.objectMapper.reader().readValue(message.getPayload());

        if (topic.equals(MQTTUtil.bomb_result_topic))
            if (receivedBomb.isSuccessful) {
                attackScreen.addSuccessfulBombPoint(
                        new Point(receivedBomb.targetX, receivedBomb.targetY, Color.RED));

                score++;
            } else
                attackScreen.addWaistedBombPoint(
                        new Point(receivedBomb.targetX, receivedBomb.targetY, Color.GREEN));


        else if (topic.equals(MQTTUtil.bombard_topic))
            MQTTUtil.advertiseTheResultOfABomb(mapScreen.putABombOnMap(receivedBomb));



    }

    void cleanScreen() {
        SenseHatUtil.senseHat.ledMatrix.clear();
    }
}