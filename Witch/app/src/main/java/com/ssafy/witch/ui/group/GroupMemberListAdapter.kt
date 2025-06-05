package com.ssafy.witch.ui.group

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.witch.data.model.dto.User
import com.ssafy.witch.databinding.GroupListItemBinding
import com.ssafy.witch.databinding.GroupMemberItemBinding
import com.ssafy.witch.databinding.HomeAppointmentListItemBinding
import kotlin.math.abs

class GroupMemberListAdapter(val groupMemberList: List<User>) : RecyclerView.Adapter<GroupMemberListAdapter.GroupMemberListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupMemberListViewHolder {
        val binding = GroupMemberItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GroupMemberListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GroupMemberListViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = groupMemberList.size


    inner class GroupMemberListViewHolder(private val binding: GroupMemberItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.groupMemberItemTvProfileName.text = groupMemberList[position].nickname

            Glide.with(binding.root)
                .load(groupMemberList[position].profileImageUrl)
                .into(binding.groupMemberItemIvProfileImage)
        }
    }
}
