package ru.abyzbaev.mvp_github

import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import ru.abyzbaev.mvp_github.model.SearchResponse
import ru.abyzbaev.mvp_github.presenter.search.SearchPresenter
import ru.abyzbaev.mvp_github.repository.GitHubRepository
import ru.abyzbaev.mvp_github.presenter.stubs.ScheduleProviderStub
import ru.abyzbaev.mvp_github.view.search.ViewSearchContract

class SearchPresenterTestRx {
    private lateinit var presenter: SearchPresenter

    @Mock
    private lateinit var repository: GitHubRepository

    @Mock
    private lateinit var viewContract: ViewSearchContract

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        presenter = SearchPresenter(viewContract, repository, ScheduleProviderStub())
    }

    @Test
    fun searchGitHub_Test() {
        `when`(repository.searchGithub(SEARCH_QUERY)).thenReturn(
            Observable.just(
                SearchResponse(
                    1,
                    listOf()
                )
            )
        )
        presenter.onAttach()
        presenter.searchGithub(SEARCH_QUERY)
        verify(repository, times(1)).searchGithub(SEARCH_QUERY)
    }

    @Test //Проверяем как обрабатывается ошибка запроса
    fun handleRequestError_Test() {
        `when`(repository.searchGithub(SEARCH_QUERY)).thenReturn(
            Observable.error(Throwable(ERROR_TEXT))
        )
        presenter.onAttach()
        presenter.searchGithub(SEARCH_QUERY)
        verify(viewContract, times(1)).displayError("error")
    }

    @Test //Проверяем как обрабатываются неполные данные
    fun handleResponseError_TotalCountIsNull() {
        `when`(repository.searchGithub(SEARCH_QUERY)).thenReturn(
            Observable.just(
                SearchResponse(
                    null,
                    listOf()
                )
            )
        )
        presenter.onAttach()
        presenter.searchGithub(SEARCH_QUERY)
        verify(viewContract, times(1)).displayError("Search results or total count are null")
    }

    @Test //Проверим порядок вызова методов viewContract при ошибке
    fun handleResponseError_TotalCountIsNull_ViewContractMethodOrder() {
        `when`(repository.searchGithub(SEARCH_QUERY)).thenReturn(
            Observable.just(
                SearchResponse(
                    null,
                    listOf()
                )
            )
        )
        presenter.onAttach()
        presenter.searchGithub(SEARCH_QUERY)
        val inOrder = inOrder(viewContract)
        inOrder.verify(viewContract).displayLoading(true)
        inOrder.verify(viewContract).displayError("Search results or total count are null")
        inOrder.verify(viewContract).displayLoading(false)
    }

    @Test //Теперь проверим успешный ответ сервера
    fun handleResponseSuccess() {
        `when`(repository.searchGithub(SEARCH_QUERY)).thenReturn(
            Observable.just(
                SearchResponse(
                    TEST_NUMBER,
                    listOf()
                )
            )
        )
        presenter.onAttach()
        presenter.searchGithub(SEARCH_QUERY)
        verify(viewContract, times(1)).displaySearchResults(listOf(), TEST_NUMBER)
    }

    companion object {
        private const val SEARCH_QUERY = "some query"
        private const val ERROR_TEXT = "error"
    }
}