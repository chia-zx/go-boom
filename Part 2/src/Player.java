package src;

import java.util.Set;
import java.util.TreeSet;

public class Player {
    private int playerScore = 0;
    private Set<String> playerCards;
    private Set<String> displayPlayerCards;

    public Player () {
        playerCards = new TreeSet<>(new CardSortComparator());
    }

    public Player (int playerScore, Set<String> playerCard) {
        playerCards = new TreeSet<>(new CardSortComparator());
        this.playerScore = playerScore;
        this.playerCards = playerCard;
    }

    public Set<String> getPlayerCards () {
        return playerCards;
    }

    public Set<String> getDisplayPlayerCards () {
        displayPlayerCards = new TreeSet<>(new CardDisplayComparator());
        displayPlayerCards.addAll(playerCards);
        return displayPlayerCards;
    }

    public int getPlayerScore () {
        return playerScore;
    }

    public void setPlayerScore (int score) {
        playerScore = score;
    }

    public void removePlayerCards (String card) {
        playerCards.remove(card);
    }

    public void receiveCard (String card) {
        playerCards.add(card);
    }

}
