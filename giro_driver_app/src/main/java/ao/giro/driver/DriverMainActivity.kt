package ao.giro.driver

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import ao.giro.core.GiroTheme

class DriverMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GiroTheme {
                var isAuthenticated by remember { mutableStateOf(false) }
                var driverName by remember { mutableStateOf("Mateus Domingos") }
                var driverPhone by remember { mutableStateOf("+244 923 884 192") }
                var vehicleModel by remember { mutableStateOf("Toyota Corolla (Branco)") }
                var licensePlate by remember { mutableStateOf("LD-45-89-GH") }

                if (!isAuthenticated) {
                    DriverAuthScreen(
                        onAuthSuccess = { name, phone, vehicle, plate ->
                            driverName = name
                            driverPhone = phone
                            vehicleModel = vehicle
                            licensePlate = plate
                            isAuthenticated = true
                        }
                    )
                } else {
                    DriverDashboardScreen(
                        driverName = driverName,
                        driverPhone = driverPhone,
                        vehicleModel = vehicleModel,
                        licensePlate = licensePlate,
                        onLogout = { isAuthenticated = false }
                    )
                }
            }
        }
    }
}
