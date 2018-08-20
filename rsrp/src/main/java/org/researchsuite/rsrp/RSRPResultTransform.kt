package org.researchsuite.rsrp

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import org.researchsuite.researchsuiteextensions.common.asJsonObjectOrNull
import org.researchsuite.researchsuiteextensions.common.getStringOrNull
import java.lang.reflect.Type


open public class RSRPResultTransform(
        val transform: String,
        val inputMapping: List<RSRPResultTransformInputMapping>) {

    open public class Deserializer : JsonDeserializer<RSRPResultTransform> {
        override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): RSRPResultTransform {

            val resultTransform: RSRPResultTransform? = json.asJsonObjectOrNull?.let {

                val transform = it.getStringOrNull("transform")
                val inputMapping: List<RSRPResultTransformInputMapping>? = { memberName: String ->

                    if (it.has(memberName) && it.get(memberName).isJsonArray) {

                        it.get(memberName).asJsonArray.map { context.deserialize<RSRPResultTransformInputMapping>(it, RSRPResultTransformInputMapping::class.java) }
                    }
                    else null

                }("inputMapping")
                if (transform != null && inputMapping != null) RSRPResultTransform(transform, inputMapping)
                else null

            }

            return resultTransform ?: { throw JsonParseException("Cannot decode RSRPResultTransform") }()

        }

    }

}