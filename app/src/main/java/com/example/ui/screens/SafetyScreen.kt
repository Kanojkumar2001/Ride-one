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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.EmergencyContactEntity
import com.example.ui.theme.PolishBorder
import com.example.ui.theme.PolishRose50
import com.example.ui.theme.PolishRose600
import com.example.ui.theme.PolishSlate100
import com.example.ui.theme.PolishSlate400
import com.example.ui.theme.PolishSlate50
import com.example.ui.theme.PolishSlate500
import com.example.ui.theme.PolishSlate800
import com.example.ui.theme.PolishSlate900
import com.example.ui.theme.PolishSurface
import com.example.ui.theme.RideSOSRed

@Composable
fun SafetyScreen(
    emergencyContacts: List<EmergencyContactEntity>,
    onTriggerSos: () -> Unit,
    onManageContactsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PolishSlate50)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = "Safety Center",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = PolishSlate900
        )
        Text(
            text = "Your safety is our #1 priority on every trip, 24/7",
            fontSize = 13.sp,
            color = PolishSlate500
        )

        Spacer(modifier = Modifier.height(16.dp))

        // SOS Panic Alert Card
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = PolishRose50),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, PolishRose600.copy(alpha = 0.3f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(RideSOSRed),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Emergency SOS",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "24/7 Emergency SOS",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = PolishRose600
                        )
                        Text(
                            text = "Instantly sends GPS coordinates to emergency contacts and police",
                            fontSize = 12.sp,
                            color = PolishSlate800
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = onTriggerSos,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RideSOSRed,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .testTag("safety_trigger_sos_button")
                ) {
                    Text(
                        text = "TRIGGER EMERGENCY SOS",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Safety Pillars
        Text(
            text = "SAFETY FEATURES",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = PolishSlate400,
            letterSpacing = 0.5.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        SafetyFeatureItem(
            icon = Icons.Default.Shield,
            iconTint = Color(0xFF10B981),
            iconBg = Color(0xFFECFDF5),
            title = "Ride Shield Deviation Detection",
            description = "Real-time monitoring detects unexpected route deviations or extended stops automatically."
        )

        Spacer(modifier = Modifier.height(10.dp))

        SafetyFeatureItem(
            icon = Icons.Default.Share,
            iconTint = Color(0xFF0284C7),
            iconBg = Color(0xFFE0F2FE),
            title = "Share Live Trip Status",
            description = "Share live tracking link with loved ones so they can follow your trip until safe arrival."
        )

        Spacer(modifier = Modifier.height(10.dp))

        SafetyFeatureItem(
            icon = Icons.Default.Lock,
            iconTint = Color(0xFF8B5CF6),
            iconBg = Color(0xFFF5F3FF),
            title = "Secure 4-Digit OTP Verification",
            description = "Trips and parcel deliveries begin only after mutual OTP confirmation."
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Emergency Helpline Numbers
        Text(
            text = "EMERGENCY HELPLINES (TOLL-FREE)",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = PolishSlate400,
            letterSpacing = 0.5.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            EmergencyNumberChip(title = "National SOS", number = "112", modifier = Modifier.weight(1f))
            EmergencyNumberChip(title = "Women Helpline", number = "1091", modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            EmergencyNumberChip(title = "Police", number = "100", modifier = Modifier.weight(1f))
            EmergencyNumberChip(title = "Ambulance", number = "108", modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun SafetyFeatureItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    iconBg: Color,
    title: String,
    description: String
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = PolishSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, PolishBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconTint,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = PolishSlate900
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = PolishSlate500,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@Composable
private fun EmergencyNumberChip(
    title: String,
    number: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = PolishSurface,
        border = androidx.compose.foundation.BorderStroke(1.dp, PolishBorder),
        shadowElevation = 1.dp,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(PolishSlate100),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = "Call",
                    tint = PolishSlate800,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(text = title, fontSize = 11.sp, color = PolishSlate500)
                Text(text = number, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = PolishSlate900)
            }
        }
    }
}
