package kolsa.test.core.common.gson_serialization

sealed class IntOrStringGson {
    data class AsInt(val value: Int) : IntOrStringGson()
    data class AsString(val value: String) : IntOrStringGson()
}