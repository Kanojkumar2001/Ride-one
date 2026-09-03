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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SignalCellularConnectedNoInternet0Bar
import androidx.compose.material.icons.filled.Train
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.data.auth.AuthProfile
import com.example.data.model.EmergencyContactEntity
import com.example.data.model.SavedPlaceEntity
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

@Composable
fun ProfileScreen(
    emergencyContacts: List<EmergencyContactEntity>,
    savedPlaces: List<SavedPlaceEntity>,
    isOfflineMode: Boolean,
    onToggleOfflineMode: () -> Unit,
    onAddEmergencyContact: (String, String, String) -> Unit,
    onDeleteEmergencyContact: (Int) -> Unit,
    onAddSavedPlace: (String, String) -> Unit,
    onDeleteSavedPlace: (Int) -> Unit,
    authProfile: AuthProfile? = null,
    authError: String? = null,
    onSignIn: (String, String, (Boolean, String?) -> Unit) -> Unit = { _, _, _ -> },
    onSignUp: (String, String, String, (Boolean, String?) -> Unit) -> Unit = { _, _, _, _ -> },
    onSignInAnonymously: ((Boolean, String?) -> Unit) -> Unit = {},
    onSignOut: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showAddContactDialog by remember { mutableStateOf(false) }
    var showAddPlaceDialog by remember { mutableStateOf(false) }
    var showAuthDialog by remember { mutableStateOf(false) }

    var authIsSignUp by remember { mutableStateOf(false) }
    var authEmailInput by remember { mutableStateOf("") }
    var authPassInput by remember { mutableStateOf("") }
    var authNameInput by remember { mutableStateOf("") }
    var authLocalMsg by remember { mutableStateOf<String?>(null) }

    var newContactName by remember { mutableStateOf("") }
    var newContactPhone by remember { mutableStateOf("") }
    var newContactRel by remember { mutableStateOf("Family") }

    var newPlaceTitle by remember { mutableStateOf("") }
    var newPlaceAddress by remember { mutableStateOf("") }

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
                    text = "ACCOUNT",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = com.example.ui.theme.PolishIndigo600,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Profile & Settings",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = com.example.ui.theme.PolishSlate900
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Manage safety contacts, saved places, and offline settings",
                    fontSize = 12.sp,
                    color = com.example.ui.theme.PolishSlate500
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // User Card & Authentication Profile
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = com.example.ui.theme.PolishSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, com.example.ui.theme.PolishBorder),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val photoUrl = authProfile?.photoUrl
                            if (!photoUrl.isNullOrBlank()) {
                                AsyncImage(
                                    model = ImageRequest.Builder(context)
                                        .data(photoUrl)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = "User Avatar",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(56.dp)
                                        .clip(CircleShape)
                                        .border(2.dp, com.example.ui.theme.PolishIndigo600, CircleShape)
                                )
                            } else {
                                val initials = if (authProfile?.displayName?.isNotBlank() == true) {
                                    authProfile.displayName.split(" ")
                                        .take(2)
                                        .mapNotNull { it.firstOrNull()?.uppercaseChar() }
                                        .joinToString("")
                                } else "AK"
                                Box(
                                    modifier = Modifier
                                        .size(56.dp)
                                        .clip(CircleShape)
                                        .background(com.example.ui.theme.PolishIndigo600),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = initials.ifEmpty { "U" },
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = authProfile?.displayName ?: "Arun Kumar",
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = com.example.ui.theme.PolishSlate900
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = if (authProfile?.isAuthenticated == true) com.example.ui.theme.PolishEmerald50 else com.example.ui.theme.PolishIndigo50
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                Icons.Default.VerifiedUser,
                                                contentDescription = null,
                                                tint = if (authProfile?.isAuthenticated == true) com.example.ui.theme.PolishEmerald600 else com.example.ui.theme.PolishIndigo600,
                                                modifier = Modifier.size(12.dp)
                                            )
                                            Spacer(modifier = Modifier.width(2.dp))
                                            Text(
                                                text = if (authProfile?.isAnonymous == true) "Guest Session" else if (authProfile?.isAuthenticated == true) "Firebase Verified" else "Demo Account",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (authProfile?.isAuthenticated == true) com.example.ui.theme.PolishEmerald600 else com.example.ui.theme.PolishIndigo600
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = authProfile?.email ?: "arun.kumar@gmail.com",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = com.example.ui.theme.PolishSlate700
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(color = com.example.ui.theme.PolishBorderSubtle)
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Firebase Auth & Credential Sync",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = com.example.ui.theme.PolishIndigo700
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                if (authProfile?.isAuthenticated == true) {
                                    TextButton(
                                        onClick = { onSignOut() },
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(
                                            text = "Sign Out",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = com.example.ui.theme.RideSOSRed
                                        )
                                    }
                                }
                                Button(
                                    onClick = { showAuthDialog = true },
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = com.example.ui.theme.PolishIndigo600,
                                        contentColor = Color.White
                                    )
                                ) {
                                    Text(
                                        text = if (authProfile?.isAuthenticated == true) "Switch Account" else "Sign In",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Offline Capabilities Toggle (Mandated requirement)
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = if (isOfflineMode) com.example.ui.theme.PolishIndigo50 else com.example.ui.theme.PolishSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isOfflineMode) com.example.ui.theme.PolishIndigo600 else com.example.ui.theme.PolishBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isOfflineMode) com.example.ui.theme.PolishIndigo100 else com.example.ui.theme.PolishSlate100),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SignalCellularConnectedNoInternet0Bar,
                                    contentDescription = null,
                                    tint = if (isOfflineMode) com.example.ui.theme.PolishIndigo600 else com.example.ui.theme.PolishSlate700
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Offline Mode Support",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = com.example.ui.theme.PolishSlate900
                                )
                                Text(
                                    text = if (isOfflineMode) "Active: Powered by Room Database local cache" else "Toggle to test offline caching & local Room storage",
                                    fontSize = 11.sp,
                                    color = com.example.ui.theme.PolishSlate500
                                )
                            }
                        }

                        Switch(
                            checked = isOfflineMode,
                            onCheckedChange = { onToggleOfflineMode() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = com.example.ui.theme.PolishIndigo600
                            ),
                            modifier = Modifier.testTag("offline_toggle_switch")
                        )
                    }
                }
            }

            // Emergency Contacts Section (Section 24 & 25: Mother, Father, Friend, Spouse, etc.)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Emergency Contacts",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = com.example.ui.theme.PolishSlate900
                        )
                        Text(
                            text = "Alerted during active ride SOS incidents",
                            fontSize = 11.sp,
                            color = com.example.ui.theme.PolishSlate500
                        )
                    }
                    Button(
                        onClick = { showAddContactDialog = true },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = com.example.ui.theme.PolishIndigo600, contentColor = Color.White),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Add", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            items(emergencyContacts) { contact ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = com.example.ui.theme.PolishSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, com.example.ui.theme.PolishBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFFF1F2)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "🛡️", fontSize = 18.sp)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "${contact.name} (${contact.relationship})",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = com.example.ui.theme.PolishSlate900
                            )
                            Text(text = contact.phone, fontSize = 12.sp, color = com.example.ui.theme.PolishSlate500)
                        }
                        IconButton(onClick = { onDeleteEmergencyContact(contact.id) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = com.example.ui.theme.PolishSlate400, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }

            // Saved Places Section (Section 53: Home, Work, Station, Hospital, Airport)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Saved Places",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = com.example.ui.theme.PolishSlate900
                        )
                        Text(
                            text = "Quick 1-tap booking destinations",
                            fontSize = 11.sp,
                            color = com.example.ui.theme.PolishSlate500
                        )
                    }
                    Button(
                        onClick = { showAddPlaceDialog = true },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = com.example.ui.theme.PolishIndigo600, contentColor = Color.White),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Add", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            items(savedPlaces) { place ->
                val placeIcon = when (place.iconName) {
                    "home" -> Icons.Default.Home
                    "work" -> Icons.Default.Work
                    "train" -> Icons.Default.Train
                    "local_hospital" -> Icons.Default.LocalHospital
                    else -> Icons.Default.DirectionsBus
                }
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = com.example.ui.theme.PolishSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, com.example.ui.theme.PolishBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(com.example.ui.theme.PolishIndigo50),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(placeIcon, contentDescription = null, tint = com.example.ui.theme.PolishIndigo600, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = place.title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = com.example.ui.theme.PolishSlate900)
                            Text(text = place.address, fontSize = 11.sp, color = com.example.ui.theme.PolishSlate500, maxLines = 1)
                        }
                        IconButton(onClick = { onDeleteSavedPlace(place.id) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = com.example.ui.theme.PolishSlate400, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }

            // Payment Methods Card
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = com.example.ui.theme.PolishSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, com.example.ui.theme.PolishBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CreditCard, contentDescription = null, tint = com.example.ui.theme.PolishIndigo600)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Payment Methods Configured", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = com.example.ui.theme.PolishSlate900)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "• UPI: arun@okaxis (Primary)\n• Cards: HDFC Visa ending in 9012\n• Cash: Enabled for dropoff settlement", fontSize = 12.sp, color = com.example.ui.theme.PolishSlate500, lineHeight = 16.sp)
                    }
                }
            }
        }
    }

    // Add Contact Dialog
    if (showAddContactDialog) {
        AlertDialog(
            onDismissRequest = { showAddContactDialog = false },
            title = { Text("Add Emergency Contact", fontWeight = FontWeight.Bold, color = com.example.ui.theme.PolishSlate900) },
            text = {
                Column {
                    OutlinedTextField(
                        value = newContactName,
                        onValueChange = { newContactName = it },
                        label = { Text("Contact Name") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newContactPhone,
                        onValueChange = { newContactPhone = it },
                        label = { Text("Phone Number") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newContactRel,
                        onValueChange = { newContactRel = it },
                        label = { Text("Relationship (Mother, Father, Spouse, Friend)") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newContactName.isNotBlank() && newContactPhone.isNotBlank()) {
                            onAddEmergencyContact(newContactName, newContactPhone, newContactRel)
                            showAddContactDialog = false
                            newContactName = ""
                            newContactPhone = ""
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = com.example.ui.theme.PolishIndigo600, contentColor = Color.White)
                ) {
                    Text("Save Contact", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddContactDialog = false }) {
                    Text("Cancel", color = com.example.ui.theme.PolishSlate500)
                }
            }
        )
    }

    // Add Place Dialog
    if (showAddPlaceDialog) {
        AlertDialog(
            onDismissRequest = { showAddPlaceDialog = false },
            title = { Text("Add Saved Place", fontWeight = FontWeight.Bold, color = com.example.ui.theme.PolishSlate900) },
            text = {
                Column {
                    OutlinedTextField(
                        value = newPlaceTitle,
                        onValueChange = { newPlaceTitle = it },
                        label = { Text("Place Name (e.g. Gym, Library)") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newPlaceAddress,
                        onValueChange = { newPlaceAddress = it },
                        label = { Text("Full Address / Area") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newPlaceTitle.isNotBlank() && newPlaceAddress.isNotBlank()) {
                            onAddSavedPlace(newPlaceTitle, newPlaceAddress)
                            showAddPlaceDialog = false
                            newPlaceTitle = ""
                            newPlaceAddress = ""
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = com.example.ui.theme.PolishIndigo600, contentColor = Color.White)
                ) {
                    Text("Save Place", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddPlaceDialog = false }) {
                    Text("Cancel", color = com.example.ui.theme.PolishSlate500)
                }
            }
        )
    }

    // Firebase Auth Login / Signup Dialog
    if (showAuthDialog) {
        AlertDialog(
            onDismissRequest = {
                showAuthDialog = false
                authLocalMsg = null
            },
            title = {
                Column {
                    Text(
                        text = if (authIsSignUp) "Create RideOne Account" else "Sign In to Platform",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = com.example.ui.theme.PolishSlate900
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Powered by Firebase Auth & Credential Manager",
                        fontSize = 11.sp,
                        color = com.example.ui.theme.PolishIndigo600,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Mode selector: Sign In vs Sign Up
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(com.example.ui.theme.PolishSlate100)
                            .padding(3.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (!authIsSignUp) com.example.ui.theme.PolishIndigo600 else Color.Transparent,
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    authIsSignUp = false
                                    authLocalMsg = null
                                }
                        ) {
                            Text(
                                text = "Sign In",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (!authIsSignUp) Color.White else com.example.ui.theme.PolishSlate700,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (authIsSignUp) com.example.ui.theme.PolishIndigo600 else Color.Transparent,
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    authIsSignUp = true
                                    authLocalMsg = null
                                }
                        ) {
                            Text(
                                text = "Sign Up",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (authIsSignUp) Color.White else com.example.ui.theme.PolishSlate700,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    if (authIsSignUp) {
                        OutlinedTextField(
                            value = authNameInput,
                            onValueChange = { authNameInput = it },
                            label = { Text("Full Name", fontWeight = FontWeight.Medium) },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("auth_name_input")
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    OutlinedTextField(
                        value = authEmailInput,
                        onValueChange = { authEmailInput = it },
                        label = { Text("Email Address", fontWeight = FontWeight.Medium) },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("auth_email_input")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = authPassInput,
                        onValueChange = { authPassInput = it },
                        label = { Text("Password", fontWeight = FontWeight.Medium) },
                        singleLine = true,
                        visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("auth_password_input")
                    )

                    val activeMsg = authLocalMsg ?: authError
                    if (activeMsg != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = activeMsg,
                            color = com.example.ui.theme.RideSOSRed,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Guest / Fast demo option
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = com.example.ui.theme.PolishSlate50,
                        border = androidx.compose.foundation.BorderStroke(1.dp, com.example.ui.theme.PolishBorder),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onSignInAnonymously { success, err ->
                                    if (success) {
                                        showAuthDialog = false
                                    } else {
                                        authLocalMsg = err ?: "Guest login failed"
                                    }
                                }
                            }
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.VerifiedUser,
                                contentDescription = null,
                                tint = com.example.ui.theme.PolishIndigo600,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Continue with Guest / Anonymous Login",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = com.example.ui.theme.PolishSlate800
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (authEmailInput.isBlank() || authPassInput.isBlank()) {
                            authLocalMsg = "Please enter email and password"
                            return@Button
                        }
                        if (authIsSignUp) {
                            val name = if (authNameInput.isNotBlank()) authNameInput else authEmailInput.substringBefore("@")
                            onSignUp(name, authEmailInput, authPassInput) { success, err ->
                                if (success) {
                                    showAuthDialog = false
                                    authLocalMsg = null
                                } else {
                                    authLocalMsg = err ?: "Signup failed"
                                }
                            }
                        } else {
                            onSignIn(authEmailInput, authPassInput) { success, err ->
                                if (success) {
                                    showAuthDialog = false
                                    authLocalMsg = null
                                } else {
                                    authLocalMsg = err ?: "Invalid credentials"
                                }
                            }
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = com.example.ui.theme.PolishIndigo600,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.testTag("auth_submit_btn")
                ) {
                    Text(
                        text = if (authIsSignUp) "Create Account" else "Sign In",
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showAuthDialog = false
                    authLocalMsg = null
                }) {
                    Text("Cancel", color = com.example.ui.theme.PolishSlate600)
                }
            }
        )
    }
}
