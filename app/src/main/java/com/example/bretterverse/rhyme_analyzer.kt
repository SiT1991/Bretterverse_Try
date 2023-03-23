package com.example.bretterverse

import kotlin.math.abs

class RhymeAnalyzer {
    companion object {
        private val VOWELS = setOf('a', 'e', 'i', 'o', 'u')

        fun getRhymeScore(word1: String, word2: String): Int {
            val lastVowel1 = getLastVowelIndex(word1)
            val lastVowel2 = getLastVowelIndex(word2)

            if (lastVowel1 == -1 || lastVowel2 == -1) {
                return 0
            }

            return if (word1.substring(lastVowel1) == word2.substring(lastVowel2)) {
                1
            } else {
                0
            }
        }

        private fun getLastVowelIndex(word: String): Int {
            var index = -1

            for (i in word.length - 1 downTo 0) {
                if (word[i].toLowerCase() in VOWELS) {
                    index = i
                    break
                }
            }

            return index
        }
    }
}
