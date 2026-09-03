package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.data.auth.AuthManager
import com.example.data.db.AppDatabase
import com.example.data.repository.RideOneRepository
import com.example.ui.RideOneApp
import com.example.ui.theme.MyApplicationTheme
import com.example.utils.LocationService
import com.example.viewmodel.RideOneViewModel

class MainActivity : ComponentActivity() {

  private val viewModel: RideOneViewModel by viewModels {
    object : ViewModelProvider.Factory {
      @Suppress("UNCHECKED_CAST")
      override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val database = AppDatabase.getInstance(applicationContext)
        val repository = RideOneRepository(
          bookingDao = database.bookingDao(),
          savedPlaceDao = database.savedPlaceDao(),
          emergencyContactDao = database.emergencyContactDao()
        )
        val authManager = AuthManager(applicationContext)
        val locationService = LocationService(applicationContext)
        return RideOneViewModel(
          repository = repository,
          authManager = authManager,
          locationService = locationService
        ) as T
      }
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        RideOneApp(viewModel = viewModel)
      }
    }
  }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
  Text(text = "Hello $name!", modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
  MyApplicationTheme { Greeting("Android") }
}

