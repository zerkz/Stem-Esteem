package whipkey.stemesteem.main;

// Copyright 2011, Zackary Whipkey
// This program/source is distributed under the terms of the GNU General Public License.
// Please read the License.txt file for more information.

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.support.v4.app.*;

import whipkey.stemesteem.R;
import whipkey.stemesteem.components.EndingAddDialog;
import whipkey.stemesteem.components.EndingEditDialog;
import whipkey.stemesteem.components.EndingListFragment;
import whipkey.stemesteem.components.EndingListFragment.DialogItemClickListener;
import whipkey.stemesteem.components.StemListFragment;
import whipkey.stemesteem.components.StemListFragment.StemListItemClickListener;
import whipkey.stemesteem.db.StemDbAdapter;
import whipkey.stemesteem.db.StemDbHelper;
import whipkey.stemesteem.db.StemProvider;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

/**
 * Stem-Esteem:
 * 
 * Requires Android 2.2
 * 
 * Main activity for the Stem-Esteem Android App. Stem-Esteem is a "framework"
 * app for sentence completion exercises, particularly focused upon self-esteem
 * issues. The application code can be modified to suit other uses, however.
 * v1.0's setup goes like this.
 * 
 * Weeks have stems (stems are the beginning fragment of a sentence, ie.
 * "I am reading--"
 * 
 * Stems have endings (Note that a stem can have multiple
 * endings... ie. "-- some source code." or "-- a book".
 * 
 * Weeks are handled by StemEsteem.java. Stems and Endings are handled by a
 * SQLite Database which is further explored in StemDbHelper and StemDbAdapter.
 * 
 * Endings can be inserted, edited, and deleted from the app itself. Stems and
 * Weeks can only be added programmatically.
 * 
 * Stem-Esteem includes support for both larger screen devices (tablets) and
 * medium-small screen (phones). Large and XLarge (tablets) devices will display
 * the current week, that week's stems, and a chosen stem's endings at once. <
 * Large devices will split the fragments of stems and endings into two
 * different "activities/screens".
 * 
 * Stem-Esteem used to the V4 android compatibility library to use fragments.
 */

public class StemEsteem extends FragmentActivity implements
		StemListItemClickListener, DialogItemClickListener {

	private StemDbAdapter dbHelper;
	private int currentWeek = 1;
	private int maxWeek = 20;
	private int minWeek = 1;

	private ListFragment f; // EndingListFragment. Only used in Large or above
							// screens (tablets, mainly)
	private Button nextButton; // button for going to the next week.
	private Button prevButton; // button for going to the previous week.
	private Button submitFeedback; // used to be used for Zubhium.
	private TextView weekText; // Textview to show what week the app is "on".
	private ListView fragList; // Listview of the stemlistfragment.

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// get database access object.
		dbHelper = StemEsteemApplication.getDbHelper();
		dbHelper.open();
		// inflate views from layout.
		setContentView(R.layout.main);
		nextButton = (Button) findViewById(R.id.nextButton);
		prevButton = (Button) findViewById(R.id.prevButton);

		weekText = (TextView) findViewById(R.id.weekText);
		weekText.setText("Week " + currentWeek);
		f = (ListFragment) getSupportFragmentManager().findFragmentById(
				R.id.stemListFragment);
		fragList = f.getListView(); // get StemListFragment's listview.
		fillData(); // refresh listview/adapter

		nextButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				if (currentWeek < maxWeek) {
					currentWeek++;

					weekText.setText("Week " + currentWeek);
					fillData();
				}
			}
		});

		prevButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				if (currentWeek > minWeek) {
					currentWeek--;
					weekText.setText("Week " + currentWeek);
					fillData();
				}
			}
		});
	}

	// SUGGESTION - This method's function could be inserted into the
	// StemListFragment for more cohesion.
	public void fillData() { // method to reset the adapter with a new cursor
								// when a new week is chosen.

		if (dbHelper.getDatabase().isDbLockedByOtherThreads() == false) {
			Cursor c = dbHelper.getAllStems(currentWeek);
			String[] from = new String[] { "stem" };
			int[] to = new int[] { R.id.stemText };
			startManagingCursor(c);
			SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
					R.layout.stemlist, c, from, to);
			fragList.setAdapter(adapter);

		} else {
			Log.w("DB LOCKED", "Other thread locked database during fillData()");
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (dbHelper != null) {
			dbHelper.close();
		}
	}

	public int getCurrentWeek() {
		return currentWeek;
	}

	public void setCurrentWeek(int currentWeek) {
		this.currentWeek = currentWeek;
	}

	public void stemListItemClick(int weekNo, int stemNo, String stem) {
		if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE) {
			EndingListFragment f = (EndingListFragment) getSupportFragmentManager()
					.findFragmentByTag("endingList");
			Bundle b = new Bundle();
			b.putInt("weekNo", weekNo);
			b.putInt("stemNo", stemNo);
			b.putString("stem", stem);
			f.setB(b);
			f.fillData();

		} else {
			Intent showEndings = new Intent(getApplicationContext(),
					EndingListActivity.class);
			showEndings.putExtra("weekNo", weekNo);
			showEndings.putExtra("stemNo", stemNo);
			showEndings.putExtra("stem", stem);
			startActivity(showEndings);
		}
	}

	public void dialogItemClick(Bundle b, int dialogId) { // called from our
															// endingListFragment
															// when on a
															// tablet/large
															// scrn.
		showDialog(dialogId, b);
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog, Bundle args) { // Prepares
																			// the
																			// dialog
																			// with
																			// its
																			// required
																			// information.
		switch (id) {
		case 0: // We are editing an ending, so setup EditEndingDialog.
			EndingEditDialog d = (EndingEditDialog) dialog;
			d.setId(args.getInt("_id"));
			d.getInputEnding().setText(args.getString("ending"));
			d.setOnDismissListener(new OnDismissListener() {

				public void onDismiss(DialogInterface dialog) {// When
																// dismissed/OK
																// is clicked,
																// refresh the
																// list of
																// endings to
																// show changes.
					EndingListFragment f = (EndingListFragment) getSupportFragmentManager()
							.findFragmentByTag("endingList");
					if (f != null) {
						f.fillData();
					}
				}
			});
			break;
		case 1: // We are adding an ending, so setup AddEndingDialog.
			EndingAddDialog a = (EndingAddDialog) dialog;
			a.getStem().setText(args.getString("stem") + ":");
			a.getInputEnding().setText("");
			a.setB(args);
			a.setOnDismissListener(new OnDismissListener() {
				public void onDismiss(DialogInterface dialog) { // When
																// dismissed/OK
																// is clicked,
																// refresh the
																// list of
																// endings to
																// show changes.
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

}
