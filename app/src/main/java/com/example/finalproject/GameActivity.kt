package com.example.finalproject

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.*

class GameActivity : AppCompatActivity() {
    lateinit var gameView: GameView
    lateinit var buttons: Array<Array<Button>>
    val matchList = mutableListOf<Pair<Int, Int>>()
    lateinit var firebase: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebase = FirebaseDatabase.getInstance()



        Log.w("MainActivity", "inside GameActivity onCreate")
        var width : Int = resources.displayMetrics.widthPixels
        val cellWidth : Int = width / 5  // Adjust the number to get the desired aspect ratio
        val cellHeight : Int = cellWidth * 2

        Log.w("MainActivity", "initialized model")
        game = Model()

        gameView = GameView(cellWidth, cellHeight, this )
        Log.w("MainActivity", "initialized gameView")
        setContentView(gameView)
        Log.w("MainActivity", "setContentView to gameView")

        buttons = gameView.getButtons()
        Log.w("MainActivity", "initialized buttons in GameActivity")

        var bh : ButtonHandler = ButtonHandler()

        for( i in 0 .. buttons.size - 1 ) {
            for (j in 0..buttons[i].size - 1) {
                buttons[i][j].setOnClickListener( bh )
            }
        }

    }

    fun flipCard(row: Int, col: Int) {
        Log.w("MainActivity", "here")
        val button = buttons[row][col]
        val currBackCard = button.background
        val backGround = ContextCompat.getDrawable(button.context, R.drawable.backimage)
        Log.w("MainActivity", currBackCard.toString() + " == " + backGround.toString())
        Log.w("MainActivity", " Real Background " + R.drawable.backimage.toString())
        Log.w("MainActivity", "here1")
        if (currBackCard?.constantState == backGround?.constantState){
            Log.w("MainActivity", "flipping card")
            button.setBackgroundResource(game.getCard(row, col))
            matchList.add(Pair(row, col))

            if (matchList.size == 2) {
                val (row1, col1) = matchList[0]
                val (row2, col2) = matchList[1]
                val card1 = game.getCard(row1, col1)
                val card2 = game.getCard(row2, col2)

                if (game.checkMatch(matchList[0], matchList[1])) {
                    game.updateMatches()
                    gameView.updateProgressBar()
                    if(game.gameOver()){
                        game.setScore(gameView.getTime())
                        val databaseReference = firebase.reference.child("/Users")
                        Log.w("MainActivity", "Game Over Block 2")
                        Log.w("MainActivity", databaseReference.toString())
                        var highScoreRef = firebase.getReference("/Users/" + MainActivity.email.substringBefore('@'))
                        Log.w("MainActivity", highScoreRef.toString())
                        Log.w("MainActivity", "Before get")
                        highScoreRef.get().addOnSuccessListener { dataSnapshot ->
                            val highScoreValue = dataSnapshot.getValue(Double::class.java)
                            Log.w("MainActivity", "Inside listener")
                            Log.w("MainActivity", highScoreValue.toString())
                            if (highScoreValue != null && highScoreValue > game.getScore()) {
                                Log.w("MainActivity", "Inside if")
                                highScoreRef.setValue(game.getScore())
                            } else if (highScoreValue == null){
                                highScoreRef.setValue(game.getScore())
                            }
                        }

                        Log.w("MainActivity", "Game Over Block 3")
                        //reference.setValue(game.getScore())
                        Log.w("MainActivity", "Game Over Block 4")
                        //go to leaderboard

                        showGameOverPopup(button.context)
                        Log.w("MainActivity", "Game Over Finished")
                    }
                } else {
                    // Cards don't match, flip them back face down or perform any other actions
                    GlobalScope.launch {
                        delay(1000) // Delay for 1 second (1000 milliseconds)
                        withContext(Dispatchers.Main) {
                            buttons[row1][col1].setBackgroundResource(R.drawable.backimage)
                            buttons[row2][col2].setBackgroundResource(R.drawable.backimage)
                        }
                    }
                }

                // Clear the matchList for the next pair of flips
                matchList.clear()
            }
        }
    }

    fun showGameOverPopup(context: Context) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        Log.w("MainActivity", "alertDialogBuilder created")
        alertDialogBuilder.setTitle("You Won!")
        alertDialogBuilder.setMessage("You have completed the game in " + game.getScore() +" seconds!")
        alertDialogBuilder.setPositiveButton("Show Leaderboard") { dialog, _ ->
            // Handle button click if needed
            dialog.dismiss()
            val intent = Intent(this@GameActivity, LeaderboardActivity::class.java)
            startActivity(intent)
            Log.w("MainActivity", "button")
        }
        Log.w("MainActivity", "here?")
        val alertDialog = alertDialogBuilder.create()
        Log.w("MainActivity", "here2")
        alertDialog.show()
        Log.w("MainActivity", "here3")
    }

    inner class ButtonHandler : View.OnClickListener {
        override fun onClick( view : View? ) {
            for( row in 0 .. buttons.size - 1 ) {
                for( col in 0 .. buttons[row].size - 1 ) {
                    if( view != null && view == buttons[row][col] ) {
                        Log.w("MAinActivity", "row " + row + ", col " + col + " was clicked")
                        flipCard(row, col)
                    }
                }
            }
        }

    }

    companion object{
        lateinit var game: Model
    }
}