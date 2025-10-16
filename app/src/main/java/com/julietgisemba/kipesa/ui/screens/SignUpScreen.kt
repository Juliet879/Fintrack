package com.julietgisemba.kipesa.ui.screens

import android.content.Context
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
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
fun SignUpScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
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
                        "Create your account",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color(0xFF2C8A5B)
                    )
                    Text(
                        "Start tracking, spending, saving",
                        fontWeight = FontWeight.Light,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            // 🔐 Password field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
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

            // 🔐 Confirm password field
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible) Icons.Filled.Info else Icons.Filled.CheckCircle
                    val description = if (confirmPasswordVisible) "Hide password" else "Show password"
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
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
                onClick = {
                    validateAndSignUp(navController, context, name, email, password, confirmPassword)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(Color(0xFF2C8A5B))
            ) {
                Row {
                    Icon(Icons.Default.Person, contentDescription = "person_icon")
                    Spacer(Modifier.width(6.dp))
                    Text("Create Account")
                }
            }

            Spacer(Modifier.height(10.dp))
            Row {
                Text("Already have an account?  ")
                Text(
                    "Login",
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF2C8A5B),
                    modifier = Modifier.clickable {
                        navController.navigate(Destinations.Login.route)
                    }
                )
            }
        }
    }
}

fun validateAndSignUp(
    navController: NavController,
    context: Context,
    name: String,
    email: String,
    pass: String,
    confirmPass: String
) {
    when {
        name.isBlank() -> {
            Toast.makeText(context, "Please enter your full name", Toast.LENGTH_SHORT).show()
            return
        }

        email.isBlank() -> {
            Toast.makeText(context, "Please enter your email", Toast.LENGTH_SHORT).show()
            return
        }

        !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
            Toast.makeText(context, "Please enter a valid email", Toast.LENGTH_SHORT).show()
            return
        }

        pass.isBlank() -> {
            Toast.makeText(context, "Please enter a password", Toast.LENGTH_SHORT).show()
            return
        }

        pass.length < 6 -> {
            Toast.makeText(context, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
            return
        }

        confirmPass != pass -> {
            Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        else -> {
            val firebaseAuth = FirebaseAuth.getInstance()
            firebaseAuth.createUserWithEmailAndPassword(email.trim(), pass)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, "Account created successfully", Toast.LENGTH_SHORT).show()
                        navController.navigate(Destinations.Dashboard.route) {
                            popUpTo(Destinations.SignUp.route) { inclusive = true }
                        }
                    } else {
                        Toast.makeText(
                            context,
                            task.exception?.message ?: "Sign-up failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }
}
