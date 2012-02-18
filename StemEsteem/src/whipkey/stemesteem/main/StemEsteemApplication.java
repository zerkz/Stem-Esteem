package whipkey.stemesteem.main;

//Copyright 2011, Zackary Whipkey
//This program/source is distributed under the terms of the GNU General Public License.
//Please read the License.txt file for more information.

import whipkey.stemesteem.db.StemDbAdapter;
import android.app.Application;

/*
 * StemEsteemApplication overrides our default Application implementation to provide us with a global access method for our Database Access Object.
 */

public class StemEsteemApplication extends Application {
	private static StemDbAdapter dbHelper;

	public static StemDbAdapter getDbHelper() {
		return dbHelper;
	}

	@Override
	public void onCreate() {
		dbHelper = new StemDbAdapter(this);
	}
}
