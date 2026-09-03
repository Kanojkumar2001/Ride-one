package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SignalCellularConnectedNoInternet0Bar
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.PolishBorder
import com.example.ui.theme.PolishIndigo100
import com.example.ui.theme.PolishIndigo50
import com.example.ui.theme.PolishIndigo600
import com.example.ui.theme.PolishRose50
import com.example.ui.theme.PolishRose600
import com.example.ui.theme.PolishSlate100
import com.example.ui.theme.PolishSlate500
import com.example.ui.theme.PolishSlate900
import com.example.ui.theme.PolishSurface
import com.example.viewmodel.AppEcosystemMode

@Composable
fun AppHeader(
    currentMode: AppEcosystemMode,
    isOffline: Boolean,
    onSelectMode: (AppEcosystemMode) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = PolishSurface,
        border = androidx.compose.foundation.BorderStroke(1.dp, PolishBorder),
        shadowElevation = 2.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Top Row: Brand, Location, and User Avatar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(PolishIndigo50),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_rideone_logo),
                            contentDescription = "Ride One Logo",
                            modifier = Modifier.size(28.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "RIDE",
                                fontWeight = FontWeight.Black,
                                fontSize = 18.sp,
                                color = PolishSlate900,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "ONE",
                                fontWeight = FontWeight.Black,
                                fontSize = 18.sp,
                                color = PolishIndigo600,
                                letterSpacing = 1.sp
                            )
                        }
                        Text(
                            text = "One App. Every Move.",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = PolishSlate500
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (isOffline) {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = PolishRose50,
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SignalCellularConnectedNoInternet0Bar,
                                    contentDescription = "Offline",
                                    tint = PolishRose600,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Offline",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PolishRose600
                                )
                            }
                        }
                    }

                    // Location Chip (Pill styled with Indigo accent)
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = PolishIndigo50,
                        border = androidx.compose.foundation.BorderStroke(1.dp, PolishIndigo100)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "Location",
                                tint = PolishIndigo600,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Ongole, AP",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = PolishIndigo600
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // User Profile Avatar (matching Professional Polish avatar from design)
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(PolishIndigo100)
                            .border(2.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(PolishIndigo600),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "RO",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Ecosystem Switcher Tabs (Segmented control in Slate 100 with white active pill)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(PolishSlate100)
                    .padding(3.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                EcosystemTabItem(
                    title = "User App",
                    icon = Icons.Default.Person,
                    isSelected = currentMode == AppEcosystemMode.USER_APP,
                    onClick = { onSelectMode(AppEcosystemMode.USER_APP) },
                    modifier = Modifier.weight(1f)
                )
                EcosystemTabItem(
                    title = "Driver Partner",
                    icon = Icons.Default.TwoWheeler,
                    isSelected = currentMode == AppEcosystemMode.DRIVER_APP,
                    onClick = { onSelectMode(AppEcosystemMode.DRIVER_APP) },
                    modifier = Modifier.weight(1f)
                )
                EcosystemTabItem(
                    title = "Admin Portal",
                    icon = Icons.Default.AdminPanelSettings,
                    isSelected = currentMode == AppEcosystemMode.ADMIN_PORTAL,
                    onClick = { onSelectMode(AppEcosystemMode.ADMIN_PORTAL) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun EcosystemTabItem(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) Color.White else Color.Transparent)
            .then(
                if (isSelected) {
                    Modifier.shadow(2.dp, RoundedCornerShape(12.dp))
                } else Modifier
            )
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isSelected) PolishIndigo600 else PolishSlate500,
                modifier = Modifier.size(15.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) PolishSlate900 else PolishSlate500
            )
        }
    }
}
