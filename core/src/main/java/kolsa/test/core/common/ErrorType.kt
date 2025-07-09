package kolsa.test.core.common

import androidx.annotation.StringRes
import kolsa.test.core.R

enum class ErrorType(
    @StringRes val messageId: Int
) {
    NETWORK(R.string.error_network),
    SERVER(R.string.error_server),
    VALIDATION(R.string.error_validation),
    UNKNOWN(R.string.error_unknown)
}