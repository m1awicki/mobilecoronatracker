package com.mobilecoronatracker.ui.countrieslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.mobilecoronatracker.R
import com.mobilecoronatracker.databinding.FragmentCountriesListBinding
import com.mobilecoronatracker.ui.utils.hideKeyboard
import kotlinx.android.synthetic.main.fragment_countries_list.*
import kotlinx.android.synthetic.main.item_country_report.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentCountriesList : Fragment() {
    private val viewModel: CountriesListViewModelable by viewModel<CountriesListViewModel>()
    private val adapter by lazy {
        CountriesListAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentCountriesListBinding>(
            inflater, R.layout.fragment_countries_list, container, false
        )
        adapter.followListener = viewModel
        adapter.shareReportListener = viewModel
        adapter.countryAnalysisRequestListener = viewModel
        binding.adapter = adapter
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        bindObservers()
    }

    private fun setupViews() {
        countries_search_view.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(text: String?): Boolean {
                viewModel.onFilterTextChanged(text ?: "")
                context?.let { hideKeyboard(it, countries_search_view) }
                return true
            }

            override fun onQueryTextChange(text: String?): Boolean {
                viewModel.onFilterTextChanged(text ?: "")
                return true
            }
        })

        countries_reports_swipe_refresh.setOnRefreshListener {
            viewModel.onRefreshRequested()
        }
    }

    private fun bindObservers() {
        viewModel.countryReports.observe(viewLifecycleOwner, Observer {
            adapter.deltas = it
            adapter.notifyDataSetChanged()
        })
        viewModel.toggleCollapse.observe(viewLifecycleOwner, Observer {
            if (collapsible_container.visibility == View.VISIBLE) {
                collapsible_container.visibility = View.GONE
            } else {
                collapsible_container.visibility = View.VISIBLE
            }
        })
    }
}