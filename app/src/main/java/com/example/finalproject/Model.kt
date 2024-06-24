package com.example.finalproject

import android.util.Log
import kotlin.random.Random

class Model {

    private val NUM_ROWS : Int = 4
    private val NUM_COLS : Int = 4

    private var matches: Int = 0
    private var score: Double = 0.0
    private var scoreFormatted: String = ""
    private var cards: Array<Array<Int>> = Array(NUM_ROWS) { Array(NUM_COLS) { -1 } }
    private val cardSet = mutableListOf<Int>()
    val allCards = arrayOf(
        R.drawable.card1,
        R.drawable.card2,
        R.drawable.card3,
        R.drawable.card4,
        R.drawable.card5,
        R.drawable.card6,
        R.drawable.card7,
        R.drawable.card8,
        R.drawable.card9,
        R.drawable.card10,
        R.drawable.card11,
        R.drawable.card12,
        R.drawable.card13,
        R.drawable.card14,
        R.drawable.card15,
        R.drawable.card16
    )

    init {
        Log.w("MainActivity", "initialized images")
        initializeImages()
        Log.w("MainActivity", "shuffling cards")
        shuffleCards()
        Log.w("MainActivity", "shuffling cards2")
    }

    // Initialize images hashmap
    private fun initializeImages() {
        while (cardSet.size < NUM_ROWS * NUM_COLS / 2) {
            val card = allCards.random()
            if (!cardSet.contains(card)){
                cardSet.add(card)
            }
        }
    }

    private fun shuffleCards() {
        val cardsList = mutableListOf<Int>()
        for (card in cardSet) {
            cardsList.add(card)
            cardsList.add(card)
        }
        Log.w("MainActivity", "success1")

        cardsList.shuffle()
        Log.w("MainActivity", "success2")

        var index = 0
        Log.w("MainActivity", cardsList.size.toString())
        for (row in 0 until NUM_ROWS) {
            for (col in 0 until NUM_COLS) {
                cards[row][col] = cardsList[index]
                Log.w("MainActivity", index.toString())
                index++
            }
        }
        Log.w("MainActivity", "success3")
    }


    // Check if two cards match
    fun checkMatch(card1: Pair<Int, Int>, card2: Pair<Int, Int>): Boolean {
        val cardType1 = cards[card1.first][card1.second]
        val cardType2 = cards[card2.first][card2.second]
        return cardType1 == cardType2
    }

    // Getters and setters for matches and score
    fun getMatches(): Int {
        return matches
    }

    fun updateMatches() {
        this.matches += 1
    }

    fun getCard(row:Int, col:Int) : Int {
        return cards[row][col]
    }

    fun getScore(): Double {
        return score
    }

    fun getScoreFormatted(): String {
        return scoreFormatted
    }

    fun setScore(scoreString: String) {
        scoreFormatted = scoreString
        val parts = scoreString.split(":")
        val minutes = parts[0].toInt()
        val seconds = parts[1].toInt()
        val millis = parts[2].toInt()
        this.score = minutes * 60 + seconds + millis / 1000.0
    }

    fun gameOver(): Boolean {
        return matches == (NUM_ROWS * NUM_COLS / 2)
    }


}

