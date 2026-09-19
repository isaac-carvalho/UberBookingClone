package com.example.uberbookingexperience.ui.screens.dashboard.components

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.example.uberbookingexperience.R

@Immutable
data class Offer(
    var title: String,
    var image: Int,
    var bgColor: Color
)

@Immutable
data class OfferForBiggerScreen(
    val offerFirst: Offer,
    val offerSecond: Offer
)

fun getOffers() : List<Offer> {
    val offers = arrayListOf<Offer>()
    offers.add(Offer("A melhor forma de se deslocar na cidade", R.drawable.womanwithphone, Color(0XFF0D4930)))
    offers.add(Offer("Poupança de dados: consome menos internet", R.drawable.wheelchair,Color(0XFF81959F)))
    offers.add(Offer("Segurança em cada viagem com a GIRO", R.drawable.diversity,Color(0XFFFFD7E4)))
    offers.add(Offer("Pronto? Vamos a isso com a GIRO.", R.drawable.cityscape,Color(0XFF34D19B)))
    return offers
}

fun getOffersForBiggerScreen() : List<OfferForBiggerScreen> {
    val offerForBiggerScreen = arrayListOf<OfferForBiggerScreen>()
    offerForBiggerScreen.add(
        OfferForBiggerScreen(
        Offer("A melhor forma de se deslocar na cidade", R.drawable.womanwithphone, Color(0XFF0D4930)),
        Offer("Poupança de dados: consome menos internet", R.drawable.wheelchair,Color(0XFF81959F))
        )
    )
    offerForBiggerScreen.add(
        OfferForBiggerScreen(
            Offer("Segurança em cada viagem com a GIRO", R.drawable.diversity,Color(0XFFFFD7E4)),
            Offer("Pronto? Vamos a isso com a GIRO.", R.drawable.cityscape,Color(0XFF34D19B))
        )
    )
    return offerForBiggerScreen
}
