package com.ssafy.witch.ui.group

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.witch.data.model.response.GroupJoinListResponse.JoinRequest
import com.ssafy.witch.databinding.GroupApprovalMemberItemBinding

class GroupApprovalListAdapter(val groupApprovalList: List<JoinRequest>, var itemClickListener: OnItemClickListener) : RecyclerView.Adapter<GroupApprovalListAdapter.GroupMemberListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupMemberListViewHolder {
        val binding = GroupApprovalMemberItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GroupMemberListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GroupMemberListViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = groupApprovalList.size


    interface OnItemClickListener {
        fun onApprove(id: String)
        fun onReject(id: String)
    }


    inner class GroupMemberListViewHolder(private val binding: GroupApprovalMemberItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.groupApprovalMemberItemTvProfileName.text = groupApprovalList[position].user.nickname

            Glide
                .with(binding.root)
                .load(groupApprovalList[position].user.profileImageUrl)
                .into(binding.groupApprovalMemberItemIvProfileImage)


            binding.groupApprovalMemberItemBtnApprove.setOnClickListener {
                itemClickListener.onApprove(groupApprovalList[position].joinRequestId)
            }

            binding.groupApprovalMemberItemBtnReject.setOnClickListener {
                itemClickListener.onReject(groupApprovalList[position].joinRequestId)
            }

            binding.groupApprovalMemberItemTvProfileName.isSelected = true

        }
    }
}
