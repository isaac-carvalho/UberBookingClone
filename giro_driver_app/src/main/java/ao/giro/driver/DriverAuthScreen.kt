package ao.giro.driver

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ao.giro.core.*

@Composable
fun DriverAuthScreen(
    onAuthSuccess: (name: String, phone: String, vehicle: String, plate: String) -> Unit
) {
    var name by remember { mutableStateOf("Mateus Domingos") }
    var phone by remember { mutableStateOf("923884192") }
    var vehicle by remember { mutableStateOf("Toyota Corolla (Branco)") }
    var plate by remember { mutableStateOf("LD-45-89-GH") }
    var code by remember { mutableStateOf("") }
    var isCodeSent by remember { mutableStateOf(false) }

    Surface(modifier = Modifier.fillMaxSize(), color = UberBlack) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "GIRO CONDUTOR",
                    color = UberWhite,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.5.sp
                )
                Text(
                    text = "PARCEIRO OFICIAL ANGOLA 🇦🇴",
                    color = UberGreen,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Text(
                    text = "Você retém 90% líquido de todas as viagens!",
                    color = UberGrayText,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (!isCodeSent) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Nome Completo", color = UberGrayText) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = UberWhite,
                            unfocusedTextColor = UberWhite,
                            focusedBorderColor = UberWhite,
                            unfocusedBorderColor = UberDarkBorder,
                            focusedContainerColor = UberDarkCard,
                            unfocusedContainerColor = UberDarkCard
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .height(56.dp)
                                .width(90.dp)
                                .background(UberDarkCard, RoundedCornerShape(10.dp))
                                .border(1.dp, UberDarkBorder, RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("+244 🇦🇴", color = UberWhite, fontWeight = FontWeight.Bold)
                        }
                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = { Text("Telemóvel (ex: 923...)", color = UberGrayText) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = UberWhite,
                                unfocusedTextColor = UberWhite,
                                focusedBorderColor = UberWhite,
                                unfocusedBorderColor = UberDarkBorder,
                                focusedContainerColor = UberDarkCard,
                                unfocusedContainerColor = UberDarkCard
                            ),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }

                    OutlinedTextField(
                        value = vehicle,
                        onValueChange = { vehicle = it },
                        label = { Text("Modelo do Veículo", color = UberGrayText) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = UberWhite,
                            unfocusedTextColor = UberWhite,
                            focusedBorderColor = UberWhite,
                            unfocusedBorderColor = UberDarkBorder,
                            focusedContainerColor = UberDarkCard,
                            unfocusedContainerColor = UberDarkCard
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = plate,
                        onValueChange = { plate = it },
                        label = { Text("Matrícula (ex: LD-45-89-GH)", color = UberGrayText) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = UberWhite,
                            unfocusedTextColor = UberWhite,
                            focusedBorderColor = UberWhite,
                            unfocusedBorderColor = UberDarkBorder,
                            focusedContainerColor = UberDarkCard,
                            unfocusedContainerColor = UberDarkCard
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    GiroButton(
                        text = "Solicitar Código de Acesso",
                        backgroundColor = UberWhite,
                        contentColor = UberBlack,
                        onClick = {
                            isCodeSent = true
                            code = "7788"
                        }
                    )
                } else {
                    Text(
                        text = "Validação de Segurança",
                        color = UberWhite,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Introduza o código de condutor parceiro enviado para +244 ",
                        color = UberGrayText,
                        fontSize = 13.sp
                    )
                    OutlinedTextField(
                        value = code,
                        onValueChange = { code = it },
                        label = { Text("Código de 4 dígitos", color = UberGrayText) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = UberGreen,
                            unfocusedTextColor = UberWhite,
                            focusedBorderColor = UberGreen,
                            unfocusedBorderColor = UberDarkBorder,
                            focusedContainerColor = UberDarkCard,
                            unfocusedContainerColor = UberDarkCard
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )
                    GiroButton(
                        text = "Iniciar Sessão do Condutor",
                        backgroundColor = UberGreen,
                        contentColor = UberBlack,
                        onClick = {
                            onAuthSuccess(name, "+244 ", vehicle, plate)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
