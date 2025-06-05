package com.ssafy.witch.ui.group

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.ssafy.witch.R
import com.ssafy.witch.base.BaseFragment
import com.ssafy.witch.data.model.response.GroupListResponse
import com.ssafy.witch.databinding.FragmentGroupListBinding
import com.ssafy.witch.ui.ContentActivity
import com.ssafy.witch.ui.MainActivity

class GroupListFragment : BaseFragment<FragmentGroupListBinding>(FragmentGroupListBinding::bind, R.layout.fragment_group_list) {
    private val viewModel: GroupListViewModel by viewModels()

    private lateinit var mainActivity: MainActivity
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = requireActivity() as MainActivity

        initView()
        initObserver()
    }

    fun initObserver() {
        viewModel.errorMessage.observe(viewLifecycleOwner, {
            if (!it.isNullOrBlank()) {
                showCustomToast(viewModel.errorMessage.value.toString())
            }
        })

        viewModel.groupList.observe(viewLifecycleOwner) {
            binding.groupListFgRvGroupList.adapter = GroupListAdapter(it) { id ->
                (requireActivity() as MainActivity).openFragment(5, id)
            }
        }
    }

    fun initView(){
        viewModel.getGroupList()

        binding.groupListFgIbAddGroup.setOnClickListener {
            val contentActivity = Intent(requireContext(), ContentActivity::class.java)
            contentActivity.putExtra("openFragment", 1)
            startActivity(contentActivity)
        }

        binding.groupListFgSrlGroupList.setOnRefreshListener {
            viewModel.getGroupList()
            binding.groupListFgSrlGroupList.isRefreshing = false
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getGroupList()
    }

}