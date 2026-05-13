package com.example.appbanco_s8.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.appbanco_s8.ui.theme.AzulPrincipal
import com.example.appbanco_s8.ui.theme.FondoGrisClaro
import com.example.appbanco_s8.ui.theme.GrisTexto

@Composable
fun ContactoScreen(navController: NavHostController) {
    LazyColumn(
        modifier            = Modifier
            .fillMaxSize()
            .background(FondoGrisClaro),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding      = PaddingValues(16.dp)
    ) {
        item {
            Row(
                modifier              = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text("Contacto", color = AzulPrincipal, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Icon(Icons.Default.Menu, contentDescription = null, tint = AzulPrincipal)
            }
        }

        // Card Asistente Virtual
        item {
            ContactoCard(
                icon       = Icons.Default.SmartToy,
                titulo     = "Asistente Virtual",
                descripcion = "Nuestro asistente está disponible las 24 horas del día para ayudarte.",
                botonLabel = "Comenzar",
                onClick    = {}
            )
        }

        // Card Puntos de atención
        item {
            ContactoCard(
                icon        = Icons.Default.LocationOn,
                titulo      = "Puntos de atención",
                descripcion = "Consulta las oficinas, cajeros y agentes que tienes a tu disposición.",
                botonLabel  = "Consultar",
                onClick     = {}
            )
        }

        // Card Banca por Teléfono
        item {
            ContactoCard(
                icon        = Icons.Default.Phone,
                titulo      = "Banca por Teléfono",
                descripcion = "Horario de atención de lunes a sábado de 8:00 a 20:00.",
                botonLabel  = "Llamar",
                onClick     = {}
            )
        }

        // Card Zona de cobro
        item {
            ContactoCard(
                icon        = Icons.Default.Calculate,
                titulo      = "Zona de cobro",
                descripcion = "Consulta y gestiona tus cobros pendientes.",
                botonLabel  = "Ver cobros",
                onClick     = {}
            )
        }

        item { Spacer(Modifier.height(8.dp)) }
    }
}

@Composable
private fun ContactoCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    titulo: String,
    descripcion: String,
    botonLabel: String,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 2.dp
    ) {
        Column(
            modifier            = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector        = icon,
                contentDescription = titulo,
                tint               = AzulPrincipal,
                modifier           = Modifier.size(48.dp)
            )
            Text(titulo,      color = AzulPrincipal, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(descripcion, color = GrisTexto,   fontSize = 13.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center)
            Button(
                onClick  = onClick,
                modifier = Modifier.fillMaxWidth().height(46.dp),
                shape    = RoundedCornerShape(12.dp),
                colors   = ButtonDefaults.buttonColors(containerColor = AzulPrincipal)
            ) {
                Text(botonLabel, color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}
