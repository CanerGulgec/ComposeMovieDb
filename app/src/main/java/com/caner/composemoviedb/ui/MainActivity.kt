package com.caner.composemoviedb.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.lifecycle.lifecycleScope
import com.caner.composemoviedb.domain.local.ThemeManager
import com.caner.composemoviedb.ui.view.*
import com.caner.composemoviedb.ui.theme.MovieItemComposeTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@ExperimentalPagerApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var themeManager: ThemeManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val darkMode by themeManager.uiModeFlow.collectAsState(initial = isSystemInDarkTheme())

            /**
             * Set UI Mode accordingly
             */
            val toggleTheme: () -> Unit = {
                lifecycleScope.launch {
                    themeManager.setDarkMode(!darkMode)
                }
            }

            MovieItemComposeTheme(darkTheme = darkMode) {
                SetStatusBarColor()
                Home(toggleTheme)
            }
        }
        observeThemeMode()
    }

    private fun observeThemeMode() {
        lifecycleScope.launchWhenStarted {
            themeManager.uiModeFlow.collect {
                val mode = when (it) {
                    true -> AppCompatDelegate.MODE_NIGHT_YES
                    false -> AppCompatDelegate.MODE_NIGHT_NO
                }
                AppCompatDelegate.setDefaultNightMode(mode)
            }
        }
    }
}

@Composable
fun SetStatusBarColor() {
    val systemUiController = rememberSystemUiController()
    val color = MaterialTheme.colors.surface

    SideEffect {
        systemUiController.setStatusBarColor(
            color = color,
            darkIcons = true
        )
    }
}