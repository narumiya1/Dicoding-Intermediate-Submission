package com.ackerman.intermediatesubmission.data.view_ui.story

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import androidx.recyclerview.widget.ListUpdateCallback
import com.ackerman.intermediatesubmission.DummyData
import com.ackerman.intermediatesubmission.data.local.UserModel
import com.ackerman.intermediatesubmission.data.remote.response.StoryResponse
import com.ackerman.intermediatesubmission.data.repository.DataStoryRepository
import com.ackerman.intermediatesubmission.data.view_ui.adapter.StoryAdapter
import com.ackerman.intermediatesubmission.utils.MainDispatcherRule
import com.ackerman.intermediatesubmission.utils.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner


@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class StoryViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var repository: DataStoryRepository

    @Test
    fun `Paging story data should return the correct data`() = runTest {
        val dummyStory = DummyData.generateDummyStoryList()
        val data: PagingData<StoryResponse.StoryApp> = StorySource.snapshot(dummyStory)
        val exceptedData = MutableLiveData<PagingData<StoryResponse.StoryApp>>()
        exceptedData.value = data
        Mockito.`when`(repository.getAllzStory()).thenReturn(exceptedData)

        val storyViewModel = StoryViewModel(repository)
        val actualStory: PagingData<StoryResponse.StoryApp> = storyViewModel.getStory().getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)

        assertNotNull(differ.snapshot())
        assertEquals(dummyStory, differ.snapshot())
        assertEquals(dummyStory.size, differ.snapshot().size)
        assertEquals(dummyStory[0].id, differ.snapshot()[0]?.id)
    }

    @Test
    fun `test get user`(){
        val storyRepository = Mockito.mock(DataStoryRepository::class.java)
        val expectedLiveData = MutableLiveData<UserModel>()
        expectedLiveData.value = UserModel("tri", "abcd", true)

        Mockito.`when`(storyRepository.getCurrentUserData()).thenReturn(expectedLiveData)
        val viewModel = StoryViewModel(storyRepository)
        val actualResult = viewModel.getCurrentUser().getOrAwaitValue()

        assertNotNull(actualResult)
        assertEquals(actualResult, expectedLiveData.value)
        assertEquals(actualResult.name, expectedLiveData.value?.name)
        assertEquals(actualResult.token, expectedLiveData.value?.token)
        assertEquals(actualResult.isLogin, expectedLiveData.value?.isLogin)
    }

}

class StorySource : PagingSource<Int, LiveData<List<StoryResponse.StoryApp>>>() {
    companion object {
        fun snapshot(items: List<StoryResponse.StoryApp>): PagingData<StoryResponse.StoryApp> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<StoryResponse.StoryApp>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<StoryResponse.StoryApp>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}