package com.dat.blackjacktest.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DAT on 7/8/2015.
 */
public class Dealer {
    List<Card> cardsOnHand = new ArrayList<>();


    public List<Card> getCardsOnHand() {
        return cardsOnHand;
    }

    public void getMoreCard(Card card) {
        cardsOnHand.add(card);
    }
}
