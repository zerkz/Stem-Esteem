package whipkey.stemesteem.components;

//Copyright 2011, Zackary Whipkey
//This program/source is distributed under the terms of the GNU General Public License.
//Please read the License.txt file for more information.

import whipkey.stemesteem.R;
import whipkey.stemesteem.main.StemEsteemApplication;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnDismissListener;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 
 * EndingAddDialog is created whenever a user called the "Add Ending" button in
 * EndingListFragment.
 * 
 * 
 */
public class EndingAddDialog extends Dialog {

	private int title;
	private int id;
	private Bundle b;

	private EditText inputEnding; // EditText that the user types into for
									// "ending" input.
	private TextView stem; // Shows in the dialog what stem the user is under.
	private Button ok;
	private Button cancel;

	public EndingAddDialog(Context context) {
		super(context);

	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.endingadddialog);

		// inflate our views.
		inputEnding = (EditText) findViewById(R.id.endingAddInputText);
		stem = (TextView) findViewById(R.id.endingAddStemText);

		ok = (Button) findViewById(R.id.okEndingAddButton);
		cancel = (Button) findViewById(R.id.cancelEndingAddButton);
		setTitle("Add Ending");
		ok.setOnClickListener(new View.OnClickListener() { // assign the
															// function of our
															// ok button.
			public void onClick(View v) {

				StemEsteemApplication.getDbHelper().createEnding(
						b.getInt("weekNo"), b.getInt("stemNo"),
						inputEnding.getText().toString());
				dismiss(); // this will call FillData() through the dismiss
							// listener and also kill the dialog.
			}
		});

		cancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				cancel(); // This will kill the dialog but will not call
							// fillData(), as we logically know nothing has
							// changed in the database.
			}
		});

		setOnDismissListener((OnDismissListener) getOwnerActivity());// sets the
																		// dismiss
																		// listener
																		// of
																		// the
																		// fragment's
																		// activity.
	}

	public Bundle getB() {
		return b;
	}

	public void setB(Bundle b) {
		this.b = b;
	}

	public TextView getStem() {
		return stem;
	}

	public void setStem(TextView stem) {
		this.stem = stem;
	}

	public EditText getInputEnding() {
		return inputEnding;
	}

	public void setInputEnding(EditText inputEnding) {
		this.inputEnding = inputEnding;
	}

}