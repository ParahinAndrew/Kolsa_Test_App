package kolsa.test.feature_training.presentation.workout_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kolsa.test.data.local.database.TrainingEntity
import kolsa.test.feature_training.domain.TrainingsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class TrainingListViewModel @Inject constructor(
    private val trainingsUseCase: TrainingsUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedType = MutableStateFlow<Int?>(null)
    val selectedType: StateFlow<Int?> = _selectedType.asStateFlow()

    val trainingPagingData: Flow<PagingData<TrainingEntity>> =
        combine(_searchQuery, _selectedType) { query, type ->
            Log.d("ViewModel", "Combine: query='$query', type=$type")
            Pair(query, type)
        }.flatMapLatest { (query, type) ->
            Log.d("ViewModel", "flatMapLatest: Fetching for query='$query', type=$type")
            trainingsUseCase(query, type)
        }
            .distinctUntilChanged()
            .cachedIn(viewModelScope)


    fun setSearchQuery(query: String) {
        _searchQuery.value = query.trim()
    }


    fun setSelectedType(type: Int?) {
        _selectedType.value = type
    }



}