package src;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private List<String> decks;
    public static List<String> centerDeck;
    public static final char[] cardSuits = {'c', 'd', 'h', 's'};
    public static final char[] cardRanks = {'A', '2', '3', '4', 
                                            '5', '6', '7', '8', 
                                            '9', 'X', 'J', 'Q', 'K'};
    public Deck () {
        centerDeck = new ArrayList<>();
        // create and assign value into decks
        decks = new ArrayList<>();
        for (int j = 0; j < cardSuits.length; ++j)
            for (int k = 0; k < cardRanks.length; ++k)
                decks.add("" + cardSuits[j] + cardRanks[k]);
    }

    public Deck (List<String> decks) {
        centerDeck = new ArrayList<>();
        this.decks = new ArrayList<>();
        this.decks = decks;
    }

    public List<String> getDecks () {
        return decks;
    }

    public void shuffleDecks () {
        Collections.shuffle(decks);
    }

    public String drawOneCard () {
        return decks.remove(0);
    }

    @Override
    public String toString() {
        return "Deck\t  : " + getDecks();
    }
}


