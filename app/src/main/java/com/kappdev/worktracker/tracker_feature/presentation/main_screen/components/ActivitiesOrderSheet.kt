package com.kappdev.worktracker.tracker_feature.presentation.main_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kappdev.worktracker.R
import com.kappdev.worktracker.tracker_feature.domain.util.ActivityOrder
import com.kappdev.worktracker.tracker_feature.domain.util.OrderType
import com.kappdev.worktracker.tracker_feature.presentation.common.components.DefaultRadioButton
import com.kappdev.worktracker.tracker_feature.presentation.main_screen.MainScreenViewModel

@Composable
fun ActivitiesOrderSheet(
    viewModel: MainScreenViewModel
) {
    val order = viewModel.order.value
    val onOrderChange: (ActivityOrder) -> Unit = { newOrder ->
        viewModel.setOrder(newOrder)
        viewModel.refreshData()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colors.surface,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            )
            .padding(vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            DefaultRadioButton(
                text = stringResource(R.string.asc_title),
                modifier = Modifier.fillMaxWidth(0.5f),
                selected = order.orderType is OrderType.Ascending,
                onSelect = {
                    onOrderChange(order.copy(orderType = OrderType.Ascending))
                }
            )
            DefaultRadioButton(
                text = stringResource(R.string.desc_title),
                selected = order.orderType is OrderType.Descending,
                onSelect = {
                    onOrderChange(order.copy(orderType = OrderType.Descending))
                }
            )
        }

        Divider(
            Modifier
                .fillMaxWidth(0.95f)
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            DefaultRadioButton(
                text = stringResource(R.string.by_name_title),
                modifier = Modifier.fillMaxWidth(0.5f),
                selected = order is ActivityOrder.Name,
                onSelect = {
                    onOrderChange(ActivityOrder.Name(order.orderType))
                }
            )
            DefaultRadioButton(
                text = stringResource(R.string.by_time_title),
                selected = order is ActivityOrder.Date,
                onSelect = {
                    onOrderChange(ActivityOrder.Date(order.orderType))
                }
            )
        }
    }
}