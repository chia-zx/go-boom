import java.util.Comparator;

class CardComparator implements Comparator<String> {
    @Override
    public int compare(String s1, String s2) {
        // string having length = 1 to pass
        if (s1.length() > 1 && s2.length() > 1) {
            String str1 = getRankValue(s1.charAt(1));
            String str2 = getRankValue(s2.charAt(1));

            s1 = s1.charAt(0) + str1;
            s2 = s2.charAt(0) + str2;
        }
            return s1.compareTo(s2);
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