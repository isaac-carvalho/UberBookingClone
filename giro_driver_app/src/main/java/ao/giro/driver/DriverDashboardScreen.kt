package ao.giro.driver

import android.widget.Toast
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ao.giro.core.*
import kotlinx.coroutines.delay

@Composable
fun DriverDashboardScreen(
    driverName: String,
    driverPhone: String,
    vehicleModel: String,
    licensePlate: String,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    var isOnline by remember { mutableStateOf(true) }
    var activeTrip by remember { mutableStateOf<RideOrder?>(null) }
    var pendingDispatch by remember { mutableStateOf<RideOrder?>(null) }
    var countdownSeconds by remember { mutableStateOf(30) }
    var showWithdrawalDialog by remember { mutableStateOf(false) }
    var withdrawalAmount by remember { mutableStateOf("25000") }

    val activeRides by GiroRealtimeHub.activeRides.collectAsState()

    // Ouve corridas solicitadas em Luanda
    LaunchedEffect(activeRides, isOnline) {
        if (isOnline && activeTrip == null) {
            val searchingOrder = activeRides.find { it.status == RideStatus.SEARCHING }
            if (searchingOrder != null && pendingDispatch == null) {
                pendingDispatch = searchingOrder
                countdownSeconds = 30
            }
        }
    }

    // Contagem decrescente de 30s
    LaunchedEffect(pendingDispatch) {
        if (pendingDispatch != null) {
            while (countdownSeconds > 0 && pendingDispatch != null) {
                GiroAudioAlert.playCountdownBeep()
                delay(1000)
                countdownSeconds--
            }
            if (countdownSeconds <= 0) {
                pendingDispatch = null
            }
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = UberBlack) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Extrato e SOS
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
                                text = "Hoje: Kz 38.500",
                                color = UberWhite,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = "Você retém 90% líquido \u00B7 Taxa GIRO 10%",
                                color = UberGreen,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        GiroSosButton(
                            onSosClick = {
                                GiroRealtimeHub.triggerPanicAlert(
                                    rideId = activeTrip?.id ?: "SOS-DRV",
                                    triggeredBy = "MOTORISTA",
                                    initiatorName = driverName,
                                    initiatorPhone = driverPhone,
                                    vehiclePlate = licensePlate,
                                    vehicleModel = vehicleModel
                                )
                                Toast.makeText(context, "SOS CONDUTOR ENVIADO À CENTRAL 24H!", Toast.LENGTH_LONG).show()
                            }
                        )
                    }

                    Divider(color = UberDarkBorder, modifier = Modifier.padding(vertical = 10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = " \u00B7 ",
                            color = UberGrayText,
                            fontSize = 12.sp
                        )
                        Text(
                            text = "Levantar MCX ➔",
                            color = UberWhite,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { showWithdrawalDialog = true }
                        )
                    }
                }
            }

            // Centro: Modal 30s ou Viagem Ativa ou GO ONLINE
            if (pendingDispatch != null) {
                DriverDispatchModal(
                    order = pendingDispatch!!,
                    countdownSeconds = countdownSeconds,
                    onAccept = {
                        val driver = DriverPartner(
                            id = "DRV-AO-001",
                            name = driverName,
                            phone = driverPhone,
                            vehicleModel = vehicleModel,
                            licensePlate = licensePlate
                        )
                        GiroRealtimeHub.acceptRide(pendingDispatch!!.id, driver)
                        activeTrip = pendingDispatch
                        pendingDispatch = null
                        Toast.makeText(context, "Viagem Aceite! A caminho da recolha.", Toast.LENGTH_SHORT).show()
                    },
                    onReject = { pendingDispatch = null }
                )
            } else if (activeTrip != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = UberDarkCard),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, UberDarkBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "VIAGEM EM ANDAMENTO",
                            color = UberGreen,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = "Passageiro: ",
                            color = UberWhite,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        Text(
                            text = "Destino: ",
                            color = UberGrayText,
                            fontSize = 14.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    GiroRealtimeHub.updateRideStatus(activeTrip!!.id, RideStatus.ARRIVED_PICKUP)
                                    Toast.makeText(context, "Passageiro avisado que chegou!", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = UberDarkBorder)
                            ) {
                                Text("Cheguei", fontSize = 12.sp)
                            }
                            Button(
                                onClick = {
                                    GiroRealtimeHub.updateRideStatus(activeTrip!!.id, RideStatus.IN_PROGRESS)
                                    Toast.makeText(context, "Viagem iniciada!", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = UberWhite, contentColor = UberBlack)
                            ) {
                                Text("Iniciar", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                            Button(
                                onClick = {
                                    GiroRealtimeHub.updateRideStatus(activeTrip!!.id, RideStatus.COMPLETED)
                                    Toast.makeText(context, "Viagem finalizada! +Kz  creditados.", Toast.LENGTH_LONG).show()
                                    activeTrip = null
                                },
                                modifier = Modifier.weight(1.2f),
                                colors = ButtonDefaults.buttonColors(containerColor = UberGreen, contentColor = UberBlack)
                            ) {
                                Text("Finalizar", fontSize = 12.sp, fontWeight = FontWeight.Black)
                            }
                        }
                    }
                }
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val infiniteTransition = rememberInfiniteTransition()
                    val pulseScale by infiniteTransition.animateFloat(
                        initialValue = 1f,
                        targetValue = if (isOnline) 1.08f else 1f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(1200, easing = LinearEasing),
                            repeatMode = RepeatMode.Reverse
                        )
                    )

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(170.dp)
                            .scale(pulseScale)
                            .background(if (isOnline) UberGreen else UberDarkCard, CircleShape)
                            .border(3.dp, if (isOnline) UberWhite else UberDarkBorder, CircleShape)
                            .clickable {
                                isOnline = !isOnline
                                GiroRealtimeHub.toggleDriverOnline("DRV-AO-001", isOnline)
                            }
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = if (isOnline) "ONLINE" else "OFFLINE",
                                color = if (isOnline) UberBlack else UberGrayText,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = if (isOnline) "A receber chamadas" else "Toque para ligar",
                                color = if (isOnline) UberBlack.copy(alpha = 0.7f) else UberGrayText,
                                fontSize = 10.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = if (isOnline) "GPS Ativo em Luanda \u00B7 Pronto para Corridas" else "Você está desconectado",
                        color = if (isOnline) UberGreen else UberGrayText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )

                    TextButton(
                        onClick = {
                            GiroRealtimeHub.requestRide(
                                passengerName = "Isaac Carvalho",
                                passengerPhone = "+244 923 884 192",
                                origin = LocationPoint("Belas Shopping, Talatona", "Luanda"),
                                destination = LocationPoint("Ilha de Luanda", "Luanda"),
                                category = VehicleCategory.CAR_SEDAN,
                                paymentMethod = PaymentMethod.MULTICAIXA_EXPRESS
                            )
                        },
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text("Simular Chamada de Passageiro ⚡", color = UberWhite, fontSize = 12.sp)
                    }
                }
            }

            // Rodapé
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onLogout) {
                    Text("Terminar Sessão", color = UberEmergencyRed, fontSize = 13.sp)
                }
                Text(
                    text = "GIRO Condutor v1.0 \u00B7 <25MB",
                    color = UberGrayText,
                    fontSize = 11.sp
                )
            }
        }
    }

    if (showWithdrawalDialog) {
        AlertDialog(
            onDismissRequest = { showWithdrawalDialog = false },
            containerColor = UberDarkCard,
            title = { Text("Levantamento Multicaixa Express", color = UberWhite, fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Transferência imediata para o seu número MCX:", color = UberGrayText, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    GiroTextField(
                        value = withdrawalAmount,
                        onValueChange = { withdrawalAmount = it },
                        label = "Valor em Kwanzas (Kz)",
                        leadingText = "Kz",
                        placeholder = "25000"
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val amount = withdrawalAmount.toDoubleOrNull() ?: 25000.0
                        GiroRealtimeHub.requestMcxWithdrawal("DRV-AO-001", driverName, driverPhone, amount)
                        Toast.makeText(context, "Pedido de Kz  enviado para processamento MCX!", Toast.LENGTH_LONG).show()
                        showWithdrawalDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = UberGreen, contentColor = UberBlack)
                ) {
                    Text("Confirmar Levantamento", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showWithdrawalDialog = false }) {
                    Text("Cancelar", color = UberGrayText)
                }
            }
        )
    }
}
