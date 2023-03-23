package com.example.bretterverse

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask

object FirebaseManager {
    val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    val storage: FirebaseStorage by lazy { FirebaseStorage.getInstance() }

    fun init() {
        // Hier kannst du Firebase initialisieren und weitere Konfigurationen vornehmen
    }

    // Funktionen zum Hinzufügen, Aktualisieren oder Löschen von Daten in der Firebase Firestore-Datenbank
    fun addDocument(collectionPath: String, data: Any): Task<DocumentReference> {
        return firestore.collection(collectionPath).add(data)
    }

    fun updateDocument(collectionPath: String, documentId: String, data: Map<String, Any>): Task<Void> {
        return firestore.collection(collectionPath).document(documentId).update(data)
    }

    fun deleteDocument(collectionPath: String, documentId: String): Task<Void> {
        return firestore.collection(collectionPath).document(documentId).delete()
    }

    // Funktionen zum Speichern oder Löschen von Dateien in der Firebase Storage-Cloud
    fun uploadFile(path: String, file: Uri): UploadTask {
        return storage.reference.child(path).putFile(file)
    }

    fun deleteFile(path: String): Task<Void> {
        return storage.reference.child(path).delete()
    }

    fun initialize() {
        TODO("Not yet implemented")
    }
}
