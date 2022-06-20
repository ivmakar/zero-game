package ru.ivmak.zerogame

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainViewModel: ViewModel() {

    private val _items = MutableStateFlow<MutableList<Number>>(arrayListOf())
    val items: StateFlow<List<Number>> = _items

    private val _events = MutableSharedFlow<Event>()
    val events: Flow<Event> = _events

    sealed class Event {
        data class ClickEvent(val number: Number): Event()
        object GameEndedEvent: Event()
        data class NextTeamEvent(val number: Int): Event()
    }

    val count = 50
    val zeroPercentage = 15
    val start = 1
    val end = 1000

    val teamCount = 2

    private val _curTeam = MutableStateFlow(0)
    val curTeam: StateFlow<Int> = _curTeam

    private val _curTeamScore = MutableStateFlow(0)
    val curTeamScore: StateFlow<Int> = _curTeamScore

    private val _teamScores = MutableStateFlow<List<Int>>(arrayListOf(0, 0, 0, 0, 0))
    val teamScores: StateFlow<List<Int>> = _teamScores

    private val _isScoreVisible = MutableStateFlow(false)
    val isScoreVisible: StateFlow<Boolean> = _isScoreVisible

    fun setScoreVisible() {
        viewModelScope.launch {
            _isScoreVisible.emit(!_isScoreVisible.value)
        }
    }


    fun generateNumbers() {
        val list = mutableListOf<Number>()
        val numCount = ((count.toDouble() / 100.0) * (100 - zeroPercentage)).toInt()
        val random = Random(System.currentTimeMillis())

        Log.d(TAG, "generateNumbers: numCount: $numCount")

        viewModelScope.launch(Dispatchers.IO) {

            repeat(numCount) { curId ->
                val value = random.nextInt(start, end + 1)
                Log.d(TAG, "generateNumbers: value = $value")
                list.add(Number(
                    id = curId,
                    number = value,
                    onClick = {
                        numberClicked(curId)
                    }
                ))
            }

            repeat(count - numCount) { curId ->
                list.add(Number(
                    id = numCount + curId,
                    number = 0,
                    onClick = {
                        numberClicked(numCount + curId)
                    }
                ))
            }

            // Преремешивание
            repeat(count - 1) {
                val id = random.nextInt(0, count - it - 1)
                val value = list[id]
                list[id] = list[count - it - 1]
                list[count - it - 1] = value
            }

            Log.d(TAG, "generateNumbers: $list")

            _items.emit(list)
        }
    }

    private fun numberClicked(id: Int) {
        val item = _items.value.find { it.id == id }
        _items.value.remove(item)
        if (item == null) return
        viewModelScope.launch {
            _events.emit(Event.ClickEvent(item))
            if (item.number == 0) {
                _curTeamScore.emit(0)
            } else {
                _curTeamScore.emit(_curTeamScore.value + item.number)
            }
        }
    }

    fun nextTeam() {
        viewModelScope.launch {
            val scores = _teamScores.value.toMutableList()
            scores[_curTeam.value] += _curTeamScore.value
            _teamScores.emit(scores)
            _curTeamScore.emit(0)
            val curT = (_curTeam.value + 1) % teamCount
            _curTeam.emit(curT)
            _events.emit(Event.NextTeamEvent(curT))
        }
    }

    fun endGame() {
        viewModelScope.launch {
            val scores = _teamScores.value.toMutableList()
            scores[_curTeam.value] += _curTeamScore.value
            _teamScores.emit(scores)
            _curTeamScore.emit(0)
            _events.emit(Event.GameEndedEvent)
        }
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}