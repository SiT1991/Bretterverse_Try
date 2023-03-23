package com.example.bretterverse

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.bretterverse.databinding.ActivityFreestyleBinding
import java.util.*
import kotlin.collections.ArrayList

class FreestyleActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    // Views
    private lateinit var binding: ActivityFreestyleBinding

    // SoundPool
    private lateinit var soundPool: SoundPool
    private var soundMap: MutableMap<String, Int> = mutableMapOf()

    // TextToSpeech
    private lateinit var tts: TextToSpeech

    // Freestyle bot
    private lateinit var freestyleBot: FreestyleBot

    private val viewModel: FreestyleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFreestyleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSpinners()

        // Initialize SoundPool
        val audioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .build()
        soundPool = SoundPool.Builder()
            .setMaxStreams(10)
            .setAudioAttributes(audioAttributes)
            .build()

        // Load sound files into SoundPool
        loadSoundFiles()

        // Initialize TextToSpeech
        tts = TextToSpeech(this, this)

        // Initialize FreestyleBot
        val assets = this.assets
        val languageModelPath = "language_models/lyrics_lm.arpa"
        val acousticModelPath = "acoustic_models/lyrics_3e_mdef.ci_cont"
        freestyleBot = FreestyleBot(assets, languageModelPath, acousticModelPath)

        binding.startButton.setOnClickListener {
            val beat = binding.beatSpinner.selectedItem.toString()
            val instrument = binding.instrumentSpinner.selectedItem.toString()
            val textToSpeechEnabled = binding.textToSpeechSwitch.isChecked

            viewModel.startFreestyle(beat, instrument, textToSpeechEnabled)
        }

        viewModel.freestyleResult.observe(this) { result ->
            when (result) {
                is FreestyleResult.Success -> {
                    // Show the result in a dialog
                    showResultDialog(result.score)
                }
                is FreestyleResult.Error -> {
                    Toast.makeText(this, result.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        soundPool.release()
        tts.shutdown()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(this, "Text-to-speech language not supported", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Failed to initialize text-to-speech", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupSpinners() {
        // Set up the beat spinner
        ArrayAdapter.createFromResource(
            this,
            R.array.beat_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.beatSpinner.adapter = adapter

            // Set up the sound spinner
            ArrayAdapter.createFromResource(
                this,
                R.array.sound_array,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.soundSpinner.adapter = adapter
            }
        }

        private fun setupMetronome() {
            // Get the values from the spinners
            val beatValue = binding.beatSpinner.selectedItem.toString().toInt()
            val soundValue = binding.soundSpinner.selectedItem.toString()

            // Create the metronome instance
            metronome = Metronome(beatValue, soundValue)

            // Set up the click listener for the start/stop button
            binding.startStopButton.setOnClickListener {
                if (metronome.isPlaying()) {
                    metronome.stop()
                    binding.startStopButton.text = getString(R.string.start_button_text)
                } else {
                    metronome.start()
                    binding.startStopButton.text = getString(R.string.stop_button_text)
                }
            }
        }
    }