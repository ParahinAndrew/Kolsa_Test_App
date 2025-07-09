package kolsa.test.feature_training.presentation.workout_screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import kolsa.test.core.common.BaseFragment
import kolsa.test.feature_training.R
import kolsa.test.feature_training.databinding.FragmentTrainingBinding
import kolsa.test.feature_training.presentation.TrainingSharedViewModel
import kotlin.getValue

@AndroidEntryPoint
class TrainingFragment : BaseFragment<FragmentTrainingBinding>(
    FragmentTrainingBinding::inflate
) {

    private val viewModel: TrainingSharedViewModel by activityViewModels()

    override fun setupUi() {

    }

}