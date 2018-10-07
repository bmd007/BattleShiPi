package ir.tiroon.battleshipi;

public class Main {

    public static void main(String[] args) {

        try {
            MQTTUtil.MQTTUtilConnect(args[2]);
        } catch (Exception e) {
            e.printStackTrace();
        }


        while (!MQTTUtil.mqttClient.isConnected()) { }
        Game game = new Game();

    }
}
