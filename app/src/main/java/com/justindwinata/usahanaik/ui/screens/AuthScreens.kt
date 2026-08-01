package com.justindwinata.usahanaik.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.justindwinata.usahanaik.domain.auth.AuthEntryValidator
import com.justindwinata.usahanaik.domain.localization.AppLanguage
import com.justindwinata.usahanaik.ui.components.LanguageSelector
import com.justindwinata.usahanaik.ui.components.PillBadge
import com.justindwinata.usahanaik.ui.components.PrimaryActionButton
import com.justindwinata.usahanaik.ui.components.UsahaNaikCard
import com.justindwinata.usahanaik.ui.localization.LocalAppStrings
import com.justindwinata.usahanaik.ui.theme.AppSpacing
import com.justindwinata.usahanaik.ui.theme.Error
import com.justindwinata.usahanaik.ui.theme.OnSurface
import com.justindwinata.usahanaik.ui.theme.OnSurfaceVariant
import com.justindwinata.usahanaik.ui.theme.OnSecondary
import com.justindwinata.usahanaik.ui.theme.Outline
import com.justindwinata.usahanaik.ui.theme.OutlineVariant
import com.justindwinata.usahanaik.ui.theme.Primary
import com.justindwinata.usahanaik.ui.theme.Secondary
import com.justindwinata.usahanaik.ui.theme.SecondaryContainer
import com.justindwinata.usahanaik.ui.theme.SurfaceContainerLowest
import com.justindwinata.usahanaik.ui.theme.SurfaceContainerLow
import com.justindwinata.usahanaik.ui.theme.AppRadius

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
    var passwordVisible by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = AppSpacing.containerMargin, vertical = AppSpacing.xl)
    ) {
        Spacer(modifier = Modifier.height(AppSpacing.xl))

        Text(
            text = "Welcome Back.",
            style = MaterialTheme.typography.displayLarge,
            color = OnSurface,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(AppSpacing.xs))
        Text(
            text = "Access your business intelligence suite.",
            style = MaterialTheme.typography.bodyLarge,
            color = OnSurfaceVariant
        )

        Spacer(modifier = Modifier.height(AppSpacing.xl))

        UsahaNaikCard(containerColor = SurfaceContainerLowest) {
            StitchInputField(
                value = email,
                onValueChange = { email = it },
                label = strings.email.uppercase(),
                keyboardType = KeyboardType.Email,
                singleLine = true
            )
            Spacer(modifier = Modifier.height(AppSpacing.md))
            StitchPasswordField(
                value = password,
                onValueChange = { password = it },
                label = strings.password.uppercase(),
                isVisible = passwordVisible,
                onVisibilityChange = { passwordVisible = it }
            )
            message?.let {
                Spacer(modifier = Modifier.height(AppSpacing.sm))
                Text(text = it, style = MaterialTheme.typography.bodySmall, color = Error)
            }
            Spacer(modifier = Modifier.height(AppSpacing.lg))
            PrimaryActionButton(
                text = strings.login,
                onClick = {
                    focusManager.clearFocus()
                    val result = AuthEntryValidator.validateLogin(email, password)
                    message = if (result.isValid) strings.authDemoNote else result.message
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(AppSpacing.md))
            OutlinedButton(
                onClick = onContinueLocalMode,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(strings.continueLocalMode)
            }
        }

        Spacer(modifier = Modifier.height(AppSpacing.md))

        AuthDivider(text = "Or continue with")

        Spacer(modifier = Modifier.height(AppSpacing.md))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.md)
        ) {
            SsoButton(
                text = "Google",
                icon = "g",
                modifier = Modifier.weight(1f),
                onClick = { }
            )
            SsoButton(
                text = "Apple",
                icon = "apple_logo",
                modifier = Modifier.weight(1f),
                onClick = { }
            )
        }

        Spacer(modifier = Modifier.height(AppSpacing.lg))
        AuthSwitchCard(
            message = "Don't have an account?",
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
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = AppSpacing.containerMargin, vertical = AppSpacing.xl)
    ) {
        Spacer(modifier = Modifier.height(AppSpacing.xl))

        Text(
            text = "Create Your Account.",
            style = MaterialTheme.typography.displayLarge,
            color = OnSurface,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(AppSpacing.xs))
        Text(
            text = "Join thousands of entrepreneurs scaling with precision.",
            style = MaterialTheme.typography.bodyLarge,
            color = OnSurfaceVariant
        )

        Spacer(modifier = Modifier.height(AppSpacing.xl))

        UsahaNaikCard(containerColor = SurfaceContainerLowest) {
            StitchInputField(
                value = name,
                onValueChange = { name = it },
                label = strings.ownerName.uppercase(),
                keyboardType = KeyboardType.Text,
                singleLine = true
            )
            Spacer(modifier = Modifier.height(AppSpacing.md))
            StitchInputField(
                value = email,
                onValueChange = { email = it },
                label = strings.email.uppercase(),
                keyboardType = KeyboardType.Email,
                singleLine = true
            )
            Spacer(modifier = Modifier.height(AppSpacing.md))
            StitchPasswordField(
                value = password,
                onValueChange = { password = it },
                label = strings.password.uppercase(),
                isVisible = passwordVisible,
                onVisibilityChange = { passwordVisible = it }
            )
            Spacer(modifier = Modifier.height(AppSpacing.md))
            StitchPasswordField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = strings.confirmPassword.uppercase(),
                isVisible = confirmPasswordVisible,
                onVisibilityChange = { confirmPasswordVisible = it }
            )
            message?.let {
                Spacer(modifier = Modifier.height(AppSpacing.sm))
                Text(text = it, style = MaterialTheme.typography.bodySmall, color = Error)
            }
            Spacer(modifier = Modifier.height(AppSpacing.lg))
            PrimaryActionButton(
                text = strings.register,
                onClick = {
                    focusManager.clearFocus()
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
            Spacer(modifier = Modifier.height(AppSpacing.md))
            OutlinedButton(
                onClick = onContinueLocalMode,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(strings.continueLocalMode)
            }
        }

        Spacer(modifier = Modifier.height(AppSpacing.md))

        AuthDivider(text = "Or continue with")

        Spacer(modifier = Modifier.height(AppSpacing.md))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.md)
        ) {
            SsoButton(
                text = "Google",
                icon = "g",
                modifier = Modifier.weight(1f),
                onClick = { }
            )
            SsoButton(
                text = "Apple",
                icon = "apple_logo",
                modifier = Modifier.weight(1f),
                onClick = { }
            )
        }

        Spacer(modifier = Modifier.height(AppSpacing.lg))
        AuthSwitchCard(
            message = "Already registered?",
            action = strings.login,
            onClick = onLoginClick
        )
    }
}

@Composable
private fun HeaderBrand() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AppSpacing.md)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(SurfaceContainerLow),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "UN",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                color = OnSurface
            )
        }
        Text(
            "UsahaNaik",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
            color = OnSurface
        )
    }
}

@Composable
private fun StitchInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = false
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = OnSurfaceVariant
        )
        Spacer(modifier = Modifier.height(AppSpacing.xs))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            singleLine = singleLine,
            shape = RoundedCornerShape(8.dp),
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = OnSurface),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Secondary,
                unfocusedBorderColor = OutlineVariant,
                focusedContainerColor = SurfaceContainerLowest,
                unfocusedContainerColor = SurfaceContainerLowest,
                cursorColor = Secondary
            )
        )
    }
}

@Composable
private fun StitchPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isVisible: Boolean,
    onVisibilityChange: (Boolean) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = OnSurfaceVariant
        )
        Spacer(modifier = Modifier.height(AppSpacing.xs))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(SurfaceContainerLowest, RoundedCornerShape(8.dp))
                .border(1.dp, OutlineVariant, RoundedCornerShape(8.dp))
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 48.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = OnSurface),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Secondary,
                    unfocusedBorderColor = OutlineVariant,
                    focusedContainerColor = SurfaceContainerLowest,
                    unfocusedContainerColor = SurfaceContainerLowest,
                    cursorColor = Secondary
                )
            )
            IconButton(
                onClick = { onVisibilityChange(!isVisible) },
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(
                    imageVector = if (isVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                    contentDescription = if (isVisible) "Hide password" else "Show password",
                    tint = Outline
                )
            }
        }
    }
}

@Composable
private fun AuthDivider(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(OutlineVariant)
        )
        Spacer(modifier = Modifier.padding(horizontal = AppSpacing.md))
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = OnSurfaceVariant
        )
        Spacer(modifier = Modifier.padding(horizontal = AppSpacing.md))
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(OutlineVariant)
        )
    }
}

@Composable
private fun SsoButton(
    text: String,
    icon: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(start = AppSpacing.xs)
        )
    }
}

@Composable
private fun AuthSwitchCard(
    message: String,
    action: String,
    onClick: () -> Unit
) {
    UsahaNaikCard(containerColor = SurfaceContainerLowest) {
        Text(text = message, style = MaterialTheme.typography.bodyMedium, color = OnSurfaceVariant)
        Spacer(modifier = Modifier.height(AppSpacing.sm))
        TextButton(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
            Text(action, color = Secondary, style = MaterialTheme.typography.bodyMedium)
        }
    }
}