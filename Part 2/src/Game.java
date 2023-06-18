package src;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Game {
    Display display;
    Player[] players = new Player[4];
    Deck deck;
    Trick trick;
    File file;
    protected int currentPlayer;
    protected int winner;
    protected boolean centerEmpty = false; 
    protected int count = 0;

    public Game() {
        reset();
    }

    public void reset () {
        deck = new Deck();
        trick = new Trick(this);
        file = new File(this);
        for (int i = 0; i < players.length; ++i)
            players[i] = new Player();
        display = new Display(this);
    }

    public void startNewGame () {
        reset();
        count = 0;
        System.out.println ("\n------------New Game--------------");
        deck.shuffleDecks();
        dealCards();
        String card = deck.drawOneCard();
        trick.setLeadCard(card, -1); // card not be played by any player
        trick.setLeadPlayer();
        setCurrentPlayer();
        display.refresh();
        displayTrick();
    }

    public void resume (int currentPlayer, int count, int trickNumber, 
                        String leadCard, Map<String, Integer> centerDeck,
                        List<String> decks, int[] scoreList,
                        List<Set<String>> playerCards) {

        this.currentPlayer = currentPlayer;
        this.count = count;
        trick = new Trick(this, trickNumber, leadCard, centerDeck);
        deck = new Deck(decks);
        for (int i = 0; i < players.length; i++) 
            players[i] = new Player(scoreList[i], playerCards.get(i));
        display = new Display(this);
        file = new File(this);
    }

    public void dealCards () { // 7 cards each player
        for (int i = 0; i < 7; ++i) 
            for (Player player : players)
                player.receiveCard(deck.drawOneCard());
    }

    public void setCurrentPlayer () {
        currentPlayer = trick.getLeadPlayer();
    }

    // check whether player contain that card
    public boolean checkPlayerInput(String card) {
        return players[currentPlayer - 1].getPlayerCards().contains(card);
    }

    public void removeFromPlayerCard (String card) {
        players[currentPlayer - 1].removePlayerCards(card);
    }

    // switch between player
    public void switchPlayerTurn () {
        if (count != 0) {
            if (currentPlayer != 4)
                currentPlayer++;
            else
                currentPlayer = 1;
        }
    }

    public void drawFromDeck () {
        String card = deck.drawOneCard();
        players[currentPlayer - 1].receiveCard(card);
        System.out.println ("\n--------Player " + currentPlayer + " draw " + card + "--------");
    }

    public void quitProgram() {
        System.out.println ("\n--------Game End--------");
        System.exit(0);
    }

    public void displayTrick () {
        System.out.println (trick);

        // display all player cards
        for (int i = 1; i < players.length+1; ++i)
            System.out.println ("Player" + i + "\t  : " + players[i-1].getDisplayPlayerCards());
        
        // deck and center deck
        System.out.println ("Center\t  : " + trick.getCenterDeck().keySet());
        System.out.println (deck);
        
        System.out.println ("Score\t  : Player1 = " + players[0].getPlayerScore() +
                            " | Player2 = " + players[1].getPlayerScore() + 
                            " | Player3 = " + players[2].getPlayerScore() + 
                            " | Player4 = " + players[3].getPlayerScore());

        System.out.println ("Turn\t  : Player" + currentPlayer);
    }

    public void addPlayerScore() {
        int score = players[trick.getLeadPlayer() - 1].getPlayerScore();
        score += 1;
        players[trick.getLeadPlayer() - 1].setPlayerScore(score);
    }

    public boolean checkPlayerScore (int endPoint) {
        for (int i = 0; i < players.length; i++) {
            if (players[i].getPlayerScore() >= endPoint) {
                winner = i+1;
                return true;
            }
        }
        return false;
    }

    public boolean isDeckEmpty () {
        return deck.getDecks().isEmpty();
    }

    public boolean isPlayerCardEmpty () {
        return players[currentPlayer - 1].getPlayerCards().isEmpty();
    }

    public void gameEnd () {
        List<Integer> scoreList = new ArrayList<>(); 
        for (int i = 0; i < players.length; i++)
            scoreList.add(i, players[i].getPlayerScore());
        
        // get highest score and check is the value duplicate
        int highest = Collections.max(scoreList);
        int duplicate = -1;
        for (int i = 0; i < scoreList.size(); i++) {
            if (players[i].getPlayerScore() == highest)
                duplicate++;
        }

        if (highest != 0 && duplicate == 0) {
            winner = scoreList.indexOf(highest) + 1;
            System.out.println ("\n--------Player " + winner + " win---------");
        }
        else {
            System.out.print ("\n--------No winner---------");
        }
        quitProgram();
    }

    public String getFirstCardDeck() {
        if (deck.getDecks().size() == 0)
            return null;
        else
            return deck.getDecks().get(0);
    }

    public boolean isPassValid () {
        for (String card: players[currentPlayer-1].getPlayerCards()) {
            if (card.charAt(0) == trick.getLeadSuit() || 
                 card.charAt(1) == trick.getLeadRank())
                return false;
        }
        return true;
    }

    public void isCardValid (String selectedCard) {
        String header, content;
        if (selectedCard != null) {
            if (checkPlayerInput(selectedCard)) {
                System.out.println ("> " + selectedCard);
                if (trick.checkCardFollow(selectedCard)) {
                    removeFromPlayerCard(selectedCard);
                    trick.setCenterDeck(selectedCard, currentPlayer);
                    count++;
                    display.refresh();
                    trickEnd();
                    switchPlayerTurn();
                    displayTrick();
                }
                else {
                    header = "Error: Not following lead card suit or rank";
                    content = "Please choose another card";
                    display.showErrorMessage(header, content);
                    System.out.println ("\nError: Not following lead card suit or rank");
                }
            }
            else {
                header = "Error: That card belongs to other player";
                content = "Please choose from your deck";
                display.showErrorMessage(header, content);
                System.out.println ("\nError: That card belongs to other player");
            }
        }
        else {
            header = "Error: Press Play button without choosing a card";
            content = "Please choose a card";
            display.showErrorMessage(header, content);
            System.out.println ("\nError: Please choose a card");
        }
        display.resetSelected(); 
    }

    public void trickEnd() {
        int endPoint = 10;
        String title;
        String header;
        String content;
        if (count == 4) {
            count = 0;
            int trickWinner = trick.checkTrickWinner();
            if (!centerEmpty) {
                addPlayerScore();
                title = "Trick " + trick.getTrickNumber() + " Winner";
                header = "Congratulation";
                content = "Player " + trickWinner + " win Trick #" + trick.getTrickNumber();
                display.showMessage(title, header, content);
                display.refresh();
                trick.nextTrick();
                setCurrentPlayer();
            }
            else {
                display.declareWinner();
            }

            if (checkPlayerScore(endPoint)) {
                display.declareWinner();
            }

            if (isPlayerCardEmpty()) {
                winner = currentPlayer;
                display.declareWinner();
            }
        }
    }

    public int getWinner() {
        return winner;
    }
    
}

