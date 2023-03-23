package com.example.bretterverse.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bretterverse.R

class ChatMessageAdapter(private val chatMessages: List<ChatMessage>) :
    RecyclerView.Adapter<ChatMessageAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageTextView: TextView = itemView.findViewById(R.id.message_text_view)
        val senderTextView: TextView = itemView.findViewById(R.id.sender_text_view)
        val timestampTextView: TextView = itemView.findViewById(R.id.timestamp_text_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.chat_message_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chatMessage = chatMessages[position]
        holder.messageTextView.text = chatMessage.text
        holder.senderTextView.text = chatMessage.senderId
        holder.timestampTextView.text = chatMessage.timestamp.toString()
    }

    override fun getItemCount() = chatMessages.size
}
