package kolsa.test.data.local.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): TrainingDatabase = Room.databaseBuilder(
        context,
        TrainingDatabase::class.java,
        "training.db"
    ).fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideDao(database: TrainingDatabase): TrainingDao = database.trainingDao()

}