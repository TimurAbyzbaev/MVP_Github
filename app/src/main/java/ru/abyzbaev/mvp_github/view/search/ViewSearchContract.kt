package ru.abyzbaev.mvp_github.view.search

import ru.abyzbaev.mvp_github.model.SearchResult
import ru.abyzbaev.mvp_github.view.ViewContract

interface ViewSearchContract : ViewContract {
    fun displaySearchResults(
        searchResults: List<SearchResult>,
        totalCount: Int
    )

    fun displayError()
    fun displayError(error: String)
    fun displayLoading(show: Boolean)
}