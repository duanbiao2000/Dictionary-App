package com.ahmed_apps.dictionary_app.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ahmed_apps.dictionary_app.R
import com.ahmed_apps.dictionary_app.domain.model.Meaning
import com.ahmed_apps.dictionary_app.domain.model.WordItem
import com.ahmed_apps.dictionary_app.ui.theme.DictionaryAppTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    /**
     * 当活动被创建时调用
     * @param savedInstanceState 之前实例化的保存状态
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 设置内容描述
        setContent {
            // 设置应用程序主题
            DictionaryAppTheme {
                // 设置底部导航栏颜色
                BarColor()

                // 使用Hilt注入主视图模型
                val mainViewModel = hiltViewModel<MainViewModel>()
                // 收集并作为状态观察主视图模型的状态
                val mainState by mainViewModel.mainState.collectAsState()

                // 构建 Scaffold UI 结构
                Scaffold(
                    modifier = Modifier.fillMaxSize(), // 填充最大尺寸
                ) { paddingValues ->
                    // 填充最大尺寸的 Box，用于包含主要内容
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = paddingValues.calculateTopPadding()) // 考虑到顶部 padding
                    ) {
                        // 显示主屏幕 UI，传递主状态
                        MainScreen(mainState)
                    }
                }

                // 定义 Scaffold 的顶部栏
                topBar = {
                    // 搜索字段
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth() // 横向填充
                            .padding(horizontal = 16.dp), // 横向内边距
                        value = mainState.searchWord, // 当前搜索词
                        onValueChange = {
                            // 搜索词变化时更新视图模型状态
                            mainViewModel.onEvent(
                                MainUiEvents.OnSearchWordChange(it)
                            )
                        },
                        trailingIcon = {
                            // 搜索图标，可点击
                            Icon(
                                imageVector = Icons.Rounded.Search,
                                contentDescription = getString(R.string.search_a_word),
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .size(30.dp) // 图标尺寸
                                    .clickable { // 点击时触发搜索事件
                                        mainViewModel.onEvent(
                                            MainUiEvents.OnSearchClick
                                        )
                                    }
                            )
                        },
                        label = {
                            // 字段标签
                            Text(
                                text = getString(R.string.search_a_word),
                                fontSize = 15.sp,
                                modifier = Modifier.alpha(0.7f) // 透明度
                            )
                        },
                        textStyle = TextStyle( // 文本样式
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 19.5.sp
                        )
                    )
                }
            }
        }
    }


    /**
     * Composable function for the main screen.
     *
     * @param mainState The state of the main screen, containing the data and status needed for display.
     */
    @Composable
    fun MainScreen(
        mainState: MainState
    ) {

        // Use Box to create a container that fills the entire screen
        Box(modifier = Modifier.fillMaxSize()) {

            // Create a column at the top of the screen to display word information
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 30.dp)
            ) {
                // If the word information exists, display it
                mainState.wordItem?.let { wordItem ->

                    Spacer(modifier = Modifier.height(20.dp))

                    // Display the word text
                    Text(
                        text = wordItem.word,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Display the phonetic symbol of the word
                    Text(
                        text = wordItem.phonetic,
                        fontSize = 17.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }

            // Create a Box occupying the lower part of the screen, with a rounded top edge
            Box(
                modifier = Modifier
                    .padding(top = 110.dp)
                    .fillMaxSize()
                    .clip(
                        RoundedCornerShape(
                            topStart = 50.dp,
                            topEnd = 50.dp
                        )
                    )
                    .background(
                        MaterialTheme.colorScheme.secondaryContainer.copy(0.7f)
                    )
            ) {
                // If the data is being loaded, display a progress circle
                if (mainState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(80.dp)
                            .align(Alignment.Center),
                        color = MaterialTheme.colorScheme.primary
                    )
                } else {
                    // If the data has been loaded, display the word detailed information
                    mainState.wordItem?.let { wordItem ->
                        WordResult(wordItem)
                    }
                }
            }
        }
    }


       /**
        * A Composable function that displays the meanings of a word.
        *
        * @param wordItem The WordItem object containing the word and its meanings.
    */
       @Composable
       fun WordResult(wordItem: WordItem) {
           // Create a vertically scrollable column with overall content padding.
           LazyColumn(
               contentPadding = PaddingValues(vertical = 32.dp)
           ) {
               // Iterate through each meaning of the word, using the index as the unique key for each item.
               items(wordItem.meanings.size) { index ->
                   // Display a single meaning component.
                   Meaning(
                       meaning = wordItem.meanings[index],
                       index = index
                   )
                   // Add vertical space as separation between meanings.
                   Spacer(modifier = Modifier.height(32.dp))
               }
           }
       }

    @Composable
    fun Meaning(
        meaning: Meaning,
        index: Int
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {

            Text(
                text = "${index + 1}. ${meaning.partOfSpeech}",
                fontSize = 17.sp,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        brush = Brush.horizontalGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.primary.copy(0.4f),
                                Color.Transparent
                            )
                        )
                    )
                    .padding(
                        top = 2.dp, bottom = 4.dp,
                        start = 12.dp, end = 12.dp
                    )
            )

            if (meaning.definition.definition.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                ) {

                    Text(
                        text = stringResource(R.string.definition),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 19.sp,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = meaning.definition.definition,
                        fontSize = 17.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                }
            }

            if (meaning.definition.example.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                ) {

                    Text(
                        text = stringResource(R.string.definition),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 19.sp,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = meaning.definition.example,
                        fontSize = 17.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                }
            }

        }

    }

    /**
     * 使用Composable风格设置系统栏（状态栏和导航栏）的背景颜色。
     * 此函数通过获取系统UI控制器并应用当前材料主题的背景颜色来实现。
     */
    @Composable
    private fun BarColor() {
        // 记住系统UI控制器实例，以便后续使用。
        val systemUiController = rememberSystemUiController()
        // 获取当前材料主题的背景颜色，作为系统栏的目标颜色。
        val color = MaterialTheme.colorScheme.background
        // 当目标颜色改变时，启动一个新效应，使用系统UI控制器设置系统栏颜色。
        LaunchedEffect(color) {
            systemUiController.setSystemBarsColor(color)
        }
    }


}

























