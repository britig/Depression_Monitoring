package com.example.briti.ecslab;

/**
 * Created by Briti on 22-Jan-18.
 */

public class Data {
    String position;
    String acceleration;
    String x;
    String y;
    String z;
    Data(String lat,String lng,String acc,String x,String y,String z){
        this.position = lat+"-"+lng;
        this.acceleration=acc;
        this.x=x;
        this.y=y;
        this.z=z;
    }
    public String getPos() {
        return position;
    }

    public String getAcc() {
        return acceleration;
    }
    public String getX() {
        return x;
    }
    public String getY() {
        return y;
    }
    public String getZ() {
        return z;
    }
}
