package com.a004d4ky3983.mystoryapp.ui.detail

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.a004d4ky3983.mystoryapp.R
import com.a004d4ky3983.mystoryapp.data.remote.response.ListStoryItem
import com.a004d4ky3983.mystoryapp.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storyItem = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_STORY, ListStoryItem::class.java)
        } else {
            intent.getParcelableExtra(EXTRA_STORY)
        }

        getDetailStory(storyItem!!)

        supportActionBar?.title = getString(R.string.title_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun getDetailStory(detail: ListStoryItem) {
        binding.apply {
            tvItemName.text = detail.name
            storyDescription.text = detail.description
            storyDate.text = detail.createdAt
            Glide.with(this@DetailActivity)
                .load(detail.photoUrl)
                .into(imgItemPhoto)
        }
    }

    companion object {
        var EXTRA_STORY = "extra_story"
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}