package ru.abyzbaev.mvp_github.presenter.details

import ru.abyzbaev.mvp_github.presenter.PresenterContract

interface PresenterDetailsContract : PresenterContract {
    fun setCounter(count: Int)
    fun onIncrement()
    fun onDecrement()
}