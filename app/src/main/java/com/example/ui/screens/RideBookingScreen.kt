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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.SwapVert
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
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.model.VehicleOption
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

@Composable
fun RideBookingScreen(
    vehicleOptions: List<VehicleOption>,
    selectedVehicleId: String,
    isGirlRiderAvailable: Boolean,
    onSelectVehicle: (String) -> Unit,
    onToggleGirlRider: () -> Unit,
    onConfirmRide: (String, String) -> Unit,
    onBack: () -> Unit,
    initialPickupLocation: String = "Current Location (Ongole Central)",
    initialDropLocation: String = "Ongole Railway Station (OGL)",
    onOpenMap: () -> Unit = {},
    onDetectLocation: ((String) -> Unit) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var pickupLocation by remember(initialPickupLocation) { mutableStateOf(initialPickupLocation) }
    var dropLocation by remember(initialDropLocation) { mutableStateOf(initialDropLocation) }
    var isDetectingPickupLocation by remember { mutableStateOf(false) }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) {
            isDetectingPickupLocation = true
            onDetectLocation { detected ->
                pickupLocation = detected
                isDetectingPickupLocation = false
            }
        }
    }

    val selectedOption = vehicleOptions.find { it.id == selectedVehicleId } ?: vehicleOptions.first()

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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = com.example.ui.theme.PolishSlate800
                    )
                }
                Column {
                    Text(
                        text = "BOOK A RIDE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = com.example.ui.theme.PolishIndigo600,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "Choose Your Option",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = com.example.ui.theme.PolishSlate900
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Location Selection Card with GPS Detection Handler
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = com.example.ui.theme.PolishSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, com.example.ui.theme.PolishBorder),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Pickup & Drop Location",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = com.example.ui.theme.PolishSlate900
                            )
                            // GPS Detect Button
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
                                        contentDescription = "Detect GPS",
                                        tint = com.example.ui.theme.PolishIndigo600,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (isDetectingPickupLocation) "Detecting..." else "Use GPS",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = com.example.ui.theme.PolishIndigo600
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Pickup Field
                        OutlinedTextField(
                            value = pickupLocation,
                            onValueChange = { pickupLocation = it },
                            label = { Text("Pickup Location", fontWeight = FontWeight.Medium) },
                            leadingIcon = {
                                Icon(Icons.Default.LocationOn, contentDescription = null, tint = com.example.ui.theme.PolishEmerald500)
                            },
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
                                        contentDescription = "Current Location",
                                        tint = com.example.ui.theme.PolishIndigo600
                                    )
                                }
                            },
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = com.example.ui.theme.PolishIndigo600,
                                unfocusedBorderColor = com.example.ui.theme.PolishBorderSubtle,
                                focusedContainerColor = com.example.ui.theme.PolishSlate50,
                                unfocusedContainerColor = com.example.ui.theme.PolishSlate50,
                                focusedTextColor = com.example.ui.theme.PolishSlate900,
                                unfocusedTextColor = com.example.ui.theme.PolishSlate900,
                                focusedLabelColor = com.example.ui.theme.PolishIndigo600,
                                unfocusedLabelColor = com.example.ui.theme.PolishSlate700
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("ride_pickup_input")
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Drop Field
                        OutlinedTextField(
                            value = dropLocation,
                            onValueChange = { dropLocation = it },
                            label = { Text("Drop Destination", fontWeight = FontWeight.Medium) },
                            leadingIcon = {
                                Icon(Icons.Default.Navigation, contentDescription = null, tint = com.example.ui.theme.PolishRose600)
                            },
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = com.example.ui.theme.PolishIndigo600,
                                unfocusedBorderColor = com.example.ui.theme.PolishBorderSubtle,
                                focusedContainerColor = com.example.ui.theme.PolishSlate50,
                                unfocusedContainerColor = com.example.ui.theme.PolishSlate50,
                                focusedTextColor = com.example.ui.theme.PolishSlate900,
                                unfocusedTextColor = com.example.ui.theme.PolishSlate900,
                                focusedLabelColor = com.example.ui.theme.PolishIndigo600,
                                unfocusedLabelColor = com.example.ui.theme.PolishSlate700
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("ride_drop_input")
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Interactive Google Map Picker Button
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = com.example.ui.theme.PolishIndigo50,
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFC7D2FE)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(onClick = onOpenMap)
                                .testTag("set_on_google_map_card")
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Map,
                                        contentDescription = null,
                                        tint = com.example.ui.theme.PolishIndigo600,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = "Set Markers on Google Map",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = com.example.ui.theme.PolishSlate900
                                        )
                                        Text(
                                            text = "Tap or drag pins to pinpoint exact pickup & drop-off",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = com.example.ui.theme.PolishSlate600
                                        )
                                    }
                                }
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = "Open Map",
                                    tint = com.example.ui.theme.PolishIndigo600,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Girl Rider Dynamic Availability Showcase Switch (Section 15)
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF1F2)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFE4E6))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "👩", fontSize = 18.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Girl Rider Availability",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF9F1239)
                                )
                            }
                            Text(
                                text = if (isGirlRiderAvailable) "Eligible female rider is active nearby" else "No female riders currently available (Option hidden)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFFBE123C)
                            )
                        }
                        Switch(
                            checked = isGirlRiderAvailable,
                            onCheckedChange = { onToggleGirlRider() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = com.example.ui.theme.PolishRose600,
                                checkedTrackColor = Color(0xFFFECDD3)
                            )
                        )
                    }
                }
            }

            // Vehicle Selection
            item {
                Text(
                    text = "Select Vehicle",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = com.example.ui.theme.PolishSlate900
                )
            }

            items(vehicleOptions) { vehicle ->
                val isSelected = vehicle.id == selectedVehicleId
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) com.example.ui.theme.PolishIndigo50 else com.example.ui.theme.PolishSurface
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        if (isSelected) 2.dp else 1.dp,
                        if (isSelected) com.example.ui.theme.PolishIndigo600 else com.example.ui.theme.PolishBorder
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 2.dp else 0.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelectVehicle(vehicle.id) }
                        .testTag("vehicle_card_${vehicle.id}")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Vehicle Preview via Coil or Emoji Fallback
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (isSelected) com.example.ui.theme.PolishIndigo100 else com.example.ui.theme.PolishSlate100)
                                .border(1.dp, if (isSelected) com.example.ui.theme.PolishIndigo600 else com.example.ui.theme.PolishBorder, RoundedCornerShape(14.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            if (vehicle.imageUrl.isNotBlank()) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(vehicle.imageUrl)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = vehicle.name,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(14.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Text(text = vehicle.iconEmoji, fontSize = 24.sp)
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = vehicle.name,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = com.example.ui.theme.PolishSlate900
                                )
                                if (vehicle.isGirlRider) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = com.example.ui.theme.PolishRose600
                                    ) {
                                        Text(
                                            text = "WOMEN ONLY",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = "${vehicle.etaMinutes} min away • ${vehicle.description}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = com.example.ui.theme.PolishSlate700
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "₹${vehicle.fare.toInt()}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = if (isSelected) com.example.ui.theme.PolishIndigo600 else com.example.ui.theme.PolishSlate900
                            )
                            Text(
                                text = "Capacity: ${vehicle.capacity}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = com.example.ui.theme.PolishSlate700
                            )
                        }
                    }
                }
            }

            // Fare Calculation Breakdown (Section 16)
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = com.example.ui.theme.PolishSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, com.example.ui.theme.PolishBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Info, contentDescription = null, tint = com.example.ui.theme.PolishIndigo600, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Fare Calculation Breakdown",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = com.example.ui.theme.PolishSlate900
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        FareRow("Base Fare", "₹${(selectedOption.fare * 0.5).toInt()}")
                        FareRow("Distance Charge (4.2 km)", "₹${(selectedOption.fare * 0.3).toInt()}")
                        FareRow("Time Charge (12 mins)", "₹${(selectedOption.fare * 0.15).toInt()}")
                        FareRow("Platform Fee & Taxes", "₹10")
                        HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp), color = com.example.ui.theme.PolishBorder)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Estimated Fare", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = com.example.ui.theme.PolishSlate900)
                            Text("₹${selectedOption.fare.toInt()}", fontWeight = FontWeight.Black, fontSize = 16.sp, color = com.example.ui.theme.PolishSlate900)
                        }
                    }
                }
            }
        }

        // Bottom Fixed CTA
        Surface(
            color = com.example.ui.theme.PolishSurface,
            shadowElevation = 0.dp,
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, com.example.ui.theme.PolishBorder)
        ) {
            Box(modifier = Modifier.padding(16.dp)) {
                Button(
                    onClick = { onConfirmRide(pickupLocation, dropLocation) },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = com.example.ui.theme.PolishIndigo600,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("confirm_ride_button")
                ) {
                    Text(
                        text = "Confirm ${selectedOption.name} (₹${selectedOption.fare.toInt()})",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
