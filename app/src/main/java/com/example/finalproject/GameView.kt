package com.example.finalproject

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

class GameView : RelativeLayout {

    private lateinit var buttons: Array<Array<Button>>
    lateinit var gridLayout: GridLayout
    private lateinit var progressBar : ProgressBar
    private lateinit var adView : AdView
    private lateinit var timerView : TextView
    private lateinit var handler: Handler
    private lateinit var updateTimeTask: Runnable

    constructor(cellWidth: Int, cellHeight : Int, context: Context) : super(context) {

        gridLayout = GridLayout(context)
        gridLayout.id = View.generateViewId()

        gridLayout.columnCount = 4
        gridLayout.rowCount = 4
        buttons = Array(4) { Array(4) { Button(context) } }

        for( i in 0 .. buttons.size - 1 ) {
            for( j in 0 .. buttons[i].size - 1 ) {
                buttons[i][j].setBackgroundResource(R.drawable.backimage)
                buttons[i][j].textSize = 48.0f

                gridLayout.addView( buttons[i][j], cellWidth, cellHeight )

            }
        }

        val gridParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        gridParams.addRule(CENTER_HORIZONTAL)
        gridParams.addRule(CENTER_VERTICAL)
        addView(gridLayout, gridParams)

//        val adView = TextView(context)
//        adView.id = View.generateViewId()
//        adView.text = "This is a AD"
//        // Set a background color so you can see the TextView boundaries
//        adView.setBackgroundColor(resources.getColor(android.R.color.holo_blue_light))
//        // Ensure text color contrasts with the background
//        adView.setTextColor(resources.getColor(android.R.color.white))
//        val adParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
//        adParams.addRule(CENTER_HORIZONTAL)
//        adParams.addRule(BELOW, gridLayout.id)
//        // Add some margin to position the TextView below the GridLayout
//        adParams.topMargin = 25
//        addView(adView, adParams)

//        val adView = AdView(context)
//        adView.id = View.generateViewId()
//        val adUnitId = "ca-app-pub-3940256099942544/6300978111"
//        adView.adUnitId = adUnitId
//        val adSize = AdSize( AdSize.FULL_WIDTH, AdSize.AUTO_HEIGHT )
//        adView.setAdSize(adSize)
//        val builder = AdRequest.Builder()
//        builder.addKeyword("workout").addKeyword("fitness")
//        val request = builder.build()
//        adView.loadAd(request)
//
//        val adParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
//        adParams.addRule(CENTER_HORIZONTAL)
//        adParams.addRule(BELOW, gridLayout.id)
//        adParams.topMargin = 25
//        addView(adView, adParams)


        timerView = TextView(context)
        timerView.id = View.generateViewId()
        timerView.text = "00:00" // Initial timer value
        timerView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30f)
        // Set a background color so you can see the TextView boundaries
        // Ensure text color contrasts with the background
        timerView.setTextColor(resources.getColor(android.R.color.black))
        val timerParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        timerParams.addRule(ALIGN_PARENT_LEFT)
        timerParams.addRule(ABOVE, gridLayout.id)
        // Add some margin to position the TextView below the GridLayout
        timerParams.leftMargin = 120
        timerParams.bottomMargin = 40
        addView(timerView, timerParams)

        // Create a handler to update the timer
        handler = Handler(Looper.getMainLooper())

        // Create a Runnable to update the timer TextView
        updateTimeTask = object : Runnable {
            var startTime = System.currentTimeMillis()
            override fun run() {
                // Calculate elapsed time
                val elapsedTime = System.currentTimeMillis() - startTime

                // Calculate minutes, seconds, and milliseconds
                val minutes = (elapsedTime / (1000 * 60)) % 60
                val seconds = (elapsedTime / 1000) % 60
                val millis = elapsedTime % 1000
                val millisStr = String.format("%03d", millis)
                timerView.text = String.format("%02d:%02d:%s", minutes, seconds, millisStr.substring(0, 2))

                // Schedule the next update after 10 milliseconds
                handler.postDelayed(this, 10)
            }
        }

        // Start the timer
        handler.post(updateTimeTask)

        progressBar = ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal)
        val progressBarWidth = 300
        val progressBarHeight = 100
        progressBar.progressDrawable = ContextCompat.getDrawable(context, R.drawable.progress_bar_track)
        progressBar.layoutParams = LayoutParams(progressBarWidth, progressBarHeight)
        progressBar.max = 100 // Set the maximum value for the progress bar (e.g., 100%)
        progressBar.progress = 4 // Set the initial progress value
        val color = ContextCompat.getColor(context, R.color.red)

        progressBar.progressTintMode = PorterDuff.Mode.SRC_IN
        progressBar.progressTintList = ColorStateList.valueOf(color)
        //val progressBarParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        val progressBarParams = LayoutParams(progressBarWidth, progressBarHeight)
        progressBarParams.addRule(ABOVE, gridLayout.id)
        progressBarParams.addRule(ALIGN_PARENT_RIGHT)
        progressBarParams.rightMargin = 120
        progressBarParams.bottomMargin = 40
        addView(progressBar, progressBarParams)

    }

    fun getButtons(): Array<Array<Button>>{
        return buttons
    }

    fun updateProgressBar() {
        progressBar.progress += (progressBar.max-4) / (gridLayout.columnCount * gridLayout.rowCount / 2)
        val colors = arrayOf(R.color.match1, R.color.match2, R.color.match3, R.color.match4, R.color.match5, R.color.match6, R.color.match7, R.color.match8)
        if (progressBar.progress / 12 < colors.size){
            val color = ContextCompat.getColor(context, colors[progressBar.progress / 12])
            progressBar.progressTintMode = PorterDuff.Mode.SRC_IN
            progressBar.progressTintList = ColorStateList.valueOf(color)
        }
    }

    fun getTime() : String {
        handler.removeCallbacks(updateTimeTask)
        return timerView.text.toString()
    }

}