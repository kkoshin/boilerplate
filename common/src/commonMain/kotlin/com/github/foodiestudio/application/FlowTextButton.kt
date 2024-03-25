package com.github.foodiestudio.application

import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

/**
 * 使用 Flow 来描述 onXX 事件，看上去也更符合单向数据流的传递关系。
 *
 * 带来的影响是将一些复杂度挪到了 State 里，外部使用的时候，可以只对感兴趣的 Flow 进行订阅，而且可以这个 Flow 对象本身可以再次传递，
 * 以及使用 Flow 的一些操作符来简化代码。
 *
 */
@Composable
fun FlowTextButton(state: FlowButtonState = rememberFlowButtonState("")) {
    TextButton(onClick = {
        state.dispatchClickEvent()
    }) {
        Text(state.text)
    }
}

@Composable
fun rememberFlowButtonState(text: String): FlowButtonState = remember {
    FlowButtonStateImpl(text)
}

interface FlowButtonState {
    var text: String

    val clicks: Flow<Unit>

    val selects: Flow<String?>

    fun dispatchClickEvent()

    fun dispatchSelectEvent(id: String?)
}

class FlowButtonStateImpl(
    text: String,
) : FlowButtonState {
    override var text: String by mutableStateOf(text)

    // 看实际场景需要，来调整 extraBufferCapacity 和 onBufferOverflow
    private val _clicked =
        MutableSharedFlow<Unit>(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_LATEST)

    override val clicks: Flow<Unit> = _clicked

    private val _selected =
        MutableSharedFlow<String?>(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override val selects: Flow<String?> = _selected

    override fun dispatchClickEvent() {
        _clicked.tryEmit(Unit)
    }

    override fun dispatchSelectEvent(id: String?) {
        _selected.tryEmit(id)
    }
}