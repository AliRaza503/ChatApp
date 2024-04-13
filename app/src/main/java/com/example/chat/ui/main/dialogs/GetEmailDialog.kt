package com.example.chat.ui.main.dialogs
import android.app.AlertDialog
import android.content.Context
import android.widget.EditText
import android.widget.LinearLayout

class GetEmailDialog(context: Context, val emailEnteredCallback: (String) -> Unit) {

    private val dialog: AlertDialog = AlertDialog.Builder(context).create()

    init {
        // Set the custom layout for the dialog
        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.VERTICAL

        // Create an EditText for user input
        val input = EditText(context)
        layout.addView(input)

        // Set the layout parameters for the EditText (optional)
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(40, 20, 40, 0)
        input.layoutParams = params

        // Set the custom layout to the dialog
        dialog.setView(layout)

        // Set dialog title (optional)
        dialog.setTitle("Enter Receiver's Email")

        // Set positive button and its action
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Submit") { _, _ ->
            // Pass the entered input to the calling fragment and dismiss the dialog
            emailEnteredCallback(input.text.toString())
        }

        // Set negative button and its action
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel") { _, _ ->
            // Handle the negative button click here
            dialog.dismiss()
        }
    }

    // Show the dialog
    fun show() {
        dialog.show()
    }
}