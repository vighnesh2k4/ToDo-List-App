package com.example.todolist

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import com.example.todolist.R
import androidx.appcompat.app.AlertDialog

import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var toDoList: MutableList<String>
    private lateinit var toDoListView: ListView
    private lateinit var dateTextView: TextView
    private lateinit var timeTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toDoList = mutableListOf()
        val addTaskButton = findViewById<Button>(R.id.addTaskButton)
        val taskEditText = findViewById<EditText>(R.id.taskEditText)
        toDoListView = findViewById<ListView>(R.id.toDoListView)
        dateTextView = findViewById(R.id.dateTextView)
        timeTextView = findViewById(R.id.timeTextView)
        dateTextView.text = getString(R.string.selected_date)
        timeTextView.text = getString(R.string.selected_time)

        addTaskButton.setOnClickListener {
            val task = taskEditText.text.toString()
            if (task.isNotBlank() && dateTextView.text != getString(R.string.selected_date) && timeTextView.text != getString(R.string.selected_time)) {
                toDoList.add("$task - Date: ${dateTextView.text}, Time: ${timeTextView.text}")
                taskEditText.text.clear()
                dateTextView.text = getString(R.string.selected_date)
                timeTextView.text = getString(R.string.selected_time)
                updateToDoList()
            } else {
                Toast.makeText(this, "Please enter task and select date/time", Toast.LENGTH_SHORT).show()
            }
        }

        val dateButton = findViewById<Button>(R.id.dateButton)
        val timeButton = findViewById<Button>(R.id.timeButton)

        dateButton.setOnClickListener {
            showDatePicker()
        }

        timeButton.setOnClickListener {
            showTimePicker()
        }

        toDoListView.setOnItemLongClickListener { _, _, position, _ ->
            showContextMenu(position)
            true
        }

        updateToDoList()
    }

    private fun showContextMenu(position: Int) {
        val menuItems = arrayOf("Mark as Completed", "Delete Task")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose Action")
        builder.setItems(menuItems) { _, which ->
            when (which) {
                0 -> markTaskAsComplete(position)
                1 -> deleteTask(position)
            }
        }
        builder.create().show()
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                dateTextView.text = selectedDate
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val timePickerDialog = TimePickerDialog(
            this,
            { _, selectedHour, selectedMinute ->
                val selectedTime = "$selectedHour:$selectedMinute"
                timeTextView.text = selectedTime
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
        timePickerDialog.show()
    }

    private fun markTaskAsComplete(position: Int) {
        toDoList.removeAt(position)
        updateToDoList()
    }

    private fun deleteTask(position: Int) {
        toDoList.removeAt(position)
        updateToDoList()
    }

    private fun updateToDoList() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, toDoList)
        toDoListView.adapter = adapter
    }
}
