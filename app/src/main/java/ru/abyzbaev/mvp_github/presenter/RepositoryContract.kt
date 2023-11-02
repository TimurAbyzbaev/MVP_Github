package ru.abyzbaev.mvp_github.presenter

import io.reactivex.Observable
import ru.abyzbaev.mvp_github.model.SearchResponse
import ru.abyzbaev.mvp_github.repository.RepositoryCallback

interface RepositoryContract {
    fun searchGithub(
        query: String,
        callback: RepositoryCallback
    )

    fun searchGithub(
        query: String
    ): Observable<SearchResponse>
}