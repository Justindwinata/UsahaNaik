package com.justindwinata.usahanaik.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.justindwinata.usahanaik.domain.auth.AuthEntryValidator
import com.justindwinata.usahanaik.domain.localization.AppLanguage
import com.justindwinata.usahanaik.ui.components.LanguageSelector
import com.justindwinata.usahanaik.ui.components.PillBadge
import com.justindwinata.usahanaik.ui.components.PrimaryActionButton
import com.justindwinata.usahanaik.ui.components.UsahaNaikCard
import com.justindwinata.usahanaik.ui.localization.LocalAppStrings
import com.justindwinata.usahanaik.ui.theme.AppSpacing
import com.justindwinata.usahanaik.ui.theme.BlueSoft
import com.justindwinata.usahanaik.ui.theme.CoralPrimary
import com.justindwinata.usahanaik.ui.theme.CoralSoft
import com.justindwinata.usahanaik.ui.theme.GreenPositive
import com.justindwinata.usahanaik.ui.theme.GreenSoft
import com.justindwinata.usahanaik.ui.theme.InkMuted

@Composable
fun LoginScreen(
    selectedLanguage: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
    onContinueLocalMode: () -> Unit,
    onRegisterClick: () -> Unit
) {
    val strings = LocalAppStrings.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var message by remember { mutableStateOf<String?>(null) }

    AuthScreenFrame(
        title = strings.login,
        subtitle = strings.authDemoNote,
        selectedLanguage = selectedLanguage,
        onLanguageSelected = onLanguageSelected
    ) {
        UsahaNaikCard(containerColor = BlueSoft) {
            AuthTextField(
                value = email,
                onValueChange = { email = it },
                label = strings.email,
                keyboardType = KeyboardType.Email
            )
            Spacer(modifier = Modifier.height(AppSpacing.sm))
            AuthTextField(
                value = password,
                onValueChange = { password = it },
                label = strings.password,
                keyboardType = KeyboardType.Password,
                isPassword = true
            )
            message?.let {
                Spacer(modifier = Modifier.height(AppSpacing.sm))
                Text(text = it, style = MaterialTheme.typography.bodySmall, color = CoralPrimary)
            }
            Spacer(modifier = Modifier.height(AppSpacing.md))
            PrimaryActionButton(
                text = strings.login,
                onClick = {
                    val result = AuthEntryValidator.validateLogin(email, password)
                    message = if (result.isValid) strings.authDemoNote else result.message
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(AppSpacing.sm))
            OutlinedButton(onClick = onContinueLocalMode, modifier = Modifier.fillMaxWidth()) {
                Text(strings.continueLocalMode)
            }
        }
        Spacer(modifier = Modifier.height(AppSpacing.md))
        AuthSwitchCard(
            message = "Belum punya akun? / New here?",
            action = strings.register,
            onClick = onRegisterClick
        )
    }
}

@Composable
fun RegisterScreen(
    selectedLanguage: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
    onContinueLocalMode: () -> Unit,
    onLoginClick: () -> Unit
) {
    val strings = LocalAppStrings.current
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var message by remember { mutableStateOf<String?>(null) }

    AuthScreenFrame(
        title = strings.register,
        subtitle = strings.authDemoNote,
        selectedLanguage = selectedLanguage,
        onLanguageSelected = onLanguageSelected
    ) {
        UsahaNaikCard(containerColor = BlueSoft) {
            AuthTextField(value = name, onValueChange = { name = it }, label = strings.ownerName)
            Spacer(modifier = Modifier.height(AppSpacing.sm))
            AuthTextField(
                value = email,
                onValueChange = { email = it },
                label = strings.email,
                keyboardType = KeyboardType.Email
            )
            Spacer(modifier = Modifier.height(AppSpacing.sm))
            AuthTextField(
                value = password,
                onValueChange = { password = it },
                label = strings.password,
                keyboardType = KeyboardType.Password,
                isPassword = true
            )
            Spacer(modifier = Modifier.height(AppSpacing.sm))
            AuthTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = strings.confirmPassword,
                keyboardType = KeyboardType.Password,
                isPassword = true
            )
            message?.let {
                Spacer(modifier = Modifier.height(AppSpacing.sm))
                Text(text = it, style = MaterialTheme.typography.bodySmall, color = CoralPrimary)
            }
            Spacer(modifier = Modifier.height(AppSpacing.md))
            PrimaryActionButton(
                text = strings.register,
                onClick = {
                    val result = AuthEntryValidator.validateRegister(
                        name = name,
                        email = email,
                        password = password,
                        confirmPassword = confirmPassword
                    )
                    message = if (result.isValid) strings.authDemoNote else result.message
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(AppSpacing.sm))
            OutlinedButton(onClick = onContinueLocalMode, modifier = Modifier.fillMaxWidth()) {
                Text(strings.continueLocalMode)
            }
        }
        Spacer(modifier = Modifier.height(AppSpacing.md))
        AuthSwitchCard(
            message = "Sudah punya akun? / Already registered?",
            action = strings.login,
            onClick = onLoginClick
        )
    }
}

@Composable
private fun AuthScreenFrame(
    title: String,
    subtitle: String,
    selectedLanguage: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(AppSpacing.lg),
        verticalArrangement = Arrangement.spacedBy(AppSpacing.md)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(AppSpacing.md)) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(CoralSoft),
                contentAlignment = Alignment.Center
            ) {
                Text("UN", color = CoralPrimary, fontWeight = FontWeight.Bold)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text("UsahaNaik", style = MaterialTheme.typography.titleLarge)
                Text(title, style = MaterialTheme.typography.bodyMedium, color = InkMuted)
            }
        }
        LanguageSelector(
            selectedLanguage = selectedLanguage,
            onLanguageSelected = onLanguageSelected
        )
        PillBadge(text = "Local-first demo", containerColor = GreenSoft, contentColor = GreenPositive)
        Text(text = subtitle, style = MaterialTheme.typography.bodyMedium, color = InkMuted)
        content()
    }
}

@Composable
private fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
        singleLine = true
    )
}

@Composable
private fun AuthSwitchCard(
    message: String,
    action: String,
    onClick: () -> Unit
) {
    UsahaNaikCard(containerColor = GreenSoft) {
        Text(text = message, style = MaterialTheme.typography.bodyMedium, color = InkMuted)
        Spacer(modifier = Modifier.height(AppSpacing.sm))
        OutlinedButton(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
            Text(action)
        }
    }
}
