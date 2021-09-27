package com.mobilecoronatracker.ui.countrieslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mobilecoronatracker.R
import com.mobilecoronatracker.databinding.FragmentCountriesListBinding
import com.mobilecoronatracker.ui.utils.hideKeyboard
import org.koin.androidx.viewmodel.ext.android.viewModel

class CountriesListFragment : Fragment() {
    private val viewModel: CountriesListViewModelable by viewModel<CountriesListViewModel>()
    private val adapter by lazy {
        CountriesListAdapter()
    }
    private var _binding: FragmentCountriesListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        _binding = FragmentCountriesListBinding.inflate(inflater, container, false)
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


    override fun onDestroyView() {
        super.onDestroyView()
        adapter.followListener = null
        adapter.shareReportListener = null
        adapter.countryAnalysisRequestListener = null
        _binding = null
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
        binding.countriesSearchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(text: String?): Boolean {
                viewModel.onFilterTextChanged(text ?: "")
                context?.let { hideKeyboard(it, binding.countriesSearchView) }
                return true
            }

            override fun onQueryTextChange(text: String?): Boolean {
                viewModel.onFilterTextChanged(text ?: "")
                return true
            }
        })

        binding.countriesReportsSwipeRefresh.setOnRefreshListener {
            viewModel.onRefreshRequested()
        }
    }

    private fun bindObservers() {
        viewModel.countryReports.observe(viewLifecycleOwner, {
            adapter.countriesReports = it
            adapter.notifyDataSetChanged()
        })
        viewModel.navigationToCountryRequested.observe(viewLifecycleOwner, {
            if (it.isBlank()) {
                return@observe
            }
            val action =
                CountriesListFragmentDirections.actionNavigationCountriesListToCountryAnalysisFragment(
                    it
                )
            viewModel.navigationToCountryRequested.value = ""
            findNavController().navigate(action)
        })
    }
}
