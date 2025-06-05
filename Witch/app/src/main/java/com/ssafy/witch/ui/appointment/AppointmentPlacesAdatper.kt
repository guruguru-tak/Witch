package com.ssafy.witch.ui.appointment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.witch.data.model.dto.AppointmentPlacesItem
import com.ssafy.witch.databinding.ChoicePlaceItemBinding

class AppointmentPlacesAdatper(val appointmentPlacesList: List<AppointmentPlacesItem>, val itemClickListener: ItemClickListener)
    : RecyclerView.Adapter<AppointmentPlacesAdatper.PlacesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlacesViewHolder {
        val binding = ChoicePlaceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlacesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlacesViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = appointmentPlacesList.size

    fun interface ItemClickListener {
        fun onItemClick(id: Int)
    }

    inner class PlacesViewHolder(private val binding: ChoicePlaceItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.appointmentDlTvAddress.isSelected = true
            binding.appointmentDlTvName.text = appointmentPlacesList[position].placeName
            binding.appointmentDlTvAddress.text = appointmentPlacesList[position].placeAddress
            binding.appointmentDlTvChoice.setOnClickListener {
                itemClickListener.onItemClick(position)
            }
        }
    }

}