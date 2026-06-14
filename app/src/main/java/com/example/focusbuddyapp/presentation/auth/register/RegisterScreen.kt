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
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(48.dp))

        Text("Create Account", style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.onBackground)
        Spacer(Modifier.height(6.dp))
        Text("Join thousands of scholars achieving more.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)

        Spacer(Modifier.height(32.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(Modifier.padding(24.dp)) {
                listOf(
                    Triple("Full Name", uiState.name, viewModel::onNameChange),
                    Triple("Email", uiState.email, viewModel::onEmailChange),
                ).forEach { (label, value, onChange) ->
                    Text(label, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onBackground)
                    Spacer(Modifier.height(6.dp))
                    OutlinedTextField(
                        value = value, onValueChange = onChange,
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryNavy, unfocusedBorderColor = OutlineVariant,
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                        )
                    )
                    Spacer(Modifier.height(16.dp))
                }

                // Password
                Text("Password", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onBackground)
                Spacer(Modifier.height(6.dp))
                OutlinedTextField(
                    value = uiState.password, onValueChange = viewModel::onPasswordChange,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation = if (uiState.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = viewModel::togglePasswordVisibility) {
                            Icon(if (uiState.isPasswordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility, null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryNavy, unfocusedBorderColor = OutlineVariant,
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f), unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                    )
                )
                Spacer(Modifier.height(16.dp))

                // Confirm Password
                Text("Confirm Password", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onBackground)
                Spacer(Modifier.height(6.dp))
                OutlinedTextField(
                    value = uiState.confirmPassword, onValueChange = viewModel::onConfirmPasswordChange,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                    keyboardActions = androidx.compose.foundation.text.KeyboardActions(onDone = { viewModel.register() }),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryNavy, unfocusedBorderColor = OutlineVariant,
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f), unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                    )
                )

                Spacer(Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth().clickable(enabled = uiState.isPrivacyPolicyViewed) { 
                        if (uiState.isPrivacyPolicyViewed) viewModel.onPrivacyPolicyAcceptChange(!uiState.isPrivacyPolicyAccepted) 
                    },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = uiState.isPrivacyPolicyAccepted,
                        onCheckedChange = viewModel::onPrivacyPolicyAcceptChange,
                        enabled = uiState.isPrivacyPolicyViewed,
                        colors = CheckboxDefaults.colors(checkedColor = PrimaryNavy)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("I agree to the ", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onBackground)
                    Text(
                        "Privacy Policy",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold),
                        color = PrimaryNavy,
                        modifier = Modifier.clickable { viewModel.showPrivacyPolicyDialog() }
                    )
                }

                if (uiState.showPrivacyPolicyDialog) {
                    com.example.focusbuddyapp.ui.components.PrivacyPolicyDialog(
                        onDismissRequest = {
                            viewModel.hidePrivacyPolicyDialog()
                            viewModel.onPrivacyPolicyUnderstood()
                        }
                    )
                }

                if (uiState.errorMessage != null) {
                    Spacer(Modifier.height(8.dp))
                    Text(uiState.errorMessage!!, color = ErrorRed, style = MaterialTheme.typography.bodySmall)
                }

                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = viewModel::register,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    enabled = !uiState.isLoading && uiState.isPrivacyPolicyViewed && uiState.isPrivacyPolicyAccepted,
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
                    Text("Already have an account? ", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
                    Text("Login", color = PrimaryTerracotta, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
        Spacer(Modifier.height(32.dp))
    }
}
