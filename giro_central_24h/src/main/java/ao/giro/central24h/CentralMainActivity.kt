package ao.giro.central24h

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import ao.giro.core.GiroTheme

class CentralMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GiroTheme {
                var isAuthenticated by remember { mutableStateOf(false) }
                var operatorCode by remember { mutableStateOf("OP-LUANDA-07") }
                var shift by remember { mutableStateOf("DIURNO") }

                if (!isAuthenticated) {
                    CentralAuthScreen(
                        onLoginSuccess = { code, opShift ->
                            operatorCode = code
                            shift = opShift
                            isAuthenticated = true
                        }
                    )
                } else {
                    CentralRadarScreen(
                        operatorCode = operatorCode,
                        shift = shift,
                        onLogout = { isAuthenticated = false }
                    )
                }
            }
        }
    }
}
