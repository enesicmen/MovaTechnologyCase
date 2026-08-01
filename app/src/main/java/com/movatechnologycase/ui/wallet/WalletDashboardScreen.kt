package com.movatechnologycase.ui.wallet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.movatechnologycase.core.design.Colors
import com.movatechnologycase.data.repository.WalletScenario
import com.movatechnologycase.R

@Composable
fun WalletDashboardScreen(
    viewModel: WalletDashboardViewModel,
    onTopUpClick: () -> Unit = {},
    onChildClick: (String) -> Unit = {},
    onActivityTabClick: () -> Unit = {},
    onProfileTabClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    WalletDashboardScreenContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onTopUpClick = onTopUpClick,
        onChildClick = onChildClick,
        onActivityTabClick = onActivityTabClick,
        onProfileTabClick = onProfileTabClick
    )
}

@Composable
fun WalletDashboardScreenContent(
    uiState: WalletDashboardContract.UiState,
    onEvent: (WalletDashboardContract.WalletEvent) -> Unit,
    onTopUpClick: () -> Unit = {},
    onChildClick: (String) -> Unit = {},
    onActivityTabClick: () -> Unit = {},
    onProfileTabClick: () -> Unit = {}
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Colors.Background,
        bottomBar = {
            WalletBottomNavigation(
                onWalletClick = {},
                onActivityClick = onActivityTabClick,
                onProfileClick = onProfileTabClick
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Colors.Background)
        ) {
            when {
                uiState.isLoading -> {
                    WalletStateContent(
                        selectedScenario = uiState.selectedScenario,
                        onScenarioSelected = { scenario -> onEvent(WalletDashboardContract.WalletEvent.OnScenarioSelected(scenario)) }
                    ) {
                        WalletLoadingContent()
                    }
                }

                uiState.error != null -> {
                    WalletStateContent(
                        selectedScenario = uiState.selectedScenario,
                        onScenarioSelected = { scenario -> onEvent(WalletDashboardContract.WalletEvent.OnScenarioSelected(scenario)) }
                    ) {
                        WalletErrorContent(
                            message = uiState.error,
                            onRetry = { onEvent(WalletDashboardContract.WalletEvent.OnRetryClick) }
                        )
                    }
                }

                uiState.dashboard != null -> {
                    WalletLoadedContent(
                        dashboard = uiState.dashboard,
                        selectedScenario = uiState.selectedScenario,
                        isBalanceVisible = uiState.isBalanceVisible,
                        onScenarioSelected = { scenario -> onEvent(WalletDashboardContract.WalletEvent.OnScenarioSelected(scenario)) },
                        onBalanceVisibilityClick = { onEvent(WalletDashboardContract.WalletEvent.OnBalanceVisibilityClick) },
                        onTopUpClick = onTopUpClick,
                        onChildClick = onChildClick
                    )
                }

                else -> {
                    WalletStateContent(
                        selectedScenario = uiState.selectedScenario,
                        onScenarioSelected = { scenario -> onEvent(WalletDashboardContract.WalletEvent.OnScenarioSelected(scenario)) }
                    ) {
                        WalletErrorContent(
                            message = stringResource(id = R.string.wallet_information_could_not_be_found),
                            onRetry = { onEvent(WalletDashboardContract.WalletEvent.OnRetryClick) }
                        )
                    }
                }
            }
        }
    }
}

@Preview(
    name = "Wallet Dashboard Screen",
    showBackground = true,
    showSystemUi = true,
    widthDp = 411,
    heightDp = 915
)
@Composable
private fun WalletDashboardScreenPreview() {
    MaterialTheme {
        WalletDashboardScreenContent(
            uiState = WalletDashboardContract.UiState(
                isLoading = false,
                selectedScenario = WalletScenario.LOADED,
                dashboard = WalletDashboardUiModel(
                    balanceText = stringResource(
                        id = R.string.preview_total_balance
                    ),
                    currencyCode = stringResource(
                        id = R.string.preview_currency_code
                    ),
                    children = listOf(
                        ChildWalletUiModel(
                            id = "1",
                            name = stringResource(
                                id = R.string.preview_child_mert_name
                            ),
                            grade = stringResource(
                                id = R.string.preview_child_mert_grade
                            ),
                            balanceText = stringResource(
                                id = R.string.preview_child_mert_balance
                            )
                        ),
                        ChildWalletUiModel(
                            id = "2",
                            name = stringResource(
                                id = R.string.preview_child_ece_name
                            ),
                            grade = stringResource(
                                id = R.string.preview_child_ece_grade
                            ),
                            balanceText = stringResource(
                                id = R.string.preview_child_ece_balance
                            )
                        )
                    ),
                    transactions = listOf(
                        WalletTransactionUiModel(
                            id = "1",
                            title = stringResource(
                                id = R.string.preview_transaction_wallet_top_up
                            ),
                            dateText = stringResource(
                                id = R.string.preview_transaction_wallet_top_up_date
                            ),
                            amountText = stringResource(
                                id = R.string.preview_transaction_wallet_top_up_amount
                            ),
                            isIncome = true
                        ),
                        WalletTransactionUiModel(
                            id = "2",
                            title = stringResource(
                                id = R.string.preview_transaction_school_cafeteria
                            ),
                            dateText = stringResource(
                                id = R.string.preview_transaction_school_cafeteria_date
                            ),
                            amountText = stringResource(
                                id = R.string.preview_transaction_school_cafeteria_amount
                            ),
                            isIncome = false
                        )
                    )
                ),
                error = null
            ),
            onEvent = {},
            onTopUpClick = {},
            onChildClick = {},
            onActivityTabClick = {},
            onProfileTabClick = {}
        )
    }
}