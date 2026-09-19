package com.example.uberbookingexperience.ui.screens.activeRide

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ao.giro.core.*
import kotlinx.coroutines.delay

@Composable
fun PassengerActiveRideScreen(
    rideId: String,
    onFinishRide: () -> Unit
) {
    val context = LocalContext.current
    val activeRides by GiroRealtimeHub.activeRides.collectAsState()
    val currentRide = activeRides.find { it.id == rideId } ?: activeRides.lastOrNull()
    var sosTriggered by remember { mutableStateOf(false) }

    // Auto-dispatch simulador se nenhum motorista aceitou após 4 segundos (para testes rápidos)
    LaunchedEffect(currentRide?.status) {
        if (currentRide != null && currentRide.status == RideStatus.SEARCHING) {
            delay(4000)
            val driver = GiroRealtimeHub.drivers.value.firstOrNull { it.isOnline } ?: DriverPartner()
            GiroRealtimeHub.acceptRide(currentRide.id, driver)
        }
    }

    BackHandler {
        onFinishRide()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = UberBlack
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Bar com SOS
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onFinishRide,
                    modifier = Modifier.background(UberDarkCard, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Fechar",
                        tint = UberWhite
                    )
                }

                // Botão de Pânico SOS obrigatório
                GiroSosButton(
                    onSosClick = {
                        sosTriggered = true
                        currentRide?.let { r ->
                            GiroRealtimeHub.triggerPanicAlert(
                                rideId = r.id,
                                triggeredBy = "PASSAGEIRO",
                                initiatorName = r.passengerName,
                                initiatorPhone = r.passengerPhone,
                                vehiclePlate = r.assignedDriver?.licensePlate ?: "LD-45-89-GH",
                                vehicleModel = r.assignedDriver?.vehicleModel ?: "Toyota Corolla"
                            )
                        }
                        Toast.makeText(context, "ALERTA SOS ENVIADO À CENTRAL 24H!", Toast.LENGTH_LONG).show()
                    }
                )
            }

            // Radar e Status da Viagem
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val infiniteTransition = rememberInfiniteTransition()
                val radarScale by infiniteTransition.animateFloat(
                    initialValue = 0.95f,
                    targetValue = 1.15f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1000),
                        repeatMode = RepeatMode.Reverse
                    )
                )

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(130.dp)
                        .scale(radarScale)
                        .background(
                            if (sosTriggered) UberEmergencyRed.copy(alpha = 0.2f)
                            else UberGreen.copy(alpha = 0.15f),
                            CircleShape
                        )
                        .border(
                            2.dp,
                            if (sosTriggered) UberEmergencyRed else UberGreen,
                            CircleShape
                        )
                ) {
                    Icon(
                        imageVector = if (sosTriggered) Icons.Default.Warning else Icons.Default.DirectionsCar,
                        contentDescription = "Carro",
                        tint = if (sosTriggered) UberEmergencyRed else UberGreen,
                        modifier = Modifier.size(54.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                val statusTitle = when (currentRide?.status) {
                    RideStatus.SEARCHING -> "A procurar condutor próximo..."
                    RideStatus.DISPATCHED -> "Chamada enviada ao condutor..."
                    RideStatus.ACCEPTED -> "Motorista a caminho (4 min)"
                    RideStatus.ARRIVED_PICKUP -> "Motorista chegou ao ponto de recolha!"
                    RideStatus.IN_PROGRESS -> "Viagem em curso..."
                    RideStatus.COMPLETED -> "Viagem concluída!"
                    else -> "Viagem confirmada"
                }

                Text(
                    text = statusTitle,
                    color = if (sosTriggered) UberEmergencyRed else UberWhite,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "${currentRide?.origin?.name ?: "Ilha de Luanda"} ➔ ${currentRide?.destination?.name ?: "Talatona"}",
                    color = UberGrayText,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 4.dp),
                    textAlign = TextAlign.Center
                )

                if (sosTriggered) {
                    Card(
                        modifier = Modifier.padding(top = 12.dp),
                        colors = CardDefaults.cardColors(containerColor = UberEmergencyRed)
                    ) {
                        Text(
                            text = "CENTRAL 24H ACIONADA \u00B7 PATRULHA PNA 111 A SER NOTIFICADA",
                            color = UberWhite,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }

            // Card com dados do Motorista e Veículo
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = UberDarkCard),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, UberDarkBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = currentRide?.assignedDriver?.name ?: "Mateus Domingos",
                                color = UberWhite,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(top = 2.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Rating",
                                    tint = Color(0xFFFFC107),
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = " ${currentRide?.assignedDriver?.rating ?: 4.98} \u00B7 ${currentRide?.assignedDriver?.totalTrips ?: 1420} viagens",
                                    color = UberGrayText,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        // Matrícula Luanda destacada
                        Box(
                            modifier = Modifier
                                .background(UberWhite, RoundedCornerShape(6.dp))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = currentRide?.assignedDriver?.licensePlate ?: "LD-45-89-GH",
                                color = UberBlack,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }

                    Divider(
                        color = UberDarkBorder,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "Veículo", color = UberGrayText, fontSize = 12.sp)
                            Text(
                                text = currentRide?.assignedDriver?.vehicleModel ?: "Toyota Corolla",
                                color = UberWhite,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(text = "Tarifa Estimada", color = UberGrayText, fontSize = 12.sp)
                            Text(
                                text = (currentRide?.totalFareKz ?: 2800.0).formatKz(),
                                color = UberGreen,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Botões de contato rápido
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                Toast.makeText(context, "A ligar para o motorista...", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = UberWhite)
                        ) {
                            Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Contactar")
                        }

                        Button(
                            onClick = {
                                // Simula avanço de status para teste
                                when (currentRide?.status) {
                                    RideStatus.ACCEPTED -> currentRide.id.let { GiroRealtimeHub.updateRideStatus(it, RideStatus.ARRIVED_PICKUP) }
                                    RideStatus.ARRIVED_PICKUP -> currentRide.id.let { GiroRealtimeHub.updateRideStatus(it, RideStatus.IN_PROGRESS) }
                                    RideStatus.IN_PROGRESS -> {
                                        currentRide.id.let { GiroRealtimeHub.updateRideStatus(it, RideStatus.COMPLETED) }
                                        Toast.makeText(context, "Viagem finalizada com sucesso!", Toast.LENGTH_SHORT).show()
                                        onFinishRide()
                                    }
                                    else -> onFinishRide()
                                }
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = UberWhite, contentColor = UberBlack)
                        ) {
                            Text(
                                text = when (currentRide?.status) {
                                    RideStatus.ACCEPTED -> "Chegou"
                                    RideStatus.ARRIVED_PICKUP -> "Iniciar"
                                    RideStatus.IN_PROGRESS -> "Concluir"
                                    else -> "Voltar"
                                },
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
