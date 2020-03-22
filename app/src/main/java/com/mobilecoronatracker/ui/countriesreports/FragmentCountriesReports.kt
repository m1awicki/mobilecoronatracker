package com.mobilecoronatracker.ui.countriesreports

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.mobilecoronatracker.R
import com.mobilecoronatracker.databinding.FragmentCountriesReportsBinding
import com.mobilecoronatracker.ui.utils.hideKeyboard
import kotlinx.android.synthetic.main.fragment_countries_reports.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentCountriesReports : Fragment() {
    private val viewModel: CountriesListViewModelable by viewModel<CountriesListViewModel>()
    private val adapter by lazy {
        CountriesListAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        val binding = DataBindingUtil.inflate<FragmentCountriesReportsBinding>(
            inflater, R.layout.fragment_countries_reports, container, false
        )
        adapter.listener = viewModel
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

    override fun onDestroyView() {
        super.onDestroyView()
        adapter.listener = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.reports_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_refresh -> {
                viewModel.onRefreshRequested()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupViews() {
        countries_reports_search_view.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(text: String?): Boolean {
                viewModel.onFilterTextChanged(text ?: "")
                context?.let { hideKeyboard(it, countries_reports_search_view) }
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
            adapter.reports = it
            adapter.notifyDataSetChanged()
        })
    }
}
