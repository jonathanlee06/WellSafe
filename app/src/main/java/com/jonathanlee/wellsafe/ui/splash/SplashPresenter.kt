package com.jonathanlee.wellsafe.ui.splash

import android.util.Log
import com.jonathanlee.wellsafe.data.source.StatsRepository
import com.jonathanlee.wellsafe.data.source.remote.StatsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashPresenter(
    private val statsRepository: StatsRepository,
    private val splashView: SplashContract.View
) : SplashContract.Presenter {

    init {
        splashView.presenter = this
    }

    override fun getMalaysiaData() {
        val getData = statsRepository.getMalaysiaData()
        getData.enqueue(object : Callback<StatsResponse?> {
            override fun onResponse(
                call: Call<StatsResponse?>,
                response: Response<StatsResponse?>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()?.data
                    result?.let {
                        splashView.onGetMalaysiaDataSuccess(it)
                    }
                }
            }

            override fun onFailure(call: Call<StatsResponse?>, t: Throwable) {
                splashView.onGetMalaysiaDataFailure(t.toString())
            }

        })
    }

}