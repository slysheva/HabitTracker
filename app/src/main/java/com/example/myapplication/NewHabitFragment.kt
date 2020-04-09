package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.get
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.myapplication.viewmodels.EditHabitViewModel
import kotlinx.android.synthetic.main.new_habit_fragment.*
import kotlinx.android.synthetic.main.new_habit_fragment.view.*
import java.io.Serializable
import java.util.*


class NewHabitFragment : Fragment() {
    private var habit: Habit? = null
    private var  idParam: Int = -1
    var callback: OnHabitSelectedListener? = null
    private lateinit var viewModel: EditHabitViewModel
    private lateinit var navController: NavController

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

    private lateinit var myView: View

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = activity as OnHabitSelectedListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            habit = it.get(HABIT_PARAM) as Habit?
            idParam = it.getInt(ID_PARAM, -1)
        }
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return EditHabitViewModel(idParam) as T
            }
        }).get(EditHabitViewModel::class.java)

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
            idParam = it.getInt(ID_PARAM, -1)
        }

        navController = findNavController()

        myView = view
        viewModel.habit.observe(viewLifecycleOwner, Observer { habit ->
            habit?.let { fillFields(it) }
        })
        val deleteButton = view.button_delete

        if (habit != null) {
//            fillFields(habit!!)

            deleteButton.setOnClickListener {
                viewModel.deleteHabit()
                navController.navigateUp()
            }
        }

        if (idParam == -1)
            deleteButton.visibility = View.GONE
        if (habit != null)
            Log.d("tag", "in edit ${habit!!.id}")
        val createButton = view.button_create
        createButton.setOnClickListener{
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
                        if (habit != null) habit!!.id else -1,
                        nameText,
                        descriptionText,
                        priority,
                        if(typeId != typeBadBtn.id ) HabitType.BAD else HabitType.GOOD,
                        quantityText.toInt(),
                        periodicityText.toInt(),
                        Date()
                )
                viewModel.saveHabit(newHabit)
                navController.popBackStack()
              //  navController.navigate(R.id.action_newHabitFragment_to_homeFragment)
            }
        }
    }

    fun fillFields(newHabit: Habit){
        Log.d("tag", "in fill habit ${newHabit.id}")
        editTitle.setText(newHabit.name)
        editDescription.setText(newHabit.description)
        habitPriority.progress = newHabit.priority
        (habitType[(newHabit.type.num + 1) % 2] as RadioButton)
            .isChecked = true
        editTimes.setText(newHabit.quantity.toString())
        editDays.setText(newHabit.periodicity.toString())
    }

    interface OnHabitSelectedListener{
        fun onHabitChange(habit: Habit, pos: Int?, statusCode: Int?)
    }
}
