package com.ssafy.witch.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.witch.data.model.dto.MyAppointment
import com.ssafy.witch.data.model.response.MyAppointmentResponse
import com.ssafy.witch.databinding.HomeAppointmentListItemBinding
import com.ssafy.witch.util.TimeConverter
import kotlin.math.abs

class HomeListAdapter(val appointmentList: List<MyAppointment>,val itemClickListener:ItemClickListener) : RecyclerView.Adapter<HomeListAdapter.HomeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding = HomeAppointmentListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = appointmentList.size

    fun interface ItemClickListener {
        fun onItemClick(id: String)
    }

    inner class HomeViewHolder(private val binding: HomeAppointmentListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("NewApi")
        fun bind(position: Int) {
            val time= TimeConverter().convertToLocalDateTime(appointmentList[position].appointmentTime)
            binding.homeLiTvAppointmentGroup.text=appointmentList[position].name
            binding.homeLiTvAppointmentTime.text= time.hour.toString()+"시 "+ time.minute.toString() + "분"
            binding.homeLiTvAppointmentName.text=appointmentList[position].group.name
            binding.homeLiTvAppointmentActive.isGone = appointmentList[position].status != "ONGOING"

            binding.homeAppointmentListItem.setOnClickListener {
                itemClickListener.onItemClick(appointmentList[position].appointmentId)
            }
        }
    }
}
