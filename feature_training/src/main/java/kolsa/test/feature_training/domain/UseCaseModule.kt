package kolsa.test.feature_training.domain

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kolsa.test.data.Repository


@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    @ViewModelScoped
    fun provideGetTrainingsUseCase(repository: Repository) : TrainingsUseCase {
        return TrainingsUseCase(repository)
    }


}