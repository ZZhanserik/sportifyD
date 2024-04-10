package com.example.sportify

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.sportify.data.Service
import com.example.sportify.databinding.ActivityMainBinding
import com.example.sportify.entity.User
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_base)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_search,
                R.id.navigation_new_contest,
                R.id.navigation_notifications,
                R.id.navigation_profile
            )
        )

        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        supportActionBar?.hide()

        Service.getUsersDataRef().child(Service.getCurrentUser()?.uid.orEmpty())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val userProfile = dataSnapshot.getValue(User::class.java)
                    saveUserDataToSharedPreferences(userProfile!!, this@MainActivity)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Обработка ошибок, если не удалось получить данные из базы данных
                }
            })
    }

}

fun saveUserDataToSharedPreferences(user: User, context: Context) {
    val gson = Gson()
    val userJsonString = gson.toJson(user)

    val sharedPreferences = context.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString("user_data", userJsonString)
    editor.apply()
}