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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocalAtm
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
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
import com.example.ui.components.DriverCommitmentDialog
import com.example.ui.components.FareRow
import com.example.ui.theme.RideBackgroundLight
import com.example.ui.theme.RideBorderLight
import com.example.ui.theme.RideDeepNavy
import com.example.ui.theme.RideElectricLime
import com.example.ui.theme.RideNavyElevated
import com.example.ui.theme.RideNavySurface
import com.example.ui.theme.RideSOSRed
import com.example.ui.theme.RideSuccess
import com.example.ui.theme.RideSurfaceLight
import com.example.ui.theme.RideTextMuted
import com.example.ui.theme.RideTextPrimary
import com.example.ui.theme.RideTextSecondary
import com.example.ui.theme.RideWarning

@Composable
fun DriverPartnerScreen(
    driverStatus: String,
    timerSeconds: Int,
    activeBooking: BookingEntity?,
    onSetStatus: (String) -> Unit,
    onAcceptBooking: () -> Unit,
    onVerifyDriverOtp: () -> Unit,
    onCompleteBooking: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showCommitmentDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(RideBackgroundLight)
    ) {
        // Driver Header with Status Switcher (Section 55: Online, Available Later, Offline)
        Surface(color = RideDeepNavy, modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Driver Partner App",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Ravi Kumar • AP 27 AB 1234 • Maruti Swift",
                            fontSize = 12.sp,
                            color = RideTextMuted
                        )
                    }

                    // Status Pill
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = when (driverStatus) {
                            "ONLINE" -> RideElectricLime
                            "AVAILABLE_LATER" -> Color(0xFFFFF8E1)
                            else -> RideNavyElevated
                        }
                    ) {
                        Text(
                            text = when (driverStatus) {
                                "ONLINE" -> "🟢 ONLINE"
                                "AVAILABLE_LATER" -> "🟡 LATER"
                                else -> "⚫ OFFLINE"
                            },
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = if (driverStatus == "ONLINE") RideDeepNavy else if (driverStatus == "AVAILABLE_LATER") RideWarning else Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Three Status Modes Selector
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(RideNavySurface)
                        .padding(2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StatusModeOption("Online", driverStatus == "ONLINE", { onSetStatus("ONLINE") }, Modifier.weight(1f))
                    StatusModeOption("Available Later", driverStatus == "AVAILABLE_LATER", { onSetStatus("AVAILABLE_LATER") }, Modifier.weight(1.3f))
                    StatusModeOption("Offline", driverStatus == "OFFLINE", { onSetStatus("OFFLINE") }, Modifier.weight(1f))
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Earnings Dashboard (Section 63)
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = RideSurfaceLight),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = "Today's Earnings", fontSize = 13.sp, color = RideTextSecondary)
                                Text(text = "₹1,850", fontSize = 26.sp, fontWeight = FontWeight.Black, color = RideDeepNavy)
                            }
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(RideElectricLime),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = RideDeepNavy)
                            }
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = RideBorderLight)

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            EarningStat("Completed", "6 Trips")
                            EarningStat("Rides", "3 (₹450)")
                            EarningStat("Parcels", "2 (₹140)")
                            EarningStat("Driver Hire", "1 (₹600)")
                        }
                    }
                }
            }

            // Incoming Request with 60-second timer & Commitment Notice (Sections 41, 42, 59, 60)
            if (activeBooking != null && (activeBooking.status == "SEARCHING" || activeBooking.status == "ACCEPTED")) {
                item {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = RideNavySurface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        border = androidx.compose.foundation.BorderStroke(2.dp, RideElectricLime)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(shape = RoundedCornerShape(8.dp), color = RideElectricLime) {
                                    Text(
                                        text = "NEW REQUEST: ${activeBooking.serviceType}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Black,
                                        color = RideDeepNavy,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                                Text(
                                    text = "Accept within 00:${timerSeconds.toString().padStart(2, '0')}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "Pickup: ${activeBooking.pickupLocation}",
                                fontSize = 13.sp,
                                color = Color.White
                            )
                            Text(
                                text = "Drop: ${activeBooking.dropoffLocation}",
                                fontSize = 13.sp,
                                color = RideTextMuted
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Est. Earning: ₹${(activeBooking.fare * 0.85).toInt()}",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Black,
                                    color = RideElectricLime
                                )
                                Button(
                                    onClick = { showCommitmentDialog = true },
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = RideElectricLime, contentColor = RideDeepNavy),
                                    modifier = Modifier.testTag("driver_accept_cta")
                                ) {
                                    Text("Accept Request", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            } else if (activeBooking != null && activeBooking.status == "ACTIVE") {
                // Active Driver Navigation State
                item {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = RideDeepNavy),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Navigation, contentDescription = null, tint = RideElectricLime)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Active Navigation • Customer: Arun",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "Drop: ${activeBooking.dropoffLocation}", fontSize = 13.sp, color = RideTextMuted)
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = onCompleteBooking,
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = RideElectricLime, contentColor = RideDeepNavy),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Mark Arrived & Settle Trip", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            } else {
                item {
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = RideSurfaceLight),
                        border = androidx.compose.foundation.BorderStroke(1.dp, RideBorderLight)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "🟢", fontSize = 32.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "You Are Ready For Orders",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = RideDeepNavy
                            )
                            Text(
                                text = "Keep the app open. New ride, parcel, and driver hire bookings within Ongole will appear automatically.",
                                fontSize = 12.sp,
                                color = RideTextSecondary,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }

    if (showCommitmentDialog) {
        DriverCommitmentDialog(
            onAcceptCommit = {
                showCommitmentDialog = false
                onAcceptBooking()
            },
            onDecline = {
                showCommitmentDialog = false
            }
        )
    }
}

@Composable
private fun StatusModeOption(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) RideElectricLime else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) RideDeepNavy else Color.White
        )
    }
}

@Composable
private fun EarningStat(label: String, value: String) {
    Column {
        Text(text = label, fontSize = 11.sp, color = RideTextSecondary)
        Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = RideDeepNavy)
    }
}
