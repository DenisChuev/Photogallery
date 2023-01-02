package dc.photogallery

import android.app.Application
import androidx.lifecycle.*
import dc.photogallery.api.GalleryItem

class PhotoGalleryViewModel(private val app: Application) : AndroidViewModel(app) {
    private val flickrFetcher = FlickrFetcher()
    private val mutableSearchTerm = MutableLiveData<String>()

    val searchTerm: String
        get() = mutableSearchTerm.value ?: ""
    val galleryItemLiveData: LiveData<List<GalleryItem>> =
        Transformations.switchMap(mutableSearchTerm) { searchTerm ->
            if (searchTerm.isBlank()) {
                flickrFetcher.fetchPhotos()
            } else {
                flickrFetcher.searchPhotos(searchTerm)
            }
        }

    init {
        mutableSearchTerm.value = QueryPreferences.getStoredQuery(app)
    }

    fun fetchPhotos(query: String = "") {
        QueryPreferences.setStoredQuery(app, query)
        mutableSearchTerm.value = query
    }
}