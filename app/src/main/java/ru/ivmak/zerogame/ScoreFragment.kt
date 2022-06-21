package ru.ivmak.zerogame

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.onEach
import ru.ivmak.zerogame.databinding.FragmentScoreBinding

class ScoreFragment : Fragment() {

    private lateinit var binding: FragmentScoreBinding
    private val viewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_score, container, false)

        initView()

        return binding.root
    }

    private fun initView() {

        binding.lTeam1.isVisible = viewModel.teamCount.value > 0
        binding.lTeam2.isVisible = viewModel.teamCount.value > 1
        binding.lTeam3.isVisible = viewModel.teamCount.value > 2
        binding.lTeam4.isVisible = viewModel.teamCount.value > 3
        binding.lTeam5.isVisible = viewModel.teamCount.value > 4

        viewModel.teamScores.onEach {
            binding.txtTeam1ScoreValue.text = it[0].toString()
            binding.txtTeam2ScoreValue.text = it[1].toString()
            binding.txtTeam3ScoreValue.text = it[2].toString()
            binding.txtTeam4ScoreValue.text = it[3].toString()
            binding.txtTeam5ScoreValue.text = it[4].toString()
        }.observeInLifecycle(viewLifecycleOwner)

        binding.btnNext.setOnClickListener {
            findNavController().popBackStack()
        }
    }

}