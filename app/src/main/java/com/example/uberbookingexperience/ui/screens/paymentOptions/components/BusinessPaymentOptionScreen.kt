package com.example.uberbookingexperience.ui.screens.paymentOptions.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.uberbookingexperience.R
import com.example.uberbookingexperience.ui.common.UberButton
import com.example.uberbookingexperience.ui.theme.UberBookingExperienceTheme
import com.example.uberbookingexperience.ui.theme.spacing
import com.example.uberbookingexperience.ui.util.UberIconSize
import com.example.uberbookingexperience.ui.util.rememberIsMobileDevice

@Composable
fun BusinessPaymentOptionScreen() {
    val requiredSizeModifier = remember {
        Modifier.sizeIn(minWidth = 200.dp, maxWidth = 400.dp)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(MaterialTheme.spacing.medium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = requiredSizeModifier,
            color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
            shape = RoundedCornerShape(MaterialTheme.spacing.small)
        ) {
            Column(
                modifier = Modifier.padding(MaterialTheme.spacing.medium)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        modifier = Modifier
                            .padding(start = MaterialTheme.spacing.medium)
                            .size(UberIconSize.LargeIcon)
                            .aspectRatio(1f),
                        painter = painterResource(id = R.drawable.ic_business_travel),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        text = "Aproveite mais com viagens de trabalho",
                        style = MaterialTheme.typography.displaySmall,
                        textAlign = TextAlign.Center
                    )
                }
                BusinessBannerText(
                    title = "Despesas mais rápidas e simples",
                    value = "Sincronização automática com relatórios de despesas"
                )
                BusinessBannerText(
                    title = "Separe viagens de trabalho",
                    value = "Receba faturas e recibos no seu email profissional"
                )
                BusinessBannerText(
                    title = "Relatórios de viagem",
                    value = "Consulte todas as deslocações num único local"
                )
            }
        }
        if (rememberIsMobileDevice()) {
            Spacer(modifier = Modifier.weight(1f))
        }
        UberButton(
            modifier = requiredSizeModifier
                .padding(horizontal = MaterialTheme.spacing.small)
                .padding(top = MaterialTheme.spacing.extraLarge),
            text = "Ativar perfil profissional",
            onClick = {}
        )
    }
}

@Composable
private fun BusinessBannerText(
    imageVector: ImageVector = Icons.Filled.Done,
    title: String,
    value: String
) {
    Row(
        modifier = Modifier.padding(top = MaterialTheme.spacing.medium)
    ) {
        Image(
            modifier = Modifier.padding(end = MaterialTheme.spacing.medium),
            imageVector = imageVector,
            contentDescription = null
        )
        Column {
            Text(
                text = title,
                fontWeight = FontWeight.Medium
            )
            Text(
                modifier = Modifier
                    .padding(top = MaterialTheme.spacing.small)
                    .fillMaxWidth(0.8f),
                text = value,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Preview(showSystemUi = true, device = "spec:width=411dp,height=891dp")
@Preview(showSystemUi = true, device = "spec:width=673.5dp,height=841dp,dpi=480")
@Preview(showSystemUi = true, device = "spec:width=1280dp,height=800dp,dpi=480")
@Preview(showSystemUi = true, device = "spec:width=1920dp,height=1080dp,dpi=480")
@Composable
private fun BusinessPaymentOptionScreenPreview() {
    UberBookingExperienceTheme {
        BusinessPaymentOptionScreen()
    }
}
