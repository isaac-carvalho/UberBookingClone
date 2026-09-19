package com.example.uberbookingexperience.ui.screens.paymentOptions

import androidx.annotation.DrawableRes
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.uberbookingexperience.R
import com.example.uberbookingexperience.ui.common.UberTopBar
import com.example.uberbookingexperience.ui.screens.paymentOptions.components.BusinessPaymentOptionScreen
import com.example.uberbookingexperience.ui.screens.paymentOptions.components.PayeeType
import com.example.uberbookingexperience.ui.screens.paymentOptions.components.PaymentOptionsCategory
import com.example.uberbookingexperience.ui.theme.UberBookingExperienceTheme

@Immutable
data class PaymentOption(
    @DrawableRes val icon: Int,
    val name: String,
    val value: String? = null,
    val selected: Boolean = false,
    val onClick: () -> Unit = {}
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentOptionsScreen(
    onAddedPaymentOptionClick: () -> Unit = {},
    onNavigationBack: () -> Unit
) {
    Scaffold { bodyPadding ->
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.padding(bodyPadding)) {
                UberTopBar(
                    title = "Opções de Pagamento",
                    iconOnClick = onNavigationBack
                )

                var selectedPayeeType by rememberSaveable { mutableStateOf("Pessoal") }
                PayeeType(selectedItemTitle = selectedPayeeType) { selectedItem ->
                    selectedPayeeType = selectedItem
                }

                Crossfade(targetState = selectedPayeeType) { payeeType ->
                    when (payeeType) {
                        "Pessoal" -> {
                            var isGiroCashSelected by rememberSaveable { mutableStateOf(true) }
                            var currentPaymentOption by rememberSaveable { mutableStateOf("Multicaixa Express") }

                            LazyColumn(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                item {
                                    PaymentOptionsCategory(
                                        title = "Saldo GIRO",
                                        paymentOptions = listOf(
                                            PaymentOption(
                                                R.drawable.ic_uber_logo,
                                                name = "Saldo GIRO",
                                                value = "0 Kz",
                                                selected = isGiroCashSelected,
                                                onClick = {
                                                    isGiroCashSelected = !isGiroCashSelected
                                                }
                                            )
                                        ),
                                        mobileUseSwitchForSelected = true
                                    )
                                }
                                item {
                                    PaymentOptionsCategory(
                                        title = "Método de Pagamento",
                                        paymentOptions = listOf(
                                            PaymentOption(
                                                R.drawable.ic_paytm_logo,
                                                name = "Multicaixa Express",
                                                selected = currentPaymentOption == "Multicaixa Express",
                                                onClick = {
                                                    currentPaymentOption = "Multicaixa Express"
                                                }
                                            ),
                                            PaymentOption(
                                                R.drawable.ic_google_pay_logo,
                                                name = "Transferência Bancária (IBAN)",
                                                selected = currentPaymentOption == "Transferência Bancária (IBAN)",
                                                onClick = {
                                                    currentPaymentOption =
                                                        "Transferência Bancária (IBAN)"
                                                }
                                            ),
                                            PaymentOption(
                                                R.drawable.ic_cash_logo,
                                                name = "Dinheiro",
                                                selected = currentPaymentOption == "Dinheiro",
                                                onClick = {
                                                    currentPaymentOption = "Dinheiro"
                                                }
                                            )
                                        ),
                                        footer = "Adicionar Método de Pagamento"
                                    ) {
                                        onAddedPaymentOptionClick()
                                    }
                                }
                                item {
                                    PaymentOptionsCategory(
                                        modifier = Modifier.padding(top = 48.dp),
                                        title = "Vales e Códigos",
                                        useBigTitle = true,
                                        paymentOptions = emptyList(),
                                        footer = "Adicionar código promocional"
                                    )
                                }
                            }
                        }
                        "Empresa" -> {
                            BusinessPaymentOptionScreen()
                        }
                        else -> Unit
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true, device = "spec:width=411dp,height=891dp")
@Preview(showSystemUi = true, device = "spec:width=673.5dp,height=841dp,dpi=480")
@Preview(showSystemUi = true, device = "spec:width=1280dp,height=800dp,dpi=480")
@Preview(showSystemUi = true, device = "spec:width=1920dp,height=1080dp,dpi=480")
@Composable
private fun PaymentOptionsScreenPreview() {
    UberBookingExperienceTheme {
        PaymentOptionsScreen {}
    }
}
