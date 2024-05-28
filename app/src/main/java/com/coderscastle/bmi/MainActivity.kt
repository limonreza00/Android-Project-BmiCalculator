package com.coderscastle.bmi

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: BmiViewModel
    private lateinit var calculateButton: MaterialButton
    private lateinit var resultTextView: TextView
    private lateinit var feetLayout: LinearLayout
    private lateinit var cmEditText: TextInputEditText
    private lateinit var feetEditText: TextInputEditText
    private lateinit var inchEditText: TextInputEditText
    private lateinit var weightEditText: TextInputEditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel = ViewModelProvider(this).get(BmiViewModel::class.java)

        calculateButton = findViewById(R.id.calculateButton)
        resultTextView = findViewById(R.id.resultTextView)
        feetLayout = findViewById(R.id.feetLayout)
        cmEditText = findViewById(R.id.heightCmEdit)
        feetEditText = findViewById(R.id.heightFeetEdit)
        inchEditText = findViewById(R.id.heightInchEdit)
        weightEditText = findViewById(R.id.weightEditText)

        val radioGroupHeight = findViewById<RadioGroup>(R.id.heightGroup)
        radioGroupHeight.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.feetRadio -> {
                    feetLayout.visibility = View.VISIBLE
                    cmEditText.visibility = View.GONE
                }
                R.id.cmRadio -> {
                    feetLayout.visibility = View.GONE
                    cmEditText.visibility = View.VISIBLE
                }
            }
        }

        calculateButton.setOnClickListener {
            val weight = weightEditText.text.toString().toDoubleOrNull() ?: 0.0
            val isMale = getGenderSelection()
            val isKg = getUnitSelection()
            val bmiData = getBmiData(weight, isMale, isKg)

            viewModel.calculateBmi(bmiData)
        }

        viewModel.bmiResult.observe(this, Observer { result ->
            resultTextView.text = result
        })

    }

    private fun getBmiData(weight: Double, isMale: Boolean, isKg: Boolean): BmiData {
        val heightInCm = cmEditText.text.toString().toDoubleOrNull()
        val heightInFeet = feetEditText.text.toString().toDoubleOrNull()
        val heightInInches = inchEditText.text.toString().toDoubleOrNull()

        return BmiData(
            weight = weight,
            heightCm = heightInCm,
            heightFeet = heightInFeet,
            heightInch = heightInInches,
            isMale = isMale,
            isKg = isKg
        )
    }

    private fun getGenderSelection(): Boolean {
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
        val checkedRadioButtonId = radioGroup.checkedRadioButtonId
        val radioButton = findViewById<RadioButton>(checkedRadioButtonId)
        return radioButton.text.toString() == "Male"
    }

    private fun getUnitSelection(): Boolean {
        val radioGroup = findViewById<RadioGroup>(R.id.weightGroup)
        val checkedRadioButtonId = radioGroup.checkedRadioButtonId
        val radioButton = findViewById<RadioButton>(checkedRadioButtonId)
        return radioButton.text.toString() == "Kg"
    }
}