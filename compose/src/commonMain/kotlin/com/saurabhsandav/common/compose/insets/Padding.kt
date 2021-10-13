/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:Suppress("NOTHING_TO_INLINE", "unused", "KDocUnresolvedReference")

package com.saurabhsandav.common.compose.insets

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.*

/**
 * Selectively apply additional space which matches the width/height of any system bars present
 * on the respective edges of the screen.
 *
 * @param enabled Whether to apply padding using the system bars dimensions on the respective edges.
 * Defaults to `true`.
 */
public expect fun Modifier.systemBarsPadding(
    enabled: Boolean = true
): Modifier

/**
 * Apply additional space which matches the height of the status bars height along the top edge
 * of the content.
 */
public expect fun Modifier.statusBarsPadding(): Modifier

/**
 * Apply additional space which matches the height of the navigation bars height
 * along the [bottom] edge of the content, and additional space which matches the width of
 * the navigation bars on the respective [start] and [end] edges.
 *
 * @param bottom Whether to apply padding to the bottom edge, which matches the navigation bars
 * height (if present) at the bottom edge of the screen. Defaults to `true`.
 * @param start Whether to apply padding to the left edge, which matches the navigation bars width
 * (if present) on the left edge of the screen. Defaults to `true`.
 * @param end Whether to apply padding to the right edge, which matches the navigation bars width
 * (if present) on the right edge of the screen. Defaults to `true`.
 */
public expect fun Modifier.navigationBarsPadding(
    bottom: Boolean = true,
    start: Boolean = true,
    end: Boolean = true
): Modifier

/**
 * Apply additional space which matches the height of the [WindowInsets.ime] (on-screen keyboard)
 * height along the bottom edge of the content.
 *
 * This method has no special handling for the [WindowInsets.navigationBars], which usually
 * intersect the [WindowInsets.ime]. Most apps will usually want to use the
 * [Modifier.navigationBarsWithImePadding] modifier.
 */
public expect fun Modifier.imePadding(): Modifier

/**
 * Apply additional space which matches the height of the [WindowInsets.ime] (on-screen keyboard)
 * height and [WindowInsets.navigationBars]. This is what apps should use to handle any insets
 * at the bottom of the screen.
 */
public expect fun Modifier.navigationBarsWithImePadding(): Modifier
