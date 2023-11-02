package ru.abyzbaev.mvp_github.view.search

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import ru.abyzbaev.mvp_github.BuildConfig
import ru.abyzbaev.mvp_github.R
import ru.abyzbaev.mvp_github.databinding.ActivityMainBinding
import ru.abyzbaev.mvp_github.model.SearchResult
import ru.abyzbaev.mvp_github.presenter.RepositoryContract
import ru.abyzbaev.mvp_github.presenter.search.SearchPresenter
import ru.abyzbaev.mvp_github.repository.GitHubApi
import ru.abyzbaev.mvp_github.repository.GitHubRepository
import ru.abyzbaev.mvp_github.view.details.DetailsActivity
import ru.abyzbaev.mvp_github.viewmodel.ScreenState
import ru.abyzbaev.mvp_github.viewmodel.SearchViewModel
import java.util.*

class MainActivity : AppCompatActivity(), ViewSearchContract {

    private val adapter = SearchResultAdapter()
    private var totalCount: Int = 0
    private val viewModel: SearchViewModel by lazy {
        ViewModelProvider(this).get(SearchViewModel::class.java)
    }

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUI()
        viewModel.subscribeToLiveData().observe(this) { onStateChange(it) }
    }

    private fun onStateChange(screenState: ScreenState) {
        when (screenState) {
            is ScreenState.Working -> {
                val searchResponse = screenState.searchResponse
                val totalCount = searchResponse.totalCount
                binding.progressBar.visibility = View.GONE
                with(binding.totalCountTextViewMainActivity) {
                    visibility = View.VISIBLE
                    text = String.format(
                        Locale.getDefault(),
                        getString(R.string.results_count),
                        totalCount
                    )
                }

                this.totalCount = totalCount!!
                adapter.updateResults(searchResponse.searchResults!!)
            }
            is ScreenState.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
            }
            is ScreenState.Error -> {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, screenState.error.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    private fun createRepository(): RepositoryContract {
        return GitHubRepository(createRetrofit().create(GitHubApi::class.java))
    }

    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun setUI() {
        binding.toDetailsActivityButton.setOnClickListener {
            startActivity(DetailsActivity.getIntent(this, totalCount))
        }
        binding.searchButton.setOnClickListener {
            searchTask()
        }
        setQueryListener()
        setRecyclerView()
    }

    private fun setRecyclerView() {
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.adapter = adapter
    }

    private fun searchTask(): Boolean {
        val query = binding.searchEditText.text.toString()
        return if (query.isNotBlank()) {
            viewModel.searchGithub(query)
            true
        } else {
            Toast.makeText(
                this,
                getString(R.string.enter_search_word),
                Toast.LENGTH_SHORT
            ).show()
            false
        }
    }

    private fun setQueryListener() {
        binding.searchEditText.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchTask()
            }
            false
        })
    }

    override fun displaySearchResults(searchResults: List<SearchResult>, totalCount: Int) {
        with(binding.totalCountTextViewMainActivity) {
            visibility = View.VISIBLE
            text = String.format(Locale.getDefault(), getString(R.string.results_count), totalCount)
        }

        this.totalCount = totalCount
        adapter.updateResults(searchResults)
    }

    override fun displayError() {
        Toast.makeText(this, getString(R.string.undefined_error), Toast.LENGTH_SHORT).show()
    }

    override fun displayError(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

    override fun displayLoading(show: Boolean) {
        if (show) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        const val BASE_URL = "https://api.github.com"
    }
}