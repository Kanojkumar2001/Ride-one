package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.RideDeepNavy
import com.example.ui.theme.RideElectricLime
import com.example.ui.theme.RideNavyElevated
import com.example.ui.theme.RideSurfaceLight

@Composable
fun MapVisualizer(
    modifier: Modifier = Modifier,
    activeStatus: String? = null,
    isGirlRiderAvailable: Boolean = true,
    pickupText: String = "Current Location",
    dropText: String? = null,
    onRecenterClick: () -> Unit = {}
) {
    val infiniteTransition = rememberInfiniteTransition(label = "mapMotion")

    // Pulse animation for user pin
    val pulseRadius by infiniteTransition.animateFloat(
        initialValue = 18f,
        targetValue = 38f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse"
    )

    // Motion offset for nearby moving vehicles
    val vehicleMotion by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "vehicleMotion"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFE8EDF5))
            .testTag("live_map_container")
    ) {
        // Custom Canvas drawing realistic roads, rivers, grid, route, and vehicle markers
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // Background subtle city tint
            drawRect(color = Color(0xFFEFF3F8), size = size)

            // Stylized park / green zone
            drawRoundRect(
                color = Color(0xFFDFF0D8),
                topLeft = Offset(width * 0.05f, height * 0.12f),
                size = androidx.compose.ui.geometry.Size(width * 0.28f, height * 0.22f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(24f, 24f)
            )
            drawRoundRect(
                color = Color(0xFFDFF0D8),
                topLeft = Offset(width * 0.65f, height * 0.55f),
                size = androidx.compose.ui.geometry.Size(width * 0.3f, height * 0.25f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(24f, 24f)
            )

            // Major Roads (Deep grey/white avenues with shadows)
            val roadColor = Color.White
            val roadOutline = Color(0xFFD3DBE6)

            // Horizontal roads
            val hRoads = listOf(height * 0.25f, height * 0.48f, height * 0.72f)
            hRoads.forEach { y ->
                drawLine(roadOutline, Offset(0f, y), Offset(width, y), strokeWidth = 32f, cap = StrokeCap.Round)
                drawLine(roadColor, Offset(0f, y), Offset(width, y), strokeWidth = 24f, cap = StrokeCap.Round)
            }

            // Vertical roads
            val vRoads = listOf(width * 0.22f, width * 0.52f, width * 0.78f)
            vRoads.forEach { x ->
                drawLine(roadOutline, Offset(x, 0f), Offset(x, height), strokeWidth = 32f, cap = StrokeCap.Round)
                drawLine(roadColor, Offset(x, 0f), Offset(x, height), strokeWidth = 24f, cap = StrokeCap.Round)
            }

            // Diagonal bypass avenue
            val bypassPath = Path().apply {
                moveTo(0f, height * 0.85f)
                cubicTo(
                    width * 0.3f, height * 0.75f,
                    width * 0.6f, height * 0.35f,
                    width, height * 0.15f
                )
            }
            drawPath(bypassPath, color = roadOutline, style = Stroke(width = 36f, cap = StrokeCap.Round))
            drawPath(bypassPath, color = roadColor, style = Stroke(width = 28f, cap = StrokeCap.Round))

            // Dashed center line for main avenue
            drawPath(
                bypassPath,
                color = Color(0xFFCAD4E2),
                style = Stroke(
                    width = 3f,
                    pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)
                )
            )

            // User Location Pin (Center-left)
            val userPos = Offset(width * 0.52f, height * 0.48f)

            // Active Route Path if in active booking or dropoff selected
            if (dropText != null || activeStatus != null) {
                val dropPos = Offset(width * 0.78f, height * 0.25f)
                val routePath = Path().apply {
                    moveTo(userPos.x, userPos.y)
                    lineTo(width * 0.52f, height * 0.25f)
                    lineTo(dropPos.x, dropPos.y)
                }
                // Route glow
                drawPath(routePath, color = com.example.ui.theme.PolishIndigo600.copy(alpha = 0.25f), style = Stroke(width = 16f, cap = StrokeCap.Round))
                drawPath(routePath, color = com.example.ui.theme.PolishIndigo600, style = Stroke(width = 8f, cap = StrokeCap.Round))

                // Dropoff Pin
                drawCircle(color = com.example.ui.theme.PolishRose600, radius = 14f, center = dropPos)
                drawCircle(color = Color.White, radius = 7f, center = dropPos)
            }

            // User Pulse Ring
            drawCircle(
                color = com.example.ui.theme.PolishIndigo600.copy(alpha = (1f - (pulseRadius / 40f)).coerceIn(0f, 0.6f)),
                radius = pulseRadius,
                center = userPos
            )
            // User Anchor Circle
            drawCircle(color = com.example.ui.theme.PolishIndigo600, radius = 13f, center = userPos)
            drawCircle(color = Color.White, radius = 6f, center = userPos)

            // Moving Vehicle Markers (Auto, Car, Bike, Scooty, Girl Rider)
            // Vehicle 1: Auto 🛺 moving on vertical avenue
            val autoPos = Offset(width * 0.22f, height * (0.3f + 0.35f * vehicleMotion))
            drawCircle(color = Color.White, radius = 16f, center = autoPos)
            drawCircle(color = com.example.ui.theme.PolishBorder, radius = 16f, center = autoPos, style = Stroke(width = 2.5f))

            // Vehicle 2: Car 🚗 moving along diagonal bypass
            val carPos = Offset(width * (0.35f + 0.25f * vehicleMotion), height * (0.6f - 0.25f * vehicleMotion))
            drawCircle(color = Color.White, radius = 16f, center = carPos)
            drawCircle(color = com.example.ui.theme.PolishBorder, radius = 16f, center = carPos, style = Stroke(width = 2.5f))

            // Vehicle 3: Bike 🏍️ moving along horizontal avenue
            val bikePos = Offset(width * (0.6f + 0.25f * (1f - vehicleMotion)), height * 0.72f)
            drawCircle(color = Color.White, radius = 16f, center = bikePos)
            drawCircle(color = com.example.ui.theme.PolishBorder, radius = 16f, center = bikePos, style = Stroke(width = 2.5f))

            // Vehicle 4: Scooty 🛵 on top avenue
            val scootyPos = Offset(width * (0.2f + 0.3f * vehicleMotion), height * 0.25f)
            drawCircle(color = Color.White, radius = 16f, center = scootyPos)
            drawCircle(color = com.example.ui.theme.PolishBorder, radius = 16f, center = scootyPos, style = Stroke(width = 2.5f))

            // Draw Emojis onto markers using native canvas
            val paint = android.graphics.Paint().apply {
                textSize = 28f
                textAlign = android.graphics.Paint.Align.CENTER
            }
            drawContext.canvas.nativeCanvas.drawText("🛺", autoPos.x, autoPos.y + 10f, paint)
            drawContext.canvas.nativeCanvas.drawText("🚗", carPos.x, carPos.y + 10f, paint)
            drawContext.canvas.nativeCanvas.drawText("🏍️", bikePos.x, bikePos.y + 10f, paint)
            drawContext.canvas.nativeCanvas.drawText("🛵", scootyPos.x, scootyPos.y + 10f, paint)

            if (isGirlRiderAvailable) {
                val femaleRiderPos = Offset(width * 0.78f, height * (0.4f + 0.2f * (1f - vehicleMotion)))
                drawCircle(color = Color.White, radius = 16f, center = femaleRiderPos)
                drawCircle(color = com.example.ui.theme.PolishRose600, radius = 16f, center = femaleRiderPos, style = Stroke(width = 2.5f))
                drawContext.canvas.nativeCanvas.drawText("👩", femaleRiderPos.x, femaleRiderPos.y + 10f, paint)
            }
        }

        // Live Location & Recenter Chip
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = com.example.ui.theme.PolishSurface.copy(alpha = 0.96f),
            border = androidx.compose.foundation.BorderStroke(1.dp, com.example.ui.theme.PolishBorder),
            shadowElevation = 2.dp,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Box(modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)) {
                Text(
                    text = "📍 Live Hub: Ongole Central",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = com.example.ui.theme.PolishSlate900
                )
            }
        }

        // Floating Recenter Button
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = com.example.ui.theme.PolishSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, com.example.ui.theme.PolishBorder),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            IconButton(
                onClick = onRecenterClick,
                modifier = Modifier.testTag("recenter_map_button")
            ) {
                Icon(
                    imageVector = Icons.Default.MyLocation,
                    contentDescription = "Recenter Map",
                    tint = com.example.ui.theme.PolishIndigo600
                )
            }
        }
    }
}
