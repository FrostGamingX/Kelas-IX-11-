package com.example.simpleanimation.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.easybot.R
import kotlinx.coroutines.launch
import kotlin.math.pow

// Custom Easing to mimic OvershootInterpolator
class OvershootEasing(private val tension: Float = 2f) : Easing {
    override fun transform(fraction: Float): Float {
        val t = fraction - 1f
        return (t * t * ((tension + 1) * t + tension) + 1f)
    }
}

@Composable
fun SimpleAnimationPage(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues = PaddingValues(0.dp) // Add innerPadding parameter
) {
    val scale = remember { Animatable(0f) }
    val rotation = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(innerPadding) // Apply innerPadding here
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Logo",
            modifier = Modifier
                .size(200.dp)
                .graphicsLayer(
                    scaleX = scale.value,
                    scaleY = scale.value,
                    rotationZ = rotation.value
                )
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = {
            coroutineScope.launch {
                scale.snapTo(0f)
                rotation.snapTo(0f)
                launch {
                    scale.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(
                            durationMillis = 1000,
                            easing = OvershootEasing(tension = 2f)
                        )
                    )
                }
                launch {
                    rotation.animateTo(
                        targetValue = 360f,
                        animationSpec = tween(
                            durationMillis = 1000,
                            easing = OvershootEasing(tension = 2f)
                        )
                    )
                }
            }
        }) {
            Text("Start Animation")
        }
    }
}