package com.example.bretterverse.chatroom

import android.os.Bundle
import android.text.format.DateFormat
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bretterverse.BretterverseApplication
import com.example.bretterverse.R
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class ChatroomActivity : AppCompatActivity() {

    private lateinit var messageEditText: EditText
    private lateinit var sendButton: Button
    private lateinit var messageRecyclerView: RecyclerView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messages: MutableList<Message>
    private lateinit var childEventListener: ChildEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatroom)

        // Initialisiere FirebaseManager
        (application as BretterverseApplication).initFirebaseManager()

        // Setze Title des Chatrooms
        val chatroomTitle = findViewById<TextView>(R.id.chatroom_title)
        val chatroomName = intent.getStringExtra("CHATROOM_NAME") ?: "Chatroom"
        chatroomTitle.text = chatroomName

        // Initialisiere Views
        messageEditText = findViewById(R.id.message_edit_text)
        sendButton = findViewById(R.id.send_button)
        messageRecyclerView = findViewById(R.id.message_recycler_view)

        // Initialisiere Messages
        messages = mutableListOf()
        messageAdapter = MessageAdapter(messages)
        messageRecyclerView.adapter = messageAdapter
        messageRecyclerView.layoutManager = LinearLayoutManager(this)

        // Lade Nachrichten aus der Datenbank
        val databaseReference = FirebaseDatabase.getInstance().reference
        val chatroomReference = databaseReference.child("chatrooms").child(chatroomName)
        childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                val message = dataSnapshot.getValue(Message::class.java)
                messages.add(message!!)
                messageAdapter.notifyItemInserted(messages.size - 1)
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                // not used
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                // not used
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                // not used
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // not used
            }
        }
        chatroomReference.addChildEventListener(childEventListener)

        // Formatierung von Nachrichten
        val dateFormat = "dd/MM/yyyy HH:mm:ss"
        val calendar = Calendar.getInstance()
        val timeZone = TimeZone.getTimeZone("UTC")
        val formatter = DateFormat.getDateFormat(this).apply {
            setTimeZone(timeZone)
        }
        val timeFormatter = DateFormat.getTimeFormat(this).apply {
            setTimeZone(timeZone)
        }

        // Versende Nachrichten
        sendButton.setOnClickListener {
            val messageText = messageEditText.text.toString()
            if (messageText.isNotBlank()) {
                val currentDateTimeString = formatter.format(calendar.time) + " " + timeFormatter.format(calendar.time)
                val message = Message(currentDateTimeString, messageText)
                chatroomReference.push().setValue(message)
                messageEditText.text.clear()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val databaseReference = FirebaseDatabase.getInstance().reference
        val chatroomReference = databaseReference.child("chatrooms").child(chatroomId)
        messagesEventListener?.let {
            chatroomReference.removeEventListener(it)
        }
    }
}