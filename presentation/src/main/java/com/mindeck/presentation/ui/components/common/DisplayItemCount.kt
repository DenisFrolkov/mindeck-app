package com.mindeck.presentation.ui.components.common

import androidx.annotation.PluralsRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import com.mindeck.presentation.R
import com.mindeck.presentation.ui.components.utils.dimenDpResource
import com.mindeck.presentation.ui.components.utils.dimenFloatResource

@Composable
fun DisplayItemCount(
    @PluralsRes plurals: Int,
    count: Int,
    textStyle: TextStyle
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = pluralStringResource(plurals, count, count),
            style = textStyle,
            modifier = Modifier.weight(dimenFloatResource(R.dimen.float_one_significance)),
            textAlign = TextAlign.Center
        )
    }
    Spacer(modifier = Modifier.height(dimenDpResource(R.dimen.spacer_medium)))
}