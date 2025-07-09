package kolsa.test.core.common.gson_serialization

import java.lang.reflect.Type
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException

class IntOrStringDeserializer : JsonDeserializer<IntOrStringGson> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): IntOrStringGson {
        return if (json.asJsonPrimitive.isString) {
            IntOrStringGson.AsString(json.asString)
        } else if (json.asJsonPrimitive.isNumber) {
            IntOrStringGson.AsInt(json.asInt)
        } else {
            throw JsonParseException("Expected a string or number for IntOrStringGson field")
        }
    }
}