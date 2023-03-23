package com.example.bretterverse.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bretterverse.databinding.BoardItemBinding
import com.example.bretterverse.models.Board

class BoardAdapter(private val dataset: List<Board>, private val onClickListener: (Board) -> Unit) :
    RecyclerView.Adapter<BoardAdapter.ViewHolder>() {

    class ViewHolder(val binding: BoardItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            BoardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataset[position]
        holder.binding.boardName.text = item.name
        holder.binding.boardDescription.text = item.description
        holder.binding.boardImage.setImageResource(item.imageResource)
        holder.itemView.setOnClickListener { onClickListener(item) }
    }

    override fun getItemCount() = dataset.size
}