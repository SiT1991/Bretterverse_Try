package com.example.bretterverse.auth

import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.bretterverse.R
import com.example.bretterverse.models.User
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        val registerButton = findViewById<Button>(R.id.register_button)
        registerButton.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        val emailEditText = findViewById<EditText>(R.id.email_edittext)
        val passwordEditText = findViewById<EditText>(R.id.password_edittext)
        val displayNameEditText = findViewById<EditText>(R.id.display_name_edittext)

        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()
        val displayName = displayNameEditText.text.toString()

        if (!validateInput(email, password, displayName)) {
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(displayName)
                        .build()

                    user?.updateProfile(profileUpdates)
                        ?.addOnCompleteListener { innerTask ->
                            if (innerTask.isSuccessful) {
                                val newUser = User(user.uid, displayName, email)
                                newUser.saveUserToFirebase()
                                finish()
                            }
                        }
                } else {
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        getString(R.string.register_failed),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
    }

    private fun validateInput(email: String, password: String, displayName: String): Boolean {
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            findViewById<EditText>(R.id.email_edittext).error = getString(R.string.invalid_email)
            return false
        }
        if (password.isEmpty() || password.length < 6) {
            findViewById<EditText>(R.id.password_edittext).error =
                getString(R.string.invalid_password)
            return false
        }
        if (displayName.isEmpty()) {
            findViewById<EditText>(R.id.display_name_edittext).error =
                getString(R.string.invalid_display_name)
            return false
        }
        return true
    }
}
