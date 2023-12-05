package com.ackerman.intermediatesubmission.data.view_ui.auth

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.ackerman.intermediatesubmission.DummyAuth
import com.ackerman.intermediatesubmission.data.remote.response.RegisterResponse
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
class RegisterViewModelTest {

    @get: Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: DataStoryRepository
    private lateinit var registerViewModel: RegisterViewModel
    private val auth = DummyAuth.provideRegisterResponse()
    private val name = "Panji"
    private val email = "panjinauli1@gmail.com"
    private val password = "P@dang"


    @Before
    fun setup() {
        registerViewModel = RegisterViewModel(repository)
    }

    @Test
    fun `when register called from repository it should return Success and not null`() {
        val expected = MutableLiveData<com.ackerman.intermediatesubmission.data.utils.Result<RegisterResponse>>()
        expected.value = com.ackerman.intermediatesubmission.data.utils.Result.Success(auth)
        Mockito.`when`(repository.registerUser(name, email, password)).thenReturn(expected)

        val actual = registerViewModel.userRegister(name, email, password).getOrAwaitValue()
        Assert.assertTrue(actual is com.ackerman.intermediatesubmission.data.utils.Result.Success)
        Assert.assertNotNull(actual)
    }

    @Test
    fun `when register failed it should return Error and also not null`(){
        val expected = MutableLiveData<com.ackerman.intermediatesubmission.data.utils.Result<RegisterResponse>>()
        expected.value = com.ackerman.intermediatesubmission.data.utils.Result.Error("Something Error")
        Mockito.`when`(repository.registerUser(name, email, password)).thenReturn(expected)

        val actual = registerViewModel.userRegister(name, email, password).getOrAwaitValue()
        Assert.assertTrue(actual is com.ackerman.intermediatesubmission.data.utils.Result.Error)
        Assert.assertNotNull(actual)
    }

}