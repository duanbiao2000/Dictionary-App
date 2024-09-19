package com.ahmed_apps.dictionary_app.di

import com.ahmed_apps.dictionary_app.data.repository.DictionaryRepositoryImpl
import com.ahmed_apps.dictionary_app.domain.repository.DictionaryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * 该类为Repository模块，用于Dagger Hilt的依赖注入。
 * 它将实现类绑定到接口，确保DictionaryRepository的实现类被正确创建并使用。
 *
 * @author Ahmed Guedmioui
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    /**
     * 将DictionaryRepositoryImpl绑定到DictionaryRepository接口。
     * 这样，当应用程序请求DictionaryRepository的实例时，将返回DictionaryRepositoryImpl的实例。
     *
     * @param dictionaryRepositoryImpl 字典仓库的实现类实例。
     * @return 字典仓库接口的实例。
     */
    @Binds
    @Singleton
    abstract fun bindDictionaryRepository(
        dictionaryRepositoryImpl: DictionaryRepositoryImpl
    ): DictionaryRepository

}
