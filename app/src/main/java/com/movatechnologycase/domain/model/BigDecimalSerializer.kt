package com.movatechnologycase.domain.model

import java.math.BigDecimal
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive

object BigDecimalSerializer : KSerializer<BigDecimal> {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor(
            serialName = "BigDecimal",
            kind = PrimitiveKind.STRING
        )

    override fun deserialize(
        decoder: Decoder
    ): BigDecimal {
        val value = if (decoder is JsonDecoder) {
            decoder.decodeJsonElement().jsonPrimitive.content
        } else {
            decoder.decodeString()
        }

        return value.toBigDecimal()
    }

    override fun serialize(
        encoder: Encoder,
        value: BigDecimal
    ) {
        if (encoder is JsonEncoder) {
            encoder.encodeJsonElement(
                JsonPrimitive(value)
            )
        } else {
            encoder.encodeString(value.toPlainString())
        }
    }
}