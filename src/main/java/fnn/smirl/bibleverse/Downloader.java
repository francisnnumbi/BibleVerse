package fnn.smirl.bibleverse;

import android.os.*;
import android.app.*;
import android.content.*;
import com.android.volley.*;
import com.android.volley.toolbox.*;
import android.util.*;
import android.appwidget.*;
import java.util.*;

public class Downloader extends IntentService {
	static final long SECOND = 1000;
	static final long MINUTE = 60 * SECOND;

	public static final int 
	STATUS_RUNNING = 0,
	STATUS_FINISHED = 1,
	STATUS_ERROR = 2;
	ResultReceiver receiver;
	static Downloader downloader;
	
	boolean doIt = true;
	public static final String URL = "http://labs.bible.org/api/?passage=random&formatting=plain";
	public static final String CONNECTION_FAILIURE = "Pas de connexion au serveur...";
	SharedPreferences prefs;
	SharedPreferences.Editor editor;
	Context context;

	private String text = "";

	public Downloader() {
		super("BibleVerse");
		downloader = this;
		
		
	 }

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO: Implement this method
		pause(5*SECOND);
		context = getBaseContext();
		prefs = context.getSharedPreferences("verses", 0);
		editor = prefs.edit();
		 receiver = intent.getParcelableExtra("receiver");
		 Log.e("bibleservice 43", "service started again...");
		Bundle b = intent.getExtras();
		String command = b.getString("command");
		
		if (command.equals("query")) {
			
			try {
				// run download
				update();
			 } catch (Exception e) {
		
			 }
		 }

			this.stopSelf();
		 }
	 

	// download it
	public void update() {
		// TODO: Implement this method

		RequestQueue queue = Volley.newRequestQueue(context);

		StringRequest request = new StringRequest(Request.Method.GET, URL,
		 new Response.Listener<String>(){

			 @Override
			 public void onResponse(String p1) {
				 // TODO: Implement this method
				 try {
					 text = p1;
					 Log.e("bibleservice", text);
					 editor.putString("verse", text);
					 editor.commit();
					 updateMyBible();
					} catch (Exception e) {
					 text = e.toString();
					 Log.e("bibleservice error", "159 >> " + text);
					}
				}

			 private void updateMyBible() {
				 // TODO: Implement this method
				 Intent intent = new Intent(context, MyBibleVerse.class);
				 intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
				 int[] ids = {R.id.update};
				 intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
				 intent.putExtra("verse", text);
				 context.sendBroadcast(intent);
				}
			}, new Response.ErrorListener(){

			 @Override
			 public void onErrorResponse(VolleyError p1) {
				 // TODO: Implement this method
				 text = CONNECTION_FAILIURE;
				 Log.e("bibleservice", text);
				}
			});
		queue.add(request);
	 }
	 
	 private void pause(long millis){
		 try {
			 Thread.sleep(millis);
			} catch (InterruptedException e) {}
		}
 }
