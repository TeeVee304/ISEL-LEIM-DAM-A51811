package dam_a51811.weatherbuddy.presentation.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import dam_a51811.weatherbuddy.R
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MapFragment : Fragment(), OnMapReadyCallback {

    private val viewModel: MapViewModel by viewModels()
    private var googleMap: GoogleMap? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.selectedLocation.collect { location ->
                location?.let {
                    val bundle = Bundle().apply {
                        putFloat("latitude", it.first.toFloat())
                        putFloat("longitude", it.second.toFloat())
                    }
                    findNavController().navigate(R.id.action_mapFragment_to_weatherFragment, bundle)
                    viewModel.clearSelection()
                }
            }
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        
        // Default location: Lisbon
        val lisbon = LatLng(38.7223, -9.1393)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(lisbon, 10f))

        map.setOnMapClickListener { latLng ->
            map.clear()
            map.addMarker(MarkerOptions().position(latLng).title("Selected Location"))
            viewModel.onMapClick(latLng.latitude, latLng.longitude)
        }
    }
}
