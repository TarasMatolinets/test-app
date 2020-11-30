package com.demo.mvvm.view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue

@Composable
fun DotTextField(
    state: MutableState<TextFieldValue>,
    circleParams: CircleParams,
    lineParams: LineParams,
    shapeCount: Int,
    shiftBetween: Int = 60,
    onImeActionPerformed: (ImeAction, SoftwareKeyboardController?) -> Unit = { _, _ -> },
    onValueChange: (TextFieldValue) -> Unit = {},
    imeAction: ImeAction = ImeAction.Done,
    onTextInputStarted: (SoftwareKeyboardController) -> Unit = {},
    onFinishTypeListener: (TextFieldValue) -> Unit = {}
) {

    @Composable
    fun drawShape(
        value: TextFieldValue,
        shapeCount: Int,
        shiftBetween: Int,
        circleParams: CircleParams,
        lineParams: LineParams
    ) {
        Canvas(modifier = Modifier.fillMaxWidth()) {
            val centerX = size.width / 2
            val lineSize = (shapeCount * lineParams.lineLength + (shiftBetween * (shapeCount - 1))) / 2

            var start = centerX - lineSize

            for (index in 0 until shapeCount) {
                val end = start + lineParams.lineLength
                if (value.text.isNotEmpty()) {
                    if (index < value.text.length) {
                        drawCircle(
                            color = circleParams.color,
                            radius = circleParams.radius,
                            center = Offset(start + lineParams.lineLength / 2, 0f)
                        )
                    } else {
                        drawLine(
                            color = lineParams.color,
                            start = Offset(start, 0f),
                            end = Offset(end, 0f),
                            strokeWidth = lineParams.strokeWidth
                        )
                    }
                } else {
                    drawLine(
                        color = lineParams.color,
                        start = Offset(start, 0f),
                        end = Offset(end, 0f),
                        strokeWidth = lineParams.strokeWidth
                    )
                }
                start = end + shiftBetween
            }
        }
    }

    TextField(
        value = state.value,
        onValueChange = {
            if (it.text.length <= shapeCount) {
                state.value = it
            }
            if (it.text.length == shapeCount) {
                onFinishTypeListener(it)
            }
            onValueChange(it)
        },
        label = {
            drawShape(state.value, shapeCount, shiftBetween, circleParams, lineParams)
        },
        imeAction = imeAction,
        onImeActionPerformed = onImeActionPerformed,
        keyboardType = KeyboardType.NumberPassword,
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = Color.Transparent,
        activeColor = Color.Transparent,
        inactiveColor = Color.Transparent,
        onTextInputStarted = { onTextInputStarted(it) }
    )
}

data class CircleParams(val color: Color, val radius: Float)
data class LineParams(val color: Color, val start: Offset, val end: Offset, val strokeWidth: Float, val lineLength: Int = 60)
