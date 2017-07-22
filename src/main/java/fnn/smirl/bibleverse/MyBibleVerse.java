package fnn.smirl.bibleverse;

import android.app.*;
import android.os.*;
import android.appwidget.*;
import android.content.*;
import java.util.*;
import android.widget.*;
import android.util.*;
import com.android.volley.*;
import com.android.volley.toolbox.*;
import org.json.*;

public class MyBibleVerse extends AppWidgetProvider {
	//private static final String URL = "http://www.ourmanna.com/verses/api/get?format=text&order=random";
//	private static final String URL = "http://labs.bible.org/api/?passage=random&formatting=plain";
	private static String text = "Bible Verse";
//	private String[] texts = {"text 1", "text 2", "text 3"};
	private static final String
	COMMAND = "command";

	private static RemoteViews remoteViews;
	SharedPreferences pref; 


//	@Override
//	public void onReceive(Context context, Intent intent) {
//		// TODO: Implement this method
//		String b = intent.getStringExtra("verse");
//		Log.e("MyBibleVerse **** 36", "onReceive() is called");
//	 }
	 
	 


	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
											 int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		pref = context.getSharedPreferences("verses", 0);
		ComponentName thisWidget = new ComponentName(context,
																							 MyBibleVerse.class);
		int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

		for (int widgetId : allWidgetIds) {
			remoteViews = new RemoteViews(context.getPackageName(),
																		R.layout.widget_layout);
			Log.e("bibleverse", "testing...");

			text = pref.getString("verse", "Bible verses");
			Log.e("MybibleVerse 57", text);
			remoteViews.setTextViewText(R.id.update, text);

			//Register an onClickListener
			Intent intent = new Intent(context, Downloader.class);
			intent.putExtra(COMMAND, "query");
			context.startService(intent);
			PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			remoteViews.setOnClickPendingIntent(R.id.layout, pendingIntent);
			appWidgetManager.updateAppWidget(widgetId, remoteViews);

		 }
	 }

 }
