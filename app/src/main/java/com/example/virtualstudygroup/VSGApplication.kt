package com.example.virtualstudygroup

import android.app.Application
import android.util.Log
import com.example.virtualstudygroup.chatActivity.MessageActivity
import com.example.virtualstudygroup.chatActivity.NotificationManager
import com.example.virtualstudygroup.groupActivity.ExploreActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class VSGApplication: Application() {
    var currentUser: FirebaseUser? = null
    var groupCount: Int? = null
    var notificationManager: NotificationManager?= null

    override fun onCreate() {
        super.onCreate()

        val groupCountRef = Firebase.database.getReference("groupCount")

        groupCountRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val groupCountValue = dataSnapshot.getValue<Int>()
                if (groupCountValue != null) {
                    groupCount = groupCountValue
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.i(ExploreActivity.TAG, "Failed to read value.", error.toException())
            }
        })

        notificationManager = NotificationManager(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        notificationManager?.stopSendingNotification()
    }
}