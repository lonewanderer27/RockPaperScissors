package com.jay_puzon.rockpaperscissors;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Objects;

public class RPCSQLiteDB extends SQLiteOpenHelper {

    public static final String DB_NAME = "records.db", HISTORY = "HISTORY";
    static final String ROCK = "ROCK";
    static final String PAPER = "PAPER";
    static final String SCISSORS = "SCISSORS";
    public static final String AI = "AI",
            PLAYER = "PLAYER",
            TIE = "TIE",
            COMPUTER_SCORE = "COMPUTER_SCORE",
            PLAYER_SCORE = "PLAYER_SCORE",
            COMPUTER_CHOICE = "COMPUTER_CHOICE",
            PLAYER_CHOICE = "PLAYER_CHOICE",
            WINNER = "WINNER",
            RECORD_ID = "RECORD_ID";

    ArrayList<String> Items;
    ArrayList<Integer> ItemsId;
    ContentValues vals;
    Cursor rs;

    public RPCSQLiteDB(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + HISTORY + "(" + RECORD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COMPUTER_CHOICE + " TEXT, " + PLAYER_CHOICE + " TEXT, " + WINNER + " TEXT, " + COMPUTER_SCORE + " INTEGER, " + PLAYER_SCORE + " INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_NAME);
        onCreate(db);
    }

    boolean IsEmpty() {
        SQLiteDatabase db = this.getReadableDatabase();
        rs = db.rawQuery("SELECT * FROM " + HISTORY, null);
        return rs.getCount() == 0;
    }

    public void ResetScores () {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + HISTORY);
    }

    public int GetPlayerWins () {
        SQLiteDatabase db = this.getReadableDatabase();
        rs = db.rawQuery("SELECT SUM(" + PLAYER_SCORE + ") FROM " + HISTORY, null);
        rs.moveToFirst();
        return rs.getInt(0);
    }

    public int GetComputerWins () {
        SQLiteDatabase db = this.getReadableDatabase();
        rs = db.rawQuery("SELECT SUM(" + COMPUTER_SCORE + ") FROM " + HISTORY, null);
        rs.moveToFirst();
        return rs.getInt(0);
    }

    public boolean AddScore (String winner, String computerChoice, String playerChoice) {
        SQLiteDatabase db = this.getWritableDatabase();
        vals = new ContentValues();
        vals.put(WINNER, winner);
        vals.put(COMPUTER_CHOICE, computerChoice);
        vals.put(PLAYER_CHOICE, playerChoice);
        if (winner.equals(AI)) {
            // add 1 to computer score
            vals.put(COMPUTER_SCORE, 1);
            vals.put(PLAYER_SCORE, 0);

        } else if (winner.equals(PLAYER)) {
            // add 1 to player score
            vals.put(COMPUTER_SCORE, 0);
            vals.put(PLAYER_SCORE, 1);
        } else {
            // do not add any score since tie
            vals.put(COMPUTER_SCORE, 0);
            vals.put(PLAYER_SCORE, 0);
        }
        try {
            db.insert(HISTORY, null, vals);
            return true;
        } catch (SQLException e) {
            Log.e("AddScore", Objects.requireNonNull(e.getMessage()));
            return false;
        }
    }

    @SuppressLint("Range")
    public ArrayList<String> GetRecords() {
        SQLiteDatabase db = this.getReadableDatabase();
        Items = new ArrayList<>();
        ItemsId = new ArrayList<>();
        // get the last result as first
        rs = db.rawQuery("SELECT * FROM " + HISTORY + " ORDER BY " + RECORD_ID + " DESC", null);
        rs.moveToFirst();

        while (!rs.isAfterLast()) {
            do {
                Items.add("Player: " + rs.getString(rs.getColumnIndex(PLAYER_CHOICE)) + " VS AI: " + rs.getString(rs.getColumnIndex(COMPUTER_CHOICE)) + " \nWinner: " + rs.getString(rs.getColumnIndex(WINNER)));
                ItemsId.add(rs.getInt(rs.getColumnIndex(RECORD_ID)));
            } while (rs.moveToNext());
        }

        rs.close();
        return Items;
    }
}
