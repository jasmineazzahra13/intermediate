package com.dicoding.intermediate.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.intermediate.data.md.Story
import com.dicoding.intermediate.databinding.ItemStoryBinding
import com.dicoding.intermediate.utils.helper.getTimeAgo
import com.dicoding.intermediate.utils.helper.setImageUrl

class StoryPagingAdapt(
    private val onClick: (String) -> Unit
) :
    PagingDataAdapter<Story, StoryPagingAdapt.StoryViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    inner class StoryViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: Story) {
            binding.apply {
                imgStory.setImageUrl(story.photoUrl)
                imgStory.setOnClickListener {
                    onClick(story.id)
                }
                tvUserName.text = story.name
                tvUploadAt.text = story.createdAt.getTimeAgo(root.context)
                tvDescription.text = story.description
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(
                oldItem: Story,
                newItem: Story
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: Story,
                newItem: Story
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}