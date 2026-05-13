package com.example.appbanco_s8.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appbanco_s8.R
import com.example.appbanco_s8.ui.theme.AzulPrincipal
import com.example.appbanco_s8.ui.theme.AzulSecundario
import com.example.appbanco_s8.ui.theme.DoradoCP
import com.example.appbanco_s8.ui.theme.FondoGrisClaro
import com.example.appbanco_s8.ui.theme.GrisTexto
import com.example.appbanco_s8.ui.viewmodel.AuthUiState
import com.example.appbanco_s8.ui.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    onLoginSuccess: (token: String, email: String, userId: String) -> Unit,
    viewModel: AuthViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    var email           by remember { mutableStateOf("user1@conti.com") }
    var password        by remember { mutableStateOf("1234") }
    var passwordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.Success) {
            val s = uiState as AuthUiState.Success
            onLoginSuccess(s.token, s.email, s.userId)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoGrisClaro)
    ) {
        // Fondo azul superior más grande
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.45f)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(AzulPrincipal, AzulSecundario)
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(60.dp))

            // Logo y Título centrados en la parte superior azul
            AnimatedVisibility(
                visible = true,
                enter   = fadeIn() + slideInVertically(initialOffsetY = { -40 })
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Surface(
                        shape           = RoundedCornerShape(24.dp),
                        color           = Color.White,
                        modifier        = Modifier.size(100.dp),
                        shadowElevation = 12.dp
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(12.dp)) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_launcher_cp),
                                contentDescription = "Logo Caja Piura",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text          = "Caja Piura",
                        fontSize      = 32.sp,
                        fontWeight    = FontWeight.ExtraBold,
                        color         = Color.White,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text          = "Impulsando oportunidades",
                        fontSize      = 14.sp,
                        color         = Color.White.copy(alpha = 0.9f),
                        fontWeight    = FontWeight.Medium
                    )
                }
            }

            Spacer(Modifier.height(40.dp))

            // Card de Login
            Surface(
                shape          = RoundedCornerShape(28.dp),
                color          = Color.White,
                modifier       = Modifier.fillMaxWidth(),
                shadowElevation = 6.dp
            ) {
                Column(
                    modifier            = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Text(
                        text       = "Iniciar sesión",
                        fontSize   = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color      = AzulPrincipal
                    )

                    OutlinedTextField(
                        value         = email,
                        onValueChange = { email = it },
                        label         = { Text("Correo electrónico") },
                        leadingIcon   = {
                            Icon(
                                imageVector        = Icons.Default.Email,
                                contentDescription = null,
                                tint               = AzulPrincipal
                            )
                        },
                        singleLine      = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction    = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        shape    = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value         = password,
                        onValueChange = { password = it },
                        label         = { Text("Contraseña") },
                        leadingIcon   = {
                            Icon(
                                imageVector        = Icons.Default.Lock,
                                contentDescription = null,
                                tint               = AzulPrincipal
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible)
                                        Icons.Default.VisibilityOff
                                    else
                                        Icons.Default.Visibility,
                                    contentDescription = null,
                                    tint = GrisTexto
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible)
                            VisualTransformation.None
                        else
                            PasswordVisualTransformation(),
                        singleLine      = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction    = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                                viewModel.login(email.trim(), password)
                            }
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        shape    = RoundedCornerShape(12.dp)
                    )

                    AnimatedVisibility(visible = uiState is AuthUiState.Error) {
                        Surface(
                            shape    = RoundedCornerShape(8.dp),
                            color    = Color(0xFFE74C3C).copy(alpha = 0.1f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text     = (uiState as? AuthUiState.Error)?.mensaje ?: "",
                                color    = Color(0xFFE74C3C),
                                fontSize = 13.sp,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }

                    Button(
                        onClick  = {
                            focusManager.clearFocus()
                            viewModel.login(email.trim(), password)
                        },
                        enabled  = uiState !is AuthUiState.Loading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape  = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor         = AzulPrincipal,
                            contentColor           = Color.White
                        )
                    ) {
                        if (uiState is AuthUiState.Loading) {
                            CircularProgressIndicator(
                                color       = Color.White,
                                modifier    = Modifier.size(24.dp),
                                strokeWidth = 3.dp
                            )
                        } else {
                            Text(
                                text       = "Ingresar",
                                fontSize   = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(40.dp))

            Text(
                text          = "CAJA PIURA · SOLIDEZ Y CONFIANZA",
                fontSize      = 11.sp,
                color         = GrisTexto.copy(alpha = 0.7f),
                letterSpacing = 1.5.sp,
                textAlign     = TextAlign.Center
            )

            Spacer(Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(DoradoCP, shape = RoundedCornerShape(50))
            )
            
            Spacer(Modifier.height(40.dp))
        }
    }
}
