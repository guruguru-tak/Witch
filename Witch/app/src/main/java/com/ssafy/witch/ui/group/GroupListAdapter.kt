package com.ssafy.witch.ui.group

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.witch.data.model.response.GroupListResponse
import com.ssafy.witch.databinding.GroupListItemBinding
import com.ssafy.witch.databinding.HomeAppointmentListItemBinding
import com.ssafy.witch.util.TimeConverter
import kotlin.math.abs

class GroupListAdapter(val groupList: List<GroupListResponse.GroupListItem>, val itemClickListener:ItemClickListener) : RecyclerView.Adapter<GroupListAdapter.GroupListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupListViewHolder {
        val binding = GroupListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GroupListViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: GroupListViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = groupList.size

    fun interface ItemClickListener {
        fun onItemClick(id: String)
    }

    inner class GroupListViewHolder(private val binding: GroupListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(position: Int) {
            binding.groupLiTvGroupName.text = groupList[position].name
            binding.groupLiTvGroupDay.text = "함께한 지 " + TimeConverter().calDaysDiff(groupList[position].createdAt) +"일 째"
            binding.groupLiIvGroupMaster.text = groupList[position].leader.nickname

            Glide.with(binding.root)
                .load(groupList[position].groupImageUrl)
                .into(binding.groupLiIvGroupImg)



            binding.groupLiIbGroupEnter.setOnClickListener {
                itemClickListener.onItemClick(groupList[position].groupId)
            }
        }
    }
}
