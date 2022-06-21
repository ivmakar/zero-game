package ru.ivmak.zerogame

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.flow.onEach
import ru.ivmak.zerogame.databinding.FragmentGameBinding

class GameFragment : Fragment() {

    private lateinit var binding: FragmentGameBinding
    private val viewModel by activityViewModels<MainViewModel>()

    private lateinit var adapter: GameRvAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_game, container, false)

        initView()

        return binding.root
    }

    private fun initView() {
        adapter = GameRvAdapter(arrayListOf())

        binding.gameRv.apply {
            adapter = this@GameFragment.adapter
            layoutManager = GridLayoutManager(requireContext(), 3)
        }

        binding.lTeam1.isVisible = viewModel.teamCount > 0
        binding.lTeam2.isVisible = viewModel.teamCount > 1
        binding.lTeam3.isVisible = viewModel.teamCount > 2
        binding.lTeam4.isVisible = viewModel.teamCount > 3
        binding.lTeam5.isVisible = viewModel.teamCount > 4


        binding.lDropDown.setOnClickListener {
            viewModel.setScoreVisible()
        }

        binding.btnNextTeam.setOnClickListener {
            viewModel.nextTeam()
        }


        viewModel.items.onEach {
            Log.d(TAG, "initView: ${it.size}")
            adapter.replace(it)
        }.observeInLifecycle(viewLifecycleOwner)

        viewModel.events.onEach {
            when (it) {
                is MainViewModel.Event.ClickEvent -> numberClicked(it.number)
                MainViewModel.Event.GameEndedEvent -> findNavController().navigate(R.id.action_gameFragment_to_scoreFragment)
                is MainViewModel.Event.NextTeamEvent -> showNextTeamDialog(it.number)
            }
        }.observeInLifecycle(viewLifecycleOwner)

        viewModel.curTeam.onEach {
            val str = "Текущий счет, Команда ${it + 1}:"
            binding.txtCurTeamScore.text = str
        }.observeInLifecycle(viewLifecycleOwner)

        viewModel.curTeamScore.onEach {
            binding.txtCurTeamScoreValue.text = it.toString()
            binding.btnNextTeam.isEnabled = it != 0
        }.observeInLifecycle(viewLifecycleOwner)

        viewModel.teamScores.onEach {
            binding.txtTeam1ScoreValue.text = it[0].toString()
            binding.txtTeam2ScoreValue.text = it[1].toString()
            binding.txtTeam3ScoreValue.text = it[2].toString()
            binding.txtTeam4ScoreValue.text = it[3].toString()
            binding.txtTeam5ScoreValue.text = it[4].toString()
        }.observeInLifecycle(viewLifecycleOwner)

        viewModel.isScoreVisible.onEach {
            binding.lScores.isVisible = it
            binding.imgDropDown.rotation = if (it) 180f else 0f
        }.observeInLifecycle(viewLifecycleOwner)
    }

    private fun showNextTeamDialog(team: Int) {
        Toast.makeText(requireContext(), "Ходит команда ${team + 1}", Toast.LENGTH_LONG).show()
    }

    private fun numberClicked(number: Number) {
        val dialog = EnsureDialog(requireContext()).builder()
        dialog.setGravity(Gravity.CENTER)
        dialog.setTitle(number.number.toString())
        dialog.setCancelable(false)
        dialog.setPositiveButton("Ok") {
            dialog.dismiss()
            adapter.remove(number.id)
            if (viewModel.items.value.isEmpty()) {
                viewModel.endGame()
                return@setPositiveButton
            }
            if (number.number == 0) {
                viewModel.nextTeam()
            }
        }
        dialog.show()
    }

    companion object {
        private const val TAG = "GameFragment"
    }
}