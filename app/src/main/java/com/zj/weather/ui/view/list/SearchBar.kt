package com.zj.weather.ui.view.list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zj.weather.R

@Composable
fun SearchBar(searchCity: (String) -> Unit) {
    var value by remember { mutableStateOf("") }
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(Modifier.height(30.dp))
        Text(
            text = stringResource(id = R.string.city_list_title),
            fontSize = 30.sp,
            modifier = Modifier.padding(10.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(43.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Box(
                modifier = Modifier
                    .weight(5f)
                    .fillMaxWidth()
                    .padding(5.dp)
                    .border(
                        border = BorderStroke(1.dp, Color.Gray),
                        shape = MaterialTheme.shapes.medium
                    )
                    .height(43.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (value.isEmpty()) {
                    Text(
                        text = "请输入城市名称",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(5.dp)
                    )
                } else {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            imageVector = Icons.Rounded.Close, contentDescription = "",
                            modifier = Modifier
                                .clickable {
                                    value = ""
                                }
                                .fillMaxHeight()
                                .padding(end = 5.dp)
                        )
                    }
                }
                BasicTextField(
                    value = value,
                    onValueChange = { value = it },
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Characters,
                        keyboardType = KeyboardType.Email,
                        autoCorrect = true,
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(onSearch = {
                        searchCity(value)
                    }),
                    singleLine = true,
                    textStyle = TextStyle(color = MaterialTheme.colors.primary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 5.dp, end = 45.dp)
                )
            }
            IconButton(
                modifier = Modifier.wrapContentWidth(Alignment.End),
                onClick = {
                    searchCity(value)
                }
            ) {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = "查询"
                )
            }
        }
    }
}


@Preview(name = "搜索头", widthDp = 360, heightDp = 70, showBackground = true)
@Composable
fun SearchBarPreview() {
    SearchBar {}
}