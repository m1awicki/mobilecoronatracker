package com.mobilecoronatracker.ui.countriesreports

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobilecoronatracker.databinding.ItemCountryReportBinding
import com.mobilecoronatracker.model.CountryReportModelable

class CountriesListAdapter : RecyclerView.Adapter<CountriesListAdapter.ViewHolder>() {
    var reports = emptyList<CountryReportModelable>()

    inner class ViewHolder(private val binding: ItemCountryReportBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CountryReportModelable) {
            binding.item = item
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCountryReportBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = reports.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(reports[position])
}