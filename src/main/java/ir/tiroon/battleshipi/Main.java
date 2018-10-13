package ir.tiroon.battleshipi;

import org.eclipse.paho.client.mqttv3.MqttException;

public class Main {

    public static int playerNumber;

    //Todo later removed system.out.println ....
    //Todo  later replace yellow  with green
    public static void main(String[] args) throws MqttException {

        String brokerIP = args[0];
        playerNumber = Integer.valueOf(args[1]);

        try {
            MQTTUtil.MQTTUtilConnect(args[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        while (!MQTTUtil.mqttClient.isConnected()) {
        }

        new Game();
    }
}
