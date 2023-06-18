package src;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class GoBoom extends Application {
    private Stage primaryStage;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        Game game = new Game();

        // new game
        Button newButton = new Button("New \nGame");
        newButton.setPrefSize(160, 80);
        newButton.setFont(Font.font(20));
        newButton.setOnMouseClicked(e -> {
            game.startNewGame();
            switchScene(game);
        });

        // load game
        Button loadButton = new Button("Load \nGame");
        loadButton.setPrefSize(160, 80);
        loadButton.setFont(Font.font(20));
        loadButton.setOnMouseClicked(e -> {
            TextInputDialog textInputDialog = new TextInputDialog();
            textInputDialog.setTitle("Load File");
            textInputDialog.setHeaderText("Enter your file name :");
            textInputDialog.setContentText("File Name :");
            textInputDialog.showAndWait().ifPresent(input -> {
            game.file.load(input);
            if (!game.file.fileError)
                switchScene(game);
            });
        });

        // image
        Image icon = new Image("file:image/icon.png");
        ImageView imageView = new ImageView(icon);

        // horizontal new game button and load button
        HBox hBox = new HBox(30);
        hBox.getChildren().addAll(newButton, loadButton);

        VBox vBox = new VBox(100);
        vBox.getChildren().addAll(imageView, hBox);
        vBox.setPadding(new Insets(150, 700, 100, 700));
        StackPane pane = new StackPane();
        pane.getStyleClass().add("pane");
        pane.setPrefSize(1000, 200);
        pane.getStyleClass().add("stack-pane");
        pane.getChildren().add(vBox);
        Scene scene = new Scene(pane, 1500, 800);
        String css = getClass().getResource("css/scene.css").toExternalForm();
        scene.getStylesheets().add(css);

        primaryStage.getIcons().add(icon);
        primaryStage.setTitle("Go Boom");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    public void switchScene (Game game) {
        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(10));
        pane.getStyleClass().add("pane");
        
        pane.setTop(game.display.showTrick);
        pane.setRight(game.display.vBox);
        pane.setLeft(game.display.hBox);
        pane.setCenter(game.display.buttonVBox);
        
        Scene scene = new Scene(pane, 1500, 800);
        String css = getClass().getResource("css/scene.css").toExternalForm();
        scene.getStylesheets().add(css);

        primaryStage.setScene(scene);
    }
}
