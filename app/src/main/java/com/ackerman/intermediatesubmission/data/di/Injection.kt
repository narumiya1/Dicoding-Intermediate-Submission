package com.ackerman.intermediatesubmission.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.ackerman.intermediatesubmission.data.local.UserPreference
import com.ackerman.intermediatesubmission.data.remote.api.ApiConfig
import com.ackerman.intermediatesubmission.data.repository.StoryaRepository


val Context.dataStore: DataStore<Preferences> by preferencesDataStore("storyApp")

object Injection {
    fun provideRepository(context: Context) : StoryaRepository{
        val preference = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return StoryaRepository.getInstance(preference, apiService)
    }
}