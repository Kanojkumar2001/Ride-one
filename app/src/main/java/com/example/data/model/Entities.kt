package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class ServiceType(val displayName: String) {
    RIDE("Book a Ride"),
    PARCEL("Book a Parcel"),
    DRIVER("Book a Driver")
}

enum class BookingStatus(val label: String) {
    SEARCHING("Finding Driver..."),
    ACCEPTED("Driver Assigned"),
    DRIVER_ARRIVING("Driver Arriving"),
    ARRIVED("Driver Arrived"),
    OTP_PENDING("Awaiting OTP Verification"),
    ACTIVE("Active Ride"),
    IN_TRANSIT("In Transit"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled")
}

@Entity(tableName = "bookings")
data class BookingEntity(
    @PrimaryKey val id: String,
    val serviceType: String, // RIDE, PARCEL, DRIVER
    val pickupLocation: String,
    val dropoffLocation: String,
    val vehicleOrItemType: String,
    val fare: Double,
    val status: String,
    val otp: String,
    val driverName: String = "Ravi Kumar",
    val driverRating: Double = 4.8,
    val driverVehicle: String = "Maruti Swift",
    val driverVehicleNumber: String = "AP 27 AB 1234",
    val driverEtaMinutes: Int = 4,
    val receiverName: String = "",
    val receiverPhone: String = "",
    val driverBookingHours: Int = 2,
    val isScheduled: Boolean = false,
    val scheduledTimeText: String = "",
    val startTimeMillis: Long = System.currentTimeMillis(),
    val endTimeMillis: Long = System.currentTimeMillis() + 7200000L,
    val createdAtMillis: Long = System.currentTimeMillis()
)

@Entity(tableName = "emergency_contacts")
data class EmergencyContactEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val phone: String,
    val relationship: String // Mother, Father, Spouse, Friend, Brother, Sister, Custom
)

@Entity(tableName = "saved_places")
data class SavedPlaceEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String, // Home, Work, Railway Station, Hospital, Airport
    val address: String,
    val iconName: String
)

data class VehicleOption(
    val id: String,
    val name: String,
    val iconEmoji: String,
    val etaMinutes: Int,
    val fare: Double,
    val capacity: Int,
    val isGirlRider: Boolean = false,
    val description: String = "",
    val imageUrl: String = ""
)

val BookingEntity.driverPhotoUrl: String
    get() = if (vehicleOrItemType.contains("Girl", ignoreCase = true)) {
        "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?w=150&auto=format&fit=crop&q=80"
    } else {
        "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150&auto=format&fit=crop&q=80"
    }

data class ParcelCategory(
    val id: String,
    val name: String,
    val iconEmoji: String,
    val description: String
)
