package ru.abyzbaev.mvp_github.presenter

import android.content.res.Resources
import retrofit2.Response
import ru.abyzbaev.mvp_github.R
import ru.abyzbaev.mvp_github.model.SearchResponse
import ru.abyzbaev.mvp_github.repository.GitHubRepository
import ru.abyzbaev.mvp_github.view.ViewContract

internal class SearchPresenter internal constructor(
    private val viewContract: ViewContract,
    private val repository: GitHubRepository
) : PresenterContract, GitHubRepository.GitHubRepositoryCallback {

    override fun searchGitHub(searchQuery: String) {
        viewContract.displayLoading(true)
        repository.searchGithub(searchQuery, this)
    }

    override fun handleGitHubResponse(response: Response<SearchResponse?>?) {
        viewContract.displayLoading(false)
        if (response != null && response.isSuccessful) {
            val searchResponse = response.body()
            val searchResults = searchResponse?.searchResults
            val totalCount = searchResponse?.totalCount
            if (searchResults != null && totalCount != null) {
                viewContract.displaySearchResults(searchResults, totalCount)
            } else {
                viewContract.displayError(
                    Resources.getSystem()
                        .getString(R.string.error_null_search_result_or_total_count)
                )
            }
        } else {
            viewContract.displayError(
                Resources.getSystem().getString(R.string.error_null_response_or_unsuccessful)
            )
        }
    }

    override fun handleGitHubError() {
        viewContract.displayLoading(false)
        viewContract.displayError()
    }
}