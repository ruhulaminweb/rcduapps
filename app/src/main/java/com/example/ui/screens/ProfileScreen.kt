package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.MainViewModel
import com.example.model.MemberProfile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(viewModel: MainViewModel) {
    val profileState by viewModel.profileState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    var name by remember(profileState) { mutableStateOf(profileState.name) }
    var email by remember(profileState) { mutableStateOf(profileState.email) }
    var phone by remember(profileState) { mutableStateOf(profileState.phone) }
    var memberType by remember(profileState) { mutableStateOf(profileState.memberType) }
    var duRegistrationNo by remember(profileState) { mutableStateOf(profileState.duRegistrationNo) }
    var yearOfEnrollment by remember(profileState) { mutableStateOf(profileState.yearOfEnrollment) }
    var residentHall by remember(profileState) { mutableStateOf(profileState.residentHall) }
    var department by remember(profileState) { mutableStateOf(profileState.department) }
    var faculty by remember(profileState) { mutableStateOf(profileState.faculty) }

    val memberTypes = listOf("Guest", "Current Student", "Ex-Student")
    var expanded by remember { mutableStateOf(false) }
    var showSnackbar by remember { mutableStateOf(false) }

    if (showSnackbar) {
        Snackbar(
            modifier = Modifier.padding(16.dp),
            action = {
                TextButton(onClick = { showSnackbar = false }) {
                    Text("OK")
                }
            }
        ) {
            Text("Profile updated successfully!")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Member Registration / Profile",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(16.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email (Unique)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Mobile Number (Unique)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = memberType,
                onValueChange = {},
                readOnly = true,
                label = { Text("Member Type") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                memberTypes.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type) },
                        onClick = {
                            memberType = type
                            expanded = false
                        }
                    )
                }
            }
        }

        if (memberType == "Current Student" || memberType == "Ex-Student") {
            Text(
                text = "DU Academic Info",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 8.dp)
            )

            OutlinedTextField(
                value = duRegistrationNo,
                onValueChange = { duRegistrationNo = it },
                label = { Text("DU Registration No") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(16.dp)
            )
            OutlinedTextField(
                value = yearOfEnrollment,
                onValueChange = { yearOfEnrollment = it },
                label = { Text("Year of Enrollment") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            OutlinedTextField(
                value = residentHall,
                onValueChange = { residentHall = it },
                label = { Text("Resident Hall") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(16.dp)
            )
            OutlinedTextField(
                value = faculty,
                onValueChange = { faculty = it },
                label = { Text("Faculty") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(16.dp)
            )
            OutlinedTextField(
                value = department,
                onValueChange = { department = it },
                label = { Text("Department") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.updateProfile(
                    MemberProfile(
                        name = name,
                        email = email,
                        phone = phone,
                        memberType = memberType,
                        duRegistrationNo = duRegistrationNo,
                        yearOfEnrollment = yearOfEnrollment,
                        residentHall = residentHall,
                        department = department,
                        faculty = faculty
                    )
                )
                showSnackbar = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("submit_profile_button"),
            shape = RoundedCornerShape(16.dp),
            contentPadding = PaddingValues(vertical = 14.dp)
        ) {
            Icon(Icons.Filled.Person, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Update Profile")
        }
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}
