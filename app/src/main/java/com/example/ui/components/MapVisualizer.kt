package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.PolishBorder
import com.example.ui.theme.PolishSlate400
import com.example.ui.theme.PolishSlate600
import com.example.ui.theme.PolishSlate900
import com.example.ui.theme.RideDarkPill
import com.example.ui.theme.RideOrangePrimary

@Composable
fun MapVisualizer(
    modifier: Modifier = Modifier,
    activeStatus: String? = null,
    isGirlRiderAvailable: Boolean = true,
    pickupText: String = "Current Location",
    dropText: String? = null,
    onRecenterClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onRadarClick: () -> Unit = {},
    onVehicleSelect: (String) -> Unit = {}
) {
    val infiniteTransition = rememberInfiniteTransition(label = "mapMotion")

    // Pulse animation for current location pin
    val pulseRadius by infiniteTransition.animateFloat(
        initialValue = 18f,
        targetValue = 44f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse"
    )

    // Subtle drift motion for live vehicles
    val vehicleMotion by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "vehicleMotion"
    )

    Surface(
        shape = RoundedCornerShape(24.dp),
        color = Color(0xFFF8FAFC),
        border = androidx.compose.foundation.BorderStroke(1.dp, PolishBorder),
        shadowElevation = 3.dp,
        modifier = modifier
            .fillMaxWidth()
            .testTag("live_map_container")
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val totalWidth = maxWidth
            val totalHeight = maxHeight

            // Vector Map Canvas (realistic clean city streets, pastel zones, curved avenues)
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height

                // Map Base Tint
                drawRect(color = Color(0xFFF8FAFC), size = size)

                // Pastel Green Zone (Top Left Park/Zone)
                drawRoundRect(
                    color = Color(0xFFE8F5E9),
                    topLeft = Offset(w * 0.04f, h * 0.08f),
                    size = Size(w * 0.32f, h * 0.22f),
                    cornerRadius = CornerRadius(20f, 20f)
                )

                // Light Grey Zone (Top Center building lot)
                drawRoundRect(
                    color = Color(0xFFEEF2F6),
                    topLeft = Offset(w * 0.42f, h * 0.08f),
                    size = Size(w * 0.16f, h * 0.20f),
                    cornerRadius = CornerRadius(16f, 16f)
                )

                // Pastel Blue Zone (Top Right commercial hub)
                drawRoundRect(
                    color = Color(0xFFE0F2FE),
                    topLeft = Offset(w * 0.62f, h * 0.09f),
                    size = Size(w * 0.32f, h * 0.16f),
                    cornerRadius = CornerRadius(20f, 20f)
                )

                // Pastel Green Zone (Mid Right)
                drawRoundRect(
                    color = Color(0xFFE8F5E9),
                    topLeft = Offset(w * 0.64f, h * 0.45f),
                    size = Size(w * 0.32f, h * 0.18f),
                    cornerRadius = CornerRadius(20f, 20f)
                )

                // Soft Grey Building blocks (Left Middle)
                drawRoundRect(
                    color = Color(0xFFEEF2F6),
                    topLeft = Offset(w * 0.12f, h * 0.36f),
                    size = Size(w * 0.24f, h * 0.18f),
                    cornerRadius = CornerRadius(16f, 16f)
                )

                // Soft Grey Building blocks (Bottom Left)
                drawRoundRect(
                    color = Color(0xFFEEF2F6),
                    topLeft = Offset(w * 0.14f, h * 0.60f),
                    size = Size(w * 0.24f, h * 0.16f),
                    cornerRadius = CornerRadius(16f, 16f)
                )

                // Road Specifications
                val roadColor = Color.White
                val roadBorder = Color(0xFFE2E8F0)

                // Vertical main avenue (Center Left)
                val vRoad1 = w * 0.38f
                drawLine(roadBorder, Offset(vRoad1, 0f), Offset(vRoad1, h), strokeWidth = 26f, cap = StrokeCap.Square)
                drawLine(roadColor, Offset(vRoad1, 0f), Offset(vRoad1, h), strokeWidth = 20f, cap = StrokeCap.Square)

                // Horizontal avenue (Upper)
                val hRoad1 = h * 0.32f
                drawLine(roadBorder, Offset(0f, hRoad1), Offset(w, hRoad1), strokeWidth = 24f, cap = StrokeCap.Square)
                drawLine(roadColor, Offset(0f, hRoad1), Offset(w, hRoad1), strokeWidth = 18f, cap = StrokeCap.Square)

                // Horizontal avenue (Middle)
                val hRoad2 = h * 0.55f
                drawLine(roadBorder, Offset(0f, hRoad2), Offset(w, hRoad2), strokeWidth = 24f, cap = StrokeCap.Square)
                drawLine(roadColor, Offset(0f, hRoad2), Offset(w, hRoad2), strokeWidth = 18f, cap = StrokeCap.Square)

                // Curved diagonal bypass avenue
                val bypassPath = Path().apply {
                    moveTo(w * 0.38f, h * 0.32f)
                    cubicTo(
                        w * 0.48f, h * 0.35f,
                        w * 0.68f, h * 0.45f,
                        w * 0.85f, h * 0.68f
                    )
                }
                drawPath(bypassPath, color = roadBorder, style = Stroke(width = 24f, cap = StrokeCap.Round))
                drawPath(bypassPath, color = roadColor, style = Stroke(width = 18f, cap = StrokeCap.Round))

                // Center Location Anchor & Pulsing Ring
                val centerPin = Offset(w * 0.38f, h * 0.40f)

                // Outer Soft Orange Pulse Ring
                drawCircle(
                    color = RideOrangePrimary.copy(alpha = (1f - (pulseRadius / 45f)).coerceIn(0f, 0.25f)),
                    radius = pulseRadius,
                    center = centerPin
                )
                // Center Orange Anchor
                drawCircle(color = RideOrangePrimary, radius = 9f, center = centerPin)
                drawCircle(color = Color.White, radius = 4f, center = centerPin)
            }

            // Top-Right Floating Controls: Radar On Pill + GPS Target Button
            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Black Pill: "• RADAR ON"
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(RideDarkPill)
                        .clickable(onClick = onRadarClick)
                        .padding(horizontal = 11.dp, vertical = 7.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF22C55E))
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "RADAR ON",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // GPS Target Circular Button
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(1.dp, PolishBorder, CircleShape)
                        .shadow(2.dp, CircleShape)
                        .clickable(onClick = onRecenterClick)
                        .testTag("map_target_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.MyLocation,
                        contentDescription = "My Location",
                        tint = RideOrangePrimary,
                        modifier = Modifier.size(19.dp)
                    )
                }
            }

            // Marker 1: Top-Left "🏍️ Bike 2m"
            FloatingVehicleMarker(
                emoji = "🏍️",
                label = "Bike 2m",
                onClick = { onVehicleSelect("bike") },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(x = totalWidth * 0.06f, y = totalHeight * 0.22f)
            )

            // Marker 2: Top-Right "🛺 Auto 3m"
            FloatingVehicleMarker(
                emoji = "🛺",
                label = "Auto 3m",
                onClick = { onVehicleSelect("auto") },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(x = totalWidth * 0.70f, y = totalHeight * 0.23f)
            )

            // Marker 3: Mid-Right "🛵 Scooty 3m"
            FloatingVehicleMarker(
                emoji = "🛵",
                label = "Scooty 3m",
                onClick = { onVehicleSelect("scooty") },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(x = totalWidth * 0.68f, y = totalHeight * 0.50f)
            )

            // Marker 4: Center "• Current Location"
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                shadowElevation = 3.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(x = totalWidth * 0.35f, y = totalHeight * 0.44f)
                    .clickable(onClick = onRecenterClick)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF10B981))
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = pickupText,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = PolishSlate900
                    )
                }
            }

            // Bottom Floating Search Pill Bar ("Where do you want to go?")
            Surface(
                shape = RoundedCornerShape(32.dp),
                color = Color.White,
                shadowElevation = 4.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, PolishBorder),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 12.dp)
                    .clickable(onClick = onSearchClick)
                    .testTag("map_search_bar")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Orange circular search button
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(RideOrangePrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search destination",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Where do you want to go?",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = PolishSlate900
                        )
                        Spacer(modifier = Modifier.height(1.dp))
                        Text(
                            text = dropText ?: "Enter drop location or landmark",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = PolishSlate600
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FloatingVehicleMarker(
    emoji: String,
    label: String,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        shadowElevation = 3.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = modifier.clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = emoji, fontSize = 13.sp)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = PolishSlate900
            )
        }
    }
}
