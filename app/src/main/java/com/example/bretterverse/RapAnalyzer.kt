package com.example.bretterverse

import android.util.Log

class RapAnalyzer(private val rapLyrics: String) {

    private val words: List<String> = rapLyrics
        .toLowerCase()
        .split("""\s+""".toRegex())

    fun getWordCount(): Int {
        return words.size
    }

    fun getUniqueWordCount(): Int {
        return words.toSet().size
    }

    fun getMostCommonWords(count: Int): List<Pair<String, Int>> {
        val wordCountMap = mutableMapOf<String, Int>()

        for (word in words) {
            wordCountMap[word] = wordCountMap.getOrDefault(word, 0) + 1
        }

        return wordCountMap
            .toList()
            .sortedByDescending { (_, count) -> count }
            .take(count)
    }

    fun getWordFrequency(word: String): Int {
        return words.count { it == word }
    }
}

class LyricsNotFoundException(message: String) : Exception(message)

class RapAnalyzerController(private val rapAnalyzer: RapAnalyzer) {

    fun displayWordCount() {
        Log.d(TAG, "Word count: ${rapAnalyzer.getWordCount()}")
    }

    fun displayUniqueWordCount() {
        Log.d(TAG, "Unique word count: ${rapAnalyzer.getUniqueWordCount()}")
    }

    fun displayMostCommonWords(count: Int) {
        Log.d(TAG, "Most common words:")

        val mostCommonWords = rapAnalyzer.getMostCommonWords(count)

        for ((word, frequency) in mostCommonWords) {
            Log.d(TAG, "$word ($frequency)")
        }
    }

    fun displayWordFrequency(word: String) {
        val frequency = rapAnalyzer.getWordFrequency(word)
        Log.d(TAG, "Frequency of $word: $frequency")
    }

    companion object {
        private const val TAG = "RapAnalyzerController"
    }
}
