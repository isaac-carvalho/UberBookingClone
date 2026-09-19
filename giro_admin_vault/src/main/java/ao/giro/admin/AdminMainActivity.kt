package ao.giro.admin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import ao.giro.core.GiroTheme

class AdminMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GiroTheme {
                var isVaultUnlocked by remember { mutableStateOf(false) }

                if (!isVaultUnlocked) {
                    AdminVaultAuthScreen(
                        onUnlockSuccess = { isVaultUnlocked = true }
                    )
                } else {
                    AdminDashboardScreen(
                        onLogout = { isVaultUnlocked = false }
                    )
                }
            }
        }
    }
}
