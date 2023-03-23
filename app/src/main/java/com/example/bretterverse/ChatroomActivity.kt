package com.example.bretterverse.chatroom

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bretterverse.R
import com.google.firebase.database.*

class ChatroomActivity : AppCompatActivity() {

    private lateinit var messageRecyclerView: RecyclerView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>

    private lateinit var messageEditText: EditText
    private lateinit var sendButton: ImageButton

    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatroom)

        // Initialisiere RecyclerView, Adapter und MessageList
        messageRecyclerView = findViewById(R.id.message_recyclerview)
        messageList = ArrayList()
        messageAdapter = MessageAdapter(this, messageList)
        messageRecyclerView.adapter = messageAdapter
        messageRecyclerView.layoutManager = LinearLayoutManager(this)

        // Initialisiere Firebase Database Referenz
        databaseReference = FirebaseDatabase.getInstance().reference.child("messages")

        // Initialisiere UI-Elemente
        messageEditText = findViewById(R.id.message_input_edittext)
        sendButton = findViewById(R.id.message_input_layout_endIcon)

        sendButton.setOnClickListener {
            val messageText = messageEditText.text.toString()
            if (messageText.isNotEmpty()) {
                val message = Message("username_placeholder", messageText)
                databaseReference.push().setValue(message)
                messageEditText.text.clear()
            }
        }

        // Füge ValueEventListener hinzu, um Nachrichten aus der Datenbank zu laden
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                messageList.clear()
                for (messageSnapshot in snapshot.children) {
                    val message = messageSnapshot.getValue(Message::class.java)
                    if (message != null) {
                        messageList.add(message)
                    }
                }
                messageAdapter.notifyDataSetChanged()
                messageRecyclerView.smoothScrollToPosition(messageAdapter.itemCount)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle cancelled event
            }
        })
    }

    private fun formatMessageText(messageText: String): String {
        // Hier kann die Nachricht formatiert werden, z.B. durch Ersetzen von Emojis oder Links
        return messageText
    }
}
