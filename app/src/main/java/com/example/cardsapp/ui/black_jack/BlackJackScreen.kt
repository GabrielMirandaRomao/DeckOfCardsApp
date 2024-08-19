package com.example.cardsapp.ui.black_jack

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import coil.compose.AsyncImage
import com.example.cardsapp.R
import com.example.cardsapp.configuration.SharedPreferencesManager
import com.example.cardsapp.configuration.SharedPreferencesManager.Companion.BLACK_JACK_DECK_ID
import com.example.cardsapp.ui.black_jack.BlackJackViewModel.Companion.DEALER_WIN
import com.example.cardsapp.ui.black_jack.BlackJackViewModel.Companion.PLAYER_WIN
import com.example.cardsapp.utils.Resource
import org.koin.androidx.viewmodel.ext.android.viewModel

class ComposeBlackJackFragment : Fragment() {

    private val viewModel: BlackJackViewModel by viewModel()
    private lateinit var sharedPreferencesManager: SharedPreferencesManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragment = this
        sharedPreferencesManager = SharedPreferencesManager(requireContext())
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                BlackJackScreen(
                    sharedPreferencesManager = sharedPreferencesManager,
                    viewModel = viewModel,
                    navController = findNavController(fragment)
                )
            }
        }
    }
}

@Composable
fun BlackJackScreen(
    sharedPreferencesManager: SharedPreferencesManager?,
    viewModel: BlackJackViewModel?,
    navController: NavController?
) {

    var isGameStarted by remember {
        mutableStateOf(false)
    }

    var isProgression by remember {
        mutableStateOf(false)
    }

    var isShowingAlert by remember {
        mutableStateOf(true)
    }

    val deckState = viewModel?.deck?.observeAsState()

    val alert = viewModel?.alert?.observeAsState()

    MaterialTheme {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
        ) {
            val (header, tableRow, playerRow, winnerScore, table, player, footer) = createRefs()

            deckState?.value.let { response ->
                when (response) {
                    is Resource.Success -> {
                        isProgression = false
                        sharedPreferencesManager?.saveDeckId(BLACK_JACK_DECK_ID, response.data.id)
                    }

                    is Resource.Fail -> {
                        isProgression = false
                        isGameStarted = false
                        if(isShowingAlert) {
                            AlertDialogExample(
                                onDismissRequest = { isShowingAlert = false },
                                onConfirmation = { viewModel?.getShuffleDeck() },
                                dialogTitle = "Alert",
                                dialogText = response.message,
                                icon = Icons.Default.Info
                            )
                        } else {

                        }
                    }

                    Resource.Processing -> {
                        isProgression = true
                    }

                    else -> {}
                }
            }

            alert?.value.let { alert ->
                if(isShowingAlert) {
                    AlertDialogExample(
                        onDismissRequest = { isShowingAlert = false },
                        onConfirmation = { viewModel?.getShuffleDeck() },
                        dialogTitle = "Alert",
                        dialogText = alert.orEmpty(),
                        icon = Icons.Default.Info
                    )
                } else {

                }
            }

            Row(modifier = Modifier
                .constrainAs(header) {
                    top.linkTo(parent.top)
                }
                .padding(top = 14.dp, start = 12.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = "Get back home",
                    modifier = Modifier
                        .clickable {
                            navController?.popBackStack()
                        }
                        .padding(12.dp)
                )

                Spacer(modifier = Modifier.width(32.dp))

                if (deckState?.value != null && isGameStarted) {
                    Text(
                        modifier = Modifier.padding(top = 12.dp), text = "Id: ${
                            sharedPreferencesManager?.getDeckId(
                                BLACK_JACK_DECK_ID
                            )
                        }"
                    )
                } else {
                    Text(modifier = Modifier.padding(top = 12.dp), text = "Id: ")
                }
            }

            if (viewModel != null) {
                Text(
                    modifier = Modifier
                        .constrainAs(player) {
                            top.linkTo(tableRow.bottom)
                        }
                        .padding(vertical = 22.dp, horizontal = 22.dp),
                    fontSize = 18.sp,
                    text = "Count : ${viewModel.dealerCards.value.sumOf { it.value }}"
                )
            }

            LazyRow(modifier = Modifier
                .constrainAs(tableRow) {
                    top.linkTo(header.bottom)
                }
                .padding(12.dp)
                .fillMaxWidth()
            ) {
                if (viewModel != null) {
                    items(items = viewModel.dealerCards.value) { card ->
                        AsyncImage(
                            model = card.png,
                            contentDescription = null
                        )

                        Spacer(modifier = Modifier.width(16.dp))
                    }
                }
            }

            if (viewModel != null) {
                val winnerText = when {
                    viewModel.playerCards.value.sumOf { it.value } == 21 -> PLAYER_WIN
                    viewModel.dealerCards.value.sumOf { it.value } == 21 -> DEALER_WIN
                    viewModel.playerCards.value.sumOf { it.value } > 21 -> DEALER_WIN
                    viewModel.dealerCards.value.sumOf { it.value } > 21 -> PLAYER_WIN
                    else -> null
                }

                winnerText?.let {
                    Text(
                        modifier = Modifier
                            .constrainAs(winnerScore) {
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                            .padding(start = 12.dp),
                        fontWeight = FontWeight(600),
                        fontSize = 32.sp,
                        text = it
                    )
                }
            }

            if (viewModel != null) {
                Text(
                    modifier = Modifier
                        .constrainAs(table) {
                            bottom.linkTo(playerRow.top)
                        }
                        .padding(vertical = 22.dp, horizontal = 22.dp),
                    fontSize = 18.sp,
                    text = "Player count : ${viewModel.playerCards.value.sumOf { it.value }}"
                )
            }

            LazyRow(modifier = Modifier
                .constrainAs(playerRow) {
                    bottom.linkTo(footer.top)
                }
                .padding(12.dp)
                .fillMaxWidth()
            ) {
                if (viewModel != null) {
                    items(items = viewModel.playerCards.value) { card ->
                        AsyncImage(
                            model = card.png,
                            contentDescription = null
                        )

                        Spacer(modifier = Modifier.width(16.dp))
                    }
                }
            }

            Row(
                modifier = Modifier
                    .constrainAs(footer) {
                        bottom.linkTo(parent.bottom)
                    }
                    .height(64.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!isGameStarted) {
                    Button(
                        modifier = Modifier
                            .width(124.dp)
                            .height(34.dp),
                        onClick = {
                            isGameStarted = !isGameStarted
                            sharedPreferencesManager?.remove(BLACK_JACK_DECK_ID)
                            viewModel?.getShuffleDeck()
                        }
                    ) {
                        Text("Start game")
                    }
                }

                if (isGameStarted) {
                    Button(
                        modifier = Modifier
                            .width(124.dp)
                            .height(34.dp),
                        onClick = {
                            viewModel?.getOneMoreCard(
                                sharedPreferencesManager?.getDeckId(
                                    BLACK_JACK_DECK_ID
                                ).orEmpty()
                            )
                        }
                    ) {
                        Text("One more")
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Button(
                        modifier = Modifier
                            .width(124.dp)
                            .height(34.dp),
                        onClick = {
                            isGameStarted = false
                            viewModel?.restartGame()
                        }
                    ) {
                        Text("Restart")
                    }
                }
            }
        }
    }
}

@Composable
fun AlertDialogExample(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "Example Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Try again")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}

@Preview
@Composable
private fun AlertDialogExamplePreview() {
    AlertDialogExample(
        {},
        {},
        "Alert dialog",
        "This is an example of an alert dialog with buttons.",
        icon = Icons.Default.Info
    )
}


@Preview(showBackground = true)
@Composable
fun BlackJackScreenPreview() {
    BlackJackScreen(
        sharedPreferencesManager = null,
        viewModel = null,
        navController = null
    )
}