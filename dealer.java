/*
 * 13/AUG/2026
 * Dealer
 * This portion houses all of the code
 */

// Notes:

/*
 * Add double opportunity.
 * Fix game start not allowing 'Yes' after failing first auth.
 * Find a way to have Ace resemeble either 1 or 11 depending on the user's total.
 * Integrate a UI. ✓
 * Integrate a betting/currency system.
 * Integrate insurance.
 */


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.Random;

public class dealer extends Application {
    // Game variables
    private String[] card;
    private Random generator;
    private int playerTotal;
    private int dealerTotal;
    private boolean gameInProgress;
    private boolean gameEnded;

    // UI components
    private Label titleLabel;
    private Label dealerInfoLabel;
    private Label dealerCardLabel;
    private Label playerInfoLabel;
    private Label playerCardLabel;
    private Label messageLabel;
    private Button playButton;
    private Button hitButton;
    private Button standButton;
    private Button newGameButton;
    private VBox mainLayout;

    @Override
    public void start(Stage stage) {
        // Initialize game
        initializeGame();

        // Create UI
        createUI();

        // Create scene and show window
        Scene scene = new Scene(mainLayout, 600, 500);
        stage.setTitle("Blackjack Dealer");
        stage.setScene(scene);
        stage.show();

        // Show welcome screen
        showWelcomeScreen();
    }

    private void initializeGame() {
        card = new String[14];
        card[0] = "Ace";
        card[1] = "1";
        card[2] = "2";
        card[3] = "3";
        card[4] = "4";
        card[5] = "5";
        card[6] = "6";
        card[7] = "7";
        card[8] = "8";
        card[9] = "9";
        card[10] = "10";
        card[11] = "Jack";
        card[12] = "Queen";
        card[13] = "King";

        generator = new Random();
        playerTotal = 0;
        dealerTotal = 0;
        gameInProgress = false;
        gameEnded = false;
    }

    private void createUI() {
        mainLayout = new VBox(10);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setStyle("-fx-background-color: #2d5016;");

        // Title
        titleLabel = new Label("Welcome to Blackjack!");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titleLabel.setStyle("-fx-text-fill: white;");

        // Dealer Info
        dealerInfoLabel = new Label("Dealer's Hand:");
        dealerInfoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        dealerInfoLabel.setStyle("-fx-text-fill: white;");

        dealerCardLabel = new Label("");
        dealerCardLabel.setFont(Font.font("Arial", 14));
        dealerCardLabel.setStyle("-fx-text-fill: white;");

        VBox dealerSection = new VBox(5);
        dealerSection.getChildren().addAll(dealerInfoLabel, dealerCardLabel);

        // Player Info
        playerInfoLabel = new Label("Your Hand:");
        playerInfoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        playerInfoLabel.setStyle("-fx-text-fill: white;");

        playerCardLabel = new Label("");
        playerCardLabel.setFont(Font.font("Arial", 14));
        playerCardLabel.setStyle("-fx-text-fill: white;");

        VBox playerSection = new VBox(5);
        playerSection.getChildren().addAll(playerInfoLabel, playerCardLabel);

        // Message
        messageLabel = new Label("");
        messageLabel.setFont(Font.font("Arial", 14));
        messageLabel.setStyle("-fx-text-fill: #ffff00;");
        messageLabel.setWrapText(true);

        // Buttons
        playButton = new Button("Play");
        playButton.setStyle("-fx-font-size: 14; -fx-padding: 10;");
        playButton.setOnAction(e -> startNewGame());

        hitButton = new Button("Hit");
        hitButton.setStyle("-fx-font-size: 14; -fx-padding: 10;");
        hitButton.setOnAction(e -> handleHit());
        hitButton.setDisable(true);

        standButton = new Button("Stand");
        standButton.setStyle("-fx-font-size: 14; -fx-padding: 10;");
        standButton.setOnAction(e -> handleStand());
        standButton.setDisable(true);

        newGameButton = new Button("New Game");
        newGameButton.setStyle("-fx-font-size: 14; -fx-padding: 10;");
        newGameButton.setOnAction(e -> newGame());
        newGameButton.setDisable(true);

        // Button layout
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(playButton, hitButton, standButton, newGameButton);

        // Add all to main layout
        mainLayout.getChildren().addAll(
            titleLabel,
            new Label(""),  // Spacer
            dealerSection,
            new Label(""),  // Spacer
            playerSection,
            messageLabel,
            buttonBox
        );
    }

    private void showWelcomeScreen() {
        dealerCardLabel.setText("Waiting to start...");
        playerCardLabel.setText("Waiting to start...");
        messageLabel.setText("Would you like to play a game of Blackjack?");
    }

    private void startNewGame() {
        playerTotal = 0;
        dealerTotal = 0;
        gameInProgress = true;
        gameEnded = false;

        messageLabel.setText("Great, let's begin!");
        playButton.setDisable(true);

        // Deal first card to player
        int cardIndex = generator.nextInt(card.length);
        String playerCard = card[cardIndex];
        playerTotal += getCardValue(playerCard);

        // Deal first card to dealer
        int dealerCardIndex = generator.nextInt(card.length);
        String dealerCard = card[dealerCardIndex];
        dealerTotal += getCardValue(dealerCard);

        // Display cards
        dealerCardLabel.setText("Dealer's Card: " + dealerCard + "\nDealer's Total: " + dealerTotal);
        playerCardLabel.setText("Your Card: " + playerCard + "\nYour Total: " + playerTotal);

        // Enable Hit and Stand buttons
        hitButton.setDisable(false);
        standButton.setDisable(false);
        messageLabel.setText("Hit or Stand?");
    }

    private void handleHit() {
        int nextCardIndex = generator.nextInt(card.length);
        String newCard = card[nextCardIndex];
        playerTotal += getCardValue(newCard);

        // Update display
        playerCardLabel.setText(playerCardLabel.getText() + "\nYou drew: " + newCard + "\nYour Total: " + playerTotal);

        if (playerTotal > 21) {
            endGame("Bust! You exceeded 21. Dealer wins!");
        } else if (playerTotal == 21) {
            endGame("Blackjack! You win!");
        }
    }

    private void handleStand() {
        messageLabel.setText("You stand. Dealer is playing...");
        hitButton.setDisable(true);
        standButton.setDisable(true);

        // Dealer logic
        while (dealerTotal < 17) {
            int dealerCardIndex = generator.nextInt(card.length);
            String dealerCard = card[dealerCardIndex];
            dealerTotal += getCardValue(dealerCard);
            dealerCardLabel.setText(dealerCardLabel.getText() + "\nDealer drew: " + dealerCard + "\nDealer's Total: " + dealerTotal);
        }

        // Determine winner
        if (dealerTotal > 21) {
            endGame("Dealer busts! You win!");
        } else if (dealerTotal == 21) {
            endGame("Dealer has Blackjack! Dealer wins!");
        } else if (playerTotal > dealerTotal) {
            endGame("You win! Your " + playerTotal + " beats Dealer's " + dealerTotal);
        } else if (playerTotal < dealerTotal) {
            endGame("Dealer wins! Dealer's " + dealerTotal + " beats Your " + playerTotal);
        } else {
            endGame("Push! Both have " + playerTotal);
        }
    }

    private void endGame(String result) {
        gameEnded = true;
        gameInProgress = false;
        messageLabel.setText(result + " - Click 'New Game' to play again.");
        hitButton.setDisable(true);
        standButton.setDisable(true);
        newGameButton.setDisable(false);
    }

    private void newGame() {
        playerTotal = 0;
        dealerTotal = 0;
        gameInProgress = false;
        gameEnded = false;

        dealerCardLabel.setText("Waiting to start...");
        playerCardLabel.setText("Waiting to start...");
        messageLabel.setText("Click 'Play' to start a new game!");

        playButton.setDisable(false);
        hitButton.setDisable(true);
        standButton.setDisable(true);
        newGameButton.setDisable(true);
    }

    public static int getCardValue(String cardName) {
        switch (cardName) {
            case "Ace":
                return 11;
            case "Jack":
            case "Queen":
            case "King":
                return 10;
            default:
                return Integer.parseInt(cardName);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

