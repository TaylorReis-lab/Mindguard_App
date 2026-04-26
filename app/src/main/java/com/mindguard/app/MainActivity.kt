package com.mindguard.app

import android.content.Intent
import android.net.VpnService
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.mindguard.app.ui.MainScreen
import com.mindguard.app.ui.theme.MindguardappTheme
import com.mindguard.app.viewmodel.MindguardViewModel
import com.mindguard.app.vpn.MindguardVpnService
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    
    private val viewModel: MindguardViewModel by viewModels()

    private val vpnPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            startVpnService()
        } else {
            viewModel.toggleVpn(false)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setupCrashHandler()

        lifecycleScope.launch {
            viewModel.isVpnActive.collectLatest { active ->
                if (active) {
                    prepareVpn()
                } else {
                    stopVpnService()
                }
            }
        }
        
        setContent {
            MindguardappTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    MainScreen()
                }
            }
        }
    }

    private fun prepareVpn() {
        val intent = VpnService.prepare(this)
        if (intent != null) {
            vpnPermissionLauncher.launch(intent)
        } else {
            startVpnService()
        }
    }

    private fun startVpnService() {
        val intent = Intent(this, MindguardVpnService::class.java)
        startService(intent)
    }

    private fun stopVpnService() {
        val intent = Intent(this, MindguardVpnService::class.java).apply {
            action = "STOP"
        }
        startService(intent)
    }

    private fun setupCrashHandler() {
        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Log.e("MindguardCrash", "Uncaught exception", throwable)
            // Error report via email logic could go here
            defaultHandler?.uncaughtException(thread, throwable)
        }
    }
}
