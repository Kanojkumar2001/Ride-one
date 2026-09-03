package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import com.example.ui.components.FareRow
import com.example.ui.theme.RideBackgroundLight
import com.example.ui.theme.RideBorderLight
import com.example.ui.theme.RideDeepNavy
import com.example.ui.theme.RideElectricLime
import com.example.ui.theme.RideNavyElevated
import com.example.ui.theme.RideNavySurface
import com.example.ui.theme.RideSurfaceLight
import com.example.ui.theme.RideTextMuted
import com.example.ui.theme.RideTextPrimary
import com.example.ui.theme.RideTextSecondary
import com.example.ui.theme.RideWarning

@Composable
fun DriverBookingScreen(
    bookingMode: String,
    durationHours: Int,
    vehicleType: String,
    searchRadiusKm: Int,
    activeBooking: BookingEntity?,
    onSetMode: (String) -> Unit,
    onSetDuration: (Int) -> Unit,
    onSetVehicleType: (String) -> Unit,
    onRequestDriver: (String, String) -> Unit,
    onTrigger15MinWarning: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var pickupLocation by remember { mutableStateOf("Green Meadows, Ongole") }
    var scheduledDateText by remember { mutableStateOf("12 September 2026") }
    var scheduledTimeText by remember { mutableStateOf("9:00 PM – 11:00 PM") }

    val isDriverActive = activeBooking != null && activeBooking.serviceType == "DRIVER"

    val calculatedFare = when (durationHours) {
        1 -> 250.0
        2 -> 450.0
        4 -> 800.0
        else -> durationHours * 200.0
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(RideBackgroundLight)
    ) {
        // Top App Bar
        Surface(color = RideDeepNavy, modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Text(
                    text = if (isDriverActive) "Active Driver Hire" else "Hire a Professional Driver",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Differentiation Value Proposition Banner
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = RideNavySurface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "👨‍✈️", fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "You Own The Vehicle. We Provide The Driver.",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Safe night driving, outstation tours, business events, or medical needs. Fully verified, uniformed drivers committed to your schedule.",
                            fontSize = 12.sp,
                            color = RideTextMuted,
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            // Booking Modes (Section 37: Live Booking vs Scheduled Booking)
            item {
                Text(
                    text = "Booking Mode",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = RideDeepNavy
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ModeChoiceCard(
                        title = "Live Booking",
                        subtitle = "Need a driver right now",
                        emoji = "⚡",
                        isSelected = bookingMode == "LIVE",
                        onClick = { onSetMode("LIVE") },
                        modifier = Modifier.weight(1f),
                        testTag = "driver_live_mode"
                    )
                    ModeChoiceCard(
                        title = "Schedule",
                        subtitle = "Book for future time",
                        emoji = "📅",
                        isSelected = bookingMode == "SCHEDULED",
                        onClick = { onSetMode("SCHEDULED") },
                        modifier = Modifier.weight(1f),
                        testTag = "driver_schedule_mode"
                    )
                }
            }

            // If Scheduled, show date and time window
            if (bookingMode == "SCHEDULED") {
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = RideSurfaceLight),
                        border = androidx.compose.foundation.BorderStroke(1.dp, RideBorderLight)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "Schedule Date & Time Window",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = RideDeepNavy
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            OutlinedTextField(
                                value = scheduledDateText,
                                onValueChange = { scheduledDateText = it },
                                label = { Text("Date") },
                                leadingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = null, tint = RideDeepNavy) },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = scheduledTimeText,
                                onValueChange = { scheduledTimeText = it },
                                label = { Text("Time Window") },
                                leadingIcon = { Icon(Icons.Default.AccessTime, contentDescription = null, tint = RideDeepNavy) },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }

            // Pickup Location
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = RideSurfaceLight),
                    border = androidx.compose.foundation.BorderStroke(1.dp, RideBorderLight)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Pickup Location for Driver",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = RideDeepNavy
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = pickupLocation,
                            onValueChange = { pickupLocation = it },
                            label = { Text("Your Location / Vehicle Location") },
                            leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = RideElectricLime) },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("driver_pickup_input")
                        )
                    }
                }
            }

            // Vehicle Category (Section 38 & 71: Scalable architecture ready for 6W/8W)
            item {
                Text(
                    text = "Vehicle You Want Driven",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = RideDeepNavy
                )
                Spacer(modifier = Modifier.height(8.dp))
                val vehicleTypes = listOf(
                    Pair("4-Wheeler / Light Vehicle", "Cars, Sedans, SUVs, Hatchbacks"),
                    Pair("2-Wheeler", "Motorcycles, Scooters"),
                    Pair("Heavy (6W / 8W)", "Mini-trucks, Commercial (Coming Soon)")
                )
                vehicleTypes.forEach { (type, desc) ->
                    val isSelected = vehicleType == type
                    val isComingSoon = type.contains("Heavy")
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) RideNavySurface else RideSurfaceLight
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            if (isSelected) 2.dp else 1.dp,
                            if (isSelected) RideElectricLime else RideBorderLight
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 6.dp)
                            .clickable(enabled = !isComingSoon) { onSetVehicleType(type) }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (type.contains("2-Wheeler")) Icons.Default.TwoWheeler else Icons.Default.DirectionsCar,
                                contentDescription = null,
                                tint = if (isSelected) RideElectricLime else RideDeepNavy
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = type,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else RideTextPrimary
                                )
                                Text(
                                    text = desc,
                                    fontSize = 11.sp,
                                    color = if (isSelected) RideTextMuted else RideTextSecondary
                                )
                            }
                        }
                    }
                }
            }

            // Duration Selector (Section 39: 1h, 2h, 4h, custom)
            item {
                Text(
                    text = "Hire Duration",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = RideDeepNavy
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val durations = listOf(1 to "1 Hour", 2 to "2 Hours", 4 to "4 Hours")
                    durations.forEach { (hrs, label) ->
                        val isSelected = durationHours == hrs
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) RideDeepNavy else RideSurfaceLight,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) RideDeepNavy else RideBorderLight
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onSetDuration(hrs) }
                        ) {
                            Column(
                                modifier = Modifier.padding(vertical = 12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else RideTextPrimary
                                )
                                Text(
                                    text = when (hrs) {
                                        1 -> "₹250"
                                        2 -> "₹450"
                                        else -> "₹800"
                                    },
                                    fontSize = 11.sp,
                                    color = if (isSelected) RideElectricLime else RideTextSecondary
                                )
                            }
                        }
                    }
                }
            }

            // Progressive Search Radius & Matching Conditions Indicator (Section 40 & 43)
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = RideSurfaceLight),
                    border = androidx.compose.foundation.BorderStroke(1.dp, RideBorderLight)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Security, contentDescription = null, tint = RideDeepNavy, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Strict Driver Commitment & Verification",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = RideDeepNavy
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "• Drivers are locked to your booking upon acceptance and cannot accept any other booking until completed.\n• Background verified with driving license & compliance checks.\n• Search Radius expands progressively: 5 KM ➔ 10 KM ➔ 15 KM.",
                            fontSize = 11.sp,
                            color = RideTextSecondary,
                            lineHeight = 15.sp
                        )
                    }
                }
            }

            // Fare Breakdown
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = RideSurfaceLight),
                    border = androidx.compose.foundation.BorderStroke(1.dp, RideBorderLight)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("Driver Fare Breakdown", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = RideDeepNavy)
                        Spacer(modifier = Modifier.height(6.dp))
                        FareRow("Duration Charge ($durationHours Hours)", "₹${(calculatedFare * 0.85).toInt()}")
                        FareRow("Service & Insurance Guarantee", "₹${(calculatedFare * 0.15).toInt()}")
                        HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp), color = RideBorderLight)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total Driver Fare", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = RideDeepNavy)
                            Text("₹${calculatedFare.toInt()}", fontWeight = FontWeight.Black, fontSize = 16.sp, color = RideDeepNavy)
                        }
                    }
                }
            }
        }

        // Bottom CTA
        Surface(
            color = RideSurfaceLight,
            shadowElevation = 10.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(modifier = Modifier.padding(16.dp)) {
                Button(
                    onClick = { onRequestDriver(pickupLocation, scheduledTimeText) },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = RideDeepNavy, contentColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("hire_driver_button")
                ) {
                    Text(
                        text = if (bookingMode == "SCHEDULED") "Confirm Scheduled Driver (₹${calculatedFare.toInt()})" else "Find Driver Now (₹${calculatedFare.toInt()})",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun ModeChoiceCard(
    title: String,
    subtitle: String,
    emoji: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) RideNavySurface else RideSurfaceLight,
        border = androidx.compose.foundation.BorderStroke(
            if (isSelected) 2.dp else 1.dp,
            if (isSelected) RideElectricLime else RideBorderLight
        ),
        modifier = modifier
            .clickable(onClick = onClick)
            .testTag(testTag)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = emoji, fontSize = 22.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) Color.White else RideTextPrimary
            )
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = if (isSelected) RideTextMuted else RideTextSecondary
            )
        }
    }
}
