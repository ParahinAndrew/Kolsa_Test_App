package kolsa.test.feature_training.presentation.workout_list

import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kolsa.test.core.common.BaseFragment
import kolsa.test.feature_training.databinding.FragmentTrainingListBinding
import kolsa.test.feature_training.presentation.TrainingSharedViewModel

@AndroidEntryPoint
class TrainingListFragment : BaseFragment<FragmentTrainingListBinding>(
    FragmentTrainingListBinding::inflate
) {

    private val viewModel: TrainingSharedViewModel by activityViewModels()

    override fun setupUi() {

    }

}