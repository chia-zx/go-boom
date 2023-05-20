import java.util.Set;
import java.util.TreeSet;

public class Player {
    private int playerScore = 0;
    private Set<String> playerCards;

    public Player () {
        playerCards = new TreeSet<>(new CardComparator());
    }

    public Set<String> getPlayerCards () {
        return playerCards;
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
