package com.example.jaweed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

private val allWordsList: List<String> = listOf(
    "android", "applications", "architecture", "scramble",
    "composable", "viewmodel", "lifecycle", "jetpack",
    "kotlin", "stateflow"
)

const val MAX_NO_OF_WORDS = 10

private fun scrambleWord(word: String): String {
    val chars = word.toCharArray()
    var scrambled: String
    do {
        chars.shuffle()
        scrambled = String(chars)
    } while (scrambled == word)
    return scrambled
}

class GameViewModel : ViewModel() {

    private val _currentScrambledWord = MutableLiveData<String>()
    val currentScrambledWord: LiveData<String> = _currentScrambledWord

    private val _score = MutableLiveData(0)
    val score: LiveData<Int> = _score

    private val _currentWordCount = MutableLiveData(0)
    val currentWordCount: LiveData<Int> = _currentWordCount

    private var wordsList = allWordsList.toMutableList()
    private lateinit var currentWord: String

    init {
        getNextWord()
    }

    private fun getNextWord() {
        if (wordsList.isNotEmpty()) {
            currentWord = wordsList.removeAt(0)
            _currentScrambledWord.value = scrambleWord(currentWord)
            _currentWordCount.value = (_currentWordCount.value ?: 0) + 1
        }
    }

    fun isUserWordCorrect(playerWord: String): Boolean {
        return playerWord.equals(currentWord, ignoreCase = true)
    }

    fun nextWord(): Boolean {
        return if (_currentWordCount.value == MAX_NO_OF_WORDS) {
            false
        } else {
            getNextWord()
            true
        }
    }

    fun increaseScore() {
        _score.value = (_score.value ?: 0) + 20
    }

    fun reinitializeData() {
        _score.value = 0
        _currentWordCount.value = 0
        wordsList = allWordsList.toMutableList()
        wordsList.shuffle()
        getNextWord()
    }
}
