package com.ackerman.intermediatesubmission.data.view_ui.story

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.ackerman.intermediatesubmission.DummyData
import com.ackerman.intermediatesubmission.data.local.UserModel
import com.ackerman.intermediatesubmission.data.remote.response.PostStoryResponse
import com.ackerman.intermediatesubmission.data.repository.DataStoryRepository
import com.ackerman.intermediatesubmission.utils.getOrAwaitValue
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.io.File

@RunWith(MockitoJUnitRunner::class)

class PostStoryViewModelTest{
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: DataStoryRepository
    private lateinit var postStoryViewModel: PostStoryViewModel
    private val auth = DummyData.postStoryResponse()
    private val token = ""
    private val file = Mockito.mock(File::class.java)
    private val lat = -6.1947508
    private val lon = 106.4867868

    @Before
    fun setup() {
        postStoryViewModel = PostStoryViewModel(repository)

    }


    @Test
    fun `posting live data should return success when invoking upload to Server`(){
        val requestImageFile = file.asRequestBody("image/jpg".toMediaTypeOrNull())
        val photo: MultipartBody.Part =  MultipartBody.Part.createFormData("photo", file.name, requestImageFile)
        val description = "description".toRequestBody("text/plain".toMediaType())

        val expectedLiveData = MutableLiveData<com.ackerman.intermediatesubmission.data.utils.Result<PostStoryResponse>>()
        expectedLiveData.value = com.ackerman.intermediatesubmission.data.utils.Result.Success(auth)

        val expected = expectedLiveData.getOrAwaitValue()

        Mockito.`when`(repository.postStory(token, photo, description, lat, lon)).thenReturn(expectedLiveData)
        val actual = postStoryViewModel.postStory(token, photo, description,lat, lon).getOrAwaitValue()

        Mockito.verify(repository).postStory(token, photo, description, lat, lon)
        Assert.assertNotNull(actual)
        Assert.assertTrue(actual is com.ackerman.intermediatesubmission.data.utils.Result.Success)
        assertEquals(expected, actual)
    }
    @Test
    fun `test get user`(){
        val storyRepository = Mockito.mock(DataStoryRepository::class.java)
        val expectedLiveData = MutableLiveData<UserModel>()
        expectedLiveData.value = UserModel("Panji", "P@dang", true)

        Mockito.`when`(storyRepository.getCurrentUserData()).thenReturn(expectedLiveData)
        val viewModel = PostStoryViewModel(storyRepository)
        val actualResult = viewModel.getUser().getOrAwaitValue()

        Assert.assertNotNull(actualResult)
        assertEquals(actualResult, expectedLiveData.value)
        assertEquals(actualResult.name, expectedLiveData.value?.name)
        assertEquals(actualResult.token, expectedLiveData.value?.token)
        assertEquals(actualResult.isLogin, expectedLiveData.value?.isLogin)
    }
}