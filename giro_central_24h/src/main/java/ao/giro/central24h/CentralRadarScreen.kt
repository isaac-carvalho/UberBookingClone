package ao.giro.central24h

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ao.giro.core.*

@Composable
fun CentralRadarScreen(
    operatorCode: String,
    shift: String,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val drivers by GiroRealtimeHub.drivers.collectAsState()
    val activeRides by GiroRealtimeHub.activeRides.collectAsState()
    val panicAlerts by GiroRealtimeHub.panicAlerts.collectAsState()

    var selectedAlert by remember { mutableStateOf<PanicAlert?>(null) }

    // Abre automaticamente incidente SOS se houver alerta não resolvido
    LaunchedEffect(panicAlerts) {
        val unresolved = panicAlerts.firstOrNull { !it.resolved }
        if (unresolved != null && selectedAlert == null) {
            selectedAlert = unresolved
            GiroAudioAlert.playEmergencySiren()
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = UberBlack) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(16.dp)
        ) {
            // Header do Operador
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "RADAR TÁTICO 24H",
                        color = UberWhite,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = " \u00B7 ",
                        color = UberGreen,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                TextButton(onClick = onLogout) {
                    Text("Sair", color = UberEmergencyRed, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Contadores Táticos
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MetricCard(
                    title = "Condutores",
                    value = drivers.count { it.isOnline }.toString(),
                    sub = "🟢 Online",
                    color = UberGreen,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "Viagens",
                    value = activeRides.count { it.status != RideStatus.COMPLETED && it.status != RideStatus.CANCELLED }.toString(),
                    sub = "⚪ Ativas",
                    color = UberWhite,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "Alertas SOS",
                    value = panicAlerts.count { !it.resolved }.toString(),
                    sub = "🔴 Críticos",
                    color = if (panicAlerts.any { !it.resolved }) UberEmergencyRed else UberGrayText,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Mapa Tático Simulado de Luanda com Veículos Plotados
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                colors = CardDefaults.cardColors(containerColor = UberDarkCard),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, UberDarkBorder)
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "MAPA TÁTICO DE LUANDA 🇦🇴",
                            color = UberGrayText,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = "-8.83833, 13.23444 (Talatona \u00B7 Ilha \u00B7 Kilamba \u00B7 Viana)",
                            color = UberGreen,
                            fontSize = 10.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Plotagem dos condutores
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            drivers.forEach { d ->
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .background(
                                                if (panicAlerts.any { it.vehiclePlate == d.licensePlate && !it.resolved }) UberEmergencyRed
                                                else if (d.isOnline) UberGreen else UberDarkBorder,
                                                CircleShape
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            Icons.Default.DirectionsCar,
                                            contentDescription = null,
                                            tint = UberBlack,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                    Text(
                                        text = d.licensePlate,
                                        color = UberWhite,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(top = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Feed de Eventos Ao Vivo
            Text(
                text = "FEED OPERACIONAL AO VIVO",
                color = UberWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Alertas de Pânico prioritários no topo
                items(panicAlerts.filter { !it.resolved }) { alert ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedAlert = alert },
                        colors = CardDefaults.cardColors(containerColor = UberEmergencyRed.copy(alpha = 0.2f)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, UberEmergencyRed)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Warning, contentDescription = null, tint = UberEmergencyRed)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "SOS:  \u00B7 ",
                                    color = UberWhite,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = "Acionado por  \u00B7 Toque para despachar PNA (111)",
                                    color = UberEmergencyRed,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }

                // Eventos de corridas
                items(activeRides.reversed()) { ride ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = UberDarkCard),
                        border = androidx.compose.foundation.BorderStroke(1.dp, UberDarkBorder)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = " ➔ ",
                                    color = UberWhite,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Status:  \u00B7 ",
                                    color = UberGreen,
                                    fontSize = 11.sp
                                )
                            }
                            Text(
                                text = ride.totalFareKz.formatKz(),
                                color = UberWhite,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }

    // Modal de Incidente SOS
    selectedAlert?.let { alert ->
        CentralIncidentModal(
            alert = alert,
            onDismiss = { selectedAlert = null }
        )
    }
}

@Composable
fun MetricCard(
    title: String,
    value: String,
    sub: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = UberDarkCard),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, UberDarkBorder)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = title, color = UberGrayText, fontSize = 10.sp)
            Text(text = value, color = color, fontSize = 20.sp, fontWeight = FontWeight.Black)
            Text(text = sub, color = UberGrayText, fontSize = 9.sp)
        }
    }
}
