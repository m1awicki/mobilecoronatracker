package com.mobilecoronatracker.ui.countriesreports

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.mobilecoronatracker.R
import com.mobilecoronatracker.databinding.FragmentCountriesReportsBinding

class FragmentCountriesReports : Fragment() {
    private val viewModel = CountriesListViewModel()
    private val adapter = CountriesListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentCountriesReportsBinding>(
            inflater, R.layout.fragment_countries_reports, container, false)
        binding.adapter = adapter
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindObservers()
    }

    private fun bindObservers() {
        viewModel.countryReports.observe(this, Observer {
            adapter.reports = it
            adapter.notifyDataSetChanged()
        })
    }
}