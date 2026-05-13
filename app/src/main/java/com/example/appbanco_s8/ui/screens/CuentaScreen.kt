package com.example.appbanco_s8.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.appbanco_s8.ui.theme.AzulPrincipal
import com.example.appbanco_s8.ui.theme.FondoGrisClaro

@Composable
fun CuentaScreen(token: String, navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoGrisClaro),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Cuenta Screen — en construcción", 
            color = AzulPrincipal,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
