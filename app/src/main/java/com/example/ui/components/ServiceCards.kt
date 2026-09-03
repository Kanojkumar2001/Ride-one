package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.PolishAmber50
import com.example.ui.theme.PolishBorder
import com.example.ui.theme.PolishEmerald50
import com.example.ui.theme.PolishIndigo100
import com.example.ui.theme.PolishIndigo50
import com.example.ui.theme.PolishIndigo600
import com.example.ui.theme.PolishSlate500
import com.example.ui.theme.PolishSlate900
import com.example.ui.theme.PolishSurface

@Composable
fun MainServiceCards(
    onBookRideClick: () -> Unit,
    onBookParcelClick: () -> Unit,
    onBookDriverClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Hero Mobility Banner (Matching Indigo 600 Hero Card with rounded-[2rem] and translucent circle)
        Card(
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = PolishIndigo600),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(28.dp))
            ) {
                // Decorative ambient circle
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .offset(x = 240.dp, y = (-24).dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.12f))
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = Color.White.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "3-IN-1 MOBILITY",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                letterSpacing = 0.5.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Move People. Move Parcels. Move Your Vehicle.",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            lineHeight = 20.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Unified on-demand transport across Ongole",
                            fontSize = 11.sp,
                            color = PolishIndigo100
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Box(
                        modifier = Modifier
                            .size(68.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(Color.White.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.banner_mobility),
                            contentDescription = "Mobility illustration",
                            modifier = Modifier.size(56.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "What do you need today?",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = PolishSlate900
            )
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = PolishIndigo50,
                modifier = Modifier.padding(bottom = 2.dp)
            ) {
                Text(
                    text = "3 Services",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = PolishIndigo600,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 1. Book a Ride Card
        ServiceItemCard(
            title = "Book a Ride",
            subtitle = "Travel anywhere with Bike, Scooty, Auto, Car, or Girl Rider",
            emoji = "🚗",
            buttonText = "Book Ride",
            isPrimary = true,
            iconContainerColor = PolishIndigo50,
            onClick = onBookRideClick,
            testTag = "book_ride_card"
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 2. Book a Parcel Card
        ServiceItemCard(
            title = "Book a Parcel",
            subtitle = "Send documents, food, clothes, & packages securely",
            emoji = "📦",
            buttonText = "Send Parcel",
            isPrimary = false,
            iconContainerColor = PolishAmber50,
            onClick = onBookParcelClick,
            testTag = "book_parcel_card"
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 3. Book a Driver Card
        ServiceItemCard(
            title = "Book a Driver",
            subtitle = "Drive your own vehicle with a professional driver (Live / Schedule)",
            emoji = "👨‍✈️",
            buttonText = "Hire Driver",
            isPrimary = false,
            iconContainerColor = PolishEmerald50,
            onClick = onBookDriverClick,
            testTag = "book_driver_card"
        )
    }
}

@Composable
private fun ServiceItemCard(
    title: String,
    subtitle: String,
    emoji: String,
    buttonText: String,
    isPrimary: Boolean,
    iconContainerColor: Color,
    onClick: () -> Unit,
    testTag: String
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = PolishSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, PolishBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon Badge (rounded-2xl)
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(iconContainerColor),
                contentAlignment = Alignment.Center
            ) {
                Text(text = emoji, fontSize = 26.sp)
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = PolishSlate900
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = PolishSlate500,
                    lineHeight = 16.sp
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Button(
                onClick = onClick,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isPrimary) PolishIndigo600 else PolishIndigo50,
                    contentColor = if (isPrimary) Color.White else PolishIndigo600
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
    }
}
