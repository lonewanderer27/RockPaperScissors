package com.jay_puzon.rockpaperscissors;

import static com.jay_puzon.rockpaperscissors.RPCSQLiteDB.AI;
import static com.jay_puzon.rockpaperscissors.RPCSQLiteDB.PLAYER;
import static com.jay_puzon.rockpaperscissors.RPCSQLiteDB.TIE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RPCMainActivity extends AppCompatActivity {
    RPCSQLiteDB db = new RPCSQLiteDB(this);
    String playerChoice;
    int playerScore = 0;
    String computerChoice;
    int computerScore = 0;
    static final String ROCK = "ROCK";
    static final String PAPER = "PAPER";
    static final String SCISSORS = "SCISSORS";
    String winner = "";
    String[] choices = {ROCK, PAPER, SCISSORS};

    ImageView dispHand;
    TextView winsText;
    TextView loseText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rps_activity_main);

        // assign all ui elements
        dispHand = findViewById(R.id.dispHands);
        ImageView rockButton = findViewById(R.id.rock);
        ImageView paperButton = findViewById(R.id.paper);
        ImageView scissorsButton = findViewById(R.id.scissors);
        winsText = findViewById(R.id.win);
        loseText = findViewById(R.id.lose);
        Button playButton = findViewById(R.id.btnPlay);
        Button historyButton = findViewById(R.id.btnHistory);

        rockButton.setOnClickListener(v -> {
            // set the player's choice
            playerChoice = ROCK;

            choiceFunc();
        });

        paperButton.setOnClickListener(v -> {
            // set the player's choice
            playerChoice = PAPER;

            choiceFunc();
        });

        scissorsButton.setOnClickListener(v -> {
            // set the player's choice
            playerChoice = SCISSORS;

            choiceFunc();
        });

        playButton.setOnClickListener(v -> {
            // check if the user has already chosen a hand
            if (!playChecks()) {
                return;
            }

            // get the computer's choice
            getComputerChoice();

            // determine the winner
            determineWinner();

            // display the winner
            displayWinner();

            // save the game to the database
            saveToDB();

            // update the scores ui
            updateScoresUI();
        });

        historyButton.setOnClickListener(v -> {
            if (db.IsEmpty()) {
                Toast.makeText(this, "No history yet", Toast.LENGTH_SHORT).show();
                return;
            }

            // show the history
            Intent CR = new Intent(this, RPCRecords.class);
            startActivity(CR);
        });

        // update the scores ui for first time opening
        updateScoresUI();
    }

    void choiceFunc() {
        // update the displayed hand
        updateDispHand();
    }

    void updateDispHand() {
        // update the displayed hand

        switch (playerChoice) {
            case SCISSORS:
                dispHand.setImageResource(R.drawable.scissor);
                break;
            case PAPER:
                dispHand.setImageResource(R.drawable.paper);
                break;
            case ROCK:
                dispHand.setImageResource(R.drawable.rock);
                break;
        }
    }

    boolean playChecks() {
        // check if the user has already chosen a hand
        if (playerChoice == null) {
            Toast.makeText(this, "Please choose a hand", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    void saveToDB() {
        // save the game to the database
        db.AddScore(winner, computerChoice, playerChoice);
    }

    void getComputerChoice() {
        // get the computer's choice
        int randomIndex = (int) (Math.random() * 3);
        computerChoice = choices[randomIndex];
    }

    void updateScoresUI() {
        // update the scores ui
        loseText.setText("Lose: " + db.GetComputerWins());
        winsText.setText("Win: " + db.GetPlayerWins());
    }

    void displayWinner() {
        String prefix = "Player: " + playerChoice + " VS AI: " + computerChoice;

        // display the winner
        if (winner.equals(AI)) {
            Toast.makeText(this, prefix + " = WINNER: " + AI, Toast.LENGTH_LONG).show();
        } else if (winner.equals(PLAYER)) {
            Toast.makeText(this, prefix + " = WINNER: " + PLAYER, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, prefix + " = WINNER: " + TIE, Toast.LENGTH_SHORT).show();
        }
    }

    void determineWinner() {
        // determine the winner
        if (playerChoice.equals(computerChoice)) {
            // tie so no score
            winner = TIE;
        } else if (playerChoice.equals(ROCK)) {
            if (computerChoice.equals(PAPER)) {
                // computer wins
                computerScore++;
                winner = AI;
            } else {
                // player wins
                playerScore++;
                winner = PLAYER;
            }
        } else if (playerChoice.equals(PAPER)) {
            if (computerChoice.equals(SCISSORS)) {
                // computer wins
                computerScore++;
                winner = AI;
            } else {
                // player wins
                playerScore++;
                winner = PLAYER;
            }
        } else if (playerChoice.equals(SCISSORS)) {
            if (computerChoice.equals(ROCK)) {
                // computer wins
                computerScore++;
                winner = AI;
            } else {
                // player wins
                playerScore++;
                winner = PLAYER;
            }
        }
    }
}