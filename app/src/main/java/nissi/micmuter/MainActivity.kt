package nissi.micmuter

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    lateinit var context: Context

    private lateinit var button : Button

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        context = this

        button = findViewById(R.id.button)

        val audioManager: AudioManager = applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        if (!audioManager.isMicrophoneMute) {
            button.text = "Mute mic"
        } else if (audioManager.isMicrophoneMute) {
            button.text = "Un-mute mic"
        }

        button.setOnClickListener {
            if (!audioManager.isMicrophoneMute) {
                audioManager.isMicrophoneMute = true
                button.text = "Un-mute mic"
                Toast.makeText(context, "Mic muted!", Toast.LENGTH_SHORT).show()
            } else {
                audioManager.isMicrophoneMute = false
                button.text = "Mute mic"
                Toast.makeText(context, "Mic un-muted!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}