package ao.giro.central24h

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ao.giro.core.*

@Composable
fun CentralAuthScreen(
    onLoginSuccess: (operatorCode: String, shift: String) -> Unit
) {
    var operatorCode by remember { mutableStateOf("OP-LUANDA-07") }
    var password by remember { mutableStateOf("giro2026") }
    var shift by remember { mutableStateOf("DIURNO (07h - 19h)") }
    var errorMessage by remember { mutableStateOf("") }

    Surface(modifier = Modifier.fillMaxSize(), color = UberBlack) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "GIRO CENTRAL 24H",
                    color = UberWhite,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.5.sp
                )
                Text(
                    text = "DESPACHO TÁTICO & SEGURANÇA 🇦🇴",
                    color = UberGreen,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Text(
                    text = "Comando de Operações da Província de Luanda",
                    color = UberGrayText,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                GiroTextField(
                    value = operatorCode,
                    onValueChange = { operatorCode = it },
                    label = "Código do Operador (ex: OP-LUANDA-07)",
                    modifier = Modifier.fillMaxWidth()
                )

                GiroTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = "Palavra-passe de Segurança",
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "Turno Operacional",
                    color = UberGrayText,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("DIURNO (07h - 19h)", "NOTURNO (19h - 07h)").forEach { s ->
                        val isSelected = shift == s
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) UberGreen else UberDarkCard)
                                .border(1.dp, if (isSelected) UberGreen else UberDarkBorder, RoundedCornerShape(10.dp))
                                .clickable { shift = s }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = s,
                                color = if (isSelected) UberBlack else UberWhite,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                if (errorMessage.isNotBlank()) {
                    Text(text = errorMessage, color = UberEmergencyRed, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                GiroButton(
                    text = "Autenticar no Radar 24h",
                    backgroundColor = UberWhite,
                    contentColor = UberBlack,
                    onClick = {
                        if (operatorCode.isNotBlank() && password.length >= 4) {
                            onLoginSuccess(operatorCode, shift)
                        } else {
                            errorMessage = "Preencha o código do operador e a senha."
                        }
                    }
                )
            }

            Text(
                text = "Linha Direta de Emergência PNA: 111 \u00B7 Sistema Tático Ativo",
                color = UberGrayText,
                fontSize = 11.sp
            )
        }
    }
}
