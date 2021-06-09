package com.jonathanlee.wellsafe.utils

import com.jonathanlee.wellsafe.data.source.StatsRepository
import com.jonathanlee.wellsafe.data.source.remote.StatsRemoteApiTask

object Injection {
    fun provideStatsRepository(): StatsRepository {
        return StatsRepository.getInstance(StatsRemoteApiTask())
    }
}