package com.shared.compose_foundation.ktor.socket

import com.shared.compose_foundation.ktor.logger.asTagAndLog
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

enum class EventType {
    OFF,
    ON,
    TRANSMISSION
}

interface SocketMessage {
    fun toFrame(): Frame
}

val serializer = Json {
    ignoreUnknownKeys = true
}

@Serializable
data class EmissionPayload<T>(
    @SerialName("event")
    val event: String,
    @SerialName("data")
    val data: T
)

interface EventPayLoad {
    @SerialName(value = "event")
    val event: String

    @SerialName(value = "eventType")
    val eventType: EventType
}

interface EventPayloadWithData<T> : EventPayLoad {
    val data: T
}


@Serializable
private data class OnOffEventPayload constructor(
    override val event: String,
    override val eventType: EventType,
) : SocketMessage, EventPayLoad {

    override fun toFrame(): Frame {
        return Frame.Text(serializer.encodeToString(this))
    }
}

@Serializable
data class Event(
    val event: String
)

interface WebSocketUtil {

    fun getIncoming(): Flow<Frame>

    suspend fun WebSocketSession.on(event: String): Flow<JsonObject> {
        send(OnOffEventPayload(event, EventType.ON).toFrame())
        return getIncoming()
            .onEach {
                // lgo everything
                if(it is Frame.Text){
                  println(it.readText())
                } else println(it.toString())
            }
            .onEach {
                //log with event name
                if (it is Frame.Text) {
                    event asTagAndLog it.readText()
                }
            }
            .filterIsInstance<Frame.Text>()
            .filter {
                try {
                    event asTagAndLog "isMatch ${serializer.decodeFromString<Event>(it.readText()).event == event}"
                    serializer.decodeFromString<Event>(it.readText()).event == event
                } catch (e: Exception) {
                    event asTagAndLog e
                    false
                }
            }.mapNotNull { frame ->
                try {
                    serializer.decodeFromString<JsonObject>(frame.readText())
                } catch (e: Exception) {
                    event asTagAndLog e
                    null
                }
            }

    }

    fun errorInActiveSocket(): Nothing = error("Socket session is not active")
}