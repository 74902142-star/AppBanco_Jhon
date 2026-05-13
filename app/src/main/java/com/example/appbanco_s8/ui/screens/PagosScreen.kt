package com.example.appbanco_s8.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.appbanco_s8.data.model.PagoServicioInsert
import com.example.appbanco_s8.data.remote.RetrofitClient
import com.example.appbanco_s8.ui.theme.AzulPrincipal
import com.example.appbanco_s8.ui.theme.FondoGrisClaro
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PagosScreen(token: String, userId: String, navController: NavHostController) {
    val scope = rememberCoroutineScope()
    var servicio by remember { mutableStateOf("Luz") }
    var contrato by remember { mutableStateOf("") }
    var monto by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }

    val servicios = listOf("Luz", "Agua", "Internet", "Telefonía")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pago de Servicios", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AzulPrincipal)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(FondoGrisClaro)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Detalles del Pago", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = AzulPrincipal)

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = servicio,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Seleccionar Servicio") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    servicios.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item) },
                            onClick = {
                                servicio = item
                                expanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = contrato,
                onValueChange = { contrato = it },
                label = { Text("Número de Contrato / Suministro") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            OutlinedTextField(
                value = monto,
                onValueChange = { monto = it },
                label = { Text("Monto a pagar") },
                modifier = Modifier.fillMaxWidth(),
                prefix = { Text("S/ ") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    scope.launch {
                        loading = true
                        val res = RetrofitClient.api.registrarPagoServicio(
                            "Bearer $token",
                            PagoServicioInsert(userId, servicio, monto.toDoubleOrNull() ?: 0.0, contrato)
                        )
                        loading = false
                        if (res.isSuccessful) navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AzulPrincipal),
                shape = RoundedCornerShape(12.dp),
                enabled = !loading
            ) {
                if (loading) CircularProgressIndicator(color = Color.White)
                else {
                    Icon(Icons.Default.Payment, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Pagar Servicio", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
