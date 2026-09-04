package com.example.ui.screens

import android.Manifest
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.ui.theme.PolishBorder
import com.example.ui.theme.PolishEmerald500
import com.example.ui.theme.PolishIndigo50
import com.example.ui.theme.PolishIndigo600
import com.example.ui.theme.PolishSlate100
import com.example.ui.theme.PolishSlate400
import com.example.ui.theme.PolishSlate500
import com.example.ui.theme.PolishSlate600
import com.example.ui.theme.PolishSlate700
import com.example.ui.theme.PolishSlate800
import com.example.ui.theme.PolishSlate900
import com.example.ui.theme.PolishSurface
import com.example.ui.theme.RideOrangeLight
import com.example.ui.theme.RideOrangePrimary
import com.example.viewmodel.RideOneViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.launch

enum class MapTargetMode {
    PICKUP,
    DROPOFF
}

private data class QuickLandmark(
    val name: String,
    val address: String,
    val latLng: LatLng
)

private val OngoleLandmarks = listOf(
    QuickLandmark("Central Bus Stand", "RTC Bus Stand, Ongole Central", LatLng(15.5057, 80.0499)),
    QuickLandmark("Railway Station", "Ongole Railway Station (OGL), Mastan Dargah Rd", LatLng(15.5180, 80.0380)),
    QuickLandmark("Lawyer Pet", "Lawyer Pet Main Road, Ongole", LatLng(15.5085, 80.0425)),
    QuickLandmark("Collectorate", "District Collector Office, Kurnool Rd", LatLng(15.5140, 80.0570)),
    QuickLandmark("RIMS Hospital", "GGH / RIMS Medical Campus, Ongole", LatLng(15.5220, 80.0610)),
    QuickLandmark("Kurnool Road", "Kurnool Bypass Junction, Ongole", LatLng(15.4980, 80.0390))
)

@Composable
fun MapScreen(
    viewModel: RideOneViewModel,
    onBack: (() -> Unit)? = null,
    onConfirmSelection: (pickup: String, dropoff: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val currentLocation by viewModel.currentLocationLatLng.collectAsState()
    val pickupLatLng by viewModel.pickupLatLng.collectAsState()
    val dropoffLatLng by viewModel.dropoffLatLng.collectAsState()
    val pickupAddress by viewModel.pickupAddress.collectAsState()
    val dropoffAddress by viewModel.dropoffAddress.collectAsState()

    var activeMode by remember { mutableStateOf(MapTargetMode.PICKUP) }
    var isLocatingUser by remember { mutableStateOf(false) }
    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        )
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        hasPermission = granted
        if (granted) {
            isLocatingUser = true
            viewModel.detectLocation(
                onSuccess = {
                    isLocatingUser = false
                },
                onError = {
                    isLocatingUser = false
                }
            )
        }
    }

    // Google Maps Camera Position State
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(pickupLatLng, 14.5f)
    }

    // Marker States
    val pickupMarkerState = rememberMarkerState(position = pickupLatLng)
    val dropoffMarkerState = rememberMarkerState(position = dropoffLatLng)

    // Sync marker states with ViewModel updates
    LaunchedEffect(pickupLatLng) {
        pickupMarkerState.position = pickupLatLng
    }
    LaunchedEffect(dropoffLatLng) {
        dropoffMarkerState.position = dropoffLatLng
    }

    // Handle marker drags
    LaunchedEffect(pickupMarkerState.position) {
        if (pickupMarkerState.position != pickupLatLng) {
            viewModel.setPickup(pickupMarkerState.position)
        }
    }
    LaunchedEffect(dropoffMarkerState.position) {
        if (dropoffMarkerState.position != dropoffLatLng) {
            viewModel.setDropoff(dropoffMarkerState.position)
        }
    }

    val distanceKm = remember(pickupLatLng, dropoffLatLng) {
        viewModel.calculateRouteDistanceKm()
    }
    val etaMinutes = remember(distanceKm) {
        viewModel.calculateEstimatedMinutes()
    }

    Box(modifier = modifier.fillMaxSize()) {
        // Real Google Maps View
        GoogleMap(
            modifier = Modifier
                .fillMaxSize()
                .testTag("google_map_view"),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                isMyLocationEnabled = hasPermission,
                mapType = MapType.NORMAL,
                isTrafficEnabled = true
            ),
            uiSettings = MapUiSettings(
                myLocationButtonEnabled = false,
                zoomControlsEnabled = false,
                compassEnabled = true,
                mapToolbarEnabled = false
            ),
            onMapClick = { clickedLatLng ->
                if (activeMode == MapTargetMode.PICKUP) {
                    viewModel.setPickup(clickedLatLng)
                } else {
                    viewModel.setDropoff(clickedLatLng)
                }
            }
        ) {
            // Pickup Marker (Green Pin)
            Marker(
                state = pickupMarkerState,
                title = "Pickup Location",
                snippet = pickupAddress,
                draggable = true,
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
            )

            // Drop-off Marker (Red/Orange Pin)
            Marker(
                state = dropoffMarkerState,
                title = "Drop-off Location",
                snippet = dropoffAddress,
                draggable = true,
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
            )

            // Dynamic Route Polyline
            Polyline(
                points = listOf(pickupLatLng, dropoffLatLng),
                color = RideOrangePrimary,
                width = 10f
            )

            // Ambient Nearby Vehicles for Map Realism
            Marker(
                state = rememberMarkerState(
                    position = LatLng(pickupLatLng.latitude + 0.003, pickupLatLng.longitude + 0.002)
                ),
                title = "Nearby Bike (3m away)",
                snippet = "Driver: Suresh (Hero Splendor)",
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)
            )
            Marker(
                state = rememberMarkerState(
                    position = LatLng(pickupLatLng.latitude - 0.002, pickupLatLng.longitude + 0.004)
                ),
                title = "Nearby Auto (4m away)",
                snippet = "Driver: Ravi (Bajaj Compact)",
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)
            )
        }

        // Top Control Overlay: Header & Mode Selector Tabs
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(16.dp)
        ) {
            // Top Bar Card
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = PolishSurface.copy(alpha = 0.96f),
                shadowElevation = 6.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, PolishBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (onBack != null) {
                                IconButton(onClick = onBack, modifier = Modifier.size(36.dp)) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Back",
                                        tint = PolishSlate800
                                    )
                                }
                                Spacer(modifier = Modifier.width(4.dp))
                            }
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Map,
                                        contentDescription = null,
                                        tint = RideOrangePrimary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "GOOGLE MAPS SDK",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = RideOrangePrimary,
                                        letterSpacing = 0.5.sp
                                    )
                                }
                                Text(
                                    text = "Tap Map to Place Marker",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PolishSlate900
                                )
                            }
                        }

                        // SDK Live Badge
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFDCFCE7),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBBF7D0))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF16A34A))
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Live GPS",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF15803D)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Mode Toggle: Pickup vs Dropoff
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Pickup Tab
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (activeMode == MapTargetMode.PICKUP) PolishEmerald500 else PolishSlate100,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { activeMode = MapTargetMode.PICKUP }
                        ) {
                            Row(
                                modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Place,
                                    contentDescription = null,
                                    tint = if (activeMode == MapTargetMode.PICKUP) Color.White else PolishSlate700,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Set Pickup",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (activeMode == MapTargetMode.PICKUP) Color.White else PolishSlate800
                                )
                            }
                        }

                        // Dropoff Tab
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (activeMode == MapTargetMode.DROPOFF) RideOrangePrimary else PolishSlate100,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { activeMode = MapTargetMode.DROPOFF }
                        ) {
                            Row(
                                modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = if (activeMode == MapTargetMode.DROPOFF) Color.White else PolishSlate700,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Set Drop-off",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (activeMode == MapTargetMode.DROPOFF) Color.White else PolishSlate800
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Quick Landmarks Carousel
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OngoleLandmarks.forEach { landmark ->
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color.White.copy(alpha = 0.95f),
                        shadowElevation = 3.dp,
                        border = androidx.compose.foundation.BorderStroke(1.dp, PolishBorder),
                        modifier = Modifier.clickable {
                            if (activeMode == MapTargetMode.PICKUP) {
                                viewModel.setPickup(landmark.latLng, landmark.address)
                            } else {
                                viewModel.setDropoff(landmark.latLng, landmark.address)
                            }
                            coroutineScope.launch {
                                cameraPositionState.animate(
                                    CameraUpdateFactory.newLatLngZoom(landmark.latLng, 15.5f),
                                    800
                                )
                            }
                        }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (activeMode == MapTargetMode.PICKUP) "🟢" else "🔴",
                                fontSize = 10.sp
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = landmark.name,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = PolishSlate800
                            )
                        }
                    }
                }
            }
        }

        // Floating Action Buttons on Right (Fit Bounds, Swap, GPS Center)
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Fit route bounds button
            FloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        try {
                            val builder = LatLngBounds.builder()
                            builder.include(pickupLatLng)
                            builder.include(dropoffLatLng)
                            val bounds = builder.build()
                            cameraPositionState.animate(
                                CameraUpdateFactory.newLatLngBounds(bounds, 120),
                                800
                            )
                        } catch (e: Exception) {
                            cameraPositionState.animate(
                                CameraUpdateFactory.newLatLngZoom(pickupLatLng, 14f),
                                800
                            )
                        }
                    }
                },
                containerColor = Color.White,
                contentColor = PolishSlate800,
                shape = CircleShape,
                modifier = Modifier
                    .size(44.dp)
                    .testTag("fit_route_button")
            ) {
                Icon(
                    imageVector = Icons.Default.NearMe,
                    contentDescription = "Fit Route",
                    modifier = Modifier.size(20.dp)
                )
            }

            // Swap pickup and drop-off
            FloatingActionButton(
                onClick = {
                    viewModel.swapPickupAndDropoff()
                },
                containerColor = Color.White,
                contentColor = PolishSlate800,
                shape = CircleShape,
                modifier = Modifier
                    .size(44.dp)
                    .testTag("swap_locations_button")
            ) {
                Icon(
                    imageVector = Icons.Default.SwapVert,
                    contentDescription = "Swap Locations",
                    modifier = Modifier.size(20.dp)
                )
            }

            // Current Location GPS Button
            FloatingActionButton(
                onClick = {
                    if (!hasPermission) {
                        locationPermissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    } else {
                        isLocatingUser = true
                        viewModel.detectLocation(
                            onSuccess = {
                                isLocatingUser = false
                                coroutineScope.launch {
                                    cameraPositionState.animate(
                                        CameraUpdateFactory.newLatLngZoom(currentLocation, 16f),
                                        800
                                    )
                                }
                            },
                            onError = {
                                isLocatingUser = false
                            }
                        )
                    }
                },
                containerColor = Color.White,
                contentColor = if (isLocatingUser) RideOrangePrimary else PolishIndigo600,
                shape = CircleShape,
                modifier = Modifier
                    .size(48.dp)
                    .testTag("gps_locate_button")
            ) {
                if (isLocatingUser) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        strokeWidth = 2.dp,
                        color = RideOrangePrimary
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.MyLocation,
                        contentDescription = "Locate Me",
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }

        // Bottom Card: Locations Summary & Confirm Button
        Surface(
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = PolishSurface,
            shadowElevation = 16.dp,
            border = androidx.compose.foundation.BorderStroke(1.dp, PolishBorder),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                // Pickup Point Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            activeMode = MapTargetMode.PICKUP
                            coroutineScope.launch {
                                cameraPositionState.animate(
                                    CameraUpdateFactory.newLatLngZoom(pickupLatLng, 15.5f),
                                    600
                                )
                            }
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFDCFCE7)),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(PolishEmerald500)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "PICKUP LOCATION",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = PolishEmerald500,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = pickupAddress,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = PolishSlate900,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Drop-off Point Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            activeMode = MapTargetMode.DROPOFF
                            coroutineScope.launch {
                                cameraPositionState.animate(
                                    CameraUpdateFactory.newLatLngZoom(dropoffLatLng, 15.5f),
                                    600
                                )
                            }
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFEDD5)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = RideOrangePrimary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "DROP-OFF LOCATION",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = RideOrangePrimary,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = dropoffAddress,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = PolishSlate900,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Distance & Travel Stats Pill
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = PolishSlate100,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.DirectionsCar,
                                contentDescription = null,
                                tint = PolishSlate700,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Estimated Distance:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = PolishSlate700
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "$distanceKm km",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = PolishSlate900
                            )
                        }

                        Text(
                            text = "~$etaMinutes mins",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = RideOrangePrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Confirm & Apply Button
                Button(
                    onClick = {
                        onConfirmSelection(pickupAddress, dropoffAddress)
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = RideOrangePrimary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("confirm_map_locations_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Confirm Locations & Proceed",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
