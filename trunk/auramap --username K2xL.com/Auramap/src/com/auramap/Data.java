package com.auramap;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Contacts;

public class Data {
public static String pNumber;
public static String pKey;
	
	public static String getContactNameFromNumber(String number, ContentResolver cont) {
		if (number.equals(Data.pNumber)) { return "Me"; }
		// define the columns I want the query to return
		String[] projection = new String[] { Contacts.Phones.DISPLAY_NAME,
				Contacts.Phones.NUMBER };

		// encode the phone number and build the filter URI
		Uri contactUri = Uri.withAppendedPath(
				Contacts.Phones.CONTENT_FILTER_URL, Uri.encode(number));

		// query time
		Cursor c = cont.query(contactUri, projection, null,
				null, null);

		// if the query returns 1 or more results
		// return the first result
		if (c.moveToFirst()) {
			String name = c.getString(c
					.getColumnIndex(Contacts.Phones.DISPLAY_NAME));
			return name;
		}

		// return the original number if no match was found
		return number;
	}
	
	public static String distanceTo(long distanceInSeconds, boolean
			showLessThanAMinute)
			        {
			                long distanceInMinutes = Math.round(distanceInSeconds / 60);

			        if ( distanceInMinutes <= 1 ) {
			            if ( !showLessThanAMinute ) {
			                return (distanceInMinutes == 0) ? "less than a minute ago" : "1 minute ago";
			            } else {
			                if ( distanceInSeconds < 5 ) {
			                    return "less than 5 seconds ago";
			                }
			                if ( distanceInSeconds < 10 ) {
			                    return "less than 10 seconds ago";
			                }
			                if ( distanceInSeconds < 20 ) {
			                    return "less than 20 seconds ago";
			                }
			                if ( distanceInSeconds < 40 ) {
			                    return "about half a minute ago";
			                }
			                if ( distanceInSeconds < 60 ) {
			                    return "less than a minute ago";
			                }

			                return "1 minute ago";
			            }
			        }
			        if ( distanceInMinutes < 45 ) {
			            return distanceInMinutes + " minutes ago";
			        }
			        if ( distanceInMinutes < 90 ) {
			            return "about 1 hour ago";
			        }
			        if ( distanceInMinutes < 1440 ) {
			            return "about " + Math.round(1.0*(distanceInMinutes) / 60)
			+ " hours ago";
			        }
			        if ( distanceInMinutes < 2880 ) {
			            return "1 day";
			        }
			        if ( distanceInMinutes < 43200 ) {
			            return "about " + Math.round(1.0*(distanceInMinutes) /
			1440) + " days ago";
			        }
			        if ( distanceInMinutes < 86400 ) {
			            return "about 1 month";
			        }
			        if ( distanceInMinutes < 525600 ) {
			            return Math.round(1.0*(distanceInMinutes) / 43200) + " months ago";
			        }
			        if ( distanceInMinutes < 1051199 ) {
			            return "about 1 year ago";
			        }

			        return "over " + Math.round(1.0*(distanceInMinutes) / 525600)
			+ " years ago";
			        }
}
