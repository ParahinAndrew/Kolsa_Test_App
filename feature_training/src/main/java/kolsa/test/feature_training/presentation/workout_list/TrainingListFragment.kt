package kolsa.test.feature_training.presentation.workout_list

import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import dagger.hilt.android.AndroidEntryPoint
import kolsa.test.core.common.BaseFragment
import kolsa.test.core.common.TypesTraining
import kolsa.test.feature_training.R
import kolsa.test.feature_training.databinding.FragmentTrainingListBinding
import kolsa.test.feature_training.presentation.workout_list.adapter.SpacingItemDecoration
import kolsa.test.feature_training.presentation.workout_list.adapter.TrainingPagingAdapter
import kolsa.test.feature_training.presentation.workout_screen.PlayerViewModel
import kolsa.test.feature_training.presentation.workout_screen.TrainingFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TrainingListFragment : BaseFragment<FragmentTrainingListBinding>(
    FragmentTrainingListBinding::inflate
) {

    private val viewModel: TrainingListViewModel by viewModels()
    private val playerViewModel: PlayerViewModel by activityViewModels()
    private lateinit var trainingAdapter: TrainingPagingAdapter

    override fun setupUi() {
        setupTrainingRecyclerView()
        setupSearchView()
        setupFilterChips()
    }

    private fun setupTrainingRecyclerView() {
        trainingAdapter = TrainingPagingAdapter { trainingEntity ->
            playerViewModel.getVideoLink(trainingEntity.id)
            playerViewModel.getVideoInfoById(trainingEntity.id)
            findNavController().navigate(R.id.action_trainingListFragment_to_trainingFragment)
        }

        binding.trainingRV.apply {
            adapter = trainingAdapter
            val spacingInPixels =
                resources.getDimensionPixelSize(kolsa.test.core.R.dimen.small_margin)
            addItemDecoration(SpacingItemDecoration(spacingInPixels))
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.trainingPagingData.collectLatest { pagingData ->
                trainingAdapter.submitData(pagingData)
            }
        }

        // Это наблюдатель состояния списка, можно прогресс-бар показать или ещё как-то обработать
        // Но делать я этого не буду, т.к. лень и оверхед для тестового, а пагинация с таким запросом и так лишняя вообще
        viewLifecycleOwner.lifecycleScope.launch {
            trainingAdapter.loadStateFlow.collectLatest { loadStates ->
                val refreshState = loadStates.refresh
                Log.d(
                    "Fragment",
                    "Adapter LoadState: $refreshState, itemCount: ${trainingAdapter.itemCount}"
                )
                if (refreshState is LoadState.NotLoading && trainingAdapter.itemCount == 0 && loadStates.source.refresh is LoadState.NotLoading) {
                    Log.d(
                        "Fragment",
                        "Adapter has no data after loading and source is also not loading."
                    )
                }
                if (refreshState is LoadState.Error) {
                    Log.e("Fragment", "Adapter LoadState Error: ${refreshState.error}")
                }
            }
        }
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.setSearchQuery(query.orEmpty())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.setSearchQuery(newText.orEmpty())
                return true
            }
        })
    }

    private fun setupFilterChips() {
        binding.filterChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.isEmpty()) {
                viewModel.setSelectedType(null)
            } else {
                val checkedChipId = checkedIds.first()
                val selectedType = when (checkedChipId) {
                    R.id.strengthChip -> TypesTraining.STRENGTH.intType
                    R.id.cardioChip -> TypesTraining.CARDIO.intType
                    R.id.stretchingChip -> TypesTraining.STRETCHING.intType
                    else -> null
                }
                viewModel.setSelectedType(selectedType)
            }
        }
    }

}