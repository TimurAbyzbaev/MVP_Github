package ru.abyzbaev.mvp_github.repository

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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

    override fun searchGithub(query: String): Observable<SearchResponse> {
        return gitHubApi.searchGithubRx(query)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override suspend fun searchGithubAsync(query: String): SearchResponse {
        return gitHubApi.searchGithubAsync(query).await()
    }

    interface GitHubRepositoryCallback {
        fun handleGitHubResponse(response: Response<SearchResponse?>?)
        fun handleGitHubError()
    }
}