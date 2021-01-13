package com.example.cxensesdk.kotlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.cxensesdk.kotlin.databinding.ListItemBinding

class MainAdapter(
    private val data: List<String>,
    private val clickListener: (String) -> Unit
) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.title.text = data[position]
    }

    override fun getItemCount(): Int = data.size

    inner class ViewHolder(containerView: View) : RecyclerView.ViewHolder(containerView) {
        val binding: ListItemBinding by viewBinding()

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    clickListener(data[position])
                }
            }
        }
    }
}

