package com.example.mymovieapp.features.profile.ui

import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.abs
import kotlin.math.roundToInt


@Composable
fun CustomContent() {
    var day by remember {
        mutableStateOf("16")
    }
    var month by remember {
        mutableStateOf("Jul")
    }
    var year by remember {
        mutableStateOf("1980")
    }
    Box(
        modifier = Modifier
            .background(Color.Gray)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            /*Picker(
                list = (1..2).toList(),
                onValueChanged = {
                    day = it.toString()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                showAmount = 10
            )*/

            VerticalPicker(
                list = listOf("English","Turkish"),
                onValueChanged = {
                    day = it
                },
                modifier = Modifier
                    .width(60.dp)
                    .height(150.dp),
                showAmount = 2
            )
            /*Picker(
                listOf(
                    "Jan",
                    "Feb",
                    "Mar",
                    "Apr",
                    "May",
                    "Jun",
                    "Jul",
                    "Aug",
                    "Sep",
                    "Oct",
                    "Nov",
                    "Dec"
                ),
                onValueChanged = {
                    month = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                showAmount = 6
            )
            Picker(
                list = (1950..2010).toList(),
                onValueChanged = {
                    year = it.toString()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                showAmount = 8
            )*/
        }
        Rechtangle(
            modifier = Modifier
                .height(40.dp)
                .fillMaxWidth()
        )
        Text(
            "Date of Birth: $month $day $year",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 130.dp),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp
        )
    }
}

@Composable
fun Rechtangle(
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
    ) {
        drawRoundRect(
            size = Size(size.width, size.height),
            style = Stroke(width = 10f, join = StrokeJoin.Round),
            color = Color.Yellow,
            cornerRadius = CornerRadius(25f, 25f)
        )
    }
}

@Composable
fun <T> Picker(
    list: List<T>,
    showAmount: Int = 10,
    modifier: Modifier = Modifier,
    style: PickerStyle = PickerStyle(),
    onValueChanged: (T) -> Unit
) {

    val listCount by remember {
        mutableStateOf(list.size)
    }

    val correctionValue by remember {
        if (list.size % 2 == 0) {
            mutableStateOf(1)
        } else {
            mutableStateOf(0)
        }
    }

    var dragStartedX by remember {
        mutableStateOf(0f)
    }

    var currentDragX by remember {
        mutableStateOf(0f)
    }

    var oldX by remember {
        mutableStateOf(0f)
    }

    Canvas(
        modifier = modifier
            .pointerInput(true) {
                detectDragGestures(
                    onDragStart = { offset ->
                        dragStartedX = offset.x
                    },
                    onDragEnd = {
                        val spacePerItem = size.width / showAmount
                        val rest = currentDragX % spacePerItem

                        val roundUp = abs(rest / spacePerItem).roundToInt() == 1
                        val newX = if (roundUp) {
                            if (rest < 0) {
                                currentDragX + abs(rest) - spacePerItem
                            } else {
                                currentDragX - rest + spacePerItem
                            }
                        } else {
                            if (rest < 0) {
                                currentDragX + abs(rest)
                            } else {
                                currentDragX - rest
                            }
                        }
                        currentDragX = newX.coerceIn(
                            minimumValue = -(listCount / 2f) * spacePerItem,
                            maximumValue = (listCount / 2f - correctionValue) * spacePerItem
                        )
                        val index = (listCount / 2) + (currentDragX / spacePerItem).toInt()
                        onValueChanged(list[index])
                        oldX = currentDragX
                    },
                    onDrag = { change, _ ->
                        val changeX = change.position.x
                        val newX = oldX + (dragStartedX - changeX)
                        val spacePerItem = size.width / showAmount
                        currentDragX = newX.coerceIn(
                            minimumValue = -(listCount / 2f) * spacePerItem,
                            maximumValue = (listCount / 2f - correctionValue) * spacePerItem
                        )
                        val index = (listCount / 2) + (currentDragX / spacePerItem).toInt()
                        onValueChanged(list[index])
                    }
                )
            }
    ) {

        val top = 0f
        val bot = size.height

        drawContext.canvas.nativeCanvas.apply {
            drawRect(
                Rect(0, top.toInt(), size.width.toInt(), bot.toInt()),
                Paint().apply {
                    color = Color.White.copy(alpha = 0.8f).toArgb()
                    setShadowLayer(
                        30f,
                        0f,
                        0f,
                        android.graphics.Color.argb(50, 0, 0, 0)
                    )
                }
            )
        }
        val spaceForEachItem = size.width / showAmount
        for (i in 0 until listCount) {
            val currentX = i * spaceForEachItem - currentDragX -
                    ((listCount - 1 + correctionValue - showAmount) / 2 * spaceForEachItem)

            val lineStart = Offset(
                x = currentX,
                y = 0f
            )

            val lineEnd = Offset(
                x = currentX,
                y = style.lineLength
            )

            drawLine(
                color = style.lineColor,
                strokeWidth = 1.5.dp.toPx(),
                start = lineStart,
                end = lineEnd
            )

            drawContext.canvas.nativeCanvas.apply {
                val y = style.lineLength + 5.dp.toPx() + style.textSize.toPx()

                drawText(
                    list[i].toString(),
                    currentX,
                    y,
                    Paint().apply {
                        textSize = style.textSize.toPx()
                        textAlign = Paint.Align.CENTER
                        isFakeBoldText = true
                    }
                )
            }

        }


    }

}

@Composable
fun <T> VerticalPicker(
    list: List<T>,
    showAmount: Int = 10,
    modifier: Modifier = Modifier,
    style: PickerStyle = PickerStyle(),
    onValueChanged: (T) -> Unit
) {
    val listCount by remember {
        mutableStateOf(list.size)
    }

    val correctionValue by remember {
        if (list.size % 2 == 0) {
            mutableStateOf(1)
        } else {
            mutableStateOf(0)
        }
    }

    var dragStartedY by remember {
        mutableStateOf(0f)
    }

    var currentDragY by remember {
        mutableStateOf(0f)
    }

    var oldY by remember {
        mutableStateOf(0f)
    }

    Canvas(
        modifier = modifier
            .pointerInput(true) {
                detectDragGestures(
                    onDragStart = { offset ->
                        dragStartedY = offset.y
                    },
                    onDragEnd = {
                        val spacePerItem = size.height / showAmount
                        val rest = currentDragY % spacePerItem

                        val roundUp = abs(rest / spacePerItem).roundToInt() == 1
                        val newY = if (roundUp) {
                            if (rest < 0) {
                                currentDragY + abs(rest) - spacePerItem
                            } else {
                                currentDragY - rest + spacePerItem
                            }
                        } else {
                            if (rest < 0) {
                                currentDragY + abs(rest)
                            } else {
                                currentDragY - rest
                            }
                        }
                        currentDragY = newY.coerceIn(
                            minimumValue = -(listCount / 2f) * spacePerItem,
                            maximumValue = (listCount / 2f - correctionValue) * spacePerItem
                        )
                        val index = (listCount / 2) + (currentDragY / spacePerItem).toInt()
                        onValueChanged(list[index])
                        oldY = currentDragY
                    },
                    onDrag = { change, _ ->
                        val changeY = change.position.y
                        val newY = oldY + (dragStartedY - changeY)
                        val spacePerItem = size.height / showAmount
                        currentDragY = newY.coerceIn(
                            minimumValue = -(listCount / 2f) * spacePerItem,
                            maximumValue = (listCount / 2f - correctionValue) * spacePerItem
                        )
                        val index = (listCount / 2) + (currentDragY / spacePerItem).toInt()
                        onValueChanged(list[index])
                    }
                )
            }
    ) {
        val left = 0f
        val right = size.width

        drawContext.canvas.nativeCanvas.apply {
            drawRect(
                Rect(left.toInt(), 0, right.toInt(), size.height.toInt()),
                Paint().apply {
                    color = Color.White.copy(alpha = 0.8f).toArgb()
                    setShadowLayer(
                        30f,
                        0f,
                        0f,
                        android.graphics.Color.argb(50, 0, 0, 0)
                    )
                }
            )
        }

        val spaceForEachItem = size.height / showAmount
        for (i in 0 until listCount) {
            val currentY = i * spaceForEachItem - currentDragY -
                    ((listCount - 1 + correctionValue - showAmount) / 2 * spaceForEachItem)

            val lineStart = Offset(
                x = 0f,
                y = currentY
            )

            val lineEnd = Offset(
                x = style.lineLength,
                y = currentY
            )

            drawLine(
                color = style.lineColor,
                strokeWidth = 1.5.dp.toPx(),
                start = lineStart,
                end = lineEnd
            )

            drawContext.canvas.nativeCanvas.apply {
                val x = style.lineLength + 5.dp.toPx() + style.textSize.toPx()

                drawText(
                    list[i].toString(),
                    0f,
                    currentY + style.textSize.toPx() / 2,
                    Paint().apply {
                        textSize = style.textSize.toPx()
                        textAlign = Paint.Align.LEFT
                        isFakeBoldText = true
                    }
                )
            }
        }
    }
}



data class PickerStyle(
    val lineColor: Color = Color.Yellow,
    val lineLength: Float = 45f,
    val textSize: TextUnit = 24.sp
)