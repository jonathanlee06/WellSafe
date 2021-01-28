package com.example.wellsafe.api

import com.example.wellsafe.authentication.Users
import com.example.wellsafe.ui.checkin.CheckInSummary
import com.example.wellsafe.ui.profile.EditProfile
import com.example.wellsafe.ui.profile.ProfileFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class FirebaseUtilsKot {
    var reference: DatabaseReference? = null
    var user: FirebaseUser? = null
    val profileData: Unit
        get() {
            reference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().currentUser!!.uid).child("User Information")
            reference!!.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val userInfo = snapshot.getValue(Users::class.java)
                        val profileFragment = ProfileFragment()
                        ProfileFragment.fullNameData = userInfo!!.fullName
                        ProfileFragment.emailData = userInfo.email
                        ProfileFragment.phoneNumberData = userInfo.phone
                        EditProfile.fullNameProfile = userInfo.fullName
                        EditProfile.emailProfile = userInfo.email
                        EditProfile.phoneNumberProfile = userInfo.phone
                        CheckInSummary.nameSummary = userInfo.fullName
                        CheckInSummary.phoneSummary = userInfo.phone
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }
    val summaryData: Unit
        get() {
            reference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().currentUser!!.uid).child("User Information")
            reference!!.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val userInfo = snapshot.getValue(Users::class.java)
                        CheckInSummary.nameSummary = userInfo!!.fullName
                        CheckInSummary.phoneSummary = userInfo.phone
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }

    fun deleteProfile() {
        val userID = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().currentUser!!.uid).key
        reference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().currentUser!!.uid)
        user = FirebaseAuth.getInstance().currentUser
        reference!!.removeValue()
    }
}