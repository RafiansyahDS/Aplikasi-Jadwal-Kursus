package com.dicoding.courseschedule.ui.add

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.data.DataRepository
import com.dicoding.courseschedule.ui.home.HomeActivity
import com.dicoding.courseschedule.util.TimePickerFragment
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

class AddCourseActivity : AppCompatActivity(), TimePickerFragment.DialogTimeListener {
    private lateinit var addCourseViewmodel: AddCourseViewModel
    private lateinit var edtCourseName: TextInputEditText
    private lateinit var spnDay: Spinner
    private lateinit var tvStartTime: TextView
    private lateinit var tvEndTime: TextView
    private lateinit var edtLecturer: TextInputEditText
    private lateinit var edtNote: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_course)

        tvStartTime = findViewById(R.id.tv_start_time)
        tvEndTime = findViewById(R.id.tv_end_time)
        edtCourseName = findViewById(R.id.edt_course_name)
        edtLecturer = findViewById(R.id.edt_lecturer)
        edtNote = findViewById(R.id.edt_note)
        spnDay = findViewById(R.id.spn_Day)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_insert -> {
                val repository = DataRepository.getInstance(this)
                addCourseViewmodel = AddCourseViewModel(repository!!)

                if(!(edtCourseName.text.toString() == "" || tvEndTime.text.toString() == "" || tvStartTime.text.toString() == "")){
                    addCourseViewmodel.insertCourse(
                        courseName = edtCourseName.text.toString(),
                        lecturer = edtLecturer.text.toString(),
                        note = edtNote.text.toString(),
                        startTime = tvStartTime.text.toString(),
                        endTime = tvEndTime.text.toString(),
                        day = spnDay.selectedItemPosition)

                    val toHomeActivity = Intent(this, HomeActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(toHomeActivity)
                }else{
                    Toast.makeText(this, getString(R.string.input_empty_message), Toast.LENGTH_SHORT).show()
                }
                true
            }
            else->super.onOptionsItemSelected(item)
        }
    }

    fun showTimeStartPicker(view: View) {
        val timeFragment = TimePickerFragment()
        timeFragment.show(supportFragmentManager, "StartTime")
    }

    fun showTimeEndPicker(view: View) {
        val timeFragment = TimePickerFragment()
        timeFragment.show(supportFragmentManager, "EndTime")
    }

    override fun onDialogTimeSet(tag: String?, hour: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val time = timeFormat.format(calendar.time)

        when(tag){
            "StartTime" ->{
                tvStartTime.text = time
            }
            "EndTime" ->{
                tvEndTime.text = time
            }
        }
    }
}