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
            CreditsEntryModelImpl(getString(R.string.icon_header), "made by Smashicons from www.flaticon.com")
            , CreditsEntryModelImpl(getString(R.string.icon_header), "made by ultimatearm from www.flaticon.com")
            , CreditsEntryModelImpl(getString(R.string.image_header), "made by ultimatearm from www.flaticon.com")
            , CreditsEntryModelImpl(getString(R.string.data_source), "provided by https://github.com/NovelCOVID/API")
        )
        credits.postValue(list)
    }

    private fun getString(stringId: Int): String {
        val context = getApplication<Application>().baseContext
        return context.getString(stringId)
    }
}