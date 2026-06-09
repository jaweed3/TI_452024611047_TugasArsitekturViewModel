package com.example.jaweed

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

private val allWords = listOf(
    "android", "applications", "architecture", "scramble",
    "composable", "viewmodel", "lifecycle", "jetpack",
    "kotlin", "stateflow"
)

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

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private var wordList = allWords.toMutableList()
    private lateinit var currentWord: String

    init {
        resetGame()
    }

    fun submitWord(guess: String) {
        if (guess.equals(currentWord, ignoreCase = true)) {
            val newScore = _uiState.value.score + 20
            updateNextWord(newScore)
        } else {
            _uiState.update { it.copy(isGuessedWordWrong = true) }
        }
    }

    fun skipWord() {
        updateNextWord(_uiState.value.score)
    }

    fun resetGame() {
        wordList = allWords.toMutableList()
        wordList.shuffle()
        _uiState.update { GameUiState() }
        pickNextWord()
    }

    private fun updateNextWord(newScore: Int) {
        if (_uiState.value.currentWordCount == allWords.size) {
            _uiState.update {
                it.copy(
                    score = newScore,
                    isGuessedWordWrong = false,
                    isGameOver = true
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    score = newScore,
                    currentWordCount = it.currentWordCount + 1,
                    isGuessedWordWrong = false
                )
            }
            pickNextWord()
        }
    }

    private fun pickNextWord() {
        if (wordList.isNotEmpty()) {
            currentWord = wordList.removeAt(0)
            _uiState.update {
                it.copy(currentScrambledWord = scrambleWord(currentWord))
            }
        }
    }
}
