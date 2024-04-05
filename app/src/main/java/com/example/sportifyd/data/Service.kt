package com.example.sportifyd.data

import com.example.sportifyd.entity.SportEvent
import com.example.sportifyd.entity.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

object Service {
    private val auth: FirebaseAuth = Firebase.auth
    private val usersRef = FirebaseDatabase.getInstance().getReference("users")
    private val eventsRef = FirebaseDatabase.getInstance().getReference("events")

    private fun getCurrentUser() = auth.currentUser

    fun getEventsDataRef() = eventsRef
    fun getUsersDataRef() = usersRef

    fun getUsersAsMutableList(): List<User> {
        val userList: MutableList<User> = mutableListOf()


        usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val user = snapshot.getValue(User::class.java)
                    user?.let {
                        userList.add(it)
                    }
                }
                // Теперь у вас есть список пользователей userList
                // Вы можете использовать его здесь или передать в другой метод для обработки
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Обработка ошибок, если не удалось получить данные из базы данных
            }
        })

        return userList
    }

    fun createUser(email: String, password: String): Task<AuthResult> {
        return auth.createUserWithEmailAndPassword(email, password)
    }
    fun createNewUserToDB(user: User) {
        usersRef.child(getCurrentUser()?.uid ?: "").setValue(user)
    }
    fun createNewEventToDB(event: SportEvent): Task<Void> {
        val key = eventsRef.push().key ?: ""
        return eventsRef.child(key).setValue(event)
    }
    fun subscribeToEvent(eventId: String) {
        usersRef.child(getCurrentUser()?.uid ?: "").child("events").child(eventId).setValue(true)
        eventsRef.child("participants").child(getCurrentUser()?.uid.orEmpty()).setValue(true);
    }
}