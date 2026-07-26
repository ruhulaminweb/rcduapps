package com.example.ui.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.example.BuildConfig
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(onSignInSuccess: () -> Unit) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // App Logo / Title
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "R",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "Rotaract Club",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp
        )
        Text(
            text = "Dhaka University",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold
        )
        
        Spacer(modifier = Modifier.height(48.dp))

        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        Button(
            onClick = {
                isLoading = true
                errorMessage = null
                coroutineScope.launch {
                    try {
                        signInWithGoogle(context, onSignInSuccess)
                    } catch (e: Exception) {
                        Log.e("LoginScreen", "Sign in failed", e)
                        errorMessage = "Sign in failed: ${e.message}"
                        isLoading = false
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            contentPadding = PaddingValues(vertical = 14.dp),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Icon(Icons.Filled.Email, contentDescription = "Google Sign In")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Sign in with Google")
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "By signing in, you agree to our Terms and Conditions.",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

suspend fun signInWithGoogle(context: Context, onSuccess: () -> Unit) {
    val credentialManager = CredentialManager.create(context)
    
    // Check if WEB_CLIENT_ID is available in BuildConfig (from secrets)
    // If not, we might fail gracefully or throw an error indicating configuration is missing
    val webClientId = try {
        BuildConfig::class.java.getField("WEB_CLIENT_ID").get(null) as String
    } catch (e: Exception) {
        throw Exception("WEB_CLIENT_ID is not configured in Secrets.")
    }

    if (webClientId.isEmpty() || webClientId == "\"YOUR_WEB_CLIENT_ID\"" || webClientId == "YOUR_WEB_CLIENT_ID") {
        throw Exception("WEB_CLIENT_ID is missing. Please configure it in AI Studio Secrets.")
    }
    
    val googleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setServerClientId(webClientId)
        .build()

    val request = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    val result = credentialManager.getCredential(context, request)
    val credential = result.credential

    if (credential is GoogleIdTokenCredential) {
        val authCredential = GoogleAuthProvider.getCredential(credential.idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(authCredential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess()
            } else {
                throw task.exception ?: Exception("Authentication failed")
            }
        }
    } else {
        throw Exception("Unexpected type of credential")
    }
}
