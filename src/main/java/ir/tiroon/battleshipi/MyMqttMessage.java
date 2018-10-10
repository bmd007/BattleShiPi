package ir.tiroon.battleshipi;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MyMqttMessage {
    public Bomb bomb;
    public boolean toSendOrToInformAbout = true;

    @JsonCreator
    public MyMqttMessage(@JsonProperty("bomb") Bomb bomb,@JsonProperty("toSendOrToInformAbout") boolean
            toSendOrToInformAbout) {
        this.bomb = bomb;
        this.toSendOrToInformAbout = toSendOrToInformAbout;
    }
}
