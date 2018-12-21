package com.wwsdemo.explorerdemo.provider;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract.Root;
import android.provider.DocumentsProvider;

import java.io.FileNotFoundException;

import androidx.annotation.Nullable;

public class RecentProvider extends DocumentsProvider {
    @Override
    public Cursor queryRoots(String[] projection) throws FileNotFoundException {
        // Create a cursor with either the requested fields, or the default
        // projection if "projection" is null.
        final MatrixCursor result =
                new MatrixCursor(resolveRootProjection(projection));

        // If user is not logged in, return an empty root cursor.  This removes our
        // provider from the list entirely.
        if (!isUserLoggedIn()) {
            return result;
        }

        // It's possible to have multiple roots (e.g. for multiple accounts in the
        // same app) -- just add multiple cursor rows.
        // Construct one row for a root called "MyCloud".
        final MatrixCursor.RowBuilder row = result.newRow();
        row.add(Root.COLUMN_ROOT_ID, ROOT);
        row.add(Root.COLUMN_SUMMARY, getContext().getString(R.string.root_summary));

        // FLAG_SUPPORTS_CREATE means at least one directory under the root supports
        // creating documents. FLAG_SUPPORTS_RECENTS means your application's most
        // recently used documents will show up in the "Recents" category.
        // FLAG_SUPPORTS_SEARCH allows users to search all documents the application
        // shares.
        row.add(Root.COLUMN_FLAGS, Root.FLAG_SUPPORTS_CREATE |
                Root.FLAG_SUPPORTS_RECENTS |
                Root.FLAG_SUPPORTS_SEARCH);

        // COLUMN_TITLE is the root title (e.g. Gallery, Drive).
        row.add(Root.COLUMN_TITLE, getContext().getString(R.string.title));

        // This document id cannot change once it's shared.
        row.add(Root.COLUMN_DOCUMENT_ID, getDocIdForFile(mBaseDir));

        // The child MIME types are used to filter the roots and only present to the
        //  user roots that contain the desired type somewhere in their file hierarchy.
        row.add(Root.COLUMN_MIME_TYPES, getChildMimeTypes(mBaseDir));
        row.add(Root.COLUMN_AVAILABLE_BYTES, mBaseDir.getFreeSpace());
        row.add(Root.COLUMN_ICON, R.drawable.ic_launcher);

        return result;
    }

    @Override
    public Cursor queryDocument(String documentId, String[] projection) throws FileNotFoundException {
        return null;
    }

    @Override
    public Cursor queryChildDocuments(String parentDocumentId, String[] projection, String sortOrder) throws FileNotFoundException {
        return null;
    }

    @Override
    public ParcelFileDescriptor openDocument(String documentId, String mode, @Nullable CancellationSignal signal) throws FileNotFoundException {
        return null;
    }

    @Override
    public boolean onCreate() {
        return false;
    }
}
