package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.model.BookingEntity
import com.example.data.model.driverPhotoUrl
import com.example.ui.theme.RideBackgroundLight
import com.example.ui.theme.RideBorderLight
import com.example.ui.theme.RideDeepNavy
import com.example.ui.theme.RideElectricLime
import com.example.ui.theme.RideNavyElevated
import com.example.ui.theme.RideNavySurface
import com.example.ui.theme.RideSOSRed
import com.example.ui.theme.RideSOSRedContainer
import com.example.ui.theme.RideSOSRedDark
import com.example.ui.theme.RideSuccess
import com.example.ui.theme.RideSurfaceLight
import com.example.ui.theme.RideTextMuted
import com.example.ui.theme.RideTextPrimary
import com.example.ui.theme.RideTextSecondary
import com.example.ui.theme.RideWarning

@Composable
fun ActiveBookingCard(
    booking: BookingEntity,
    onVerifyOtp: () -> Unit,
    onCompleteBooking: () -> Unit,
    onCancelBooking: () -> Unit,
    onSosClick: () -> Unit,
    onSimulate15MinWarning: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = RideDeepNavy),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("active_booking_card")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // Header Row: Status & Service Type Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(
                                when (booking.status) {
                                    "ACTIVE", "IN_TRANSIT", "ARRIVED" -> com.example.ui.theme.PolishEmerald500
                                    else -> com.example.ui.theme.PolishAmber500
                                }
                            )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = when (booking.status) {
                            "SEARCHING" -> "Finding nearby drivers..."
                            "ACCEPTED" -> "Driver Assigned"
                            "DRIVER_ARRIVING" -> "Driver is Arriving (ETA ${booking.driverEtaMinutes}m)"
                            "ARRIVED" -> "Driver Has Arrived!"
                            "ACTIVE" -> "Ride in Progress"
                            "IN_TRANSIT" -> "Parcel in Transit"
                            "OTP_PENDING" -> "At Destination • Receiver OTP Pending"
                            else -> booking.status
                        },
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                // SOS Trigger Button (Active during active ride / booking)
                Button(
                    onClick = onSosClick,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = com.example.ui.theme.PolishRose600,
                        contentColor = Color.White
                    ),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.testTag("sos_button")
                ) {
                    Text(
                        text = "🔴 SOS",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Driver Details Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(alpha = 0.12f))
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Driver Avatar with Coil Image Loading
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.25f))
                        .border(1.5.dp, Color.White.copy(alpha = 0.85f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(booking.driverPhotoUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Driver ${booking.driverName}",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = booking.driverName,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color.White.copy(alpha = 0.25f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Rating",
                                    tint = com.example.ui.theme.PolishAmber500,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = "${booking.driverRating}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${booking.driverVehicle} • ${booking.driverVehicleNumber}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White.copy(alpha = 0.95f)
                    )
                }

                // OTP Display Badge (Prominently styled)
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White,
                    modifier = Modifier.padding(start = 6.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "OTP",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = com.example.ui.theme.PolishIndigo600
                        )
                        Text(
                            text = booking.otp,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = com.example.ui.theme.PolishIndigo600,
                            letterSpacing = 1.5.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Route & Details
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Pickup: ${booking.pickupLocation}",
                        fontSize = 12.sp,
                        color = Color.White,
                        maxLines = 1
                    )
                    Text(
                        text = "Drop: ${booking.dropoffLocation}",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.8f),
                        maxLines = 1
                    )
                }
                Text(
                    text = "₹${booking.fare.toInt()}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (booking.status == "ARRIVED" || booking.status == "ACCEPTED" || booking.status == "DRIVER_ARRIVING") {
                    Button(
                        onClick = onVerifyOtp,
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = com.example.ui.theme.PolishIndigo600
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = if (booking.status == "ARRIVED") "Verify OTP & Start" else "Driver Reached Pickup",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else if (booking.status == "ACTIVE") {
                    Button(
                        onClick = onCompleteBooking,
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = com.example.ui.theme.PolishIndigo600
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Complete Trip & Pay",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                if (booking.serviceType == "DRIVER" && booking.status == "ACTIVE") {
                    OutlinedButton(
                        onClick = onSimulate15MinWarning,
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.6f))
                    ) {
                        Text(text = "15m Alert", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                OutlinedButton(
                    onClick = onCancelBooking,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.4f))
                ) {
                    Text(text = "Cancel", fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun SosConfirmationDialog(
    onConfirmSos: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "🚨", fontSize = 24.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Emergency SOS Alert",
                    fontWeight = FontWeight.Bold,
                    color = RideSOSRed
                )
            }
        },
        text = {
            Column {
                Text(
                    text = "Are you sure you want to send an emergency safety alert?",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = RideTextPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = RideSOSRedContainer,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "Safety Protocol:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = RideSOSRedDark
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "• Live GPS location will be broadcast to your saved emergency contacts.\n• Platform safety team will be notified immediately.\n• Driver personal contact details will NOT be exposed to ensure mutual safety.",
                            fontSize = 11.sp,
                            color = RideSOSRedDark,
                            lineHeight = 15.sp
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirmSos,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = RideSOSRed),
                modifier = Modifier.testTag("send_sos_confirm_button")
            ) {
                Text("Send SOS Now", fontWeight = FontWeight.Bold, color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = RideTextSecondary)
            }
        }
    )
}

@Composable
fun PaymentDialog(
    fare: Double,
    selectedMethod: String,
    onSelectMethod: (String) -> Unit,
    onConfirmPayment: () -> Unit,
    onDismiss: () -> Unit
) {
    val methods = listOf("UPI (Google Pay / PhonePe)", "Credit / Debit Card", "Cash on Drop")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Trip Completed • Transparent Payment",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = RideDeepNavy
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = RideBackgroundLight,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Fare Breakdown",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = RideDeepNavy
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        FareRow("Ride Base Fare", "₹${(fare * 0.7).toInt()}")
                        FareRow("Distance & Time Charge", "₹${(fare * 0.2).toInt()}")
                        FareRow("Platform Fee", "₹10")
                        FareRow("Taxes & GST", "₹${(fare * 0.05).toInt()}")
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = RideBorderLight)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total Payable", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = RideDeepNavy)
                            Text("₹${fare.toInt()}", fontWeight = FontWeight.Black, fontSize = 18.sp, color = RideDeepNavy)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Choose Payment Method",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = RideDeepNavy
                )

                methods.forEach { method ->
                    val isChosen = selectedMethod.startsWith(method.take(3))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelectMethod(method) }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = isChosen,
                            onClick = { onSelectMethod(method) },
                            colors = RadioButtonDefaults.colors(selectedColor = RideDeepNavy)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = method, fontSize = 13.sp, color = RideTextPrimary)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirmPayment,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = com.example.ui.theme.PolishIndigo600, contentColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("confirm_payment_button")
            ) {
                Text("Pay ₹${fare.toInt()}", fontWeight = FontWeight.Bold)
            }
        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RatingDialog(
    score: Int,
    onScoreChange: (Int) -> Unit,
    onSubmitRating: (Int, List<String>) -> Unit,
    onDismiss: () -> Unit
) {
    val quickTags = listOf("Excellent driver", "Clean vehicle", "Safe ride", "On time", "Polite driver", "Smooth navigation")
    val selectedTags = remember { mutableStateListOf<String>() }
    var issueReportText by remember { mutableStateOf("") }
    var showReportBox by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "How was your ride?",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = com.example.ui.theme.PolishSlate900,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Star row
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    (1..5).forEach { star ->
                        IconButton(onClick = { onScoreChange(star) }) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "$star stars",
                                tint = if (star <= score) com.example.ui.theme.PolishAmber500 else com.example.ui.theme.PolishSlate200,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }
                }

                Text(
                    text = "Tell us about your experience",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = com.example.ui.theme.PolishSlate600
                )

                Spacer(modifier = Modifier.height(10.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    quickTags.forEach { tag ->
                        val isSelected = selectedTags.contains(tag)
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (isSelected) com.example.ui.theme.PolishIndigo600 else com.example.ui.theme.PolishIndigo50,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) com.example.ui.theme.PolishIndigo600 else com.example.ui.theme.PolishBorder
                            ),
                            modifier = Modifier.clickable {
                                if (isSelected) selectedTags.remove(tag) else selectedTags.add(tag)
                            }
                        ) {
                            Text(
                                text = tag,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.White else com.example.ui.theme.PolishIndigo600,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (!showReportBox) {
                    TextButton(onClick = { showReportBox = true }) {
                        Text("Report an issue with this trip", fontSize = 12.sp, color = com.example.ui.theme.PolishRose600)
                    }
                } else {
                    OutlinedTextField(
                        value = issueReportText,
                        onValueChange = { issueReportText = it },
                        label = { Text("Describe issue (Fare, safety, driver conduct)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onSubmitRating(score, selectedTags.toList()) },
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = com.example.ui.theme.PolishIndigo600, contentColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Submit Feedback", fontWeight = FontWeight.Bold)
            }
        }
    )
}

@Composable
fun DriverCommitmentDialog(
    onAcceptCommit: () -> Unit,
    onDecline: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDecline,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Warning, contentDescription = "Warning", tint = com.example.ui.theme.PolishAmber500)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Important Commitment", fontWeight = FontWeight.Bold, color = com.example.ui.theme.PolishSlate900)
            }
        },
        text = {
            Text(
                text = "Only accept this booking if you can commit to completing the full booking.\n\nOnce accepted, you cannot accept another booking while this booking is active. This ensures driver reliability and customer safety.",
                fontSize = 14.sp,
                color = com.example.ui.theme.PolishSlate600,
                lineHeight = 18.sp
            )
        },
        confirmButton = {
            Button(
                onClick = onAcceptCommit,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = com.example.ui.theme.PolishIndigo600, contentColor = Color.White)
            ) {
                Text("Accept & Commit", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDecline) {
                Text("Decline", color = com.example.ui.theme.PolishRose600)
            }
        }
    )
}

@Composable
fun Driver15MinWarningDialog(
    onEndBooking: () -> Unit,
    onExtendBooking: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {},
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("⚠️", fontSize = 22.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Booking Ending Soon", fontWeight = FontWeight.Bold, color = com.example.ui.theme.PolishSlate900)
            }
        },
        text = {
            Text(
                text = "Your hired driver booking ends in 15 minutes.\n\nWould you like to end the booking on schedule or extend the duration?",
                fontSize = 14.sp,
                color = com.example.ui.theme.PolishSlate800,
                lineHeight = 18.sp
            )
        },
        confirmButton = {
            Button(
                onClick = onExtendBooking,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = com.example.ui.theme.PolishIndigo600, contentColor = Color.White)
            ) {
                Text("EXTEND (+1h / +30m)", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onEndBooking,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("END Trip", color = com.example.ui.theme.PolishSlate900, fontWeight = FontWeight.Bold)
            }
        }
    )
}

@Composable
fun DriverExtendDialog(
    onConfirmExtension: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedExtensionHours by remember { mutableStateOf(1) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Extend Driver Booking", fontWeight = FontWeight.Bold, color = com.example.ui.theme.PolishSlate900)
        },
        text = {
            Column {
                Text(
                    text = "Select additional hours with your professional driver:",
                    fontSize = 13.sp,
                    color = com.example.ui.theme.PolishSlate600
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ExtensionChoiceChip(
                        label = "+1 Hour",
                        price = "₹200",
                        isSelected = selectedExtensionHours == 1,
                        onClick = { selectedExtensionHours = 1 },
                        modifier = Modifier.weight(1f)
                    )
                    ExtensionChoiceChip(
                        label = "+2 Hours",
                        price = "₹400",
                        isSelected = selectedExtensionHours == 2,
                        onClick = { selectedExtensionHours = 2 },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirmExtension(selectedExtensionHours) },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = com.example.ui.theme.PolishIndigo600, contentColor = Color.White)
            ) {
                Text("Confirm Extension", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = com.example.ui.theme.PolishSlate500)
            }
        }
    )
}

@Composable
private fun ExtensionChoiceChip(
    label: String,
    price: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = if (isSelected) com.example.ui.theme.PolishIndigo600 else com.example.ui.theme.PolishIndigo50,
        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) com.example.ui.theme.PolishIndigo600 else com.example.ui.theme.PolishBorder),
        modifier = modifier.clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) Color.White else com.example.ui.theme.PolishSlate900
            )
            Text(
                text = price,
                fontSize = 12.sp,
                color = if (isSelected) com.example.ui.theme.PolishIndigo100 else com.example.ui.theme.PolishIndigo600
            )
        }
    }
}

@Composable
fun FareRow(title: String, amount: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = com.example.ui.theme.PolishSlate700
        )
        Text(
            text = amount,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = com.example.ui.theme.PolishSlate900
        )
    }
}
