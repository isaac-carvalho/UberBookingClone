package com.example.uberbookingexperience.ui.screens.dashboard.components

import androidx.compose.runtime.Immutable
import com.example.uberbookingexperience.R

@Immutable
data class RideOptions(
    val title: String,
    val image: Int
)

fun getRideOptions() : List<RideOptions> {
    val options = arrayListOf<RideOptions>()
    options.add(RideOptions("GIRO Moto", R.drawable.ub__mode_nav_bike_scooter))
    options.add(RideOptions("Bicicleta", R.drawable.ub__mode_nav_bike))
    options.add(RideOptions("GIRO Clássico", R.drawable.ub__mode_nav_ride))
    options.add(RideOptions("Partilhado", R.drawable.ub__mode_nav_carpool))
    options.add(RideOptions("GIRO Conforto", R.drawable.ub__mode_nav_ride))
    options.add(RideOptions("Trotinete", R.drawable.ub__mode_nav_bike))
    options.add(RideOptions("Aluguer", R.drawable.ub__mode_nav_ride))
    return options
}
