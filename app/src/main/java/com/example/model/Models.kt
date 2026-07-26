package com.example.model

data class Project(
    val id: String,
    val title: String,
    val description: String,
    val date: String,
    val category: String
)

data class Meeting(
    val id: String,
    val title: String,
    val date: String,
    val time: String,
    val location: String,
    val description: String
)

data class MemberProfile(
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val memberType: String = "Guest", // Guest, Current Student, Ex-Student
    val duRegistrationNo: String = "",
    val yearOfEnrollment: String = "",
    val results: String = "",
    val residentHall: String = "",
    val department: String = "",
    val faculty: String = ""
)
