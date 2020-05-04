package com.mobilecoronatracker.ui.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.mobilecoronatracker.R
import com.mobilecoronatracker.databinding.FragmentAboutBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class AboutFragment : Fragment() {
    private val viewModel by viewModel<AboutViewModel>()
    private val adapter = AboutSectionsListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentAboutBinding>(inflater,
            R.layout.fragment_about, container, false
        )
        binding.viewModel = viewModel
        binding.adapter = adapter
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindObservers()
    }

    private fun bindObservers() {
        viewModel.credits.observe(viewLifecycleOwner, Observer {
            adapter.credits = it
            adapter.notifyDataSetChanged()
        })
        viewModel.onOpenUrl.observe(viewLifecycleOwner, Observer { url ->
            if (url.isEmpty()) {
                return@Observer
            }
            val uri = Uri.parse(url)
            viewModel.onOpenUrl.value = ""
            startActivity(Intent(Intent.ACTION_VIEW, uri))
        })
    }
}