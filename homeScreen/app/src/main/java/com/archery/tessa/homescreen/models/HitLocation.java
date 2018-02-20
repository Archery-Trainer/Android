package com.archery.tessa.homescreen.models;

/**
 * Created by mkkvj on 18.2.2018.
 */

public class HitLocation {
    //coordinates of hit location
    private int x;
    private int y;

    public HitLocation(int x, int y){
        this.x=x;
        this.y=y;
    }
    public int getX(){return this.x;}
    public int getY(){return this.y;}

    public void setX(int x){this.x=x;}
    public void setY(int y){this.y=y;}
}
