package kolsa.test.data

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kolsa.test.data.local.database.TrainingDao
import kolsa.test.data.local.database.TrainingDatabase
import kolsa.test.data.network.TrainingAPI
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideRepository(
        trainingAPI: TrainingAPI,
        trainingDao: TrainingDao,
        trainingDatabase: TrainingDatabase
    ): Repository {
        return Repository(
            trainingAPI = trainingAPI,
            trainingDao = trainingDao,
            trainingDatabase = trainingDatabase
        )
    }

}