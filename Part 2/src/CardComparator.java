package src;
import java.util.Comparator;

public class CardComparator {
    public char getSuitValue (char suit) {
        char value;
        switch (suit) {
            case 's': 
                value = '1';
                break;
            case 'h': 
                value = '2';
                break;
            case 'c': 
                value = '3';
                break;
            default :   // d
                value = '4';
                break;
        }
        return value;
    }

    public char getSuitValue2 (char suit) {
        char value;
        switch (suit) {
            case 's': 
                value = '4';
                break;
            case 'h': 
                value = '3';
                break;
            case 'c': 
                value = '2';
                break;
            default :   // d
                value = '1';
                break;
        }
        return value;
    }

    public String getRankValue (char rank) {
        String value;
        switch (rank) {
            case 'A': 
                value = "01";
                break;
            case 'X': 
                value = "10";
                break;
            case 'J': 
                value = "11";
                break;
            case 'Q': 
                value = "12";
                break;
            case 'K': 
                value = "13";
                break;
            default :
                value = "0" + rank;
                break;
        }
        return value;
    }
}

// sort to display card
class CardDisplayComparator implements Comparator<String> { 
    CardComparator cardComparator = new CardComparator();
    
    @Override
    public int compare(String s1, String s2) {
        // string having length = 1 to pass
        if (s1.length() > 1 && s2.length() > 1) {
            char suit1 = cardComparator.getSuitValue(s1.charAt(0));
            char suit2 = cardComparator.getSuitValue(s2.charAt(0));

            String rank1 = cardComparator.getRankValue(s1.charAt(1));
            String rank2 = cardComparator.getRankValue(s2.charAt(1));

            s1 = suit1 + rank1;
            s2 = suit2 + rank2;
        }
            return s1.compareTo(s2);
    }
}

// compare the value
class CardSortComparator implements Comparator<String> {  
    CardComparator cardComparator = new CardComparator();

    @Override
    public int compare(String s1, String s2) {
        // string having length = 1 to pass
        if (s1.length() > 1 && s2.length() > 1) {
            char suit1 = cardComparator.getSuitValue2(s1.charAt(0));
            char suit2 = cardComparator.getSuitValue2(s2.charAt(0));

            String rank1 = cardComparator.getRankValue(s1.charAt(1));
            String rank2 = cardComparator.getRankValue(s2.charAt(1));

            s1 = rank1 + suit1;
            s2 = rank2 + suit2;
        }
            return s1.compareTo(s2);
    }

}
