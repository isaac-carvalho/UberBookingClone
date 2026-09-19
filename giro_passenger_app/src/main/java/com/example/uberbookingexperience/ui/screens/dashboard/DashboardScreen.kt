package com.example.uberbookingexperience.ui.screens.dashboard

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ao.giro.core.*
import com.example.uberbookingexperience.ui.screens.dashboard.components.*
import com.example.uberbookingexperience.ui.theme.UberBookingExperienceTheme
import com.example.uberbookingexperience.ui.theme.spacing
import com.example.uberbookingexperience.ui.util.rememberDeviceWidth
import com.example.uberbookingexperience.ui.util.rememberIsMobileDevice
import com.google.accompanist.pager.ExperimentalPagerApi

@OptIn(ExperimentalPagerApi::class)
@Composable
fun DashboardScreen(
    onGotoWhereScreen: () -> Unit,
    onGotoActiveRide: () -> Unit = {},
    onGotoMap: () -> Unit
) {
    val context = LocalContext.current
    val isMobile = rememberIsMobileDevice()
    val screenWidth = rememberDeviceWidth().dp
    val activeRides by GiroRealtimeHub.activeRides.collectAsState()
    val ongoingRide = activeRides.firstOrNull { it.status != RideStatus.COMPLETED && it.status != RideStatus.CANCELLED }

    Row {
        if (!isMobile) {
            SideBar()
        }
        Column {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .background(color = MaterialTheme.colorScheme.onPrimary)
            ) {
                Spacer(modifier = Modifier.statusBarsPadding())

                // GIRO Top Bar com SOS 24h
                GiroTopBar(
                    title = "GIRO Angola",
                    subtitle = "Luanda \u00B7 Mobilidade & Logística",
                    showSos = true,
                    onSosClick = {
                        GiroRealtimeHub.triggerPanicAlert(
                            rideId = ongoingRide?.id ?: "SOS-DASHBOARD",
                            triggeredBy = "PASSAGEIRO",
                            initiatorName = ongoingRide?.passengerName ?: "Isaac Carvalho",
                            initiatorPhone = ongoingRide?.passengerPhone ?: "+244 923 884 192",
                            vehiclePlate = ongoingRide?.assignedDriver?.licensePlate ?: "LD-45-89-GH",
                            vehicleModel = ongoingRide?.assignedDriver?.vehicleModel ?: "Toyota Corolla"
                        )
                        Toast.makeText(context, "ALERTA SOS ENVIADO À CENTRAL 24H!", Toast.LENGTH_LONG).show()
                    }
                )

                // Card de Viagem em Curso (se existir)
                if (ongoingRide != null) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .clickable { onGotoActiveRide() },
                        colors = CardDefaults.cardColors(containerColor = UberGreen),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "VIAGEM EM ANDAMENTO \u00B7 ${ongoingRide?.status?.name ?: ""}",
                                    color = UberBlack,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black
                                )
                                Text(
                                    text = "${ongoingRide?.pickup?.name ?: ""} \u2192 ${ongoingRide?.destination?.name ?: ""}",
                                    color = UberBlack,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "Motorista: ${ongoingRide?.assignedDriver?.name ?: "A aguardar confirmação..."}",
                                    color = UberBlack.copy(alpha = 0.8f),
                                    fontSize = 11.sp
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "Acompanhar",
                                tint = UberBlack
                            )
                        }
                    }
                }

                // Categorias de Serviço: Viagens, Entregas, Cargas & Mudanças
                ServiceCategoryTabs(
                    onSelectVehicle = { _ ->
                        onGotoWhereScreen()
                    }
                )

                // Header carousel
                HorizontalPagerWithIndicator(isMobile, screenWidth)

                // Opções rápidas
                QuickOptions(isMobile)

                // Ponto de recolha e destino
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                DataSaverBanner(Modifier.align(Alignment.CenterHorizontally))
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
                PickupSelection(Modifier.align(Alignment.CenterHorizontally), onGotoWhereScreen)
                DestinationSelection(Modifier.align(Alignment.CenterHorizontally), onGotoWhereScreen)
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLarge))

                // Ao seu redor
                AroundYou(isMobile, screenWidth, onGotoMap)
            }
            if (isMobile) {
                BottomTabs()
            }
        }
    }
}

@Preview(showSystemUi = true, device = "spec:width=1920dp,height=1080dp")
@Composable
private fun DashboardScreenPreview() {
    UberBookingExperienceTheme {
        DashboardScreen(
            onGotoWhereScreen = {},
            onGotoActiveRide = {},
            onGotoMap = {}
        )
    }
}