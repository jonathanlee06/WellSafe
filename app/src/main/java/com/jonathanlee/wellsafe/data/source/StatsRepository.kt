package com.jonathanlee.wellsafe.data.source

import com.jonathanlee.wellsafe.data.source.remote.StatsRemoteApiTask
import com.jonathanlee.wellsafe.data.source.remote.StatsResponse
import retrofit2.Call

class StatsRepository private constructor(private val remoteDataSource: StatsRemoteApiTask)
    : StatsApiTask {

    companion object {
        private var INSTANCE: StatsRepository? = null

        /**
         * Returns the single instance of this class, creating it if necessary.

         * @param tasksRemoteDataSource the backend data source
         * *
         * @param tasksLocalDataSource  the device storage data source
         * *
         * @return the [TasksRepository] instance
         */
        @JvmStatic
        fun getInstance(statsRemoteDataSource: StatsRemoteApiTask): StatsRepository {
            return INSTANCE ?: StatsRepository(statsRemoteDataSource)
                .apply { INSTANCE = this }


        }

        /**
         * Used to force [getInstance] to create a new instance
         * next time it's called.
         */
        @JvmStatic
        fun newInstance(statsRemoteDataSource: StatsRemoteApiTask): StatsRepository {
            INSTANCE = null
            return getInstance(statsRemoteDataSource)
        }
    }

    override fun getMalaysiaData(): Call<StatsResponse?> {
        return remoteDataSource.getMalaysiaData()
    }

}