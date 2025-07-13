package kolsa.test.feature_training.presentation.workout_screen

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.WindowMetrics
import androidx.activity.OnBackPressedCallback
import androidx.media3.common.Player
import dagger.hilt.android.AndroidEntryPoint
import kolsa.test.core.common.BaseFragment
import kolsa.test.feature_training.databinding.FragmentTrainingBinding
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import kolsa.test.core.common.ApiResult
import kolsa.test.core.common.SnackBarMessage
import kolsa.test.core.common.TypesTraining
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TrainingFragment : BaseFragment<FragmentTrainingBinding>(
    FragmentTrainingBinding::inflate
) {

    private val viewModel: PlayerViewModel by activityViewModels()
    private var currentOrientationIsLandscape = false

    @SuppressLint("SourceLockedOrientationActivity")
    override fun setupUi() {
        currentOrientationIsLandscape =
            resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

        if (currentOrientationIsLandscape) {
            applyFullscreenUiChanges()
        } else {
            applyPortraitUiChanges()
        }
        setupBackButtonHandler()

        setupVideoPlayer()

        setupVideoInfoObserver()

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun setupVideoInfoObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.videoInfo.collectLatest { result ->
                    when (result) {
                        is ApiResult.Loading -> {
                            setupVideoInfoStatus(isLoading = true)
                        }

                        is ApiResult.Success -> {
                            setupVideoInfoStatus(isLoading = false)
                            result.data?.let { info ->
                                binding.apply {
                                    titleText.text = info.title
                                    typeText.text =
                                        getString(TypesTraining.fromInt(info.type).stringTypeId)
                                    durationText.text = formatDuration(info.duration)
                                    descriptionText.text = info.description ?: ""
                                }
                            }
                        }

                        is ApiResult.Error -> {
                            setupVideoInfoStatus(isLoading = false)
                            SnackBarMessage().makeSnackBarShort(
                                requireView(),
                                getString(kolsa.test.core.R.string.no_video_info)
                            )
                        }
                    }
                }
            }
        }
    }

    private fun formatDuration(durationString: String?): String {
        return when {
            durationString == null -> ""
            durationString.all { it.isDigit() } -> {
                if (durationString.isNotEmpty()) "$durationString ${requireContext().getString(kolsa.test.core.R.string.minutes)}" else ""
            }

            else -> durationString
        }
    }

    private fun setupVideoInfoStatus(isLoading: Boolean) {
        binding.progressBarVideoInfo.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setupVideoPlayer() {
        binding.playerView.player = viewModel.player
        binding.playerView.setFullscreenButtonClickListener { isEnterFullscreen ->
            if (isEnterFullscreen) {
                activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                applyFullscreenUiChanges()
            } else {
                activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                applyPortraitUiChanges()
            }
        }
    }

    private fun applyFullscreenUiChanges() {
        val window = activity?.window
        if (window != null) {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            WindowInsetsControllerCompat(window, binding.playerView).let { controller ->
                controller.hide(WindowInsetsCompat.Type.systemBars())
                controller.systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }

        val params = binding.playerView.layoutParams
        params.height = ViewGroup.LayoutParams.MATCH_PARENT
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        binding.playerView.layoutParams = params

        toggleUiElementsVisibility(View.GONE)
    }

    private fun applyPortraitUiChanges() {
        val window = activity?.window
        if (window != null) {
            WindowCompat.setDecorFitsSystemWindows(window, true)
            WindowInsetsControllerCompat(
                window,
                binding.playerView
            ).show(WindowInsetsCompat.Type.systemBars())
        }

        val params = binding.playerView.layoutParams
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        binding.playerView.layoutParams = params

        toggleUiElementsVisibility(View.VISIBLE)
    }

    private fun toggleUiElementsVisibility(visibility: Int) {
        binding.backBtn.visibility = visibility
        binding.titleText.visibility = visibility
        binding.typeText.visibility = visibility
        binding.durationText.visibility = visibility
        binding.descriptionText.visibility = visibility
    }


    private fun setupBackButtonHandler() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object :
            OnBackPressedCallback(true) {
            @SuppressLint("SourceLockedOrientationActivity")
            override fun handleOnBackPressed() {
                if (currentOrientationIsLandscape) {
                    activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
                } else {
                    findNavController().popBackStack()
                    isEnabled = false
                }
            }
        })
    }


    override fun onResume() {
        super.onResume()
        binding.playerView.onResume()
        if (viewModel.playWhenReady && !viewModel.player.isPlaying && viewModel.player.playbackState == Player.STATE_READY) {
            viewModel.play()
        }
    }

    override fun onPause() {
        super.onPause()
        binding.playerView.onPause()
        viewModel.pause()
    }

}
