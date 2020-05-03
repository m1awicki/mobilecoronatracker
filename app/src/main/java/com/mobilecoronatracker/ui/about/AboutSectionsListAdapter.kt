package com.mobilecoronatracker.ui.about

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobilecoronatracker.databinding.AboutSectionItemBinding

class AboutSectionsListAdapter : RecyclerView.Adapter<AboutSectionsListAdapter.ViewHolder>() {
    var credits = emptyList<AboutViewModelable.AboutSection>()

    inner class ViewHolder(private val binding: AboutSectionItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AboutViewModelable.AboutSection) {
            binding.item = item
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AboutSectionsListAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AboutSectionItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = credits.size

    override fun onBindViewHolder(holder: AboutSectionsListAdapter.ViewHolder, position: Int) = holder.bind(credits[position])
}