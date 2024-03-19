package com.example.tipcalculator

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.example.tipcalculator.databinding.ActivityMainBinding
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var tipPercentage = 0.0
    private var numberOfPeople = 1
    private val numberFormat = NumberFormat.getCurrencyInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.etBaseAmount.addTextChangedListener(textWatcher)
        binding.btnIncreaseNumberOfPeople.setOnClickListener { increaseNumberOfPeople() }
        binding.btnDecreaseNumberOfPeople.setOnClickListener { decreaseNumberOfPeople() }

        binding.seekBarTipPercentage.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                updateTipPercentage(progress / 100.0)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        updateTipPercentage(0.15) // Set initial tip percentage to 15%
        updateNumberOfPeople(1) // Set initial number of people to 1
    }

    private fun updateTipPercentage(newTipPercentage: Double) {
        tipPercentage = newTipPercentage
        binding.tvTipPercentage.text = String.format("%.0f%%", tipPercentage * 100)
        binding.seekBarTipPercentage.progress = (tipPercentage * 100).toInt()
        calculateTip()
    }

    private fun increaseTipPercentage() {
        updateTipPercentage(tipPercentage + 0.01)
    }

    private fun decreaseTipPercentage() {
        updateTipPercentage(tipPercentage - 0.01)
    }

    private fun updateNumberOfPeople(newNumberOfPeople: Int) {
        numberOfPeople = newNumberOfPeople
        binding.etNumberOfPeople.setText(numberOfPeople.toString())
        calculateTip()
    }

    private fun increaseNumberOfPeople() {
        updateNumberOfPeople(numberOfPeople + 1)
    }

    private fun decreaseNumberOfPeople() {
        if (numberOfPeople > 1) {
            updateNumberOfPeople(numberOfPeople - 1)
        }
    }

    private fun calculateTip() {
        val baseAmount = binding.etBaseAmount.text.toString().toDoubleOrNull() ?: 0.0
        val tipAmount = baseAmount * tipPercentage
        val totalAmountForAll = baseAmount + tipAmount
        val totalAmountPerPerson = totalAmountForAll / numberOfPeople

        binding.tvTipAmount.text = numberFormat.format(tipAmount)
        binding.tvTotalAmount.text = numberFormat.format(totalAmountPerPerson)
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) {
            calculateTip()
        }
    }
}