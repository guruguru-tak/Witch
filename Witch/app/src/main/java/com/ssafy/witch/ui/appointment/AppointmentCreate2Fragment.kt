package com.ssafy.witch.ui.appointment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.api.net.SearchByTextRequest
import com.ssafy.witch.R
import com.ssafy.witch.base.BaseFragment
import com.ssafy.witch.data.model.dto.AppointmentPlacesItem
import com.ssafy.witch.databinding.FragmentAppointmentCreate2Binding
import com.ssafy.witch.ui.ContentActivity
import java.math.BigDecimal
import java.math.RoundingMode


private const val TAG = "AppointmentCreate2Fragment_Witch"
class AppointmentCreate2Fragment : BaseFragment<FragmentAppointmentCreate2Binding>(
    FragmentAppointmentCreate2Binding::bind, R.layout.fragment_appointment_create2),
    OnMapReadyCallback {

    private val appointmentViewModel: AppointmentViewModel by activityViewModels()

    private lateinit var placesClient: PlacesClient
    private lateinit var placesList: MutableList<AppointmentPlacesItem>
    private lateinit var placesAdapter: AppointmentPlacesAdatper
    private lateinit var etPlace: String
    private lateinit var choiceLatLng: LatLng

    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), getString(R.string.api_key))
        }
        placesClient = Places.createClient(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.appointment_fg_map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.appointmentFgTvAppointmentPlaceName.isSelected= true
        binding.appointmentFgTvAppointmentAddress.isSelected= true

        binding.appointmentFgTvSearchPlace.setOnClickListener {
            etPlace = binding.appointmentFgEtAddress.text.toString()

            val placeFields: List<Place.Field> = listOf(
                Place.Field.ID,
                Place.Field.DISPLAY_NAME,
                Place.Field.FORMATTED_ADDRESS,
                Place.Field.LAT_LNG,
                Place.Field.PHONE_NUMBER
            )

            // 검색할 지역의 남서쪽, 북동쪽 좌표 설정
            val southWest = LatLng(33.1900, 125.6042)
            val northEast = LatLng(38.6235, 128.4696)

            // SearchByTextRequest 객체 생성
            val searchByTextRequest = SearchByTextRequest.builder(etPlace, placeFields)
                .setMaxResultCount(10)
                .setLocationRestriction(RectangularBounds.newInstance(southWest, northEast))
                .build()

            // PlacesClient를 사용하여 검색 요청 수행
            placesClient.searchByText(searchByTextRequest)
                .addOnSuccessListener { response ->
                    val places: List<Place> = response.places

                    placesList = mutableListOf()
                    for (place in places) {
                        val latitude = BigDecimal(place.latLng!!.latitude).setScale(6, RoundingMode.HALF_UP)
                        val longitude = BigDecimal(place.latLng!!.longitude).setScale(6, RoundingMode.HALF_UP)
                        val phoneNumber = "0" + place.phoneNumber?.removePrefix("+82 ")?.trim() ?: ""
                        placesList.add(AppointmentPlacesItem(place.displayName,
                            place.address,
                            latitude,
                            longitude,
                            phoneNumber)
                        )
                    }

                    setDialog()
                }
                .addOnFailureListener { exception ->
                }
        }

        binding.appointmentFgBtnNext.setOnClickListener{
            if (binding.appointmentFgTvInitialize.visibility == View.VISIBLE) {
                showCustomToast("약속 장소를 선택해주세요!")
            } else {
                appointmentViewModel.setLongitude(choiceLatLng.longitude)
                appointmentViewModel.setLatitude(choiceLatLng.latitude)
                appointmentViewModel.setAddress(binding.appointmentFgTvAppointmentAddress.text.toString())
                (requireActivity() as ContentActivity).openFragment(8, "")
            }
        }

        binding.appointmentFgBtnBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    @SuppressLint("MissingInflatedId")
    private fun setDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_appointment_search, null)
        val dialogBuilder = Dialog(requireContext())
        dialogBuilder.setContentView(dialogView)
        dialogBuilder.create()
        dialogBuilder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogBuilder.show()

        val recyclerView = dialogView.findViewById<RecyclerView>(R.id.appointment_dl_rv_choice_place)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        placesAdapter = AppointmentPlacesAdatper(placesList) { position ->
            setInfoVisibility()

            val selectedPlace = placesList[position]
            choiceLatLng = LatLng(selectedPlace.placeLatitude.toDouble(), selectedPlace.placeLongitude.toDouble())
            binding.appointmentFgTvAppointmentPlaceName.text = selectedPlace.placeName
            binding.appointmentFgTvAppointmentAddress.text = selectedPlace.placeAddress
            if (selectedPlace.placePhoneNumber == "0null") {
                binding.appointmentFgTvAppointmentPhoneNumber.text = ""
            } else {
                binding.appointmentFgTvAppointmentPhoneNumber.text = selectedPlace.placePhoneNumber
            }

            val appointmentLocation = LatLng(selectedPlace.placeLatitude.toDouble(), selectedPlace.placeLongitude.toDouble())
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(appointmentLocation, 15f))

            val placeMarkerBitmap = getBitmapFromVectorDrawable(requireContext(), R.drawable.marker)
            val scaledplaceMarker = Bitmap.createScaledBitmap(placeMarkerBitmap, 100, 100, false)
            map.addMarker(MarkerOptions()
                .position(appointmentLocation)
                .title(selectedPlace.placeName)
                .icon(BitmapDescriptorFactory.fromBitmap(scaledplaceMarker)))

            dialogBuilder.dismiss()
        }

        recyclerView.adapter = placesAdapter
    }

    private fun setInfoVisibility() {
        binding.imageView.visibility = View.VISIBLE
        binding.appointmentFgTvAppointmentPlaceName.visibility = View.VISIBLE
        binding.appointmentFgTvAppointmentAddress.visibility = View.VISIBLE
        binding.appointmentFgTvAppointmentPhoneNumber.visibility = View.VISIBLE
        binding.appointmentFgTvInitialize.visibility = View.GONE
    }

    lateinit var mylocation: Location
    private fun startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val userLocation = LatLng(location.latitude, location.longitude)
                    mylocation = Location("")
                    mylocation.latitude = location.latitude
                    mylocation.longitude = location.longitude
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15f))
                } else {
                }
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                startLocationUpdates()
            } else {
                Toast.makeText(requireContext(), "위치 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
            }
        }

    private fun requestLocationPermissions() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            startLocationUpdates()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        requestLocationPermissions()
    }

    fun getBitmapFromVectorDrawable(context: Context, drawableId: Int) : Bitmap {
        val drawable = ContextCompat.getDrawable(context, drawableId)
            ?: return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)

        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
    }
}