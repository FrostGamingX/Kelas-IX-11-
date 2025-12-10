package com.example.cgpacalculator

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CourseAdapter(private val courses: MutableList<Course>) :
    RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {

    class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(android.R.id.text1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return CourseViewHolder(view)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = courses[position]
        holder.tvName.text = "${course.name} | SKS: ${course.credit} | Nilai: ${course.grade}"
    }

    override fun getItemCount() = courses.size

    fun addCourse(course: Course) {
        courses.add(course)
        notifyItemInserted(courses.size - 1)
    }

    fun clear() {
        courses.clear()
        notifyDataSetChanged()
    }
}