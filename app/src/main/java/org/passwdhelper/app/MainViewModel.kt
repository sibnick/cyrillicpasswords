package org.passwdhelper.app

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    companion object {
        private const val TAG = "MainViewModel"
        private const val ENG = "`1234567890-=qwertyuiop[]\\asdfghjkl;'zxcvbnm,./~!@#$%^&*()_+QWERTYUIOP{}|ASDFGHJKL:\"ZXCVBNM<>?"
        private const val RUS = "ё1234567890-=йцукенгшщзхъ\\фывапролджэячсмитьбю.Ё!\"№;%:?*()_+ЙЦУКЕНГШЩЗХЪ/ФЫВАПРОЛДЖЭЯЧСМИТЬБЮ,"
        private val map = HashMap<Char, Char>().apply {
            for (i in ENG.indices) {
                val eng = ENG[i]
                val rus = RUS[i]
                put(rus, eng)
            }
        }
    }

    // Password state
    var password by mutableStateOf("")
        private set

    // Password visibility state
    var isPasswordVisible by mutableStateOf(false)
        private set

    // Toast message state
    var toastMessage by mutableStateOf<String?>(null)
        private set

    fun updatePassword(newPassword: String) {
        password = newPassword
    }

    fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible
    }

    @RequiresApi(Build.VERSION_CODES.HONEYCOMB)
    fun copyPasswordToClipboard(clipboardManager: ClipboardManager) {
        if (password.isNotEmpty()) {
            val transformedPassword = transform(password)
            val clipData = ClipData.newPlainText("password", transformedPassword)
            clipboardManager.setPrimaryClip(clipData)
            toastMessage = "Password copied to clipboard"
            Log.d(TAG, "Password copied to clipboard")
        }
    }

    fun clearToastMessage() {
        toastMessage = null
    }

    fun clearPassword() {
        password = ""
        Log.d(TAG, "Remove password from field")
    }

    private fun transform(s: String): CharSequence {
        val sb = StringBuilder()
        for (i in s.indices) {
            val cur = s[i]
            val transformed = map[cur]
            if (transformed == null) {
                sb.append(cur)
            } else {
                sb.append(transformed)
            }
        }
        return sb.toString()
    }
}
