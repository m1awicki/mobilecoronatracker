package com.mobilecoronatracker.ui.about

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobilecoronatracker.databinding.ItemCreditsBinding
import com.mobilecoronatracker.model.CreditsEntryModel

class CreditsListAdapter : RecyclerView.Adapter<CreditsListAdapter.ViewHolder>() {
    var credits = emptyList<CreditsEntryModel>()

    inner class ViewHolder(private val binding: ItemCreditsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CreditsEntryModel) {
            binding.header.text = item.header
            binding.body.text = item.body
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreditsListAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCreditsBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = credits.size

    override fun onBindViewHolder(holder: CreditsListAdapter.ViewHolder, position: Int) = holder.bind(credits[position])
}