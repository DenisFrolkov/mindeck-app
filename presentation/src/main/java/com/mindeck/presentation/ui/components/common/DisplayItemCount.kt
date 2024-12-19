package com.mindeck.presentation.ui.components.common

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mindeck.presentation.ui.components.utils.getPluralForm
import com.mindeck.presentation.ui.theme.scrim_black

@SuppressLint("Range")
@Composable
fun DisplayItemCount(
    pluralsTextOne: Int,
    pluralsTextTwo: Int = 0,
    listOne: List<Any>,
    listTwo: List<Any> = emptyList(),
    textStyle: TextStyle
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = pluralStringResource(
                pluralsTextOne,
                getPluralForm(listOne.size),
                listOne.size
            ),
            style = textStyle,
            modifier = Modifier.weight(1f),
            textAlign = if (pluralsTextTwo != 0) TextAlign.End else TextAlign.Center
        )
        if (pluralsTextTwo != 0) {
            Spacer(modifier = Modifier.width(4.dp))
            Box(
                modifier = Modifier
                    .size(height = 16.dp, width = 1.dp)
                    .background(
                        color = scrim_black
                    )
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = pluralStringResource(
                    pluralsTextTwo,
                    getPluralForm(listTwo.size),
                    listTwo.size
                ),
                style = textStyle,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Start
            )
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
}