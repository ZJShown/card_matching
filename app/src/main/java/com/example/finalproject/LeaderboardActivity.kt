package com.example.finalproject

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.marginTop
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.firebase.database.FirebaseDatabase

class LeaderboardActivity: AppCompatActivity() {
    lateinit var firebase: FirebaseDatabase
    lateinit var tableLayout: TableLayout
    //var adLoaded: Boolean = false
    var usersList = mutableListOf<Pair<String, String>>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)
        firebase = FirebaseDatabase.getInstance()
        val databaseReference = firebase.reference

        tableLayout = findViewById(R.id.tableLayout)


        var usersRef = databaseReference.child("Users")
        usersRef.get().addOnSuccessListener { dataSnapshot ->
            for (child in dataSnapshot.children) {
                Log.w("MainActivity", child.key.toString() + ", " + child.value.toString())
                val username = child.key.toString()
                val score = child.value.toString()
                usersList.add(Pair(username, score))
            }

            //usersList.sortByDescending { it.second.toDouble() }
            usersList.sortBy { it.second.toDouble() }

            var count = 0

            for (user in usersList) {

                if(count == 15){
                    break
                }

                val username = user.first
                val score = user.second

                val tableRow = TableRow(this)
                tableRow.gravity = Gravity.CENTER


                val usernameTextView = TextView(this)
                usernameTextView.text = username
                usernameTextView.textSize = 20f
                usernameTextView.setTextColor(resources.getColor(R.color.charcoal))
                usernameTextView.setBackgroundColor(resources.getColor(R.color.very_light_gray))
                usernameTextView.setPadding(8, 8, 8, 8)
                usernameTextView.setTypeface(null, Typeface.BOLD)

                val scoreTextView = TextView(this)
                scoreTextView.text = score
                scoreTextView.textSize = 20f
                scoreTextView.setTextColor(resources.getColor(R.color.charcoal))
                scoreTextView.setBackgroundColor(resources.getColor(R.color.light_gray))
                scoreTextView.setPadding(8, 8, 8, 8)
                scoreTextView.setTypeface(null, Typeface.BOLD_ITALIC)

                tableRow.addView(usernameTextView)
                tableRow.addView(scoreTextView)

                tableLayout.addView(tableRow)
                count += 1

                if(username == MainActivity.email.substringBefore('@')){
                    scoreTextView.setBackgroundColor(resources.getColor(R.color.match5))
                    usernameTextView.setBackgroundColor(resources.getColor(R.color.match5))
                }
            }

            // Create and add a button at the bottom of the screen
            val bottomButton = Button(this)
            val buttonParams = TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(20, 20, 20, 20)
                gravity = android.view.Gravity.CENTER_HORIZONTAL
                gravity = android.view.Gravity.BOTTOM
            }
            bottomButton.layoutParams = buttonParams
            bottomButton.text = "Play Again"
            bottomButton.textSize = 24f
            bottomButton.typeface = Typeface.create("serif", Typeface.BOLD)
            bottomButton.setTextColor(resources.getColor(R.color.charcoal))
            bottomButton.setBackgroundColor(resources.getColor(R.color.medium_gray))
            bottomButton.setOnClickListener {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            tableLayout.addView(bottomButton)


        }


    }
}