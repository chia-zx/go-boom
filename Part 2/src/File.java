package src;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class File {
    Game game;
    Boolean fileError = false;
    
    public File (Game game) {
        this.game = game;
    }

    public void save (String input) {
        String filename = input + ".txt";

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));

            writer.write(game.currentPlayer + "");
            writer.newLine();
            writer.write(game.count + "");
            writer.newLine();
            writer.write(game.trick.getTrickNumber() + "");
            writer.newLine();
            writer.write(game.trick.getLeadCard() + "");

            // center 
            writer.newLine();
            for (var entry : game.trick.getCenterDeck().entrySet())
                writer.write(entry.getKey() + "@" + entry.getValue() + "#");

            // deck
            writer.newLine();
            for (int i = 0; i < game.deck.getDecks().size(); i++)
                writer.write(game.deck.getDecks().get(i) + "#");

            // player score
            writer.newLine();
            for (int i = 0; i < game.players.length; i++)
                writer.write(game.players[i].getPlayerScore() + "#");

            // player card
            for (int i = 0; i < game.players.length; i++) {
                writer.newLine();
                for (String card : game.players[i].getPlayerCards())
                    writer.write(card + "#");
            }

            writer.close();

        } catch (IOException e) {
            System.out.println("Error saving game: " + e.getMessage());
        }
    }

    public void load (String input) {
        String filename = input + ".txt";

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));

            try {
                Map<String, Integer> centerDeck = new LinkedHashMap<>();
                List<String> decks = new ArrayList<>();
                int[] playerScore = new int[4];
                List<Set<String>> playerCard = new ArrayList<>();
                Set<String> cards;

                int currentPlayer = Integer.parseInt(reader.readLine());
                int count = Integer.parseInt(reader.readLine());
                int trickNumber = Integer.parseInt(reader.readLine());
                String leadCard = reader.readLine();
                
                // center deck
                String[] list = reader.readLine().split("#");
                for (int i = 0; i < list.length; i++) {
                    String[] entry = list[i].split("@");
                    String key = entry[0];
                    int value = Integer.parseInt(entry[1]);
                    centerDeck.put(key, value);
                }

                // deck 
                list = reader.readLine().split("#");
                for (int i = 0; i < list.length; i++)
                    decks.add(list[i]);
                
                // player score
                list = reader.readLine().split("#");
                for (int i = 0; i < list.length; i++) {
                    int e = Integer.parseInt(list[i]);
                    playerScore[i] = e;
                }
                
                // player card
                for (int j = 0; j < 4; j++) {
                    cards = new LinkedHashSet<>();
                    list = reader.readLine().split("#");
                    for (int i = 0; i < list.length; i++)
                        cards.add(list[i]);
                    playerCard.add(j, cards);
                }

                reader.close();
                game.resume(currentPlayer, count, trickNumber, leadCard, centerDeck, decks, playerScore, playerCard);

            } catch (IOException e) {
                System.out.println("Error loading game: " + e.getMessage());
            }

        } catch (FileNotFoundException e) {
            String header = "Filename Error";
            String content = "File not found";
            game.display.showErrorMessage(header, content);
            fileError = true;
            System.out.println("Error loading game: File not found");
        }
    }
}