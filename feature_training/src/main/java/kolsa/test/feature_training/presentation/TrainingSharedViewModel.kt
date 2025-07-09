package kolsa.test.feature_training.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bumptech.glide.Glide.init
import dagger.hilt.android.lifecycle.HiltViewModel
import kolsa.test.core.common.ApiResult
import kolsa.test.data.local.database.TrainingEntity
import kolsa.test.data.network.responses.VideoResponse
import kolsa.test.feature_training.domain.TrainingsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrainingSharedViewModel @Inject constructor(
    private val trainingsUseCase: TrainingsUseCase
) : ViewModel() {

    private val _trainingPagingData = MutableStateFlow<PagingData<TrainingEntity>>(PagingData.Companion.empty())
    val trainingPagingData: StateFlow<PagingData<TrainingEntity>> = _trainingPagingData.asStateFlow()

    private val _trainingVideoData = MutableStateFlow<ApiResult<VideoResponse>>(ApiResult.Loading)
    val trainingVideoData: StateFlow<ApiResult<VideoResponse>> = _trainingVideoData


    init {
        fetchTrainings()
    }


    fun fetchTrainings() {
        viewModelScope.launch(Dispatchers.IO) {
            trainingsUseCase()
                .distinctUntilChanged()
                .cachedIn(viewModelScope)
                .collect { pagingData ->
                    _trainingPagingData.value = pagingData
                }
        }
    }


    fun getVideoData(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            trainingsUseCase.getVideoLinkById(id)
                .collect { result ->
                    _trainingVideoData.value = result
                }
        }
    }

}