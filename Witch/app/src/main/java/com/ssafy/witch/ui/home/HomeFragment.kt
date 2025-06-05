package com.ssafy.witch.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.icu.text.DateFormat
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.map
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.ssafy.witch.R
import com.ssafy.witch.base.ApplicationClass
import com.ssafy.witch.base.BaseFragment
import com.ssafy.witch.data.model.dto.MyAppointment
import com.ssafy.witch.data.model.response.MyAppointmentResponse
import com.ssafy.witch.databinding.FragmentHomeBinding
import com.ssafy.witch.ui.ContentActivity
import com.ssafy.witch.ui.MainActivity
import com.ssafy.witch.ui.group.GroupViewModel
import com.ssafy.witch.util.TimeConverter
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.Calendar
import java.util.Date


private const val TAG = "HomeFragment"
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::bind, R.layout.fragment_home) {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var calendarView: MaterialCalendarView

    private lateinit var homeListAdapter: HomeListAdapter

    private lateinit var mainActivity: MainActivity


    private lateinit var schedule:MyAppointmentResponse
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initView()
        initObserver()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun initView() {

        viewModel.getProfile()

        viewModel.name.observe(viewLifecycleOwner){
            binding.homeFgTvUsername.text = it
        }

        mainActivity= requireActivity() as MainActivity

        calendarView = binding.homeFgCvCalendar

        calendarView.selectedDate = CalendarDay.from(Date(System.currentTimeMillis()))

        viewModel.getAllAppointments(calendarView.selectedDate.year, calendarView.selectedDate.month + 1)

        calendarView.setOnMonthChangedListener { widget, date ->
            viewModel.getAllAppointments(date.year, date.month + 1)
        }

        calendarView.setOnDateChangedListener { widget, date, selected ->
            getAppointmentList(date)
        }
        dotSchedule()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun initAdapter(){
        viewModel.appointmentList.observe(viewLifecycleOwner,{
            dotSchedule()
            getAppointmentList(calendarView.selectedDate)
        })

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAppointmentList(date: CalendarDay){
        binding.homeFgTvDate.text = "${date.year}-${date.month + 1}-${date.day}"

        binding.homeFgRvAppointment.adapter =
                viewModel.appointmentList.value?.let {
                    HomeListAdapter(it.appointments.filter {
                        val time=TimeConverter().convertToLocalDateTime(it.appointmentTime)
                        time.dayOfMonth == date.day && time.monthValue-1==date.month }.sortedBy {
                            it.appointmentTime
                    }) { id ->
                        val intent = Intent(mainActivity, ContentActivity::class.java)
                        intent.putExtra("openFragment", 9)
                        intent.putExtra("id", id)
                        startActivity(intent)
                    }
                }
    }

    fun initObserver(){
        viewModel.errorMessage.observe(viewLifecycleOwner, {
            if(!it.isNullOrBlank()){
                showCustomToast(viewModel.errorMessage.value.toString())
            }
        })



    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun dotSchedule(){
        val eventDates = viewModel.appointmentList.value?.appointments?.map { appointment ->
            val time=TimeConverter().convertToLocalDateTime(appointment.appointmentTime)
            CalendarDay.from(time.year, time.monthValue - 1,
                time.dayOfMonth) }?.toSet()

        Log.d(TAG, "dotSchedule: ${viewModel.appointmentList.value}")

        val eventDecorator = eventDates?.let { EventDecorator(it) }
        calendarView.addDecorator(eventDecorator)

        calendarView.invalidateDecorators()
    }
}