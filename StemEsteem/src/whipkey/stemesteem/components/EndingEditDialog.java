package whipkey.stemesteem.components;

//Copyright 2011, Zackary Whipkey
//This program/source is distributed under the terms of the GNU General Public License.
//Please read the License.txt file for more information.

import whipkey.stemesteem.R;
import whipkey.stemesteem.main.StemEsteemApplication;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/*
 * EndingEditDialog is called whenever the user clicks an ending (ie, ListView item), in EndingListFragmenet.
 * It has the functions to edit or delete the selected ending.
 */
public class EndingEditDialog extends Dialog {

	private int title;
	private int id;

	private EditText inputEnding;
	private TextView stem;
	private Button ok;
	private Button cancel;
	private Button delete;

	public EndingEditDialog(Context context) {
		super(context);

	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.endingeditdialog);
		inputEnding = (EditText) findViewById(R.id.endingEditInputText);
		stem = (TextView) findViewById(R.id.endingEditStemText);

		ok = (Button) findViewById(R.id.okEndingEditButton);
		cancel = (Button) findViewById(R.id.cancelEndingEditButton);
		delete = (Button) findViewById(R.id.deleteEndingButton);
		setTitle("Edit/Delete Ending");

		ok.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				StemEsteemApplication.getDbHelper().updateEnding(id,
						inputEnding.getText().toString());
				dismiss(); // call fillData() to refresh the listview with the
							// newly edited ending.
			}
		});

		cancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				cancel(); // will not call FillData().
			}
		});
		delete.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				StemEsteemApplication.getDbHelper().deleteEnding(id); // delete
																		// the
																		// selected
																		// ending.
				dismiss(); // call fillData() to refresh the listview and
							// prevent any nullPointerexceptions.
			}
		});
		setOnDismissListener((OnDismissListener) getOwnerActivity()); // sets
																		// the
																		// dismiss
																		// listener
																		// of
																		// the
																		// fragment's
																		// activity.

	}

	public int getTitle() {
		return title;
	}

	public void setTitle(int title) {
		this.title = title;
	}

	public EditText getInputEnding() {
		return inputEnding;
	}

	public void setInputEnding(EditText inputEnding) {
		this.inputEnding = inputEnding;
	}

	public TextView getStem() {
		return stem;
	}

	public void setStem(TextView stem) {
		this.stem = stem;
	}

	public Button getOk() {
		return ok;
	}

	public void setOk(Button ok) {
		this.ok = ok;
	}

	public Button getCancel() {
		return cancel;
	}

	public void setCancel(Button cancel) {
		this.cancel = cancel;
	}

	public Button getDelete() {
		return delete;
	}

	public void setDelete(Button delete) {
		this.delete = delete;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
