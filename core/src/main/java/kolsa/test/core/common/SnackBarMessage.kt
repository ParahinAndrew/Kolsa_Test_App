package kolsa.test.core.common

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import kolsa.test.core.R

class SnackBarMessage {

    fun makeSnackBarShort(view: View, text: String) {
        showSnackBar(view, text, Snackbar.LENGTH_SHORT)
    }

    fun makeSnackBarLong(view: View, text: String) {
        showSnackBar(view, text, Snackbar.LENGTH_LONG)
    }


    private fun showSnackBar(view: View, text: String, duration: Int) {
        val snackBar = Snackbar.make(view, text, duration)

        snackBar.view.backgroundTintList = ContextCompat.getColorStateList(view.context, R.color.background_white)

        val snackBarTextView = snackBar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        snackBarTextView.setTextAppearance(R.style.Core_TextStyle_Bold)

        snackBar.setTextColor(ContextCompat.getColor(view.context, R.color.text_black))

        snackBar.show()
    }

}