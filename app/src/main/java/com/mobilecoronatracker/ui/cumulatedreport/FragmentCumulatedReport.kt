package com.mobilecoronatracker.ui.cumulatedreport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.mobilecoronatracker.R
import com.mobilecoronatracker.databinding.FragmentCumulatedReportBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentCumulatedReport : Fragment() {
    private val viewModel: CumulatedReportViewModelable by viewModel<CumulatedReportViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentCumulatedReportBinding>(
            inflater, R.layout.fragment_cumulated_report, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
}
