package com.ahmed_apps.dictionary_app.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmed_apps.dictionary_app.domain.repository.DictionaryRepository
import com.ahmed_apps.dictionary_app.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Ahmed Guedmioui
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val dictionaryRepository: DictionaryRepository
) : ViewModel() {

    private val _mainState = MutableStateFlow(MainState())
    val mainState = _mainState.asStateFlow()

    private var searchJob: Job? = null

    init {
        _mainState.update {
            it.copy(searchWord = "Word")
        }

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            loadWordResult()
        }
    }

    /**
     * 处理主界面UI事件的函数。
     *
     * @param mainUiEvent 主界面UI事件，决定具体的行为。
     */
    fun onEvent(mainUiEvent: MainUiEvents) {
        when (mainUiEvent) {
            MainUiEvents.OnSearchClick -> {
                // 取消正在进行的搜索任务，以便重新开始搜索。
                searchJob?.cancel()
                // 启动一个新的协程来加载单词搜索结果。
                searchJob = viewModelScope.launch {
                    loadWordResult()
                }
            }

            is MainUiEvents.OnSearchWordChange -> {
                // 更新主状态中的搜索单词。
                _mainState.update {
                    // 将搜索词转换为小写，以保持一致性。
                    it.copy(
                        searchWord = mainUiEvent.newWord.lowercase()
                    )
                }
            }
        }
    }

    /**
     * 加载单词查询结果
     *
     * 此函数用于在ViewModelScope中启动一个协程，以收集来自字典仓库的单词查询结果
     * 根据查询结果的不同状态，更新_mainState中的数据
     */
    private fun loadWordResult() {
        viewModelScope.launch {
            // 向字典仓库请求单词查询结果
            dictionaryRepository.getWordResult(
                mainState.value.searchWord
            ).collect { result ->
                // 根据查询结果的不同状态进行处理
                when (result) {
                    is Result.Error -> Unit // 如果查询结果为错误，不进行任何操作

                    is Result.Loading -> {
                        // 如果查询结果为加载中状态，更新_mainState中的isLoading状态
                        _mainState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }

                    is Result.Success -> {
                        // 如果查询结果为成功，更新_mainState中的wordItem数据
                        result.data?.let { wordItem ->
                            _mainState.update {
                                it.copy(
                                    wordItem = wordItem
                                )
                            }
                        }
                    }
                }
            }
        }
    }


}





















