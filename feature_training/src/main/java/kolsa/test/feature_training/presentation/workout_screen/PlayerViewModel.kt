package kolsa.test.feature_training.presentation.workout_screen

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import dagger.hilt.android.lifecycle.HiltViewModel
import kolsa.test.core.common.ApiResult
import kolsa.test.feature_training.domain.TrainingsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.core.net.toUri
import kolsa.test.data.local.database.TrainingEntity
import kolsa.test.data.network.responses.VideoResponse
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn


@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val trainingsUseCase: TrainingsUseCase,
    val player: Player,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _videoInfo = MutableStateFlow<ApiResult<TrainingEntity?>>(ApiResult.Loading)
    val videoInfo: StateFlow<ApiResult<TrainingEntity?>> = _videoInfo.asStateFlow()

    fun getVideoInfoById(id: Int) {
        viewModelScope.launch {
            trainingsUseCase.getVideoInfoById(id).collect { result ->
                _videoInfo.value = result
            }
        }
    }


    private val _currentMediaItem = MutableStateFlow<MediaItem?>(null)

    private val _trainingVideoLink = MutableStateFlow<ApiResult<VideoResponse>>(ApiResult.Loading)

    var playWhenReady: Boolean
        get() = savedStateHandle.get<Boolean>("playWhenReady") ?: true
        set(value) {
            savedStateHandle["playWhenReady"] = value
            player.playWhenReady = value
        }

    var currentPlaybackPosition: Long
        get() = savedStateHandle.get<Long>("currentPosition") ?: 0L
        set(value) = savedStateHandle.set("currentPosition", value)


    init {
        player.prepare()
        player.playWhenReady = playWhenReady

        player.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                if (!isPlaying) {
                    currentPlaybackPosition = player.currentPosition
                }
            }
        })
    }

    fun getVideoLink(id: Int) {

        resetPlayerStateForNewVideo()

        viewModelScope.launch {
            _trainingVideoLink.value = ApiResult.Loading
            trainingsUseCase.getVideoLinkById(id).collect { result ->
                _trainingVideoLink.value = result
            }

            _trainingVideoLink.collect { result ->
                if (result is ApiResult.Success) {
                    val uri = result.data.link.toUri()
                    setVideo(uri)
                }
            }
        }
    }

    private fun setVideo(uri: Uri) {
        val mediaItem = MediaItem.fromUri(uri)
        _currentMediaItem.value = mediaItem
        player.setMediaItem(mediaItem, currentPlaybackPosition)
        player.playWhenReady = playWhenReady
        if (playWhenReady) {
            player.play()
        }
    }

    fun play() {
        playWhenReady = true
        player.play()
    }

    fun pause() {
        playWhenReady = false
        player.pause()
        currentPlaybackPosition = player.currentPosition
    }

    private fun resetPlayerStateForNewVideo() {
        player.stop()
        player.clearMediaItems()
        currentPlaybackPosition = 0L
        _currentMediaItem.value = null
    }

    override fun onCleared() {
        super.onCleared()
        currentPlaybackPosition = player.currentPosition
        playWhenReady = player.playWhenReady
        player.release()
    }
}


