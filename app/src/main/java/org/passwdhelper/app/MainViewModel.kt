package org.passwdhelper.app

import android.content.ClipData
import android.content.ClipDescription
import android.content.ClipboardManager
import android.os.Build
import android.os.PersistableBundle
import android.util.Log
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    companion object {
        private const val TAG = "MainViewModel"
        private const val ENG = "`1234567890-=qwertyuiop[]\\asdfghjkl;'zxcvbnm,./~!@#$%^&*()_+QWERTYUIOP{}|ASDFGHJKL:\"ZXCVBNM<>?"
        private const val RUS = "ё1234567890-=йцукенгшщзхъ\\фывапролджэячсмитьбю.Ё!\"№;%:?*()_+ЙЦУКЕНГШЩЗХЪ/ФЫВАПРОЛДЖЭЯЧСМИТЬБЮ,"

        private val rusToEngMap: Map<Char, Char> = buildMap {
            val minLen = minOf(ENG.length, RUS.length)
            for (i in 0 until minLen) {
                put(RUS[i], ENG[i])
            }
        }
    }

    // Password state
    var password by mutableStateOf("")
        private set

    // Computed real-time converted password
    val transformedPassword: String by derivedStateOf {
        transform(password)
    }

    // Password visibility state
    var isPasswordVisible by mutableStateOf(false)
        private set

    // Toast message state (for pre-Android 13 devices)
    var toastMessage by mutableStateOf<String?>(null)
        private set

    // Copied feedback state
    var isJustCopied by mutableStateOf(false)
        private set

    fun updatePassword(newPassword: String) {
        password = newPassword
        isJustCopied = false
    }

    fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible
    }

    fun copyPasswordToClipboard(clipboardManager: ClipboardManager) {
        val textToCopy = transformedPassword
        if (textToCopy.isNotEmpty()) {
            val clipData = ClipData.newPlainText("password", textToCopy)

            // Hide sensitive password content from Android 13+ clipboard preview
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                clipData.description.extras = PersistableBundle().apply {
                    putBoolean(ClipDescription.EXTRA_IS_SENSITIVE, true)
                }
            }

            clipboardManager.setPrimaryClip(clipData)
            isJustCopied = true

            // Android 13+ shows built-in clipboard toast overlay; only show manual toast on earlier APIs
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                toastMessage = "Password copied to clipboard"
            }
            Log.d(TAG, "Password copied to clipboard securely")
        }
    }

    fun clearToastMessage() {
        toastMessage = null
    }

    fun clearPassword() {
        password = ""
        isJustCopied = false
        Log.d(TAG, "Cleared password field")
    }

    private fun transform(s: String): String {
        if (s.isEmpty()) return ""
        val sb = StringBuilder(s.length)
        for (ch in s) {
            sb.append(rusToEngMap[ch] ?: ch)
        }
        return sb.toString()
    }
}
