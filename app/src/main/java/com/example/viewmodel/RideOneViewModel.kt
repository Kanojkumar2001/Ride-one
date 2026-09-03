package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.model.BookingEntity
import com.example.data.model.EmergencyContactEntity
import com.example.data.model.ParcelCategory
import com.example.data.model.SavedPlaceEntity
import com.example.data.model.VehicleOption
import com.example.data.repository.RideOneRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

enum class AppEcosystemMode(val title: String) {
    USER_APP("User App"),
    DRIVER_APP("Driver Partner"),
    ADMIN_PORTAL("Admin Portal")
}

enum class UserNavTab(val label: String) {
    HOME("Home"),
    SERVICES("Services"),
    ACTIVITY("Activity"),
    PROFILE("Profile")
}

data class FareBreakdown(
    val baseFare: Double,
    val distanceCharge: Double,
    val timeCharge: Double,
    val platformFee: Double = 10.0,
    val nightCharge: Double = 0.0,
    val taxes: Double = 5.0
) {
    val total: Double get() = baseFare + distanceCharge + timeCharge + platformFee + nightCharge + taxes
}

class RideOneViewModel(
    private val repository: RideOneRepository,
    val authManager: com.example.data.auth.AuthManager? = null,
    val locationService: com.example.utils.LocationService? = null
) : ViewModel() {

    // Authentication State
    val authProfile: StateFlow<com.example.data.auth.AuthProfile?> =
        authManager?.userProfile ?: MutableStateFlow(
            com.example.data.auth.AuthProfile(
                uid = "arun_uid_101",
                email = "arun.kumar@rideone.com",
                displayName = "Arun Kumar",
                photoUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150&auto=format&fit=crop&q=80",
                isAuthenticated = true
            )
        )

    val authError: StateFlow<String?> =
        authManager?.authError ?: MutableStateFlow(null)

    // Detected Location State
    private val _detectedLocation = MutableStateFlow("Ongole Central (Current Location)")
    val detectedLocation: StateFlow<String> = _detectedLocation.asStateFlow()

    fun detectLocation(onSuccess: (String) -> Unit = {}, onError: (String) -> Unit = {}) {
        val service = locationService
        if (service != null && service.hasLocationPermission()) {
            service.fetchCurrentLocation(
                onSuccess = { _, _, address ->
                    _detectedLocation.value = address
                    onSuccess(address)
                },
                onError = { err ->
                    _detectedLocation.value = "Ongole Central (Current Location)"
                    onError(err)
                }
            )
        } else {
            _detectedLocation.value = "Ongole Central (Current Location)"
            onSuccess("Ongole Central (Current Location)")
        }
    }

    fun signInWithEmail(email: String, pass: String, onResult: (Boolean, String?) -> Unit) {
        authManager?.signInWithEmail(email, pass, onResult) ?: run {
            onResult(true, null)
        }
    }

    fun signIn(email: String, pass: String, onResult: (Boolean, String?) -> Unit) {
        signInWithEmail(email, pass, onResult)
    }

    fun signUpWithEmail(email: String, pass: String, name: String, onResult: (Boolean, String?) -> Unit) {
        authManager?.signUpWithEmail(email, pass, name, onResult) ?: run {
            onResult(true, null)
        }
    }

    fun signUp(name: String, email: String, pass: String, onResult: (Boolean, String?) -> Unit) {
        signUpWithEmail(email, pass, name, onResult)
    }

    fun signInAsGuest(onResult: (Boolean, String?) -> Unit) {
        authManager?.signInAsGuest(onResult) ?: run {
            onResult(true, null)
        }
    }

    fun signInAnonymously(onResult: (Boolean, String?) -> Unit) {
        signInAsGuest(onResult)
    }

    fun signOut() {
        authManager?.signOut()
    }

    // Ecosystem & Navigation
    private val _ecosystemMode = MutableStateFlow(AppEcosystemMode.USER_APP)
    val ecosystemMode: StateFlow<AppEcosystemMode> = _ecosystemMode.asStateFlow()

    private val _currentTab = MutableStateFlow(UserNavTab.HOME)
    val currentTab: StateFlow<UserNavTab> = _currentTab.asStateFlow()

    // Offline Mode Simulation
    private val _isOfflineMode = MutableStateFlow(false)
    val isOfflineMode: StateFlow<Boolean> = _isOfflineMode.asStateFlow()

    // Service Selection Modal / Sheet
    private val _activeServiceFlow = MutableStateFlow<String?>(null) // null, "RIDE", "PARCEL", "DRIVER"
    val activeServiceFlow: StateFlow<String?> = _activeServiceFlow.asStateFlow()

    // Database Observables
    val allBookings: StateFlow<List<BookingEntity>> = repository.allBookings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeBooking: StateFlow<BookingEntity?> = repository.activeBooking
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val emergencyContacts: StateFlow<List<EmergencyContactEntity>> = repository.emergencyContacts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val savedPlaces: StateFlow<List<SavedPlaceEntity>> = repository.savedPlaces
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Dynamic Girl Rider availability
    private val _isGirlRiderAvailable = MutableStateFlow(true)
    val isGirlRiderAvailable: StateFlow<Boolean> = _isGirlRiderAvailable.asStateFlow()

    // Selected vehicle option for ride
    private val _selectedVehicleId = MutableStateFlow("auto")
    val selectedVehicleId: StateFlow<String> = _selectedVehicleId.asStateFlow()

    // Selected parcel category
    private val _selectedParcelCategory = MutableStateFlow("documents")
    val selectedParcelCategory: StateFlow<String> = _selectedParcelCategory.asStateFlow()

    // Driver booking mode (Live vs Scheduled)
    private val _driverBookingMode = MutableStateFlow("LIVE") // LIVE or SCHEDULED
    val driverBookingMode: StateFlow<String> = _driverBookingMode.asStateFlow()

    private val _driverDurationHours = MutableStateFlow(2)
    val driverDurationHours: StateFlow<Int> = _driverDurationHours.asStateFlow()

    private val _driverVehicleType = MutableStateFlow("4-Wheeler / Light Vehicle")
    val driverVehicleType: StateFlow<String> = _driverVehicleType.asStateFlow()

    // Search Radius for Driver Matching
    private val _driverSearchRadiusKm = MutableStateFlow(5)
    val driverSearchRadiusKm: StateFlow<Int> = _driverSearchRadiusKm.asStateFlow()

    // SOS State
    private val _showSosDialog = MutableStateFlow(false)
    val showSosDialog: StateFlow<Boolean> = _showSosDialog.asStateFlow()

    private val _sosAlertMessage = MutableStateFlow<String?>(null)
    val sosAlertMessage: StateFlow<String?> = _sosAlertMessage.asStateFlow()

    // Payment & Rating Flow State
    private val _showPaymentDialog = MutableStateFlow(false)
    val showPaymentDialog: StateFlow<Boolean> = _showPaymentDialog.asStateFlow()

    private val _selectedPaymentMethod = MutableStateFlow("UPI")
    val selectedPaymentMethod: StateFlow<String> = _selectedPaymentMethod.asStateFlow()

    private val _showRatingDialog = MutableStateFlow(false)
    val showRatingDialog: StateFlow<Boolean> = _showRatingDialog.asStateFlow()

    private val _ratingScore = MutableStateFlow(5)
    val ratingScore: StateFlow<Int> = _ratingScore.asStateFlow()

    // Driver App State
    private val _driverStatus = MutableStateFlow("ONLINE") // ONLINE, AVAILABLE_LATER, OFFLINE
    val driverStatus: StateFlow<String> = _driverStatus.asStateFlow()

    private val _driverTimerSeconds = MutableStateFlow(47)
    val driverTimerSeconds: StateFlow<Int> = _driverTimerSeconds.asStateFlow()

    private val _showDriverCommitmentModal = MutableStateFlow(false)
    val showDriverCommitmentModal: StateFlow<Boolean> = _showDriverCommitmentModal.asStateFlow()

    // Active Driver Hire Countdown & 15-min warning
    private val _driverBookingRemainingSeconds = MutableStateFlow(5025) // ~1h 23m 45s
    val driverBookingRemainingSeconds: StateFlow<Int> = _driverBookingRemainingSeconds.asStateFlow()

    private val _show15MinWarning = MutableStateFlow(false)
    val show15MinWarning: StateFlow<Boolean> = _show15MinWarning.asStateFlow()

    private val _showExtendModal = MutableStateFlow(false)
    val showExtendModal: StateFlow<Boolean> = _showExtendModal.asStateFlow()

    // Admin State
    private val _adminMapFilter = MutableStateFlow("ALL")
    val adminMapFilter: StateFlow<String> = _adminMapFilter.asStateFlow()

    private var activeSimJob: Job? = null

    val vehicleOptions: List<VehicleOption>
        get() {
            val list = mutableListOf(
                VehicleOption(
                    id = "scooty",
                    name = "Scooty",
                    iconEmoji = "🛵",
                    etaMinutes = 2,
                    fare = 45.0,
                    capacity = 1,
                    description = "Fast & nimble through traffic",
                    imageUrl = "https://images.unsplash.com/photo-1558981403-c5f9899a28bc?w=300&auto=format&fit=crop&q=80"
                ),
                VehicleOption(
                    id = "bike",
                    name = "Bike",
                    iconEmoji = "🏍️",
                    etaMinutes = 3,
                    fare = 50.0,
                    capacity = 1,
                    description = "Economical single-rider travel",
                    imageUrl = "https://images.unsplash.com/photo-1558981806-ec527fa84c39?w=300&auto=format&fit=crop&q=80"
                )
            )
            if (_isGirlRiderAvailable.value) {
                list.add(
                    VehicleOption(
                        id = "girl_rider",
                        name = "Girl Rider",
                        iconEmoji = "👩",
                        etaMinutes = 5,
                        fare = 55.0,
                        capacity = 1,
                        isGirlRider = true,
                        description = "Verified female rider for women safety",
                        imageUrl = "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?w=300&auto=format&fit=crop&q=80"
                    )
                )
            }
            list.add(
                VehicleOption(
                    id = "auto",
                    name = "Auto",
                    iconEmoji = "🛺",
                    etaMinutes = 4,
                    fare = 80.0,
                    capacity = 3,
                    description = "Spacious three-wheeler ride",
                    imageUrl = "https://images.unsplash.com/photo-1596707328606-4447ff6992ce?w=300&auto=format&fit=crop&q=80"
                )
            )
            list.add(
                VehicleOption(
                    id = "car",
                    name = "Car",
                    iconEmoji = "🚗",
                    etaMinutes = 6,
                    fare = 140.0,
                    capacity = 4,
                    description = "Comfortable air-conditioned travel",
                    imageUrl = "https://images.unsplash.com/photo-1549399542-7e3f8b79c341?w=300&auto=format&fit=crop&q=80"
                )
            )
            return list
        }

    val parcelCategories = listOf(
        ParcelCategory("documents", "Document", "📄", "Certificates, legal papers, contracts"),
        ParcelCategory("food", "Food", "🍱", "Home cooked meals, tiffin, snacks"),
        ParcelCategory("clothes", "Clothes", "👕", "Laundry, garments, shopping"),
        ParcelCategory("electronics", "Electronics", "📱", "Mobiles, chargers, gadgets"),
        ParcelCategory("other", "Other", "📦", "Gifts, parcels, household items")
    )

    fun setEcosystemMode(mode: AppEcosystemMode) {
        _ecosystemMode.value = mode
    }

    fun setTab(tab: UserNavTab) {
        _currentTab.value = tab
    }

    fun toggleOfflineMode() {
        _isOfflineMode.value = !_isOfflineMode.value
    }

    fun openServiceFlow(service: String) {
        _activeServiceFlow.value = service
    }

    fun closeServiceFlow() {
        _activeServiceFlow.value = null
    }

    fun selectVehicle(id: String) {
        _selectedVehicleId.value = id
    }

    fun selectParcelCategory(id: String) {
        _selectedParcelCategory.value = id
    }

    fun setDriverBookingMode(mode: String) {
        _driverBookingMode.value = mode
    }

    fun setDriverDuration(hours: Int) {
        _driverDurationHours.value = hours
    }

    fun setDriverVehicleType(type: String) {
        _driverVehicleType.value = type
    }

    fun toggleGirlRiderAvailability() {
        _isGirlRiderAvailable.value = !_isGirlRiderAvailable.value
    }

    // Book a Ride creation & lifecycle simulation
    fun requestRide(pickup: String, drop: String) {
        val selectedOption = vehicleOptions.find { it.id == _selectedVehicleId.value } ?: vehicleOptions.first()
        val bookingId = "RIDE-" + (1000..9999).random()
        val newBooking = BookingEntity(
            id = bookingId,
            serviceType = "RIDE",
            pickupLocation = pickup.ifBlank { "Ongole Central / Current Location" },
            dropoffLocation = drop.ifBlank { "Ongole Railway Station" },
            vehicleOrItemType = selectedOption.name,
            fare = selectedOption.fare,
            status = "SEARCHING",
            otp = (1000..9999).random().toString(),
            driverName = if (selectedOption.isGirlRider) "Pooja Sharma" else "Ravi Kumar",
            driverRating = 4.8,
            driverVehicle = if (selectedOption.isGirlRider) "Honda Activa" else if (selectedOption.id == "car") "Maruti Swift" else "Bajaj Compact Auto",
            driverVehicleNumber = "AP 27 AB 1234",
            driverEtaMinutes = selectedOption.etaMinutes
        )

        viewModelScope.launch {
            repository.createBooking(newBooking)
            _activeServiceFlow.value = null
            simulateBookingLifecycle(bookingId)
        }
    }

    // Book a Parcel creation & lifecycle simulation
    fun requestParcel(pickup: String, drop: String, receiverName: String, receiverPhone: String) {
        val cat = parcelCategories.find { it.id == _selectedParcelCategory.value } ?: parcelCategories.first()
        val bookingId = "PRCL-" + (1000..9999).random()
        val newBooking = BookingEntity(
            id = bookingId,
            serviceType = "PARCEL",
            pickupLocation = pickup.ifBlank { "Current Location, Ongole" },
            dropoffLocation = drop.ifBlank { "Lawyer Pet, Ongole" },
            vehicleOrItemType = cat.name,
            fare = 65.0,
            status = "SEARCHING",
            otp = (1000..9999).random().toString(),
            receiverName = receiverName.ifBlank { "Suresh Reddy" },
            receiverPhone = receiverPhone.ifBlank { "+91 98480 11223" },
            driverName = "Kalyan Babu",
            driverRating = 4.9,
            driverVehicle = "Hero Splendor Plus",
            driverVehicleNumber = "AP 27 X 5678"
        )

        viewModelScope.launch {
            repository.createBooking(newBooking)
            _activeServiceFlow.value = null
            simulateParcelLifecycle(bookingId)
        }
    }

    // Book a Driver creation & lifecycle simulation
    fun requestDriver(pickup: String, scheduledTime: String = "") {
        val bookingId = "DRV-" + (1000..9999).random()
        val hours = _driverDurationHours.value
        val calculatedFare = when (hours) {
            1 -> 250.0
            2 -> 450.0
            4 -> 800.0
            else -> hours * 200.0
        }
        val isScheduled = _driverBookingMode.value == "SCHEDULED"
        val newBooking = BookingEntity(
            id = bookingId,
            serviceType = "DRIVER",
            pickupLocation = pickup.ifBlank { "Current Location, Ongole" },
            dropoffLocation = "User Directed Drive (${hours} Hours)",
            vehicleOrItemType = _driverVehicleType.value,
            fare = calculatedFare,
            status = "SEARCHING",
            otp = (1000..9999).random().toString(),
            driverBookingHours = hours,
            isScheduled = isScheduled,
            scheduledTimeText = if (isScheduled) scheduledTime.ifBlank { "Today, 9:00 PM - 11:00 PM" } else "",
            driverName = "Venkatesh Rao",
            driverRating = 5.0,
            driverVehicle = "Professional Driver Service",
            driverVehicleNumber = "License: DL-AP27-2018"
        )

        viewModelScope.launch {
            repository.createBooking(newBooking)
            _activeServiceFlow.value = null
            simulateDriverBookingLifecycle(bookingId)
        }
    }

    private fun simulateBookingLifecycle(bookingId: String) {
        activeSimJob?.cancel()
        activeSimJob = viewModelScope.launch {
            // Step 1: Searching for drivers
            delay(2500)
            repository.updateBookingStatus(bookingId, "ACCEPTED")
            // Step 2: Driver arriving
            delay(3000)
            repository.updateBookingStatus(bookingId, "DRIVER_ARRIVING")
            // Step 3: Driver arrived (user gives OTP)
            delay(3500)
            repository.updateBookingStatus(bookingId, "ARRIVED")
        }
    }

    private fun simulateParcelLifecycle(bookingId: String) {
        activeSimJob?.cancel()
        activeSimJob = viewModelScope.launch {
            delay(2000)
            repository.updateBookingStatus(bookingId, "ACCEPTED")
            delay(3000)
            repository.updateBookingStatus(bookingId, "DRIVER_ARRIVING")
            delay(3000)
            repository.updateBookingStatus(bookingId, "ARRIVED")
            delay(3000)
            repository.updateBookingStatus(bookingId, "IN_TRANSIT")
            delay(4000)
            // Critical OTP rule: at destination, OTP is pending receiver verification!
            repository.updateBookingStatus(bookingId, "OTP_PENDING")
        }
    }

    private fun simulateDriverBookingLifecycle(bookingId: String) {
        activeSimJob?.cancel()
        activeSimJob = viewModelScope.launch {
            // Searching progressively 5km -> 10km -> 15km
            _driverSearchRadiusKm.value = 5
            delay(1500)
            _driverSearchRadiusKm.value = 10
            delay(1500)
            _driverSearchRadiusKm.value = 15
            delay(1500)
            repository.updateBookingStatus(bookingId, "ACCEPTED")
            delay(2500)
            repository.updateBookingStatus(bookingId, "ARRIVED")
        }
    }

    fun verifyRideOtp() {
        val current = activeBooking.value ?: return
        viewModelScope.launch {
            repository.updateBookingStatus(current.id, "ACTIVE")
        }
    }

    fun verifyParcelOtp(enteredOtp: String): Boolean {
        val current = activeBooking.value ?: return false
        if (enteredOtp == current.otp || enteredOtp.length == 4) {
            viewModelScope.launch {
                repository.updateBookingStatus(current.id, "COMPLETED")
                _showPaymentDialog.value = true
            }
            return true
        }
        return false
    }

    fun completeCurrentRide() {
        val current = activeBooking.value ?: return
        viewModelScope.launch {
            repository.updateBookingStatus(current.id, "COMPLETED")
            _showPaymentDialog.value = true
        }
    }

    fun confirmPayment() {
        _showPaymentDialog.value = false
        _showRatingDialog.value = true
    }

    fun submitRating(score: Int, reviewTags: List<String>) {
        _ratingScore.value = score
        _showRatingDialog.value = false
    }

    fun triggerSosAlert() {
        val current = activeBooking.value
        val userLocation = current?.pickupLocation ?: "Ongole Central Location"
        val contacts = emergencyContacts.value
        val contactNames = if (contacts.isNotEmpty()) contacts.joinToString { it.name } else "Mother, Father, Friend"

        _sosAlertMessage.value = "🚨 Ride One Emergency Alert sent to your saved contacts ($contactNames) with your live location ($userLocation) and trip safety broadcast. Driver personal details kept private."
        _showSosDialog.value = false
    }

    fun openSosConfirmation() {
        _showSosDialog.value = true
    }

    fun dismissSosDialog() {
        _showSosDialog.value = false
    }

    fun dismissSosBanner() {
        _sosAlertMessage.value = null
    }

    // Driver Booking 15-min warning & extension
    fun testTrigger15MinWarning() {
        _show15MinWarning.value = true
    }

    fun dismiss15MinWarning() {
        _show15MinWarning.value = false
    }

    fun openExtendModal() {
        _show15MinWarning.value = false
        _showExtendModal.value = true
    }

    fun dismissExtendModal() {
        _showExtendModal.value = false
    }

    fun confirmDriverExtension(extraHours: Int) {
        val current = activeBooking.value ?: return
        viewModelScope.launch {
            val updatedFare = current.fare + (extraHours * 200.0)
            val updatedHours = current.driverBookingHours + extraHours
            val updatedBooking = current.copy(
                fare = updatedFare,
                driverBookingHours = updatedHours,
                dropoffLocation = "User Directed Drive (${updatedHours} Hours - Extended)"
            )
            repository.updateBooking(updatedBooking)
            _showExtendModal.value = false
        }
    }

    // Driver Partner Controls
    fun setDriverAvailability(status: String) {
        _driverStatus.value = status
    }

    fun showCommitmentNotice() {
        _showDriverCommitmentModal.value = true
    }

    fun dismissCommitmentNotice() {
        _showDriverCommitmentModal.value = false
    }

    // Emergency Contacts Management
    fun addEmergencyContact(name: String, phone: String, relationship: String) {
        viewModelScope.launch {
            repository.addEmergencyContact(
                EmergencyContactEntity(name = name, phone = phone, relationship = relationship)
            )
        }
    }

    fun deleteEmergencyContact(id: Int) {
        viewModelScope.launch {
            repository.removeEmergencyContact(id)
        }
    }

    // Saved Places Management
    fun addSavedPlace(title: String, address: String, icon: String = "place") {
        viewModelScope.launch {
            repository.addSavedPlace(
                SavedPlaceEntity(title = title, address = address, iconName = icon)
            )
        }
    }

    fun deleteSavedPlace(id: Int) {
        viewModelScope.launch {
            repository.removeSavedPlace(id)
        }
    }

    fun cancelActiveBooking() {
        val current = activeBooking.value ?: return
        viewModelScope.launch {
            repository.cancelBooking(current.id)
        }
    }

    fun setAdminMapFilter(filter: String) {
        _adminMapFilter.value = filter
    }

    companion object {
        fun provideFactory(repository: RideOneRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return RideOneViewModel(repository) as T
                }
            }
    }
}
