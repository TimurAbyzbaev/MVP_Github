package ru.abyzbaev.mvp_github.presenter.details

import ru.abyzbaev.mvp_github.view.details.ViewDetailsContract

internal class DetailsPresenter internal constructor(
    private val viewContract: ViewDetailsContract,
    private var count: Int = 0
) : PresenterDetailsContract {
    private var isAttached = false
    override fun setCounter(count: Int) {
        this.count = count
    }

    override fun onIncrement() {
        if (isAttached) {
            count++
            viewContract.setCount(count)
        }
    }

    override fun onDecrement() {
        if (isAttached) {
            count--
            viewContract.setCount(count)
        }
    }

    override fun onAttach() {
        isAttached = true
    }

    override fun onDetach() {
        isAttached = false
    }

}