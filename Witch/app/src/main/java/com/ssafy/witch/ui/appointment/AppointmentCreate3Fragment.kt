package com.ssafy.witch.ui.appointment

import android.app.Dialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.NumberPicker
import androidx.fragment.app.activityViewModels
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.ssafy.witch.R
import com.ssafy.witch.base.BaseFragment
import com.ssafy.witch.databinding.DialogSnackTextBinding
import com.ssafy.witch.databinding.DialogTimePickerBinding
import com.ssafy.witch.databinding.FragmentAppointmentCreate3Binding
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId


private const val TAG = "AppointmentCreate3Fragment_Witch"
class AppointmentCreate3Fragment : BaseFragment<FragmentAppointmentCreate3Binding>(
    FragmentAppointmentCreate3Binding::bind, R.layout.fragment_appointment_create3){
    private var today: Long= 0
    private lateinit var minute: String
    private lateinit var hour: String
    private lateinit var localDateTime: LocalDateTime
    private val appointmentViewModel: AppointmentViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        today = MaterialDatePicker.todayInUtcMilliseconds()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.appointmentFgBtnNext.setOnClickListener {
            if (binding.appointmentFgTvDateChoice.text == "날짜를 선택하세요") {
                showCustomToast("날짜를 설정해주세요!")
            } else if (binding.appointmentFgTvTimeChoice.text == "시간을 선택하세요") {
                showCustomToast("시간을 설정해주세요!")
            } else {
                setDialog()
            }
        }

        binding.appointmentFgBtnBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.appointmentFgTvDateChoice.setOnClickListener {
            showDatePickerDialog()
        }

        binding.appointmentFgIvDateClear.setOnClickListener {
            binding.appointmentFgTvDateChoice.text = "날짜를 선택하세요"
        }

        binding.appointmentFgTvTimeChoice.setOnClickListener {
            showTimePickerDialog()
        }

        binding.appointmentFgIvTimeClear.setOnClickListener {
            binding.appointmentFgTvTimeChoice.text = "시간을 선택하세요"
        }
    }

    private fun showTimePickerDialog() {

        val dialogBinding= DialogTimePickerBinding.inflate(layoutInflater)


        dialogBinding.timePicker.run {
            val minutePicker =
                findViewById<NumberPicker>(resources.getIdentifier("minute", "id", "android"))

            val minuteValues = Array(6) { (it * 10).toString().padStart(2, '0') }
            minutePicker.minValue = 0
            minutePicker.maxValue = 5
            minutePicker.displayedValues = minuteValues
        }

        val dialog = Dialog(requireContext())
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogBinding.groupOutDlBtnYes.setOnClickListener{
            hour= String.format("%02d", dialogBinding.timePicker.hour)
            minute= String.format("%02d", dialogBinding.timePicker.minute*10)
            var state= "오전/오후"
            if (hour.toInt() >= 12) {
                state= "오후"
                binding.appointmentFgTvTimeChoice.text = state + " " + (hour.toInt() - 12).toString() + "시 " + minute + "분"
            } else {
                state= "오전"
                binding.appointmentFgTvTimeChoice.text = state + " " + hour + "시 " + minute + "분"
            }

            dialog.dismiss()
        }

        dialogBinding.groupOutDlBtnNo.setOnClickListener{
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showDatePickerDialog() {
        val builder= MaterialDatePicker.Builder.datePicker()
        builder.setTheme(R.style.CustomDatePickerDialog)
            .setSelection(today)
            .setCalendarConstraints(
                com.google.android.material.datepicker.CalendarConstraints.Builder()
                    .setValidator(DateValidatorPointForward.now())
                    .build()
            )

        val picker = builder.build()



        picker.addOnPositiveButtonClickListener { datepicker ->
            localDateTime = Instant.ofEpochMilli(datepicker)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()

            binding.appointmentFgTvDateChoice.text = picker.headerText
        }
        picker.addOnNegativeButtonClickListener {

        }

        picker.show(childFragmentManager, picker.toString())
    }

    private fun setDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_appointment_create, null)
        val dialogBuilder = Dialog(requireContext())
        dialogBuilder.setContentView(dialogView)
        dialogBuilder.create()
        dialogBuilder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogBuilder.show()

        val appointmentCreateDlBtnYes = dialogView.findViewById<Button>(R.id.dl_btn_yes)
        val appointmentCreateDlBtnNo = dialogView.findViewById<Button>(R.id.dl_btn_no)

        appointmentCreateDlBtnYes.setOnClickListener {
            appointmentViewModel.setAppointmentTime(LocalDateTime.of(localDateTime.year, localDateTime.monthValue, localDateTime.dayOfMonth, hour.toInt(), minute.toInt()).toString())
            appointmentViewModel.registerAppointment()
            appointmentViewModel.toastMsg.observe(viewLifecycleOwner) { msg ->
                showCustomToast(msg)
                if (msg == "약속이 생성되었어요!") {
                    requireActivity().finish()
                    dialogBuilder.dismiss()
                }
            }
        }

        appointmentCreateDlBtnNo.setOnClickListener {
            dialogBuilder.dismiss()
        }
    }
}