package com.julietgisemba.kipesa.ui.screens

import android.content.Context
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.julietgisemba.kipesa.R
import com.julietgisemba.kipesa.navigation.Destinations

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Kipesa", fontWeight = FontWeight.Bold) }
            )
        },
        containerColor = Color(0x54EFFBF6)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 15.dp)
        ) {
            HorizontalDivider()
            Spacer(Modifier.height(60.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Icon(
                    painter = painterResource(R.drawable.app_icon),
                    contentDescription = "app_icon"
                )
                Spacer(Modifier.width(10.dp))
                Column {
                    Text(
                        "Welcome back",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = Color(0xFF2C8A5B)
                    )
                    Text(
                        "Sign in to view your finances",
                        fontWeight = FontWeight.Light,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = false
                                },
                label = { Text("Password") },
                isError = passwordError,
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible) Icons.Filled.Info else Icons.Filled.CheckCircle
                    val description = if (passwordVisible) "Hide password" else "Show password"
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = description)
                    }
                }
            )

            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                Checkbox(false, onCheckedChange = {})
                TextButton(onClick = {}) {
                    Text("I agree to the Terms & Privacy")
                }
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    validateAndLogin(navController, context, email, password, {
                        success -> passwordError = !success
                    })
                },
                colors = ButtonDefaults.buttonColors(Color(0xFF2C8A5B))
            ) {
                Row {
                    Icon(Icons.Default.ArrowForward, contentDescription = "login_icon")
                    Spacer(Modifier.width(6.dp))
                    Text("Sign in")
                }
            }

            Spacer(Modifier.height(10.dp))
            Row {
                Text("Don't have an account? ")
                Text(
                    "Sign Up",
                    color = Color(0xFF2C8A5B),
                    modifier = Modifier.clickable {
                        navController.navigate(Destinations.SignUp.route)
                    }
                )
            }
        }
    }
}

fun validateAndLogin(navController: NavController, context: Context, email: String, pass: String, onResult: (Boolean) -> Unit) {
    when {
        email.isBlank() -> {
            Toast.makeText(context, "Please enter your email", Toast.LENGTH_SHORT).show()
            return
        }

        !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
            Toast.makeText(context, "Please enter a valid email", Toast.LENGTH_SHORT).show()
            return
        }

        pass.isBlank() -> {
            Toast.makeText(context, "Please enter your password", Toast.LENGTH_SHORT).show()
            return
        }

        pass.length < 6 -> {
            Toast.makeText(context, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
            return
        }

        else -> {
            val firebaseAuth = FirebaseAuth.getInstance()
            firebaseAuth.signInWithEmailAndPassword(email.trim(), pass)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onResult(true)
                        Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                        navController.navigate(Destinations.Dashboard.route) {
                            popUpTo(Destinations.Login.route) { inclusive = true }
                        }
                    } else {
                        onResult(false)
                        Toast.makeText(
                            context,
                            task.exception?.message ?: "Login failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }
}
