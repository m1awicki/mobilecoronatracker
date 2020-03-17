package com.mobilecoronatracker.ui.about

import android.app.Application
import androidx.annotation.NonNull
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.mobilecoronatracker.R
import com.mobilecoronatracker.model.CreditsEntryModel
import com.mobilecoronatracker.model.CreditsEntryModelImpl

class CreditsListViewModel(@NonNull app: Application) : AndroidViewModel(app), CreditsListViewModelable {
    override val credits = MutableLiveData<List<CreditsEntryModel>>()

    init {
        val list = listOf<CreditsEntryModel>(
            CreditsEntryModelImpl(getString(R.string.icon_header), getString(R.string.credits_smashicons_from_flaticon)),
            CreditsEntryModelImpl(getString(R.string.icon_header), getString(R.string.credits_ultimatearm_from_flaticon)),
            CreditsEntryModelImpl(getString(R.string.icon_header), getString(R.string.credits_freepik_from_flaticon)),
            CreditsEntryModelImpl(getString(R.string.image_header), getString(R.string.credits_freepik_from_freepik)),
            CreditsEntryModelImpl(getString(R.string.data_source), getString(R.string.credits_elite_da_myth_github))
        )
        credits.postValue(list)
    }

    private fun getString(stringId: Int): String {
        val context = getApplication<Application>().baseContext
        return context.getString(stringId)
    }
}