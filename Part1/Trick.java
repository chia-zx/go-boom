import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;

public class Trick {
    private int trickNumber = 1;
    private TreeMap<String, Integer> trickList;
    private Map<String, Integer> centerDeck;
    private String leadCard;
    private char leadSuit;
    private char leadRank;
    private int leadPlayer;

    public Trick () {
        centerDeck = new LinkedHashMap<>();
    }

    public int getTrickNumber () {
        return trickNumber;
    }

    public String getLeadCard () {
        return leadCard;
    }

    public Map<String, Integer> getCenterDeck () {
        return centerDeck;
    }

    public void setCenterDeck (String card, int player) {
        centerDeck.put(card, player);
    }

    public void removeFromCenterDeck () {
        centerDeck.remove(leadCard, -1);
    }

    public void setLeadCard (String card, int player) {
        setCenterDeck(card, player);
        leadCard = card;
        setLeadSuit();
        setLeadRank();
    }

    public void setLeadSuit () {
        leadSuit = leadCard.charAt(0);
    }

    public void setLeadRank () {
        leadRank = leadCard.charAt(1);
    }

    public int getLeadPlayer () {
        return leadPlayer;
    }

    public void setLeadPlayer () {
        switch (leadRank) {
            case 'A', '5', '9', 'K':
                leadPlayer = 1;
                break;
            case '2', '6', 'X':
                leadPlayer = 2;
                break;
            case '3', '7', 'J':
                leadPlayer = 3;
                break;
            case '4', '8','Q':
                leadPlayer = 4;
                break;
        }
    }

    // check card by player is following the card at center
    public boolean checkCardFollow (String card) {
        if (centerDeck.isEmpty()) {
            setCenterDeck(card, Game.currentPlayer);
            setLeadCard(card, Game.currentPlayer);
        }
        char cardSuit  = card.charAt(0);
        char cardRank  = card.charAt(1);

        return (cardSuit == leadSuit) || (cardRank == leadRank);  
    }

    public void sortInOrder () {
        trickList = new TreeMap<>(new CardComparator());
        trickList.putAll(centerDeck);
    }

    public void checkTrickWinner () {
        removeFromCenterDeck();
        sortInOrder();
        // get the highest rank card
        try {
            String highest = trickList.lastKey();
            leadPlayer = trickList.get(highest);
        }
        catch (NoSuchElementException ex) {
            Game.centerEmpty = true;
        }
    }

    public void nextTrick() {
        System.out.println ("\n--------Player " + leadPlayer + " win Trick #" + getTrickNumber() + "--------");
        trickNumber++;
        centerDeck.clear();
        trickList.clear();
        leadCard = "-";
    }

    @Override
    public String toString() {
        return "\nTrick #" + getTrickNumber() + "\nLead Card : " + getLeadCard();
    }
}
