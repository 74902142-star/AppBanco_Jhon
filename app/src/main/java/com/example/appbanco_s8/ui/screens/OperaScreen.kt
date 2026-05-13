package com.example.appbanco_s8.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.appbanco_s8.ui.theme.AzulPrincipal
import com.example.appbanco_s8.ui.theme.AzulSecundario
import com.example.appbanco_s8.ui.theme.FondoGrisClaro
import com.example.appbanco_s8.ui.theme.GrisTexto

@Composable
fun OperaScreen(token: String, navController: NavHostController) {

    data class OperaItem(val icon: ImageVector, val label: String)

    val operacionesCuenta = listOf(
        OperaItem(Icons.Default.SwapHoriz,        "Transferir"),
        OperaItem(Icons.Default.MoneyOff,          "Retiro sin\ntarjeta"),
        OperaItem(Icons.Default.Description,       "Ver estado\nde cuenta"),
        OperaItem(Icons.Default.PhoneAndroid,      "Recargar\ncelular"),
        OperaItem(Icons.Default.Receipt,           "Pagar\nservicio"),
        OperaItem(Icons.Default.CreditCard,        "Pagar\ntarjeta"),
        OperaItem(Icons.Default.CardGiftcard,      "Tarjeta\nregalo"),
        OperaItem(Icons.Default.PhoneIphone,       "PLIN"),
        OperaItem(Icons.Default.CurrencyExchange,  "T-Cambio"),
        OperaItem(Icons.Default.Link,              "Vincular\ntarjeta"),
        OperaItem(Icons.Default.FlightTakeoff,     "Giro\nexterior")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoGrisClaro)
    ) {
        // Header con gradiente similar a Home
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
            Text(
                text       = "Operaciones",
                color      = Color.White,
                fontSize   = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Spacer(Modifier.height(16.dp))
            
            Text(
                text     = "Operaciones con cuentas",
                color    = GrisTexto,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                items(operacionesCuenta) { item ->
                    Column(
                        modifier = Modifier
                            .clickable { }
                            .padding(vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color.White,
                            shadowElevation = 2.dp,
                            modifier = Modifier.size(56.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector        = item.icon,
                                    contentDescription = item.label,
                                    tint               = AzulPrincipal,
                                    modifier           = Modifier.size(26.dp)
                                )
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text      = item.label,
                            color     = AzulPrincipal,
                            fontSize  = 11.sp,
                            maxLines  = 2,
                            lineHeight = 14.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}
