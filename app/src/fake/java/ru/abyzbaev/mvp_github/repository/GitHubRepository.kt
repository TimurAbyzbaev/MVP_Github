package ru.abyzbaev.mvp_github.repository

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.abyzbaev.mvp_github.model.SearchResponse
import ru.abyzbaev.mvp_github.model.SearchResult
import ru.abyzbaev.mvp_github.presenter.RepositoryContract
import kotlin.random.Random

internal class GitHubRepository(private val gitHubApi: GitHubApi) : RepositoryContract {
    override fun searchGithub(query: String, callback: RepositoryCallback) {
        callback.handleGithubResponse(Response.success(getFakeResponse()))
    }

    override fun searchGithub(query: String): Observable<SearchResponse> {
        return Observable.just(getFakeResponse())
    }

    private fun getFakeResponse(): SearchResponse {
        val list: MutableList<SearchResult> = mutableListOf()
        for (index in 1..100) {
            list.add(
                SearchResult(
                    id = index,
                    name = "Name: $index",
                    fullName = "FullName: $index",
                    private = Random.nextBoolean(),
                    description = "Description: $index",
                    updatedAt = "UpdatedAt: $index",
                    size = index,
                    stargazersCount = Random.nextInt(),
                    language = "",
                    hasWiki = Random.nextBoolean(),
                    archived = Random.nextBoolean(),
                    score = index.toDouble()
                )
            )
        }
        return SearchResponse(list.size, list)
    }

    interface GitHubRepositoryCallback {
        fun handleGitHubResponse(response: Response<SearchResponse?>?)
        fun handleGitHubError()
    }
}