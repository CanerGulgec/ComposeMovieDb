package com.caner.composemoviedb.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import com.caner.composemoviedb.MovieApp
import com.caner.composemoviedb.ui.screen.BottomNavigationBar
import com.caner.composemoviedb.ui.screen.FloatingButton
import com.caner.composemoviedb.ui.screen.Navigation
import com.caner.composemoviedb.ui.theme.MovieItemComposeTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var app: MovieApp

    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieItemComposeTheme(darkTheme = app.isDark.value) {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = { BottomNavigationBar(navController) },
                    floatingActionButton = {
                        val rippleExplode = remember { mutableStateOf(false) }
                        FloatingButton(rippleExplode, app)
                        if (rippleExplode.value) {

                        }
                    },
                    content = {
                        Navigation(navController = navController)
                    }
                )
            }
        }
    }
}