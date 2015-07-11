package com.dat.blackjacktest.model;

/**
 * Created by DAT on 7/10/2015.
 */
public class Chip {
    int value;
    int imageID;

    public Chip() {
    }

    public Chip(int value, int imageID) {
        this.value = value;
        this.imageID = imageID;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }
}
