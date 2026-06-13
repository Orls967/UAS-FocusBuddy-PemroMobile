package com.example.focusbuddyapp.presentation.auth.register

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.focusbuddyapp.ui.theme.*

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) onRegisterSuccess()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(48.dp))

        Text("Create Account", style = MaterialTheme.typography.headlineLarge, color = PrimaryText)
        Spacer(Modifier.height(6.dp))
        Text("Join thousands of scholars achieving more.", style = MaterialTheme.typography.bodyMedium, color = SecondaryText, textAlign = TextAlign.Center)

        Spacer(Modifier.height(32.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(Modifier.padding(24.dp)) {
                listOf(
                    Triple("Full Name", uiState.name, viewModel::onNameChange),
                    Triple("Email", uiState.email, viewModel::onEmailChange),
                ).forEach { (label, value, onChange) ->
                    Text(label, style = MaterialTheme.typography.labelLarge, color = PrimaryText)
                    Spacer(Modifier.height(6.dp))
                    OutlinedTextField(
                        value = value, onValueChange = onChange,
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryNavy, unfocusedBorderColor = OutlineVariant,
                            focusedContainerColor = WarmBeige.copy(alpha = 0.4f),
                            unfocusedContainerColor = WarmBeige.copy(alpha = 0.4f)
                        )
                    )
                    Spacer(Modifier.height(16.dp))
                }

                // Password
                Text("Password", style = MaterialTheme.typography.labelLarge, color = PrimaryText)
                Spacer(Modifier.height(6.dp))
                OutlinedTextField(
                    value = uiState.password, onValueChange = viewModel::onPasswordChange,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation = if (uiState.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = viewModel::togglePasswordVisibility) {
                            Icon(if (uiState.isPasswordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility, null, tint = SecondaryText)
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryNavy, unfocusedBorderColor = OutlineVariant,
                        focusedContainerColor = WarmBeige.copy(alpha = 0.4f), unfocusedContainerColor = WarmBeige.copy(alpha = 0.4f)
                    )
                )
                Spacer(Modifier.height(16.dp))

                // Confirm Password
                Text("Confirm Password", style = MaterialTheme.typography.labelLarge, color = PrimaryText)
                Spacer(Modifier.height(6.dp))
                OutlinedTextField(
                    value = uiState.confirmPassword, onValueChange = viewModel::onConfirmPasswordChange,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryNavy, unfocusedBorderColor = OutlineVariant,
                        focusedContainerColor = WarmBeige.copy(alpha = 0.4f), unfocusedContainerColor = WarmBeige.copy(alpha = 0.4f)
                    )
                )

                if (uiState.errorMessage != null) {
                    Spacer(Modifier.height(8.dp))
                    Text(uiState.errorMessage!!, color = ErrorRed, style = MaterialTheme.typography.bodySmall)
                }

                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = viewModel::register,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    enabled = !uiState.isLoading,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy)
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                    } else {
                        Text("Sign Up", style = MaterialTheme.typography.titleMedium, color = Color.White)
                    }
                }

                Spacer(Modifier.height(12.dp))

                TextButton(onClick = onNavigateToLogin, modifier = Modifier.fillMaxWidth()) {
                    Text("Already have an account? ", color = SecondaryText, style = MaterialTheme.typography.bodyMedium)
                    Text("Login", color = PrimaryTerracotta, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
        Spacer(Modifier.height(32.dp))
    }
}
