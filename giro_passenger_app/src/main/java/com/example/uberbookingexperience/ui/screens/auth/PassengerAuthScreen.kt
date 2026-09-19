package com.example.uberbookingexperience.ui.screens.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ao.giro.core.*

@Composable
fun PassengerAuthScreen(
    onAuthSuccess: (name: String, phone: String) -> Unit,
    onSwitchApp: (String) -> Unit = {}
) {
    val context = LocalContext.current
    var step by remember { mutableStateOf(1) }
    var name by remember { mutableStateOf("Isaac Carvalho") }
    var phone by remember { mutableStateOf("923 884 192") }
    var otpCode by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = UberBlack
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
            ) {
                Text(
                    text = "GIRO",
                    color = UberWhite,
                    fontSize = 38.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp
                )
                Text(
                    text = "ANGOLA 🇦🇴 \u00B7 PASSAGEIRO",
                    color = UberGreen,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Text(
                    text = "Mobilidade, Entregas e Cargas em Luanda",
                    color = UberGrayText,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // Form
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (step == 1) {
                    Text(
                        text = "Introduza o seu telemóvel",
                        color = UberWhite,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Enviaremos um código SMS para validação",
                        color = UberGrayText,
                        fontSize = 13.sp
                    )

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
                        // Prefixo DDI Angola fixo
                        Box(
                            modifier = Modifier
                                .height(56.dp)
                                .width(90.dp)
                                .background(UberDarkCard, RoundedCornerShape(10.dp))
                                .border(1.dp, UberDarkBorder, RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "+244 🇦🇴",
                                color = UberWhite,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
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

                    Spacer(modifier = Modifier.height(8.dp))

                    GiroButton(
                        text = if (isLoading) "A enviar SMS..." else "Continuar",
                        backgroundColor = UberWhite,
                        contentColor = UberBlack,
                        enabled = name.isNotBlank() && phone.length >= 9,
                        onClick = {
                            if (phone.length >= 9) {
                                step = 2
                                otpCode = "1234" // Pré-preenche código para facilitar teste
                                Toast.makeText(context, "SMS de teste enviado: 1234", Toast.LENGTH_LONG).show()
                            }
                        }
                    )
                } else {
                    Text(
                        text = "Código de Validação SMS",
                        color = UberWhite,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Introduza o código de 4 dígitos enviado para +244 $phone",
                        color = UberGrayText,
                        fontSize = 13.sp
                    )

                    OutlinedTextField(
                        value = otpCode,
                        onValueChange = { if (it.length <= 4) otpCode = it },
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

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextButton(onClick = { step = 1 }) {
                            Text("Alterar Número", color = UberGrayText, fontSize = 12.sp)
                        }
                        TextButton(onClick = {
                            Toast.makeText(context, "Novo código reenviado: 1234", Toast.LENGTH_SHORT).show()
                        }) {
                            Text("Reenviar SMS", color = UberGreen, fontSize = 12.sp)
                        }
                    }

                    GiroButton(
                        text = "Entrar no GIRO",
                        backgroundColor = UberGreen,
                        contentColor = UberBlack,
                        enabled = otpCode.length == 4,
                        onClick = {
                            onAuthSuccess(name, "+244 $phone")
                        }
                    )
                }
            }

            // Footer com seletor dos 4 Apps para teste imediato
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            ) {
                Text(
                    text = "Ambiente de Testes GIRO Angola \u00B7 4 Apps",
                    color = UberGrayText,
                    fontSize = 11.sp
                )
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TextButton(onClick = { onSwitchApp("DRIVER") }) {
                        Text("Condutor 🚗", color = UberWhite, fontSize = 12.sp)
                    }
                    TextButton(onClick = { onSwitchApp("CENTRAL") }) {
                        Text("Central 24h 🛡️", color = UberWhite, fontSize = 12.sp)
                    }
                    TextButton(onClick = { onSwitchApp("ADMIN") }) {
                        Text("Admin 🔑", color = UberWhite, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
