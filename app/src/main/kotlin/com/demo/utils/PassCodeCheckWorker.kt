package com.demo.utils

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class PassCodeCheckWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        SharedPreferencesHelper.lockApp = false
        return Result.success()
    }
}