package com.wallback.android.widget.test;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;

import com.wallback.android.test.R;

public class HelloWidget extends AppWidgetProvider {
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new MyTime(context, appWidgetManager), 1,
				1000);
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
			super.onReceive(context, intent);
		}
	}

	private class MyTime extends TimerTask {
		RemoteViews remoteViews;
		AppWidgetManager appWidgetManager;
		ComponentName thisWidget;
		DateFormat format = SimpleDateFormat.getTimeInstance(
				SimpleDateFormat.MEDIUM, Locale.getDefault());

		public MyTime(Context context, AppWidgetManager appWidgetManager) {
			this.appWidgetManager = appWidgetManager;
			remoteViews = new RemoteViews(context.getPackageName(),
					R.layout.main);
			thisWidget = new ComponentName(context, HelloWidget.class);
		}

		@Override
		public void run() {
			remoteViews.setTextViewText(R.id.widget_textview, "Time = "
					+ format.format(new Date(0, 0, 0)));
			appWidgetManager.updateAppWidget(thisWidget, remoteViews);
		}
	}

	public static class UpdateService extends Service {
		@Override
		public void onStart(Intent intent, int startId) {
			RemoteViews updateViews = new RemoteViews(this.getPackageName(),
					R.layout.main);
			Date date = new Date(0, 0, 0);
			DateFormat format = SimpleDateFormat.getTimeInstance(
					SimpleDateFormat.MEDIUM, Locale.getDefault());
			// set the text of component TextView with id 'message'
			updateViews.setTextViewText(R.id.widget_textview, "Current Time "
					+ format.format(date));
			// Push update for this widget to the home screen
			ComponentName thisWidget = new ComponentName(this,
					HelloWidget.class);
			AppWidgetManager manager = AppWidgetManager.getInstance(this);
			manager.updateAppWidget(thisWidget, updateViews);
		}

		@Override
		public IBinder onBind(Intent intent) {
			// TODO Auto-generated method stub
			return null;
		}
	}
}
