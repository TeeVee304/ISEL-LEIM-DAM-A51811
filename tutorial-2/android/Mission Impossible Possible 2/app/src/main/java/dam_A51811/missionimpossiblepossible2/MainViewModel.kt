package dam_A51811.missionimpossiblepossible2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dam_A51811.missionimpossiblepossible2.model.CatImageItem
import dam_A51811.missionimpossiblepossible2.network.RetrofitClient
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _images = MutableLiveData<List<CatImageItem>>()
    val images: LiveData<List<CatImageItem>> = _images

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadImages() {
        _isLoading.value = true
        _error.value = null
        viewModelScope.launch {
            try {
                val res = RetrofitClient.apiService.searchImages(limit = 20)
                _images.value = res
            } catch (e: Exception) {
                _error.value = e.localizedMessage
            } finally {
                _isLoading.value = false
            }
        }
    }
}
