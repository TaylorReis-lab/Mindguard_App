package com.mindguard.app.vpn

import android.annotation.SuppressLint
import android.content.Intent
import android.net.VpnService
import android.os.ParcelFileDescriptor
import android.util.Log
import com.mindguard.app.data.MindguardRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

@SuppressLint("VpnServicePolicy")
class MindguardVpnService : VpnService() {

    private var vpnInterface: ParcelFileDescriptor? = null
    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)
    private lateinit var repository: MindguardRepository

    override fun onCreate() {
        super.onCreate()
        repository = MindguardRepository(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == "STOP") {
            stopVpn()
            return START_NOT_STICKY
        }
        startVpn()
        return START_STICKY
    }

    private fun startVpn() {
        if (vpnInterface != null) return

        val builder = Builder()
            .setSession("Mindguard Block")
            .addAddress("10.0.0.2", 32)
            // DNS AdGuard Family (Bloqueia Adulto + Ads)
            .addDnsServer("94.140.14.15") 
            .addDnsServer("94.140.15.15")
            // Crucial: Não adicionar addRoute("0.0.0.0", 0) para não sequestrar o tráfego de dados.
            // Ao não definir rotas globais, o Android usa a VPN apenas para DNS (Split-Tunneling).
            // Isso evita a mensagem "Network has no internet access".
            .setMtu(1500)

        try {
            vpnInterface = builder.establish()
            Log.d("MindguardVPN", "Proteção DNS Ativa (Modo Split)")
        } catch (e: Exception) {
            Log.e("MindguardVPN", "Falha ao iniciar VPN", e)
        }
    }

    private fun stopVpn() {
        vpnInterface?.close()
        vpnInterface = null
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
        stopVpn()
    }
}
