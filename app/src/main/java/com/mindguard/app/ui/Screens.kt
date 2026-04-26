package com.mindguard.app.ui

import android.content.Intent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.Launch
import androidx.compose.material.icons.automirrored.rounded.Login
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mindguard.app.BlockActivity
import com.mindguard.app.data.Translations
import com.mindguard.app.viewmodel.MindguardViewModel
import java.text.SimpleDateFormat
import java.util.*

// --- CSS-based Style Helpers ---
val GlassColor = Color(255, 255, 255, 15) // rgba(255,255,255,0.06)
val GlassBorderColor = Color(255, 255, 255, 20) // rgba(255,255,255,0.08)

fun Modifier.glass(radius: Int = 20) = this
    .background(GlassColor)
    .border(1.dp, GlassBorderColor, RoundedCornerShape(radius.dp))

@Composable
fun EntranceAnimation(delay: Int = 0, content: @Composable () -> Unit) {
    val visible = remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(delay.toLong())
        visible.value = true
    }
    AnimatedVisibility(
        visible = visible.value,
        enter = slideInVertically(initialOffsetY = { 40 }) + fadeIn(animationSpec = tween(600)),
        content = { content() }
    )
}

fun translateLabel(label: String, lang: String): String {
    return Translations.get(lang, "nav.${label.lowercase()}")
}

@Composable
fun MainScreen() {
    val viewModel: MindguardViewModel = viewModel()
    var selectedTab by remember { mutableIntStateOf(0) }
    val lang by viewModel.language.collectAsState()

    Scaffold(
        containerColor = Color(0xFF0A0A1A),
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF0A0A1A).copy(alpha = 0.95f),
                tonalElevation = 0.dp,
                modifier = Modifier.border(1.dp, GlassBorderColor, RoundedCornerShape(0.dp))
            ) {
                val items = listOf(
                    Triple(0, "Home", Icons.Rounded.Shield),
                    Triple(1, "Activity", Icons.Rounded.History),
                    Triple(2, "Lista", Icons.Rounded.Language),
                    Triple(3, "Profile", Icons.Rounded.Person)
                )
                items.forEach { (index, label, icon) ->
                    NavigationBarItem(
                        icon = { Icon(icon, contentDescription = label, modifier = Modifier.size(24.dp)) },
                        label = { Text(Translations.get(lang, "nav.${label.lowercase()}"), style = MaterialTheme.typography.labelSmall) },
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF7C3AED),
                            unselectedIconColor = Color(0xFF64748B),
                            indicatorColor = Color(0xFF7C3AED).copy(alpha = 0.1f)
                        )
                    )
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (selectedTab) {
                0 -> DashboardScreen(viewModel)
                1 -> HistoryScreen(viewModel)
                2 -> CustomBlocklistScreen(viewModel)
                3 -> ProfileWrapper(viewModel)
            }
        }
    }
}

@Composable
fun ProfileWrapper(viewModel: MindguardViewModel) {
    var subScreen by remember { mutableStateOf("main") } // main, settings, privacy, about
    val lang by viewModel.language.collectAsState()
    
    fun t(key: String) = Translations.get(lang, key)

    AnimatedContent(targetState = subScreen, label = "profile_nav") { screen ->
        when (screen) {
            "main" -> ProfilePage(viewModel, onNavigate = { subScreen = it })
            "settings" -> SettingsScreen(viewModel, onBack = { subScreen = "main" })
            "privacy" -> PrivacyPage(lang, onBack = { subScreen = "main" })
            "about" -> AboutPage(viewModel, onBack = { subScreen = "main" })
        }
    }
}

@Composable
fun DashboardScreen(viewModel: MindguardViewModel) {
    val stats by viewModel.userStats.collectAsState()
    val isEnabled by viewModel.isVpnActive.collectAsState()
    val lang by viewModel.language.collectAsState()
    val context = LocalContext.current
    
    fun t(key: String) = Translations.get(lang, key)

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
        contentPadding = PaddingValues(top = 20.dp, bottom = 24.dp)
    ) {
        item {
            EntranceAnimation(delay = 100) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row {
                            Text("Mind", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold, color = Color.White)
                            Text("Guard", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold, color = Color(0xFF7C3AED))
                        }
                        Text(t("app.slogan"), style = MaterialTheme.typography.labelSmall, color = Color(0xFF94A3B8))
                    }
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .shadow(16.dp, RoundedCornerShape(16.dp), spotColor = Color(0xFF7C3AED))
                            .clip(RoundedCornerShape(16.dp))
                            .background(Brush.linearGradient(listOf(Color(0xFF7C3AED), Color(0xFF4F46E5)))),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🧠", fontSize = 26.sp)
                    }
                }
            }
        }

        item {
            EntranceAnimation(delay = 200) {
                DNSToggleSection(enabled = isEnabled, onToggle = { viewModel.toggleVpn(it) })
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            EntranceAnimation(delay = 300) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    StatCardCSS(
                        icon = "🔞", 
                        label = "Bloqueios", 
                        value = stats.totalBlockedAttempts, 
                        color = Color(0xFFEF4444), 
                        modifier = Modifier.weight(1f)
                    )
                    StatCardCSS(
                        icon = "📉", 
                        label = "Tentativas", 
                        value = stats.totalFailedAttempts, 
                        color = Color(0xFFF59E0B), 
                        modifier = Modifier.weight(1f)
                    )
                    StatCardCSS(
                        icon = "🛡️", 
                        label = "Seguro", 
                        value = stats.daysClean, 
                        color = Color(0xFF10B981), 
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            EntranceAnimation(delay = 400) {
                StreakProgressCardCSS(days = stats.daysClean)
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            EntranceAnimation(delay = 500) {
                MotivationCardCSS(message = viewModel.motivationalMessage)
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
        
        item {
            EntranceAnimation(delay = 600) {
                Button(
                    onClick = { 
                        viewModel.simulateBlock()
                        val intent = Intent(context, BlockActivity::class.java).apply {
                            putExtra("url", "exemplo-vicio.com")
                        }
                        context.startActivity(intent)
                    },
                    modifier = Modifier.fillMaxWidth().height(58.dp).glass(16),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Icon(Icons.AutoMirrored.Rounded.Launch, null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Testar Escudo de Bloqueio", style = MaterialTheme.typography.bodyMedium, color = Color.White)
                }
            }
        }
    }
}

@Composable
fun DNSToggleSection(enabled: Boolean, onToggle: (Boolean) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().height(110.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorderColor)
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = if (enabled) "Proteção Ativa" else "Proteção Inativa",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (enabled) Color.White else Color(0xFF94A3B8)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(if (enabled) Color(0xFF10B981) else Color(0xFF64748B)))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (enabled) "Filtro AdGuard Ativo" else "Escudo desativado",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (enabled) Color(0xFF10B981) else Color(0xFF64748B)
                    )
                }
            }
            
            Box(
                modifier = Modifier
                    .size(64.dp, 34.dp)
                    .clip(CircleShape)
                    .background(
                        if (enabled) Brush.linearGradient(listOf(Color(0xFF10B981), Color(0xFF059669)))
                        else Brush.linearGradient(listOf(Color.White.copy(alpha = 0.15f), Color.White.copy(alpha = 0.15f)))
                    )
                    .clickable { onToggle(!enabled) }
                    .padding(3.dp)
            ) {
                val offset by animateDpAsState(if (enabled) 30.dp else 0.dp, label = "toggle")
                Box(
                    modifier = Modifier
                        .offset(x = offset)
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .shadow(6.dp, CircleShape)
                )
            }
        }
    }
}

@Composable
fun StatCardCSS(icon: String, label: String, value: Int, color: Color, modifier: Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.08f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.15f))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(icon, fontSize = 22.sp)
            Spacer(modifier = Modifier.height(10.dp))
            Text("$value", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color.White)
            Text(label, style = MaterialTheme.typography.labelSmall, color = Color(0xFF94A3B8), fontSize = 10.sp)
        }
    }
}

@Composable
fun StreakProgressCardCSS(days: Int) {
    val progress = (days / 30f).coerceIn(0f, 1f)
    Card(
        modifier = Modifier.fillMaxWidth().glass(24),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(50.dp).clip(CircleShape).background(Color(0xFF7C3AED).copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text("🔥", fontSize = 28.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("$days dias protegidos", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
                    color = Color(0xFF7C3AED),
                    trackColor = Color.White.copy(alpha = 0.05f)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text("${(progress * 100).toInt()}%", style = MaterialTheme.typography.labelSmall, color = Color(0xFF7C3AED), fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun MotivationCardCSS(message: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.03f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
    ) {
        Row(modifier = Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(36.dp).clip(CircleShape).background(Color(0xFFF59E0B).copy(alpha = 0.1f)), contentAlignment = Alignment.Center) {
                Text("✨", fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(message, style = MaterialTheme.typography.bodySmall, color = Color(0xFFCBD5E1), lineHeight = 18.sp)
        }
    }
}

@Composable
fun HistoryScreen(viewModel: MindguardViewModel) {
    val events by viewModel.allEvents.collectAsState()
    val dateFormat = SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault())

    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Text("HISTÓRICO", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold, letterSpacing = 2.sp)
        Spacer(modifier = Modifier.height(24.dp))
        
        if (events.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Rounded.Inbox, null, modifier = Modifier.size(48.dp), tint = Color(0xFF334155))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Sem atividade recente.", color = Color(0xFF64748B))
                }
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                items(events) { event ->
                    Card(modifier = Modifier.fillMaxWidth().glass(16), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.Transparent)) {
                        ListItem(
                            colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                            headlineContent = { Text(event.url, fontWeight = FontWeight.Bold, color = Color.White) },
                            supportingContent = { 
                                Text(
                                    "${if(event.type == "ADULT") "Bloqueio Adulto" else "Anúncio"} • ${dateFormat.format(Date(event.timestamp))}",
                                    color = if(event.type == "ADULT") Color(0xFFEF4444) else Color(0xFF94A3B8)
                                ) 
                            },
                            leadingContent = {
                                Box(modifier = Modifier.size(42.dp).clip(RoundedCornerShape(12.dp)).background(if(event.type == "ADULT") Color(0xFFEF4444).copy(alpha = 0.1f) else Color(0xFF7C3AED).copy(alpha = 0.1f)), contentAlignment = Alignment.Center) {
                                    Icon(if(event.type == "ADULT") Icons.Rounded.Block else Icons.Rounded.AdsClick, null, tint = if(event.type == "ADULT") Color(0xFFEF4444) else Color(0xFF7C3AED), modifier = Modifier.size(20.dp))
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CustomBlocklistScreen(viewModel: MindguardViewModel) {
    val list by viewModel.customBlocklist.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var newUrl by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Card(modifier = Modifier.fillMaxWidth().glass(20), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.Transparent)) {
            Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(44.dp).clip(CircleShape).background(Color(0xFF10B981).copy(alpha = 0.1f)), contentAlignment = Alignment.Center) {
                    Icon(Icons.Rounded.VerifiedUser, null, tint = Color(0xFF10B981), modifier = Modifier.size(24.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("FILTRO MESTRE ATIVO", fontWeight = FontWeight.Bold, color = Color(0xFF10B981))
                    Text("2.4M+ sites protegidos", style = MaterialTheme.typography.labelSmall, color = Color(0xFF94A3B8))
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("LISTA PERSONALIZADA", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            IconButton(onClick = { showDialog = true }, modifier = Modifier.glass(12).size(42.dp)) {
                Icon(Icons.Rounded.Add, null, tint = Color(0xFF7C3AED))
            }
        }
        
        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(list) { url ->
                Card(modifier = Modifier.fillMaxWidth().glass(16), shape = RoundedCornerShape(16.dp)) {
                    Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text(url, color = Color.White)
                        Icon(Icons.Rounded.Delete, null, tint = Color(0xFF64748B), modifier = Modifier.clickable { viewModel.removeCustomSite(url) })
                    }
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            containerColor = Color(0xFF1E293B),
            title = { Text("Bloquear Site") },
            text = { OutlinedTextField(value = newUrl, onValueChange = { newUrl = it }, label = { Text("URL") }) },
            confirmButton = { Button(onClick = { if(newUrl.isNotBlank()) { viewModel.addCustomSite(newUrl); newUrl = ""; showDialog = false } }) { Text("OK") } }
        )
    }
}

@Composable
fun ProfilePage(viewModel: MindguardViewModel, onNavigate: (String) -> Unit) {
    val lang by viewModel.language.collectAsState()
    val notificationsEnabled by viewModel.notificationsEnabled.collectAsState()
    val context = LocalContext.current
    
    fun t(key: String) = Translations.get(lang, key)

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
        contentPadding = PaddingValues(top = 32.dp, bottom = 40.dp)
    ) {
        // Profile header
        item {
            Column(
                modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(listOf(Color(0xFF7C3AED), Color(0xFF4F46E5))))
                        .border(3.dp, Color.White.copy(alpha = 0.1f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Rounded.Person, null, tint = Color.White, modifier = Modifier.size(40.dp))
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Taylor Reis", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(text = "taylor.reis@mindguard.com", style = MaterialTheme.typography.labelSmall, color = Color(0xFF94A3B8))
            }
        }

        // Account
        item {
            Text("Conta", style = MaterialTheme.typography.labelSmall, color = Color(0xFF64748B), fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            SettingsItem(icon = Icons.Rounded.Edit, label = t("profile.editProfile"), onClick = { /* TODO */ })
            SettingsItem(icon = Icons.Rounded.Language, label = t("profile.language"), value = Translations.LANGUAGES.find { it.code == lang }?.name ?: lang, onClick = { onNavigate("settings") })
        }

        // Security
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Text("Segurança", style = MaterialTheme.typography.labelSmall, color = Color(0xFF64748B), fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            SettingsItem(icon = Icons.Rounded.Lock, label = t("profile.security"), onClick = { onNavigate("settings") })
            SettingsItem(icon = Icons.Rounded.Shield, label = t("profile.privacy"), onClick = { onNavigate("privacy") })
        }

        // Preferences
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Text("Preferências", style = MaterialTheme.typography.labelSmall, color = Color(0xFF64748B), fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            SettingsItem(
                icon = Icons.Rounded.Notifications,
                label = t("profile.notifications"),
                value = if(notificationsEnabled) t("general.active") else t("general.inactive"),
                showArrow = false,
                onClick = { viewModel.toggleNotifications() }
            )
            SettingsItem(icon = Icons.Rounded.Info, label = t("about.title"), onClick = { onNavigate("about") })
        }

        // Logout
        item {
            Spacer(modifier = Modifier.height(24.dp))
            SettingsItem(icon = Icons.AutoMirrored.Rounded.Logout, label = t("profile.logout"), danger = true, onClick = {
                android.widget.Toast.makeText(context, "Saindo...", android.widget.Toast.LENGTH_SHORT).show()
            })
        }
        
        item {
            Spacer(modifier = Modifier.height(32.dp))
            Text(t("profile.version") + " 2.0.0", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, color = Color(0xFF334155), style = MaterialTheme.typography.labelSmall)
            Text(t("profile.developed"), modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, color = Color(0xFF334155), style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
fun SettingsScreen(viewModel: MindguardViewModel, onBack: () -> Unit) {
    val lang by viewModel.language.collectAsState()
    val context = LocalContext.current
    
    fun t(key: String) = Translations.get(lang, key)

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
        contentPadding = PaddingValues(top = 20.dp, bottom = 40.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 24.dp)) {
                IconButton(onClick = onBack, modifier = Modifier.glass(12).size(40.dp)) {
                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, null, tint = Color.White)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = t("nav.settings"), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }
        }

        // --- LOGIN / CONTA AREA ---
        item {
            Card(modifier = Modifier.fillMaxWidth().glass(20), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.Transparent)) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(48.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.05f)), contentAlignment = Alignment.Center) {
                            Icon(Icons.Rounded.AccountCircle, null, tint = Color.Gray, modifier = Modifier.size(32.dp))
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text("Acesse sua Conta", fontWeight = FontWeight.Bold, color = Color.White)
                            Text("Sincronize seus dados", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = { android.widget.Toast.makeText(context, "Módulo de Login em desenvolvimento", android.widget.Toast.LENGTH_SHORT).show() },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    ) {
                        Icon(Icons.AutoMirrored.Rounded.Login, null, tint = Color.Black, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(t("auth.login"), color = Color.Black, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }

        item {
            Text("TECNOLOGIA", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
            Spacer(modifier = Modifier.height(12.dp))
            SettingsItem(icon = Icons.Rounded.AdsClick, label = "AdGuard DNS", value = t("about.adguard"), onClick = {})
            SettingsItem(icon = Icons.Rounded.VerifiedUser, label = "BlockPorn", value = t("about.taylor"), onClick = {})
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }

        item {
            Text(t("lang.title").uppercase(), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
            Spacer(modifier = Modifier.height(12.dp))
            FlowRow(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Translations.LANGUAGES.forEach { info ->
                    LanguageButton(
                        label = info.flag + " " + info.name,
                        selected = lang == info.code,
                        onClick = { viewModel.setLanguage(info.code) },
                        modifier = Modifier.weight(1f).padding(bottom = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun PrivacyPage(lang: String, onBack: () -> Unit) {
    fun t(key: String) = Translations.get(lang, key)

    LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp), contentPadding = PaddingValues(top = 20.dp, bottom = 40.dp)) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 24.dp)) {
                IconButton(onClick = onBack, modifier = Modifier.glass(12).size(40.dp)) {
                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, null, tint = Color.White)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "🛡️ " + t("privacy.title"), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }
        }

        item {
            Card(modifier = Modifier.fillMaxWidth().background(Brush.linearGradient(listOf(Color(0xFF10B981).copy(alpha = 0.1f), Color(0xFF10B981).copy(alpha = 0.05f))), RoundedCornerShape(20.dp)).border(1.dp, Color(0xFF10B981).copy(alpha = 0.15f), RoundedCornerShape(20.dp)), colors = CardDefaults.cardColors(containerColor = Color.Transparent)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🔒", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(t("privacy.dataTitle"), fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    Text(t("privacy.dataDesc"), style = MaterialTheme.typography.labelSmall, color = Color(0xFF10B981))
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(t("privacy.p1"), style = MaterialTheme.typography.bodySmall, color = Color(0xFFCBD5E1), lineHeight = 18.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Text(t("privacy.p2"), style = MaterialTheme.typography.bodySmall, color = Color(0xFFCBD5E1), lineHeight = 18.sp)
            Spacer(modifier = Modifier.height(20.dp))
            
            val points = listOf("Nenhum dado pessoal é coletado", "Navegação não é rastreada", "DNS opera 100% localmente", "Sem servidores externos", "Sem venda ou compartilhamento de dados")
            points.forEach { pt ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).glass(12), colors = CardDefaults.cardColors(containerColor = Color.Transparent)) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("✅", fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(pt, style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.8f))
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            Box(modifier = Modifier.fillMaxWidth().glass(16).padding(16.dp), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Desenvolvido com ❤️ por Taylor Reis", style = MaterialTheme.typography.labelSmall, color = Color(0xFF94A3B8))
                    Text("© 2026 MindGuard — Direitos reservados", style = MaterialTheme.typography.labelSmall, color = Color(0xFF475569))
                }
            }
        }
    }
}

@Composable
fun AboutPage(viewModel: MindguardViewModel, onBack: () -> Unit) {
    val lang by viewModel.language.collectAsState()
    val context = LocalContext.current
    fun t(key: String) = Translations.get(lang, key)

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(top = 20.dp, bottom = 40.dp)
    ) {
        item {
            Row(modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack, modifier = Modifier.glass(12).size(40.dp)) {
                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, null, tint = Color.White)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = t("about.title"), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }
        }
        item {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .shadow(24.dp, RoundedCornerShape(28.dp), spotColor = Color(0xFF7C3AED))
                    .clip(RoundedCornerShape(28.dp))
                    .background(Brush.linearGradient(listOf(Color(0xFF7C3AED), Color(0xFF4F46E5)))),
                contentAlignment = Alignment.Center
            ) {
                Text("🧠", fontSize = 44.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row {
                Text("Mind", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold, color = Color.White)
                Text("Guard", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold, color = Color(0xFF7C3AED))
            }
            Text(t("about.version"), color = Color(0xFF64748B), style = MaterialTheme.typography.labelSmall)
            Text(t("app.slogan"), color = Color(0xFF475569), style = MaterialTheme.typography.labelSmall)
            
            Spacer(modifier = Modifier.height(40.dp))

            Text("Nossa História", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color(0xFF7C3AED), modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                "O Mindguard Block nasceu da necessidade de retomar o controle sobre a nossa própria atenção e saúde mental em um mundo hiperconectado. Taylor Reis idealizou este projeto para ser um guardião silencioso contra as distrações e vícios da era digital.",
                style = MaterialTheme.typography.bodyMedium, color = Color(0xFFCBD5E1), textAlign = TextAlign.Start, lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(40.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Text("✨ Funcionalidades", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                Spacer(modifier = Modifier.height(16.dp))
                
                val features = listOf(
                    "🛡️" to "Bloqueio DNS local inteligente",
                    "🔞" to "Filtragem de conteúdo adulto",
                    "🚫" to "Remoção de anúncios invasivos",
                    "👁" to "Bloqueio de rastreadores",
                    "🎯" to "Sistema de metas pessoais",
                    "💬" to "Motivação diária inteligente"
                )
                
                features.forEach { (icon, text) ->
                    Card(
                        modifier = Modifier.padding(vertical = 5.dp).fillMaxWidth().glass(16),
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text(icon, fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(text, style = MaterialTheme.typography.bodySmall, color = Color(0xFFCBD5E1))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Card(
                modifier = Modifier.fillMaxWidth().border(1.dp, Color(0xFF7C3AED).copy(alpha = 0.2f), RoundedCornerShape(24.dp)),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF7C3AED).copy(alpha = 0.08f)),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.Laptop, null, tint = Color.White, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(if (lang == "pt-BR") "Extensão para PC" else "PC Extension", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(if (lang == "pt-BR") "Sincronize sua proteção em todos os seus navegadores." else "Sync protection across all your browsers.", style = MaterialTheme.typography.labelSmall, color = Color(0xFF94A3B8))
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(onClick = { android.widget.Toast.makeText(context, "Chrome Store soon", android.widget.Toast.LENGTH_SHORT).show() }, modifier = Modifier.weight(1f).glass(12), colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)) { Text("Chrome", fontSize = 11.sp) }
                        Button(onClick = { android.widget.Toast.makeText(context, "Add-ons store soon", android.widget.Toast.LENGTH_SHORT).show() }, modifier = Modifier.weight(1f).glass(12), colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)) { Text("Firefox", fontSize = 11.sp) }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { android.widget.Toast.makeText(context, "Support soon", android.widget.Toast.LENGTH_SHORT).show() },
                modifier = Modifier.fillMaxWidth().height(60.dp).shadow(24.dp, RoundedCornerShape(20.dp), spotColor = Color(0xFFF59E0B)),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF59E0B)),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(if (lang == "pt-BR") "☕ Me pague um café" else "☕ Buy me a coffee", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(48.dp))
            
            Text(t("profile.developed"), style = MaterialTheme.typography.labelSmall, color = Color(0xFF64748B))
            Text("© 2026 MindGuard — Direitos reservados", style = MaterialTheme.typography.labelSmall, color = Color(0xFF475569))
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    iconBg: Color = Color(0xFF7C3AED).copy(alpha = 0.1f),
    label: String,
    value: String? = null,
    showArrow: Boolean = true,
    danger: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    val alpha = if (onClick != null) 1f else 0.6f
    
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .alpha(alpha)
            .clickable(enabled = onClick != null) { onClick?.invoke() },
        shape = RoundedCornerShape(16.dp),
        color = Color.White.copy(alpha = 0.03f),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.06f))
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = if(danger) Color(0xFFEF4444) else Color.White, modifier = Modifier.size(22.dp))
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(text = label, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = if (danger) Color(0xFFF87171) else Color.White)
                if (value != null) {
                    Text(text = value, style = MaterialTheme.typography.labelSmall, color = Color(0xFF94A3B8))
                }
            }
            
            if (showArrow && onClick != null) {
                Icon(Icons.Rounded.ChevronRight, null, tint = Color(0xFF334155), modifier = Modifier.size(20.dp))
            }
        }
    }
}

@Composable
fun LanguageButton(label: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier) {
    Surface(
        modifier = modifier.clickable { onClick() }.height(52.dp),
        shape = RoundedCornerShape(14.dp),
        color = if (selected) Color(0xFF7C3AED) else Color.White.copy(alpha = 0.05f),
        border = if (selected) null else androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(text = label, fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal, color = if (selected) Color.White else Color(0xFF94A3B8), style = MaterialTheme.typography.labelSmall)
        }
    }
}
