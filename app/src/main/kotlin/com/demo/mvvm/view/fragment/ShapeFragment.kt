package com.demo.mvvm.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.core.*
import androidx.compose.animation.transition
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.ToggleableState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Slider
import androidx.compose.material.TriStateCheckbox
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.demo.demo.R
import com.demo.mvvm.model.ShapeParams
import com.demo.mvvm.viewModel.MainActivityViewModel

private const val ROTATE_DURATION_MILLIS = 1000
private const val ROTATE_DURATION_SECONDS = 10

class ShapeFragment(private val shapeTypeType: ShapeType) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProvider(requireActivity()).get(MainActivityViewModel::class.java)
        val shapeParams: ShapeParams? = viewModel.mapShapes?.get(shapeTypeType)

        view.findViewById<ComposeView>(R.id.composeShapeView).setContent {
            val modifier = Modifier.padding(16.dp)
            val rotation = FloatPropKey()

            fun createDefinition(
                speedState: MutableState<Float>,
                clockWise: MutableState<Boolean>
            ) = transitionDefinition<Int> {
                if (clockWise.value) {
                    state(0) { this[rotation] = 0f }
                    state(1) { this[rotation] = 360f }
                } else {
                    state(0) { this[rotation] = 360f }
                    state(1) { this[rotation] = 0f }
                }

                transition {
                    rotation using repeatable(
                        animation = tween(
                            easing = LinearEasing,
                            durationMillis = (ROTATE_DURATION_SECONDS - speedState.value.toInt()) * ROTATE_DURATION_MILLIS
                        ),
                        iterations = AnimationConstants.Infinite
                    )
                }
            }

            ScrollableColumn(modifier = modifier.fillMaxSize()) {
                val speedIndicator =
                    remember { mutableStateOf(shapeParams?.speed?.toFloat() ?: 5f) }
                val sizeIndicator = remember { mutableStateOf(shapeParams?.size?.toFloat() ?: 1f) }
                val clockWiseIndicator =
                    remember { mutableStateOf(shapeParams?.clockWise ?: false) }

                shapeParams?.apply {
                    speed = speedIndicator.value.toInt()
                    size = sizeIndicator.value.toInt()
                    clockWise = clockWiseIndicator.value
                }?.also {
                    viewModel.updateMap(shapeTypeType, it)
                }

                Column {
                    Box(
                        modifier = Modifier.preferredHeight(300.dp).fillMaxWidth(),
                        gravity = ContentGravity.Center,
                        backgroundColor = Color.Green,
                        children = {
                            val state = transition(
                                definition = createDefinition(speedIndicator, clockWiseIndicator),
                                initState = 0,
                                toState = 1
                            )
                            Canvas(modifier = Modifier.preferredSize(buildSize(sizeIndicator))) {
                                rotate(state[rotation]) {
                                    when (shapeTypeType) {
                                        ShapeType.Square -> drawRect(color = Color(255, 342, 212))
                                        ShapeType.Triangle -> drawTriangle(sizeIndicator)
                                        ShapeType.Hexagon -> drawHexagon(sizeIndicator)

                                    }
                                }
                            }

                        }
                    )
                    ComposeSpeedIndicator(speedIndicator)
                    ComposeSizeIndicator(sizeIndicator)
                    ComposeClockWise(clockWiseIndicator)
                }
            }
        }
    }


    private fun buildSize(sizeIndicator: MutableState<Float>): Dp {
        return when (sizeIndicator.value) {
            1f -> 100.dp
            2f -> 150.dp
            else -> 200.dp
        }
    }

    private fun DrawScope.drawHexagon(sizeIndicator: MutableState<Float>) {
        val centerX = size.width / 2
        val centerY = size.height / 2
        val shift = when (sizeIndicator.value) {
            1f -> 25
            2f -> 50
            else -> 75
        }
        val step = when (sizeIndicator.value) {
            1f -> 50
            2f -> 100
            else -> 150
        }
        //A-B line
        drawLine(
            strokeWidth = Stroke.DefaultMiter,
            color = Color.Red,
            start = Offset(
                centerX - step,
                centerY - step
            ),
            end = Offset(
                centerX + step,
                centerY - step
            )
        )
        // B -D line
        drawLine(
            strokeWidth = Stroke.DefaultMiter,
            color = Color.Red,
            start = Offset(
                centerX + step,
                centerY - step
            ),
            end = Offset(
                centerX + step + shift,
                centerY
            )
        )
        // D - F line
        drawLine(
            strokeWidth = Stroke.DefaultMiter,
            color = Color.Red,
            start = Offset(
                centerX + step + shift,
                centerY
            ),
            end = Offset(
                centerX + step,
                centerY + step
            )
        )
        // F - E line
        drawLine(
            strokeWidth = Stroke.DefaultMiter,
            color = Color.Red,
            start = Offset(
                centerX + step,
                centerY + step
            ),
            end = Offset(
                centerX - step,
                centerY + step
            )
        )
        //E-C line
        drawLine(
            strokeWidth = Stroke.DefaultMiter,
            color = Color.Red,
            start = Offset(
                centerX - step,
                centerY + step
            ),
            end = Offset(
                centerX - step - shift,
                centerY
            )
        )

        //C-A line
        drawLine(
            strokeWidth = Stroke.DefaultMiter,
            color = Color.Red,
            start = Offset(
                centerX - step - shift,
                centerY
            ),
            end = Offset(
                centerX - step,
                centerY - step
            )
        )

    }

    private fun DrawScope.drawTriangle(sizeIndicator: MutableState<Float>) {
        val centerX = size.width / 2
        val centerY = size.height / 2

        val step = when (sizeIndicator.value) {
            1f -> 50
            2f -> 100
            else -> 150
        }
        //A point
        drawLine(
            strokeWidth = Stroke.DefaultMiter,
            color = Color.White,
            start = Offset(
                centerX,
                centerY - step
            ),
            end = Offset(
                centerX - step,
                centerY + step
            )
        )
        // B point
        drawLine(
            strokeWidth = Stroke.DefaultMiter,
            color = Color.White,
            start = Offset(
                centerX - step,
                centerY + step
            ),
            end = Offset(
                centerX + step,
                centerY + step
            )
        )
        // C point
        drawLine(
            strokeWidth = Stroke.DefaultMiter,
            color = Color.White,
            start = Offset(
                centerX,
                centerY - step
            ),
            end = Offset(
                centerX + step,
                centerY + step
            )
        )
    }

    @Composable
    fun ComposeSpeedIndicator(indicator: MutableState<Float>) {
        var speedIndicator by remember { mutableStateOf(indicator.value) }
        Card(shape = RoundedCornerShape(4.dp)) {
            Slider(
                modifier = Modifier.padding(8.dp),
                value = speedIndicator,
                valueRange = 1f..9f,
                steps = 2,
                onValueChange = { speedIndicator = it })
        }

        Text(
            text = "Speed value is %.1f".format(speedIndicator),
            modifier = Modifier.padding(8.dp),
            color = Color.White
        )
        indicator.value = speedIndicator
    }

    @Composable
    fun ComposeSizeIndicator(sizeIndicator: MutableState<Float>) {
        var sliderValue by remember { mutableStateOf(sizeIndicator.value) }

        Card(shape = RoundedCornerShape(4.dp)) {
            Slider(modifier = Modifier.padding(8.dp),
                value = sliderValue,
                valueRange = 1f..3f,
                steps = 1,
                onValueChange = { sliderValue = it })
        }

        Text(
            text = "Size value is %.1f".format(sliderValue),
            modifier = Modifier.padding(8.dp),
            color = Color.White
        )
        sizeIndicator.value = sliderValue
    }

    @Composable
    fun ComposeClockWise(clockWiseIndicator: MutableState<Boolean>) {
        val clockWise = if (clockWiseIndicator.value) {
            1
        } else {
            0
        }

        val toggleableStateArray = listOf(ToggleableState.Off, ToggleableState.On)
        var counter by remember { mutableStateOf(clockWise) }

        Card(shape = RoundedCornerShape(4.dp)) {
            Row(modifier = Modifier.padding(16.dp)) {
                TriStateCheckbox(
                    state = toggleableStateArray[counter % 2],
                    onClick = {
                        counter++
                        clockWiseIndicator.value =
                            toggleableStateArray[counter % 2] == ToggleableState.On
                    })
                Text(
                    text = "ClockWise",
                    modifier = Modifier.padding(start = 8.dp),
                )
            }
        }
    }
}

enum class ShapeType {
    Square, Triangle, Hexagon
}
