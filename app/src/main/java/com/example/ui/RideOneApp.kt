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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.example.ui.screens.AdminPortalScreen
import com.example.ui.screens.DriverBookingScreen
import com.example.ui.screens.DriverPartnerScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ParcelBookingScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.RideBookingScreen
import com.example.ui.theme.RideDeepNavy
import com.example.ui.theme.RideElectricLime
import com.example.ui.theme.RideSurfaceLight
import com.example.ui.theme.RideTextMuted
import com.example.viewmodel.AppEcosystemMode
import com.example.viewmodel.RideOneViewModel
import kotlinx.coroutines.launch

enum class UserScreenDestination(val route: String, val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    HOME("home", "Home", Icons.Default.Home),
    RIDE("ride", "Ride", Icons.Default.DirectionsCar),
    PARCEL("parcel", "Parcel", Icons.Default.LocalShipping),
    DRIVER("driver", "Driver", Icons.Default.Person),
    ACTIVITY("activity", "Activity", Icons.Default.History),
    PROFILE("profile", "Profile", Icons.Default.Person)
}

@Composable
fun RideOneApp(
    viewModel: RideOneViewModel,
    modifier: Modifier = Modifier
) {
    val ecosystemMode by viewModel.ecosystemMode.collectAsState()
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

    val driverStatus by viewModel.driverStatus.collectAsState()
    val driverTimerSeconds by viewModel.driverTimerSeconds.collectAsState()

    val show15MinWarning by viewModel.show15MinWarning.collectAsState()
    val showExtendModal by viewModel.showExtendModal.collectAsState()
    val adminMapFilter by viewModel.adminMapFilter.collectAsState()

    val authProfile by viewModel.authProfile.collectAsState()
    val authError by viewModel.authError.collectAsState()

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: UserScreenDestination.HOME.route

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val isExpandedLayout = maxWidth >= 600.dp

        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                AppHeader(
                    currentMode = ecosystemMode,
                    isOffline = isOfflineMode,
                    onSelectMode = { mode -> viewModel.setEcosystemMode(mode) }
                )
            },
            bottomBar = {
                if (!isExpandedLayout && ecosystemMode == AppEcosystemMode.USER_APP) {
                    androidx.compose.foundation.layout.Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(com.example.ui.theme.PolishSurface)
                    ) {
                        NavigationBar(
                            containerColor = com.example.ui.theme.PolishSurface,
                            contentColor = com.example.ui.theme.PolishIndigo600,
                            tonalElevation = 0.dp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    width = 1.dp,
                                    color = com.example.ui.theme.PolishBorder,
                                    shape = androidx.compose.ui.graphics.RectangleShape
                                )
                                .testTag("bottom_nav_bar")
                        ) {
                            UserScreenDestination.values().forEach { destination ->
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
                                            modifier = Modifier.size(20.dp)
                                        )
                                    },
                                    label = {
                                        Text(
                                            text = destination.label.uppercase(),
                                            fontSize = 10.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            letterSpacing = 0.5.sp
                                        )
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = com.example.ui.theme.PolishIndigo600,
                                        selectedTextColor = com.example.ui.theme.PolishIndigo600,
                                        indicatorColor = com.example.ui.theme.PolishIndigo50,
                                        unselectedIconColor = com.example.ui.theme.PolishSlate400,
                                        unselectedTextColor = com.example.ui.theme.PolishSlate400
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
            ) {
                // Tablet / Desktop Navigation Rail
                if (isExpandedLayout && ecosystemMode == AppEcosystemMode.USER_APP) {
                    NavigationRail(
                        containerColor = com.example.ui.theme.PolishSurface,
                        contentColor = com.example.ui.theme.PolishIndigo600,
                        modifier = Modifier
                            .fillMaxHeight()
                            .border(1.dp, com.example.ui.theme.PolishBorder)
                    ) {
                        UserScreenDestination.values().forEach { destination ->
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
                                        text = destination.label.uppercase(),
                                        fontSize = 10.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        letterSpacing = 0.5.sp
                                    )
                                },
                                colors = NavigationRailItemDefaults.colors(
                                    selectedIconColor = com.example.ui.theme.PolishIndigo600,
                                    selectedTextColor = com.example.ui.theme.PolishIndigo600,
                                    indicatorColor = com.example.ui.theme.PolishIndigo50,
                                    unselectedIconColor = com.example.ui.theme.PolishSlate400,
                                    unselectedTextColor = com.example.ui.theme.PolishSlate400
                                )
                            )
                        }
                    }
                }

                // Main Content Switching
                Box(modifier = Modifier.weight(1f)) {
                    when (ecosystemMode) {
                        AppEcosystemMode.USER_APP -> {
                            NavHost(
                                navController = navController,
                                startDestination = UserScreenDestination.HOME.route,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                composable(UserScreenDestination.HOME.route) {
                                    HomeScreen(
                                        activeBooking = activeBooking,
                                        isGirlRiderAvailable = isGirlRiderAvailable,
                                        sosAlertMessage = sosAlertMessage,
                                        onDismissSosAlert = { viewModel.dismissSosBanner() },
                                        onBookRideClick = { navController.navigate(UserScreenDestination.RIDE.route) },
                                        onBookParcelClick = { navController.navigate(UserScreenDestination.PARCEL.route) },
                                        onBookDriverClick = { navController.navigate(UserScreenDestination.DRIVER.route) },
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
                                composable(UserScreenDestination.RIDE.route) {
                                    RideBookingScreen(
                                        vehicleOptions = viewModel.vehicleOptions,
                                        selectedVehicleId = selectedVehicleId,
                                        isGirlRiderAvailable = isGirlRiderAvailable,
                                        onSelectVehicle = { viewModel.selectVehicle(it) },
                                        onToggleGirlRider = { viewModel.toggleGirlRiderAvailability() },
                                        onConfirmRide = { pickup, drop ->
                                            viewModel.requestRide(pickup, drop)
                                            navController.navigate(UserScreenDestination.HOME.route) {
                                                popUpTo(UserScreenDestination.HOME.route) { inclusive = false }
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
                                composable(UserScreenDestination.PARCEL.route) {
                                    ParcelBookingScreen(
                                        categories = viewModel.parcelCategories,
                                        selectedCategoryId = selectedParcelCategory,
                                        activeBooking = activeBooking,
                                        onSelectCategory = { viewModel.selectParcelCategory(it) },
                                        onConfirmParcel = { pickup, drop, recName, recPhone ->
                                            viewModel.requestParcel(pickup, drop, recName, recPhone)
                                            navController.navigate(UserScreenDestination.HOME.route) {
                                                popUpTo(UserScreenDestination.HOME.route) { inclusive = false }
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
                                composable(UserScreenDestination.DRIVER.route) {
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
                                            navController.navigate(UserScreenDestination.HOME.route) {
                                                popUpTo(UserScreenDestination.HOME.route) { inclusive = false }
                                            }
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar("Driver booking initiated!")
                                            }
                                        },
                                        onTrigger15MinWarning = { viewModel.testTrigger15MinWarning() },
                                        onBack = { navController.popBackStack() }
                                    )
                                }
                                composable(UserScreenDestination.ACTIVITY.route) {
                                    ActivityScreen(
                                        bookings = allBookings,
                                        onBookAgain = { type ->
                                            when (type) {
                                                "RIDE" -> navController.navigate(UserScreenDestination.RIDE.route)
                                                "PARCEL" -> navController.navigate(UserScreenDestination.PARCEL.route)
                                                "DRIVER" -> navController.navigate(UserScreenDestination.DRIVER.route)
                                                else -> navController.navigate(UserScreenDestination.HOME.route)
                                            }
                                        }
                                    )
                                }
                                composable(UserScreenDestination.PROFILE.route) {
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

                        AppEcosystemMode.DRIVER_APP -> {
                            DriverPartnerScreen(
                                driverStatus = driverStatus,
                                timerSeconds = driverTimerSeconds,
                                activeBooking = activeBooking,
                                onSetStatus = { viewModel.setDriverAvailability(it) },
                                onAcceptBooking = {
                                    viewModel.verifyRideOtp()
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("Booking accepted! Committed to trip.")
                                    }
                                },
                                onVerifyDriverOtp = { viewModel.verifyRideOtp() },
                                onCompleteBooking = {
                                    viewModel.completeCurrentRide()
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("Trip completed & settled.")
                                    }
                                }
                            )
                        }

                        AppEcosystemMode.ADMIN_PORTAL -> {
                            AdminPortalScreen(
                                mapFilter = adminMapFilter,
                                onSetMapFilter = { viewModel.setAdminMapFilter(it) }
                            )
                        }
                    }
                }
            }
        }

        // Global Dialogs

        // 1. SOS Confirmation Dialog (Section 24)
        if (showSosDialog) {
            SosConfirmationDialog(
                onConfirmSos = { viewModel.triggerSosAlert() },
                onDismiss = { viewModel.dismissSosDialog() }
            )
        }

        // 2. Payment Dialog (Section 26 & 27)
        if (showPaymentDialog && activeBooking != null) {
            PaymentDialog(
                fare = activeBooking!!.fare,
                selectedMethod = selectedPaymentMethod,
                onSelectMethod = {},
                onConfirmPayment = { viewModel.confirmPayment() },
                onDismiss = { viewModel.confirmPayment() }
            )
        }

        // 3. Rating Dialog (Section 28 & 29)
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

        // 4. Driver 15-Min Ending Warning Dialog (Section 48 & 49)
        if (show15MinWarning) {
            Driver15MinWarningDialog(
                onEndBooking = { viewModel.completeCurrentRide() },
                onExtendBooking = { viewModel.openExtendModal() }
            )
        }

        // 5. Driver Extension Dialog (Section 50 & 51)
        if (showExtendModal) {
            DriverExtendDialog(
                onConfirmExtension = { hours ->
                    viewModel.confirmDriverExtension(hours)
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Booking extended by $hours hour(s)!")
                    }
                },
                onDismiss = { viewModel.dismissExtendModal() }
            )
        }
    }
}
