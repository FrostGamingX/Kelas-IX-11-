package com.example.cgpacalculator

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var etCourseName: EditText
    private lateinit var etCredit: EditText
    private lateinit var spinnerGrade: Spinner
    private lateinit var btnAdd: Button
    private lateinit var btnClear: Button
    private lateinit var tvResult: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CourseAdapter

    private val courses = mutableListOf<Course>()

    // Konversi nilai huruf ke angka (sesuai standar Indonesia)
    private val gradeValues = mapOf(
        "A" to 4.0, "A-" to 3.7,
        "B+" to 3.3, "B" to 3.0, "B-" to 2.7,
        "C+" to 2.3, "C" to 2.0,
        "D" to 1.0, "E" to 0.0
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etCourseName = findViewById(R.id.etCourseName)
        etCredit = findViewById(R.id.etCredit)
        spinnerGrade = findViewById(R.id.spinnerGrade)
        btnAdd = findViewById(R.id.btnAdd)
        btnClear = findViewById(R.id.btnClear)
        tvResult = findViewById(R.id.tvResult)
        recyclerView = findViewById(R.id.recyclerView)

        // Setup Spinner
        ArrayAdapter.createFromResource(
            this,
            R.array.grade_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerGrade.adapter = adapter
        }

        // Setup RecyclerView
        adapter = CourseAdapter(courses)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        btnAdd.setOnClickListener { addCourse() }
        btnClear.setOnClickListener {
            courses.clear()
            adapter.clear()
            updateCGPA()
        }
    }

    private fun addCourse() {
        val name = etCourseName.text.toString().trim()
        val creditText = etCredit.text.toString()
        val grade = spinnerGrade.selectedItem.toString()

        if (name.isEmpty() || creditText.isEmpty()) {
            Toast.makeText(this, "Isi semua data!", Toast.LENGTH_SHORT).show()
            return
        }

        val credit = creditText.toIntOrNull()
        if (credit == null || credit <= 0) {
            Toast.makeText(this, "SKS tidak valid!", Toast.LENGTH_SHORT).show()
            return
        }

        val course = Course(name, credit, grade)
        courses.add(course)
        adapter.addCourse(course)

        // Bersihkan input
        etCourseName.text?.clear()
        etCredit.text?.clear()
        spinnerGrade.setSelection(0)

        updateCGPA()
    }

    private fun updateCGPA() {
        if (courses.isEmpty()) {
            tvResult.text = "Total SKS: 0 | CGPA: 0.00"
            return
        }

        var totalPoints = 0.0
        var totalCredits = 0

        for (course in courses) {
            val value = gradeValues[course.grade] ?: 0.0
            totalPoints += value * course.credit
            totalCredits += course.credit
        }

        val cgpa = totalPoints / totalCredits
        tvResult.text = "Total SKS: $totalCredits | CGPA: ${String.format("%.2f", cgpa)}"
    }
}