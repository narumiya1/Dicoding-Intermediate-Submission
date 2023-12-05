package com.ackerman.intermediatesubmission.data.view_ui.maaps

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.ackerman.intermediatesubmission.DummyData
import com.ackerman.intermediatesubmission.data.local.UserModel
import com.ackerman.intermediatesubmission.data.remote.response.StoryResponse
import com.ackerman.intermediatesubmission.data.repository.DataStoryRepository
import com.ackerman.intermediatesubmission.utils.getOrAwaitValue
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)

class MapsViewModelTest{
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: DataStoryRepository
    private lateinit var mapsViewModel: MapsViewModel
    private val mapResponse = DummyData.storyResponseLocation()
    private val token = ""


    @Before
    fun setup() {
        mapsViewModel = MapsViewModel(repository)
    }

    @Test
    fun `data should return the api with a location`() {
        val exceptedLiveData = MutableLiveData<com.ackerman.intermediatesubmission.data.utils.Result<StoryResponse>>()
        exceptedLiveData.value = com.ackerman.intermediatesubmission.data.utils.Result.Success(mapResponse)

        val expected = exceptedLiveData.getOrAwaitValue()
        Mockito.`when`(repository.getAllzStoryWithLocation(token)).thenReturn(exceptedLiveData)
        val actual = mapsViewModel.getLocationStory(token).getOrAwaitValue()

        Assert.assertNotNull(actual)
        assertTrue(actual is com.ackerman.intermediatesubmission.data.utils.Result.Success)
        assertEquals(expected, actual)
    }

    @Test
    fun `when get Story Location is Error`() {
        val expectedResponse = MutableLiveData<com.ackerman.intermediatesubmission.data.utils.Result<StoryResponse>>()
        expectedResponse.value = com.ackerman.intermediatesubmission.data.utils.Result.Error("error")

        Mockito.`when`(repository.getAllzStoryWithLocation(token)).thenReturn(expectedResponse)

        val actualResponse = mapsViewModel.getLocationStory(token).getOrAwaitValue()

        Mockito.verify(repository).getAllzStoryWithLocation(token)
        Assert.assertNotNull(actualResponse)
        assertTrue(actualResponse is com.ackerman.intermediatesubmission.data.utils.Result.Error)
    }

    @Test
    fun `test get user`(){
        val storyRepository = Mockito.mock(DataStoryRepository::class.java)
        val expectedLiveData = MutableLiveData<UserModel>()
        expectedLiveData.value = UserModel("tri", "abcd", true)

        Mockito.`when`(storyRepository.getCurrentUserData()).thenReturn(expectedLiveData)
        val viewModel = MapsViewModel(storyRepository)
        val actualResult = viewModel.getUser().getOrAwaitValue()

        Assert.assertNotNull(actualResult)
        assertEquals(actualResult, expectedLiveData.value)
        assertEquals(actualResult.name, expectedLiveData.value?.name)
        assertEquals(actualResult.token, expectedLiveData.value?.token)
        assertEquals(actualResult.isLogin, expectedLiveData.value?.isLogin)
    }

}