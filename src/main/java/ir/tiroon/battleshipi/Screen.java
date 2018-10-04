package ir.tiroon.battleshipi;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import rpi.sensehat.api.dto.Color;

public abstract class Screen implements IMqttMessageListener {


    Point points[][] = new Point[8][8];

    public Screen(){

        for(int i=0; i<8;i++){
            for(int j=0; j<8; j++){
                points[i][j] = new Point(i,j,Color.of(0,0,0));
            }
        }


    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {

    }

//    Color[] colors = {
//            X, O, X, O, X, O, X, O,
//            O, X, O, X, O, X, O, X,
//            X, O, X, O, X, O, X, O,
//            O, X, O, X, O, X, O, X,
//            X, O, X, O, X, O, X, O,
//            O, X, O, X, O, X, O, X,
//            X, O, X, O, X, O, X, O,
//            O, X, O, X, O, X, O, X
//    };
//    waitFor(5);

}
