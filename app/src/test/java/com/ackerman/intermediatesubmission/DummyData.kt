package com.ackerman.intermediatesubmission

import com.ackerman.intermediatesubmission.data.remote.response.PostStoryResponse
import com.ackerman.intermediatesubmission.data.remote.response.StoryResponse

object DummyData {
    fun postStoryResponse(): PostStoryResponse {
        return PostStoryResponse(false, "Ok")
    }
    fun generateDummyStoryList(): List<StoryResponse.StoryApp>{
        val storyList = ArrayList<StoryResponse.StoryApp>()
        for (i in 0..100) {
            val story = StoryResponse.StoryApp(
                i.toString(),
                "name + $i",
                "desc + $i",
                "$i",
                "created + $i",
                i.toDouble(),
                i.toDouble()
            )
            storyList.add(story)
        }
        return storyList
    }
    fun storyResponseLocation(): StoryResponse {
        return StoryResponse(false, "Ok", generateDummyStoryLocation())
    }
    private fun generateDummyStoryLocation(): List<StoryResponse.StoryApp> {
        val storyList = ArrayList<StoryResponse.StoryApp>()
        for (i in 0..10) {
            val story = StoryResponse.StoryApp(
                "ID",
                "Panji",
                "desc",
                "https://story-api.dicoding.dev/images/stories/photos-1701737143429_lpdTGBpm.jpg",
                "Thursday, October 20, 2022 at 7:03:44 PM Indochina Time",
                -6.1947508,
                106.4867868
            )
            storyList.add(story)
        }
        return storyList
    }

}