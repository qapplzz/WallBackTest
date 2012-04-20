package com.wallback.android.test.contact;

import java.io.InputStream;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wallback.android.test.R;

public class ContactListTest extends Activity {

	TextView idView;
	TextView phone;
	TextView name;
	ImageView photo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pick_contact);
		idView = (TextView) findViewById(R.id.id);
		phone = (TextView) findViewById(R.id.phone);
		name = (TextView) findViewById(R.id.name);
		photo = (ImageView) findViewById(R.id.photo);
		((Button) findViewById(R.id.pick_contact))
				.setOnClickListener((new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(Intent.ACTION_PICK);
						intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
						startActivityForResult(intent, 1);
					}

				}));

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		Uri dataUri = data.getData();
		Cursor cursor = managedQuery(dataUri, null, null, null, null);
		String people_Number = null;
		while (cursor.moveToNext()) {
			int getcolumnId = cursor
					.getColumnIndex(ContactsContract.Contacts._ID);
			long id = cursor.getLong(getcolumnId);
			String people_Name = cursor
					.getString(cursor
							.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
			String hasPhoneNumber = cursor
					.getString(cursor
							.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
			if (hasPhoneNumber.equalsIgnoreCase("1")) {
				hasPhoneNumber = "true";
			} else {
				hasPhoneNumber = "false";
			}
			if (Boolean.parseBoolean(hasPhoneNumber)) {
				Cursor phones = getContentResolver().query(
						ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
						null,
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID
								+ " = " + id, null, null);
				while (phones.moveToNext()) {
					people_Number = phones
							.getString(phones
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
				}
				phones.close(); // End
			}
			idView.setText(String.valueOf(id));
			phone.setText(people_Name);
			name.setText(people_Number);
			Uri uri = ContentUris.withAppendedId(
					ContactsContract.Contacts.CONTENT_URI, id);

			Log.d("TAG", "id = " + id + ", uri = " + uri);

			InputStream input = ContactsContract.Contacts
					.openContactPhotoInputStream(getContentResolver(), uri);

			photo.setImageBitmap(BitmapFactory.decodeStream(input));
		}
	}
}
