package com.demo.mvvm.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ShapeParams(
    var clockWise: Boolean = true,
    var speed: Int = 5,
    var size: Int = 1
) : Parcelable



