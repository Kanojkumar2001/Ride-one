package com.example.data.repository

import com.example.data.dao.BookingDao
import com.example.data.dao.EmergencyContactDao
import com.example.data.dao.SavedPlaceDao
import com.example.data.model.BookingEntity
import com.example.data.model.EmergencyContactEntity
import com.example.data.model.SavedPlaceEntity
import kotlinx.coroutines.flow.Flow

class RideOneRepository(
    private val bookingDao: BookingDao,
    private val emergencyContactDao: EmergencyContactDao,
    private val savedPlaceDao: SavedPlaceDao
) {
    val allBookings: Flow<List<BookingEntity>> = bookingDao.getAllBookings()
    val activeBooking: Flow<BookingEntity?> = bookingDao.getActiveBooking()
    val emergencyContacts: Flow<List<EmergencyContactEntity>> = emergencyContactDao.getAllContacts()
    val savedPlaces: Flow<List<SavedPlaceEntity>> = savedPlaceDao.getAllPlaces()

    suspend fun createBooking(booking: BookingEntity) {
        bookingDao.insertBooking(booking)
    }

    suspend fun updateBooking(booking: BookingEntity) {
        bookingDao.updateBooking(booking)
    }

    suspend fun updateBookingStatus(id: String, status: String) {
        bookingDao.updateStatus(id, status)
    }

    suspend fun cancelBooking(id: String) {
        bookingDao.updateStatus(id, "CANCELLED")
    }

    suspend fun completeBooking(id: String) {
        bookingDao.updateStatus(id, "COMPLETED")
    }

    suspend fun addEmergencyContact(contact: EmergencyContactEntity) {
        emergencyContactDao.insertContact(contact)
    }

    suspend fun removeEmergencyContact(id: Int) {
        emergencyContactDao.deleteContact(id)
    }

    suspend fun addSavedPlace(place: SavedPlaceEntity) {
        savedPlaceDao.insertPlace(place)
    }

    suspend fun removeSavedPlace(id: Int) {
        savedPlaceDao.deletePlace(id)
    }
}
