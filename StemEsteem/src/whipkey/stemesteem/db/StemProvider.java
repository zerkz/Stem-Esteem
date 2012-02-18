package whipkey.stemesteem.db;

//Copyright 2011, Zackary Whipkey
//This program/source is distributed under the terms of the GNU General Public License.
//Please read the License.txt file for more information.

import java.util.List;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class StemProvider extends ContentProvider {

	/**
	 * StemProvider was a unfinished attempt to port all database/cursor use to
	 * CursorLoader. Therefore, StemProvider is not used in the current version
	 * of Stem-Esteem, and does not require it. My understanding of
	 * ContentProviders was rather weak, and I prefered to use a DAO instead.
	 */

	// SQLite
	private StemDbHelper database;

	// ContentProvider needs
	private static final String TAG = "StemProvider";
	private static final String AUTHORITY = "whipkey.stemesteem.db.StemProvider";
	private static final String BASE_PATH = "weeks";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + BASE_PATH);
	public static final String uriString = ("content://" + AUTHORITY + "/" + BASE_PATH);
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/Weeks";

	// TABLE ATTRIBUTES

	private static final String WEEKS_TABLE_NAME = "Weeks";
	private static final String STEMS_TABLE_NAME = "Stems";
	private static final String ENDINGS_TABLE_NAME = "Endings";
	private static final String WEEK_NO_NAME = "weekNo";
	private static final String STEM_NO_NAME = "stemNo";
	private static final String ENDING_NO_NAME = "endingNo";

	// URI's

	// TABLES
	private static final int WEEKS = 1;
	private static final int WEEK_NO = 2;
	private static final int STEMS = 3;
	private static final int STEM_NO = 4;
	private static final int ENDINGS = 5;
	private static final int ENDING_NO = 6;

	// URI additions
	private static final UriMatcher matcher = new UriMatcher(
			UriMatcher.NO_MATCH);

	static {
		matcher.addURI(AUTHORITY, "weeks", WEEKS);
		matcher.addURI(AUTHORITY, "weeks/#", WEEK_NO);
		matcher.addURI(AUTHORITY, "weeks/#/stems", STEMS);
		matcher.addURI(AUTHORITY, "weeks/#/stems/#", STEM_NO);
		matcher.addURI(AUTHORITY, "weeks/#/stems/#/endings", ENDINGS);
		matcher.addURI(AUTHORITY, "weeks/#/stems/#/endings/#", ENDING_NO);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int uriType = matcher.match(uri);
		SQLiteDatabase db = database.getWritableDatabase();
		int rowsDeleted = 0;
		switch (uriType) {
		case ENDING_NO: {
			List<String> list1 = uri.getPathSegments();
			rowsDeleted = db.delete(ENDINGS_TABLE_NAME,
					WEEK_NO_NAME + " = " + list1.get(1) + " and "
							+ STEM_NO_NAME + " = " + list1.get(3) + " and "
							+ ENDING_NO_NAME + " = " + list1.get(5),
					selectionArgs);
			break;
		}
		default:
			throw new IllegalArgumentException(
					"Unsupported URI: Use ENDING_NO(6)");

		}
		getContext().getContentResolver().notifyChange(uri, null);

		return rowsDeleted;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int uriType = matcher.match(uri);
		SQLiteDatabase db = database.getWritableDatabase();
		List<String> list;
		long id = 0;
		switch (uriType) {
		case WEEK_NO: {
			values = new ContentValues();
			values.put(WEEK_NO_NAME, Integer.parseInt(uri.getLastPathSegment()));
			id = db.insert(WEEKS_TABLE_NAME, null, values);
			break;
		}
		case STEM_NO: {
			list = uri.getPathSegments();
			values.put(WEEK_NO_NAME, Integer.parseInt(list.get(1)));
			values.put(STEM_NO_NAME, Integer.parseInt(list.get(3)));
			id = db.insert(STEMS_TABLE_NAME, null, values);
			break;
		}
		case ENDING_NO: {
			list = uri.getPathSegments();
			values.put(WEEK_NO_NAME, Integer.parseInt(list.get(1)));
			values.put(STEM_NO_NAME, Integer.parseInt(list.get(3)));
			values.put(ENDING_NO_NAME, Integer.parseInt(list.get(5)));
			id = db.insert(ENDINGS_TABLE_NAME, null, values);
			break;
		}

		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);

		}
		getContext().getContentResolver().notifyChange(uri, null);

		return Uri.parse(String.valueOf(id));
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		qb.setTables(WEEKS_TABLE_NAME);

		int uriType = matcher.match(uri);
		switch (uriType) {
		case WEEKS:
		case WEEK_NO:
			qb.appendWhere(WEEK_NO_NAME + " = " + uri.getLastPathSegment());
			break;
		case STEMS:
			qb.setTables(STEMS_TABLE_NAME);
			break;
		case STEM_NO:
			List<String> list = uri.getPathSegments();
			qb.appendWhere(WEEK_NO_NAME + " = " + list.get(1) + " and "
					+ STEM_NO_NAME + " = " + list.get(3));
			break;
		case ENDINGS:
			qb.setTables(ENDINGS_TABLE_NAME);
			break;
		case ENDING_NO:
			List<String> list1 = uri.getPathSegments();
			qb.appendWhere(WEEK_NO_NAME + " = " + list1.get(1) + " and "
					+ STEM_NO_NAME + " = " + list1.get(3) + " and "
					+ ENDING_NO_NAME + " = " + list1.get(5));
			break;
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
		SQLiteDatabase db = database.getWritableDatabase();
		Cursor cursor = qb.query(db, projection, selection, selectionArgs,
				null, null, sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);

		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int uriType = matcher.match(uri);
		SQLiteDatabase db = database.getWritableDatabase();
		int rowsUpdated = 0;
		List<String> list;
		switch (uriType) {
		case ENDING_NO: {
			list = uri.getPathSegments();
			values.put(WEEK_NO_NAME, Integer.parseInt(list.get(1)));
			values.put(STEM_NO_NAME, Integer.parseInt(list.get(3)));
			values.put(ENDING_NO_NAME, Integer.parseInt(list.get(5)));
			rowsUpdated = db.update(
					ENDINGS_TABLE_NAME,
					values,
					WEEK_NO_NAME + " = " + Integer.parseInt(list.get(1))
							+ " and " + STEM_NO_NAME + " = "
							+ Integer.parseInt(list.get(3)) + " and "
							+ ENDING_NO_NAME + " = "
							+ Integer.parseInt(list.get(5)), null);
			break;
		}
		default:
			throw new IllegalArgumentException("Unknown URI:" + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);

		return rowsUpdated;
	}

}
