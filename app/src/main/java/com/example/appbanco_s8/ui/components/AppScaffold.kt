package com.example.appbanco_s8.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.appbanco_s8.R
import com.example.appbanco_s8.navigation.Screen
import com.example.appbanco_s8.ui.theme.*
import kotlinx.coroutines.launch

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String,
    val isCentral: Boolean = false
)

data class DrawerItem(
    val label: String,
    val icon: ImageVector,
    val onClick: () -> Unit,
    val isDestructive: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(
    token: String,
    email: String,
    navController: NavHostController,
    onLogout: () -> Unit,
    onOpenDrawer: ((openFn: () -> Unit) -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope       = rememberCoroutineScope()

    SideEffect {
        onOpenDrawer?.invoke {
            scope.launch { drawerState.open() }
        }
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route?.substringBefore("/") ?: ""

    val nombreCorto = email.substringBefore("@").replaceFirstChar { it.uppercase() }

    val bottomItems = listOf(
        BottomNavItem("Inicio",   Icons.Filled.Home,          Screen.Home.route.substringBefore("/")),
        BottomNavItem("Opera",    Icons.Filled.SwapHoriz,     Screen.Opera.route.substringBefore("/")),
        BottomNavItem("Para mí",  Icons.Filled.Add,           Screen.Cuenta.route.substringBefore("/"), isCentral = true),
        BottomNavItem("Notifica", Icons.Filled.Notifications, Screen.Notifica.route.substringBefore("/")),
        BottomNavItem("Contacto", Icons.Filled.HeadsetMic,    Screen.Contacto.route)
    )

    val drawerItems = listOf(
        DrawerItem("Configuración",          Icons.Outlined.Settings,       {}),
        DrawerItem("Token Digital",          Icons.Outlined.Shield,         {}),
        DrawerItem("Seguridad y privacidad", Icons.Outlined.Lock,           {}),
        DrawerItem("Operativas",             Icons.Outlined.AccountBalance, {}),
        DrawerItem("Operar con QR / Plin",   Icons.Outlined.QrCode,         {}),
        DrawerItem("Puntos y promociones",   Icons.Outlined.Stars,          {}),
        DrawerItem("Experiencias",           Icons.Outlined.Explore,        {}),
        DrawerItem("Historial retiro",       Icons.Outlined.History,        {}),
        DrawerItem("Puntos de atención",     Icons.Outlined.LocationOn,     {}),
        DrawerItem("Zona de cobro",          Icons.Outlined.Calculate,      {}),
        DrawerItem("Aplicaciones",           Icons.Outlined.Apps,           {}),
        DrawerItem("Ayúdanos a mejorar",     Icons.Outlined.Favorite,       {}),
        DrawerItem("Acerca de Caja Piura",     Icons.Outlined.Info,           {}),
        DrawerItem("Salir",                  Icons.Outlined.Logout,         onLogout, isDestructive = true)
    )

    ModalNavigationDrawer(
        drawerState   = drawerState,
        drawerContent = {
            DrawerContent(
                nombreCorto = nombreCorto,
                items       = drawerItems,
                onClose     = { scope.launch { drawerState.close() } }
            )
        },
        scrimColor = Color.Black.copy(alpha = 0.6f)
    ) {
        Scaffold(
            containerColor = FondoGrisClaro,
            bottomBar = {
                BottomNavBar(
                    items        = bottomItems,
                    currentRoute = currentRoute,
                    onItemClick  = { item ->
                        if (!item.isCentral) {
                            val route = when (item.label) {
                                "Inicio"   -> Screen.Home.createRoute(token, email)
                                "Opera"    -> Screen.Opera.createRoute(token)
                                "Notifica" -> Screen.Notifica.createRoute(token)
                                "Contacto" -> Screen.Contacto.route
                                else       -> null
                            }
                            route?.let {
                                navController.navigate(it) {
                                    popUpTo(Screen.Home.route.substringBefore("/")) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState    = true
                                }
                            }
                        }
                    }
                )
            }
        ) { paddingValues ->
            content(paddingValues)
        }
    }
}

@Composable
private fun BottomNavBar(
    items: List<BottomNavItem>,
    currentRoute: String,
    onItemClick: (BottomNavItem) -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        items.forEach { item ->
            val selected = currentRoute.startsWith(item.route.substringBefore("/"))
            if (item.isCentral) {
                NavigationBarItem(
                    selected = false,
                    onClick  = { onItemClick(item) },
                    icon = {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .background(AzulPrincipal, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector        = item.icon,
                                contentDescription = item.label,
                                tint               = Color.White,
                                modifier           = Modifier.size(24.dp)
                            )
                        }
                    },
                    label  = { 
                        Text(
                            text = item.label, 
                            fontSize = 10.sp, 
                            color = AzulPrincipal,
                            fontWeight = FontWeight.Bold
                        ) 
                    },
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = GrisTexto,
                        unselectedTextColor = GrisTexto,
                        indicatorColor      = Color.Transparent
                    )
                )
            } else {
                NavigationBarItem(
                    selected = selected,
                    onClick  = { onItemClick(item) },
                    icon = {
                        Icon(
                            imageVector        = item.icon,
                            contentDescription = item.label,
                            modifier           = Modifier.size(24.dp)
                        )
                    },
                    label  = { Text(item.label, fontSize = 10.sp, fontWeight = FontWeight.Medium) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor   = AzulPrincipal,
                        selectedTextColor   = AzulPrincipal,
                        unselectedIconColor = GrisTexto,
                        unselectedTextColor = GrisTexto,
                        indicatorColor      = AzulPrincipal.copy(alpha = 0.1f)
                    )
                )
            }
        }
    }
}

@Composable
private fun DrawerContent(
    nombreCorto: String,
    items: List<DrawerItem>,
    onClose: () -> Unit
) {
    ModalDrawerSheet(
        drawerContainerColor = Color.White,
        modifier             = Modifier.width(300.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(AzulPrincipal)
                .padding(24.dp)
        ) {
            Column {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White,
                    modifier = Modifier.size(60.dp)
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(8.dp)) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_launcher_cp),
                            contentDescription = "Logo Caja Piura",
                            contentScale = ContentScale.Fit
                        )
                    }
                }
                Spacer(Modifier.height(12.dp))
                Text(
                    text       = nombreCorto,
                    color      = Color.White,
                    fontSize   = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text     = "Caja Piura - Solidez y Confianza",
                    color    = Color.White.copy(alpha = 0.8f),
                    fontSize = 12.sp
                )
                Spacer(Modifier.height(8.dp))
                TextButton(
                    onClick  = { onClose() },
                    colors = ButtonDefaults.textButtonColors(contentColor = DoradoCP),
                    modifier = Modifier.padding(0.dp)
                ) {
                    Text(
                        text     = "Ver mi perfil",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        items.forEach { item ->
            NavigationDrawerItem(
                label = { Text(item.label, fontWeight = if (item.isDestructive) FontWeight.Bold else FontWeight.Normal) },
                selected = false,
                onClick = {
                    item.onClick()
                    if (!item.isDestructive) onClose()
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = null,
                        tint = if (item.isDestructive) RojoError else AzulPrincipal
                    )
                },
                colors = NavigationDrawerItemDefaults.colors(
                    unselectedContainerColor = Color.Transparent,
                    unselectedTextColor = if (item.isDestructive) RojoError else AzulPrincipal
                ),
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
        }

        Spacer(Modifier.weight(1f))
        Text(
            "Versión 2.0.0",
            modifier = Modifier.padding(16.dp),
            fontSize = 10.sp,
            color = GrisTexto
        )
    }
}
