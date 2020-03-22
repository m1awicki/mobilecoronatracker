package com.mobilecoronatracker.ui.cumulatedreport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.mobilecoronatracker.R
import com.mobilecoronatracker.databinding.FragmentCumulatedReportBinding
import kotlinx.android.synthetic.main.fragment_countries_reports.*
import kotlinx.android.synthetic.main.fragment_cumulated_report.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentCumulatedReport : Fragment() {
    private val viewModel: CumulatedReportViewModelable by viewModel<CumulatedReportViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        val binding = DataBindingUtil.inflate<FragmentCumulatedReportBinding>(
            inflater, R.layout.fragment_cumulated_report, container, false
        )
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
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
        cumulated_report_swipe_refresh.setOnRefreshListener {
            viewModel.onRefreshRequested()
        }
    }
}
