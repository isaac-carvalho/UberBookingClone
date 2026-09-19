package com.example.uberbookingexperience.ui.screens.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ao.giro.core.*

@Composable
fun ServiceCategoryTabs(
    onSelectVehicle: (VehicleCategory) -> Unit
) {
    var selectedService by remember { mutableStateOf(ServiceType.RIDES) }
    var selectedVehicle by remember { mutableStateOf(VehicleCategory.CAR_SEDAN) }

    val categoriesForSelected = VehicleCategory.values().filter { it.serviceType == selectedService }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // Seletor de Categoria Principal
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ServiceType.values().forEach { service ->
                val isSelected = selectedService == service
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) UberBlack else UberLightGrayBg)
                        .border(
                            1.dp,
                            if (isSelected) UberBlack else Color.Transparent,
                            RoundedCornerShape(12.dp)
                        )
                        .clickable {
                            selectedService = service
                            val firstInService = VehicleCategory.values().first { it.serviceType == service }
                            selectedVehicle = firstInService
                            onSelectVehicle(firstInService)
                        }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = service.iconEmoji, fontSize = 20.sp)
                        Text(
                            text = service.title,
                            color = if (isSelected) UberWhite else UberBlack,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Lista horizontal dos veículos disponíveis
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(categoriesForSelected) { vehicle ->
                val isSelected = selectedVehicle == vehicle
                Card(
                    modifier = Modifier
                        .width(190.dp)
                        .clickable {
                            selectedVehicle = vehicle
                            onSelectVehicle(vehicle)
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) UberDarkCard else UberWhite
                    ),
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        if (isSelected) 2.dp else 1.dp,
                        if (isSelected) UberGreen else Color(0xFFE0E0E0)
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = vehicle.categoryName,
                                color = if (isSelected) UberWhite else UberBlack,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .background(UberGreen, RoundedCornerShape(4.dp))
                                        .padding(horizontal = 4.dp, vertical = 2.dp)
                                ) {
                                    Text("ESCOLHIDO", color = UberBlack, fontSize = 8.sp, fontWeight = FontWeight.Black)
                                }
                            }
                        }

                        Text(
                            text = vehicle.description,
                            color = if (isSelected) UberGrayText else Color.Gray,
                            fontSize = 10.sp,
                            maxLines = 1,
                            modifier = Modifier.padding(top = 2.dp)
                        )

                        Text(
                            text = vehicle.capacity,
                            color = UberGreen,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 4.dp)
                        )

                        Text(
                            text = vehicle.basePriceKz.formatKz(),
                            color = if (isSelected) UberWhite else UberBlack,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }
    }
}
