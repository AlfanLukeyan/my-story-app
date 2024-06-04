package com.a004d4ky3983.mystoryapp.ui.main

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.a004d4ky3983.mystoryapp.data.remote.response.ListStoryItem
import com.a004d4ky3983.mystoryapp.databinding.ItemStoriesBinding
import com.a004d4ky3983.mystoryapp.ui.detail.DetailActivity
import com.a004d4ky3983.mystoryapp.util.DateTime

class StoriesAdapter :
    PagingDataAdapter<ListStoryItem, StoriesAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoriesAdapter.MyViewHolder {
        val binding = ItemStoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoriesAdapter.MyViewHolder, position: Int) {
        val story = getItem(position)
        if (story != null) {
            holder.bind(story)
        }

        holder.itemView.setOnClickListener {
            val date = story?.createdAt?.let { DateTime.getDate(it) }
            val data = story?.let { it1 ->
                ListStoryItem(
                    story.photoUrl,
                    date,
                    story.name,
                    story.description,
                    story.lon,
                    it1.id,
                    story.lat
                )
            }

            val intent = Intent(it.context, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_STORY, data)

            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                it.context as Activity,
                androidx.core.util.Pair(holder.binding.imgItemPhoto, "image"),
                androidx.core.util.Pair(holder.binding.tvItemName, "name"),
                androidx.core.util.Pair(holder.binding.storyDescription, "description"),
                androidx.core.util.Pair(holder.binding.storyDate, "date")
            )

            it.context.startActivity(intent, options.toBundle())
        }
    }

    class MyViewHolder(val binding: ItemStoriesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem) {
            val date = story.createdAt?.let { DateTime.getDate(it) }

            binding.tvItemName.text = story.name
            binding.storyDescription.text = story.description
            binding.storyDate.text = date
            Glide.with(binding.root)
                .load(story.photoUrl)
                .into(binding.imgItemPhoto)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}