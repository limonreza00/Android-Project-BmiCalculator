package com.coderscastle.bmi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BmiViewModel : ViewModel() {

    private val _bmiResult = MutableLiveData<String>()
    val bmiResult: LiveData<String> = _bmiResult

    fun calculateBmi(data: BmiData) {
        val heightInMeters = when {
            data.heightCm != null -> data.heightCm / 100.0
            data.heightFeet != null && data.heightInch != null -> {
                ((data.heightFeet * 12) + data.heightInch) * 0.0254
            }
            else -> 0.0
        }

        if (heightInMeters == 0.0) {
            _bmiResult.value = "Invalid height input"
            return
        }

        val weightInKg = if (data.isKg) data.weight else data.weight * 0.453592

        if (weightInKg == 0.0) {
            _bmiResult.value = "Invalid weight input"
            return
        }

        val bmi = weightInKg / (heightInMeters * heightInMeters)
        val bmiFormatted = String.format("%.2f", bmi)

        _bmiResult.value = getBmiCategory(bmi) + "\nBMI : $bmiFormatted"
    }

    private fun getBmiCategory(bmi: Double): String {
        return when {
            bmi < 18.5 -> "Underweight"
            bmi < 25 -> "Normal weight"
            bmi < 30 -> "Overweight"
            else -> "Obese"
        }
    }
}
