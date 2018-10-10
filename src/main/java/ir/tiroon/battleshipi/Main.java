package ir.tiroon.battleshipi;

public class Main {

    public static void main(String[] args) {

        System.out.println("args:"+args[0]);
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
