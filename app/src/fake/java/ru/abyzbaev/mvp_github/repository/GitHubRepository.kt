package ru.abyzbaev.mvp_github.repository

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.abyzbaev.mvp_github.model.SearchResponse
import ru.abyzbaev.mvp_github.presenter.RepositoryContract

internal class GitHubRepository(private val gitHubApi: GitHubApi): RepositoryContract {
    override fun
            searchGithub(query: String, callback: RepositoryCallback) {
        callback.handleGithubResponse(Response.success(SearchResponse(999, listOf())))
    }

    interface GitHubRepositoryCallback {
        fun handleGitHubResponse(response: Response<SearchResponse?>?)
        fun handleGitHubError()
    }
}