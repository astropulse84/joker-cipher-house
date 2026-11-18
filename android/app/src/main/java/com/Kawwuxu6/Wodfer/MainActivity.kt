package com.Kawwuxu6.Wodfer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.Kawwuxu6.Wodfer.core.di.ServiceLocator
import com.Kawwuxu6.Wodfer.core.theme.AppTheme
import com.Kawwuxu6.Wodfer.presentation.app.AppRoot

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        ServiceLocator.init(applicationContext)
        setContent {
            AppTheme {
                AppRoot()
            }
        }
    }
}
