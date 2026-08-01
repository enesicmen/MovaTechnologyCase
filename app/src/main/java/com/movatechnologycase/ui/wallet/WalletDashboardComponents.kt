package com.movatechnologycase.ui.wallet

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ArrowDownward
import androidx.compose.material.icons.outlined.ArrowUpward
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.movatechnologycase.R
import com.movatechnologycase.core.design.Colors
import com.movatechnologycase.data.repository.WalletScenario

@Composable
fun WalletLoadedContent(
    dashboard: WalletDashboardUiModel,
    selectedScenario: WalletScenario,
    isBalanceVisible: Boolean,
    onScenarioSelected: (WalletScenario) -> Unit,
    onBalanceVisibilityClick: () -> Unit,
    onTopUpClick: () -> Unit,
    onChildClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        contentPadding = PaddingValues(
            start = 20.dp,
            end = 20.dp,
            top = 18.dp,
            bottom = 28.dp
        ),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            WalletHeader()
        }

        item {
            WalletScenarioSelector(
                selectedScenario = selectedScenario,
                onScenarioSelected = onScenarioSelected
            )
        }

        item {
            WalletBalanceCard(
                balanceText = dashboard.balanceText,
                currencyCode = dashboard.currencyCode,
                isBalanceVisible = isBalanceVisible,
                onVisibilityClick = onBalanceVisibilityClick
            )
        }

        item {
            WalletQuickActions(
                onTopUpClick = onTopUpClick
            )
        }

        item {
            WalletChildrenSection(
                children = dashboard.children,
                onChildClick = onChildClick
            )
        }

        item {
            WalletRecentTransactionsCard(
                transactions = dashboard.transactions
            )
        }
    }
}

@Composable
fun WalletStateContent(
    selectedScenario: WalletScenario,
    onScenarioSelected: (WalletScenario) -> Unit,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(
                horizontal = 20.dp,
                vertical = 18.dp
            )
    ) {
        WalletHeader()

        Spacer(modifier = Modifier.height(18.dp))

        WalletScenarioSelector(
            selectedScenario = selectedScenario,
            onScenarioSelected = onScenarioSelected
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}

@Composable
fun WalletHeader(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = stringResource(id = R.string.my_wallet),
                color = Colors.TextPrimary,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stringResource(id = R.string.manage_your_balance_and_transactions),
                color = Colors.TextSecondary,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Surface(
            modifier = Modifier.size(52.dp),
            shape = CircleShape,
            color = Colors.BlueSoft
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(26.dp),
                    imageVector = Icons.Outlined.AccountBalanceWallet,
                    contentDescription = null,
                    tint = Colors.Blue
                )
            }
        }
    }
}


@Composable
fun WalletScenarioSelector(
    modifier: Modifier = Modifier,
    selectedScenario: WalletScenario,
    onScenarioSelected: (WalletScenario) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.demo_status),
            color = Colors.TextSecondary,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xFFECEFF5))
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            WalletScenario.entries.forEach { scenario ->
                val selected = selectedScenario == scenario

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(11.dp))
                        .background(
                            if (selected) {
                                Colors.Navy
                            } else {
                                Color.Transparent
                            }
                        )
                        .clickable {
                            onScenarioSelected(scenario)
                        }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(id = scenario.displayNameRes()),
                        color = if (selected) {
                            Color.White
                        } else {
                            Colors.TextSecondary
                        },
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
fun WalletBalanceCard(
    modifier: Modifier = Modifier,
    balanceText: String,
    currencyCode: String,
    isBalanceVisible: Boolean,
    onVisibilityClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(218.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Colors.Navy,
                        Color(0xFF173A7A),
                        Colors.Blue
                    )
                )
            )
    ) {
        Box(
            modifier = Modifier
                .size(190.dp)
                .offset(
                    x = 240.dp,
                    y = (-70).dp
                )
                .clip(CircleShape)
                .background(
                    Color.White.copy(alpha = 0.08f)
                )
        )

        Box(
            modifier = Modifier
                .size(120.dp)
                .offset(
                    x = 270.dp,
                    y = 140.dp
                )
                .clip(CircleShape)
                .background(
                    Colors.Cyan.copy(alpha = 0.16f)
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.available_balance),
                    color = Color.White.copy(alpha = 0.78f),
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.weight(1f))

                TextButton(
                    onClick = onVisibilityClick,
                    contentPadding = PaddingValues(
                        horizontal = 8.dp,
                        vertical = 0.dp
                    ),
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = if (isBalanceVisible) {
                            stringResource(id = R.string.hide)
                        } else {
                            stringResource(id = R.string.show)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = if (isBalanceVisible) {
                    balanceText
                } else {
                    "••••••"
                },
                color = Color.White,
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = stringResource(id = R.string.main_wallet),
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = stringResource(id = R.string.secure_digital_account),
                        color = Color.White.copy(alpha = 0.65f),
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Surface(
                    shape = RoundedCornerShape(50.dp),
                    color = Color.White.copy(alpha = 0.14f)
                ) {
                    Text(
                        modifier = Modifier.padding(
                            horizontal = 14.dp,
                            vertical = 8.dp
                        ),
                        text = currencyCode,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}


@Composable
fun WalletQuickActions(
    modifier: Modifier = Modifier,
    onTopUpClick: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.fast_transactions),
            color = Colors.TextPrimary,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            WalletQuickActionItem(
                modifier = Modifier.weight(1f),
                title = stringResource(id = R.string.add_money),
                icon = Icons.Outlined.Add,
                iconColor = Colors.Blue,
                backgroundColor = Colors.BlueSoft,
                onClick = onTopUpClick
            )
        }
    }
}

@Composable
fun WalletQuickActionItem(
    modifier: Modifier = Modifier,
    title: String,
    icon: ImageVector,
    iconColor: Color,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Colors.Surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        ),
        border = BorderStroke(
            width = 1.dp,
            color = Colors.Border
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 6.dp,
                    vertical = 14.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier.size(42.dp),
                shape = CircleShape,
                color = backgroundColor
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier.size(21.dp),
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(9.dp))

            Text(
                text = title,
                color = Colors.TextPrimary,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                textAlign = TextAlign.Center
            )
        }
    }
}


@Composable
fun WalletChildrenSection(
    modifier: Modifier = Modifier,
    children: List<ChildWalletUiModel>,
    onChildClick: (String) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.linked_accounts),
                color = Colors.TextPrimary,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.width(8.dp))

            Surface(
                shape = CircleShape,
                color = Colors.BlueSoft
            ) {
                Text(
                    modifier = Modifier.padding(
                        horizontal = 9.dp,
                        vertical = 4.dp
                    ),
                    text = children.size.toString(),
                    color = Colors.Blue,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (children.isEmpty()) {
            WalletEmptyChildrenContent()
        } else {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = children,
                    key = ChildWalletUiModel::id
                ) { child ->
                    ChildWalletCard(
                        child = child,
                        onClick = {
                            onChildClick(child.id)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ChildWalletCard(
    modifier: Modifier = Modifier,
    child: ChildWalletUiModel,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .width(174.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = Colors.Surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        ),
        border = BorderStroke(
            width = 1.dp,
            color = Colors.Border
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(44.dp),
                    shape = CircleShape,
                    color = Colors.OrangeSoft
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = child.name
                                .firstOrNull()
                                ?.uppercase()
                                .orEmpty(),
                            color = Color(0xFFDC7A1D),
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Surface(
                    shape = RoundedCornerShape(50.dp),
                    color = Colors.PositiveSoft
                ) {
                    Text(
                        modifier = Modifier.padding(
                            horizontal = 8.dp,
                            vertical = 4.dp
                        ),
                        text = stringResource(id = R.string.active),
                        color = Colors.Positive,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = child.name,
                color = Colors.TextPrimary,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = child.grade,
                color = Colors.TextSecondary,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(id = R.string.balance),
                color = Colors.TextSecondary,
                style = MaterialTheme.typography.labelSmall
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = child.balanceText,
                color = Colors.TextPrimary,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
        }
    }
}


@Composable
fun WalletEmptyChildrenContent(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Colors.Surface
        ),
        border = BorderStroke(
            width = 1.dp,
            color = Colors.Border
        )
    ) {
        Text(
            modifier = Modifier.padding(20.dp),
            text = stringResource(id = R.string.there_is_no_linked_child_account_yet),
            color = Colors.TextSecondary,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}


@Composable
fun WalletRecentTransactionsCard(
    transactions: List<WalletTransactionUiModel>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Colors.Surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        ),
        border = BorderStroke(
            width = 1.dp,
            color = Colors.Border
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = stringResource(id = R.string.final_processes),
                        color = Colors.TextPrimary,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = stringResource(id = R.string.the_most_recent_money_movements),
                        color = Colors.TextSecondary,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (transactions.isEmpty()) {
                WalletEmptyTransactionsContent()
            } else {
                transactions.forEachIndexed { index, transaction ->
                    WalletTransactionItem(
                        transaction = transaction
                    )

                    if (index != transactions.lastIndex) {
                        HorizontalDivider(
                            color = Colors.Border
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WalletTransactionItem(
    transaction: WalletTransactionUiModel,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 11.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(46.dp),
            shape = CircleShape,
            color = if (transaction.isIncome) {
                Colors.PositiveSoft
            } else {
                Colors.NegativeSoft
            }
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(21.dp),
                    imageVector = if (transaction.isIncome) {
                        Icons.Outlined.ArrowDownward
                    } else {
                        Icons.Outlined.ArrowUpward
                    },
                    contentDescription = null,
                    tint = if (transaction.isIncome) {
                        Colors.Positive
                    } else {
                        Colors.Negative
                    }
                )
            }
        }

        Spacer(modifier = Modifier.width(13.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = transaction.title,
                color = Colors.TextPrimary,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = transaction.dateText,
                color = Colors.TextSecondary,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = transaction.amountText,
            color = if (transaction.isIncome) {
                Colors.Positive
            } else {
                Colors.TextPrimary
            },
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )
    }
}


@Composable
fun WalletEmptyTransactionsContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier.size(64.dp),
            shape = CircleShape,
            color = Colors.BlueSoft
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(29.dp),
                    imageVector = Icons.AutoMirrored.Outlined.ReceiptLong,
                    contentDescription = null,
                    tint = Colors.Blue
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = stringResource(id = R.string.no_transactions_have_been_made_yet),
            color = Colors.TextPrimary,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = stringResource(id = R.string.your_first_money_transaction_will_be_displayed_here),
            color = Colors.TextSecondary,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )
    }
}


@Composable
fun WalletLoadingContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            color = Colors.Blue
        )

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = stringResource(id = R.string.the_wallet_is_being_prepared),
            color = Colors.TextPrimary,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = stringResource(id = R.string.balance_and_transactions_are_being_loaded),
            modifier = Modifier.fillMaxWidth(),
            color = Colors.TextSecondary,
            textAlign = TextAlign.Center
        )
    }
}


@Composable
fun WalletErrorContent(
    modifier: Modifier = Modifier,
    message: String,
    onRetry: () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(
            containerColor = Colors.Surface
        ),
        border = BorderStroke(
            width = 1.dp,
            color = Colors.Border
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier.size(72.dp),
                shape = CircleShape,
                color = Colors.NegativeSoft
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier.size(32.dp),
                        imageVector = Icons.Outlined.Refresh,
                        contentDescription = null,
                        tint = Colors.Negative
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = stringResource(id = R.string.something_went_wrong),
                color = Colors.TextPrimary,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = message,
                color = Colors.TextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(22.dp))

            Button(
                onClick = onRetry,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Colors.Blue
                )
            ) {
                Icon(
                    imageVector = Icons.Outlined.Refresh,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(text = stringResource(id = R.string.try_again))
            }
        }
    }
}


@Composable
fun WalletBottomNavigation(
    onWalletClick: () -> Unit,
    onActivityClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    NavigationBar(
        containerColor = Colors.Surface,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = true,
            onClick = onWalletClick,
            icon = {
                Icon(
                    imageVector = Icons.Outlined.AccountBalanceWallet,
                    contentDescription = null
                )
            },
            label = {
                Text(text = stringResource(id = R.string.wallet))
            },
            colors = walletNavigationColors()
        )

        NavigationBarItem(
            selected = false,
            onClick = onActivityClick,
            icon = {
                Icon(
                    imageVector = Icons.Outlined.History,
                    contentDescription = null
                )
            },
            label = {
                Text(text = stringResource(id = R.string.activity))
            },
            colors = walletNavigationColors()
        )

        NavigationBarItem(
            selected = false,
            onClick = onProfileClick,
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = null
                )
            },
            label = {
                Text(text = stringResource(id = R.string.profile))
            },
            colors = walletNavigationColors()
        )
    }
}

@Composable
private fun walletNavigationColors() =
    NavigationBarItemDefaults.colors(
        selectedIconColor = Colors.Blue,
        selectedTextColor = Colors.Blue,
        indicatorColor = Colors.BlueSoft,
        unselectedIconColor = Colors.TextSecondary,
        unselectedTextColor = Colors.TextSecondary
    )

@StringRes
fun WalletScenario.displayNameRes(): Int {
    return when (this) {
        WalletScenario.LOADED -> R.string.wallet_scenario_loaded
        WalletScenario.EMPTY -> R.string.wallet_scenario_empty
        WalletScenario.ERROR -> R.string.wallet_scenario_error
    }
}

@Composable
private fun walletPreviewDashboard(): WalletDashboardUiModel {
    val children = listOf(
        ChildWalletUiModel(
            id = "1",
            name = stringResource(R.string.preview_child_mert_name),
            grade = stringResource(R.string.preview_grade_eighth),
            balanceText = "₺1,250.00"
        ),
        ChildWalletUiModel(
            id = "2",
            name = stringResource(R.string.preview_child_ece_name),
            grade = stringResource(R.string.preview_grade_fifth),
            balanceText = "₺780.50"
        ),
        ChildWalletUiModel(
            id = "3",
            name = stringResource(R.string.preview_child_can_name),
            grade = stringResource(R.string.preview_grade_third),
            balanceText = "₺540.00"
        )
    )

    val transactions = listOf(
        WalletTransactionUiModel(
            id = "1",
            title = stringResource(
                R.string.preview_transaction_wallet_top_up
            ),
            dateText = "Aug 01, 10:30",
            amountText = "+₺2,000.00",
            isIncome = true
        ),
        WalletTransactionUiModel(
            id = "2",
            title = stringResource(
                R.string.preview_transaction_school_cafeteria
            ),
            dateText = "Jul 31, 13:42",
            amountText = "-₺145.50",
            isIncome = false
        ),
        WalletTransactionUiModel(
            id = "3",
            title = stringResource(
                R.string.preview_transaction_bookstore
            ),
            dateText = "Jul 30, 17:15",
            amountText = "-₺320.00",
            isIncome = false
        )
    )

    return WalletDashboardUiModel(
        balanceText = "₺12,480.50",
        currencyCode = "TRY",
        children = children,
        transactions = transactions
    )
}

@Preview(
    name = "Wallet Loaded Screen",
    showBackground = true,
    showSystemUi = true,
    widthDp = 411,
    heightDp = 915
)
@Composable
private fun WalletDashboardLoadedPreview() {
    MaterialTheme {
        WalletDashboardScreenContent(
            uiState = WalletDashboardContract.UiState(
                isLoading = false,
                selectedScenario = WalletScenario.LOADED,
                dashboard = walletPreviewDashboard()
            ),
            onEvent = {}
        )
    }
}

@Preview(
    name = "Wallet Empty Screen",
    showBackground = true,
    showSystemUi = true,
    widthDp = 411,
    heightDp = 915
)
@Composable
private fun WalletDashboardEmptyPreview() {
    MaterialTheme {
        WalletDashboardScreenContent(
            uiState = WalletDashboardContract.UiState(
                isLoading = false,
                selectedScenario = WalletScenario.EMPTY,
                dashboard = WalletDashboardUiModel(
                    balanceText = "₺0.00",
                    currencyCode = "TRY",
                    children = emptyList(),
                    transactions = emptyList()
                ),
                error = null
            ),
            onEvent = {}
        )
    }
}

@Preview(
    name = "Wallet Loading Screen",
    showBackground = true,
    showSystemUi = true,
    widthDp = 411,
    heightDp = 915
)
@Composable
private fun WalletDashboardLoadingPreview() {
    MaterialTheme {
        WalletDashboardScreenContent(
            uiState = WalletDashboardContract.UiState(
                isLoading = true
            ),
            onEvent = {}
        )
    }
}

@Preview(
    name = "Wallet Error Screen",
    showBackground = true,
    showSystemUi = true,
    widthDp = 411,
    heightDp = 915
)
@Composable
private fun WalletDashboardErrorPreview() {
    val errorMessage = stringResource(
        id = R.string.preview_wallet_error
    )

    MaterialTheme {
        WalletDashboardScreenContent(
            uiState = WalletDashboardContract.UiState(
                isLoading = false,
                selectedScenario = WalletScenario.ERROR,
                error = errorMessage
            ),
            onEvent = {}
        )
    }
}