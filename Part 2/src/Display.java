package src;

import java.util.Map;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Display {
    private Game game;
    ImageView selectedImage = null;    // previous selected card
    String selectedCard = null;
    VBox vBox;      // player card
    HBox hBox;      // deck and center card
    VBox buttonVBox;
    StackPane showTrick;     // showing trick number

    public Display (Game game) {
        this.game = game;
        vBox = new VBox(10);
        hBox = new HBox(30);
        buttonVBox = new VBox(60);
        showTrick = new StackPane();
        addButton();
        refresh();
    }

    protected void resetSelected () {
        // set to null when proceed to next action
        selectedCard = null;
        selectedImage = null;
    }
    
    public void refresh() { 
        updateImage();
        updateDeckCenter();
        updateTrickBox();
    }

    protected void showMessage (String title, String header, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    protected void showErrorMessage (String header, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void declareWinner () {
        if (game.winner != 0) {
            String title = "Congratulation";
            String header = "Player " + game.winner + " win";
            String content = "Player " + game.winner + " win the game";
            game.display.showMessage(title, header, content);
        }
        game.gameEnd();
    }

    private void addButton () {
        Button playCardButton = new Button("Play card");
        playCardButton.setOnAction(e -> handlePlayCardButton());
        Button drawCardButton = new Button("Draw Card");
        drawCardButton.setOnAction(e -> handleDrawCardButton());
        Button passButton = new Button("Pass");
        passButton.setOnAction(e -> handlePassButton());
        Button resetButton = new Button("New Game");
        resetButton.setOnAction(e -> handleResetButton());
        Button saveButton = new Button("Save Game");
        saveButton.setOnAction(e -> handleSaveButton());
        Button quitButton = new Button("Quit Game");
        quitButton.setOnAction(e -> handleQuitButton());
        
        buttonVBox.setPadding(new Insets(100));
        buttonVBox.getChildren().addAll(playCardButton, drawCardButton, 
                                        passButton, resetButton, saveButton,
                                        quitButton);
    }

    private void handleQuitButton() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Quit game ? ");
        alert.setHeaderText("You're about to quit game");
        alert.setContentText("Press OK if you confirm to quit");
        if (alert.showAndWait().get() == ButtonType.OK) {
            game.quitProgram();
        }
    }

    private void handleSaveButton() {
        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.setTitle("Save File");
        textInputDialog.setHeaderText("Enter your file name :");
        textInputDialog.setContentText("File Name :");
        textInputDialog.showAndWait().ifPresent(input -> {
            game.file.save(input);
        });
    }
    
    private void handleResetButton() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Start new game ? ");
        alert.setHeaderText("You're about to start a new game");
        alert.setContentText("Press OK if you confirm to start new game");
        if (alert.showAndWait().get() == ButtonType.OK) {
            game.startNewGame();
            refresh();
        }
    }

    private void handlePassButton() {
        String header;
        String content;
        if (game.isPassValid()) {  
            if (game.isDeckEmpty()) {
                game.switchPlayerTurn();
                game.count++;
                game.trickEnd();
                refresh();
            }
            else {
                header = "Deck still have card";
                content = "Please draw a card from deck";
                showErrorMessage(header, content);
                System.out.println ("\nError: Please draw a card from deck");
            }
        }
        else {
            header = "You still have card to play";
            content = "Please play a card";
            showErrorMessage(header, content);
            System.out.println ("\nError: Please play a card");
        }
    }

    private void handleDrawCardButton() {
        if (!game.isDeckEmpty()) {
            game.drawFromDeck();
            refresh();
        }
        else {
            String header = "Error: No available card to draw";
            String content = "Please play a card or pass around";
            showErrorMessage(header, content);
            System.out.println ("\nError: No available card to draw");
        }
        resetSelected();
    }

    private void handlePlayCardButton() {
        game.isCardValid(selectedCard);
        refresh();
    }

    private ImageView setImage (String name) {
        String location = "file:image/card/" + name + ".png";
        Image image = new Image(location);
        ImageView imageView = new ImageView(image);
        imageView.setUserData(name);
        
        imageView.setOnMouseEntered(e -> handleMouseEntered(imageView));
        imageView.setOnMouseExited(e -> handleMouseExit(imageView));
        imageView.setOnMouseClicked(e -> clickOnImage(imageView));
        
        return imageView;
    }

    private void handleMouseEntered (ImageView imageView) {
        // effect when mouse hover
        DropShadow dropShadow = new DropShadow();
        if (imageView != selectedImage)
            imageView.setEffect(dropShadow);
    }

    private void handleMouseExit(ImageView imageView) {
        // remove the hover effect 
        if (imageView != selectedImage) 
            imageView.setEffect(null);
    }

    private void clickOnImage (ImageView imageView) {
        // remove the effect on the previous image
        if (selectedImage != null && imageView != selectedImage) 
            selectedImage.setEffect(null);
        
        ColorAdjust colorAdjust = new ColorAdjust(0.2, 0.2, 0.2, -0.2);
        imageView.setEffect(colorAdjust);
        selectedImage = imageView;  // update the selectedImage
        selectedCard = (String) imageView.getUserData();
    }

    public void updateImage () {
        vBox.getChildren().clear(); // clear previous card
        
        // right vertical player card
        for (int i = 0; i < 4; i++) {
            int size = game.players[i].getDisplayPlayerCards().size();
            String[] cardsList = game.players[i].getDisplayPlayerCards().toArray(new String[size]);
            vBox.getChildren().add(cardExceed(cardsList, i));
        }
    }

    public void updateDeckCenter () {
        hBox.getChildren().clear(); 

        // deck
        StackPane deckPane = new StackPane();
        Rectangle deckContainer = new Rectangle(70,25, Color.CORNFLOWERBLUE);
        Text deckLabel = new Text("Deck");
        deckLabel.setFont(Font.font("Serif", FontWeight.BOLD, 20));
        deckPane.getChildren().addAll(deckContainer, deckLabel);
        VBox vBox1 = new VBox(deckPane);
        vBox1.getChildren().add(getVBox(game.getFirstCardDeck()));
        
        // center
        StackPane centerPane = new StackPane();
        Rectangle centerContainer = new Rectangle(70,25, Color.CORNFLOWERBLUE);
        Text centerLabel = new Text("Center");
        centerLabel.setFont(Font.font("Serif", FontWeight.BOLD, 20));
        centerPane.getChildren().addAll(centerContainer, centerLabel);
        VBox vBox2 = new VBox(centerPane);
        vBox2.getChildren().add(getVBox(game.trick.getCenterDeck()));
        
        hBox.getChildren().addAll(vBox1, vBox2);
    }

    public VBox cardExceed(String[] cardsList, int player) {
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(1));
        int size = cardsList.length;
        int n = 0;
        int limit = 10; // card display in a row should not exceed this limit
        
        if (size % limit == 0) 
            n = size / limit;
        else
            n = (size / limit) + 1;

        for (int i = 0; i < n; i++) {
            int indexPos = i * limit;
            int lengthCopy = limit;
            if ((size % limit != 0) && ((size - limit) < 0)) 
                lengthCopy = size % limit;

            String[] newArr = new String[lengthCopy];
            System.arraycopy(cardsList, indexPos, newArr, 0, lengthCopy);
            vBox.getChildren().add(getHBox(newArr));
            size -= limit;
        }

        vbox.getChildren().add(labelScore(player));
        return vbox;
    }

    public HBox labelScore (int player) {
        // player name with container
        HBox hbox = new HBox(20);
        StackPane playerPane = new StackPane();
        Rectangle playerContainer = new Rectangle(75,25, Color.CORNFLOWERBLUE);
        Text playerLabel = new Text("Player " + (player+1));
        playerLabel.setFont(Font.font("Serif", FontWeight.BOLD, 18));
        if (game.currentPlayer == player+1) 
            playerLabel.setFill(Color.CRIMSON);

        playerPane.getChildren().addAll(playerContainer, playerLabel);

        // player score with container
        StackPane scorePane = new StackPane();
        Circle scoreContainer = new Circle(12, Color.CORNFLOWERBLUE);
        scoreContainer.setStrokeWidth(2);
        Text score = new Text("" + game.players[player].getPlayerScore());
        score.setFont(Font.font("Serif", FontWeight.BOLD, 18));
        scorePane.getChildren().addAll(scoreContainer, score);
        
        hbox.getChildren().addAll(playerPane, scorePane);
        return hbox;
    }

    // player card
    public HBox getHBox(String[] cardsList) {
        HBox hbox = new HBox(10);
        for (int i = 0; i < cardsList.length; i++) {
            ImageView imageView = setImage(cardsList[i]);
            hbox.getChildren().add(imageView);
        }
        return hbox;
    }

    // deck first card
    public VBox getVBox(String card) {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        ImageView imageView;
        if (card != null) 
            imageView = setImage(card);
        else {
            String location = "file:image/blank.png";
            Image image = new Image(location);
            imageView = new ImageView(image);
        }
        vbox.getChildren().add(imageView);
        return vbox;
    }

    // center
    public VBox getVBox(Map<String, Integer> cards) {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        
        for (var entry : cards.entrySet()) {
            ImageView imageView = setImage(entry.getKey());
            HBox hbox = new HBox(20);

            if (entry.getValue() != -1) {   
                StackPane pane = new StackPane();
                Rectangle container = new Rectangle(20,20, Color.ANTIQUEWHITE);
                Text label = new Text(entry.getValue() + "");
                label.setFont(Font.font("Serif", FontWeight.BOLD, 20));
                pane.getChildren().addAll(container, label);
                hbox.getChildren().addAll(imageView, pane);
            }
            else  // no value for the first round lead card
               hbox.getChildren().add(imageView);
        
            vbox.getChildren().add(hbox);
        }

        return vbox;
    }

    public void updateTrickBox () {
        Label label = new Label("Trick " + game.trick.getTrickNumber());
        Rectangle container = new Rectangle(100,25, Color.ANTIQUEWHITE);
        label.setFont(Font.font("Serif", FontWeight.BOLD, 20));
        showTrick.setPadding(new Insets(0, 0, 10, 0));
        showTrick.getChildren().addAll(container, label);
    }
}
