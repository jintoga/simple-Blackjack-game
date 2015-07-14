package com.dat.blackjacktest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dat.blackjacktest.model.Card;
import com.dat.blackjacktest.model.Chip;
import com.dat.blackjacktest.model.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


public class GameplayActivity extends Activity implements View.OnClickListener {
    String[] suits;
    private final int[] cardImage = {
            R.drawable.card_1a, R.drawable.card_1b, R.drawable.card_1c, R.drawable.card_1d,
            R.drawable.card_2a, R.drawable.card_2b, R.drawable.card_2c, R.drawable.card_2d,
            R.drawable.card_3a, R.drawable.card_3b, R.drawable.card_3c, R.drawable.card_3d,
            R.drawable.card_4a, R.drawable.card_4b, R.drawable.card_4c, R.drawable.card_4d,
            R.drawable.card_5a, R.drawable.card_5b, R.drawable.card_5c, R.drawable.card_5d,
            R.drawable.card_6a, R.drawable.card_6b, R.drawable.card_6c, R.drawable.card_6d,
            R.drawable.card_7a, R.drawable.card_7b, R.drawable.card_7c, R.drawable.card_7d,
            R.drawable.card_8a, R.drawable.card_8b, R.drawable.card_8c, R.drawable.card_8d,
            R.drawable.card_9a, R.drawable.card_9b, R.drawable.card_9c, R.drawable.card_9d,
            R.drawable.card_10a, R.drawable.card_10b, R.drawable.card_10c, R.drawable.card_10d,
            R.drawable.card_11a, R.drawable.card_11b, R.drawable.card_11c, R.drawable.card_11d,
            R.drawable.card_12a, R.drawable.card_12b, R.drawable.card_12c, R.drawable.card_12d,
            R.drawable.card_13a, R.drawable.card_13b, R.drawable.card_13c, R.drawable.card_13d};
    private final int[] chipImage = {R.drawable.chip_5, R.drawable.chip_25, R.drawable.chip_50};
    private final int[] chipValue = {5, 25, 50};
    List<Card> deck = new ArrayList<>();
    List<Player> players = new ArrayList<>();
    List<Player> playersAfterBlackjack = new ArrayList<>();
    Player dealer = new Player("Dealer", 6); // Name: Dealer - Chair: 6
    List<Chip> chips = new ArrayList<>();
    Random random = new Random();
    FrameLayout frameLayoutDeck, player1Hand, player2Hand, player3Hand, player4Hand, player5Hand, player6Hand, dealerHand;
    RelativeLayout gameplayLayout;
    Button buttonMove, buttonDeal, buttonHit, buttonStand;
    ImageView imageViewDeck, imageViewCard, imageViewCardDealingAfterBJ;
    TextView scorePlayer1, scorePlayer2, scorePlayer3, scorePlayer4, scorePlayer5, scorePlayer6, scoreDealer;
    TextView namePlayer1, namePlayer2, namePlayer3, namePlayer4, namePlayer5, namePlayer6;
    View cardPlace1, cardPlace2, cardPlace3, cardPlace4, cardPlace5, cardPlace6, cardPlaceDealer;
    ImageView chipPlace1, chipPlace2, chipPlace3, chipPlace4, chipPlace5, chipPlace6;
    ImageView player1Card1, player2Card1, player3Card1, player4Card1, player5Card1, player6Card1, dealerCard1;
    CardView cardViewDealer, cardViewPlayer1, cardViewPlayer2, cardViewPlayer3, cardViewPlayer4, cardViewPlayer5, cardViewPlayer6;
    List<CardView> listHighlighter = new ArrayList<>();
    ImageButton imageButtonBet5, imageButtonBet25, imageButtonBet50;
    List<View> listCardPlaces = new ArrayList<>();
    List<View> listCardPlacesAfterCheckBlackjack = new ArrayList<>();
    int playPosition;
    private final int[] imageSad = {R.drawable.sad, R.drawable.rage1};
    ProgressBar progressBarWaitBots;
    TextView textViewWaiting, textViewCommand;
    ImageView imageViewWaiting;

    boolean afterBlackjack = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);
        Intent intent = getIntent();
        playPosition = intent.getIntExtra(MainActivity.tag_play_position, 0);
        getIDs();
        setEvents();
        //Toast.makeText(GameplayActivity.this, playPosition + "", Toast.LENGTH_SHORT).show();
        new AlertDialog.Builder(this).setTitle("Start Game?").setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //dialogInterface.dismiss();
                finish();
            }
        }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Toast.makeText(GameplayActivity.this, "OK", Toast.LENGTH_SHORT).show();
                setChipsAndBets();
                createDeck();
            }
        }).show();

    }

    private void getIDs() {
        progressBarWaitBots = (ProgressBar) findViewById(R.id.progressBarWaiting);
        textViewWaiting = (TextView) findViewById(R.id.textViewWaiting);
        textViewCommand = (TextView) findViewById(R.id.textViewCommand);
        imageViewWaiting = (ImageView) findViewById(R.id.imageViewWaiting);
        progressBarWaitBots.setVisibility(View.INVISIBLE);
        textViewWaiting.setVisibility(View.INVISIBLE);
        textViewCommand.setVisibility(View.INVISIBLE);
        imageViewWaiting.setVisibility(View.INVISIBLE);


        buttonMove = (Button) findViewById(R.id.buttonMove);
        imageViewDeck = (ImageView) findViewById(R.id.imageViewDeck);
        imageViewCard = (ImageView) findViewById(R.id.imageViewCard);
        imageViewCardDealingAfterBJ = (ImageView) findViewById(R.id.imageViewCardAfterBJ);
        imageViewCardDealingAfterBJ.setVisibility(View.INVISIBLE);

        scorePlayer1 = (TextView) findViewById(R.id.textViewScorePlayer1);
        scorePlayer2 = (TextView) findViewById(R.id.textViewScorePlayer2);
        scorePlayer3 = (TextView) findViewById(R.id.textViewScorePlayer3);
        scorePlayer4 = (TextView) findViewById(R.id.textViewScorePlayer4);
        scorePlayer5 = (TextView) findViewById(R.id.textViewScorePlayer5);
        scorePlayer6 = (TextView) findViewById(R.id.textViewScorePlayer6);
        scoreDealer = (TextView) findViewById(R.id.textViewScoreDealer);
        namePlayer1 = (TextView) findViewById(R.id.textViewPlayer1);
        namePlayer2 = (TextView) findViewById(R.id.textViewPlayer2);
        namePlayer3 = (TextView) findViewById(R.id.textViewPlayer3);
        namePlayer4 = (TextView) findViewById(R.id.textViewPlayer4);
        namePlayer5 = (TextView) findViewById(R.id.textViewPlayer5);
        namePlayer6 = (TextView) findViewById(R.id.textViewPlayer6);
        cardPlace1 = (View) findViewById(R.id.viewCard1);
        cardPlace2 = (View) findViewById(R.id.viewCard2);
        cardPlace3 = (View) findViewById(R.id.viewCard3);
        cardPlace4 = (View) findViewById(R.id.viewCard4);
        cardPlace5 = (View) findViewById(R.id.viewCard5);
        cardPlace6 = (View) findViewById(R.id.viewCard6);
        cardPlaceDealer = (View) findViewById(R.id.viewCardDealer);

        cardViewDealer = (CardView) findViewById(R.id.card_viewDealer);

        cardViewPlayer1 = (CardView) findViewById(R.id.card_viewPlay1);         //those use
        cardViewPlayer2 = (CardView) findViewById(R.id.card_viewPlay2);         //to show
        cardViewPlayer3 = (CardView) findViewById(R.id.card_viewPlay3);         //player's turn
        cardViewPlayer4 = (CardView) findViewById(R.id.card_viewPlay4);         //to play
        cardViewPlayer5 = (CardView) findViewById(R.id.card_viewPlay5);         //
        cardViewPlayer6 = (CardView) findViewById(R.id.card_viewPlay6);

        chipPlace1 = (ImageView) findViewById(R.id.imageViewChip1);
        chipPlace2 = (ImageView) findViewById(R.id.imageViewChip2);
        chipPlace3 = (ImageView) findViewById(R.id.imageViewChip3);
        chipPlace4 = (ImageView) findViewById(R.id.imageViewChip4);
        chipPlace5 = (ImageView) findViewById(R.id.imageViewChip5);
        chipPlace6 = (ImageView) findViewById(R.id.imageViewChip6);
        imageButtonBet5 = (ImageButton) findViewById(R.id.imageButtonChip5);
        imageButtonBet25 = (ImageButton) findViewById(R.id.imageButtonChip25);
        imageButtonBet50 = (ImageButton) findViewById(R.id.imageButtonChip50);
        buttonDeal = (Button) findViewById(R.id.buttonDeal);
        buttonHit = (Button) findViewById(R.id.buttonHit);
        buttonStand = (Button) findViewById(R.id.buttonStand);
        buttonDeal.setEnabled(false);
        buttonHit.setVisibility(View.GONE);
        buttonStand.setVisibility(View.GONE);

        player1Card1 = (ImageView) findViewById(R.id.imageViewPlayer1Card1);//these are just to make the parent(playerHand) in exact place
        player2Card1 = (ImageView) findViewById(R.id.imageViewPlayer2Card1);
        player3Card1 = (ImageView) findViewById(R.id.imageViewPlayer3Card1);
        player4Card1 = (ImageView) findViewById(R.id.imageViewPlayer4Card1);
        player5Card1 = (ImageView) findViewById(R.id.imageViewPlayer5Card1);
        player6Card1 = (ImageView) findViewById(R.id.imageViewPlayer6Card1);
        dealerCard1 = (ImageView) findViewById(R.id.imageViewDealerCard1);

        player1Card1.setVisibility(View.INVISIBLE);//so set them all to invisible forever
        player2Card1.setVisibility(View.INVISIBLE);
        player3Card1.setVisibility(View.INVISIBLE);
        player4Card1.setVisibility(View.INVISIBLE);
        player5Card1.setVisibility(View.INVISIBLE);
        player6Card1.setVisibility(View.INVISIBLE);
        dealerCard1.setVisibility(View.INVISIBLE);

       /* imageViewHider = (ImageView) findViewById(R.id.imageViewDealerCard2);
        imageViewHider.setVisibility(View.INVISIBLE);*/

        frameLayoutDeck = (FrameLayout) findViewById(R.id.frameLayoutDeck);
        gameplayLayout = (RelativeLayout) findViewById(R.id.gameplay_layout);
        player1Hand = (FrameLayout) findViewById(R.id.frameLayout_Player1Hand);
        player2Hand = (FrameLayout) findViewById(R.id.frameLayout_Player2Hand);
        player3Hand = (FrameLayout) findViewById(R.id.frameLayout_Player3Hand);
        player4Hand = (FrameLayout) findViewById(R.id.frameLayout_Player4Hand);
        player5Hand = (FrameLayout) findViewById(R.id.frameLayout_Player5Hand);
        player6Hand = (FrameLayout) findViewById(R.id.frameLayout_Player6Hand);
        dealerHand = (FrameLayout) findViewById(R.id.frameLayout_DealerHand);
        suits = getResources().getStringArray(R.array.suits);
        addCardPlacesToList();

    }

    private void addCardPlacesToList() {
        listCardPlaces.add(cardPlace1);
        listCardPlaces.add(cardPlace2);
        listCardPlaces.add(cardPlace3);
        listCardPlaces.add(cardPlace4);
        listCardPlaces.add(cardPlace5);
        listCardPlaces.add(cardPlace6);
        listCardPlaces.add(cardPlaceDealer);

        listHighlighter.add(cardViewPlayer1);
        listHighlighter.add(cardViewPlayer2);
        listHighlighter.add(cardViewPlayer3);
        listHighlighter.add(cardViewPlayer4);
        listHighlighter.add(cardViewPlayer5);
        listHighlighter.add(cardViewPlayer6);
        listHighlighter.add(cardViewDealer);
    }

    private void setEvents() {
        buttonMove.setOnClickListener(this);
        imageButtonBet5.setOnClickListener(this);
        imageButtonBet25.setOnClickListener(this);
        imageButtonBet50.setOnClickListener(this);
        buttonDeal.setOnClickListener(this);
        buttonHit.setOnClickListener(this);
        buttonStand.setOnClickListener(this);
    }

    public void createDeck() {
        int count10 = 0;
        for (int i = 1; i < 14; i++) {
            for (int j = 0; j < 4; j++) {
                Card card = new Card();
                card.setValue(i);
                card.setSuit(suits[j]);
                /*if (card.getValue() >= 11) {  //J(11),Q(12),K(13) => 10
                    card.setValue(10);
                    count10++;
                }*/
                deck.add(card);
            }
        }
        for (int i = 0; i < deck.size(); i++) {
            deck.get(i).setImage_index(i);
        }

        Log.e("", count10 + "");
    }

    //int countMoveCard = 0;

    private void moveCard(View source, View destination) {
        //Log.e("Movecard count:", countMoveCard + "");
        //countMoveCard++;

        RelativeLayout root = (RelativeLayout) findViewById(R.id.gameplay_layout);
        DisplayMetrics metrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int statusBarOffset = metrics.heightPixels - root.getMeasuredHeight();

        int originalPos[] = new int[2];
        int desPos[] = new int[2];
        source.getLocationOnScreen(originalPos);

        destination.getLocationOnScreen(desPos);

        int xDest = metrics.widthPixels;
        xDest = xDest - source.getMeasuredWidth();
        /*int yDest = metrics.heightPixels / 2 - (view.getMeasuredHeight() / 2) - statusBarOffset;
        int af = desPos[0] - xDest;
        int getMeasuredHeight = destination.getMeasuredHeight();*/
        TranslateAnimation anim;
        if (destination == cardPlaceDealer) { //move card to dealer is different from to player since dealer's and player's views are not the same
            anim = new TranslateAnimation(0, desPos[0] - xDest + destination.getMeasuredWidth() / 2 + 20, 0, desPos[1] - destination.getMeasuredHeight() - statusBarOffset + cardViewDealer.getMeasuredHeight() - 60);
        } else {
            anim = new TranslateAnimation(0, desPos[0] - xDest + destination.getMeasuredWidth() / 2 + 20, 0, desPos[1] - destination.getMeasuredHeight() - statusBarOffset - chipPlace1.getMeasuredHeight() - 40);
        }

        anim.setDuration(500);
        if (afterBlackjack == true) {
            anim.setFillAfter(false);
        } else {
            anim.setFillAfter(true);
        }
        source.startAnimation(anim);
    }


    private Runnable runnable;
    private int countToStopGivingEachPlayer2Cards = 0;
    private boolean beforeCheckBlackjack = true;

    private void giveEachPlayer2Cards() {

        runnable = new Runnable() {
            int j = 0;

            @Override
            public void run() {
                /*imageViewCard.setImageResource(cardImage[deck.get(i).getImage_index()]); //expose card before moving
                if (countToStop == 13) {      //last card for dealer is closed
                    imageViewCard.setImageResource(R.drawable.card_backside);
                }*/
                imageViewCard.setImageResource(R.drawable.card_backside);     //expose card after moving

                moveCard(imageViewCard, listCardPlaces.get(j));
                int selectedNumber = random.nextInt(deck.size());
                addCardToHand(j, deck.get(selectedNumber));
                deck.remove(selectedNumber); //remove dealt card from deck

                j++;

                if (j > listCardPlaces.size() - 1) {
                    j = 0;
                }
                imageViewCard.postDelayed(this, 500);
                countToStopGivingEachPlayer2Cards++;
                if (countToStopGivingEachPlayer2Cards == 14) {  //this is pretty dumb way to stop, just count to 6*2 = 14 cards then stop... T_T
                    imageViewCard.removeCallbacks(runnable);
                    countToStopGivingEachPlayer2Cards = 0;
                    buttonHit.setEnabled(true);
                    buttonStand.setEnabled(true);
                    Log.e("done2cards:", deck.toString());
                    checkBlackjackAndSetScore();
                    beforeCheckBlackjack = false;
                }
            }
        };

        runOnUiThread(runnable);
        //handler.postDelayed(runnable, 0);
    }

    private void checkBlackjackAndSetScore() {

        for (Player player : players) {/*
            Log.e(player.getName(), ":");
            for (int i = 0; i < 2; i++) {
                Log.e("card " + i, player.getCardsOnHand().get(i).getValue() + player.getCardsOnHand().get(i).getSuit());

            }*/
            if (player.getCardsOnHand().get(0).getValue() == 1 && player.getCardsOnHand().get(1).getValue() > 10) { //card 1 A , card 2 J/Q/K
                Log.e(player.getName(), "blackjack");
                player.setScore("Blackjack");
            } else if (player.getCardsOnHand().get(1).getValue() == 1 && player.getCardsOnHand().get(0).getValue() > 10) { //card 2 A , card 1 J/Q/K
                Log.e(player.getName(), "blackjack");
                player.setScore("Blackjack");
            } else {
                int score = 0;
                boolean hasAce = false;
                hasAce = checkHasAce(player);
                for (int i = 0; i < 2; i++) {

                    if (player.getCardsOnHand().get(i).getValue() >= 10) { //has 10/J/Q/K => 10
                        player.getCardsOnHand().get(i).setValue(10);
                    }
                    score += player.getCardsOnHand().get(i).getValue();
                }
                Log.e("Ace?", hasAce + "");
                if (hasAce == true) {
                    player.setHasAce(true);
                    player.setTempScore1(score);
                    player.setTempScore2(score + 10);
                    if (player.getTempScore2() == 21) {  // A + 10 = 11 | 21 => 11
                        player.setScore(player.getTempScore1() + "");                               // no finalScore yet
                    } else {
                        player.setScore(player.getTempScore1() + " | " + player.getTempScore2());   // no finalScore yet
                    }
                } else {
                    player.setScore(score + "");
                    player.setFinalScore(score);
                    player.setTempScore1(score);
                }
            }
        }
        //update score of players
        scorePlayer1.setText(players.get(0).getScore());
        scorePlayer2.setText(players.get(1).getScore());
        scorePlayer3.setText(players.get(2).getScore());
        scorePlayer4.setText(players.get(3).getScore());
        scorePlayer5.setText(players.get(4).getScore());
        scorePlayer6.setText(players.get(5).getScore());

        //PREPARE PROCESS PLAYING AFTER CHECK BLACKJACK
        playersAfterBlackjack = players;        // get new list of players
        listCardPlacesAfterCheckBlackjack = listCardPlaces; // get new list of places
       /* listCardPlacesAfterCheckBlackjack.remove(6); // this list doesn't include dealer's place*/
        /*for (Player player : playersAfterBlackjack) {
            player.setScore("Blackjack");
        }*/
        Log.e("Before:" + playersAfterBlackjack.size(), playersAfterBlackjack.toString());
        Log.e("listCardPlaces Before:" + listCardPlacesAfterCheckBlackjack.size(), listCardPlacesAfterCheckBlackjack.toString());
        Iterator iterator = playersAfterBlackjack.iterator();
        Iterator iterator1 = listCardPlaces.iterator();
        Iterator iterator2 = listHighlighter.iterator();
        while (iterator.hasNext()) {
            Player player = (Player) iterator.next();
            iterator1.next();
            iterator2.next();
            if (/*Integer.parseInt(player.getScore()) < 5*/player.getScore().equals("Blackjack")) { // remove player who got Blackjacked and his place
                iterator.remove();
                iterator1.remove();
                iterator2.remove();
            }
        }
        /*Log.e("after deleted:" + playersAfterBlackjack.size(), playersAfterBlackjack.toString());
        Log.e("listCardPlaces after deleted:" + listCardPlaces.size(), listCardPlaces.toString());*/

        //Log.e("added Dealer:" + playersAfterBlackjack.size(), playersAfterBlackjack.toString());
        // Check blackjack and score for Dealer but don't show it yet (like for Players) and wait for commands of players in next step
        if (dealer.getCardsOnHand().get(0).getValue() == 1 && dealer.getCardsOnHand().get(1).getValue() > 10) {
            dealer.setScore("Blackjack");
        } else if (dealer.getCardsOnHand().get(1).getValue() == 1 && dealer.getCardsOnHand().get(0).getValue() > 10) {
            dealer.setScore("Blackjack");
        } else {
            int score = 0;
            boolean hasAce = false;
            hasAce = checkHasAce(dealer);
            for (int i = 0; i < 2; i++) {
                if (dealer.getCardsOnHand().get(i).getValue() >= 10) { //has 10/J/Q/K
                    dealer.getCardsOnHand().get(i).setValue(10);
                }
                score += dealer.getCardsOnHand().get(i).getValue();
            }
            Log.e("Dealer Ace?", hasAce + "");
            if (hasAce == true) {
                dealer.setHasAce(true);
                dealer.setTempScore1(score);
                dealer.setTempScore2(score + 10);
                if (dealer.getTempScore2() == 21) {  // A + 10 = 11 | 21 => 11
                    dealer.setScore(dealer.getTempScore1() + "");                               // no finalScore yet
                } else {
                    dealer.setScore(dealer.getTempScore1() + " | " + dealer.getTempScore2());   // no finalScore yet
                }
            } else {
                dealer.setScore(score + "");
                dealer.setFinalScore(score);
                dealer.setTempScore1(score);
            }
        }
        if (!dealer.getScore().equals("Blackjack"))
            playersAfterBlackjack.add(dealer);              /* after sorted out the winning players, add dealer to the playing list.
                                                       no need to add dealer's place to "playersAfterBlackjack" because it's already there*/


        //playAfterCheckBlackjack();
        afterBlackjack = true;

    }

    private void playAfterCheckBlackjack() {    ///((((******** this method is pure magic *_* ///////////////
       /* playersAfterBlackjack.get(0).setTempScore1(-20);
        playersAfterBlackjack.get(0).setFinalScore(-20);
        playersAfterBlackjack.get(0).setTempScore2(-20);
        playersAfterBlackjack.get(0).setScore(-20 + "");
        playersAfterBlackjack.get(1).setTempScore1(-20);
        playersAfterBlackjack.get(1).setFinalScore(-20);
        playersAfterBlackjack.get(1).setTempScore2(-20);
        playersAfterBlackjack.get(1).setScore(-20 + "");*/


        runnable = new Runnable() {
            int countChair = 0;

            @Override
            public void run() {
                progressBarWaitBots.setVisibility(View.VISIBLE);
                imageViewWaiting.setVisibility(View.VISIBLE);
                textViewWaiting.setVisibility(View.VISIBLE);
                textViewCommand.setVisibility(View.VISIBLE);

                textViewWaiting.setText(playersAfterBlackjack.get(countChair).getName());

                giveCards(countChair);
                imageViewCardDealingAfterBJ.postDelayed(this, 4500);
                countChair++;
                //imageViewCard.postDelayed(this, 5000);
                /*textViewCommand.setText("Hmmm");
                imageViewWaiting.setImageResource(R.drawable.thinking);*/
                if (countChair == playersAfterBlackjack.size()) {       //players except Dealer get cards

                    imageViewCardDealingAfterBJ.removeCallbacks(runnable);
                    imageViewCardDealingAfterBJ.removeCallbacks(runnableGivecard);
                    progressBarWaitBots.setVisibility(View.INVISIBLE);
                    textViewWaiting.setVisibility(View.INVISIBLE);
                    textViewCommand.setVisibility(View.INVISIBLE);
                    imageViewWaiting.setVisibility(View.INVISIBLE);

                    Log.e("History:", gameHistory);
                }

            }
        };
        runOnUiThread(runnable);
    }

    private Runnable runnableGivecard;

    private String gameHistory = "";

    private void giveCards(final int countChair) {
        final Player player = playersAfterBlackjack.get(countChair);

        runnableGivecard = new Runnable() {
            @Override
            public void run() {
                boolean stop = false;
                if (!player.getName().equals("Dealer")) {
                    if (player.getTempScore1() < 10) {
                        imageViewWaiting.setImageResource(R.drawable.hit);
                        textViewCommand.setText("Hit");
                        imageViewCardDealingAfterBJ.setImageResource(R.drawable.card_backside);     //expose card after moving
                        moveCard(imageViewCardDealingAfterBJ, listCardPlaces.get(countChair));
                        int selectedNumber = random.nextInt(deck.size());
                        player.addCardToHand(deck.get(selectedNumber));
                        addNewCardByCommand(player, deck.get(selectedNumber));
                        gameHistory += player.getName() + " current score:" + player.getFinalScore() + " take card:" + deck.get(selectedNumber).getValue() + deck.get(selectedNumber).getSuit() + "\n";
                        Log.e("Took Card", player.getName() + " with score " + player.getFinalScore() + " took card:" + deck.get(selectedNumber).getValue() + deck.get(selectedNumber).getSuit());
                        checkFinalScore(player, deck.get(selectedNumber));
                        Log.e("score", player.getName() + " : Score= " + player.getFinalScore());
                        deck.remove(selectedNumber); //remove dealt card from deck
                        updateScore(player);
                        if (player.getFinalScore() > 21) {
                            stop = true;
                        }

                    } else if (player.getTempScore1() >= 10 && player.getFinalScore() <= 16) {
                        Log.e("50-50", "50-50");
                        int chanceToTakeMoreCard = random.nextInt(2);
                        int selectedNumber = random.nextInt(deck.size());
                        if (chanceToTakeMoreCard != 0 && chanceToTakeMoreCard != 1) {
                            Log.e("Bad luck", player.getName() + " BadLUCK");
                            gameHistory += player.getName() + " thinks he is out of luck so he STAND with score:" + player.getFinalScore() + "\n";
                            ////*****Special case
                            // when player has 2 first cards that contain A then play decides to STAND => pass method checkFinalScore(...); => no finalScore update
                            // just check for this case and update finalScore
                            if (checkHasAce(player)/* && player.getTempScore1() == 11) || (checkHasAce(player) && player.getTempScore1() == 10)*/) {
                                Log.e("GOTTEEM", "DEEZNUTZ");
                                player.setFinalScore(player.getTempScore1());
                            }
                            stop = true;
                            imageViewWaiting.setImageResource(R.drawable.stand);
                            textViewCommand.setText("Stand");
                        /*if (player.getFinalScore() >= 10 && player.getFinalScore() <= 21) {
                            imageViewWaiting.setImageResource(R.drawable.stand);
                            textViewCommand.setText("Stand");
                        }*/
                        } else if (chanceToTakeMoreCard == 0 || chanceToTakeMoreCard == 1) {    //need to fix

                            Log.e("Chance Take more", chanceToTakeMoreCard + "");
                            imageViewCardDealingAfterBJ.setImageResource(R.drawable.card_backside);     //expose card after moving
                            moveCard(imageViewCardDealingAfterBJ, listCardPlaces.get(countChair));
                            player.addCardToHand(deck.get(selectedNumber));
                            addNewCardByCommand(player, deck.get(selectedNumber));
                            Log.e("Took Card", player.getName() + " with score " + player.getFinalScore() + " took card:" + deck.get(selectedNumber).getValue() + deck.get(selectedNumber).getSuit());
                            gameHistory += player.getName() + " current score:" + player.getFinalScore() + " take card:" + deck.get(selectedNumber).getValue() + deck.get(selectedNumber).getSuit() + "\n";
                            checkFinalScore(player, deck.get(selectedNumber));
                            deck.remove(selectedNumber); //remove dealt card from deck
                            imageViewWaiting.setImageResource(R.drawable.hit);
                            textViewCommand.setText("Hit Again");
                        }
                        updateScore(player);

                    } else if (player.getFinalScore() > 16) {          //then check if score >16 stop taking card

                        imageViewWaiting.setImageResource(R.drawable.stand);
                        textViewCommand.setText("Stand");
                        Log.e("STAND", player.getName() + " STAND WITH " + ": Score= " + player.getFinalScore());
                        gameHistory += player.getName() + " thinks he is out of luck so he STAND with score:" + player.getFinalScore() + "\n";
                        updateScore(player);

                        stop = true;
                    }

                    imageViewCardDealingAfterBJ.postDelayed(this, 750);
                    if (player.getFinalScore() > 21) {
                        imageViewWaiting.setImageResource(R.drawable.rage1);
                        textViewCommand.setText("Bust");
                    } else if (player.getFinalScore() == 21) {
                        imageViewWaiting.setImageResource(R.drawable.lol);
                    }
                    if (stop == true) {
                        imageViewCardDealingAfterBJ.removeCallbacks(runnableGivecard);
                    }

                }

            }
        };

        runOnUiThread(runnableGivecard);
    }


    private void updateScore(Player player) {
        switch (player.getChair()) {
            case 0:
                if (player.getFinalScore() > 21)
                    scorePlayer1.setText(player.getFinalScore() + " (BUST)");
                else if (player.getFinalScore() == 21)
                    scorePlayer1.setText("Blackjack");
                else
                    scorePlayer1.setText(player.getFinalScore() + "");
                break;
            case 1:
                if (player.getFinalScore() > 21)
                    scorePlayer2.setText(player.getFinalScore() + " (BUST)");
                else if (player.getFinalScore() == 21)
                    scorePlayer2.setText("Blackjack");
                else
                    scorePlayer2.setText(player.getFinalScore() + "");
                break;
            case 2:
                if (player.getFinalScore() > 21)
                    scorePlayer3.setText(player.getFinalScore() + " (BUST)");
                else if (player.getFinalScore() == 21)
                    scorePlayer3.setText("Blackjack");
                else
                    scorePlayer3.setText(player.getFinalScore() + "");
                break;
            case 3:
                if (player.getFinalScore() > 21)
                    scorePlayer4.setText(player.getFinalScore() + " (BUST)");
                else if (player.getFinalScore() == 21)
                    scorePlayer4.setText("Blackjack");
                else
                    scorePlayer4.setText(player.getFinalScore() + "");
                break;
            case 4:
                if (player.getFinalScore() > 21)
                    scorePlayer5.setText(player.getFinalScore() + " (BUST)");
                else if (player.getFinalScore() == 21)
                    scorePlayer5.setText("Blackjack");
                else
                    scorePlayer5.setText(player.getFinalScore() + "");
                break;
            case 5:
                if (player.getFinalScore() > 21)
                    scorePlayer6.setText(player.getFinalScore() + " (BUST)");
                else if (player.getFinalScore() == 21)
                    scorePlayer6.setText("Blackjack");
                else
                    scorePlayer6.setText(player.getFinalScore() + "");
                break;
            case 6:
                if (player.getFinalScore() > 21)
                    scoreDealer.setText(player.getFinalScore() + " (BUST)");
                else if (player.getFinalScore() == 21)
                    scoreDealer.setText("Blackjack");
                else
                    scoreDealer.setText(player.getFinalScore() + "");
                break;
            default:
                break;
        }
    }


    private void checkFinalScore(Player player, Card card) {

        if (player.isHasAce()) {
            if (card.getValue() >= 10) {         //J/Q/K => 10
                card.setValue(10);
            }
            int score1 = card.getValue() + player.getTempScore1();
            int score2 = card.getValue() + player.getTempScore2();
            if (score2 > 21) {
                player.setFinalScore(score1);
                player.setTempScore1(score1);   //new
            } else {
                player.setFinalScore(score2);
                player.setTempScore1(score2);   //new
            }
        } else {
            if (card.getValue() == 1) {
                int tempScore = player.getCurrentScore();
                player.setFinalScore(tempScore);
                player.setTempScore1(tempScore);
                Log.e("GOOTEEEM", "DEEEZNUZ");
            } else {
                if (card.getValue() >= 10) {         //J/Q/K => 10
                    card.setValue(10);
                }
                /*int score = player.getFinalScore();*/  //
                int score = player.getCurrentScore();
                /*score += card.getValue();*/
                player.setFinalScore(score);
                player.setTempScore1(score);    //new
            }

        }
    }

    private boolean checkHasAce(Player player) {
        boolean hasAce = false;
        for (Card card : player.getCardsOnHand()) {
            if (card.getValue() == 1)
                hasAce = true;
        }
        Log.e("Has Ace:", hasAce + "");
        return hasAce;
    }


    private ImageView imageViewNewCardAfterBJ;


    private void addNewCardByCommand(final Player player, final Card card) {
        imageViewNewCardAfterBJ = new ImageView(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(imageViewCard.getWidth(), imageViewCard.getHeight());
        if (player.getCardsOnHand().size() == 3)
            params.leftMargin = player1Card1.getWidth() / 2;
        else if (player.getCardsOnHand().size() == 4)
            params.leftMargin = player1Card1.getWidth() * 3 / 4;
        else if (player.getCardsOnHand().size() == 5)
            params.leftMargin = player1Card1.getWidth();
        else if (player.getCardsOnHand().size() == 6)
            params.leftMargin = player1Card1.getWidth() * 5 / 4;
        else if (player.getCardsOnHand().size() == 7)
            params.leftMargin = player1Card1.getWidth() * 3 / 2;
        else if (player.getCardsOnHand().size() == 8)
            params.leftMargin = player1Card1.getWidth() * 7 / 4;
        else if (player.getCardsOnHand().size() == 9)
            params.leftMargin = player1Card1.getWidth() * 2;

        imageViewNewCardAfterBJ.setLayoutParams(params);
        imageViewNewCardAfterBJ.setImageResource(cardImage[card.getImage_index()]);
        switch (player.getChair()) {
            case 0:
                handler.postDelayed(new Runnable() {
                    public void run() {
                        if (imageViewNewCardAfterBJ.getParent() != null) {
                            Log.e("Parent:", imageViewNewCardAfterBJ.getParent().toString());
                            Log.e("Children", player1Hand.toString());
                            ((ViewGroup) imageViewCard.getParent()).removeView(imageViewNewCardAfterBJ);
                        }
                        player1Hand.addView(imageViewNewCardAfterBJ);
                    }
                }, 500);
               /* player1Hand.addView(imageViewNewCardAfterBJ);*/
                break;
            case 1:
                Log.e("Player take New Card:", player.getName() + " - " + card.getValue() + card.getSuit());
                handler.postDelayed(new Runnable() {
                    public void run() {
                        if (imageViewNewCardAfterBJ.getParent() != null) {
                            Log.e("Parent:", imageViewNewCardAfterBJ.getParent().toString());
                            Log.e("Children", player1Hand.toString());
                            ((ViewGroup) imageViewCard.getParent()).removeView(imageViewNewCardAfterBJ);
                        }
                        player2Hand.addView(imageViewNewCardAfterBJ);
                    }
                }, 500);
                break;
            case 2:
                Log.e("Player take New Card:", player.getName() + " - " + card.getValue() + card.getSuit());
                handler.postDelayed(new Runnable() {
                    public void run() {
                        if (imageViewNewCardAfterBJ.getParent() != null) {
                            Log.e("Parent:", imageViewNewCardAfterBJ.getParent().toString());
                            Log.e("Children", player1Hand.toString());
                            ((ViewGroup) imageViewCard.getParent()).removeView(imageViewNewCardAfterBJ);
                        }
                        player3Hand.addView(imageViewNewCardAfterBJ);
                    }
                }, 500);
                ;
                break;
            case 3:
                Log.e("Player take New Card:", player.getName() + " - " + card.getValue() + card.getSuit());
                handler.postDelayed(new Runnable() {
                    public void run() {
                        if (imageViewNewCardAfterBJ.getParent() != null) {
                            Log.e("Parent:", imageViewNewCardAfterBJ.getParent().toString());
                            Log.e("Children", player1Hand.toString());
                            ((ViewGroup) imageViewCard.getParent()).removeView(imageViewNewCardAfterBJ);
                        }
                        player4Hand.addView(imageViewNewCardAfterBJ);
                    }
                }, 500);
                break;
            case 4:
                Log.e("Player take New Card:", player.getName() + " - " + card.getValue() + card.getSuit());
                handler.postDelayed(new Runnable() {
                    public void run() {
                        if (imageViewNewCardAfterBJ.getParent() != null) {
                            Log.e("Parent:", imageViewNewCardAfterBJ.getParent().toString());
                            Log.e("Children", player1Hand.toString());
                            ((ViewGroup) imageViewCard.getParent()).removeView(imageViewNewCardAfterBJ);
                        }
                        player5Hand.addView(imageViewNewCardAfterBJ);
                    }
                }, 500);
                break;
            case 5:
                Log.e("Player take New Card:", player.getName() + " - " + card.getValue() + card.getSuit());
                handler.postDelayed(new Runnable() {
                    public void run() {
                        if (imageViewNewCardAfterBJ.getParent() != null) {
                            Log.e("Parent:", imageViewNewCardAfterBJ.getParent().toString());
                            Log.e("Children", player1Hand.toString());
                            ((ViewGroup) imageViewCard.getParent()).removeView(imageViewNewCardAfterBJ);
                        }
                        player6Hand.addView(imageViewNewCardAfterBJ);
                    }
                }, 500);
                break;
            default:
                break;
        }

    }

    private void myTurn(Player player) {
        //Toast.makeText(this, player.getName(), Toast.LENGTH_SHORT).show();
        showToast(player);
        for (Player p : playersAfterBlackjack) {
            Log.e("Players", p.getName());
            for (Card card : p.getCardsOnHand()) {
                Log.e("Card" + p.getCardsOnHand().indexOf(card), card.getValue() + card.getSuit());
            }
        }
        buttonHit.setEnabled(true);
        buttonStand.setEnabled(true);
        Log.e("myTurn", "myTurn");

    }


    private void showToast(Player player) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast_layout, (ViewGroup) findViewById(R.id.custom_toast_layout_id));
        TextView textViewName = (TextView) layout.findViewById(R.id.textViewToastPlayerName);
        textViewName.setText(player.getName());
        if (player.getName().equals("You")) {
            final Toast toast = new Toast(getApplicationContext());
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    toast.cancel();
                }
            }, 500);
        } else {
            Toast toast = new Toast(getApplicationContext());
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();
        }
    }

    private ImageView imageViewNewCard;      //use to show animation "addCardToHand"
    private Handler handler = new Handler(); //use to delay "addCardToHand" time
    private int idToReveal2ndCardOfDealer;

    private void addCardToHand(int placeNumber, Card card) {
        imageViewNewCard = new ImageView(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(imageViewCard.getWidth(), imageViewCard.getHeight());
        params.leftMargin = player1Card1.getWidth() / 4;
        imageViewNewCard.setLayoutParams(params);
        imageViewNewCard.setImageResource(cardImage[card.getImage_index()]);
        Log.e("DECK:", deck.toString());
        switch (placeNumber) {
            case 0:
                if (player1Hand.getChildCount() == 1) { //when there is no card yet in player's hand but only dummy imageview(playerCard1), view doesn't have to has margin so set margin back to 0
                    params.leftMargin = 0;
                    imageViewNewCard.setLayoutParams(params);
                }
                doDelayAddCard(player1Hand);

               /* player1Hand.addView(imageViewNewCard);*/
                players.get(0).addCardToHand(card);
                break;
            case 1:
                if (player2Hand.getChildCount() == 1) { //when there is no card yet in player's hand but only dummy imageview(playerCard1), view doesn't have to has margin so set margin back to 0
                    params.leftMargin = 0;
                    imageViewNewCard.setLayoutParams(params);
                }
                doDelayAddCard(player2Hand);

                players.get(1).addCardToHand(card);
                //player2Hand.addView(imageViewNewCard);
                break;
            case 2:
                if (player3Hand.getChildCount() == 1) { //when there is no card yet in player's hand but only dummy imageview(playerCard1), view doesn't have to has margin so set margin back to 0
                    params.leftMargin = 0;
                    imageViewNewCard.setLayoutParams(params);
                }
                doDelayAddCard(player3Hand);
                players.get(2).addCardToHand(card);
                //player3Hand.addView(imageViewNewCard);
                break;
            case 3:
                if (player4Hand.getChildCount() == 1) { //when there is no card yet in player's hand but only dummy imageview(playerCard1), view doesn't have to has margin so set margin back to 0
                    params.leftMargin = 0;
                    imageViewNewCard.setLayoutParams(params);
                }
                doDelayAddCard(player4Hand);
                players.get(3).addCardToHand(card);
                //player4Hand.addView(imageViewNewCard);
                break;
            case 4:
                if (player5Hand.getChildCount() == 1) { //when there is no card yet in player's hand but only dummy imageview(playerCard1), view doesn't have to has margin so set margin back to 0
                    params.leftMargin = 0;
                    imageViewNewCard.setLayoutParams(params);
                }
                doDelayAddCard(player5Hand);
                players.get(4).addCardToHand(card);
                //player5Hand.addView(imageViewNewCard);
                break;
            case 5:
                if (player6Hand.getChildCount() == 1) { //when there is no card yet in player's hand but only dummy imageview(playerCard1), view doesn't have to has margin so set margin back to 0
                    params.leftMargin = 0;
                    imageViewNewCard.setLayoutParams(params);
                }
                doDelayAddCard(player6Hand);
                players.get(5).addCardToHand(card);
                //player6Hand.addView(imageViewNewCard);
                break;
            case 6:
                if (dealerHand.getChildCount() == 1) { /*// it's 2 but not 1 like for players
                    // because dealer has 2 dummies cards inside, the 2nd one is for hiding 2nd card*/
                    params.leftMargin = 0;
                    imageViewNewCard.setLayoutParams(params);
                }
                if (countToStopGivingEachPlayer2Cards == 13) {
                    //imageViewNewCard.setImageResource(R.drawable.card_backside);
                    //idToReveal2ndCardOfDealer = imageViewNewCard.getId();
                    imageViewNewCard.setVisibility(View.INVISIBLE);
                    Log.e("Hiding Card", "Hiding card");
                    //imageViewHider.setVisibility(View.VISIBLE);

                }
                doDelayAddCard(dealerHand);
                dealer.addCardToHand(card);
                //dealerHand.addView(imageViewNewCard);
                break;
            default:
                break;
        }
        Log.e("place:", placeNumber + "");
    }

    private void doDelayAddCard(FrameLayout frameLayoutHand) {
        final FrameLayout playerHand = frameLayoutHand;
        handler.postDelayed(new Runnable() {
            public void run() {
                if (imageViewNewCard.getParent() != null) {
                    Log.e("Parent:", imageViewNewCard.getParent().toString());
                    Log.e("Children", playerHand.toString());
                    ((ViewGroup) imageViewCard.getParent()).removeView(imageViewNewCard);
                }
                playerHand.addView(imageViewNewCard);
            }
        }, 500);
    }


    private void setChipsAndBets() {
        for (int i = 0; i < 3; i++) {       //create chips list with value and image
            Chip chip = new Chip();
            chip.setImageID(i);
            chip.setValue(chipValue[i]);
            chips.add(chip);
        }

        for (int i = 0; i < 6; i++) {   //create a list of Bots
            Player bot = new Player("Bot " + i);
            Chip chip = chips.get(random.nextInt(3));
            bot.setBet(chip);
            bot.setChair(i);
            players.add(bot);
        }
        Player player = new Player("You");
        player.setChair(playPosition);
        players.set(playPosition, player); //replace bot to player by playPosition from MainActivity
        setNamePlayerAndBots();


        doBet();
        Log.e("", "");
    }

    private void playerBet(Chip chip) {
        Player player = players.get(playPosition);
        player.setBet(chip);
        players.set(playPosition, player);
        doBet();
    }

    private void doBet() {
        if (players.get(0).getBet() != null)
            chipPlace1.setImageResource(chipImage[players.get(0).getBet().getImageID()]);
        if (players.get(1).getBet() != null)
            chipPlace2.setImageResource(chipImage[players.get(1).getBet().getImageID()]);
        if (players.get(2).getBet() != null)
            chipPlace3.setImageResource(chipImage[players.get(2).getBet().getImageID()]);
        if (players.get(3).getBet() != null)
            chipPlace4.setImageResource(chipImage[players.get(3).getBet().getImageID()]);
        if (players.get(4).getBet() != null)
            chipPlace5.setImageResource(chipImage[players.get(4).getBet().getImageID()]);
        if (players.get(5).getBet() != null)
            chipPlace6.setImageResource(chipImage[players.get(5).getBet().getImageID()]);
    }


    private void setNamePlayerAndBots() {
        namePlayer1.setText(players.get(0).getName());
        namePlayer2.setText(players.get(1).getName());
        namePlayer3.setText(players.get(2).getName());
        namePlayer4.setText(players.get(3).getName());
        namePlayer5.setText(players.get(4).getName());
        namePlayer6.setText(players.get(5).getName());
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.imageButtonChip5) {
            playerBet(chips.get(0));
            buttonDeal.setEnabled(true);
        }
        if (view.getId() == R.id.imageButtonChip25) {
            playerBet(chips.get(1));
            buttonDeal.setEnabled(true);
        }
        if (view.getId() == R.id.imageButtonChip50) {
            playerBet(chips.get(2));
            buttonDeal.setEnabled(true);
        }
        if (view.getId() == R.id.buttonDeal) {
            Toast.makeText(this, "Deal", Toast.LENGTH_SHORT).show();
            //handler.removeCallbacks(runnable);
            giveEachPlayer2Cards();
            buttonDeal.setVisibility(View.GONE);
            buttonHit.setVisibility(View.VISIBLE);
            buttonHit.setEnabled(false);
            buttonStand.setVisibility(View.VISIBLE);
            buttonStand.setEnabled(false);
            imageButtonBet5.setVisibility(View.GONE);
            imageButtonBet25.setVisibility(View.GONE);
            imageButtonBet50.setVisibility(View.GONE);

        }
        if (view.getId() == R.id.buttonHit) {
            Toast.makeText(this, "Hit", Toast.LENGTH_SHORT).show();
            //giveCardByCommand(0);
            //playAfterCheckBlackjackNew();
            playAfterCheckBlackjack();
        }
        if (view.getId() == R.id.buttonStand) {
            Toast.makeText(this, "Stand", Toast.LENGTH_SHORT).show();
        }
        if (view.getId() == R.id.buttonMove) {
            handler.removeCallbacksAndMessages(null);
            Log.e("child 0:", dealerHand.getChildAt(0).toString() + " child 1" + dealerHand.getChildAt(1).toString());
            if (dealerHand.getChildAt(2) != null)
                dealerHand.getChildAt(2).setVisibility(View.VISIBLE); // set 3rd item in dealerHand (1st one is dummy) => set 2nd card in dealer hand to visible
            imageViewCard.clearAnimation();     // covered card actually is just fillAfter of animation => clear it to show card

            if (dealer.getScore() != null)
                scoreDealer.setText(dealer.getScore());
        }

    }

}
