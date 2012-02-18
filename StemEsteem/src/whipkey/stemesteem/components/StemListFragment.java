package whipkey.stemesteem.components;

//Copyright 2011, Zackary Whipkey
//This program/source is distributed under the terms of the GNU General Public License.
//Please read the License.txt file for more information.

import whipkey.stemesteem.R;
import whipkey.stemesteem.db.StemDbAdapter;
import whipkey.stemesteem.db.StemDbHelper;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * 
 * StemListFragment hosts the listview that displays the stems for the current
 * week. It is displayed in the StemEsteem activity. An implementation of
 * StemEsteem.java's filldata() could be moved to here. It's current function
 * exists in a callback.
 * 
 */

public class StemListFragment extends ListFragment {
	StemListItemClickListener clickListener;

	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			clickListener = (StemListItemClickListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ "needs to implement StemListItemClickListener");
		}
	}

	public interface StemListItemClickListener {
		public void stemListItemClick(int weekNo, int stemNo, String stem);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.stemlistfragment, container);

		return v;
	}

	public void onDestroy() {
		super.onDestroy();
	}

	public void onListItemClick(ListView l, View v, int position, long id) {
		SimpleCursorAdapter adapter = (SimpleCursorAdapter) l.getAdapter();
		Cursor c = adapter.getCursor();

		c.moveToPosition(position);
		int weekNo = c.getInt(c.getColumnIndex("weekNo"));
		int stemNo = c.getInt(c.getColumnIndex("stemNo"));
		String stem = c.getString(c.getColumnIndex("stem"));
		clickListener.stemListItemClick(weekNo, stemNo, stem); // call back to
																// our activity,
																// requesting
																// the
																// currentWeek's
																// value so that
																// our listview
																// can be
																// updated.
	}

}
