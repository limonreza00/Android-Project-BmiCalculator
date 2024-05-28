package com.coderscastle.bmi

data class BmiData(
    val weight: Double,
    val heightCm: Double?,
    val heightFeet: Double?,
    val heightInch: Double?,
    val isMale: Boolean,
    val isKg: Boolean
)
