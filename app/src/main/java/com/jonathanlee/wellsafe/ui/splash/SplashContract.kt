package com.jonathanlee.wellsafe.ui.splash

import com.jonathanlee.wellsafe.BaseContract
import com.jonathanlee.wellsafe.data.model.Stats

interface SplashContract {
    interface View : BaseContract.View<Presenter> {
        fun onGetMalaysiaDataSuccess(stats: Stats)

        fun onGetMalaysiaDataFailure(errorMsg: String?)
    }

    interface Presenter : BaseContract.Presenter {
        fun getMalaysiaData()
    }
}