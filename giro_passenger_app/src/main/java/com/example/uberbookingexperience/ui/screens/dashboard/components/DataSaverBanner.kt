package com.example.uberbookingexperience.ui.screens.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uberbookingexperience.ui.theme.spacing
import com.example.uberbookingexperience.ui.util.limitWidth

@Composable
fun DataSaverBanner(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .limitWidth()
            .padding(horizontal = MaterialTheme.spacing.medium, vertical = 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFE8F5E9))
            .padding(horizontal = MaterialTheme.spacing.medium, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Bolt,
            contentDescription = "Poupanca de Dados",
            tint = Color(0xFF2E7D32),
            modifier = Modifier.size(24.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Modo Poupança de Dados Ativo",
                color = Color(0xFF1B5E20),
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
            Text(
                text = "Consumo ultrabaixo de internet para passageiros e motoristas",
                color = Color(0xFF2E7D32),
                fontSize = 11.sp
            )
        }
    }
}
