package ru.abyzbaev.mvp_github.presenter.search

import retrofit2.Response
import ru.abyzbaev.mvp_github.model.SearchResponse
import ru.abyzbaev.mvp_github.presenter.RepositoryContract
import ru.abyzbaev.mvp_github.repository.RepositoryCallback
import ru.abyzbaev.mvp_github.view.search.ViewSearchContract

internal class SearchPresenter internal constructor(
    private val viewContract: ViewSearchContract,
    private val repository: RepositoryContract
) : PresenterSearchContract, RepositoryCallback {

    private var isAttached = false
    override fun searchGitHub(searchQuery: String) {
        if(isAttached) {
            viewContract.displayLoading(true)
            repository.searchGithub(searchQuery, this)
        }
    }

    override fun onAttach() {
        isAttached = true
    }

    override fun onDetach() {
        isAttached = false
    }

    override fun handleGithubResponse(response: Response<SearchResponse?>?) {
        if(isAttached) {
            viewContract.displayLoading(false)
            if (response != null && response.isSuccessful) {
                val searchResponse = response.body()
                val searchResults = searchResponse?.searchResults
                val totalCount = searchResponse?.totalCount
                if (searchResults != null && totalCount != null) {
                    viewContract.displaySearchResults(searchResults, totalCount)
                } else {
                    viewContract.displayError("Search results or total count are null")
                }
            } else {
                viewContract.displayError("Response is null or unsuccessful" )
            }
        }

    }

    override fun handleGithubError() {
        viewContract.displayLoading(false)
        viewContract.displayError()
    }
}