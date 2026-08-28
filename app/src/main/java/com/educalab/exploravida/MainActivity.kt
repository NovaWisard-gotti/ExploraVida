package com.educalab.exploravida

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.educalab.exploravida.ui.navigation.ExploraVidaNavHost
import com.educalab.exploravida.ui.theme.ExploraVidaTheme
import com.educalab.exploravida.ui.theme.LabColors

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        val repository = (application as ExploraVidaApp).repository
        setContent {
            ExploraVidaTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = LabColors.Deep) {
                    ExploraVidaNavHost(repository = repository)
                }
            }
        }
    }
}
