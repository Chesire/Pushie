package com.chesire.pushie.pusher

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chesire.pushie.compose.components.PushieNumberPicker
import com.chesire.pushie.compose.components.PushieText

@Composable
fun PusherScreen(
    onSendClicked: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        PasswordInput()
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            DaysInput()
            Spacer(modifier = Modifier.weight(1f))
            ViewsInput()
        }
        SendButton(onSendClicked)
    }
}

@Composable
private fun PasswordInput() {
    var passwordVisibility by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("") }

    OutlinedTextField(
        value = text,
        visualTransformation = if (passwordVisibility) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        trailingIcon = {
            val image = if (passwordVisibility) {
                Icons.Default.Visibility
            } else {
                Icons.Default.VisibilityOff
            }
            IconButton(onClick = {
                passwordVisibility = !passwordVisibility
            }) {
                Icon(image, null)
            }
        },
        label = { Text(text = stringResource(id = R.string.password_label)) },
        onValueChange = { text = it },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun DaysInput() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        PushieText(text = stringResource(id = R.string.days_expiry_text))
        PushieNumberPicker(1, 1..90)
    }
}

@Composable
private fun ViewsInput() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        PushieText(text = stringResource(id = R.string.views_expiry_text))
        PushieNumberPicker(1, 1..100)
    }
}

@Composable
private fun SendButton(onSendClicked: () -> Unit) {
    Button(
        onClick = onSendClicked
    ) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = stringResource(id = R.string.main_send)
        )
    }
}

@Composable
@Preview
private fun Preview() {
    PusherScreen { /* */ }
}
