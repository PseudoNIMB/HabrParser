package com.inmicro.habrparser

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.jetbrains.compose.ui.tooling.preview.Preview

import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {
    MaterialTheme {
        val scope = rememberCoroutineScope()
        var showContent by remember { mutableStateOf(false) }
        var searchValue by remember { mutableStateOf("") }
        var requestValue = listOf<LocalRssItem>()

        LaunchedEffect(showContent) {
            scope.launch {
                try {
                    requestValue = RequestLogic().rssParseRequest(searchValue)
                } catch (e: Exception) {
                    e.localizedMessage ?: "error"
                }
            }
        }
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(0.6f).height(80.dp).padding(all = 8.dp)
            ) {
                OutlinedTextField(
                    value = searchValue,
                    textStyle = MaterialTheme.typography.bodyMedium,
                    onValueChange = {
                        searchValue = it
                    },
                    singleLine = true,
                    maxLines = 1,
                    modifier = Modifier.weight(3f).fillMaxHeight(),
                    shape = RoundedCornerShape(8.dp)
                )
                Spacer(Modifier.width(20.dp))
                Button(
                    onClick = {
                        if (searchValue.isNotEmpty()) {
                            if (showContent) {
                                //TODO Здесь обновлять поисковый запрос
                                scope.launch {
                                    showContent = false
                                    delay(1500)
                                    showContent = true
                                }
                            } else {
                                showContent = true
                            }
                        }
                    },
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Поиск")
                }
            }
            AnimatedVisibility(showContent) {
                Column(
                    modifier = Modifier.fillMaxWidth(0.9f).verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text("Последние статьи с хабра по теме \"$searchValue\": ", style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(20.dp))
                    requestValue.forEachIndexed { index,item ->
                        Text("#${index+1}: " + item.title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                        Text(item.link, style = MaterialTheme.typography.bodyMedium, fontStyle = FontStyle.Italic)
                        Text(item.pubDate, style = MaterialTheme.typography.bodyMedium)
                        Text(item.description, style = MaterialTheme.typography.bodyMedium)
                        Spacer(Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}