package ru.abyzbaev.mvp_github.view.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.abyzbaev.mvp_github.R
import ru.abyzbaev.mvp_github.databinding.ActivityDetailsBinding

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction()
            .add(
                R.id.detailsFragmentContainer,
                DetailsFragment.newInstance(intent.getIntExtra(TOTAL_COUNT_EXTRA, 0))
            )
            .commitAllowingStateLoss()
    }

    companion object {
        const val TOTAL_COUNT_EXTRA = "TOTAL_COUNT_EXTRA"
        fun getIntent(context: Context, totalCount: Int): Intent {
            return Intent(context, DetailsActivity::class.java).apply {
                putExtra(TOTAL_COUNT_EXTRA, totalCount)
            }
        }
    }
}