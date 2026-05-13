package com.example.appbanco_s8.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.appbanco_s8.data.model.MovimientoAhorro
import com.example.appbanco_s8.ui.theme.*
import com.example.appbanco_s8.ui.viewmodel.AhorroUiState
import com.example.appbanco_s8.ui.viewmodel.AhorroViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AhorroScreen(
    token: String,
    userId: String,
    navController: NavHostController,
    viewModel: AhorroViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showDepositDialog by remember { mutableStateOf(false) }

    LaunchedEffect(token, userId) {
        viewModel.cargarAhorros(token, userId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Ahorros", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AzulPrincipal)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDepositDialog = true },
                containerColor = DoradoCP,
                contentColor = AzulPrincipal
            ) {
                Icon(Icons.Default.Add, contentDescription = "Depositar")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize().background(FondoGrisClaro)) {
            when (val state = uiState) {
                is AhorroUiState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                is AhorroUiState.Error -> Text(state.message, modifier = Modifier.align(Alignment.Center))
                is AhorroUiState.Success -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth().padding(16.dp),
                                colors = CardDefaults.cardColors(containerColor = AzulSecundario),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("Saldo Total", color = Color.White.copy(alpha = 0.8f), fontSize = 16.sp)
                                    Text(
                                        text = "S/ %,.2f".format(state.cuenta.saldo),
                                        color = Color.White,
                                        fontSize = 36.sp,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                    Spacer(Modifier.height(8.dp))
                                    Text("Cuenta: ${state.cuenta.numeroCuenta}", color = Color.White, fontSize = 14.sp)
                                }
                            }
                        }
                        item {
                            Text(
                                "Movimientos Recientes",
                                modifier = Modifier.padding(16.dp),
                                color = AzulPrincipal,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        }
                        items(state.movimientos) { mov ->
                            MovimientoItem(mov)
                        }
                    }
                }
            }
        }
    }

    if (showDepositDialog) {
        DepositDialog(
            onDismiss = { showDepositDialog = false },
            onConfirm = { monto ->
                viewModel.depositar(token, userId, monto)
                showDepositDialog = false
            }
        )
    }
}

@Composable
fun MovimientoItem(mov: MovimientoAhorro) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val isDeposito = mov.tipo == "deposito"
            Box(
                modifier = Modifier.size(40.dp).background(if (isDeposito) VerdeExito.copy(alpha = 0.1f) else RojoError.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isDeposito) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
                    contentDescription = null,
                    tint = if (isDeposito) VerdeExito else RojoError
                )
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(mov.descripcion, fontWeight = FontWeight.Medium, color = AzulPrincipal)
                Text(mov.fecha, fontSize = 12.sp, color = GrisTexto)
            }
            Text(
                "${if (isDeposito) "+" else "-"} S/ %,.2f".format(mov.monto),
                fontWeight = FontWeight.Bold,
                color = if (isDeposito) VerdeExito else RojoError
            )
        }
    }
}

@Composable
fun DepositDialog(onDismiss: () -> Unit, onConfirm: (Double) -> Unit) {
    var monto by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Realizar Depósito") },
        text = {
            OutlinedTextField(
                value = monto,
                onValueChange = { if (it.isEmpty() || it.toDoubleOrNull() != null) monto = it },
                label = { Text("Monto a depositar") },
                modifier = Modifier.fillMaxWidth(),
                prefix = { Text("S/ ") }
            )
        },
        confirmButton = {
            Button(onClick = { monto.toDoubleOrNull()?.let { onConfirm(it) } }) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}
