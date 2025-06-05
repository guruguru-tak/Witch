package com.ssafy.witch.ui.appointment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.ssafy.witch.R
import com.ssafy.witch.base.BaseFragment
import com.ssafy.witch.databinding.BottomSheetLayoutBinding
import com.ssafy.witch.databinding.FragmentMapBinding
import com.ssafy.witch.ui.ContentActivity
import com.ssafy.witch.ui.MainActivity
import com.ssafy.witch.ui.snack.SnackViewModel
import com.ssafy.witch.util.Distance
import kotlinx.coroutines.runBlocking
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

private const val TAG = "MapFragment_Witch"
class MapFragment : BaseFragment<FragmentMapBinding>(FragmentMapBinding::bind, R.layout.fragment_map),
    OnMapReadyCallback {
        var destFlag = false
        private var appointmentId = ""

        private val appointmentViewModel: AppointmentViewModel by activityViewModels()
        private val mapViewModel: MapViewModel by activityViewModels()
        private val snackViewModel: SnackViewModel by activityViewModels()

        private var timer: TimerHandler? = null
        private lateinit var map: GoogleMap
        private lateinit var fusedLocationClient: FusedLocationProviderClient
        private val userMarkers = mutableMapOf<String, Marker>()

        private var _bottomSheetBinding: BottomSheetLayoutBinding?= null
        private val bottomSheetBinding get() = _bottomSheetBinding!!

        @SuppressLint("ClickableViewAccessibility")
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
        }

        @SuppressLint("ClickableViewAccessibility")
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            arguments?.let {
                appointmentId = it.getString("appointmentId").toString()
            }
            appointmentViewModel.getMyInfo()

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
            val mapFragment =
                childFragmentManager.findFragmentById(R.id.map_ac_fg) as SupportMapFragment
            mapFragment.getMapAsync(this)

            val bottomSheetView = view.findViewById<View>(R.id.bottom_sheet_layout)
            _bottomSheetBinding = BottomSheetLayoutBinding.bind(bottomSheetView)

            initUserObserver()
            initLocationObserver()
            initAppointmentObserver()

            binding.mapAcTvAppointmentName.isSelected = true
            bottomSheetBinding.mapFgTvBottomAppointmentSummary.isSelected= true
            bottomSheetBinding.mapFgTvBottomAppointmentLocation.isSelected= true
            bottomSheetBinding.mapFgTvBottomAppointmentLeader.isSelected= true

            binding.mapAcSbRemainderTime.setOnTouchListener { _, _ ->
                true
            }

            binding.mapAcTvAppointmentStatus.setOnClickListener {
                setDialog()
            }

            binding.mapAcIvSnack.setOnClickListener {
                goToSnackFragment(5, appointmentId)
            }

            bottomSheetBinding.mapFgIvBottomAdd.setOnClickListener {
                goToSnackFragment(5, appointmentId)
            }

            bottomSheetBinding.mapFgCbAppointmentIsLate.setOnCheckedChangeListener { _, isChecked ->
                val lateParticipants = if (isChecked) {
//                    appointmentViewModel.appointmentInfo.value?.participants?.filter { it.is_late } ?: emptyList()
                    appointmentViewModel.appointmentInfo.value?.participants ?: emptyList()

                } else {
                    appointmentViewModel.appointmentInfo.value?.participants ?: emptyList()
                }

//                participantsAdapter.updateList(lateParticipants)
            }

            binding.mapAcTv2.setOnClickListener {
                setDialog()
            }
        }

        private fun initUserObserver() {
            Log.d(TAG, "initUserObserver: ${mapViewModel.userStatus.value}")
            mapViewModel.userStatus.observe(viewLifecycleOwner) {
                val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.circle_btn) as GradientDrawable

                when (mapViewModel.userStatus.value) {
                    1 -> {
                        binding.mapAcTvAppointmentStatus.text = "약속 삭제"
                        drawable.setColor(ContextCompat.getColor(requireContext(), R.color.witch_red))
                    }
                    2 -> {
                        binding.mapAcTvAppointmentStatus.text = "약속 참여"
                        drawable.setColor(ContextCompat.getColor(requireContext(), R.color.witch_green))
                    }
                    3 -> {
                        binding.mapAcTvAppointmentStatus.text = "약속 탈퇴"
                        drawable.setColor(ContextCompat.getColor(requireContext(), R.color.witch_green))
                    }
                }

                binding.mapAcTvAppointmentStatus.background = drawable
            }
        }

        private fun createMarker(userId: String, latlng: LatLng, imageUrl: String) {
            val markerView = LayoutInflater.from(context).inflate(R.layout.appointment_member_item, null)
            val profileImage = markerView.findViewById<ImageView>(R.id.appointment_member_profile_image)

            Glide.with(requireContext())
                .asBitmap()
                .load(imageUrl)
                .circleCrop()
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        profileImage.setImageBitmap(resource)

                        markerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
                        markerView.layout(0, 0, markerView.measuredWidth, markerView.measuredHeight)

                        val bitmap = Bitmap.createBitmap(markerView.measuredWidth, markerView.measuredHeight, Bitmap.Config.ARGB_8888)
                        val canvas = Canvas(bitmap)
                        markerView.draw(canvas)

                        if (userMarkers.containsKey(userId)) {
                            userMarkers[userId]?.position = latlng
                        } else {
                            val marker = map.addMarker(
                                MarkerOptions()
                                    .position(latlng)
                                    .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                            )
                            marker?.let { userMarkers[userId] = it }
                        }
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        Log.d("Glide", "이미지 로드가 취소되었거나 뷰가 재사용됨")
                    }
                })
        }


    private fun initLocationObserver() {
            Log.d(TAG, "initLocationObserver: ")
            MyLocationForegroundService.locationData.observe(viewLifecycleOwner) {
                appointmentViewModel.getAppointmentInfo(appointmentId)

                // 위치 표시
                appointmentViewModel.getLocationList(appointmentId)
                if (appointmentViewModel.locationLists.value != null) {
                    for (locationInfo in appointmentViewModel.locationLists.value!!) {
                        val userId = locationInfo.userId
                        val latlng = LatLng(locationInfo.latitude, locationInfo.longitude)
                        val profileImageUrl = locationInfo.profileImageUrl
                        createMarker(userId, latlng, profileImageUrl)
                    }
                }
            }
        }

        private fun initAppointmentObserver() {
            appointmentViewModel.getAppointmentInfo(appointmentId)
            Log.d(TAG, "initAppointmentObserver: ")

            appointmentViewModel.appointmentStatus.observe(viewLifecycleOwner) {
                showSnackArea(appointmentViewModel.appointmentStatus.value.toString())
                setTimer(LocalDateTime.parse(appointmentViewModel.appointmentInfo.value?.appointmentTime))
                if (appointmentViewModel.appointmentStatus.value != "SCHEDULED") {
                    binding.mapAcTvAppointmentStatus.visibility = View.GONE
                }
                when (appointmentViewModel.appointmentStatus.value) {
                    "FINISHED" -> {
                        binding.mapAcTv1.text = "약속은"
                        binding.mapAcTvRemainderTime.text = "이미 종료되었어요!"
                        binding.mapAcTv2.visibility = View.GONE
                    }
                }
            }

            appointmentViewModel.appointmentInfo.observe(viewLifecycleOwner) {
                appointmentViewModel.setLatitude(it.latitude)
                appointmentViewModel.setLongitude(it.longitude)
                initSnackObserver()
                startLocationUpdates(it.latitude, it.longitude)

                binding.mapAcTvAppointmentName.text = it.name
                bottomSheetBinding.mapFgTvBottomAppointmentSummary.text = it.summary
                bottomSheetBinding.mapFgTvBottomAppointmentLocation.text = it.address
                bottomSheetBinding.mapFgTvBottomAppointmentTime.text = LocalDateTime.parse(it.appointmentTime).format(
                    DateTimeFormatter.ofPattern("M월 d일 a h시 mm분", Locale.KOREAN))

                var flag = false
                for(participant in it.participants) {
                    if(participant.isLeader) { // 약속장인 경우

                        Glide.with(bottomSheetBinding.mapFgIvBottomAppointmentCpProfile.context)
                            .load(participant.profileImageUrl)
                            .circleCrop()
                            .into(bottomSheetBinding.mapFgIvBottomAppointmentCpProfile)
                        bottomSheetBinding.mapFgTvBottomAppointmentLeader.text = participant.nickname
//                            if(participant.is_late) { // 약속장이 지각한 경우
//                                bottomSheetBinding.mapFgTvBottomLeaderLate.visibility = View.VISIBLE
//                            }

                        if (participant.userId == appointmentViewModel.userId.value) {
                            flag = true
                            mapViewModel.setUserStatus(1)
                        }
                    } else { // 모임원인 경우
                        if (participant.userId == appointmentViewModel.userId.value) {
                            flag = true
                            mapViewModel.setUserStatus(3)
                        }
                    }
                }
                if (flag == false) {
                    mapViewModel.setUserStatus(2)
                }
            }

            appointmentViewModel.fragmentIdx.observe(viewLifecycleOwner) {
                val mainActivity = Intent(requireContext(), MainActivity::class.java)
                mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                mainActivity.putExtra("openFragment", it)
                mainActivity.putExtra("id", appointmentViewModel.groupId.toString())
                startActivity(mainActivity)
            }

            appointmentViewModel.toastMsg.observe(viewLifecycleOwner) { msg ->
                showCustomToast(msg)
            }

            appointmentViewModel.participants.observe(viewLifecycleOwner) {
                Log.d(TAG, "initAppointmentObserver: participants: ${appointmentViewModel.participants.value}")
                bottomSheetBinding.mapFgRvBottomMembers.adapter = AppointmentDetailParticipantsAdapter(appointmentViewModel.participants.value ?: mutableListOf())

                if (appointmentViewModel.participants.value != null) {
                    for (participant in appointmentViewModel.participants.value!!) {
                        if (appointmentViewModel.userId.value == participant.userId) {
                            bottomSheetBinding.mapFgIvBottomAdd.visibility = View.VISIBLE
                        }
                    }
                }
            }

            appointmentViewModel.leader.observe(viewLifecycleOwner) {
                if (appointmentViewModel.userId.value == appointmentViewModel.leader.value?.get(0)?.userId) {
                    bottomSheetBinding.mapFgIvBottomAdd.visibility = View.VISIBLE
                }
            }

        }

        private fun initSnackObserver() {
            mapViewModel.getSnackList(appointmentId)
            mapViewModel.snackList.observe(viewLifecycleOwner) {
                bottomSheetBinding.mapFgRvBottomSnack.adapter= AppointmentSnackAdatper(
                    it!!,
                    mylocation.latitude,
                    mylocation.longitude
                ) { snackid, distance, snackLatitude, snackLongitude ->
                    snackViewModel.setDistance(distance)
                    Log.d(TAG, "initSnackObserver: distance: ${snackViewModel.distance.value}")
                    if (isDistance(snackLatitude, snackLongitude) == true) {
                        goToSnackFragment(4, snackid)
                    }
                }
            }
        }

        private fun isDistance(snackLatitude: Double, snackLongitude: Double): Boolean {
            val s2aDistance: Double = Distance().calculateDistance(snackLatitude, snackLongitude,
                appointmentViewModel.appointmentInfo.value!!.latitude, appointmentViewModel.appointmentInfo.value!!.longitude)

            val m2aDistance = Distance().calculateDistance(mylocation.latitude, mylocation.longitude,
                appointmentViewModel.appointmentInfo.value!!.latitude, appointmentViewModel.appointmentInfo.value!!.longitude)

            if (m2aDistance - s2aDistance <= 0.1) {
                return true
            }
            showCustomToast("거리 내에 위치하지 않습니다.")
            return false
        }

        private fun setDialog() {
            val dialogView= when (mapViewModel.userStatus.value) {
                1 -> layoutInflater.inflate(R.layout.dialog_appointment_delete, null)
                2 -> layoutInflater.inflate(R.layout.dialog_appointment_join, null)
                3 -> layoutInflater.inflate(R.layout.dialog_appointment_leave, null)
                else -> layoutInflater.inflate(R.layout.dialog_appointment_join, null)
            }
            val dialogBuilder = Dialog(requireContext())
            dialogBuilder.setContentView(dialogView)
            dialogBuilder.create()
            dialogBuilder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialogBuilder.show()

            val appointmentChangeDlBtnYes = dialogView.findViewById<Button>(R.id.dl_btn_yes)
            val appointmentChangeDlBtnNo = dialogView.findViewById<Button>(R.id.dl_btn_no)

            appointmentChangeDlBtnYes.setOnClickListener {
                when(mapViewModel.userStatus.value) {
                    1 -> appointmentViewModel.deleteAppointment(appointmentId)
                    2 -> {
                        appointmentViewModel.participateAppointment(appointmentId)
                        mapViewModel.setUserStatus(3)
                    }
                    3 -> {
                        appointmentViewModel.leaveAppointment(appointmentId)
                        mapViewModel.setUserStatus(2)
                    }
                }
                appointmentViewModel.updateParticipantsInfo(appointmentId)
                dialogBuilder.dismiss()
            }

            appointmentChangeDlBtnNo.setOnClickListener {
                dialogBuilder.dismiss()
            }
        }

        private fun showSnackArea(appointmentStatus: String) {
            if (appointmentStatus == "ONGOING") { // 스낵 보여주기
                bottomSheetBinding.mapFgClBottomSnack.visibility= View.VISIBLE
                binding.mapFgClSnackArea.visibility = View.VISIBLE
            } else if (appointmentStatus == "SCHEDULED") { // 약속 전
                bottomSheetBinding.mapFgClBottomSnack.visibility= View.GONE
                binding.mapFgClSnackArea.visibility = View.GONE
            } else if (appointmentStatus == "FINISHED") { // 약속 끝난 이후
                bottomSheetBinding.mapFgClBottomSnack.visibility= View.GONE
                binding.mapFgClSnackArea.visibility = View.GONE
            }
        }

        fun getBitmapFromVectorDrawable(context: Context, drawableId: Int): Bitmap {
            val drawable = ContextCompat.getDrawable(context, drawableId)
                ?: return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)

            val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            return bitmap
        }

        lateinit var mylocation: Location
        private fun startLocationUpdates(latitude: Double?, longitude: Double?) {
            Log.d(TAG, "startLocationUpdates: latitude: ${latitude}, longitude: ${longitude}")
            if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                        mylocation = Location("")
                        if (location != null) {
                            mylocation.latitude = location.latitude
                            mylocation.longitude = location.longitude
                        }
                        appointmentViewModel.appointmentInfo.observe(viewLifecycleOwner) {
                            val placeLocation = LatLng(latitude!!, longitude!!)
                            val bitmap = getBitmapFromVectorDrawable(
                                requireContext(),
                                R.drawable.place
                            )
                            val markerIcon = BitmapDescriptorFactory.fromBitmap(bitmap)
                            if (destFlag == false) {
                                map.moveCamera(
                                    CameraUpdateFactory.newLatLngZoom(
                                        placeLocation,
                                        15f
                                    )
                                )
                                destFlag = true
                            }

                            map.addMarker(
                                MarkerOptions()
                                    .position(placeLocation)
                                    .icon(markerIcon)
                            )
                        }
                }
            }
        }

        private val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    appointmentViewModel.getAppointmentInfo(appointmentId)
                    appointmentViewModel.appointmentInfo.observe(viewLifecycleOwner) {
                        startLocationUpdates(it.latitude, it.longitude)
                    }
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
                appointmentViewModel.getAppointmentInfo(appointmentId)
                appointmentViewModel.appointmentInfo.observe(viewLifecycleOwner) {
                    startLocationUpdates(it.latitude, it.longitude)
                }
            }
        }

        override fun onMapReady(googleMap: GoogleMap) {
            map = googleMap
            requestLocationPermissions()
        }

        private fun goToSnackFragment(index: Int, id: String) {
            var flag= false
            for(participant in appointmentViewModel.participants.value!!) {
                if (appointmentViewModel.userId.value == participant.userId) {
                    (requireActivity() as ContentActivity).openFragment(index, id)
                    flag = true
                }
            }
            if (appointmentViewModel.userId.value == appointmentViewModel.leader.value?.get(0)?.userId) {
                (requireActivity() as ContentActivity).openFragment(index, id)
                flag = true
            }
            if (flag == false) {
                showCustomToast("약속 참여자가 아닙니다.")
            }
        }

        @SuppressLint("SetTextI18n")
        private fun setTimer(appointmentTime: LocalDateTime) {
            val duration = Duration.between(LocalDateTime.now(), appointmentTime).seconds
            mapViewModel.setRemainderTime(duration)

            timer?.stopTimer()
            timer = null

            if (duration < 0) { // 이미 끝난 약속
                binding.mapAcTvRemainderTime.text = "0분 0초"
                binding.mapAcSbRemainderTime.progress = 0
            } else if (duration in 0..3600) { // 약속 시간 한 시간 이내
                timer = TimerHandler()
                timer?.startTimer(duration)
                mapViewModel.remainderTime.observe(viewLifecycleOwner) { remainingTime ->
                    binding.mapAcTvRemainderTime.text = remainingTime?.let { parseSeconds(it) }
                    binding.mapAcSbRemainderTime.progress = remainingTime?.toInt() ?: 0
                }
            } else if (duration < 3600 * 24) { // 약속 시간 하루 이내
                binding.mapAcTvRemainderTime.text = Duration.between(LocalDateTime.now(), appointmentTime).toHours().toString() + "시간"
                binding.mapAcSbRemainderTime.progress = 3600
            } else { // 약속 시간 하루 초과
                binding.mapAcTvRemainderTime.text = Duration.between(LocalDateTime.now(), appointmentTime).abs().toDays().toString() + "일"
                binding.mapAcSbRemainderTime.progress = 3600
            }
        }
    
        private fun parseSeconds(seconds: Long): String {
            val minutes = (seconds % 3600) / 60
            val remainingSeconds = seconds % 60
            return "${minutes}분 ${remainingSeconds}초"
        }

        override fun onDestroyView() {
            super.onDestroyView()
            _bottomSheetBinding = null
        }

        @SuppressLint("HandlerLeak")
        inner class TimerHandler : Handler(Looper.getMainLooper()) {
            private var remainingTime: Long = 0
            private var isRunning = false

            fun startTimer(initialTime: Long) {
                isRunning = true
                remainingTime = initialTime
                removeCallbacksAndMessages(null)
                post(timerRunnable)
            }

            fun stopTimer() {
                isRunning = false
                removeCallbacksAndMessages(null)
            }

            private val timerRunnable = object : Runnable {
                override fun run() {
                    if (!isRunning) return

                    if (remainingTime > 0) {
                        remainingTime -= 1
                        mapViewModel.setRemainderTime(remainingTime)
                        postDelayed(this, 1000)
                    } else {
                        stopTimer()
                    }
                }
            }
        }

        companion object {
            @JvmStatic
            fun newInstance(key:String, value:String) =
                MapFragment().apply {
                    arguments = Bundle().apply {
                        putString(key, value)
                    }
                }
        }
}