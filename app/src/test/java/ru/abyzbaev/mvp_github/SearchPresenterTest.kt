package ru.abyzbaev.mvp_github

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import retrofit2.Response
import ru.abyzbaev.mvp_github.model.SearchResponse
import ru.abyzbaev.mvp_github.model.SearchResult
import ru.abyzbaev.mvp_github.presenter.search.SearchPresenter
import ru.abyzbaev.mvp_github.repository.GitHubRepository
import ru.abyzbaev.mvp_github.view.search.ViewSearchContract

class SearchPresenterTest {
    private lateinit var presenter: SearchPresenter

    @Mock
    private lateinit var repository: GitHubRepository

    @Mock
    private lateinit var viewContract: ViewSearchContract

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        presenter = SearchPresenter(viewContract, repository)
    }

    @Test //Проверка вызова метода searchGitHub() у нашего Репозитория
    fun searchGitHub_Test() {
        presenter.onAttach()
        presenter.searchGithub(TEST_QUERY)
        verify(repository, times(1)).searchGithub(TEST_QUERY, presenter)
    }

    @Test // Проверка работы метода handleGitHubError()
    fun handleGitHubError_Test() {
        presenter.handleGithubError()
        verify(viewContract, times(1)).displayError()
    }

    @Test //Проверяем как приходит ответ с сервера
    fun handleGitHubResponse_ResponseUnsuccessful() {
        val response = mock(Response::class.java) as Response<SearchResponse?>
        `when`(response.isSuccessful).thenReturn(false)
        assertFalse(response.isSuccessful)
    }

    @Test //Проверяем как обрабатываются ошибки
    fun handleGitHubResponse_Failure() {
        val response = mock(Response::class.java) as Response<SearchResponse?>
        `when`(response.isSuccessful).thenReturn(false)
        presenter.onAttach()
        presenter.handleGithubResponse(response)
        verify(viewContract, times(1))
            .displayError("Response is null or unsuccessful")
    }

    @Test//Проверяем порядок вызова методов viewContract
    fun handleGitHubResponse_Failure_ViewContractMethodOrder() {
        val response = mock(Response::class.java) as Response<SearchResponse?>
        `when`(response.isSuccessful).thenReturn(false)

        presenter.onAttach()
        presenter.handleGithubResponse(response)

        val inOrder = inOrder(viewContract)
        inOrder.verify(viewContract).displayLoading(false)
        inOrder.verify(viewContract).displayError("Response is null or unsuccessful")
    }

    @Test //Проверим пустой ответ с сервера
    fun handleGitHubResponse_ResponseIsEmpty() {
        val response = mock(Response::class.java) as Response<SearchResponse?>
        `when`(response.body()).thenReturn(null)
        presenter.handleGithubResponse(response)
        assertNull(response.body())
    }

    @Test //Проверим непустой ответ с сервера
    fun handleGitHubResponse_ResponseIsNotEmpty() {
        val response = mock(Response::class.java) as Response<SearchResponse?>
        `when`(response.body()).thenReturn(mock(SearchResponse::class.java))
        presenter.handleGithubResponse(response)
        assertNotNull(response.body())
    }

    @Test //Проверка обработки пустого ответа с сервера
    fun handleGitHubResponse_EmptyResponse() {
        val response = mock(Response::class.java) as Response<SearchResponse?>
        `when`(response.isSuccessful).thenReturn(true)
        `when`(response.body()).thenReturn(null)

        presenter.onAttach()
        presenter.handleGithubResponse(response)
        verify(viewContract, times(1))
            .displayError("Search results or total count are null")
    }
    @Test //Проверка успешного ответа
    fun handleGitHubResponse_Success(){
        val response = mock(Response::class.java) as Response<SearchResponse?>
        val searchResponse = mock(SearchResponse::class.java)
        val searchResults = listOf(mock(SearchResult::class.java))

        `when`(response.isSuccessful).thenReturn(true)
        `when`(response.body()).thenReturn(searchResponse)
        `when`(searchResponse.searchResults).thenReturn(searchResults)
        `when`(searchResponse.totalCount).thenReturn(TEST_NUMBER)

        presenter.onAttach()
        presenter.handleGithubResponse(response)
        verify(viewContract, times(1))
            .displaySearchResults(searchResults,TEST_NUMBER)
    }
}