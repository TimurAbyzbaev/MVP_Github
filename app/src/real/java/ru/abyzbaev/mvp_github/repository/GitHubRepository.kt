package ru.abyzbaev.mvp_github.repository

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.abyzbaev.mvp_github.model.SearchResponse
import ru.abyzbaev.mvp_github.presenter.RepositoryContract

internal class GitHubRepository(private val gitHubApi: GitHubApi): RepositoryContract {
    override fun searchGithub(
        query: String,
        callback: RepositoryCallback
    ) {
        val call = gitHubApi.searchGithub(query)
        call?.enqueue(object : Callback<SearchResponse?> {

            override fun onResponse(
                call: Call<SearchResponse?>,
                response: Response<SearchResponse?>
            ) {
                callback.handleGithubResponse(response)
            }

            override fun onFailure(
                call: Call<SearchResponse?>,
                t: Throwable
            ) {
                callback.handleGithubError()
            }
        })
    }

    interface GitHubRepositoryCallback {
        fun handleGitHubResponse(response: Response<SearchResponse?>?)
        fun handleGitHubError()
    }
}