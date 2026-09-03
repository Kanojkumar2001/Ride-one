package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// ============================================================================
// Professional Polish Design Theme Palette
// Extracted from Design HTML:
// Primary: Indigo 600 (#4F46E5), Indigo 500 (#6366F1), Indigo 100 (#E0E7FF), Indigo 50 (#EEF2FF)
// Canvas: Slate 50 (#F8FAFC), Surface: White (#FFFFFF), Border: Slate 100 (#F1F5F9)
// Text: Slate 900 (#0F172A), Slate 800 (#1E293B), Slate 500 (#64748B), Slate 400 (#94A3B8)
// Accents: Emerald 500 (#10B981), Amber 500 (#F59E0B), Rose 600 (#E11D48)
// ============================================================================

val PolishIndigo600 = Color(0xFF4F46E5)
val PolishIndigo500 = Color(0xFF6366F1)
val PolishIndigo700 = Color(0xFF4338CA)
val PolishIndigo100 = Color(0xFFE0E7FF)
val PolishIndigo50 = Color(0xFFEEF2FF)

val PolishSlate900 = Color(0xFF0F172A)
val PolishSlate800 = Color(0xFF1E293B)
val PolishSlate700 = Color(0xFF334155)
val PolishSlate600 = Color(0xFF475569)
val PolishSlate500 = Color(0xFF475569) // Darkened for clear, sharp text visibility
val PolishSlate400 = Color(0xFF64748B) // Boosted contrast
val PolishSlate300 = Color(0xFFCBD5E1)
val PolishSlate200 = Color(0xFFE2E8F0)
val PolishSlate100 = Color(0xFFF1F5F9)
val PolishSlate50 = Color(0xFFF8FAFC)
val PolishSurface = Color(0xFFFFFFFF)

val PolishBorder = PolishSlate200
val PolishBorderSubtle = PolishSlate300

val PolishEmerald600 = Color(0xFF059669)
val PolishEmerald500 = Color(0xFF10B981)
val PolishEmerald100 = Color(0xFFD1FAE5)
val PolishEmerald50 = Color(0xFFECFDF5)

val PolishAmber600 = Color(0xFFD97706)
val PolishAmber500 = Color(0xFFF59E0B)
val PolishAmber100 = Color(0xFFFEF3C7)
val PolishAmber50 = Color(0xFFFFFBEB)

val PolishRose600 = Color(0xFFE11D48)
val PolishRose100 = Color(0xFFFFE4E6)
val PolishRose50 = Color(0xFFFFF1F2)

// Mapped semantic bindings for unified app styling
val RideDeepNavy = PolishIndigo600
val RideNavySurface = PolishIndigo700
val RideNavyElevated = PolishIndigo500
val RideNavyDark = Color(0xFF312E81)

// Accent Color - In Professional Polish: Indigo 50 / soft contrast for pills, or Emerald / Amber for status
val RideElectricLime = PolishIndigo50
val RideLimeHover = PolishIndigo100
val RideLimeContainer = PolishIndigo50
val RideLimeOnContainer = PolishIndigo600

// Canvas & Neutral Backgrounds
val RideBackgroundLight = PolishSlate50
val RideSurfaceLight = Color(0xFFFFFFFF)
val RideSurfaceVariant = PolishSlate100
val RideBorderLight = PolishBorder
val RideBorderSubtle = PolishBorderSubtle

// Typography Colors
val RideTextPrimary = PolishSlate900
val RideTextSecondary = PolishSlate800
val RideTextMuted = PolishSlate700
val RideTextWhite = Color(0xFFFFFFFF)

// Safety & Semantic Action Colors
val RideSOSRed = PolishRose600
val RideSOSRedDark = Color(0xFFBE123C)
val RideSOSRedContainer = PolishRose50
val RideSuccess = PolishEmerald500
val RideSuccessContainer = PolishEmerald50
val RideWarning = PolishAmber500
val RideWarningContainer = PolishAmber50
val RideInfo = PolishIndigo600

