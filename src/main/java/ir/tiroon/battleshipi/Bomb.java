package ir.tiroon.battleshipi;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Bomb {
    public Point target;


    @JsonCreator
    public Bomb(@JsonProperty("key") Point target) {
        this.target = target;
    }
}
