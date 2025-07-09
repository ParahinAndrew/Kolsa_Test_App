package kolsa.test.data.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "training")
data class TrainingEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String?,
    val description: String?,
    val type: Int?,
    val duration: String?
)
