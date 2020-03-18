package com.aaa.sportclub.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import static com.aaa.sportclub.data.Contacts.memberEntry;


public class MyContentProvider extends ContentProvider {

    MyDBHandler myDBHandler;

    private static final int MEMBERS = 111;
    private static final int MEMBER_ID = 222;

    // Creates a UriMatcher object.
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(Contacts.AUTHORITY, Contacts.PATH_MEMBERS, MEMBERS);
        uriMatcher.addURI(Contacts.AUTHORITY, Contacts.PATH_MEMBERS + "/#", MEMBER_ID);
    }

    @Override
    public boolean onCreate() {
        myDBHandler = new MyDBHandler(getContext());
        return true;
    }

    @Override
    // content://com.aaa.sportclub/members/34
    // projection = { "lastName", "gender" }
    public Cursor query(Uri uri, String[] projection,
                        String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase db = myDBHandler.getReadableDatabase();
        Cursor cursor;

        int match = uriMatcher.match(uri);

        switch (match) {
            case MEMBERS:
                cursor = db.query(memberEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;

            // selection = "_id=?"
            // selectionArgs = 34
            case MEMBER_ID:
                selection = memberEntry._ID + "=?";
                selectionArgs = new String[]
                        {String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(memberEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;

            default:
                throw new IllegalArgumentException("Can't query incorrect URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        if (values.containsKey(memberEntry.COLUMN_FIRST_NAME)) {
            String firstName = values.getAsString(memberEntry.COLUMN_FIRST_NAME);
            if (firstName == null) {
                throw new IllegalArgumentException("You have to input first name");
            }
        }

        if (values.containsKey(memberEntry.COLUMN_LAST_NAME)) {
            String lastName = values.getAsString(memberEntry.COLUMN_LAST_NAME);
            if (lastName == null) {
                throw new IllegalArgumentException("You have to input last name");
            }
        }

        if (values.containsKey(memberEntry.COLUMN_GENDER)) {
            Integer gender = values.getAsInteger(memberEntry.COLUMN_GENDER);
            if (gender == null || !(gender == memberEntry.GENDER_UNKNOWN || gender ==
                    memberEntry.GENDER_MALE || gender == memberEntry.GENDER_FEMALE)) {
                throw new IllegalArgumentException("You have to input correct gender");
            }
        }

        if (values.containsKey(memberEntry.COLUMN_SPORT)) {
            String sport = values.getAsString(memberEntry.COLUMN_SPORT);
            if (sport == null) {
                throw new IllegalArgumentException("You have to input sport");
            }
        }

        SQLiteDatabase db = myDBHandler.getWritableDatabase();
        int match = uriMatcher.match(uri);

        switch (match) {
            case MEMBERS:
                long id = db.insert(memberEntry.TABLE_NAME, null, values);
                if (id == -1) {
                    Log.e("insertMethod", "Insertion of data in the table failed for " + uri);
                    return null;
                }
                getContext().getContentResolver().notifyChange(uri, null);

                return ContentUris.withAppendedId(uri, id);

            default:
                throw new IllegalArgumentException("Insertion of data in " + "the table failed for " + uri);
        }

    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase db = myDBHandler.getWritableDatabase();

        int match = uriMatcher.match(uri);
        int rowsDeleted;

        switch (match) {
            case MEMBERS:
                rowsDeleted = db.delete(memberEntry.TABLE_NAME, selection, selectionArgs);
                break;
            // selection = "_id=?"
            // selectionArgs = 34
            case MEMBER_ID:
                selection = memberEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = db.delete(memberEntry.TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Can't delete this URI " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.containsKey(memberEntry.COLUMN_FIRST_NAME)) {
            String firstName = values.getAsString(memberEntry.COLUMN_FIRST_NAME);
            if (firstName == null) {
                throw new IllegalArgumentException("You have to input first name");
            }
        }
        if (values.containsKey(memberEntry.COLUMN_LAST_NAME)) {
            String lastName = values.getAsString(memberEntry.COLUMN_LAST_NAME);
            if (lastName == null) {
                throw new IllegalArgumentException("You have to input last name");
            }
        }
        if (values.containsKey(memberEntry.COLUMN_GENDER)) {
            Integer gender = values.getAsInteger(memberEntry.COLUMN_GENDER);
            if (gender == null || !(gender == memberEntry.GENDER_UNKNOWN || gender ==
                    memberEntry.GENDER_MALE || gender == memberEntry.GENDER_FEMALE)) {
                throw new IllegalArgumentException("You have to input correct gender");
            }
        }
        if (values.containsKey(memberEntry.COLUMN_SPORT)) {
            String sport = values.getAsString(memberEntry.COLUMN_SPORT);
            if (sport == null) {
                throw new IllegalArgumentException("You have to input sport");
            }
        }

        SQLiteDatabase db = myDBHandler.getWritableDatabase();

        int match = uriMatcher.match(uri);
        int rowsUpdated;


        switch (match) {
            case MEMBERS:
                rowsUpdated = db.update(memberEntry.TABLE_NAME, values, selection, selectionArgs);
                break;

            // selection = "_id=?"
            // selectionArgs = 34
            case MEMBER_ID:
                selection = memberEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsUpdated = db.update(memberEntry.TABLE_NAME, values, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Can't update this URI " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public String getType(Uri uri) {
        int match = uriMatcher.match(uri);
        switch (match) {
            case MEMBERS:
                return memberEntry.CONTENT_MULTIPLE_ITEMS;

            case MEMBER_ID:
                return memberEntry.CONTENT_SINGLE_ITEM;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

}

// URI - Unified Resource Identifier
// content://com.aaa.sportclub/members
// URL - Unified Resource Locator
//http://google.com
// content://com.aaa.sportclub/members/3 code 111
// content://com.aaa.sportclub/members code 221
// content://com.android.calendar/events
// content://user_dictionary/words
// content:// - scheme
// com.android.contacts - content authority
// contacts - type of data