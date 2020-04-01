package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.get
import kotlinx.android.synthetic.main.new_habit_fragment.*
import kotlinx.android.synthetic.main.new_habit_fragment.view.*
import java.io.Serializable


class NewHabitFragment : Fragment() {
    private var habit: Habit? = null
    private var idParam: Int? = null
    private var statusCode: Int? = null
    var callback: OnHabitSelectedListener? = null

    companion object {
        private const val HABIT_PARAM = "HABIT"
        private const val ID_PARAM = "ID"
        private const val STATUS_CODE = "STATUS"

        fun newInstance(habit: Habit?, idParam: Int?, status: Int) =
            NewHabitFragment().apply {
                arguments = Bundle().apply {
                    if (habit != null)
                        putSerializable(HABIT_PARAM, habit as Serializable)
                    if (idParam != null) {
                        putInt(ID_PARAM, idParam)
                    }
                    putInt(STATUS_CODE, status)
                }
            }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = activity as OnHabitSelectedListener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.new_habit_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            habit = it.get(HABIT_PARAM) as Habit?
            idParam = it.get(ID_PARAM) as Int?
            statusCode = it.get(STATUS_CODE) as Int
        }

        if (habit != null) {

            view.editTitle.setText(habit!!.name)
            view.editDescription.setText(habit!!.description)
            view.habitPriority.progress = habit!!.priority
            (view.habitType[(habit!!.type.num + 1) % 2] as RadioButton)
                .isChecked = true
            view.editTimes.setText(habit!!.quantity.toString())
            view.editDays.setText(habit!!.periodicity.toString())
        }

        val button = view.button_create
        button.setOnClickListener{
            val nameText = view.editTitle.text.toString()
            val descriptionText = view.editDescription.text.toString()
            val priority = view.habitPriority.progress
            val typeId = view.habitType.checkedRadioButtonId
            val quantityText = view.editTimes.text.toString()
            val periodicityText = view.editDays.text.toString()
            if (nameText == "" ||
                descriptionText == "" ||
                typeId == -1 ||
                quantityText == "" ||
                periodicityText == "") {
                val toast = Toast.makeText(
                    getActivity(),
                    "Fill all params",
                    Toast.LENGTH_SHORT
                )
                toast.setGravity(Gravity.BOTTOM, 0, 0)
                toast.show()
            }
            else {
                val newHabit = Habit(
                        nameText,
                        descriptionText,
                        priority,
                        if(typeId != typeBadBtn.id ) HabitType.BAD else HabitType.GOOD,
                        quantityText.toInt(),
                        periodicityText.toInt()
                )

                callback?.onHabitChange(newHabit, idParam, statusCode)
            }
        }
    }

    interface OnHabitSelectedListener{
        fun onHabitChange(habit: Habit, pos: Int?, statusCode: Int?)
    }
}
