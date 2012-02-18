package whipkey.stemesteem.main;

//Copyright 2011, Zackary Whipkey
//This program/source is distributed under the terms of the GNU General Public License.
//Please read the License.txt file for more information.

import whipkey.stemesteem.R;
import whipkey.stemesteem.components.EndingAddDialog;
import whipkey.stemesteem.components.EndingEditDialog;
import whipkey.stemesteem.components.EndingListFragment;
import whipkey.stemesteem.components.EndingListFragment.DialogItemClickListener;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ListView;

/**
 * EndingListActivity is an activity that is only used for less then Large
 * screensize devices (most phones). It hosts EndingListFragment, and its
 * functions for adding/editing/deleting stems.
 */

public class EndingListActivity extends FragmentActivity implements
		DialogItemClickListener {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.endingactivity);
	}

	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog, Bundle args) { // Prepare
																			// whatever
																			// dialog
																			// we
																			// need
																			// here.
		switch (id) {
		case 0:
			EndingEditDialog d = (EndingEditDialog) dialog;
			d.setId(args.getInt("_id"));
			d.getInputEnding().setText(args.getString("ending"));
			d.setOnDismissListener(new OnDismissListener() {

				public void onDismiss(DialogInterface dialog) {
					EndingListFragment f = (EndingListFragment) getSupportFragmentManager()
							.findFragmentByTag("endingList");
					if (f != null) {
						f.fillData();
					}
				}
			});
			break;
		case 1:
			EndingAddDialog a = (EndingAddDialog) dialog;
			a.getStem().setText(args.getString("stem") + ":");
			a.getInputEnding().setText("");
			a.setB(getIntent().getExtras());
			a.setOnDismissListener(new OnDismissListener() {
				public void onDismiss(DialogInterface dialog) {
					EndingListFragment f = (EndingListFragment) getSupportFragmentManager()
							.findFragmentByTag("endingList");
					if (f != null) {
						f.fillData();
					}
				}
			});
			break;
		}

	}

	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		switch (id) {
		case 0:
			EndingEditDialog e = new EndingEditDialog(this);
			return e;

		case 1:
			EndingAddDialog a = new EndingAddDialog(this);
			return a;
		}
		return null;

	}

	public void dialogItemClick(Bundle b, int dialogId) { // A dialog was
															// requested, so
															// let's call for
															// one.
		showDialog(dialogId, b);

	}

}
