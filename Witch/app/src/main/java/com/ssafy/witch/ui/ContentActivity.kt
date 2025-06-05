package com.ssafy.witch.ui

import android.os.Bundle
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.ssafy.witch.R
import com.ssafy.witch.base.BaseActivity
import com.ssafy.witch.data.remote.LocationWorker
import com.ssafy.witch.databinding.ActivityContentBinding
import com.ssafy.witch.ui.appointment.AppointmentCreate1Fragment
import com.ssafy.witch.ui.appointment.AppointmentCreate2Fragment
import com.ssafy.witch.ui.appointment.AppointmentCreate3Fragment
import com.ssafy.witch.ui.group.GroupCreateFragment
import com.ssafy.witch.ui.group.GroupEditFragment
import com.ssafy.witch.ui.mypage.ProfileEditFragment
import com.ssafy.witch.ui.snack.SnackCreateFragment
import com.ssafy.witch.ui.snack.SnackFragment
import com.ssafy.witch.ui.appointment.MapFragment
import com.ssafy.witch.ui.mypage.PwdEditFragment
import java.util.concurrent.TimeUnit

class ContentActivity : BaseActivity<ActivityContentBinding>(ActivityContentBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startWorkManager()

        val fragmentIdx = intent.getIntExtra("openFragment", -1)
        val id = intent.getStringExtra("id") ?: ""
        if (fragmentIdx != -1) {
            openFragment(fragmentIdx, id)
            intent.removeExtra("openFragment")
        }
    }

    fun openFragment(index: Int, id : String = ""){
        moveFragment(index , id)
    }

    private fun moveFragment(index:Int, id : String = ""){
        val transaction = supportFragmentManager.beginTransaction()
        when(index){
            1 -> transaction.replace(R.id.content_flayout, GroupCreateFragment())
            2 -> transaction.replace(R.id.content_flayout, GroupEditFragment.newInstance("groupId", id))
            3 -> transaction.replace(R.id.content_flayout, ProfileEditFragment())
            4 -> transaction.replace(R.id.content_flayout, SnackFragment.newInstance("snackId", id))
                .addToBackStack(null)
            5 -> transaction.replace(R.id.content_flayout, SnackCreateFragment.newInstance("appointmentId", id))
                .addToBackStack(null)
            6 -> transaction.replace(R.id.content_flayout, AppointmentCreate1Fragment.newInstance("groupId", id))
            7 -> transaction.replace(R.id.content_flayout, AppointmentCreate2Fragment())
                .addToBackStack(null)
            8 -> transaction.replace(R.id.content_flayout, AppointmentCreate3Fragment())
                .addToBackStack(null)
            9 -> transaction.replace(R.id.content_flayout, MapFragment.newInstance("appointmentId", id))
            10 -> transaction.replace(R.id.content_flayout, PwdEditFragment())
        }
        transaction.commit()
    }

    private fun startWorkManager() {
        val workManager = WorkManager.getInstance(this)

        workManager.cancelUniqueWork("LocationWorker")

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest = PeriodicWorkRequestBuilder<LocationWorker>(30, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        workManager.enqueueUniquePeriodicWork(
            "LocationWorker",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }

    override fun onResume() {
        super.onResume()
        startWorkManager()
    }
}