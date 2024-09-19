package com.ahmed_apps.dictionary_app.domain.repository

import com.ahmed_apps.dictionary_app.domain.model.WordItem
import com.ahmed_apps.dictionary_app.util.Result
import kotlinx.coroutines.flow.Flow

/**
 * 字典应用程序的领域层接口，定义了与字典数据交互的操作.
 *
 * @author Ahmed Guedmioui
 */
interface DictionaryRepository {
    /**
     * 悬挂函数，用于获取单词的查询结果.
     *
     * @param word 要查询的单词.
     * @return 返回一个 Flow，它发出查询结果的封装类型 Result<WordItem>，包括成功、错误和加载状态.
     */
    suspend fun getWordResult(
        word: String
    ): Flow<Result<WordItem>>
}
