package com.example.printhub

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StatusHistoryAdapter(private val history: List<String>) : RecyclerView.Adapter<StatusHistoryAdapter.StatusViewHolder>() {
    class StatusViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val statusText: TextView = itemView.findViewById(android.R.id.text1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return StatusViewHolder(view)
    }

    override fun onBindViewHolder(holder: StatusViewHolder, position: Int) {
        // Отображаем историю в обратном порядке: последнее изменение сверху
        val reversedIndex = history.size - 1 - position
        val entry = history[reversedIndex]
        // Жирная дата: выделяем до первого ':'
        val colonIndex = entry.indexOf(":")
        if (colonIndex > 0) {
            val date = entry.substring(0, colonIndex)
            val status = entry.substring(colonIndex)
            val spannable = android.text.SpannableString(entry)
            spannable.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, colonIndex, android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            holder.statusText.text = spannable
        } else {
            holder.statusText.text = entry
        }
    }

    override fun getItemCount(): Int = history.size
}
