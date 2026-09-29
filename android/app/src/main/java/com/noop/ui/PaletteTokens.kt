package com.noop.ui

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

// MARK: - PaletteTokens — the per-scheme colour set behind `object Palette`
//
// Compose has no OS-dynamic colour (unlike iOS UIColor(light:dark:)), so the light theme is built
// the same way conceptually: ONE set of colour tokens, swapped wholesale per scheme. `Palette.active`
// is snapshot state, so every `Palette.X` read (in a composable OR a Canvas DrawScope) re-resolves
// automatically when the theme flips — ZERO call-site changes across the ~1,740 references.
//
// Dark values mirror StrandPalette.swift's dark; light values are the approved "Warm Paper" set
// (docs/superpowers/specs/2026-06-16-light-theme-design.md). Names/order match the Swift palette.

data class PaletteTokens(
    val surfaceBase: Color,
    val surfaceRaised: Color,
    val surfaceOverlay: Color,
    val surfaceInset: Color,
    val hairline: Color,
    val hairlineStrong: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textTertiary: Color,
    val glowAmbient: Color,
    val accent: Color,
    val accentHover: Color,
    val accentMuted: Color,
    val focusRing: Color,
    val recovery000: Color,
    val recovery030: Color,
    val recovery055: Color,
    val recovery078: Color,
    val recovery100: Color,
    val strain000: Color,
    val strain033: Color,
    val strain066: Color,
    val strain100: Color,
    val sleepAwake: Color,
    val sleepLight: Color,
    val sleepDeep: Color,
    val sleepREM: Color,
    val zone1: Color,
    val zone2: Color,
    val zone3: Color,
    val zone4: Color,
    val zone5: Color,
    val statusPositive: Color,
    val statusWarning: Color,
    val statusCritical: Color,
    val metricCyan: Color,
    val metricPurple: Color,
    val metricAmber: Color,
    val metricRose: Color,
    val chargeColor: Color,
    val chargeDeep: Color,
    val chargeBright: Color,
    val chargeGlow: Color,
    val effortColor: Color,
    val effortDeep: Color,
    val effortBright: Color,
    val effortGlow: Color,
    val restColor: Color,
    val restDeep: Color,
    val restBright: Color,
    val restGlow: Color,
    val stressColor: Color,
    val stressDeep: Color,
    val stressBright: Color,
    val stressGlow: Color,
    val scenicCenter: Color,
    val scenicEdge: Color,
    val scenicStar: Color,
    val cardFillTop: Color,
    val cardFillBottom: Color,
    val gold: Color,
    val goldLight: Color,
    val goldDeep: Color,
    val goldDeepText: Color,
    val signalYellow: Color,
    val titaniumTop: Color,
    val titaniumMid: Color,
    val titaniumLow: Color,
    val titaniumDeep: Color,
    // The bright gauge-tip / sparkline-head core: white reads as a highlight on dark; on light it
    // would vanish into the white card, so it flips to a deep ink (crisp centre on the coloured bead).
    val tipCore: Color,
    // #1160/#1161: the Liquid hero card surface (Today's Charge/Effort/Rest vessel + every screen's hero).
    // Pinned near-black in DARK; in LIGHT it flips to a frosted white so the hero fits in with the other
    // cards instead of reading as a broken dark block (was a raw literal inlined in 11 screens). heroBorder
    // + heroLabel flip with it so the edge stays subtle and the source-badge text stays readable in both.
    val heroFill: Color,
    val heroBorder: Color,
)

// WHOOP-reset dark palette (gold killed 2026-06-22). Values match StrandPalette.swift's DARK
// Titanium column byte-for-byte: blue-grey canvas, WHOOP red→yellow→green recovery, green Charge,
// blue Effort, slate Rest, amber Stress. NO gold anywhere — accent/gold tokens point to WHOOP blue.
val DarkTokens = PaletteTokens(
    surfaceBase = Color(0xFF121518), surfaceRaised = Color(0xFF25292C), surfaceOverlay = Color(0xFF1C1F26),
    surfaceInset = Color(0xFF1F2229), hairline = Color(0xFF21304A), hairlineStrong = Color(0xFF2E3C57),
    textPrimary = Color(0xFFF4F6F8), textSecondary = Color(0xFFC8CFD8), textTertiary = Color(0xFF8A94A4),
    glowAmbient = Color(0xFF3A2D0A),
    // Brand accent → mint, parity with iOS #1068 (NoopVisualStyle.mint/mintGlow). accentMuted is a dark
    // teal muted surface (green-shifted analog of the old navy 0xFF16233A). Gold stays in the recovery world.
    accent = Color(0xFF69DDB8), accentHover = Color(0xFF54E6BD), accentMuted = Color(0xFF163329), focusRing = Color(0xFF69DDB8),
    recovery000 = Color(0xFFE0463C), recovery030 = Color(0xFFE8743C), recovery055 = Color(0xFFF9DF4A),
    recovery078 = Color(0xFF8FD86A), recovery100 = Color(0xFF03E095),
    strain000 = Color(0xFF9C5A14), strain033 = Color(0xFFC2762A), strain066 = Color(0xFFD98A3D), strain100 = Color(0xFFF0A85A),
    sleepAwake = Color(0xFFC2CCDA), sleepLight = Color(0xFF4A90E2), sleepDeep = Color(0xFF2F6FCB), sleepREM = Color(0xFF6FA8E8),
    zone1 = Color(0xFF4A90E2), zone2 = Color(0xFF3FA9C9), zone3 = Color(0xFFE8B84B), zone4 = Color(0xFFD98A3D), zone5 = Color(0xFFE0662F),
    statusPositive = Color(0xFF03E095), statusWarning = Color(0xFFF0A020), statusCritical = Color(0xFFE0662F),
    metricCyan = Color(0xFF3FA9C9), metricPurple = Color(0xFF4A90E2), metricAmber = Color(0xFFD98A3D), metricRose = Color(0xFFE0662F),
    chargeColor = Color(0xFF03E095), chargeDeep = Color(0xFF0B9D62), chargeBright = Color(0xFF6BF0B4), chargeGlow = Color(0xFF03E095),
    effortColor = Color(0xFF4090E0), effortDeep = Color(0xFF2A6FB0), effortBright = Color(0xFF74B6F0), effortGlow = Color(0xFF4090E0),
    restColor = Color(0xFF83A0B8), restDeep = Color(0xFF2F6FCB), restBright = Color(0xFF6FA8E8), restGlow = Color(0xFF4A90E2),
    stressColor = Color(0xFFF0A020), stressDeep = Color(0xFF4A90E2), stressBright = Color(0xFFE0662F), stressGlow = Color(0xFFF0A020),
    scenicCenter = Color(0xFF1C2128), scenicEdge = Color(0xFF121518), scenicStar = Color(0xFFC8CFD8),
    cardFillTop = Color(0xFF15243C), cardFillBottom = Color(0xFF0B1424),
    gold = Color(0xFF60A0E0), goldLight = Color(0xFF9FC8F0), goldDeep = Color(0xFF3A78C8),
    goldDeepText = Color(0xFFFFFFFF), signalYellow = Color(0xFFFFD63D),
    titaniumTop = Color(0xFFF1F3F5), titaniumMid = Color(0xFFC9CFD4), titaniumLow = Color(0xFF969DA4), titaniumDeep = Color(0xFF6B737B),
    tipCore = Color(0xFFFFFFFF),
    heroFill = Color(0xCC0D0E14), heroBorder = Color(0x1CFFFFFF),
)

// "Pastel Studio" light palette (custom re-skin): a soft cool-grey canvas, clean white cards, ink-black
// chrome, and one pastel colour world per domain: Charge = fresh green, Effort = sky blue,
// Rest = lavender, Stress = butter yellow. Data ramps stay saturated enough to read on white.
val LightTokens = PaletteTokens(
    surfaceBase = Color(0xFFF1F2EF), surfaceRaised = Color(0xFFFFFFFF), surfaceOverlay = Color(0xFFFFFFFF),
    surfaceInset = Color(0xFFEAECE7), hairline = Color(0xFFE3E5E0), hairlineStrong = Color(0xFFD2D5CE),
    textPrimary = Color(0xFF15171A), textSecondary = Color(0xFF585E66), textTertiary = Color(0xFF8C9299),
    glowAmbient = Color(0xFFE6F2D6),
    // Ink-black chrome accent (selected tabs, primary buttons, switches) with white text on top.
    accent = Color(0xFF1C1F23), accentHover = Color(0xFF3A3F46), accentMuted = Color(0xFFE8EAE5), focusRing = Color(0xFF1C1F23),
    recovery000 = Color(0xFFE5533D), recovery030 = Color(0xFFF08A3C), recovery055 = Color(0xFFE6BE34),
    recovery078 = Color(0xFF8CC63F), recovery100 = Color(0xFF3DAE5C),
    strain000 = Color(0xFF9FD0E6), strain033 = Color(0xFF5BB3D6), strain066 = Color(0xFF2F8FC4), strain100 = Color(0xFF1F6AA5),
    sleepAwake = Color(0xFFB5BBC4), sleepLight = Color(0xFF8FB4F0), sleepDeep = Color(0xFF4D5CC4), sleepREM = Color(0xFF9B7FE0),
    zone1 = Color(0xFF9DB2C2), zone2 = Color(0xFF5BB3D6), zone3 = Color(0xFF8CC63F), zone4 = Color(0xFFF0A43C), zone5 = Color(0xFFE5533D),
    statusPositive = Color(0xFF3DAE5C), statusWarning = Color(0xFFE39B2E), statusCritical = Color(0xFFE5533D),
    metricCyan = Color(0xFF3AA8C8), metricPurple = Color(0xFF8A6FD8), metricAmber = Color(0xFFE39B2E), metricRose = Color(0xFFE5647A),
    chargeColor = Color(0xFF7BBF45), chargeDeep = Color(0xFF4E9A2E), chargeBright = Color(0xFFB2DE86), chargeGlow = Color(0xFF9AD062),
    effortColor = Color(0xFF4FAED6), effortDeep = Color(0xFF2A86B8), effortBright = Color(0xFF9DD3EA), effortGlow = Color(0xFF6CC0E0),
    restColor = Color(0xFF7E8BE0), restDeep = Color(0xFF4F5CC0), restBright = Color(0xFFB3BAF2), restGlow = Color(0xFF8F9BEA),
    stressColor = Color(0xFFE6B92E), stressDeep = Color(0xFF7BBF45), stressBright = Color(0xFFE5623D), stressGlow = Color(0xFFF2CF55),
    scenicCenter = Color(0xFFF8F9F6), scenicEdge = Color(0xFFEEF0EB), scenicStar = Color(0xFFD9DDD4),
    cardFillTop = Color(0xFFFFFFFF), cardFillBottom = Color(0xFFFAFBF8),
    gold = Color(0xFFF2D264), goldLight = Color(0xFFF8E49A), goldDeep = Color(0xFFD8B23A),
    // Text drawn on the accent (primary buttons) — white on the ink accent.
    goldDeepText = Color(0xFFFFFFFF), signalYellow = Color(0xFFF2C230),
    titaniumTop = Color(0xFFE6E8E4), titaniumMid = Color(0xFFC8CCC6), titaniumLow = Color(0xFF9CA29B), titaniumDeep = Color(0xFF6B716B),
    tipCore = Color(0xFF15171A),
    heroFill = Color(0xFFFFFFFF), heroBorder = Color(0x0F000000),
)

// MARK: - Chart style (data-viz colour mode) + the Classic throwback ramps

enum class ChartStyle(val storageValue: String, val label: String) {
    TITANIUM("titanium", "Titanium"),
    CLASSIC("classic", "Classic");

    companion object {
        fun fromStorage(raw: String?): ChartStyle = entries.firstOrNull { it.storageValue == raw } ?: TITANIUM
    }
}

/** How a custom background image is scaled to the screen (#custom-background). [storageValue] is
 *  byte-identical to the iOS `BackgroundFillMode` rawValue so the pref reads the same on both
 *  platforms: fill → aspect-crop, fit → aspect-fit, stretch → no-aspect fill, tile → repeat. Labels
 *  are localized at the Settings call site (not in the enum), so no hardcoded UI literal lives here. */
enum class BackgroundFillMode(val storageValue: String) {
    FILL("fill"),
    FIT("fit"),
    STRETCH("stretch"),
    TILE("tile");

    companion object {
        /** Tolerant parse — an unknown/legacy rawValue falls back to [FILL] (the default). */
        fun fromStorage(raw: String?): BackgroundFillMode =
            entries.firstOrNull { it.storageValue == raw } ?: FILL
    }
}

/** Chart-colour preference, persisted in `noop_prefs` and mirrored in snapshot state so a flip
 *  re-colours every gauge/chart live (the Palette ramp accessors read [ChartStylePrefs.style]). */
object ChartStylePrefs {
    private const val FILE = "noop_prefs"
    private const val KEY = "chart.style"
    private fun prefs(ctx: Context): SharedPreferences =
        ctx.applicationContext.getSharedPreferences(FILE, Context.MODE_PRIVATE)

    var style by mutableStateOf(ChartStyle.TITANIUM)
        private set

    fun load(ctx: Context) {
        style = ChartStyle.fromStorage(prefs(ctx).getString(KEY, ChartStyle.TITANIUM.storageValue))
    }

    fun set(ctx: Context, value: ChartStyle) {
        style = value
        prefs(ctx).edit().putString(KEY, value.storageValue).apply()
    }
}

/** The Classic (throwback) data ramps — light/dark tuned. Picked by the Palette accessors when
 *  ChartStylePrefs.style == CLASSIC. Surfaces/text/accent are never classic — only data encodings. */
data class ClassicRamp(
    val recovery: List<Pair<Float, Color>>,
    val strain: List<Pair<Float, Color>>,
    val stress: List<Pair<Float, Color>>,
    val sleepAwake: Color, val sleepLight: Color, val sleepDeep: Color, val sleepREM: Color,
    val zone1: Color, val zone2: Color, val zone3: Color, val zone4: Color, val zone5: Color,
    val statusPositive: Color, val statusWarning: Color, val statusCritical: Color,
    val metricCyan: Color, val metricPurple: Color, val metricAmber: Color, val metricRose: Color,
    val chargeColor: Color, val chargeDeep: Color, val chargeBright: Color,
    val effortColor: Color, val effortDeep: Color, val effortBright: Color,
    val restColor: Color, val restDeep: Color, val restBright: Color,
    val stressColor: Color, val stressDeep: Color, val stressBright: Color,
)

val ClassicDark = ClassicRamp(
    recovery = listOf(0.0f to Color(0xFFE5483B), 0.30f to Color(0xFFEE8B3C), 0.55f to Color(0xFFF2C53D), 0.78f to Color(0xFFA6D04E), 1.0f to Color(0xFF46B45A)),
    strain = listOf(0.0f to Color(0xFF7FB2E8), 0.33f to Color(0xFF4A90E2), 0.66f to Color(0xFF2F6FCB), 1.0f to Color(0xFF1E4FA0)),
    stress = listOf(0.0f to Color(0xFF46B45A), 0.5f to Color(0xFFF2C53D), 1.0f to Color(0xFFE5483B)),
    sleepAwake = Color(0xFFC9CCD6), sleepLight = Color(0xFF6FA8E8), sleepDeep = Color(0xFF2A4C8F), sleepREM = Color(0xFF8E6FD6),
    zone1 = Color(0xFF9AA7B5), zone2 = Color(0xFF46B45A), zone3 = Color(0xFFF2C53D), zone4 = Color(0xFFEE8B3C), zone5 = Color(0xFFE5483B),
    statusPositive = Color(0xFF46B45A), statusWarning = Color(0xFFF2C53D), statusCritical = Color(0xFFE5483B),
    metricCyan = Color(0xFF3FA9C9), metricPurple = Color(0xFF8E6FD6), metricAmber = Color(0xFFF2C53D), metricRose = Color(0xFFE5483B),
    chargeColor = Color(0xFF46B45A), chargeDeep = Color(0xFF2E9E4F), chargeBright = Color(0xFF86D98E),
    effortColor = Color(0xFF4A90E2), effortDeep = Color(0xFF2F6FCB), effortBright = Color(0xFF7FB2E8),
    restColor = Color(0xFF6FA8E8), restDeep = Color(0xFF2A4C8F), restBright = Color(0xFF8E6FD6),
    stressColor = Color(0xFFF2C53D), stressDeep = Color(0xFF46B45A), stressBright = Color(0xFFE5483B),
)

val ClassicLight = ClassicRamp(
    recovery = listOf(0.0f to Color(0xFFCB3A2F), 0.30f to Color(0xFFD87328), 0.55f to Color(0xFFCFA528), 0.78f to Color(0xFF74A53A), 1.0f to Color(0xFF2E9E4F)),
    strain = listOf(0.0f to Color(0xFF5E92D6), 0.33f to Color(0xFF3A74C4), 0.66f to Color(0xFF284F9C), 1.0f to Color(0xFF1C3E80)),
    stress = listOf(0.0f to Color(0xFF2E9E4F), 0.5f to Color(0xFFCFA528), 1.0f to Color(0xFFCB3A2F)),
    sleepAwake = Color(0xFF8C95A3), sleepLight = Color(0xFF3A80D6), sleepDeep = Color(0xFF203E73), sleepREM = Color(0xFF6A4FC0),
    zone1 = Color(0xFF828D9B), zone2 = Color(0xFF2E9E4F), zone3 = Color(0xFFCFA528), zone4 = Color(0xFFD87328), zone5 = Color(0xFFCB3A2F),
    statusPositive = Color(0xFF2E9E4F), statusWarning = Color(0xFFCFA528), statusCritical = Color(0xFFCB3A2F),
    metricCyan = Color(0xFF2E92B4), metricPurple = Color(0xFF6A4FC0), metricAmber = Color(0xFFCFA528), metricRose = Color(0xFFCB3A2F),
    chargeColor = Color(0xFF2E9E4F), chargeDeep = Color(0xFF207A3C), chargeBright = Color(0xFF5FBE6E),
    effortColor = Color(0xFF3A74C4), effortDeep = Color(0xFF284F9C), effortBright = Color(0xFF5E92D6),
    restColor = Color(0xFF3A80D6), restDeep = Color(0xFF203E73), restBright = Color(0xFF6A4FC0),
    stressColor = Color(0xFFCFA528), stressDeep = Color(0xFF2E9E4F), stressBright = Color(0xFFCB3A2F),
)

// MARK: - Appearance preference (System / Light / Dark)

enum class AppearanceMode(val storageValue: String, val label: String) {
    SYSTEM("system", "System"),
    LIGHT("light", "Light"),
    DARK("dark", "Dark");

    companion object {
        fun fromStorage(raw: String?): AppearanceMode =
            entries.firstOrNull { it.storageValue == raw } ?: LIGHT
    }
}

/**
 * How heavy the numeral over a gauge is drawn (#2346).
 *
 * `BOLD` is the shipped look and stays the default, so nothing moves for anyone who does not ask. `SOFT`
 * drops to `NoopType.number`'s own default weight, which is what every other number in the app uses.
 *
 * Named for the effect rather than a font weight: the reporter asked for "softer", and the two platforms
 * reach it through different weight vocabularies. Twin of the Apple `GaugeNumeralStyle`.
 */
enum class GaugeNumeralStyle(val storageValue: String) {
    BOLD("bold"),
    SOFT("soft");

    companion object {
        fun fromStorage(raw: String?): GaugeNumeralStyle =
            entries.firstOrNull { it.storageValue == raw } ?: BOLD
    }
}

/** Theme preference, persisted in `noop_prefs` and mirrored in snapshot state so the toggle is live.
 *  [load] is called once from MainActivity before first composition (no flash); [set] writes + flips. */
object AppearancePrefs {
    private const val FILE = "noop_prefs"
    private const val KEY = "theme.appearance"
    private const val GAUGE_KEY = "appearance.gaugeNumerals"

    private fun prefs(ctx: Context): SharedPreferences =
        ctx.applicationContext.getSharedPreferences(FILE, Context.MODE_PRIVATE)

    /** Live appearance mode read by NoopTheme; defaults to System until [load] runs. */
    var mode by mutableStateOf(AppearanceMode.LIGHT)
        private set

    /**
     * Gauge numeral weight (#2346). Deliberately NOT in the `.noopbak` whitelist, matching [mode] beside
     * it: that contract carries profile, units and anything that changes a NUMBER, while a display choice
     * is device-local. Adding it would touch a byte-identical cross-platform contract for a preference
     * that means nothing on another device.
     */
    var gaugeNumerals by mutableStateOf(GaugeNumeralStyle.BOLD)
        private set

    fun load(ctx: Context) {
        mode = AppearanceMode.fromStorage(prefs(ctx).getString(KEY, AppearanceMode.LIGHT.storageValue))
        gaugeNumerals = GaugeNumeralStyle.fromStorage(
            prefs(ctx).getString(GAUGE_KEY, GaugeNumeralStyle.BOLD.storageValue))
    }

    fun setGaugeNumerals(ctx: Context, value: GaugeNumeralStyle) {
        gaugeNumerals = value
        prefs(ctx).edit().putString(GAUGE_KEY, value.storageValue).apply()
    }

    fun set(ctx: Context, value: AppearanceMode) {
        mode = value
        prefs(ctx).edit().putString(KEY, value.storageValue).apply()
    }
}

/** The user's CHROME accent colour (buttons/links/selection/focus). Chrome only — the recovery/strain/
 *  sleep DATA colour worlds are never themed by this. Twin of macOS `AccentColor` (StrandDesign). */
enum class AccentColor(val storageValue: String, val label: String) {
    MINT("mint", "Mint"),
    WHOOP_BLUE("whoopBlue", "WHOOP Blue"),
    CUSTOM("custom", "Custom");

    companion object {
        /** Seeds the custom picker (mint) so a fresh Custom selection is not black. */
        const val DEFAULT_CUSTOM_HEX = "#149A78"

        fun fromStorage(raw: String?): AccentColor =
            entries.firstOrNull { it.storageValue == raw } ?: MINT

        /** Parse `#RRGGBB` to a Color, falling back to [fallback] on any malformed value. */
        fun parseHex(hex: String, fallback: Color): Color = try {
            Color(("FF" + hex.removePrefix("#").trim()).toLong(16))
        } catch (e: Exception) {
            fallback
        }

        /** Blend a hex toward white by [amount] (0..1) — the deterministic hover for a custom accent. */
        fun lighten(hex: String, amount: Float = 0.24f): Color {
            val c = parseHex(hex, Color(0xFF149A78))
            fun up(x: Float) = (x + (1 - x) * amount).coerceIn(0f, 1f)
            return Color(up(c.red), up(c.green), up(c.blue), 1f)
        }
    }
}

/** Chrome-accent preference, persisted in `noop_prefs` + snapshot state so the picker is live. [load] is
 *  called once from MainActivity; [setColor]/[setCustomHex] write + flip. Twin of the macOS
 *  `@AppStorage(AccentColor.storageKey/customHexKey)` app-root wiring. */
object AccentPrefs {
    private const val FILE = "noop_prefs"
    private const val KEY_COLOR = "accent.color"
    private const val KEY_CUSTOM = "accent.customHex"

    private fun prefs(ctx: Context): SharedPreferences =
        ctx.applicationContext.getSharedPreferences(FILE, Context.MODE_PRIVATE)

    /** Live accent choice + custom hex read by the Palette accent getters; defaults until [load] runs. */
    var color by mutableStateOf(AccentColor.MINT)
        private set
    var customHex by mutableStateOf(AccentColor.DEFAULT_CUSTOM_HEX)
        private set

    fun load(ctx: Context) {
        color = AccentColor.fromStorage(prefs(ctx).getString(KEY_COLOR, AccentColor.MINT.storageValue))
        customHex = prefs(ctx).getString(KEY_CUSTOM, AccentColor.DEFAULT_CUSTOM_HEX)
            ?: AccentColor.DEFAULT_CUSTOM_HEX
    }

    fun setColor(ctx: Context, value: AccentColor) {
        color = value
        prefs(ctx).edit().putString(KEY_COLOR, value.storageValue).apply()
    }

    fun setCustomHex(ctx: Context, hex: String) {
        customHex = hex
        prefs(ctx).edit().putString(KEY_CUSTOM, hex).apply()
    }
}

/** A named THEME preset — a one-tap bundle coordinating accent + chart world + backdrop + card opacity.
 *  Pure orchestration over prefs that already exist; DERIVED (no stored value) via [matching], so tweaking
 *  any granular control resolves to [CUSTOM]. Theme MODE (light/dark) is independent. Twin of macOS
 *  `ThemePreset`. */
enum class ThemePreset(
    val storageValue: String,
    val label: String,
    val accent: AccentColor?,   // null for CUSTOM
    val chart: ChartStyle?,
    val backdrop: Boolean,
    val cardOpacity: Int,       // percent, 100 = solid
) {
    MINT("mint", "Mint", AccentColor.MINT, ChartStyle.TITANIUM, true, 100),
    OCEAN("ocean", "Ocean", AccentColor.WHOOP_BLUE, ChartStyle.TITANIUM, true, 100),
    CLASSIC("classic", "Classic", AccentColor.WHOOP_BLUE, ChartStyle.CLASSIC, true, 100),
    MIDNIGHT("midnight", "Midnight", AccentColor.MINT, ChartStyle.TITANIUM, false, 100),
    FROSTED("frosted", "Frosted", AccentColor.MINT, ChartStyle.TITANIUM, true, 85),
    CUSTOM("custom", "Custom", null, null, true, 100);

    companion object {
        /** The presets a user can pick (everything but the derived CUSTOM sentinel). */
        val selectable: List<ThemePreset> get() = entries.filter { it != CUSTOM }

        /** Which preset the live prefs correspond to, or CUSTOM when none match. */
        fun matching(accent: AccentColor, chart: ChartStyle, backdrop: Boolean, cardOpacity: Int): ThemePreset =
            selectable.firstOrNull {
                it.accent == accent && it.chart == chart && it.backdrop == backdrop && it.cardOpacity == cardOpacity
            } ?: CUSTOM
    }
}
