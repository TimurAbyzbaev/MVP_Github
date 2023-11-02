package ru.abyzbaev.mvp_github.presenter.search

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import retrofit2.Response
import ru.abyzbaev.mvp_github.model.SearchResponse
import ru.abyzbaev.mvp_github.presenter.RepositoryContract
import ru.abyzbaev.mvp_github.repository.RepositoryCallback
import ru.abyzbaev.mvp_github.view.search.SearchSchedulerProvider
import ru.abyzbaev.mvp_github.view.search.ViewSearchContract
import ru.abyzbaev.mvp_github.rx.SchedulerProvider

internal class SearchPresenter internal constructor(
    private val viewContract: ViewSearchContract,
    private val repository: RepositoryContract,
    private val appSchedulerProvider: SchedulerProvider = SearchSchedulerProvider()
) : PresenterSearchContract, RepositoryCallback {

    private var isAttached = false

    override fun searchGithub(searchQuery: String) {
        if (isAttached) {
            //Dispose
            val compositeDisposable = CompositeDisposable()
            compositeDisposable.add(
                repository.searchGithub(searchQuery)
                    .subscribeOn(appSchedulerProvider.io())
                    .observeOn(appSchedulerProvider.ui())
                    .doOnSubscribe { viewContract.displayLoading(true) }
                    .doOnTerminate { viewContract.displayLoading(false) }
                    .subscribeWith(object : DisposableObserver<SearchResponse>() {
                        override fun onNext(searchResponse: SearchResponse) {
                            val searchResults = searchResponse.searchResults
                            val totalCount = searchResponse.totalCount
                            if (searchResults != null && totalCount != null) {
                                viewContract.displaySearchResults(
                                    searchResults,
                                    totalCount
                                )
                            } else {
                                viewContract.displayError("Search results or total count are null")
                            }
                        }

                        override fun onError(e: Throwable) {
                            viewContract.displayError(
                                e.message ?: "Response is null or unsuccessful"
                            )
                        }

                        override fun onComplete() {}
                    })
            )

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
        if (isAttached) {
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
                viewContract.displayError("Response is null or unsuccessful")
            }
        }

    }

    override fun handleGithubError() {
        viewContract.displayLoading(false)
        viewContract.displayError()
    }
}