package whipkey.stemesteem.db;

//Copyright 2011, Zackary Whipkey
//This program/source is distributed under the terms of the GNU General Public License.
//Please read the License.txt file for more information.

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class StemDbAdapter {

	private Context context;
	private SQLiteDatabase database;
	private StemDbHelper dbHelper;

	/**
	 * StemDBAdapter is an adapter for SQLite in Stem-Esteem. Due to the
	 * simplicity of the tables, all methods of Stems and Endings are in one
	 * class. Stems table will never be altered by the user.
	 */

	public static final String TABLE_ENDINGS = "Endings";
	public static final String TABLE_STEMS = "Stems";

	public static final String ENDING_NUMBER = "endingNo"; // Primary Key for
															// Endings. TYPE -
															// INT
	public static final String STEM_NUMBER = "stemNo"; // Primary Key for Stems,
														// in addition to
														// weekNo.
	public static final String WEEK_NUMBER = "weekNo";
	public static final String ENDING = "ending"; // represents the "ending",
													// ex.
													// "--finished the sentenced".

	public StemDbAdapter(Context context) {
		this.context = context;

	}

	public StemDbAdapter open() throws SQLException {
		dbHelper = new StemDbHelper(context);
		database = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		dbHelper.close();
	}

	// ENDING METHODS
	/**
	 * Note, in SQLite, since endingNo is set to be INTEGER PRIMARY KEY, it
	 * aliases ROWID as well. This meant that where clauses would result in the
	 * same set regardless of whether endingNo, or ROWID,w as used.
	 */

	/**
	 * @createEnding attaches/inserts an ending to a designed stem.
	 */

	public long createEnding(int weekNo, int stemNo, String ending) {
		ContentValues values = new ContentValues();
		values.put(WEEK_NUMBER, weekNo);
		values.put(STEM_NUMBER, stemNo);
		values.put(ENDING, ending);
		return database.insert(TABLE_ENDINGS, null, values);
	}

	/**
	 * @updateEnding updates an ending that is already attached to the
	 *               designated stem.
	 */

	public long updateEnding(int rowid, String ending) {
		ContentValues values = new ContentValues();
		values.put(ENDING, ending);
		return database.update(TABLE_ENDINGS, values, "ROWID" + " = " + rowid,
				null);
	}

	/**
	 * @deleteEnding deletes an the designated ending attached to a stem.
	 */

	public boolean deleteEnding(int rowid) {
		return database.delete(TABLE_ENDINGS, "ROWID" + " = " + rowid, null) > 0;
	}

	/**
	 * @getAllEndings returns a Cursor with all endings within the designated
	 *                Stem.
	 */

	public Cursor getAllEndings(int weekNo, int stemNo) throws SQLException {
		return database.rawQuery("select ROWID as _id,ending FROM "
				+ TABLE_ENDINGS + " where weekNo = " + weekNo
				+ " and stemNo = " + stemNo, null);

	}

	/**
	 * @getEnding returns a Cursor containing the Ending (endingNo and Ending)
	 *            that is attached to the designated stem.
	 */

	public Cursor getEnding(int weekNo, int stemNo, int rowid) {
		Cursor c = database.rawQuery("select rowid _id,ending FROM "
				+ TABLE_ENDINGS + " where weekNo = " + weekNo
				+ " and stemNo = " + stemNo + " and ROWID = " + rowid, null);
		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}

	// STEM METHODS

	/**
	 * Returns a cursor containing stemNo and stem from the Stems Table.
	 * 
	 * @param weekNo
	 *            - Week number.
	 * @param stemNo
	 *            - Stem position/number of week.
	 * @return
	 */

	public Cursor getStem(int weekNo, int stemNo) {
		Cursor c = database.rawQuery("select rowid _id,stemNo, stem from "
				+ TABLE_STEMS + " where weekNo = " + weekNo + " and stemNo = "
				+ stemNo, null);
		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}

	/**
	 * Returns a cursor containing all stems(stemNo and stem) from that week.
	 * 
	 * @param weekNo
	 *            - Week to retrieve all stems from. Does not include endings of
	 *            stems.
	 * @return
	 */
	public Cursor getAllStems(int weekNo) {
		return database.rawQuery(
				"select ROWID as _id,stemNo, stem, weekNo from " + TABLE_STEMS
						+ " where weekNo = " + weekNo, null);
	}

	public SQLiteDatabase getDatabase() {
		return database;
	}

	public void setDatabase(SQLiteDatabase database) {
		this.database = database;
	}

}