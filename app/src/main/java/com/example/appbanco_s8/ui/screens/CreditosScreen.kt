package com.example.appbanco_s8.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.appbanco_s8.data.model.Credito
import com.example.appbanco_s8.data.model.CronogramaPago
import com.example.appbanco_s8.ui.theme.*
import com.example.appbanco_s8.ui.viewmodel.CreditoUiState
import com.example.appbanco_s8.ui.viewmodel.CreditoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreditosScreen(
    token: String,
    userId: String,
    navController: NavHostController,
    viewModel: CreditoViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val cronograma by viewModel.cronograma.collectAsStateWithLifecycle()
    var selectedCredito by remember { mutableStateOf<Credito?>(null) }

    LaunchedEffect(token, userId) {
        viewModel.cargarCreditos(token, userId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Créditos", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { 
                        if (selectedCredito != null) selectedCredito = null 
                        else navController.popBackStack() 
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AzulPrincipal)
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize().background(FondoGrisClaro)) {
            if (selectedCredito == null) {
                // Lista de Créditos
                when (val state = uiState) {
                    is CreditoUiState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    is CreditoUiState.Error -> Text(state.message, modifier = Modifier.align(Alignment.Center))
                    is CreditoUiState.Success -> {
                        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                            items(state.creditos) { credito ->
                                CreditoCard(credito) {
                                    selectedCredito = credito
                                    viewModel.cargarCronograma(token, credito.id)
                                }
                            }
                        }
                    }
                }
            } else {
                // Detalle y Cronograma
                CronogramaView(selectedCredito!!, cronograma)
            }
        }
    }
}

@Composable
fun CreditoCard(credito: Credito, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text("Crédito Personal", fontWeight = FontWeight.Bold, color = AzulPrincipal)
                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = GrisTexto)
            }
            Spacer(Modifier.height(12.dp))
            val progreso = (credito.cuotasPagadas.toFloat() / credito.cuotas.toFloat())
            LinearProgressIndicator(
                progress = { progreso },
                modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                color = DoradoCP,
                trackColor = FondoGrisClaro
            )
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Column {
                    Text("Saldo restante", fontSize = 12.sp, color = GrisTexto)
                    Text("S/ %,.2f".format(credito.saldoRestante), fontWeight = FontWeight.Bold, color = AzulPrincipal)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Progreso", fontSize = 12.sp, color = GrisTexto)
                    Text("${credito.cuotasPagadas}/${credito.cuotas} cuotas", fontWeight = FontWeight.Bold, color = AzulPrincipal)
                }
            }
        }
    }
}

@Composable
fun CronogramaView(credito: Credito, cronograma: List<CronogramaPago>) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(containerColor = AzulPrincipal)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Resumen del Crédito", color = Color.White, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    Text("Monto Total: S/ %,.2f".format(credito.monto), color = Color.White)
                    Text("Tasa: ${credito.tasaInteres}% TEA", color = Color.White)
                }
            }
            Text("Próximos Pagos", fontWeight = FontWeight.Bold, color = AzulPrincipal, modifier = Modifier.padding(bottom = 8.dp))
        }
        items(cronograma) { pago ->
            Row(
                modifier = Modifier.fillMaxWidth().background(Color.White).padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Cuota - ${pago.fechaVencimiento}", fontWeight = FontWeight.Medium)
                    Text(pago.estado.uppercase(), fontSize = 12.sp, color = if (pago.estado == "pagado") VerdeExito else DoradoCP)
                }
                Text("S/ %,.2f".format(pago.montoCuota), fontWeight = FontWeight.Bold, color = AzulPrincipal)
            }
            HorizontalDivider(color = FondoGrisClaro)
        }
    }
}
