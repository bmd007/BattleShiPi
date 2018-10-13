package ir.tiroon.battleshipi;

import org.eclipse.paho.client.mqttv3.MqttException;

public class Main {

    public static int playerNumber;

    public static void main(String[] args) throws MqttException {

        String brokerIP  = args[0];
        playerNumber = Integer.valueOf(args[1]);

        try {
            MQTTUtil.MQTTUtilConnect(args[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        while (!MQTTUtil.mqttClient.isConnected()) { }

        System.out.println((Main.playerNumber == 1)+"::"+(Main.playerNumber == 1 ? MQTTUtil.sendBombToPlayer1Topic : MQTTUtil
                .sendBombToPlayer2Topic));

        System.out.println(Main.playerNumber == 1 ? MQTTUtil.sendBombToPlayer2Topic : MQTTUtil.sendBombInfoToPlayer1Topic);

        Game game = new Game();
    }
}
