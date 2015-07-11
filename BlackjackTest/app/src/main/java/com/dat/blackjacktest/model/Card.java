package com.dat.blackjacktest.model;

/**
 * Created by DAT on 7/8/2015.
 */
public class Card {
    int image_index;
    int value;
    String suit;
    boolean isOpen;

    public Card() {
    }

    public Card(int image_index, int value) {
        this.image_index = image_index;
        this.value = value;
    }

    public int getImage_index() {
        return image_index;
    }

    public void setImage_index(int image_index) {
        this.image_index = image_index;
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
