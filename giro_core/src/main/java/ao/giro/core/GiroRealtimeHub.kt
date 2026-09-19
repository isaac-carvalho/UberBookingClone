package ao.giro.core

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object GiroRealtimeHub {

    // Lista de condutores de Luanda
    private val initialDrivers = listOf(
        DriverPartner(
            id = "DRV-AO-001",
            name = "Mateus Domingos",
            phone = "+244 923 884 192",
            vehicleModel = "Toyota Corolla (Branco)",
            licensePlate = "LD-45-89-GH",
            rating = 4.98,
            totalTrips = 1420,
            isOnline = true,
            isApproved = true,
            dailyEarningsKz = 38500.0,
            currentLat = -8.83500,
            currentLng = 13.23800
        ),
        DriverPartner(
            id = "DRV-AO-002",
            name = "António Sebastião",
            phone = "+244 912 334 556",
            vehicleModel = "Toyota Hiace (Azul)",
            licensePlate = "LD-22-10-AA",
            rating = 4.90,
            totalTrips = 980,
            isOnline = true,
            isApproved = true,
            dailyEarningsKz = 52000.0,
            currentLat = -8.84500,
            currentLng = 13.22000
        ),
        DriverPartner(
            id = "DRV-AO-003",
            name = "João Baptista",
            phone = "+244 934 778 990",
            vehicleModel = "Lingken 125cc (Preto)",
            licensePlate = "LD-99-88-ZZ",
            rating = 4.85,
            totalTrips = 2100,
            isOnline = true,
            isApproved = true,
            dailyEarningsKz = 21400.0,
            currentLat = -8.82800,
            currentLng = 13.24200
        )
    )

    private val _drivers = MutableStateFlow<List<DriverPartner>>(initialDrivers)
    val drivers: StateFlow<List<DriverPartner>> = _drivers.asStateFlow()

    private val _activeRides = MutableStateFlow<List<RideOrder>>(emptyList())
    val activeRides: StateFlow<List<RideOrder>> = _activeRides.asStateFlow()

    private val _panicAlerts = MutableStateFlow<List<PanicAlert>>(emptyList())
    val panicAlerts: StateFlow<List<PanicAlert>> = _panicAlerts.asStateFlow()

    private val _withdrawals = MutableStateFlow<List<McxWithdrawal>>(listOf(
        McxWithdrawal(
            id = "WD-001",
            driverId = "DRV-AO-001",
            driverName = "Mateus Domingos",
            phoneMcx = "+244 923 884 192",
            amountKz = 25000.0,
            status = "PENDENTE"
        )
    ))
    val withdrawals: StateFlow<List<McxWithdrawal>> = _withdrawals.asStateFlow()

    private val _platformRetainedFeesKz = MutableStateFlow(1485000.0) // 10% GIRO Vault
    val platformRetainedFeesKz: StateFlow<Double> = _platformRetainedFeesKz.asStateFlow()

    private val _totalPlatformGrossKz = MutableStateFlow(14850000.0)
    val totalPlatformGrossKz: StateFlow<Double> = _totalPlatformGrossKz.asStateFlow()

    // Operação do Passageiro: Solicitar corrida
    fun requestRide(
        passengerName: String,
        passengerPhone: String,
        origin: LocationPoint,
        destination: LocationPoint,
        category: VehicleCategory,
        paymentMethod: PaymentMethod
    ): RideOrder {
        val distanceMultiplier = 1.3 // Distância estimada
        val totalFare = category.basePriceKz + (category.pricePerKmKz * 6.5 * distanceMultiplier)
        val newOrder = RideOrder(
            id = "RO-${System.currentTimeMillis() % 100000}",
            passengerName = passengerName,
            passengerPhone = passengerPhone,
            origin = origin,
            destination = destination,
            category = category,
            totalFareKz = totalFare,
            status = RideStatus.SEARCHING,
            paymentMethod = paymentMethod
        )
        _activeRides.value = _activeRides.value + newOrder
        return newOrder
    }

    // Operação do Motorista: Aceitar corrida
    fun acceptRide(rideId: String, driver: DriverPartner) {
        _activeRides.value = _activeRides.value.map { order ->
            if (order.id == rideId) {
                order.copy(
                    status = RideStatus.ACCEPTED,
                    assignedDriver = driver
                )
            } else order
        }
    }

    // Atualização de fases da viagem
    fun updateRideStatus(rideId: String, newStatus: RideStatus) {
        _activeRides.value = _activeRides.value.map { order ->
            if (order.id == rideId) {
                if (newStatus == RideStatus.COMPLETED) {
                    // Split de 90% para o motorista e 10% retido pela GIRO
                    val driverGain = order.driverEarningKz
                    val platformGain = order.platformFeeKz
                    _platformRetainedFeesKz.value += platformGain
                    _totalPlatformGrossKz.value += order.totalFareKz

                    // Atualiza saldo do motorista
                    order.assignedDriver?.let { drv ->
                        _drivers.value = _drivers.value.map { d ->
                            if (d.id == drv.id) {
                                d.copy(
                                    dailyEarningsKz = d.dailyEarningsKz + driverGain,
                                    totalTrips = d.totalTrips + 1
                                )
                            } else d
                        }
                    }
                }
                order.copy(status = newStatus)
            } else order
        }
    }

    // Motorista: Alternar Online / Offline
    fun toggleDriverOnline(driverId: String, isOnline: Boolean) {
        _drivers.value = _drivers.value.map { d ->
            if (d.id == driverId) d.copy(isOnline = isOnline) else d
        }
    }

    // Botão de Pânico (SOS): Passageiro ou Motorista aciona
    fun triggerPanicAlert(
        rideId: String,
        triggeredBy: String,
        initiatorName: String,
        initiatorPhone: String,
        vehiclePlate: String,
        vehicleModel: String,
        lat: Double = -8.83833,
        lng: Double = 13.23444
    ): PanicAlert {
        val alert = PanicAlert(
            id = "SOS-${System.currentTimeMillis() % 10000}",
            rideId = rideId,
            triggeredBy = triggeredBy,
            initiatorName = initiatorName,
            initiatorPhone = initiatorPhone,
            vehiclePlate = vehiclePlate,
            vehicleModel = vehicleModel,
            latitude = lat,
            longitude = lng
        )
        _panicAlerts.value = _panicAlerts.value + alert
        return alert
    }

    // Central 24h: Acionar Polícia Nacional de Angola (PNA 111)
    fun dispatchPna(alertId: String) {
        _panicAlerts.value = _panicAlerts.value.map { alert ->
            if (alert.id == alertId) alert.copy(pnaDispatched = true) else alert
        }
    }

    // Central 24h: Resolver Incidente
    fun resolvePanicAlert(alertId: String) {
        _panicAlerts.value = _panicAlerts.value.map { alert ->
            if (alert.id == alertId) alert.copy(resolved = true) else alert
        }
    }

    // Motorista: Solicitar levantamento MCX
    fun requestMcxWithdrawal(driverId: String, driverName: String, phoneMcx: String, amountKz: Double) {
        val wd = McxWithdrawal(
            id = "WD-${System.currentTimeMillis() % 10000}",
            driverId = driverId,
            driverName = driverName,
            phoneMcx = phoneMcx,
            amountKz = amountKz,
            status = "PENDENTE"
        )
        _withdrawals.value = _withdrawals.value + wd
    }

    // Admin Vault: Aprovar levantamento MCX
    fun approveWithdrawal(withdrawalId: String) {
        _withdrawals.value = _withdrawals.value.map { wd ->
            if (wd.id == withdrawalId) wd.copy(status = "PROCESSADO (MCX)") else wd
        }
    }

    // Admin Vault: Aprovar / Suspender condutor
    fun setDriverApproved(driverId: String, isApproved: Boolean) {
        _drivers.value = _drivers.value.map { d ->
            if (d.id == driverId) d.copy(isApproved = isApproved) else d
        }
    }
}
