package com.mobilecoronatracker.ui.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.mobilecoronatracker.R
import com.mobilecoronatracker.databinding.FragmentAboutBinding
import kotlinx.android.synthetic.main.fragment_about.*
import com.mobilecoronatracker.ui.utils.addLinksNoAppend
import java.util.regex.Pattern

class AboutFragment : Fragment() {
    private lateinit var viewModel: CreditsListViewModel
    private val adapter = CreditsListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentAboutBinding>(inflater,
            R.layout.fragment_about, container, false
        )
        viewModel = CreditsListViewModel(requireActivity().application)
        binding.adapter = adapter
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pattern = Pattern.compile(getString(R.string.github_repo_label))
        addLinksNoAppend(gh_link, pattern, getString(R.string.repo_link))
        bindObservers()
    }

    private fun bindObservers() {
        viewModel.credits.observe(viewLifecycleOwner, Observer {
            adapter.credits = it
            adapter.notifyDataSetChanged()
        })
    }
}