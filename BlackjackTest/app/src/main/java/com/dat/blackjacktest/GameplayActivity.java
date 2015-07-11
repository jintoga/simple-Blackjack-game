package com.dat.blackjacktest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dat.blackjacktest.model.Card;
import com.dat.blackjacktest.model.Chip;
import com.dat.blackjacktest.model.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;


public class GameplayActivity extends Activity implements View.OnClickListener {
    int[] stack;
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
    List<Chip> chips = new ArrayList<>();
    Random random = new Random();
    FrameLayout frameLayoutDeck, player1Hand, player2Hand, player3Hand, player4Hand, player5Hand, player6Hand, dealerHand;
    RelativeLayout gameplayLayout;
    Button buttonMove, buttonDeal, buttonHit, buttonStand;
    ImageView imageViewDeck, imageViewCard;
    TextView textViewScore1, textViewScore2, textViewScore3, textViewScore4, textViewScore5, textViewScore6;
    TextView namePlayer1, namePlayer2, namePlayer3, namePlayer4, namePlayer5, namePlayer6;
    View cardPlace1, cardPlace2, cardPlace3, cardPlace4, cardPlace5, cardPlace6, cardPlaceDealer;
    ImageView chipPlace1, chipPlace2, chipPlace3, chipPlace4, chipPlace5, chipPlace6;
    ImageView player1Card1, player2Card1, player3Card1, player4Card1, player5Card1, player6Card1, dealerCard1;
    CardView cardViewDealer;
    ImageButton imageButtonBet5, imageButtonBet25, imageButtonBet50;
    List<View> listCardPlaces = new ArrayList<>();
    int playPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);
        Intent intent = getIntent();
        playPosition = intent.getIntExtra(MainActivity.tag_play_position, 0);
        getIDs();
        setEvents();

        AlertDialog builder = new AlertDialog.Builder(this).setTitle("Start Game?").setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //dialogInterface.dismiss();
                finish();
            }
        }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(GameplayActivity.this, "OK", Toast.LENGTH_SHORT).show();
                setChips();
                createDeck();
            }
        }).show();

    }

    private void getIDs() {
        buttonMove = (Button) findViewById(R.id.buttonMove);
        imageViewDeck = (ImageView) findViewById(R.id.imageViewDeck);
        imageViewCard = (ImageView) findViewById(R.id.imageViewCard);
        textViewScore1 = (TextView) findViewById(R.id.textViewScore1);
        textViewScore2 = (TextView) findViewById(R.id.textViewScore2);
        textViewScore3 = (TextView) findViewById(R.id.textViewScore3);
        textViewScore4 = (TextView) findViewById(R.id.textViewScore4);
        textViewScore5 = (TextView) findViewById(R.id.textViewScore5);
        textViewScore6 = (TextView) findViewById(R.id.textViewScore6);
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
        /*for (int i = 36; i < 52; i++) {
            decktemp.add(deck.get(i));
        }*/
        Log.e("", count10 + "");
    }

    int countMoveCard = 0;

    private void moveCard(View source, View destination) {
        Log.e("Movecard count:", countMoveCard + "");
        countMoveCard++;

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

        anim.setDuration(1000);
        anim.setFillAfter(true);
        source.startAnimation(anim);
    }


    private void testCards(int index) {
        //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), cardImage[index]);
        imageViewCard.setImageResource(cardImage[index]);
        moveCard(imageViewCard, cardPlace1);
    }


    Runnable runnable;

    int countToStop = 0;

    private void giveEachPlayer2Cards() {
        /*Runnable runnable = new Runnable() {
            int i = 0;

            public void run() {
                imageViewCard.setImageResource(cardImage[i]);
                i++;
                if (i > cardImage.length - 1) {
                    i = 0;
                }
                handler.postDelayed(this, 500);  //for interval...
            }
        };
        handler.postDelayed(runnable, 0); //for initial delay..*/

        runnable = new Runnable() {
            int i = 0;
            int j = 0;

            @Override
            public void run() {
                /*imageViewCard.setImageResource(cardImage[deck.get(i).getImage_index()]);
                if (countToStop == 13) {      //last card for dealer is closed
                    imageViewCard.setImageResource(R.drawable.card_backside);
                }*/
                imageViewCard.setImageResource(R.drawable.card_backside);

                moveCard(imageViewCard, listCardPlaces.get(j));

                addCardToHand(countToStop, j, deck.get(i));

                i++;
                j++;
                if (i > cardImage.length - 1) {
                    i = 0;
                }
                if (j > listCardPlaces.size() - 1) {
                    j = 0;
                }
                imageViewCard.postDelayed(this, 1500);
                countToStop++;
                //Log.e("K:", counterToStop + "");
                if (countToStop == 14) {  //this is pretty dumb way to stop, just count to 6*2 = 14 cards then stop... T_T
                    imageViewCard.removeCallbacks(runnable);
                    countToStop = 0;
                    buttonHit.setEnabled(true);
                    buttonStand.setEnabled(true);
                }
            }
        };

        runOnUiThread(runnable);
        //handler.postDelayed(runnable, 0);
    }

    private ImageView imageViewNewCard;
    private Handler handler = new Handler(); //use to delay "addCardToHand" time

    private void addCardToHand(int cardNumber, int placeNumber, Card card) {
        imageViewNewCard = new ImageView(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(imageViewCard.getWidth(), imageViewCard.getHeight());
        params.leftMargin = player1Card1.getWidth() / 4;
        imageViewNewCard.setLayoutParams(params);
        imageViewNewCard.setImageResource(cardImage[card.getImage_index()]);
        switch (placeNumber) {
            case 0:
                if (player1Hand.getChildCount() == 1) { //when there is no card yet in player's hand but only dummy imageview(playerCard1), view doesn't have to has margin so set margin back to 0
                    params.leftMargin = 0;
                    imageViewNewCard.setLayoutParams(params);
                }
                doDelayAddCard(player1Hand);
               /* player1Hand.addView(imageViewNewCard);*/
                break;
            case 1:
                if (player2Hand.getChildCount() == 1) { //when there is no card yet in player's hand but only dummy imageview(playerCard1), view doesn't have to has margin so set margin back to 0
                    params.leftMargin = 0;
                    imageViewNewCard.setLayoutParams(params);
                }
                doDelayAddCard(player2Hand);
                //player2Hand.addView(imageViewNewCard);
                break;
            case 2:
                if (player3Hand.getChildCount() == 1) { //when there is no card yet in player's hand but only dummy imageview(playerCard1), view doesn't have to has margin so set margin back to 0
                    params.leftMargin = 0;
                    imageViewNewCard.setLayoutParams(params);
                }
                doDelayAddCard(player3Hand);
                //player3Hand.addView(imageViewNewCard);
                break;
            case 3:
                if (player4Hand.getChildCount() == 1) { //when there is no card yet in player's hand but only dummy imageview(playerCard1), view doesn't have to has margin so set margin back to 0
                    params.leftMargin = 0;
                    imageViewNewCard.setLayoutParams(params);
                }
                doDelayAddCard(player4Hand);
                //player4Hand.addView(imageViewNewCard);
                break;
            case 4:
                if (player5Hand.getChildCount() == 1) { //when there is no card yet in player's hand but only dummy imageview(playerCard1), view doesn't have to has margin so set margin back to 0
                    params.leftMargin = 0;
                    imageViewNewCard.setLayoutParams(params);
                }
                doDelayAddCard(player5Hand);
                //player5Hand.addView(imageViewNewCard);
                break;
            case 5:
                if (player6Hand.getChildCount() == 1) { //when there is no card yet in player's hand but only dummy imageview(playerCard1), view doesn't have to has margin so set margin back to 0
                    params.leftMargin = 0;
                    imageViewNewCard.setLayoutParams(params);
                }
                doDelayAddCard(player6Hand);
                //player6Hand.addView(imageViewNewCard);
                break;
            case 6:
                if (dealerHand.getChildCount() == 1) { //when there is no card yet in player's hand but only dummy imageview(playerCard1), view doesn't have to has margin so set margin back to 0
                    params.leftMargin = 0;
                    imageViewNewCard.setLayoutParams(params);
                }
                if (cardNumber == 13) {
                    //imageViewNewCard.setImageResource(R.drawable.card_backside);
                    imageViewNewCard.setVisibility(View.INVISIBLE);
                }
                doDelayAddCard(dealerHand);
                //dealerHand.addView(imageViewNewCard);
                break;
            default:
                break;
        }
        Log.e("place:", placeNumber + "");
    }

    private void doDelayAddCard(final FrameLayout frameLayoutHand) {
        handler.postDelayed(new Runnable() {
            public void run() {
                frameLayoutHand.addView(imageViewNewCard);
            }
        }, 1500);
    }


    private void setChips() {
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
            players.add(bot);
        }
        Player player = new Player("You");
        players.set(playPosition, player); //replace bot to player by playPosition from MainActivity
        setNamePlayerAndBots();

        doBet();
        Log.e("", "");
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

    private void playerBet(Chip chip) {
        Player player = players.get(playPosition);
        player.setBet(chip);
        players.set(playPosition, player);
        doBet();
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
        }
        if (view.getId() == R.id.buttonStand) {
            Toast.makeText(this, "Stand", Toast.LENGTH_SHORT).show();
        }
        if (view.getId() == R.id.buttonMove) {
            Log.e("child:", dealerHand.toString());
            dealerHand.getChildAt(2).setVisibility(View.VISIBLE); // set 3rd item in dealerHand (1st one is dummy) => set 2nd card in dealer hand to visible
            imageViewCard.clearAnimation();
        }

    }

}
