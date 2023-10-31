package ru.abyzbaev.mvp_github.presenter.search

import ru.abyzbaev.mvp_github.presenter.PresenterContract

internal interface PresenterSearchContract : PresenterContract {
    fun searchGithub(searchQuery: String)
    //onAttach
    //onDetach
}