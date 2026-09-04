package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.TwoWheeler
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
import com.example.ui.theme.PolishBorder
import com.example.ui.theme.PolishSlate100
import com.example.ui.theme.PolishSlate400
import com.example.ui.theme.PolishSlate50
import com.example.ui.theme.PolishSlate500
import com.example.ui.theme.PolishSlate900
import com.example.ui.theme.PolishSurface
import com.example.ui.theme.RideOrangeLight
import com.example.ui.theme.RideOrangePrimary

@Composable
fun ServicesScreen(
    onBookRideClick: () -> Unit,
    onBookParcelClick: () -> Unit,
    onBookDriverClick: () -> Unit,
    onViewActivityClick: () -> Unit,
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
            text = "Services",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = PolishSlate900
        )
        Text(
            text = "Fast everyday rides, safe package delivery, and verified pilots",
            fontSize = 13.sp,
            color = PolishSlate500
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Category 1: Rides & Daily Commute
        Text(
            text = "RIDES & COMMUTE",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = PolishSlate400,
            letterSpacing = 0.5.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        ServiceCatalogItem(
            title = "Book a Ride",
            subtitle = "Bike, Scooty, Auto, Cab & Girl Rider for women safety",
            tag = "2-3 mins away",
            tagColor = Color(0xFF10B981),
            icon = Icons.Default.TwoWheeler,
            iconTint = RideOrangePrimary,
            iconBg = RideOrangeLight,
            buttonText = "Book Ride",
            onClick = onBookRideClick,
            testTag = "catalog_ride_item"
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Category 2: Parcel Delivery
        Text(
            text = "DELIVERY & PARCEL",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = PolishSlate400,
            letterSpacing = 0.5.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        ServiceCatalogItem(
            title = "Send a Parcel",
            subtitle = "Direct door-to-door courier protected with 4-digit OTP handover",
            tag = "OTP Verified",
            tagColor = Color(0xFF0284C7),
            icon = Icons.Default.LocalShipping,
            iconTint = Color(0xFF0284C7),
            iconBg = Color(0xFFE0F2FE),
            buttonText = "Send Parcel",
            onClick = onBookParcelClick,
            testTag = "catalog_parcel_item"
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Category 3: Driver for Vehicle
        Text(
            text = "HIRE PILOT / DRIVER",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = PolishSlate400,
            letterSpacing = 0.5.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        ServiceCatalogItem(
            title = "Book a Driver",
            subtitle = "Hire a certified pilot to drive your own 2-wheeler or 4-wheeler",
            tag = "YOUR VEHICLE",
            tagColor = Color(0xFF4F46E5),
            icon = Icons.Default.DirectionsCar,
            iconTint = Color(0xFF4F46E5),
            iconBg = Color(0xFFEEF2FF),
            buttonText = "Hire Driver",
            onClick = onBookDriverClick,
            testTag = "catalog_driver_item"
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Activity & Trip History Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = PolishSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, PolishBorder),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onViewActivityClick)
                .testTag("catalog_activity_item")
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(PolishSlate100),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = "Activity",
                        tint = PolishSlate900,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Trip Activity & Receipts",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = PolishSlate900
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "View history of your completed rides, parcels and bills",
                        fontSize = 12.sp,
                        color = PolishSlate500
                    )
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "View",
                    tint = PolishSlate500,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun ServiceCatalogItem(
    title: String,
    subtitle: String,
    tag: String,
    tagColor: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    iconBg: Color,
    buttonText: String,
    onClick: () -> Unit,
    testTag: String
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = PolishSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, PolishBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag(testTag)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(iconBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = title,
                            tint = iconTint,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = PolishSlate900
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = iconBg
                        ) {
                            Text(
                                text = tag,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = tagColor,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Button(
                    onClick = onClick,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RideOrangePrimary,
                        contentColor = Color.White
                    ),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = buttonText,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = PolishSlate500,
                lineHeight = 17.sp
            )
        }
    }
}
