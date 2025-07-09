package kolsa.test.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [TrainingEntity::class],
    version = 1,
    exportSchema = false
)
abstract class TrainingDatabase: RoomDatabase() {
    abstract fun trainingDao(): TrainingDao
}