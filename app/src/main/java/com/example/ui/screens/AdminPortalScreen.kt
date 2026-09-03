package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.FareRow
import com.example.ui.components.MapVisualizer
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AdminPortalScreen(
    mapFilter: String,
    onSetMapFilter: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var rideBaseFare by remember { mutableStateOf("50") }
    var ridePerKm by remember { mutableStateOf("14") }
    var surgeMultiplier by remember { mutableStateOf("1.0x (Normal)") }
    var parcelBaseCharge by remember { mutableStateOf("35") }
    var driverHourlyRate by remember { mutableStateOf("200") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(RideBackgroundLight)
    ) {
        // Top Admin Header
        Surface(color = RideDeepNavy, modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Admin Control Center",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Real-time ecosystem operations • Ongole Region",
                            fontSize = 12.sp,
                            color = RideTextMuted
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = RideElectricLime
                    ) {
                        Text(
                            text = "LIVE SYNC",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = RideDeepNavy,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Section 66: Top KPI Cards
            item {
                Text(
                    text = "Operational KPIs",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = RideDeepNavy
                )
            }

            item {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    KpiCard("Total Users", "25,420", "👥", Modifier.weight(1f))
                    KpiCard("Online Drivers", "842", "🟢", Modifier.weight(1f))
                    KpiCard("Active Rides", "124", "🚗", Modifier.weight(1f))
                    KpiCard("Active Parcels", "56", "📦", Modifier.weight(1f))
                    KpiCard("Driver Bookings", "32", "👨‍✈️", Modifier.weight(1f))
                    KpiCard("Gross Revenue", "₹14,28,900", "💰", Modifier.weight(1f))
                }
            }

            // Section 75 & 76: Active SOS Safety Incident Alert in Admin
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, RideSOSRed)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(RideSOSRed),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "🚨", fontSize = 20.sp)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Active SOS Incidents: 0 Active (3 Resolved)",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFB71C1C)
                            )
                            Text(
                                text = "Platform emergency desk monitored 24/7 with zero open escalations.",
                                fontSize = 11.sp,
                                color = Color(0xFFC62828)
                            )
                        }
                    }
                }
            }

            // Section 67: Live Operations Map
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = RideSurfaceLight),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Live Operations Map",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = RideDeepNavy
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(14.dp))
                        ) {
                            MapVisualizer(
                                activeStatus = "ACTIVE",
                                isGirlRiderAvailable = true,
                                pickupText = "Ongole Fleet Operations"
                            )
                        }
                    }
                }
            }

            // Section 77 & 78: Centralized Dynamic Pricing Engine
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = RideSurfaceLight),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Tune, contentDescription = null, tint = RideDeepNavy)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Centralized Pricing Engine",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = RideDeepNavy
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = rideBaseFare,
                            onValueChange = { rideBaseFare = it },
                            label = { Text("Ride Base Fare (₹)") },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = ridePerKm,
                            onValueChange = { ridePerKm = it },
                            label = { Text("Distance Rate (₹/km)") },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = driverHourlyRate,
                            onValueChange = { driverHourlyRate = it },
                            label = { Text("Driver Hire Hourly Base Rate (₹/hour)") },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {},
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = RideDeepNavy, contentColor = Color.White),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Update Live Pricing Parameters", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun KpiCard(
    title: String,
    value: String,
    emoji: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = RideSurfaceLight,
        border = androidx.compose.foundation.BorderStroke(1.dp, RideBorderLight),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = title, fontSize = 11.sp, color = RideTextSecondary)
                Text(text = emoji, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Black, color = RideDeepNavy)
        }
    }
}
