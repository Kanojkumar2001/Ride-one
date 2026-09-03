package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.dao.BookingDao
import com.example.data.dao.EmergencyContactDao
import com.example.data.dao.SavedPlaceDao
import com.example.data.model.BookingEntity
import com.example.data.model.EmergencyContactEntity
import com.example.data.model.SavedPlaceEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [BookingEntity::class, EmergencyContactEntity::class, SavedPlaceEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookingDao(): BookingDao
    abstract fun emergencyContactDao(): EmergencyContactDao
    abstract fun savedPlaceDao(): SavedPlaceDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ride_one.db"
                ).addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Seed initial mock data asynchronously
                        CoroutineScope(Dispatchers.IO).launch {
                            val database = getInstance(context)
                            seedInitialData(database)
                        }
                    }
                }).build()
                INSTANCE = instance
                instance
            }
        }

        private suspend fun seedInitialData(db: AppDatabase) {
            // Seed Emergency Contacts
            val contactDao = db.emergencyContactDao()
            contactDao.insertContact(EmergencyContactEntity(name = "Ananya (Mother)", phone = "+91 98480 22334", relationship = "Mother"))
            contactDao.insertContact(EmergencyContactEntity(name = "Kishore (Father)", phone = "+91 98480 55667", relationship = "Father"))
            contactDao.insertContact(EmergencyContactEntity(name = "Vikram (Friend)", phone = "+91 94401 88990", relationship = "Friend"))

            // Seed Saved Places
            val placeDao = db.savedPlaceDao()
            placeDao.insertPlace(SavedPlaceEntity(title = "Home", address = "Flat 302, Green Meadows, Ongole", iconName = "home"))
            placeDao.insertPlace(SavedPlaceEntity(title = "Work", address = "Tech Hub, South Bypass Road, Ongole", iconName = "work"))
            placeDao.insertPlace(SavedPlaceEntity(title = "Railway Station", address = "Ongole Railway Station (OGL), Platform 1", iconName = "train"))
            placeDao.insertPlace(SavedPlaceEntity(title = "RIMS Hospital", address = "Government General Hospital, Ongole", iconName = "local_hospital"))
            placeDao.insertPlace(SavedPlaceEntity(title = "APSRTC Bus Stand", address = "RTC Complex, Kurnool Road, Ongole", iconName = "directions_bus"))

            // Seed completed past bookings
            val bookingDao = db.bookingDao()
            val now = System.currentTimeMillis()
            bookingDao.insertBooking(
                BookingEntity(
                    id = "RIDE-7821",
                    serviceType = "RIDE",
                    pickupLocation = "Home, Green Meadows",
                    dropoffLocation = "Ongole Railway Station",
                    vehicleOrItemType = "Auto",
                    fare = 80.0,
                    status = "COMPLETED",
                    otp = "4821",
                    driverName = "Ravi Kumar",
                    driverRating = 4.8,
                    driverVehicle = "Bajaj Compact Auto",
                    driverVehicleNumber = "AP 27 TC 1102",
                    createdAtMillis = now - 86400000L
                )
            )
            bookingDao.insertBooking(
                BookingEntity(
                    id = "PRCL-4109",
                    serviceType = "PARCEL",
                    pickupLocation = "Tech Hub, South Bypass",
                    dropoffLocation = "Lawyer Pet, Ongole",
                    vehicleOrItemType = "Documents",
                    fare = 65.0,
                    status = "COMPLETED",
                    otp = "7294",
                    receiverName = "Suresh Reddy",
                    receiverPhone = "+91 98765 43210",
                    driverName = "Kalyan Babu",
                    driverRating = 4.9,
                    createdAtMillis = now - 172800000L
                )
            )
            bookingDao.insertBooking(
                BookingEntity(
                    id = "DRV-9012",
                    serviceType = "DRIVER",
                    pickupLocation = "Green Meadows",
                    dropoffLocation = "Vijayawada Airport Return",
                    vehicleOrItemType = "4-Wheeler (Hyundai Creta)",
                    fare = 600.0,
                    status = "COMPLETED",
                    otp = "3319",
                    driverBookingHours = 2,
                    driverName = "Venkatesh Rao",
                    driverRating = 5.0,
                    createdAtMillis = now - 259200000L
                )
            )
        }
    }
}
