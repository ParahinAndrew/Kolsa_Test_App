package kolsa.test.core.common

import androidx.annotation.StringRes
import kolsa.test.core.R

enum class TypesTraining(
    @StringRes val stringTypeId: Int,
    val intType: Int?
) {

    STRENGTH(R.string.strength, 1),
    STRETCHING(R.string.stretching, 2),
    CARDIO(R.string.cardio, 3),
    UNKNOWN(R.string.unknown, null);

    companion object {

        fun fromInt(intType: Int?): TypesTraining {
            return entries.find { it.intType == intType } ?: UNKNOWN
        }

    }

}