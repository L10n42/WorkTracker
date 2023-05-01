package com.kappdev.worktracker.tracker_feature.presentation.activity_review.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kappdev.worktracker.R
import com.kappdev.worktracker.tracker_feature.presentation.activity_review.GraphViewState
import com.kappdev.worktracker.tracker_feature.presentation.common.components.CustomDropDownMenu
import com.kappdev.worktracker.ui.spacing

@Composable
fun GraphViewSelector(
    selected: GraphViewState,
    modifier: Modifier = Modifier,
    onViewChange: (viewState: GraphViewState) -> Unit
) {
    val viewStates = listOf(GraphViewState.DAY, GraphViewState.WEEK, GraphViewState.MONTH, GraphViewState.YEAR)
    var expanded by remember { mutableStateOf(false) }

    val animatedButton by animateFloatAsState(
        targetValue = if (expanded) -180f else 0f,
        animationSpec = spring(stiffness = Spring.StiffnessMedium)
    )

    Row(
        modifier = modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colors.onSurface,
                shape = SelectorShape
            )
            .clip(SelectorShape)
            .clickable {
                expanded = true
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = getStateTitle(selected),
            fontSize = 16.sp,
            color = MaterialTheme.colors.onSurface,
            modifier = Modifier
                .width(64.dp)
                .padding(
                    horizontal = MaterialTheme.spacing.small,
                    vertical = MaterialTheme.spacing.extraSmall
                )
        )

        Icon(
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = "show menu icon",
            tint = MaterialTheme.colors.onSurface,
            modifier = Modifier.rotate(animatedButton)
        )

        val topPadding = with(LocalDensity.current) { 6.dp.roundToPx() }
        CustomDropDownMenu(
            expanded = expanded,
            alignment = Alignment.TopStart,
            enterAnim = enterAnimation(),
            exitAnim = exitAnimation(),
            modifier = Modifier.width(64.dp),
            offset = IntOffset(0, topPadding),
            dismiss = { expanded = false }
        ) {
            viewStates.forEach { viewState ->
                Item(viewState) {
                    expanded = false
                    onViewChange(it)
                }
            }
        }
    }
}

private val SelectorShape = RoundedCornerShape(
    topEnd = 8.dp,
    topStart = 0.dp,
    bottomEnd = 0.dp,
    bottomStart = 8.dp
)

@Composable
private fun exitAnimation() = shrinkVertically(
    animationSpec = spring(stiffness = Spring.StiffnessMedium),
    shrinkTowards = Alignment.Top,
) + fadeOut()

@Composable
private fun enterAnimation() = expandVertically(
    animationSpec = spring(stiffness = Spring.StiffnessMedium),
    expandFrom = Alignment.Top
) + fadeIn()


@Composable
private fun Item(
    state: GraphViewState,
    onClick: (state: GraphViewState) -> Unit
) {
    DropdownMenuItem(
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
        onClick = { onClick(state) }
    ) {
        Text(
            text = getStateTitle(state),
            color = MaterialTheme.colors.onSurface,
            fontSize = 16.sp
        )
    }
}

@Composable
private fun getStateTitle(state: GraphViewState): String {
    return when (state) {
        GraphViewState.DAY -> stringResource(R.string.gv_day)
        GraphViewState.WEEK -> stringResource(R.string.gv_week)
        GraphViewState.MONTH -> stringResource(R.string.gv_month)
        GraphViewState.YEAR -> stringResource(R.string.gv_year)
    }
}























