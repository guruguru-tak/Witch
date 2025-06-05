package com.ssafy.witch.ui.appointment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.activityViewModels
import com.ssafy.witch.R
import com.ssafy.witch.base.BaseFragment
import com.ssafy.witch.databinding.FragmentAppointmentCreate1Binding
import com.ssafy.witch.ui.ContentActivity

private const val TAG = "AppointmentCreate1Fragment_Witch"
class AppointmentCreate1Fragment : BaseFragment<FragmentAppointmentCreate1Binding>(
    FragmentAppointmentCreate1Binding::bind, R.layout.fragment_appointment_create1){

    private val appointmentViewModel: AppointmentViewModel by activityViewModels()

    private var groupId = ""

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            groupId = it.getString("groupId").toString()
        }
        appointmentViewModel.setGroupId(groupId)
        Log.d(TAG, "onViewCreated: groupId: ${groupId}")


        binding.appointmentFgBtnNext.setOnClickListener {
            if (binding.appointmentFgEtName.length() == 0) {
                showCustomToast("약속 이름을 작성해주세요.")
            } else if (binding.appointmentFgEtName.length() > 30) {
                showCustomToast("약속 이름은 30자 이내여야 합니다.")
            } else if (binding.appointmentFgEtSummary.length() > 50) {
                showCustomToast("약속 요약은 50자 이내여야 합니다.")
            } else {
                appointmentViewModel.setName(binding.appointmentFgEtName.text.toString())
                appointmentViewModel.setSummary(binding.appointmentFgEtSummary.text.toString())

                (requireActivity() as ContentActivity).openFragment(7, "")
            }
        }

        binding.appointmentFgBtnBack.setOnClickListener {
            requireActivity().finish()
            appointmentViewModel.appointmentClear()
        }


        val editText1 = binding.appointmentFgEtName
        editText1.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = editText1.compoundDrawablesRelative[2]

                drawableEnd?.let {
                    val iconStartX = editText1.width - editText1.paddingEnd - it.bounds.width()
                    if (event.x >= iconStartX) {
                        editText1.text.clear()
                        return@setOnTouchListener true
                    }
                }
            }
            false
        }

        val editText2 = binding.appointmentFgEtSummary
        editText2.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = editText2.compoundDrawablesRelative[2]

                drawableEnd?.let {
                    val iconStartX = editText2.width - editText2.paddingEnd - it.bounds.width()
                    if (event.x >= iconStartX) {
                        editText2.text.clear()
                        return@setOnTouchListener true
                    }
                }
            }
            false
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(key:String, value:String) =
            AppointmentCreate1Fragment().apply {
                arguments = Bundle().apply {
                    putString(key, value)
                }
            }
    }
}