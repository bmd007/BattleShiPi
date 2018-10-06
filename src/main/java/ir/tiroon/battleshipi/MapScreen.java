package ir.tiroon.battleshipi;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import rpi.sensehat.api.dto.Color;

public class MapScreen extends Screen {

    public MapScreen() {
        super();
    }

    @Override
    public void pointSelected(Point point) {
        Point temp = new Point(point);
        //Yellow as location on own stuff
        temp.lightUp(Color.of(255,255,0));
        changeGlobeSightLocationToStart();
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        System.out.println("Listened From "+getClass().getName()+" to "+message.toString() );
    }
}
