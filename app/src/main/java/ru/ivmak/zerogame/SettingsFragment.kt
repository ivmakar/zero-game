package ru.ivmak.zerogame

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.databinding.DataBindingUtil
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ivmak.zerogame.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val viewModel by activityViewModels<MainViewModel>()

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)

        initView()

        return binding.root
    }

    private fun initView() {
        binding.valueRange1SeekBar.progress = viewModel.countRange1.value
        binding.valueRange2SeekBar.progress = viewModel.countRange2.value
        binding.valueRange3SeekBar.progress = viewModel.countRange3.value
        binding.valueRange4SeekBar.progress = viewModel.countRange4.value
        binding.zeroSeekBar.progress = viewModel.countZero.value
        binding.teamCountSeekBar.progress = viewModel.teamCount.value

        viewModel.countRange1.onEach {
            binding.txtValueRange1.text = it.toString()
        }.observeInLifecycle(viewLifecycleOwner)

        viewModel.countRange2.onEach {
            binding.txtValueRange2.text = it.toString()
        }.observeInLifecycle(viewLifecycleOwner)

        viewModel.countRange3.onEach {
            binding.txtValueRange3.text = it.toString()
        }.observeInLifecycle(viewLifecycleOwner)

        viewModel.countRange4.onEach {
            binding.txtValueRange4.text = it.toString()
        }.observeInLifecycle(viewLifecycleOwner)

        viewModel.countZero.onEach {
            binding.txtZeroValue.text = it.toString()
        }.observeInLifecycle(viewLifecycleOwner)

        viewModel.totalCount.onEach {
            binding.txtCountValue.text = it.toString()
        }.observeInLifecycle(viewLifecycleOwner)

        viewModel.teamCount.onEach {
            binding.txtTeamCountValue.text = it.toString()
        }.observeInLifecycle(viewLifecycleOwner)


        binding.valueRange1SeekBar.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                viewModel.setCountRange1(p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) { }
            override fun onStopTrackingTouch(p0: SeekBar?) { }
        })

        binding.valueRange2SeekBar.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                viewModel.setCountRange2(p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) { }
            override fun onStopTrackingTouch(p0: SeekBar?) { }
        })

        binding.valueRange3SeekBar.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                viewModel.setCountRange3(p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) { }
            override fun onStopTrackingTouch(p0: SeekBar?) { }
        })

        binding.valueRange4SeekBar.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                viewModel.setCountRange4(p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) { }
            override fun onStopTrackingTouch(p0: SeekBar?) { }
        })

        binding.zeroSeekBar.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                viewModel.setCountRange0(p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) { }
            override fun onStopTrackingTouch(p0: SeekBar?) { }
        })

        binding.teamCountSeekBar.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                viewModel.setTeamCount(p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) { }
            override fun onStopTrackingTouch(p0: SeekBar?) { }
        })
    }

    companion object {
        val PREF_RANGE_1    = intPreferencesKey("PREF_RANGE_1")
        val PREF_RANGE_2    = intPreferencesKey("PREF_RANGE_2")
        val PREF_RANGE_3    = intPreferencesKey("PREF_RANGE_3")
        val PREF_RANGE_4    = intPreferencesKey("PREF_RANGE_4")
        val PREF_RANGE_0    = intPreferencesKey("PREF_RANGE_0")
        val PREF_TEAM_COUNT = intPreferencesKey("PREF_TEAM_COUNT")

        const val DEFAULT_RANGE_1    = 53
        const val DEFAULT_RANGE_2    = 20
        const val DEFAULT_RANGE_3    = 8
        const val DEFAULT_RANGE_4    = 4
        const val DEFAULT_RANGE_0    = 15
        const val DEFAULT_TEAM_COUNT = 2
    }

}