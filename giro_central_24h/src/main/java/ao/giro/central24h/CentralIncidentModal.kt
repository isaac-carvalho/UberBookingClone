package ao.giro.central24h

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ao.giro.core.*

@Composable
fun CentralIncidentModal(
    alert: PanicAlert,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var isPnaDispatched by remember { mutableStateOf(alert.pnaDispatched) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = UberDarkCard,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Warning, contentDescription = null, tint = UberEmergencyRed, modifier = Modifier.size(28.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("ALERTA CRÍTICO SOS PÂNICO", color = UberEmergencyRed, fontWeight = FontWeight.Black, fontSize = 18.sp)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Acionado por:  \u00B7 ",
                    color = UberWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Text(text = "Contacto: ", color = UberGrayText, fontSize = 13.sp)
                Text(text = "Viatura:  \u00B7 Matrícula: ", color = UberWhite, fontSize = 13.sp)
                Text(text = "Coordenadas GPS: ,  (Luanda)", color = UberGreen, fontSize = 12.sp)

                if (isPnaDispatched) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(UberEmergencyRed.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                            .border(1.dp, UberEmergencyRed, RoundedCornerShape(8.dp))
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "🚨 PATRULHA PNA (111) DESPACHADA PARA O LOCAL",
                            color = UberWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        },
        confirmButton = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        isPnaDispatched = true
                        GiroRealtimeHub.dispatchPna(alert.id)
                        Toast.makeText(context, "Polícia Nacional de Angola (111) Notificada com Sucesso!", Toast.LENGTH_LONG).show()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = UberEmergencyRed, contentColor = UberWhite)
                ) {
                    Text("🚨 ACIONAR PNA 111 (POLÍCIA)", fontWeight = FontWeight.Black)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            Toast.makeText(context, "A ligar para ...", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = UberWhite)
                    ) {
                        Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Contactar", fontSize = 11.sp)
                    }

                    Button(
                        onClick = {
                            GiroRealtimeHub.resolvePanicAlert(alert.id)
                            Toast.makeText(context, "Incidente marcado como resolvido.", Toast.LENGTH_SHORT).show()
                            onDismiss()
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = UberGreen, contentColor = UberBlack)
                    ) {
                        Text("Resolver", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Minimizar", color = UberGrayText)
            }
        }
    )
}
