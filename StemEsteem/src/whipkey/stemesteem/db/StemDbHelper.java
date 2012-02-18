package whipkey.stemesteem.db;

//Copyright 2011, Zackary Whipkey
//This program/source is distributed under the terms of the GNU General Public License.
//Please read the License.txt file for more information.

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * StemDbHelper is a class that extends SQLiteOpenHelper to provide a basic
 * schema/database setup for Stem-Esteem.
 * 
 * 
 */

public class StemDbHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "data"; // Database file name.
	private static final int DATABASE_VERSION = 2; // version number for upgrade
													// purposes. Increase to
													// prompt onUpgrade()

	public static final String DATABASE_TABLE_STEMS = "Stems"; // Table for
																// Stems
	public static final String DATABASE_TABLE_ENDINGS = "Endings"; // Table for
																	// Ending

	public static final String FOREIGN_KEYS_ON = "PRAGMA foreign_keys = ON;"; // Turns
																				// SQLite
																				// foreign
																				// keys
																				// on/enabled.

	public static final String WEEK_NUMBER = "weekNo"; // Week Number. Denotes
														// what week the stems
														// or endings are on.
	public static final String STEM_NUMBER = "stemNo"; // Stem Number. What
														// position the stem is
														// of the week (1, 2,
														// 3).
	public static final String ENDING_NUMBER = "endingNo"; // Ending Number.
															// This is
															// autoincremented
															// and aliases ROWID
															// in SQLite.
															// Therefore, every
															// endingNo is
															// unique and is the
															// primary key for
															// the Endings
															// table.

	public static final String STEM = "stem"; // holds actual text "stem"
	public static final String ENDING = "ending"; // holds actual text "ending"

	// Start SQL create table statements here

	/**
	 * private static final String DATABASE_CREATE_WEEKS = // Create Table Weeks
	 * // Statement "create table " + DATABASE_TABLE_WEEKS + " (" + WEEK_NUMBER
	 * + " int NOT NULL PRIMARY KEY" + " );";
	 */

	private static final String DATABASE_CREATE_STEMS = // Create Table Stems
														// Statement
	"create table " + DATABASE_TABLE_STEMS + " (" + WEEK_NUMBER
			+ " int NOT NULL," + STEM_NUMBER + " int NOT NULL," + STEM
			+ " text," + "PRIMARY KEY (" + STEM_NUMBER + "," + WEEK_NUMBER
			+ ") );";

	private static final String DATABASE_CREATE_ENDINGS = // Create Table
															// Endings Statement
	"create table " + DATABASE_TABLE_ENDINGS + " (" + WEEK_NUMBER
			+ " int NOT NULL," + STEM_NUMBER + " int NOT NULL," + ENDING
			+ " text," + "endingNo INTEGER PRIMARY KEY, " + "FOREIGN KEY ("
			+ STEM_NUMBER + ") REFERENCES " + DATABASE_TABLE_STEMS + "("
			+ STEM_NUMBER + ")," + "FOREIGN KEY (" + WEEK_NUMBER
			+ ") REFERENCES " + DATABASE_TABLE_STEMS + "(" + WEEK_NUMBER + "))";

	public StemDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	// called during creation of database. executes table creation on SQL
	// statements.
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(FOREIGN_KEYS_ON); // turn our foreign keys on.
		db.execSQL(DATABASE_CREATE_STEMS); // create table for Stems.
		db.execSQL(DATABASE_CREATE_ENDINGS); // create table for Endings.

		// insert stems here, using execSQL.
		db.execSQL("INSERT into STEMS values(1,1,'This is an example stem. Week 1, Stem 1.')"); // example
																								// insert
																								// into
																								// stems.

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { // this
																				// simply
																				// rebuilds
																				// the
																				// database
																				// when
																				// called,
																				// and
																				// will
																				// only
																				// be
																				// called
																				// whenever
		Log.w(this.getClass().getName(), "Updating database: " + oldVersion // the
																			// database
																			// version
																			// was
																			// increased
																			// from
																			// last
																			// build.
				+ " to " + newVersion + "\n ALL DATA WILL BE DESTROYED");
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_ENDINGS);
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_STEMS);
		onCreate(db);

	}
}