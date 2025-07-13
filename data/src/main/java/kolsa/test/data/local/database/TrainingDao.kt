package kolsa.test.data.local.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface TrainingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(trainings: List<TrainingEntity>)

    @Query("SELECT * FROM training WHERE (:query = '' OR title LIKE '%' || :query || '%') AND (:type IS NULL OR type = :type) ORDER BY id ASC")
    fun pagingSource(query: String, type: Int?): PagingSource<Int, TrainingEntity>

    @Query("SELECT * FROM training WHERE id = :id")
    fun getVideoInfoById(id: Int): Flow<TrainingEntity?>

    @Query("DELETE FROM training")
    suspend fun clearAll()

}