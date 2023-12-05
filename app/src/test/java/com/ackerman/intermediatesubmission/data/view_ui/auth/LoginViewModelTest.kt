package com.ackerman.intermediatesubmission.data.view_ui.auth

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.ackerman.intermediatesubmission.DummyAuth
import com.ackerman.intermediatesubmission.data.remote.response.ResponseLogin
import com.ackerman.intermediatesubmission.data.repository.DataStoryRepository
import com.ackerman.intermediatesubmission.utils.MainDispatcherRule
import com.ackerman.intermediatesubmission.utils.getOrAwaitValue
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)

class LoginViewModelTest{

    @get: Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get: Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var repository: DataStoryRepository
    private lateinit var loginViewModel: LoginViewModel
    private val dummyResponse = DummyAuth.provideLoginResponse()
    private val email = "panjinauli1@gmail.com"
    private val password = "P@dang"


    @Before
    fun setup() {
        loginViewModel = LoginViewModel(repository)
    }


    @Test
    fun `if login success should return success`(){
        val expactedLiveDataResult = MutableLiveData<com.ackerman.intermediatesubmission.data.utils.Result<ResponseLogin>>()
        expactedLiveDataResult.value = com.ackerman.intermediatesubmission.data.utils.Result.Success(dummyResponse)

        val expected = expactedLiveDataResult.getOrAwaitValue()

        `when` (repository.loginUser(email, password)).thenReturn(expactedLiveDataResult)
        val actual = loginViewModel.loginUser(email, password).getOrAwaitValue()

        verify(repository).loginUser(email, password)
        assertNotNull(actual)
        assertTrue(actual is com.ackerman.intermediatesubmission.data.utils.Result.Success)
        assertEquals(expected, actual)
    }

}