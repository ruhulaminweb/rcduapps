package com.example

import androidx.lifecycle.ViewModel
import com.example.model.Meeting
import com.example.model.MemberProfile
import com.example.model.Project
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel : ViewModel() {

    private val _profileState = MutableStateFlow(MemberProfile())
    val profileState: StateFlow<MemberProfile> = _profileState.asStateFlow()

    private val _projects = MutableStateFlow<List<Project>>(emptyList())
    val projects: StateFlow<List<Project>> = _projects.asStateFlow()

    private val _meetings = MutableStateFlow<List<Meeting>>(emptyList())
    val meetings: StateFlow<List<Meeting>> = _meetings.asStateFlow()

    init {
        loadMockData()
    }

    private fun loadMockData() {
        _projects.value = listOf(
            Project("1", "Blood Donation Camp", "Annual blood donation camp organized at DU Medical Center.", "Oct 15, 2026", "Health"),
            Project("2", "Winter Clothes Distribution", "Distributing warm clothes to the underprivileged.", "Dec 05, 2026", "Community"),
            Project("3", "Leadership Workshop", "Skill development workshop for new members.", "Nov 20, 2026", "Professional Development")
        )

        _meetings.value = listOf(
            Meeting("1", "General Members Meeting", "Oct 10, 2026", "4:00 PM", "TSC Cafeteria", "Monthly general meeting to discuss upcoming projects."),
            Meeting("2", "Board Meeting", "Oct 12, 2026", "5:30 PM", "Virtual (Zoom)", "Board of Directors meeting for budget approval.")
        )
    }

    fun updateProfile(newProfile: MemberProfile) {
        _profileState.update { newProfile }
    }
}
