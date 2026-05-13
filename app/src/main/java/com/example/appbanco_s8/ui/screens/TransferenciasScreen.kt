package com.example.appbanco_s8.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
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
import com.example.appbanco_s8.data.model.TransferenciaInsert
import com.example.appbanco_s8.data.remote.RetrofitClient
import com.example.appbanco_s8.ui.theme.AzulPrincipal
import com.example.appbanco_s8.ui.theme.DoradoCP
import com.example.appbanco_s8.ui.theme.FondoGrisClaro
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransferenciasScreen(token: String, userId: String, navController: NavHostController) {
    val scope = rememberCoroutineScope()
    var cuentaDestino by remember { mutableStateOf("") }
    var monto by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Transferencias", color = Color.White) },
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
            Text("Transferir a otra cuenta", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = AzulPrincipal)

            OutlinedTextField(
                value = cuentaDestino,
                onValueChange = { cuentaDestino = it },
                label = { Text("Número de cuenta destino") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            OutlinedTextField(
                value = monto,
                onValueChange = { monto = it },
                label = { Text("Monto") },
                modifier = Modifier.fillMaxWidth(),
                prefix = { Text("S/ ") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción / Motivo") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    scope.launch {
                        loading = true
                        val res = RetrofitClient.api.registrarTransferencia(
                            "Bearer $token",
                            TransferenciaInsert(userId, cuentaDestino, monto.toDoubleOrNull() ?: 0.0, descripcion)
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
                    Icon(Icons.Default.Send, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Realizar Transferencia", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
