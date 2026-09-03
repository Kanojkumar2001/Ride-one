package com.example.ui.screens

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BookingEntity
import com.example.data.model.ParcelCategory
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ParcelBookingScreen(
    categories: List<ParcelCategory>,
    selectedCategoryId: String,
    activeBooking: BookingEntity?,
    onSelectCategory: (String) -> Unit,
    onConfirmParcel: (String, String, String, String) -> Unit,
    onVerifyOtp: (String) -> Boolean,
    onBack: () -> Unit,
    onDetectLocation: ((String) -> Unit) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var pickupLocation by remember { mutableStateOf("Tech Hub, South Bypass, Ongole") }
    var dropLocation by remember { mutableStateOf("Lawyer Pet, Ongole") }
    var receiverName by remember { mutableStateOf("Suresh Reddy") }
    var receiverPhone by remember { mutableStateOf("+91 98480 11223") }
    var deliveryAddress by remember { mutableStateOf("Door #14-2-90, 2nd Cross, Lawyer Pet") }

    var otpInput by remember { mutableStateOf("") }
    var otpError by remember { mutableStateOf(false) }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) {
            onDetectLocation { detected ->
                pickupLocation = detected
            }
        }
    }

    val isTrackingActive = activeBooking != null && activeBooking.serviceType == "PARCEL"

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(RideBackgroundLight)
    ) {
        // Top Bar
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
                    text = if (isTrackingActive) "Live Parcel Tracking" else "Book a Parcel",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        if (isTrackingActive && activeBooking != null) {
            // Section 34 & 35: 8-Step Timeline Tracking with Critical OTP Verification Rule
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
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
                                    Text(
                                        text = "Tracking ID: ${activeBooking.id}",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = RideDeepNavy
                                    )
                                    Text(
                                        text = "Category: ${activeBooking.vehicleOrItemType}",
                                        fontSize = 12.sp,
                                        color = RideTextSecondary
                                    )
                                }
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = RideElectricLime
                                ) {
                                    Text(
                                        text = activeBooking.status,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = RideDeepNavy,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = RideBorderLight)

                            Text(
                                text = "Receiver: ${activeBooking.receiverName} (${activeBooking.receiverPhone})",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = RideTextPrimary
                            )
                            Text(
                                text = "Delivery To: ${activeBooking.dropoffLocation}",
                                fontSize = 12.sp,
                                color = RideTextSecondary
                            )
                        }
                    }
                }

                // 8-Step Timeline
                item {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = RideSurfaceLight),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Parcel Progress Timeline",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = RideDeepNavy
                            )
                            Spacer(modifier = Modifier.height(14.dp))

                            val steps = listOf(
                                Pair("Driver Assigned", "Kalyan Babu accepted parcel request"),
                                Pair("Driver Reaching Pickup", "Courier heading to pickup location"),
                                Pair("Driver Arrived at Pickup", "Courier at your gate"),
                                Pair("Parcel Received", "Package securely collected"),
                                Pair("On the Way", "In transit to destination"),
                                Pair("Location Arrived", "Reached receiver address"),
                                Pair("Receiver OTP Verification", "Receiver provides delivery OTP"),
                                Pair("Parcel Delivered", "Verified & handed over")
                            )

                            val currentStepIndex = when (activeBooking.status) {
                                "SEARCHING" -> 0
                                "ACCEPTED" -> 1
                                "DRIVER_ARRIVING" -> 2
                                "ARRIVED" -> 3
                                "IN_TRANSIT" -> 4
                                "OTP_PENDING" -> 6
                                "COMPLETED" -> 7
                                else -> 4
                            }

                            steps.forEachIndexed { index, (title, desc) ->
                                val isDone = index < currentStepIndex
                                val isCurrent = index == currentStepIndex
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Box(
                                            modifier = Modifier
                                                .size(24.dp)
                                                .clip(CircleShape)
                                                .background(
                                                    if (isDone) RideSuccess
                                                    else if (isCurrent) RideElectricLime
                                                    else RideBorderLight
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            if (isDone) {
                                                Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                                            } else {
                                                Text(
                                                    text = "${index + 1}",
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (isCurrent) RideDeepNavy else RideTextMuted
                                                )
                                            }
                                        }
                                        if (index < steps.size - 1) {
                                            Box(
                                                modifier = Modifier
                                                    .width(2.dp)
                                                    .height(26.dp)
                                                    .background(if (isDone) RideSuccess else RideBorderLight)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.padding(bottom = 8.dp)) {
                                        Text(
                                            text = title,
                                            fontSize = 13.sp,
                                            fontWeight = if (isCurrent || isDone) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isCurrent) RideDeepNavy else if (isDone) RideTextPrimary else RideTextMuted
                                        )
                                        Text(
                                            text = desc,
                                            fontSize = 11.sp,
                                            color = if (isCurrent) RideTextSecondary else RideTextMuted
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Critical OTP Verification Rule Box
                item {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = RideNavySurface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Lock, contentDescription = null, tint = RideElectricLime)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Critical Delivery OTP Rule",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "As per platform safety, parcel delivery is NEVER completed until the receiver confirms the 4-digit Delivery OTP.",
                                fontSize = 12.sp,
                                color = RideTextMuted
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = RideDeepNavy,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(text = "Receiver's OTP", fontSize = 11.sp, color = RideTextMuted)
                                        Text(
                                            text = activeBooking.otp,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Black,
                                            color = RideElectricLime,
                                            letterSpacing = 2.sp
                                        )
                                    }

                                    Button(
                                        onClick = {
                                            val verified = onVerifyOtp(activeBooking.otp)
                                            if (!verified) otpError = true
                                        },
                                        shape = RoundedCornerShape(10.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = RideElectricLime,
                                            contentColor = RideDeepNavy
                                        ),
                                        modifier = Modifier.testTag("verify_parcel_otp_button")
                                    ) {
                                        Text("Verify & Complete", fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // New Parcel Booking Form
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Pickup & Drop
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
                                Text(
                                    text = "Pickup & Delivery Location",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = RideDeepNavy
                                )
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = com.example.ui.theme.PolishIndigo50,
                                    modifier = Modifier.clickable {
                                        locationPermissionLauncher.launch(
                                            arrayOf(
                                                Manifest.permission.ACCESS_FINE_LOCATION,
                                                Manifest.permission.ACCESS_COARSE_LOCATION
                                            )
                                        )
                                        onDetectLocation { detected ->
                                            pickupLocation = detected
                                        }
                                    }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.MyLocation,
                                            contentDescription = "Detect Location",
                                            tint = RideDeepNavy,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "Use GPS",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = RideDeepNavy
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                value = pickupLocation,
                                onValueChange = { pickupLocation = it },
                                label = { Text("Pickup Location", fontWeight = FontWeight.Medium) },
                                leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = com.example.ui.theme.PolishEmerald500) },
                                trailingIcon = {
                                    IconButton(onClick = {
                                        locationPermissionLauncher.launch(
                                            arrayOf(
                                                Manifest.permission.ACCESS_FINE_LOCATION,
                                                Manifest.permission.ACCESS_COARSE_LOCATION
                                            )
                                        )
                                        onDetectLocation { detected ->
                                            pickupLocation = detected
                                        }
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.MyLocation,
                                            contentDescription = "Detect Pickup Point",
                                            tint = RideDeepNavy
                                        )
                                    }
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = com.example.ui.theme.PolishSlate900,
                                    unfocusedTextColor = com.example.ui.theme.PolishSlate900,
                                    focusedLabelColor = RideDeepNavy,
                                    unfocusedLabelColor = com.example.ui.theme.PolishSlate700
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("parcel_pickup_input")
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                value = dropLocation,
                                onValueChange = { dropLocation = it },
                                label = { Text("Delivery Location", fontWeight = FontWeight.Medium) },
                                leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = RideDeepNavy) },
                                trailingIcon = {
                                    IconButton(onClick = {
                                        locationPermissionLauncher.launch(
                                            arrayOf(
                                                Manifest.permission.ACCESS_FINE_LOCATION,
                                                Manifest.permission.ACCESS_COARSE_LOCATION
                                            )
                                        )
                                        onDetectLocation { detected ->
                                            dropLocation = detected
                                        }
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.MyLocation,
                                            contentDescription = "Detect Delivery Point",
                                            tint = RideDeepNavy
                                        )
                                    }
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = com.example.ui.theme.PolishSlate900,
                                    unfocusedTextColor = com.example.ui.theme.PolishSlate900,
                                    focusedLabelColor = RideDeepNavy,
                                    unfocusedLabelColor = com.example.ui.theme.PolishSlate700
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("parcel_drop_input")
                            )
                        }
                    }
                }

                // Receiver Details
                item {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = RideSurfaceLight),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Receiver Details",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = RideDeepNavy
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                value = receiverName,
                                onValueChange = { receiverName = it },
                                label = { Text("Receiver Full Name") },
                                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = RideDeepNavy) },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("receiver_name_input")
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                value = receiverPhone,
                                onValueChange = { receiverPhone = it },
                                label = { Text("Receiver Mobile Number") },
                                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = RideDeepNavy) },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("receiver_phone_input")
                            )
                        }
                    }
                }

                // Parcel Category Cards (Section 32)
                item {
                    Text(
                        text = "Parcel Type",
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
                        categories.forEach { category ->
                            val isSelected = category.id == selectedCategoryId
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = if (isSelected) RideDeepNavy else RideSurfaceLight,
                                border = androidx.compose.foundation.BorderStroke(
                                    if (isSelected) 2.dp else 1.dp,
                                    if (isSelected) RideElectricLime else RideBorderLight
                                ),
                                modifier = Modifier
                                    .clickable { onSelectCategory(category.id) }
                                    .testTag("parcel_cat_${category.id}")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = category.iconEmoji, fontSize = 20.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = category.name,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.White else RideTextPrimary
                                    )
                                }
                            }
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
                            Text("Estimated Fare Breakdown", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = RideDeepNavy)
                            Spacer(modifier = Modifier.height(6.dp))
                            FareRow("Base Charge", "₹35")
                            FareRow("Distance Charge (3.8 km)", "₹20")
                            FareRow("Platform Fee & GST", "₹10")
                            HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp), color = RideBorderLight)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Total Estimated Fare", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = RideDeepNavy)
                                Text("₹65", fontWeight = FontWeight.Black, fontSize = 16.sp, color = RideDeepNavy)
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
                        onClick = { onConfirmParcel(pickupLocation, dropLocation, receiverName, receiverPhone) },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = RideDeepNavy,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("book_parcel_button")
                    ) {
                        Text(
                            text = "Book Parcel Courier (₹65)",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
