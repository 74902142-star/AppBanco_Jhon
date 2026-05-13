package com.example.appbanco_s8.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.appbanco_s8.ui.components.AppScaffold
import com.example.appbanco_s8.ui.screens.*

@Composable
fun AppNavGraph(navController: NavHostController) {

    var tokenGlobal by remember { mutableStateOf("") }
    var emailGlobal by remember { mutableStateOf("") }
    var userIdGlobal by remember { mutableStateOf("") }

    val doLogout: () -> Unit = {
        tokenGlobal = ""
        emailGlobal = ""
        userIdGlobal = ""
        navController.navigate(Screen.Login.route) {
            popUpTo(0) { inclusive = true }
        }
    }

    NavHost(
        navController    = navController,
        startDestination = Screen.Login.route
    ) {

        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = { token, email, userId ->
                    tokenGlobal = token
                    emailGlobal = email
                    userIdGlobal = userId
                    navController.navigate(Screen.Home.createRoute(token, email)) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route     = Screen.Home.route,
            arguments = listOf(
                navArgument("token") { type = NavType.StringType },
                navArgument("email") { type = NavType.StringType }
            )
        ) { back ->
            val token = back.arguments?.getString("token") ?: tokenGlobal
            val email = back.arguments?.getString("email") ?: emailGlobal
            
            tokenGlobal = token
            emailGlobal = email

            var openDrawer by remember { mutableStateOf<(() -> Unit)?>(null) }

            AppScaffold(
                token         = token,
                email         = email,
                navController = navController,
                onLogout      = doLogout,
                onOpenDrawer  = { fn -> openDrawer = fn }
            ) { padding ->
                Box(Modifier.fillMaxSize().padding(padding)) {
                    HomeScreen(
                        token         = token,
                        email         = email,
                        navController = navController,
                        onLogout      = doLogout,
                        onMenuClick   = { openDrawer?.invoke() }
                    )
                }
            }
        }

        composable(
            route = Screen.Ahorros.route,
            arguments = listOf(navArgument("token") { type = NavType.StringType })
        ) { back ->
            val token = back.arguments?.getString("token") ?: tokenGlobal
            AhorroScreen(token = token, userId = userIdGlobal, navController = navController)
        }

        composable(
            route = Screen.Prestamo.route,
            arguments = listOf(navArgument("token") { type = NavType.StringType })
        ) { back ->
            val token = back.arguments?.getString("token") ?: tokenGlobal
            CreditosScreen(token = token, userId = userIdGlobal, navController = navController)
        }

        composable(
            route = Screen.Transferencias.route,
            arguments = listOf(navArgument("token") { type = NavType.StringType })
        ) { back ->
            val token = back.arguments?.getString("token") ?: tokenGlobal
            TransferenciasScreen(token = token, userId = userIdGlobal, navController = navController)
        }

        composable(
            route = Screen.Pagos.route,
            arguments = listOf(navArgument("token") { type = NavType.StringType })
        ) { back ->
            val token = back.arguments?.getString("token") ?: tokenGlobal
            PagosScreen(token = token, userId = userIdGlobal, navController = navController)
        }

        composable(
            route = Screen.Perfil.route,
            arguments = listOf(navArgument("token") { type = NavType.StringType })
        ) { back ->
            val token = back.arguments?.getString("token") ?: tokenGlobal
            PerfilScreen(token = token, userId = userIdGlobal, navController = navController)
        }

        composable(
            route = Screen.Opera.route,
            arguments = listOf(navArgument("token") { type = NavType.StringType })
        ) { back ->
            val token = back.arguments?.getString("token") ?: tokenGlobal
            AppScaffold(token = token, email = emailGlobal, navController = navController, onLogout = doLogout) { padding ->
                Box(Modifier.padding(padding)) { OperaScreen(token = token, navController = navController) }
            }
        }

        composable(
            route = Screen.Notifica.route,
            arguments = listOf(navArgument("token") { type = NavType.StringType })
        ) { back ->
            val token = back.arguments?.getString("token") ?: tokenGlobal
            AppScaffold(token = token, email = emailGlobal, navController = navController, onLogout = doLogout) { padding ->
                Box(Modifier.padding(padding)) { NotificaScreen(token = token, navController = navController) }
            }
        }

        composable(Screen.Contacto.route) {
            AppScaffold(token = tokenGlobal, email = emailGlobal, navController = navController, onLogout = doLogout) { padding ->
                Box(Modifier.padding(padding)) { ContactoScreen(navController = navController) }
            }
        }

        composable(
            route = Screen.Cuenta.route,
            arguments = listOf(navArgument("token") { type = NavType.StringType })
        ) { back ->
            val token = back.arguments?.getString("token") ?: tokenGlobal
            AppScaffold(token = token, email = emailGlobal, navController = navController, onLogout = doLogout) { padding ->
                Box(Modifier.padding(padding)) { CuentaScreen(token = token, navController = navController) }
            }
        }
    }
}
