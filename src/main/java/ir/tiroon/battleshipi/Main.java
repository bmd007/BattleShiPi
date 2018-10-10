package ir.tiroon.battleshipi;

import org.eclipse.paho.client.mqttv3.MqttException;

public class Main {

    public static void main(String[] args) throws MqttException {

        try {
            MQTTUtil.MQTTUtilConnect(args[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }


        while (!MQTTUtil.mqttClient.isConnected()) { }
        System.out.println("MQTT connection is made");
        Game game = new Game();

    }
}
