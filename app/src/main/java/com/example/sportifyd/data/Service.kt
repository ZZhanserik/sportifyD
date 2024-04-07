package com.example.sportifyd.data

import com.example.sportifyd.entity.SportEvent
import com.example.sportifyd.entity.SportEventStatus
import com.example.sportifyd.entity.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

object Service {
    private val auth: FirebaseAuth = Firebase.auth
    private val usersRef = FirebaseDatabase.getInstance().getReference("users")
    private val eventsRef = FirebaseDatabase.getInstance().getReference("events")
    private val userPhotoStorageRef = FirebaseStorage.getInstance().getReference("userImages")


    fun getCurrentUser() = auth.currentUser

    fun getEventsDataRef() = eventsRef

    fun getUsersDataRef() = usersRef

    fun getUserPhotoStorageRef() = userPhotoStorageRef
    fun createUser(email: String, password: String): Task<AuthResult> {
        return auth.createUserWithEmailAndPassword(email, password)
    }
    fun createNewUserToDB(user: User) {
        usersRef.child(getCurrentUser()?.uid ?: "").setValue(user)
    }
    fun createNewEventToDB(event: SportEvent): Task<Void> {
        val key = eventsRef.push().key ?: ""
        return eventsRef.child(key).setValue(event.copy(eventId = key))
    }
    fun subscribeToEvent(sportEvent: SportEvent, onSuccess: () -> Unit) {
        usersRef.child(getCurrentUser()?.uid ?: "").child("events").child(sportEvent.eventId).setValue(true)
            .addOnSuccessListener {
                if(sportEvent.participantsNumber == sportEvent.maxParticipants){
                    eventsRef.child(sportEvent.eventId).child("status").setValue(SportEventStatus.CLOSED.name)
                }
                val participantNumber = sportEvent.participantsNumber + 1
                eventsRef.child(sportEvent.eventId).child("participantsNumber").setValue(participantNumber)
                eventsRef.child(sportEvent.eventId).child("participants")
                    .child(getCurrentUser()?.uid.orEmpty())
                    .setValue(true).addOnSuccessListener {
                        onSuccess.invoke()
                    }
            }
    }
}