package kolsa.test.data.local.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface TrainingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(trainings: List<TrainingEntity>)

    @Query("SELECT * FROM training ORDER BY id ASC")
    fun pagingSource(): PagingSource<Int, TrainingEntity>

    @Query("DELETE FROM training")
    suspend fun clearAll()


}