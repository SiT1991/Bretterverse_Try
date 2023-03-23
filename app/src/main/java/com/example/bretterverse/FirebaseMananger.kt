import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseManager private constructor() {
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    companion object {
        private var INSTANCE: FirebaseManager? = null

        fun getInstance(): FirebaseManager {
            if (INSTANCE == null) {
                INSTANCE = FirebaseManager()
            }
            return INSTANCE as FirebaseManager
        }
    }

    fun signIn(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(true, null)
                } else {
                    onComplete(false, task.exception?.message)
                }
            }
    }

    fun signOut() {
        auth.signOut()
    }

    fun createUser(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(true, null)
                } else {
                    onComplete(false, task.exception?.message)
                }
            }
    }

    fun getCurrentUser(): String? {
        return auth.currentUser?.uid
    }

    fun getUserProfile(userId: String, onComplete: (Map<String, Any>?, String?) -> Unit) {
        firestore.collection("users").document(userId).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(task.result?.data, null)
                } else {
                    onComplete(null, task.exception?.message)
                }
            }
    }
}