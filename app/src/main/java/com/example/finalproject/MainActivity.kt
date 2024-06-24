package com.example.finalproject

import EmailService
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.content.SharedPreferences
import android.text.Editable
import android.util.Log
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {
    lateinit var usernameVal: EditText
    lateinit var code: EditText
    lateinit var warning: TextView
    val emailService: EmailService = EmailService()
    var newUser : Boolean = false
    var validCode : Boolean = false
    var previousUser : String? = ""
    var adLoaded: Boolean = false
    lateinit var relativeLayout: RelativeLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        relativeLayout = findViewById(R.id.relativeLayout)
        usernameVal = findViewById(R.id.username)
        warning = findViewById(R.id.emailWarning)
        warning.visibility = View.INVISIBLE
        previousUser = readData(this, USERNAME)


        val adView = AdView(this)
        adView.id = View.generateViewId()
        val adUnitId = "ca-app-pub-3940256099942544/6300978111"
        adView.adUnitId = adUnitId
        val adSize = AdSize(AdSize.FULL_WIDTH, AdSize.AUTO_HEIGHT)
        adView.setAdSize(adSize)
        val builder = AdRequest.Builder()
        builder.addKeyword("game").addKeyword("cards")
        val request = builder.build()

        val adParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )

        adParams.addRule(RelativeLayout.BELOW, R.id.play) // Set the ad to be below the username EditText
        adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
        adParams.topMargin = 20

        adView.layoutParams = adParams

        //linearLayout.addView(adView)
        relativeLayout.addView(adView)

        adView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                super.onAdLoaded()
                adLoaded = true
                Log.d("AdLoadStatus", "Ad loaded successfully")
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                super.onAdFailedToLoad(loadAdError)
                // Ad failed to load
                Log.e("AdLoadStatus", "Ad failed to load: ${loadAdError.message}")
            }
        }

        adView.loadAd(request)

        code = findViewById(R.id.code)

        if (!previousUser.isNullOrBlank()) {
            usernameVal.text = Editable.Factory.getInstance().newEditable(previousUser)
            //code.text = Editable.Factory.getInstance().newEditable(emailService.getCode())
            code.text = Editable.Factory.getInstance().newEditable("Press Play")
            validCode = true
        }
        else {
            newUser = true
        }



    }

     fun play(view: View) {
        email = usernameVal.text.toString().trim()
         Log.w("MainActivity", email + " == " + previousUser + " ?")
         if (!previousUser.isNullOrBlank() && previousUser!!.trim() == email) {
             if (!adLoaded){
                 warning.text = "WAIT FOR AD TO LOAD"
                 warning.visibility = View.VISIBLE
             }
             else{
                 saveData(this@MainActivity, USERNAME, usernameVal.text.toString())
                 val intent = Intent(this@MainActivity, GameActivity::class.java)
                 startActivity(intent)
                 warning.visibility = View.INVISIBLE
                 return
             }
         }
        var emailSent : Boolean = false
         validCode = (!code.text.isEmpty() && code.text.toString().trim() == emailService.getCode())
         Log.w("MainActivity", "Play button clicked!")
         Log.w("MainActivity", "Valid Code: " + code.text.toString() + " == " + emailService.getCode() + " ? " + validCode)

         if (validCode && adLoaded){
             if (newUser){
                 lifecycleScope.launch {
                     Log.w("MainActivity", "Inside lifecycle scope")
                     emailSent = withContext(Dispatchers.IO) {
                         Log.w("MainActivity", "inside withContext")
                         emailService.sendWelcomeEmail(email)
                     }
                     Log.w("MainActivity", "emailSent finished attempting")

                     Log.w("MainActivity", "emailSent: " + emailSent.toString())

                     if (emailSent) {
                             saveData(this@MainActivity, USERNAME, usernameVal.text.toString())
                             val intent = Intent(this@MainActivity, GameActivity::class.java)
                             startActivity(intent)
                             warning.visibility = View.INVISIBLE

                     } else {
                         warning.text = "MUST ENTER A VALID EMAIL"
                         warning.visibility = View.VISIBLE
                     }

                 }
             }
             else {
                 saveData(this@MainActivity, USERNAME, usernameVal.text.toString())
                 val intent = Intent(this@MainActivity, GameActivity::class.java)
                 startActivity(intent)
                 warning.visibility = View.INVISIBLE
             }
         }
         else{
             if (!adLoaded){
                 warning.text = "WAIT FOR AD TO LOAD"
             }
             else{
                 warning.text = "INCORRECT CODE ENTERED"
             }

             warning.visibility = View.VISIBLE
         }

    }

    fun sendCode(view: View) {
        code = findViewById(R.id.code)
        email = usernameVal.text.toString().trim()
        var emailSent : Boolean = false
        Log.w("MainActivity", "Play button clicked!")

        lifecycleScope.launch {
            Log.w("MainActivity", "Inside lifecycle scope")
            emailSent = withContext(Dispatchers.IO) {
                Log.w("MainActivity", "inside withContext")
                emailService.sendCodeEmail(email)
            }
            Log.w("MainActivity", "emailSent finished attempting")

            Log.w("MainActivity", "emailSent: " + emailSent.toString())

            if (emailSent) {
                warning.visibility = View.INVISIBLE
            } else {
                warning.visibility = View.VISIBLE
            }

        }
    }


    fun saveData(context: Context, key: String, value: String) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(context.packageName + "_preferences", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.commit()
    }


    fun readData(context: Context, key: String): String? {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(context.packageName + "_preferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString(key, null)
    }

//    private fun flipCard(row: Int, col: Int, button: Button) {
//        // Randomly select a front image
//        val frontImage = model.cardImages[row][col]
//        // Set the button background to the selected front image
//        button.setBackgroundResource(frontImage)
//        // Disable the button to prevent further clicks on it
//        button.isEnabled = false
//    }

    companion object{
        private const val USERNAME: String = "username"
        lateinit var email : String
    }




}