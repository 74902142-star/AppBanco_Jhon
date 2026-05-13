package com.example.appbanco_s8.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.appbanco_s8.R
import com.example.appbanco_s8.data.model.Cuenta
import com.example.appbanco_s8.data.model.Transaccion
import com.example.appbanco_s8.navigation.Screen
import com.example.appbanco_s8.ui.theme.*
import com.example.appbanco_s8.ui.viewmodel.DataUiState
import com.example.appbanco_s8.ui.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    token: String,
    email: String,
    navController: NavHostController,
    onLogout: () -> Unit,
    onMenuClick: () -> Unit = {},
    viewModel: HomeViewModel = viewModel()
) {
    val cuentasState       by viewModel.cuentas.collectAsStateWithLifecycle()
    val transaccionesState by viewModel.transacciones.collectAsStateWithLifecycle()

    LaunchedEffect(token) {
        viewModel.cargarDatos(token)
    }

    val nombreCorto = email.substringBefore("@")
        .replaceFirstChar { it.uppercase() }

    LazyColumn(
        modifier            = Modifier
            .fillMaxSize()
            .background(FondoGrisClaro),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {

        item {
            HeaderSection(
                nombreCorto = nombreCorto,
                onMenuClick = onMenuClick
            )
        }

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = AzulSecundario),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "¿Necesitas opciones para tu negocio?",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Conoce nuestros créditos empresariales con las mejores tasas.",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 14.sp
                    )
                    Button(
                        onClick = { navController.navigate(Screen.Prestamo.createRoute(token)) },
                        colors = ButtonDefaults.buttonColors(containerColor = DoradoCP),
                        modifier = Modifier.padding(top = 12.dp)
                    ) {
                        Text("Ver créditos", color = AzulPrincipal)
                    }
                }
            }
        }

        item {
            AccesosRapidosSection(navController, token)
        }

        item {
            SectionTitle(title = "Tus Cuentas")
        }

        when (val state = cuentasState) {
            is DataUiState.Loading -> item { LoadingCard() }
            is DataUiState.Error   -> item { ErrorCard(mensaje = state.mensaje) }
            is DataUiState.Success -> {
                items(state.data) { cuenta ->
                    CuentaCard(
                        cuenta  = cuenta,
                        onClick = {
                            navController.navigate(Screen.Cuenta.createRoute(token))
                        }
                    )
                }
            }
        }

        item {
            SectionTitle(title = "Últimos movimientos")
        }

        when (val state = transaccionesState) {
            is DataUiState.Loading -> item { LoadingCard() }
            is DataUiState.Error   -> item { ErrorCard(mensaje = state.mensaje) }
            is DataUiState.Success -> {
                val ultimos = state.data.take(5)
                if (ultimos.isEmpty()) {
                    item {
                        Text(
                            text     = "Sin movimientos recientes",
                            color    = GrisTexto,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                } else {
                    items(ultimos) { tx ->
                        MovimientoRow(transaccion = tx)
                    }
                }
            }
        }

        item {
            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
private fun HeaderSection(
    nombreCorto: String,
    onMenuClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(AzulPrincipal, AzulSecundario)
                )
            )
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White,
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(4.dp)) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_launcher_cp),
                            contentDescription = null,
                            contentScale = ContentScale.Fit
                        )
                    }
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        text       = "Hola, $nombreCorto",
                        color      = Color.White,
                        fontSize   = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text     = "Impulsando oportunidades",
                        color    = Color.White.copy(alpha = 0.8f),
                        fontSize = 11.sp
                    )
                }
            }

            IconButton(
                onClick = onMenuClick,
                modifier = Modifier.background(Color.White.copy(alpha = 0.2f), CircleShape)
            ) {
                Icon(
                    imageVector        = Icons.Default.Menu,
                    contentDescription = "Menú",
                    tint               = Color.White
                )
            }
        }
    }
}

@Composable
private fun AccesosRapidosSection(navController: NavHostController, token: String) {
    Row(
        modifier              = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        AccesoItem(icon = Icons.Default.SwapHoriz, label = "Transferir") {
            navController.navigate("transferencias/$token")
        }
        AccesoItem(icon = Icons.Default.Payments, label = "Pagos") {
            navController.navigate("pagos/$token")
        }
        AccesoItem(icon = Icons.Default.Savings, label = "Ahorros") {
            navController.navigate("ahorros/$token")
        }
        AccesoItem(icon = Icons.Default.AccountBalance, label = "Créditos") {
            navController.navigate(Screen.Prestamo.createRoute(token))
        }
    }
}

@Composable
private fun AccesoItem(icon: ImageVector, label: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier            = Modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier        = Modifier
                .size(56.dp)
                .background(Color.White, CircleShape)
                .clip(CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector        = icon,
                contentDescription = label,
                tint               = AzulPrincipal,
                modifier           = Modifier.size(28.dp)
            )
        }
        Spacer(Modifier.height(8.dp))
        Text(label, color = AzulPrincipal, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text       = title,
        color      = AzulPrincipal,
        fontSize   = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier   = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
    )
}

@Composable
private fun CuentaCard(cuenta: Cuenta, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier              = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text       = cuenta.tipo.uppercase(),
                    color      = AzulPrincipal,
                    fontSize   = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text     = cuenta.numeroCuenta,
                    color    = GrisTexto,
                    fontSize = 12.sp
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text       = "S/ %,.2f".format(cuenta.saldo),
                    color      = AzulPrincipal,
                    fontSize   = 18.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text     = "Disponible",
                    color    = VerdeExito,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun MovimientoRow(transaccion: Transaccion) {
    Row(
        modifier              = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment     = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    if (transaccion.esDebito()) RojoError.copy(alpha = 0.1f)
                    else VerdeExito.copy(alpha = 0.1f),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (transaccion.esDebito()) Icons.Default.Remove else Icons.Default.Add,
                contentDescription = null,
                tint               = if (transaccion.esDebito()) RojoError else VerdeExito
            )
        }

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text     = transaccion.descripcion,
                color    = AzulPrincipal,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text     = transaccion.fecha,
                color    = GrisTexto,
                fontSize = 11.sp
            )
        }

        Text(
            text       = "${if (transaccion.esDebito()) "-" else "+"}${transaccion.montoFormateado()}",
            color      = if (transaccion.esDebito()) RojoError else VerdeExito,
            fontSize   = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
    HorizontalDivider(color = FondoGrisClaro, thickness = 1.dp)
}

@Composable
private fun LoadingCard() {
    Box(modifier = Modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = AzulPrincipal)
    }
}

@Composable
private fun ErrorCard(mensaje: String) {
    Text(mensaje, color = RojoError, modifier = Modifier.padding(16.dp))
}
