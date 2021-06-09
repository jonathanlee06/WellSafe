package com.jonathanlee.wellsafe

interface BaseContract {
    interface View<T> {
        var presenter: T
    }

    interface Presenter
}