package ru.abyzbaev.mvp_github.presenter

import ru.abyzbaev.mvp_github.repository.RepositoryCallback

internal interface RepositoryContract {
    fun searchGithub(
        query: String,
        callback: RepositoryCallback
    )
}