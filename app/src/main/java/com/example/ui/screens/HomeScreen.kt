package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BookingEntity
import com.example.ui.components.ActiveBookingCard
import com.example.ui.components.MainServiceCards
import com.example.ui.components.MapVisualizer
import com.example.ui.theme.PolishSlate50
import com.example.ui.theme.RideSOSRed

@Composable
fun HomeScreen(
    activeBooking: BookingEntity?,
    isGirlRiderAvailable: Boolean,
    sosAlertMessage: String?,
    pickupLocationText: String = "Current Location",
    onDismissSosAlert: () -> Unit,
    onBookRideClick: () -> Unit,
    onBookParcelClick: () -> Unit,
    onBookDriverClick: () -> Unit,
    onVehicleSelect: (String) -> Unit = {},
    onRecenterClick: () -> Unit = {},
    onRadarClick: () -> Unit = {},
    onVerifyOtp: () -> Unit,
    onCompleteBooking: () -> Unit,
    onCancelBooking: () -> Unit,
    onSosClick: () -> Unit,
    onSimulate15MinWarning: () -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(PolishSlate50)
    ) {
        val screenHeight = maxHeight
        // Map covers half of the screen (~48% of screen height)
        val mapHeight = (screenHeight * 0.48f).coerceIn(320.dp, 440.dp)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // SOS Broadcast Alert Banner (if triggered)
            if (sosAlertMessage != null) {
                Surface(
                    color = RideSOSRed,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = sosAlertMessage,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = onDismissSosAlert) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Dismiss",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            // Map Section: Covered with half of the screen!
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(mapHeight)
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                MapVisualizer(
                    activeStatus = activeBooking?.status,
                    isGirlRiderAvailable = isGirlRiderAvailable,
                    pickupText = pickupLocationText,
                    dropText = activeBooking?.dropoffLocation,
                    onSearchClick = onBookRideClick,
                    onRecenterClick = onRecenterClick,
                    onRadarClick = onRadarClick,
                    onVehicleSelect = onVehicleSelect
                )
            }

            // Ongoing Active Booking Card (if ride/parcel/driver is active)
            if (activeBooking != null) {
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                    ActiveBookingCard(
                        booking = activeBooking,
                        onVerifyOtp = onVerifyOtp,
                        onCompleteBooking = onCompleteBooking,
                        onCancelBooking = onCancelBooking,
                        onSosClick = onSosClick,
                        onSimulate15MinWarning = onSimulate15MinWarning
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Services Grid: Bike, Parcel, Book a Driver
            MainServiceCards(
                onBookRideClick = onBookRideClick,
                onBookParcelClick = onBookParcelClick,
                onBookDriverClick = onBookDriverClick
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
