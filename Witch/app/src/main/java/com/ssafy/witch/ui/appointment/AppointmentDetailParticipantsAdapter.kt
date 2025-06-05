package com.ssafy.witch.ui.appointment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.witch.R
import com.ssafy.witch.data.model.dto.AppointmentDetailItem
import com.ssafy.witch.databinding.AppointmentDetailParticipantsItemBinding

class AppointmentDetailParticipantsAdapter(var participantsList: MutableList<AppointmentDetailItem.Participants>)
    : RecyclerView.Adapter<AppointmentDetailParticipantsAdapter.ParticipantsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParticipantsViewHolder {
        val binding = AppointmentDetailParticipantsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ParticipantsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ParticipantsViewHolder, position: Int) {
        val participant = participantsList[position]
        holder.bind(participant)
    }

    override fun getItemCount(): Int = participantsList.size

    inner class ParticipantsViewHolder(private val binding: AppointmentDetailParticipantsItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(participant: AppointmentDetailItem.Participants) {
            binding.appointmentDetailParticipantName.isSelected= true
            binding.appointmentDetailParticipantName.text= participantsList[position].nickname

            val context = binding.root.context
//            val textColor = if (participant.is_late) {
//                ContextCompat.getColor(context, R.color.witch_red)
//            } else {
//                ContextCompat.getColor(context, R.color.witch_white)
//            }
//            binding.appointmentDetailParticipantName.setTextColor(textColor)
        }
    }
}