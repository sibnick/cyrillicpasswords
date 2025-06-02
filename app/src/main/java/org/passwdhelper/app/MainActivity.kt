package org.passwdhelper.app

import android.content.ClipboardManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import org.passwdhelper.app.ui.theme.CyrillicPasswordsTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CyrillicPasswordsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val clipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager

                    // Observe toast messages
                    viewModel.toastMessage?.let { message ->
                        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                        viewModel.clearToastMessage()
                    }

                    // Clear password when activity is stopped
                    DisposableEffect(Unit) {
                        onDispose {
                            viewModel.clearPassword()
                        }
                    }

                    PasswordScreen(
                        password = viewModel.password,
                        isPasswordVisible = viewModel.isPasswordVisible,
                        onPasswordChange = { viewModel.updatePassword(it) },
                        onTogglePasswordVisibility = { viewModel.togglePasswordVisibility() },
                        onSubmit = { viewModel.copyPasswordToClipboard(clipboardManager) }
                    )
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // State is automatically saved by the ViewModel
    }

    override fun onPause() {
        super.onPause()
        // Clear password when app goes to background
        viewModel.clearPassword()
    }
}

@Composable
fun PasswordScreen(
    password: String,
    isPasswordVisible: Boolean,
    onPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text("Password") },
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isPasswordVisible,
                onCheckedChange = { onTogglePasswordVisibility() }
            )
            Text(text = if (isPasswordVisible) "Hide Password" else "Show Password")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onSubmit,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Copy Password")
        }
    }

    // Request focus on the password field
    LaunchedEffect(isPasswordVisible) {
        focusRequester.requestFocus()
    }
}


