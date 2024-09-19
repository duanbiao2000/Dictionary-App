package com.ahmed_apps.dictionary_app.util

/**
 * 用于表示操作结果的密封类。
 * 
 * 这个类被设计用来处理异步操作的结果，比如网络请求或数据库操作。
 * 它有三种不同的结果类型：Success（成功）、Error（错误）和Loading（加载中）。
 * 
 * @param T 操作涉及的数据类型。
 * @param data 操作成功时返回的数据。
 * @param message 操作失败时的错误信息。
 */
sealed class Result<T>(
    val data: T? = null,
    val message: String? = null
) {
    /**
     * 表示操作成功的结果。
     * 
     * @param data 操作成功时返回的数据。
     */
    class Success<T>(data: T?) : Result<T>(data)

    /**
     * 表示操作失败的结果。
     * 
     * @param message 操作失败时的错误信息。
     */
    class Error<T>(message: String) : Result<T>(null, message)

    /**
     * 表示操作正在进行中的结果。
     * 
     * 这个类通常用于显示加载中的UI状态。
     * 
     * @param isLoading 加载状态，默认为true，表示正在加载中。
     */
    class Loading<T>(val isLoading: Boolean = true) : Result<T>(null)
}
