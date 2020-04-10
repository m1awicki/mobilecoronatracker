package com.mobilecoronatracker.ui.countrieslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.mobilecoronatracker.databinding.ItemCountryReportBinding

class CountriesListAdapter :
    RecyclerView.Adapter<CountriesListAdapter.ViewHolder>()
{
    var deltas = emptyList<CountriesListViewModelable.CountryReport>()
    var followListener: CountryFollowListener? = null
    var shareReportListener: ShareCountryReportListener? = null
    var countryAnalysisRequestListener: ShowCountryAnalysisListener? = null

    inner class ViewHolder(private val binding: ItemCountryReportBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CountriesListViewModelable.CountryReport) {
            if (item.countryReport.flagPath.isNotEmpty()) {
                binding.countryFlag.load(item.countryReport.flagPath)
            }
            binding.alwaysVisible.setOnClickListener {
                val visibility = binding.collapsibleContainer.visibility
                if (visibility == View.VISIBLE) {
                    setDetailsVisible(binding, false)
                } else {
                    setDetailsVisible(binding, true)
                }
            }
            binding.item = item
            binding.followListener = followListener
            binding.shareListener = shareReportListener
            binding.analysisRequestListener = countryAnalysisRequestListener
            if (item.countryReport.followed) {
                setDetailsVisible(binding, true)
            } else {
                setDetailsVisible(binding, false)
            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCountryReportBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = deltas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(deltas[position])

    private fun setDetailsVisible(detailsContainer: ItemCountryReportBinding, visible: Boolean) {
        if (visible) {
            detailsContainer.collapsibleContainer.visibility = View.VISIBLE
        } else {
            detailsContainer.collapsibleContainer.visibility = View.GONE
        }
    }
}