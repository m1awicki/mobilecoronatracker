package com.mobilecoronatracker.ui.cumulatedreport

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.mobilecoronatracker.R
import com.mobilecoronatracker.databinding.ExperimentalFragmentBinding
import com.mobilecoronatracker.ui.chart.williamchart.view.ImplementsAlphaChart
import kotlinx.android.synthetic.main.experimental_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

@ImplementsAlphaChart
class FragmentCumulatedReport : Fragment() {
    private val viewModel: CumulatedReportViewModelable by viewModel<CumulatedReportViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        val binding = DataBindingUtil.inflate<ExperimentalFragmentBinding>(
            inflater, R.layout.experimental_fragment, container, false
        )
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        general_info_chart.donutColors = intArrayOf(
            Color.parseColor("#FFFF8C5E"),//existing
            Color.parseColor("#FF368E4C"),//recovered
            Color.parseColor("#FFC62121") //dead
        )
        general_info_chart.animation.duration = 1500L
        viewModel.recovered.observe(viewLifecycleOwner, Observer {
            if (it != "???") {
                val donutdata = listOf(
                    viewModel.deaths.value?.toFloat() ?: 0f,
                    it.toFloat(),
                    (viewModel.cases.value?.toFloat() ?: 0f) - it.toFloat() - (viewModel.deaths.value?.toFloat() ?: 0f)
                )
                general_info_chart.donutTotal = (viewModel.cases.value?.toFloat() ?: 0f) - it.toFloat() - (viewModel.deaths.value?.toFloat() ?: 0f)
                general_info_chart.animate(
                    donutdata
                )
            }
        })
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
