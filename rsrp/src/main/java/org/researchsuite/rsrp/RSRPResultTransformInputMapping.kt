package org.researchsuite.rsrp

import com.google.gson.*
import org.researchsuite.researchsuiteextensions.common.asJsonObjectOrNull
import org.researchsuite.researchsuiteextensions.common.getJsonObjectOrNull
import org.researchsuite.researchsuiteextensions.common.getStringOrNull
import java.lang.reflect.Type
import java.util.*


public sealed class RSRPResultTransformInputMappingType {

    data class StepIdentifier(val stepIdentifier: String): RSRPResultTransformInputMappingType()
    data class StepIdentifierRegex(val stepIdentifierRegex: String): RSRPResultTransformInputMappingType()
    data class Constant(val value: Any): RSRPResultTransformInputMappingType()

    companion object {
        fun fromJson(jsonObject: JsonObject, context: JsonDeserializationContext): RSRPResultTransformInputMappingType? {

            return jsonObject.getStringOrNull("stepIdentifier")?.let { StepIdentifier(it) } ?:
            jsonObject.getStringOrNull("stepIdentifierRegex")?.let { StepIdentifierRegex(it) } ?:
            {
                if (jsonObject.has("constant")) {
                    val value = context.deserialize<Any>(jsonObject.get("constant"), Any::class.java)
                    Constant(value)
                }
                else null
            }()

        }
    }

}

public data class RSRPResultTransformInputMapping(
        val parameter: String,
        val mappingType: RSRPResultTransformInputMappingType) {

    public class Deserializer : JsonDeserializer<RSRPResultTransformInputMapping> {
        override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): RSRPResultTransformInputMapping {

            val inputMapping: RSRPResultTransformInputMapping? = json.asJsonObjectOrNull?.let {

                val parameter = it.getStringOrNull("parameter")
                val mappingType = RSRPResultTransformInputMappingType.fromJson(it, context)
                if (parameter != null && mappingType != null) RSRPResultTransformInputMapping(parameter, mappingType)
                else null

            }

            return inputMapping ?: { throw JsonParseException("Cannot decode RSRPResultTransformInputMapping") }()

        }

    }

}

