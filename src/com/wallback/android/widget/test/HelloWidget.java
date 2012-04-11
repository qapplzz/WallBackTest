package com.wallback.android.widget.test;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.wallback.android.test.R;

public class HelloWidget extends AppWidgetProvider {
	public static String ACTION_WIDGET_CONFIGURE = "ConfigureWidget";
	public static String ACTION_WIDGET_RECEIVER = "ActionReceiverWidget";

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		Toast.makeText(context, "onUpdate", Toast.LENGTH_SHORT).show();

		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.main);
		Intent configIntent = new Intent(context, ClickOneActivity.class);
		configIntent.setAction(ACTION_WIDGET_CONFIGURE);

		Intent active = new Intent(context, HelloWidget.class);
		active.setAction(ACTION_WIDGET_RECEIVER);
		active.putExtra("msg", "Message for Button 1");

		PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context,
				0, active, 0);
		PendingIntent configPendingIntent = PendingIntent.getActivity(context,
				0, configIntent, 0);

		remoteViews.setOnClickPendingIntent(R.id.button_one,
				actionPendingIntent);
		remoteViews.setOnClickPendingIntent(R.id.button_two,
				configPendingIntent);

		appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
	}

	@Override
	public void onReceive(Context context, Intent intent) {

		// v1.5 fix that doesn't call onDelete Action
		final String action = intent.getAction();
		if (AppWidgetManager.ACTION_APPWIDGET_DELETED.equals(action)) {
			final int appWidgetId = intent.getExtras().getInt(
					AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
			if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
				this.onDeleted(context, new int[] { appWidgetId });
			}
		} else {
			// check, if our Action was called
			if (intent.getAction().equals(ACTION_WIDGET_RECEIVER)) {
				String msg = "null";
				try {
					msg = intent.getStringExtra("msg");
				} catch (NullPointerException e) {
					Log.e("Error", "msg = null");
				}
				Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

				PendingIntent contentIntent = PendingIntent.getActivity(
						context, 0, intent, 0);
				NotificationManager notificationManager = (NotificationManager) context
						.getSystemService(Context.NOTIFICATION_SERVICE);
				Notification noty = new Notification(R.drawable.appwidget,
						"Button 1 clicked", System.currentTimeMillis());

				noty.setLatestEventInfo(context, "Notice", msg, contentIntent);
				notificationManager.notify(1, noty);

			} else {
				// do nothing
			}

			super.onReceive(context, intent);
		}
	}
}
