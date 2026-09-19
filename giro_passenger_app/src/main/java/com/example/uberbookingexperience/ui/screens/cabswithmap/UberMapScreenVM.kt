package com.example.uberbookingexperience.ui.screens.cabswithmap

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.uberbookingexperience.R
import com.example.uberbookingexperience.model.UberCabInfo

class UberMapScreenVM : ViewModel() {

    fun selectItem(selectedUberCabIndex: Int) {
        cabListing.forEach {
            it.isChecked = false
        }
        cabListing[selectedUberCabIndex] = cabListing[selectedUberCabIndex].copy(isChecked = true)
        selectedUberCab = cabListing[selectedUberCabIndex]
    }

    fun selectItem(selectedUberCabItem: UberCabInfo) {
        cabListing.map {
            it.isChecked = it == selectedUberCabItem
        }
        selectedUberCab = selectedUberCabItem
    }

    fun selectedItem(): UberCabInfo {
        return selectedUberCab ?: cabListing.first()
    }

    var cabListing = mutableStateListOf(
        UberCabInfo(
            cabInfo = "GIRO Clássico",
            cabIcon = R.drawable.ub__mode_nav_ride,
            cabPrice = 1800f,
            isChecked = true,
            cabPriceAlter = 2200f,
            carTime = "15:09"
        ),
        UberCabInfo(
            cabInfo = "GIRO Conforto",
            cabIcon = R.drawable.ub__mode_nav_ride,
            cabPrice = 2800f,
            cabPriceAlter = 3400f,
            carTime = "15:15"
        ),
        UberCabInfo(
            cabInfo = "GIRO Moto",
            cabIcon = R.drawable.ub__mode_nav_bike_scooter,
            cabPrice = 850f,
            carTime = "15:05"
        ),
        UberCabInfo(
            cabInfo = "GIRO Partilhado",
            cabIcon = R.drawable.ub__mode_nav_carpool,
            cabPrice = 1200f,
            cabPriceAlter = 1500f,
            carTime = "15:12"
        ),
        UberCabInfo(
            cabInfo = "GIRO XL",
            cabIcon = R.drawable.ub__mode_nav_ride,
            cabPrice = 4500f,
            cabPriceAlter = 5500f,
            carTime = "15:20"
        ),
        UberCabInfo(
            cabInfo = "GIRO Expresso",
            cabIcon = R.drawable.ub__mode_nav_ride,
            cabPrice = 1600f,
            cabPriceAlter = 2000f,
            carTime = "15:08"
        )
    )
    var selectedUberCab: UberCabInfo? = null
}
