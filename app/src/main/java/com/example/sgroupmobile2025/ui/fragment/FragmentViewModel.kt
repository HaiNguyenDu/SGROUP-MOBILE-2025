package com.example.sgroupmobile2025.ui.fragment

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.sgroupmobile2025.data.model.Message
import com.example.sgroupmobile2025.ui.fragment.model.Poster
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FragmentViewModel(application: Application): ViewModel() {

    private val _posters = MutableStateFlow(Poster.listPosters.toMutableList())
    val posters: StateFlow<List<Poster>> = _posters

    fun addPoster(title: String, image: String) {
        val index = Poster.getIndexByTitle(title)
        if (index == -1) return

        val poster = _posters.value[index]

        val newImages = poster.images.toMutableList().apply {
            add(image)
        }

        val updatedPoster = poster.copy(images = newImages)

        val newPosterList = _posters.value.toMutableList()
        newPosterList[index] = updatedPoster

        _posters.value = newPosterList
    }
}
