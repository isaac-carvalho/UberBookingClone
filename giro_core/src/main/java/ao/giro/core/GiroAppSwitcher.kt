package ao.giro.core

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GiroAppSwitcher(
    currentApp: String, // "PASSENGER", "DRIVER", "CENTRAL", "ADMIN"
    onSelectApp: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(UberDarkCard)
            .border(1.dp, UberDarkBorder, RoundedCornerShape(10.dp))
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        val apps = listOf(
            "PASSENGER" to "Passageiro",
            "DRIVER" to "Condutor",
            "CENTRAL" to "Central 24h",
            "ADMIN" to "Admin"
        )
        apps.forEach { (key, label) ->
            val isSelected = currentApp == key
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isSelected) UberWhite else UberDarkCard)
                    .clickable { onSelectApp(key) }
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    color = if (isSelected) UberBlack else UberGrayText,
                    fontSize = 11.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}
