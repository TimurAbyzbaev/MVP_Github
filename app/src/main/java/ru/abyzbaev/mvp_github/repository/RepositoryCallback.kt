package ru.abyzbaev.mvp_github.repository

import retrofit2.Response
import ru.abyzbaev.mvp_github.model.SearchResponse

interface RepositoryCallback {
    fun handleGithubResponse(response: Response<SearchResponse?>?)
    fun handleGithubError()

}