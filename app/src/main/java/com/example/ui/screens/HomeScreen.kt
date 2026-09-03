package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsTransit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BookingEntity
import com.example.ui.components.ActiveBookingCard
import com.example.ui.components.MainServiceCards
import com.example.ui.components.MapVisualizer
import com.example.ui.theme.RideBackgroundLight
import com.example.ui.theme.RideBorderLight
import com.example.ui.theme.RideDeepNavy
import com.example.ui.theme.RideElectricLime
import com.example.ui.theme.RideNavyElevated
import com.example.ui.theme.RideNavySurface
import com.example.ui.theme.RideSOSRed
import com.example.ui.theme.RideSurfaceLight
import com.example.ui.theme.RideTextMuted
import com.example.ui.theme.RideTextPrimary
import com.example.ui.theme.RideTextSecondary

@Composable
fun HomeScreen(
    activeBooking: BookingEntity?,
    isGirlRiderAvailable: Boolean,
    sosAlertMessage: String?,
    onDismissSosAlert: () -> Unit,
    onBookRideClick: () -> Unit,
    onBookParcelClick: () -> Unit,
    onBookDriverClick: () -> Unit,
    onVerifyOtp: () -> Unit,
    onCompleteBooking: () -> Unit,
    onCancelBooking: () -> Unit,
    onSosClick: () -> Unit,
    onSimulate15MinWarning: () -> Unit,
    modifier: Modifier = Modifier
) {
    var searchDestinationText by remember { mutableStateOf("") }
    val quickDestinations = listOf(
        Pair("Home", Icons.Default.Home),
        Pair("Work", Icons.Default.Work),
        Pair("Railway Station", Icons.Default.DirectionsTransit),
        Pair("Hospital", Icons.Default.LocalHospital)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(RideBackgroundLight)
            .verticalScroll(rememberScrollState())
    ) {
        // SOS Broadcast Alert Banner (if active)
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

        // Upper Section: Large Live Map (~50-60% of viewport)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(340.dp)
        ) {
            MapVisualizer(
                activeStatus = activeBooking?.status,
                isGirlRiderAvailable = isGirlRiderAvailable,
                pickupText = activeBooking?.pickupLocation ?: "Ongole Central",
                dropText = activeBooking?.dropoffLocation ?: if (searchDestinationText.isNotBlank()) searchDestinationText else null
            )

            // Dynamic Greeting & Search Overlay at bottom of map
            Surface(
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                color = RideSurfaceLight,
                shadowElevation = 8.dp,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "GOOD MORNING",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = com.example.ui.theme.PolishSlate500,
                                letterSpacing = 0.5.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Where are you going?",
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Bold,
                                color = com.example.ui.theme.PolishSlate900
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Destination Search Input
                    OutlinedTextField(
                        value = searchDestinationText,
                        onValueChange = { searchDestinationText = it },
                        placeholder = { Text("Enter drop location (e.g. Railway Station)", fontSize = 13.sp, color = com.example.ui.theme.PolishSlate400) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = com.example.ui.theme.PolishIndigo600
                            )
                        },
                        trailingIcon = {
                            if (searchDestinationText.isNotEmpty()) {
                                IconButton(onClick = { searchDestinationText = "" }) {
                                    Icon(Icons.Default.Close, contentDescription = "Clear", tint = com.example.ui.theme.PolishSlate500)
                                }
                            }
                        },
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = com.example.ui.theme.PolishIndigo600,
                            unfocusedBorderColor = com.example.ui.theme.PolishBorderSubtle,
                            focusedContainerColor = com.example.ui.theme.PolishSlate50,
                            unfocusedContainerColor = com.example.ui.theme.PolishSlate50
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("destination_search_input")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Quick Saved Destination Chips
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(quickDestinations) { (place, icon) ->
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = com.example.ui.theme.PolishSurface,
                                border = androidx.compose.foundation.BorderStroke(1.dp, com.example.ui.theme.PolishBorder),
                                shadowElevation = 1.dp,
                                modifier = Modifier.clickable {
                                    searchDestinationText = "$place, Ongole"
                                    onBookRideClick()
                                }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = place,
                                        tint = com.example.ui.theme.PolishIndigo600,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = place,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = com.example.ui.theme.PolishSlate800
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Active Booking Floating Card (When a ride, parcel, or driver hire is ongoing)
        if (activeBooking != null) {
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
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

        // Main Services (The 3 Core Modules: Book a Ride, Book a Parcel, Book a Driver)
        MainServiceCards(
            onBookRideClick = onBookRideClick,
            onBookParcelClick = onBookParcelClick,
            onBookDriverClick = onBookDriverClick
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}
