import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import java.util.*
import android.util.Log

class EmailService {
    private val host: String = "smtp.gmail.com"
    private val port: String = "587"
    private val username: String = "cmsc436matchinggame@gmail.com"
    private val password: String = "grvq syip ykgx vyju"//"CMSC436password!"
    private val code : String = generateRandom4DigitString()

    suspend fun sendWelcomeEmail(recipient: String): Boolean {
        val subject : String = "Welcome to the CMSC436 Matching Game!"
        val messageText: String = "Good luck and have fun!"
        Log.w("MainActivity", "inside sendEmail")
        return withContext(Dispatchers.IO) {
            try {
                val properties = Properties().apply {
                    put("mail.smtp.auth", "true")
                    put("mail.smtp.starttls.enable", "true")
                    put("mail.smtp.host", host)
                    put("mail.smtp.port", port)
                }

                val session = Session.getInstance(properties, object : Authenticator() {
                    override fun getPasswordAuthentication(): PasswordAuthentication {
                        return PasswordAuthentication(username, password)
                    }
                })

                val message = MimeMessage(session).apply {
                    setFrom(InternetAddress(username))
                    addRecipient(Message.RecipientType.TO, InternetAddress(recipient))
                    setSubject(subject)
                    setText(messageText)
                }

                Transport.send(message)
                true // Email sent successfully
            } catch (e: Exception) {
                Log.w("MainActivity", "Error: " + e.toString())
                e.printStackTrace()
                false // Email sending failed
            }
        }
    }

    suspend fun sendCodeEmail(recipient: String): Boolean {
        val subject : String = "CMSC436 Matching Game Code"
        val messageText: String = "Your code is: " + code
        Log.w("MainActivity", "inside sendEmail")
        return withContext(Dispatchers.IO) {
            try {
                val properties = Properties().apply {
                    put("mail.smtp.auth", "true")
                    put("mail.smtp.starttls.enable", "true")
                    put("mail.smtp.host", host)
                    put("mail.smtp.port", port)
                }

                val session = Session.getInstance(properties, object : Authenticator() {
                    override fun getPasswordAuthentication(): PasswordAuthentication {
                        return PasswordAuthentication(username, password)
                    }
                })

                val message = MimeMessage(session).apply {
                    setFrom(InternetAddress(username))
                    addRecipient(Message.RecipientType.TO, InternetAddress(recipient))
                    setSubject(subject)
                    setText(messageText)
                }

                Transport.send(message)
                true // Email sent successfully
            } catch (e: Exception) {
                Log.w("MainActivity", "Error: " + e.toString())
                e.printStackTrace()
                false // Email sending failed
            }
        }
    }

    fun generateRandom4DigitString(): String {
        val random = Random(System.currentTimeMillis())
        val randomDigits = List(4) { random.nextInt(10) } // Generate 4 random digits (0-9)
        return randomDigits.joinToString("") // Convert the list of digits to a string
    }

    fun getCode(): String {
        return code
    }
}
