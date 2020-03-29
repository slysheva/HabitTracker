package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.app.Activity
import android.view.Gravity
import android.widget.*
import androidx.core.view.get
import kotlinx.android.synthetic.main.activity_new_habit.*
import java.io.Serializable


class NewHabitActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_habit)
        val bundle = intent.extras
        var id = -1
        if (bundle != null) {
            val habit = bundle.get(MainActivity.HABIT_STRING) as Habit?
            id = bundle.get(MainActivity.ID_STRING) as Int
            if (habit != null) {
                findViewById<EditText>(R.id.editTitle).setText(habit.name)
                findViewById<EditText>(R.id.editDescription).setText(habit.description)
                findViewById<SeekBar>(R.id.habitPriority).progress = habit.priority
                (findViewById<RadioGroup>(R.id.habitType)[(habit.type.num + 1) % 2] as RadioButton)
                    .isChecked = true
                findViewById<EditText>(R.id.editTimes).setText(habit.quantity.toString())
                findViewById<EditText>(R.id.editDays).setText(habit.periodicity.toString())
            }
        }

        val button = findViewById<Button>(R.id.button_create)
        button.setOnClickListener{
            val nameText = findViewById<EditText>(R.id.editTitle).text.toString()
            val descriptionText = findViewById<EditText>(R.id.editDescription).text.toString()
            val priority = findViewById<SeekBar>(R.id.habitPriority).progress
            val typeId = findViewById<RadioGroup>(R.id.habitType).checkedRadioButtonId
            val quantityText = findViewById<EditText>(R.id.editTimes).text.toString()
            val periodicityText = findViewById<EditText>(R.id.editDays).text.toString()
            if (nameText == "" ||
                descriptionText == "" ||
                typeId == -1 ||
                quantityText == "" ||
                periodicityText == "") {
                val toast = Toast.makeText(
                    applicationContext,
                    "Fill all params",
                    Toast.LENGTH_SHORT
                )
                toast.setGravity(Gravity.BOTTOM, 0, 0)
                toast.show()
            }
            else {
                val sendIntent = Intent(this, MainActivity::class.java).apply {
                    val habit = Habit(
                        nameText,
                        descriptionText,
                        priority,
                        HabitType.fromInt(typeId),
                        quantityText.toInt(),
                        periodicityText.toInt()
                    )

                    putExtras(
                        Bundle().apply {
                            putSerializable(MainActivity.HABIT_STRING, habit as Serializable)
                            putInt(MainActivity.ID_STRING, id )
                        })
                }
                setResult(Activity.RESULT_OK, sendIntent)
                finish()
            }
        }
    }
}