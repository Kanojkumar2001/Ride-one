package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ui.components.AppHeader
import com.example.ui.components.Driver15MinWarningDialog
import com.example.ui.components.DriverExtendDialog
import com.example.ui.components.PaymentDialog
import com.example.ui.components.RatingDialog
import com.example.ui.components.SosConfirmationDialog
import com.example.ui.screens.ActivityScreen
import com.example.ui.screens.DriverBookingScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MapScreen
import com.example.ui.screens.ParcelBookingScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.RideBookingScreen
import com.example.ui.screens.SafetyScreen
import com.example.ui.screens.ServicesScreen
import com.example.ui.theme.PolishBorder
import com.example.ui.theme.PolishSlate400
import com.example.ui.theme.PolishSurface
import com.example.ui.theme.RideOrangeLight
import com.example.ui.theme.RideOrangePrimary
import com.example.viewmodel.RideOneViewModel
import kotlinx.coroutines.launch

data class BottomNavDestination(
    val route: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

val BottomNavItems = listOf(
    BottomNavDestination("home", "Home", Icons.Default.Home),
    BottomNavDestination("map", "Map", Icons.Default.Map),
    BottomNavDestination("services", "Services", Icons.Default.GridView),
    BottomNavDestination("safety", "Safety", Icons.Default.Security),
    BottomNavDestination("profile", "Profile", Icons.Default.Person)
)

@Composable
fun RideOneApp(
    viewModel: RideOneViewModel,
    modifier: Modifier = Modifier
) {
    val isOfflineMode by viewModel.isOfflineMode.collectAsState()
    val activeBooking by viewModel.activeBooking.collectAsState()
    val allBookings by viewModel.allBookings.collectAsState()
    val emergencyContacts by viewModel.emergencyContacts.collectAsState()
    val savedPlaces by viewModel.savedPlaces.collectAsState()

    val isGirlRiderAvailable by viewModel.isGirlRiderAvailable.collectAsState()
    val selectedVehicleId by viewModel.selectedVehicleId.collectAsState()
    val selectedParcelCategory by viewModel.selectedParcelCategory.collectAsState()
    val driverBookingMode by viewModel.driverBookingMode.collectAsState()
    val driverDurationHours by viewModel.driverDurationHours.collectAsState()
    val driverVehicleType by viewModel.driverVehicleType.collectAsState()
    val driverSearchRadiusKm by viewModel.driverSearchRadiusKm.collectAsState()

    val showSosDialog by viewModel.showSosDialog.collectAsState()
    val sosAlertMessage by viewModel.sosAlertMessage.collectAsState()

    val showPaymentDialog by viewModel.showPaymentDialog.collectAsState()
    val selectedPaymentMethod by viewModel.selectedPaymentMethod.collectAsState()
    val showRatingDialog by viewModel.showRatingDialog.collectAsState()
    val ratingScore by viewModel.ratingScore.collectAsState()

    val show15MinWarning by viewModel.show15MinWarning.collectAsState()
    val showExtendModal by viewModel.showExtendModal.collectAsState()

    val authProfile by viewModel.authProfile.collectAsState()
    val authError by viewModel.authError.collectAsState()
    val detectedLocation by viewModel.detectedLocation.collectAsState()

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "home"

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val isExpandedLayout = maxWidth >= 600.dp

        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                AppHeader(
                    locationText = detectedLocation,
                    isOffline = isOfflineMode,
                    onLocationClick = {
                        viewModel.detectLocation(
                            onSuccess = { loc ->
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Location: $loc")
                                }
                            }
                        )
                    },
                    onNotificationClick = {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("You're all caught up! No new notifications.")
                        }
                    },
                    onProfileClick = {
                        navController.navigate("profile") {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            },
            bottomBar = {
                if (!isExpandedLayout) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(PolishSurface)
                    ) {
                        NavigationBar(
                            containerColor = PolishSurface,
                            contentColor = RideOrangePrimary,
                            tonalElevation = 0.dp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    width = 1.dp,
                                    color = PolishBorder,
                                    shape = RectangleShape
                                )
                                .testTag("bottom_nav_bar")
                        ) {
                            BottomNavItems.forEach { destination ->
                                val isSelected = currentRoute == destination.route
                                NavigationBarItem(
                                    selected = isSelected,
                                    onClick = {
                                        navController.navigate(destination.route) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },
                                    icon = {
                                        Icon(
                                            imageVector = destination.icon,
                                            contentDescription = destination.label,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    },
                                    label = {
                                        Text(
                                            text = destination.label,
                                            fontSize = 11.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                        )
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = RideOrangePrimary,
                                        selectedTextColor = RideOrangePrimary,
                                        indicatorColor = RideOrangeLight,
                                        unselectedIconColor = PolishSlate400,
                                        unselectedTextColor = PolishSlate400
                                    )
                                )
                            }
                        }
                    }
                }
            }
        ) { innerPadding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .imePadding()
            ) {
                // Tablet / Desktop Navigation Rail
                if (isExpandedLayout) {
                    NavigationRail(
                        containerColor = PolishSurface,
                        contentColor = RideOrangePrimary,
                        modifier = Modifier
                            .fillMaxHeight()
                            .border(1.dp, PolishBorder)
                    ) {
                        BottomNavItems.forEach { destination ->
                            val isSelected = currentRoute == destination.route
                            NavigationRailItem(
                                selected = isSelected,
                                onClick = {
                                    navController.navigate(destination.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                icon = {
                                    Icon(
                                        imageVector = destination.icon,
                                        contentDescription = destination.label
                                    )
                                },
                                label = {
                                    Text(
                                        text = destination.label,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                    )
                                },
                                colors = NavigationRailItemDefaults.colors(
                                    selectedIconColor = RideOrangePrimary,
                                    selectedTextColor = RideOrangePrimary,
                                    indicatorColor = RideOrangeLight,
                                    unselectedIconColor = PolishSlate400,
                                    unselectedTextColor = PolishSlate400
                                )
                            )
                        }
                    }
                }

                // Main Content Switching
                Box(modifier = Modifier.weight(1f)) {
                    NavHost(
                        navController = navController,
                        startDestination = "home",
                        modifier = Modifier.fillMaxSize()
                    ) {
                        composable("home") {
                            HomeScreen(
                                activeBooking = activeBooking,
                                isGirlRiderAvailable = isGirlRiderAvailable,
                                sosAlertMessage = sosAlertMessage,
                                pickupLocationText = detectedLocation,
                                onDismissSosAlert = { viewModel.dismissSosBanner() },
                                onBookRideClick = { navController.navigate("ride") },
                                onBookParcelClick = { navController.navigate("parcel") },
                                onBookDriverClick = { navController.navigate("driver") },
                                onVehicleSelect = { vehicleId ->
                                    viewModel.selectVehicle(vehicleId)
                                    navController.navigate("ride")
                                },
                                onRecenterClick = {
                                    viewModel.detectLocation(
                                        onSuccess = { loc ->
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar("Centered location: $loc")
                                            }
                                        }
                                    )
                                },
                                onRadarClick = {
                                    navController.navigate("map")
                                },
                                onVerifyOtp = {
                                    viewModel.verifyRideOtp()
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("OTP verified! Starting trip.")
                                    }
                                },
                                onCompleteBooking = { viewModel.completeCurrentRide() },
                                onCancelBooking = { viewModel.cancelActiveBooking() },
                                onSosClick = { viewModel.openSosConfirmation() },
                                onSimulate15MinWarning = { viewModel.testTrigger15MinWarning() }
                            )
                        }
                        composable("map") {
                            MapScreen(
                                viewModel = viewModel,
                                onBack = {
                                    if (navController.previousBackStackEntry != null) {
                                        navController.popBackStack()
                                    } else {
                                        navController.navigate("home")
                                    }
                                },
                                onConfirmSelection = { pickup, dropoff ->
                                    navController.navigate("ride") {
                                        popUpTo("home") { inclusive = false }
                                    }
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("Locations set from Google Map! Confirm vehicle.")
                                    }
                                }
                            )
                        }
                        composable("services") {
                            ServicesScreen(
                                onBookRideClick = { navController.navigate("ride") },
                                onBookParcelClick = { navController.navigate("parcel") },
                                onBookDriverClick = { navController.navigate("driver") },
                                onViewActivityClick = { navController.navigate("activity") }
                            )
                        }
                        composable("safety") {
                            SafetyScreen(
                                emergencyContacts = emergencyContacts,
                                onTriggerSos = { viewModel.openSosConfirmation() },
                                onManageContactsClick = { navController.navigate("profile") }
                            )
                        }
                        composable("ride") {
                            val mapPickup by viewModel.pickupAddress.collectAsState()
                            val mapDrop by viewModel.dropoffAddress.collectAsState()

                            RideBookingScreen(
                                vehicleOptions = viewModel.vehicleOptions,
                                selectedVehicleId = selectedVehicleId,
                                isGirlRiderAvailable = isGirlRiderAvailable,
                                initialPickupLocation = mapPickup,
                                initialDropLocation = mapDrop,
                                onOpenMap = { navController.navigate("map") },
                                onSelectVehicle = { viewModel.selectVehicle(it) },
                                onToggleGirlRider = { viewModel.toggleGirlRiderAvailability() },
                                onConfirmRide = { pickup, drop ->
                                    viewModel.requestRide(pickup, drop)
                                    navController.navigate("home") {
                                        popUpTo("home") { inclusive = false }
                                    }
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("Booking confirmed! Driver assigned.")
                                    }
                                },
                                onDetectLocation = { callback ->
                                    viewModel.detectLocation(callback)
                                },
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("parcel") {
                            val mapPickup by viewModel.pickupAddress.collectAsState()
                            val mapDrop by viewModel.dropoffAddress.collectAsState()

                            ParcelBookingScreen(
                                categories = viewModel.parcelCategories,
                                selectedCategoryId = selectedParcelCategory,
                                activeBooking = activeBooking,
                                initialPickupLocation = mapPickup,
                                initialDropLocation = mapDrop,
                                onOpenMap = { navController.navigate("map") },
                                onSelectCategory = { viewModel.selectParcelCategory(it) },
                                onConfirmParcel = { pickup, drop, recName, recPhone ->
                                    viewModel.requestParcel(pickup, drop, recName, recPhone)
                                    navController.navigate("home") {
                                        popUpTo("home") { inclusive = false }
                                    }
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("Parcel booking initiated! Courier assigned.")
                                    }
                                },
                                onVerifyOtp = { otp ->
                                    val valid = viewModel.verifyParcelOtp(otp)
                                    if (valid) {
                                        coroutineScope.launch {
                                            snackbarHostState.showSnackbar("Parcel safely delivered and verified!")
                                        }
                                    }
                                    valid
                                },
                                onDetectLocation = { callback ->
                                    viewModel.detectLocation(callback)
                                },
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("driver") {
                            DriverBookingScreen(
                                bookingMode = driverBookingMode,
                                durationHours = driverDurationHours,
                                vehicleType = driverVehicleType,
                                searchRadiusKm = driverSearchRadiusKm,
                                activeBooking = activeBooking,
                                onSetMode = { viewModel.setDriverBookingMode(it) },
                                onSetDuration = { viewModel.setDriverDuration(it) },
                                onSetVehicleType = { viewModel.setDriverVehicleType(it) },
                                onRequestDriver = { pickup, scheduledTime ->
                                    viewModel.requestDriver(pickup, scheduledTime)
                                    navController.navigate("home") {
                                        popUpTo("home") { inclusive = false }
                                    }
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("Driver booking initiated!")
                                    }
                                },
                                onTrigger15MinWarning = { viewModel.testTrigger15MinWarning() },
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("activity") {
                            ActivityScreen(
                                bookings = allBookings,
                                onBookAgain = { type ->
                                    when (type) {
                                        "RIDE" -> navController.navigate("ride")
                                        "PARCEL" -> navController.navigate("parcel")
                                        "DRIVER" -> navController.navigate("driver")
                                        else -> navController.navigate("home")
                                    }
                                }
                            )
                        }
                        composable("profile") {
                            ProfileScreen(
                                emergencyContacts = emergencyContacts,
                                savedPlaces = savedPlaces,
                                isOfflineMode = isOfflineMode,
                                onToggleOfflineMode = { viewModel.toggleOfflineMode() },
                                onAddEmergencyContact = { name, phone, rel ->
                                    viewModel.addEmergencyContact(name, phone, rel)
                                },
                                onDeleteEmergencyContact = { viewModel.deleteEmergencyContact(it) },
                                onAddSavedPlace = { title, addr ->
                                    viewModel.addSavedPlace(title, addr)
                                },
                                onDeleteSavedPlace = { viewModel.deleteSavedPlace(it) },
                                authProfile = authProfile,
                                authError = authError,
                                onSignIn = { email, pass, cb ->
                                    viewModel.signIn(email, pass, cb)
                                },
                                onSignUp = { name, email, pass, cb ->
                                    viewModel.signUp(name, email, pass, cb)
                                },
                                onSignInAnonymously = { cb ->
                                    viewModel.signInAnonymously(cb)
                                },
                                onSignOut = {
                                    viewModel.signOut()
                                }
                            )
                        }
                    }
                }
            }
        }

        // Global Dialogs

        // 1. SOS Confirmation Dialog
        if (showSosDialog) {
            SosConfirmationDialog(
                onConfirmSos = { viewModel.triggerSosAlert() },
                onDismiss = { viewModel.dismissSosDialog() }
            )
        }

        // 2. Payment Dialog
        if (showPaymentDialog && activeBooking != null) {
            PaymentDialog(
                fare = activeBooking!!.fare,
                selectedMethod = selectedPaymentMethod,
                onSelectMethod = {},
                onConfirmPayment = { viewModel.confirmPayment() },
                onDismiss = { viewModel.confirmPayment() }
            )
        }

        // 3. Rating Dialog
        if (showRatingDialog) {
            RatingDialog(
                score = ratingScore,
                onScoreChange = {},
                onSubmitRating = { score, tags ->
                    viewModel.submitRating(score, tags)
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Thank you for your rating!")
                    }
                },
                onDismiss = { viewModel.submitRating(ratingScore, emptyList()) }
            )
        }

        // 4. Driver 15-Min Ending Warning Dialog
        if (show15MinWarning) {
            Driver15MinWarningDialog(
                onEndBooking = { viewModel.completeCurrentRide() },
                onExtendBooking = { viewModel.openExtendModal() }
            )
        }

        // 5. Driver Extension Dialog
        if (showExtendModal) {
            DriverExtendDialog(
                onConfirmExtension = { hours -> viewModel.confirmDriverExtension(hours) },
                onDismiss = { viewModel.dismissExtendModal() }
            )
        }
    }
}
