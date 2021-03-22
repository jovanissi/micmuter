package nissi.micmuter

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioManager
import android.media.MediaRecorder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import nissi.micmuter.databinding.ActivityMainBinding
import java.io.File


class MainActivity : AppCompatActivity() {

    lateinit var context: Context
    lateinit var binding : ActivityMainBinding
    private val RECORD_REQUEST_CODE = 101
    private val TAG = "MainActivity"

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        context = this
        binding = ActivityMainBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)

        val audioManager: AudioManager = applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager

        if (!audioManager.isMicrophoneMute) {
            binding.button.text = "Mute mic"
        } else if (audioManager.isMicrophoneMute) {
            binding.button.text = "Un-mute mic"
        }

        binding.button.setOnClickListener {
            if (!audioManager.isMicrophoneMute) {
                audioManager.isMicrophoneMute = true
                binding.button.text = "Un-mute mic"
                Toast.makeText(context, "Mic muted!", Toast.LENGTH_SHORT).show()
            } else {
                audioManager.isMicrophoneMute = false
                binding.button.text = "Mute mic"
                Toast.makeText(context, "Mic un-muted!", Toast.LENGTH_SHORT).show()
            }
        }

//        val recorder = AudioRecord.Builder()
//                .setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION)
//                .build()
//
//        if (!isMicAvailable(recorder)) {
//            recorder.startRecording()
//            recorder.stop()
//            recorder.release()
//        }

        setupPermissions()

        getMicrophoneAvailable(this)

    }

//    private fun isMicAvailable(audioRecord: AudioRecord) : Boolean {
//        audioRecord.startRecording()
//        val isAvailable = audioRecord.recordingState == AudioRecord.RECORDSTATE_RECORDING
//        audioRecord.stop()
//        audioRecord.release()
//        return isAvailable
//    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to record denied")
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                RECORD_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            RECORD_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    Log.i(TAG, "Permission has been denied by user")
                } else {
                    Log.i(TAG, "Permission has been granted by user")
                }
            }
        }
    }

    private fun getMicrophoneAvailable(context: Context): Boolean {
        val recorder = MediaRecorder()
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC)
        recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT)
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT)
        recorder.setOutputFile(File(context.cacheDir, "MediaUtil#micAvailTestFile").absolutePath)
        var available = true
        try {
            recorder.prepare()
        } catch (exception: Exception) {
            available = false
        }
        recorder.release()
        return available
    }
}