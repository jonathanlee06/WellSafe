package com.jonathanlee.wellsafe.data.source.remote

import com.jonathanlee.wellsafe.data.source.StatsApiTask
import retrofit2.Call

class StatsRemoteApiTask : StatsApiTask {

    private var apiTask: StatsApiTask = StatsApi.client.create(StatsApiTask::class.java)

    override fun getMalaysiaData(): Call<StatsResponse?> {
        return apiTask.getMalaysiaData()
    }
}