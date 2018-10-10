package ir.tiroon.battleshipi;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Bomb {
    public int targetX;
    public int targetY;
    public boolean isSuccessful;


    @JsonCreator
    public Bomb(@JsonProperty("targetX") int targetX,
                @JsonProperty("targetY") int targetY,
                @JsonProperty
                        ("isSuccessful") boolean isSuccessful) {
        this.targetX = targetX;
        this.targetY = targetY;
        this.isSuccessful = isSuccessful;

    }
}
