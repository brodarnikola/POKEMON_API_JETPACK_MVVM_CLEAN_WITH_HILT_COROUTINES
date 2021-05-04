/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nikola_brodar.data.di

import com.nikola_brodar.data.database.mapper.DbMapper
import com.nikola_brodar.data.di_dagger2.youtube.YoutubeNetwork
import com.nikola_brodar.data.networking.youtube.YoutubeRepositoryApi
import com.nikola_brodar.data.repository.YoutubeRepositoryImpl
import com.nikola_brodar.domain.repository.YoutubeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
class YoutubeRepositoryModule {

    @Provides
    @YoutubeNetwork
    fun provideYoutubeVideosByKeywordFromRestApi(  @YoutubeNetwork youtubeRepositoryApi: YoutubeRepositoryApi, dbMapper : DbMapper) : YoutubeRepository {
        return YoutubeRepositoryImpl( youtubeRepositoryApi, dbMapper)
    }
}
