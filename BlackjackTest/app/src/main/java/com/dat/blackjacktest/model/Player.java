package com.dat.blackjacktest.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DAT on 7/8/2015.
 */
public class Player {
    String name;
    int money;
    Chip bet;
    List<Card> cardsOnHand = new ArrayList<>();

    public Player() {
    }

    public Player(String name) {
        this.name = name;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public List<Card> getCardsOnHand() {
        return cardsOnHand;
    }

    public void getMoreCard(Card card) {
        cardsOnHand.add(card);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Chip getBet() {
        return bet;
    }

    public void setBet(Chip bet) {
        this.bet = bet;
    }
}
