package ao.giro.admin

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
fun AdminDashboardScreen(
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Motoristas", "Financeiro (10%)", "Levantamentos MCX", "Tarifas")

    val drivers by GiroRealtimeHub.drivers.collectAsState()
    val withdrawals by GiroRealtimeHub.withdrawals.collectAsState()
    val platformFeeKz by GiroRealtimeHub.platformRetainedFeesKz.collectAsState()
    val totalGrossKz by GiroRealtimeHub.totalPlatformGrossKz.collectAsState()

    Surface(modifier = Modifier.fillMaxSize(), color = UberBlack) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "GIRO ADMIN VAULT",
                        color = UberWhite,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "Painel Master \u00B7 Luanda, Angola",
                        color = UberGreen,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                IconButton(onClick = onLogout) {
                    Icon(Icons.Default.ExitToApp, contentDescription = "Sair", tint = UberEmergencyRed)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Tab Bar
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                containerColor = UberDarkCard,
                contentColor = UberWhite,
                edgePadding = 0.dp
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                color = if (selectedTab == index) UberGreen else UberGrayText,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 12.sp
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Conteúdo das Abas
            when (selectedTab) {
                0 -> {
                    // Gestão de Motoristas
                    Text("Gestão de Condutores Parceiros", color = UberWhite, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth().weight(1f).padding(top = 10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(drivers) { d ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = UberDarkCard),
                                border = androidx.compose.foundation.BorderStroke(1.dp, UberDarkBorder)
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column {
                                            Text(d.name, color = UberWhite, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                            Text(" \u00B7 ", color = UberGrayText, fontSize = 12.sp)
                                            Text("Matrícula: ", color = UberGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        }

                                        Box(
                                            modifier = Modifier
                                                .background(if (d.isApproved) UberGreen.copy(alpha = 0.2f) else UberEmergencyRed.copy(alpha = 0.2f), RoundedCornerShape(6.dp))
                                                .border(1.dp, if (d.isApproved) UberGreen else UberEmergencyRed, RoundedCornerShape(6.dp))
                                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                        ) {
                                            Text(
                                                text = if (d.isApproved) "APROVADO" else "PENDENTE",
                                                color = if (d.isApproved) UberGreen else UberEmergencyRed,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Black
                                            )
                                        }
                                    }

                                    Divider(color = UberDarkBorder, modifier = Modifier.padding(vertical = 10.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Button(
                                            onClick = {
                                                GiroRealtimeHub.setDriverApproved(d.id, true)
                                                Toast.makeText(context, " aprovado na plataforma!", Toast.LENGTH_SHORT).show()
                                            },
                                            modifier = Modifier.weight(1f),
                                            colors = ButtonDefaults.buttonColors(containerColor = UberGreen, contentColor = UberBlack),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text("Aprovar", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }

                                        OutlinedButton(
                                            onClick = {
                                                GiroRealtimeHub.setDriverApproved(d.id, false)
                                                Toast.makeText(context, " suspenso temporariamente.", Toast.LENGTH_SHORT).show()
                                            },
                                            modifier = Modifier.weight(1f),
                                            shape = RoundedCornerShape(8.dp),
                                            colors = ButtonDefaults.outlinedButtonColors(contentColor = UberEmergencyRed)
                                        ) {
                                            Text("Suspender", fontSize = 11.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                1 -> {
                    // Balanço Financeiro
                    Column(
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text("Balanço Financeiro Global GIRO Angola", color = UberWhite, fontSize = 15.sp, fontWeight = FontWeight.Bold)

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = UberDarkCard),
                            border = androidx.compose.foundation.BorderStroke(1.dp, UberDarkBorder)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("VOLUME TOTAL FATURADO", color = UberGrayText, fontSize = 11.sp)
                                Text(totalGrossKz.formatKz(), color = UberWhite, fontSize = 28.sp, fontWeight = FontWeight.Black)

                                Divider(color = UberDarkBorder, modifier = Modifier.padding(vertical = 12.dp))

                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Column {
                                        Text("RETENÇÃO GIRO (10%)", color = UberGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        Text(platformFeeKz.formatKz(), color = UberGreen, fontSize = 18.sp, fontWeight = FontWeight.Black)
                                    }
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text("REPASSE MOTORISTAS (90%)", color = UberGrayText, fontSize = 11.sp)
                                        Text((totalGrossKz * 0.90).formatKz(), color = UberWhite, fontSize = 18.sp, fontWeight = FontWeight.Black)
                                    }
                                }
                            }
                        }

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = UberDarkCard),
                            border = androidx.compose.foundation.BorderStroke(1.dp, UberDarkBorder)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("MODELO DE MONETIZAÇÃO TRANSPARENTE", color = UberWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Text("A GIRO Angola opera com taxa fixa de 10% cobrada sobre cada viagem finalizada. O condutor retém 90% líquido com levantamento diário via Multicaixa Express.", color = UberGrayText, fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
                            }
                        }
                    }
                }
                2 -> {
                    // Levantamentos Multicaixa Express
                    Text("Pedidos de Levantamento Multicaixa Express (MCX)", color = UberWhite, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth().weight(1f).padding(top = 10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(withdrawals) { wd ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = UberDarkCard),
                                border = androidx.compose.foundation.BorderStroke(1.dp, UberDarkBorder)
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(wd.driverName, color = UberWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                        Text("MCX: ", color = UberGrayText, fontSize = 12.sp)
                                        Text(wd.amountKz.formatKz(), color = UberGreen, fontWeight = FontWeight.Black, fontSize = 16.sp)
                                    }

                                    if (wd.status == "PENDENTE") {
                                        Button(
                                            onClick = {
                                                GiroRealtimeHub.approveWithdrawal(wd.id)
                                                Toast.makeText(context, "Levantamento MCX processado com sucesso!", Toast.LENGTH_SHORT).show()
                                            },
                                            shape = RoundedCornerShape(8.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = UberWhite, contentColor = UberBlack)
                                        ) {
                                            Text("Pagar MCX", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                    } else {
                                        Text(wd.status, color = UberGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                }
                3 -> {
                    // Tarifas Dinâmicas
                    Text("Tabela de Tarifas por Categoria (Luanda)", color = UberWhite, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth().weight(1f).padding(top = 10.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(VehicleCategory.values()) { cat ->
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
                                        Text(cat.categoryName, color = UberWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        Text(" \u00B7 ", color = UberGrayText, fontSize = 11.sp)
                                    }
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text("Base: ", color = UberGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                        Text("+ /km", color = UberGrayText, fontSize = 10.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
