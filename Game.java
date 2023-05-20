import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Game {
    Scanner input = new Scanner(System.in);

    Player[] players = new Player[4];
    Deck deck;
    Trick trick;
    public static int currentPlayer;
    private int winner;
    public static boolean centerEmpty = false;

    public Game() {
        reset();
    }

    public void reset () {
        deck = new Deck();
        trick = new Trick();
        for (int i = 0; i < players.length; ++i)
            players[i] = new Player();
    }

    public void startNewGame () {
        System.out.println ("\n------------New Game--------------");
        deck.shuffleDecks();
        dealCards();
        String card = deck.drawOneCard();
        trick.setLeadCard(card, -1); // card not be played by any player
        trick.setLeadPlayer();
        setCurrentPlayer();
        displayTrickTime();
    }

    public void dealCards () { // 7 cards each player
        for (int i = 0; i < 7; ++i) 
            for (Player player : players)
                player.receiveCard(deck.drawOneCard());
    }

    public void setCurrentPlayer () {
        currentPlayer = trick.getLeadPlayer();
    }

    public void removeFromPlayerCard (String card) {
        players[currentPlayer - 1].removePlayerCards(card);
    }

    // check whether player contain that card
    public boolean checkPlayerInput(String card) {
        return players[currentPlayer - 1].getPlayerCards().contains(card);
    }

    // switch between player
    public void switchPlayerTurn () {
        if (currentPlayer != 4)
            currentPlayer++;
        else
            currentPlayer = 1;
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

    public void getUserCommand () {
        boolean passNext = false;
        do {
            System.out.print ("> ");
            String selectedCard = input.next();
            
            if (checkPlayerInput(selectedCard)) {
                if (trick.checkCardFollow(selectedCard)) {
                    removeFromPlayerCard(selectedCard);
                    trick.setCenterDeck(selectedCard, currentPlayer);
                    switchPlayerTurn();
                    passNext = true;
                    // break;
                }
                else 
                    System.out.println ("Error: Not following lead card suit or rank");
            }
            else {
                switch (selectedCard.toLowerCase()) {
                    case "s": 
                        reset();
                        startNewGame();
                        break;
                    case "x": 
                        quitProgram();
                        break;
                    case "d": 
                        if (!isDeckEmpty()) {
                            drawFromDeck();
                            displayTrick();
                        }
                        else  if (!isPlayerCardEmpty())
                            // skip to next player if no card can be draw
                            switchPlayerTurn();
                        else
                            gameEnd();
                        passNext = true;
                        break;
                    default :
                        System.out.println ("Error: Invalid command");
                        break;
                }
            }
        } while (!passNext);
    }

    public void displayTrick () {
        System.out.println (trick);

        // display all player cards
        for (int i = 1; i < players.length+1; ++i)
            System.out.println ("Player" + i + "\t  : " + players[i-1].getPlayerCards());
        
        // deck and center deck
        System.out.println ("Center\t  : " + trick.getCenterDeck().keySet());
        System.out.println (deck);
        
        System.out.println ("Score\t  : Player1 = " + players[0].getPlayerScore() +
                            " | Player2 = " + players[1].getPlayerScore() + 
                            " | Player3 = " + players[2].getPlayerScore() + 
                            " | Player4 = " + players[3].getPlayerScore());

        System.out.println ("Turn\t  : Player" + currentPlayer);
        getUserCommand();
    }

    public void displayTrickTime () {
        do {
            for (int j = 0; j < players.length; ++j)
                displayTrick();
            
            trick.checkTrickWinner();
            if (!centerEmpty) {
                trick.nextTrick();
                addPlayerScore();
                setCurrentPlayer();
            }
            else 
                gameEnd();
        } while (!centerEmpty);
    }

    public void addPlayerScore() {
        int score = players[trick.getLeadPlayer() - 1].getPlayerScore();
        score += 1;
        players[trick.getLeadPlayer() - 1].setPlayerScore(score);
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
}

