package com.example.sportifyd.data

import com.example.sportifyd.entity.SportEvent
import com.example.sportifyd.entity.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

object Service {

    private val auth: FirebaseAuth = Firebase.auth
    private val usersRef = FirebaseDatabase.getInstance().getReference("users")
    private val eventsRef = FirebaseDatabase.getInstance().getReference("events")

    fun getCurrentUser() = auth.currentUser

    fun createUser(email: String, password:String): Task<AuthResult> {
        return auth.createUserWithEmailAndPassword(email, password)
    }

    fun addNewUserToDB(userId: String, user: User){
        usersRef.child(userId).setValue(user)
    }

    fun addNewEventToDB(event: SportEvent){
        val key = eventsRef.push().key?:""
        eventsRef.child(key).setValue(event)
    }


}