package com.dat.blackjacktest.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by DAT on 7/8/2015.
 */
public class Player {
    String name;
    int money;
    Chip bet;
    List<Card> cardsOnHand = new ArrayList<>();
    String score; //score is String to store not only number but "blackjack"
    int countingValue;
    int chair;

    int tempScore1;
    int tempScore2;
    int finalScore;

    public Player() {
    }

    public Player(String name, int chair) {
        this.chair = chair;
        this.name = name;
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

    public void addCardToHand(Card card) {
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

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public int getChair() {
        return chair;
    }

    public void setChair(int chair) {
        this.chair = chair;
    }

    public int getTempScore1() {
        return tempScore1;
    }

    public void setTempScore1(int tempScore1) {
        this.tempScore1 = tempScore1;
    }

    public int getTempScore2() {
        return tempScore2;
    }

    public void setTempScore2(int tempScore2) {
        this.tempScore2 = tempScore2;
    }

    public int getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(int finalScore) {
        this.finalScore = finalScore;
    }
}
