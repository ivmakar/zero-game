package ru.ivmak.zerogame

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.ivmak.zerogame.SettingsFragment.Companion.DEFAULT_RANGE_0
import ru.ivmak.zerogame.SettingsFragment.Companion.DEFAULT_RANGE_1
import ru.ivmak.zerogame.SettingsFragment.Companion.DEFAULT_RANGE_2
import ru.ivmak.zerogame.SettingsFragment.Companion.DEFAULT_RANGE_3
import ru.ivmak.zerogame.SettingsFragment.Companion.DEFAULT_RANGE_4
import ru.ivmak.zerogame.SettingsFragment.Companion.DEFAULT_TEAM_COUNT
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

    var dataStore: DataStore<Preferences>? = null
        set(value) {
            field = value
            viewModelScope.launch {
                dataStore?.data?.collect { pref ->
                    val r1 = pref[SettingsFragment.PREF_RANGE_1] ?: DEFAULT_RANGE_1
                    val r2 = pref[SettingsFragment.PREF_RANGE_2] ?: DEFAULT_RANGE_2
                    val r3 = pref[SettingsFragment.PREF_RANGE_3] ?: DEFAULT_RANGE_3
                    val r4 = pref[SettingsFragment.PREF_RANGE_4] ?: DEFAULT_RANGE_4
                    val r0 = pref[SettingsFragment.PREF_RANGE_0] ?: DEFAULT_RANGE_0
                    val t = pref[SettingsFragment.PREF_TEAM_COUNT] ?: DEFAULT_TEAM_COUNT

                    setSettings(r1, r2, r3, r4, r0, t)
                }
            }
        }

    private val _countZero = MutableStateFlow(DEFAULT_RANGE_0)    // 0
    val countZero: StateFlow<Int> = _countZero
    private val _countRange1 = MutableStateFlow(DEFAULT_RANGE_1)  // 1   - 100
    val countRange1: StateFlow<Int> = _countRange1
    private val _countRange2 = MutableStateFlow(DEFAULT_RANGE_2)  // 100 - 200
    val countRange2: StateFlow<Int> = _countRange2
    private val _countRange3 = MutableStateFlow(DEFAULT_RANGE_3)  // 200 - 500
    val countRange3: StateFlow<Int> = _countRange3
    private val _countRange4 = MutableStateFlow(DEFAULT_RANGE_4)  // 500 - 1500
    val countRange4: StateFlow<Int> = _countRange4
    private val _totalCount = MutableStateFlow(DEFAULT_RANGE_0 + DEFAULT_RANGE_1 + DEFAULT_RANGE_2 + DEFAULT_RANGE_3 + DEFAULT_RANGE_4)
    val totalCount: StateFlow<Int> = _totalCount
    private val _teamCount = MutableStateFlow(DEFAULT_TEAM_COUNT)
    val teamCount: StateFlow<Int> = _teamCount

    fun setCountRange1(value: Int) {
        viewModelScope.launch {
            dataStore?.edit { settings ->
                settings[SettingsFragment.PREF_RANGE_1] = value
            }
        }
    }

    fun setCountRange2(value: Int) {
        viewModelScope.launch {
            dataStore?.edit { settings ->
                settings[SettingsFragment.PREF_RANGE_2] = value
            }
        }
    }

    fun setCountRange3(value: Int) {
        viewModelScope.launch {
            dataStore?.edit { settings ->
                settings[SettingsFragment.PREF_RANGE_3] = value
            }
        }
    }

    fun setCountRange4(value: Int) {
        viewModelScope.launch {
            dataStore?.edit { settings ->
                settings[SettingsFragment.PREF_RANGE_4] = value
            }
        }
    }

    fun setCountRange0(value: Int) {
        viewModelScope.launch {
            dataStore?.edit { settings ->
                settings[SettingsFragment.PREF_RANGE_0] = value
            }
        }
    }

    fun setTeamCount(value: Int) {
        viewModelScope.launch {
            dataStore?.edit { settings ->
                settings[SettingsFragment.PREF_TEAM_COUNT] = value
            }
        }
    }

    private fun setSettings(r1: Int, r2: Int, r3: Int, r4: Int, r0: Int, t: Int) {
        viewModelScope.launch {
            _countRange1.emit(r1)
            _countRange2.emit(r2)
            _countRange3.emit(r3)
            _countRange4.emit(r4)
            _countZero.emit(r0)
            _totalCount.emit(r0 + r1 + r2 + r3 + r4)
            _teamCount.emit(t)
        }
    }

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
        val random = Random(System.currentTimeMillis())

        viewModelScope.launch(Dispatchers.IO) {

        _teamScores.emit(arrayListOf(0, 0, 0, 0, 0))

            // Range 1: 1 - 100
            repeat(_countRange1.value) { curId ->
                val value = random.nextInt(1, 100)

                list.add(Number(
                    id = curId,
                    number = value,
                    random.nextInt(0, 14),
                    onClick = {
                        numberClicked(curId)
                    }
                ))
            }

            // Range 2: 100 - 200
            repeat(_countRange2.value) { curId ->
                val value = random.nextInt(100, 200)

                list.add(Number(
                    id = _countRange1.value + curId,
                    number = value,
                    random.nextInt(0, 14),
                    onClick = {
                        numberClicked(_countRange1.value + curId)
                    }
                ))
            }

            // Range 3: 200 - 500
            repeat(_countRange3.value) { curId ->
                val value = random.nextInt(200, 500)

                list.add(Number(
                    id = _countRange1.value + _countRange2.value + curId,
                    number = value,
                    random.nextInt(0, 14),
                    onClick = {
                        numberClicked(_countRange1.value + _countRange2.value + curId)
                    }
                ))
            }

            // Range 4: 500 - 1500
            repeat(_countRange4.value) { curId ->
                val value = random.nextInt(500, 1500)

                list.add(Number(
                    id = _countRange1.value + _countRange2.value + _countRange3.value + curId,
                    number = value,
                    random.nextInt(0, 14),
                    onClick = {
                        numberClicked(_countRange1.value + _countRange2.value + _countRange3.value + curId)
                    }
                ))
            }

            repeat(_countZero.value) { curId ->
                list.add(Number(
                    id = _countRange1.value + _countRange2.value + _countRange3.value + _countRange4.value + curId,
                    number = 0,
                    random.nextInt(0, 14),
                    onClick = {
                        numberClicked(_countRange1.value + _countRange2.value + _countRange3.value + _countRange4.value + curId)
                    }
                ))
            }

            // Преремешивание
            repeat(_totalCount.value - 1) {
                val id = random.nextInt(0, _totalCount.value - it - 1)
                val value = list[id]
                list[id] = list[_totalCount.value - it - 1]
                list[_totalCount.value - it - 1] = value
            }

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
            val curT = (_curTeam.value + 1) % _teamCount.value
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