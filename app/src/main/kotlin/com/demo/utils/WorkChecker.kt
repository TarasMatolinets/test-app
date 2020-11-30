package com.demo.utils

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class WorkChecker @Inject constructor(context: Context) {
    private val workManager = WorkManager.getInstance(context)

    fun runWorkWatcher() {
        if (!isWorkScheduled()) {
            setCheckPeriodWorker()
        }
    }

    private fun setCheckPeriodWorker() {
        val builder = PeriodicWorkRequest.Builder(PassCodeCheckWorker::class.java, PERIODIC_CHECK_TIME, TimeUnit.MINUTES).build()
        workManager.enqueueUniquePeriodicWork(WORKER_TAG, ExistingPeriodicWorkPolicy.KEEP, builder)
    }

    private fun isWorkScheduled(): Boolean {
        val statuses = workManager.getWorkInfosByTag(WORKER_TAG)
        val workInfoList: List<WorkInfo> = statuses.get()
        var running = false

        for (workInfo in workInfoList) {
            running = when (workInfo.state) {
                WorkInfo.State.RUNNING,
                WorkInfo.State.ENQUEUED -> {
                    return true
                }
                else -> false
            }
        }
        return running
    }

    companion object {
        const val PERIODIC_CHECK_TIME = 15L
        const val WORKER_TAG = " com.demo.utils.AppCheckWorker"
    }
}