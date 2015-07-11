package com.dat.blackjacktest.model;

/**
 * Created by DAT on 7/8/2015.
 */
public class Card {
    int image_id;
    int value;
    String suit;
    boolean isOpen;

    public Card() {
    }

    public Card(int image_id, int value) {
        this.image_id = image_id;
        this.value = value;
    }

    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getSuit() {
        return suit;
    }

    public void setSuit(String suit) {
        this.suit = suit;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }
}
