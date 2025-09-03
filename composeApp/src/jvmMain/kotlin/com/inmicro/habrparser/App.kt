package com.inmicro.habrparser

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.Checkbox
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.openFileSaver
import io.github.vinceglb.filekit.writeString
import kotlinx.coroutines.delay
import org.jetbrains.compose.ui.tooling.preview.Preview

import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {
    val uriHandler = LocalUriHandler.current

    MaterialTheme {
        val scope = rememberCoroutineScope()

        var showContent by remember { mutableStateOf(false) }
        var searchValue by remember { mutableStateOf("") }
        var orderBy by remember { mutableStateOf("date") }

        var requestValue = listOf<LocalRssItem>()
        var saveAsString by remember { mutableStateOf(StringBuilder()) }

        var dropdownExpanded by remember { mutableStateOf(false) }

        var checkboxLink by remember { mutableStateOf(true) }
        var checkboxDate by remember { mutableStateOf(true) }
        var checkboxDescription by remember { mutableStateOf(true) }

        LaunchedEffect(showContent) {
            scope.launch {
                try {
                    requestValue = RequestLogic().rssParseRequest(searchValue, orderBy)
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
                modifier = Modifier.fillMaxWidth(0.9f).height(80.dp).padding(all = 8.dp)
            ) {
                OutlinedTextField(
                    value = searchValue,
                    textStyle = MaterialTheme.typography.bodyMedium,
                    onValueChange = {
                        searchValue = it
                    },
                    singleLine = true,
                    maxLines = 1,
                    modifier = Modifier.weight(4f).fillMaxHeight(),
                    shape = RoundedCornerShape(8.dp)
                )
                Spacer(Modifier.width(20.dp))
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                ) {
                    Button(
                        onClick = { dropdownExpanded = true },
                        modifier = Modifier.fillMaxSize(),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(4.dp)
                    ) {
                        Text("Сортировка\nпо " + if (orderBy == "date") "дате" else if (orderBy == "relevance") "релевантности" else "рейтингу")
                    }
                    DropdownMenu(
                        expanded = dropdownExpanded,
                        onDismissRequest = { dropdownExpanded = false }
                    ) {
                        DropdownMenuItem(onClick = {
                            orderBy = "date"
                            dropdownExpanded = !dropdownExpanded
                        }) {
                            Text("По дате")
                        }
                        DropdownMenuItem(onClick = {
                            orderBy = "relevance"
                            dropdownExpanded = !dropdownExpanded
                        }) {
                            Text("По релевантности")
                        }
                        DropdownMenuItem(onClick = {
                            orderBy = "rating"
                            dropdownExpanded = !dropdownExpanded
                        }) {
                            Text("По рейтингу")
                        }
                    }
                }
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
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(4.dp)
                ) {
                    Text("Поиск")
                }
                Spacer(Modifier.width(20.dp))
                Button(
                    onClick = {
                        scope.launch {
                            val file = FileKit.openFileSaver(searchValue, "txt")
                            file?.writeString(saveAsString.toString())
                        }
                    },
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(4.dp)
                ) {
                    Text("Сохранить")
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(0.9f).height(80.dp).padding(all = 8.dp)
            ) {
                Spacer(Modifier.weight(4f))
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Checkbox(checked = checkboxLink, onCheckedChange = {
                        checkboxLink = !checkboxLink
                    })
                    Text("Ссылка")
                }
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Checkbox(checked = checkboxDate, onCheckedChange = {
                        checkboxDate = !checkboxDate
                    })
                    Text("Дата")
                }
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Checkbox(checked = checkboxDescription, onCheckedChange = {
                        checkboxDescription = !checkboxDescription
                    })
                    Text("Краткое описание")
                }
            }
            Spacer(Modifier.height(20.dp))
            AnimatedVisibility(showContent) {
                Column(
                    modifier = Modifier.fillMaxWidth(0.9f).verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text("Последние статьи с хабра по теме \"$searchValue\": ", style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(20.dp))
                    saveAsString = StringBuilder()
                    requestValue.forEachIndexed { index,item ->
                        Text("#${index+1}: " + item.title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                        if (checkboxLink) {
                            Text(
                                item.link,
                                style = MaterialTheme.typography.bodyMedium,
                                fontStyle = FontStyle.Italic,
                                modifier = Modifier.clickable {
                                    uriHandler.openUri(item.link)
                                }
                            )
                        }
                        if (checkboxDate) {
                            Text(item.pubDate, style = MaterialTheme.typography.bodyMedium)
                        }
                        if (checkboxDescription) {
                            Text(item.description, style = MaterialTheme.typography.bodyMedium)
                        }
                        Spacer(Modifier.height(8.dp))
                        saveAsString.append("#${index+1}: " + item.title + "\n")
                        if (checkboxLink) {
                            saveAsString.append(item.link + "\n")
                        }
                        if (checkboxDate) {
                            saveAsString.append(item.pubDate + "\n")
                        }
                        if (checkboxDescription) {
                            saveAsString.append(item.description)
                        }
                    }
                }
            }
        }
    }
}