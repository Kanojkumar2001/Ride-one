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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BookingEntity
import com.example.ui.theme.RideBackgroundLight
import com.example.ui.theme.RideBorderLight
import com.example.ui.theme.RideDeepNavy
import com.example.ui.theme.RideElectricLime
import com.example.ui.theme.RideNavyElevated
import com.example.ui.theme.RideNavySurface
import com.example.ui.theme.RideSuccess
import com.example.ui.theme.RideSurfaceLight
import com.example.ui.theme.RideTextMuted
import com.example.ui.theme.RideTextPrimary
import com.example.ui.theme.RideTextSecondary
import com.example.ui.theme.RideWarning
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ActivityScreen(
    bookings: List<BookingEntity>,
    onBookAgain: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedFilterIndex by remember { mutableStateOf(0) }
    val filters = listOf("All", "Rides", "Parcels", "Driver Bookings")

    val filteredBookings = bookings.filter { booking ->
        when (selectedFilterIndex) {
            1 -> booking.serviceType == "RIDE"
            2 -> booking.serviceType == "PARCEL"
            3 -> booking.serviceType == "DRIVER"
            else -> true
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(RideBackgroundLight)
    ) {
        // Top Header
        Surface(
            color = com.example.ui.theme.PolishSurface,
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, com.example.ui.theme.PolishBorder)
        ) {
            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
                Text(
                    text = "ACTIVITY",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = com.example.ui.theme.PolishIndigo600,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Booking & Trip History",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = com.example.ui.theme.PolishSlate900
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Track past rides, parcel deliveries, and driver hires",
                    fontSize = 12.sp,
                    color = com.example.ui.theme.PolishSlate500
                )
            }
        }

        // Tabs Row
        TabRow(
            selectedTabIndex = selectedFilterIndex,
            containerColor = com.example.ui.theme.PolishSurface,
            contentColor = com.example.ui.theme.PolishIndigo600,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedFilterIndex]),
                    color = com.example.ui.theme.PolishIndigo600,
                    height = 2.5.dp
                )
            },
            modifier = Modifier.border(1.dp, com.example.ui.theme.PolishBorder)
        ) {
            filters.forEachIndexed { index, title ->
                Tab(
                    selected = selectedFilterIndex == index,
                    onClick = { selectedFilterIndex = index },
                    text = {
                        Text(
                            text = title,
                            fontSize = 12.sp,
                            fontWeight = if (selectedFilterIndex == index) FontWeight.Bold else FontWeight.Medium,
                            color = if (selectedFilterIndex == index) com.example.ui.theme.PolishIndigo600 else com.example.ui.theme.PolishSlate500
                        )
                    }
                )
            }
        }

        if (filteredBookings.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = null,
                        tint = com.example.ui.theme.PolishSlate300,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No ${filters[selectedFilterIndex]} found",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = com.example.ui.theme.PolishSlate900
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "When you complete a booking, its details and receipts will appear here.",
                        fontSize = 12.sp,
                        color = com.example.ui.theme.PolishSlate500,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredBookings, key = { it.id }) { booking ->
                    BookingHistoryItemCard(booking = booking, onBookAgain = { onBookAgain(booking.serviceType) })
                }
            }
        }
    }
}

@Composable
private fun BookingHistoryItemCard(
    booking: BookingEntity,
    onBookAgain: () -> Unit
) {
    val dateFormat = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
    val formattedDate = dateFormat.format(Date(booking.createdAtMillis))

    val serviceEmoji = when (booking.serviceType) {
        "RIDE" -> "🚗"
        "PARCEL" -> "📦"
        "DRIVER" -> "👨‍✈️"
        else -> "🚀"
    }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = com.example.ui.theme.PolishSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, com.example.ui.theme.PolishBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(com.example.ui.theme.PolishIndigo50),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = serviceEmoji, fontSize = 18.sp)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = when (booking.serviceType) {
                            "RIDE" -> "Ride • ${booking.vehicleOrItemType}"
                            "PARCEL" -> "Parcel • ${booking.vehicleOrItemType}"
                            "DRIVER" -> "Driver • ${booking.driverBookingHours}h Hire"
                            else -> booking.serviceType
                        },
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = com.example.ui.theme.PolishSlate900
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = when (booking.status) {
                        "COMPLETED" -> com.example.ui.theme.PolishEmerald50
                        "ACTIVE", "IN_TRANSIT" -> com.example.ui.theme.PolishIndigo50
                        "CANCELLED" -> Color(0xFFFFF1F2)
                        else -> Color(0xFFFEF3C7)
                    }
                ) {
                    Text(
                        text = when (booking.status) {
                            "COMPLETED" -> if (booking.serviceType == "PARCEL") "Delivered" else "Completed"
                            "ACTIVE" -> "Active"
                            "IN_TRANSIT" -> "In Transit"
                            else -> booking.status
                        },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (booking.status) {
                            "COMPLETED" -> com.example.ui.theme.PolishEmerald600
                            "ACTIVE", "IN_TRANSIT" -> com.example.ui.theme.PolishIndigo600
                            "CANCELLED" -> com.example.ui.theme.PolishRose600
                            else -> com.example.ui.theme.PolishAmber600
                        },
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "${booking.pickupLocation} ➔ ${booking.dropoffLocation}",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = com.example.ui.theme.PolishSlate800
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$formattedDate • ${booking.driverName}",
                    fontSize = 11.sp,
                    color = com.example.ui.theme.PolishSlate500
                )
                Text(
                    text = "₹${booking.fare.toInt()}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    color = com.example.ui.theme.PolishSlate900
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = com.example.ui.theme.PolishBorder)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ID: ${booking.id} • Verified OTP: ${booking.otp}",
                    fontSize = 11.sp,
                    color = com.example.ui.theme.PolishSlate400
                )

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = com.example.ui.theme.PolishIndigo50,
                    border = androidx.compose.foundation.BorderStroke(1.dp, com.example.ui.theme.PolishBorderSubtle),
                    modifier = Modifier.clickable(onClick = onBookAgain)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Repeat, contentDescription = null, tint = com.example.ui.theme.PolishIndigo600, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Rebook", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = com.example.ui.theme.PolishIndigo600)
                    }
                }
            }
        }
    }
}
