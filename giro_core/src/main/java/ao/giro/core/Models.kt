package ao.giro.core

import java.text.DecimalFormat

enum class ServiceType(val title: String, val iconEmoji: String) {
    RIDES("Viagens", "🚗"),
    DELIVERY("Entregas", "📦"),
    FREIGHT("Cargas & Mudanças", "🚛")
}

enum class VehicleCategory(
    val id: String,
    val categoryName: String,
    val description: String,
    val serviceType: ServiceType,
    val basePriceKz: Double,
    val pricePerKmKz: Double,
    val capacity: String
) {
    // Viagens
    CAR_SEDAN("car_sedan", "Car (Sedan)", "Conforto e ar-condicionado", ServiceType.RIDES, 2500.0, 450.0, "4 passageiros"),
    AUTO_HIACE("auto_hiace", "Auto (7 Lugares / Hiace)", "Espaço para grupos e famílias", ServiceType.RIDES, 3800.0, 600.0, "7-12 passageiros"),
    BIKE_KUPAPATA("bike_kupapata", "Bike (Kupapata)", "Rápido para o trânsito de Luanda", ServiceType.RIDES, 950.0, 200.0, "1 passageiro"),

    // Entregas
    MOTOBOY_ESTAFETA("moto_estafeta", "MotoBoy (Estafeta)", "Documentos e pequenas encomendas", ServiceType.DELIVERY, 1200.0, 250.0, "Até 10 kg"),
    COURIER_COMERCIAL("courier_comercial", "Courier Comercial", "Caixas médias e entregas de lojas", ServiceType.DELIVERY, 2200.0, 350.0, "Até 30 kg"),
    EXPRESS_DELIVERY("express_delivery", "Express Urgente", "Prioridade máxima de entrega", ServiceType.DELIVERY, 3000.0, 500.0, "Até 20 kg"),

    // Cargas & Mudanças
    PICKUP_1TON("pickup_1ton", "Pick-Up (1 Ton)", "Eletrodomésticos e móveis", ServiceType.FREIGHT, 8500.0, 900.0, "1 Tonelada"),
    CANTER_3_5TON("canter_3_5ton", "Canter (3.5 Ton)", "Mudanças residenciais completas", ServiceType.FREIGHT, 18000.0, 1500.0, "3.5 Toneladas"),
    PESADO_10TON("pesado_10ton", "Pesado (10 Ton)", "Mercadorias e cargas pesadas", ServiceType.FREIGHT, 45000.0, 3200.0, "10 Toneladas")
}

enum class RideStatus {
    SEARCHING,
    DISPATCHED,
    ACCEPTED,
    ARRIVED_PICKUP,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED
}

enum class PaymentMethod(val label: String) {
    MULTICAIXA_EXPRESS("Multicaixa Express (MCX)"),
    CASH("Dinheiro (Kz)"),
    GIRO_WALLET("Carteira GIRO")
}

data class LocationPoint(
    val name: String,
    val address: String,
    val latitude: Double = -8.83833,
    val longitude: Double = 13.23444
)

data class DriverPartner(
    val id: String = "DRV-AO-042",
    val name: String = "Mateus Domingos",
    val phone: String = "+244 923 884 192",
    val vehicleModel: String = "Toyota Corolla",
    val licensePlate: String = "LD-45-89-GH",
    val rating: Double = 4.95,
    val totalTrips: Int = 1420,
    val isOnline: Boolean = true,
    val isApproved: Boolean = true,
    val dailyEarningsKz: Double = 38500.0,
    val currentLat: Double = -8.83833,
    val currentLng: Double = 13.23444
)

data class RideOrder(
    val id: String,
    val passengerName: String,
    val passengerPhone: String,
    val origin: LocationPoint,
    val destination: LocationPoint,
    val category: VehicleCategory,
    val totalFareKz: Double,
    val driverEarningKz: Double = totalFareKz * 0.90, // 90% Retido pelo motorista
    val platformFeeKz: Double = totalFareKz * 0.10, // 10% Taxa fixa GIRO
    val status: RideStatus = RideStatus.SEARCHING,
    val paymentMethod: PaymentMethod = PaymentMethod.MULTICAIXA_EXPRESS,
    val assignedDriver: DriverPartner? = null,
    val createdAtEpoch: Long = System.currentTimeMillis()
)

data class PanicAlert(
    val id: String,
    val rideId: String,
    val triggeredBy: String, // "PASSAGEIRO" ou "MOTORISTA"
    val initiatorName: String,
    val initiatorPhone: String,
    val vehiclePlate: String,
    val vehicleModel: String,
    val latitude: Double = -8.83833,
    val longitude: Double = 13.23444,
    val timestamp: Long = System.currentTimeMillis(),
    val resolved: Boolean = false,
    val pnaDispatched: Boolean = false
)

data class McxWithdrawal(
    val id: String,
    val driverId: String,
    val driverName: String,
    val phoneMcx: String,
    val amountKz: Double,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "PENDENTE"
)

fun Double.formatKz(): String {
    val df = DecimalFormat("#,##0")
    return "Kz " + df.format(this)
}

fun Float.formatKz(): String {
    val df = DecimalFormat("#,##0")
    return "Kz " + df.format(this)
}
