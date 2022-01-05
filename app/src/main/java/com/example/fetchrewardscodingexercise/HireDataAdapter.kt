package com.example.fetchrewardscodingexercise

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HireDataAdapter() : RecyclerView.Adapter<HireDataAdapter.ViewHolder>() {
    private var data: List<RSListData> = emptyList()

    //Implementation of the ViewHolder Class
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val listIdTextView: TextView
        val nameTextView: TextView

        init {
            listIdTextView = view.findViewById(R.id.listID_tv)
            nameTextView = view.findViewById(R.id.name_tv)
        }
    }
    fun setData(d: List<RSListData>) {
        this.data = d
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.hire_item, parent, false)

        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.listIdTextView.text = this.data[position].listId.toString()
        holder.nameTextView.text = this.data[position].name
    }
    override fun getItemCount(): Int {
        return this.data.size
    }
}