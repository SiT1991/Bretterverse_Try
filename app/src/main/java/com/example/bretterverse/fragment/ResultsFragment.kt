package com.example.bretterverse
import com.example.bretterverse.databinding.FragmentResultsBinding
import android.content.ContentValues
import android.content.Intent
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.bretterverse.databinding.FragmentResultsBinding
import com.example.bretterverse.databinding.RecordDialogBinding
import java.io.File
import java.io.IOException

class ResultsFragment : Fragment() {

    private lateinit var binding: FragmentResultsBinding
    private var _binding: FragmentResultsBinding? = null
    private val binding get() = _binding!!

    private lateinit var mediaRecorder: MediaRecorder
    private lateinit var audioFile: File

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rhymeResult = arguments?.getString("rhyme_result")
        val syllableResult = arguments?.getString("syllable_result")

        binding.rhymeResultText.text = rhymeResult
        binding.syllableResultText.text = syllableResult

        binding.recordButton.setOnClickListener {
            recordAudio()
        }

        binding.analyzeButton.setOnClickListener {
            analyzeAudio()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        binding = FragmentResultsBinding.inflate(inflater, container, false)
        return binding.root

    }

    private fun recordAudio() {
        val recordDialogBinding = RecordDialogBinding.inflate(layoutInflater)

        val recordDialog = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setView(recordDialogBinding.root)
            .create()

        recordDialogBinding.startButton.setOnClickListener {
            startRecording()
            recordDialog.dismiss()
        }

        recordDialogBinding.cancelButton.setOnClickListener {
            recordDialog.dismiss()
        }

        recordDialog.show()
    }

    private fun startRecording() {
        try {
            mediaRecorder = MediaRecorder()
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            mediaRecorder.setOutputFile(audioFilePath)
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            mediaRecorder.prepare()
            mediaRecorder.start()
            binding.recordButton.backgroundTintList =
                ContextCompat.getColorStateList(requireContext(), R.color.red)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun analyzeAudio() {
        if (!::audioFile.isInitialized || !audioFile.exists()) {
            Toast.makeText(requireContext(), "Please record audio first.", Toast.LENGTH_SHORT).show()
            return
        }

        val audioUri = Uri.fromFile(audioFile)
        val audioInputStream = requireContext().contentResolver.openInputStream(audioUri)
        val rhymeAnalyzer = RhymeAnalyzer()
        val result = rhymeAnalyzer.analyze(audioInputStream)

        val action = ResultsFragmentDirections.actionResultsFragmentToAnalysisFragment(result)
        findNavController().navigate(action)
    }

    private val audioFilePath: String
        get() {
            val storageDir = requireContext().getExternalFilesDir(null)
            if (!storageDir?.exists()!!) storageDir.mkdir()
            audioFile = File.createTempFile("audio_", ".3gp", storageDir)
            return audioFile.absolutePath
        }
}
