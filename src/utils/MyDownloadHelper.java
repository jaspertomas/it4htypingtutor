package utils;

import java.io.File;
import java.util.HashMap;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class MyDownloadHelper {
	private DownloadManager mgr = null;
	private long download_id = -1L;
	
//0------
	
	private static MyDownloadHelper instance;
	public static void initialize(Context context)
	{
		instance=new MyDownloadHelper(context);
	}
	public static MyDownloadHelper getInstance()
	{
		return instance;
	}
	private MyDownloadHelper(Context activity){
//		this.activity=activity;
		mgr = (DownloadManager) activity.getSystemService(activity.DOWNLOAD_SERVICE);
//		activity.registerReceiver(onComplete, new IntentFilter(
//				DownloadManager.ACTION_DOWNLOAD_COMPLETE));
		activity.registerReceiver(onNotificationClick, new IntentFilter(
				DownloadManager.ACTION_NOTIFICATION_CLICKED));

		//create download directory
//		Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdirs();
//        DOWNLOADDIR= new File("/unilab_edetailing");
		DOWNLOADDIR=Environment.getExternalStoragePublicDirectory(DOWNLOADPATH);
        if (!DOWNLOADDIR.exists())	DOWNLOADDIR.mkdirs();
        
		
	}

	
	//--------
	
	public DownloadManager getMgr(){return mgr;}
	public long getLastDownload(){return download_id;}
	public void setLastDownload(long lastDownload){this.download_id=lastDownload;}

	public static String DOWNLOADPATH="unilab_edetailing";
	public static File DOWNLOADDIR=null;

	public HashMap<String,String> queryStatus(long download_id) {
		HashMap<String,String> stringhash=new HashMap<String,String>(); 
	
		
		Cursor c = mgr.query(new DownloadManager.Query()
		.setFilterById(download_id));

		if (c == null || c.getCount()==0) {
			stringhash.put("error", "Download not found!");
			return stringhash;
		} else {
			
			// go to first record in result
			c.moveToFirst();
		
//			Log.e(getClass().getName(),
//					"COLUMN_ID: "
//							+ c.getLong(c
//									.getColumnIndex(DownloadManager.COLUMN_ID)));
			Long downloadedsofar=c.getLong(c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
			Long totalsize=c.getLong(c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
			Long progress=(downloadedsofar*100/totalsize);			
		
//			
//			Log.e(getClass().getName(),
//					"Free Space in MB: "
//							+ calcFreeSpaceMB().toString());
//			
//			Log.e(getClass().getName(),
//					"COLUMN_TOTAL_SIZE_BYTES: "
//							+ totalsize.toString());
//			Log.e(getClass().getName(),
//					"COLUMN_BYTES_DOWNLOADED_SO_FAR: "
//							+ downloadedsofar.toString());
//			Log.e(getClass().getName(),
//					"COLUMN_LAST_MODIFIED_TIMESTAMP: "
//							+ c.getLong(c
//									.getColumnIndex(DownloadManager.COLUMN_LAST_MODIFIED_TIMESTAMP)));
//		
//			Log.e(getClass().getName(),
//					"COLUMN_LOCAL_URI: "
//							+ c.getString(c
//									.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)));



			stringhash.put("progress", progress.toString()+"%");
			stringhash.put("reason", reasonString(c));
			stringhash.put("status", statusMessage(c));
			return stringhash;

		
		}
	}
	
//	public Long download(String uristring, String filename){
//		return download(uristring, filename, "");
//	}
	
	public Long redownload(String uristring, String filename, String description)
	{
		File file = new File(MyDownloadHelper.DOWNLOADDIR+"/"+filename);
		file.delete();
		//save download to database
		SQLiteDatabase db=MyDatabaseHelper.getInstance().getWritableDatabase();
		try{
			db.execSQL("delete from downloads where filename='"+filename+"';");
		} catch (Exception e) {
			Log.e("MyDownloadHelper: Database Error","Cannot delete download from database");
        }	
		return download( uristring,  filename,  description);
	}
//	
//	public Long download(String uristring, String filename, String description){
//		
//		//check database if file requested is already being downloaded
//		Cursor c=null;
//		Long download_id=queryDownloadIdByFilename(filename);
//		if(download_id!=null)
//			c = mgr.query(new DownloadManager.Query().setFilterById(download_id));
//
//		//if download not in DownloadManager
//		if(c==null||c.getCount()==0)
//		{
//			//look for file in download folder
//			File file = new File(DOWNLOADDIR+"/"+filename);
//			//if found
//			if(file.exists()) 
//			{
//				//do checksum
//				//if checksum fail
//				{
//					//erase file and
//					//start download
//				}
//				//else do nothing
//				return null;//placeholder
//			}
//			//else if not found, start download
//			else return processDownload( uristring,  filename,  description);
//		}
//		//else act according to status
//		else
//		{
//			c.moveToFirst();
//			
//			switch (statusInt(c)) { 
//			case DownloadManager.STATUS_FAILED:
//				//report reason failed
//				Log.e("MyDownloadHelper",": Download failed: "+filename+": "+reasonString(c));
//				return null;
//
//			case DownloadManager.STATUS_PAUSED:
//				//report reason paused
//				Log.e("MyDownloadHelper",": Download paused: "+filename+": "+reasonString(c));
//				return null;
//
//			case DownloadManager.STATUS_PENDING:
//				//do nothing
//				Log.e("MyDownloadHelper",": Download pending: "+filename);
//				return null;
//			case DownloadManager.STATUS_RUNNING:
//				//do nothing
//				Log.e("MyDownloadHelper",": Download running: "+filename);
//				return null;
//			case DownloadManager.STATUS_SUCCESSFUL:
//				Log.e("MyDownloadHelper",": Download complete: "+filename);
//				//checksum file
//				String calc_checksum=MyFileHelper.getMD5(DOWNLOADDIR+"/"+filename);
//				//if fail, 
//				if(!calc_checksum.contentEquals(checksum))
//				{
//					//save to error table and restart download?
//				}
//				//else do nothing
//				return null;
//			default: 
//				//this will not happen
//				Log.e("MyDownloadHelper",": Download unknown status: "+filename);
//				return null;
//			}		
//		}
//		
//		
//	}
	public Long download(String uristring, String filename, String description)
	{
		Uri uri = Uri.parse(uristring);
		download_id=mgr.enqueue(new DownloadManager.Request(uri)
		.setAllowedNetworkTypes(
				DownloadManager.Request.NETWORK_WIFI
				| DownloadManager.Request.NETWORK_MOBILE)
		.setAllowedOverRoaming(false)
		.setTitle(filename)
		.setDescription(description)
		.setDestinationInExternalPublicDir(DOWNLOADPATH, filename)
				);		

		//save download to database
		SQLiteDatabase db=MyDatabaseHelper.getInstance().getWritableDatabase();
		try{
			db.execSQL(
					"INSERT INTO downloads (url ,filename ,download_id )VALUES (" +//, checksum
	        				"'"+uristring
	        				+"','"
	        				+filename
	        				+"','"
	        				+download_id
//	        				+"','"
//	        				+checksum
	        				+"');");
		} catch (Exception e) {
			Log.e("MyDownloadHelper: Database Error","Cannot save download data to database");
        }	
		
		return download_id;
	}
	public Integer queryStatusByFilename(String filename)
	{
		Long download_id=queryDownloadIdByFilename(filename);
		Cursor c = mgr.query(new DownloadManager.Query().setFilterById(download_id));
		if(c==null||c.getCount()==0)return null;
		c.moveToFirst();
//		return statusMessage(c);
		return statusInt(c);

//				Long downloadedsofar=c.getLong(c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
//				Long totalsize=c.getLong(c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
//				Long progress=(downloadedsofar*100/totalsize);
//				stringhash.put("progress", progress.toString()+"%");
//				stringhash.put("reason", reasonString(c));
//				stringhash.put("status", statusMessage(c));
	}
	public Integer queryStatusByUrl(String url)
	{
		Long download_id=queryDownloadIdByUrl(url);
		Cursor c = mgr.query(new DownloadManager.Query().setFilterById(download_id));
		if(c==null||c.getCount()==0)return null;
		c.moveToFirst();
//		return statusMessage(c);
		return statusInt(c);
	}
	public Cursor queryDownloadCursorByFilename(String filename)
	{
		Long download_id=queryDownloadIdByFilename(filename);
		if(download_id==null)
		{
			return null;
		}
		//get android download manager cursor
		Cursor c2= MyDownloadHelper.getInstance().getMgr().query(new DownloadManager.Query().setFilterById(download_id));
		if(c2==null)
		{
			SQLiteDatabase db=MyDatabaseHelper.getInstance().getWritableDatabase();
    		db.execSQL("delete from downloads where id = '"+download_id+"';");	 
    		return null;
		}
		return c2;
	}
	private Long queryDownloadIdByFilename(String filename)
	{
		SQLiteDatabase db=MyDatabaseHelper.getInstance().getWritableDatabase();
		Cursor c = db.rawQuery("SELECT download_id FROM downloads where filename = '"+filename+"'", null);
		if(c.getCount()==0)return null;
		c.moveToFirst();
		return c.getLong(0);
	}
	private Long queryDownloadIdByUrl(String url)
	{
		SQLiteDatabase db=MyDatabaseHelper.getInstance().getWritableDatabase();
		Cursor c = db.rawQuery("SELECT download_id FROM downloads where url = '"+url+"'", null);
		if(c.getCount()==0)return null;
		c.moveToFirst();
		return c.getLong(0);
	}
	
	public void destroy(Activity activity) {

		//unregisterReceiver(onComplete);
		activity.unregisterReceiver(onNotificationClick);
	}	
	
//	BroadcastReceiver onComplete = new BroadcastReceiver() {
//		public void onReceive(Context ctxt, Intent intent) {
//			activity.findViewById(R.id.start).setEnabled(true);
//		}
//	};

	BroadcastReceiver onNotificationClick = new BroadcastReceiver() {
		public void onReceive(Context ctxt, Intent intent) {
			Toast.makeText(ctxt, "Ummmm...hi!", Toast.LENGTH_LONG).show();
		}
	};
	
	public final String[] STATUSLIST = { "0", "Pending", "Running", "3", "Paused",
			"5", "6", "7", "Successful", "9", "10", "11", "12", "13",
			"14", "15", "Failed" };

	public static Integer statusInt(Cursor c) {
		return c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
	}

	private String statusMessage(Cursor c) {
		String msg = "???";

		switch (statusInt(c)) {
		case DownloadManager.STATUS_FAILED:
			msg = "Download failed!";
			break;

		case DownloadManager.STATUS_PAUSED:
			msg = "Download paused!";
			break;

		case DownloadManager.STATUS_PENDING:
			msg = "Download pending!";
			break;

		case DownloadManager.STATUS_RUNNING:
			msg = "Download in progress!";
			break;

		case DownloadManager.STATUS_SUCCESSFUL:
			msg = "Download complete!";
			break;

		default:
			msg = "Download is nowhere in sight";
			break;
		}

		return (msg);
	}
	
	public static final String[] REASONLISTPAUSED = { "0", "Waiting to Retry",
			"Waiting for Network", "Queued for Wi-Fi", "Unknown", "5",
			"6", "7", "8", "9", "10", "11", "12", "13", "14", "15",
			"16" };
	public static final String[] REASONLISTFAILED = { "Unknown", "File Error",
			"Unhandled HTTP Code", "3", "HTTP Data Error",
			"Too Many Redirects", "Insufficient Space",
			"Storage Device Not Found", "Cannot Resume",
			"File Already Exists", "10", "11", "12", "13", "14", "15",
			"16" };		
	public static Integer reasonInt(Cursor c) {
		return c.getInt(c.getColumnIndex(DownloadManager.COLUMN_REASON));
	}
	public static String reasonString(Cursor c) {
		String msg = "???";

		//get reason index
		Integer statusint=statusInt(c);
		Integer reasonint=reasonInt(c);
		//interpret reason index depending on status
		if (statusint == DownloadManager.STATUS_PAUSED)
			msg = REASONLISTPAUSED[reasonint];
		else if (statusint == DownloadManager.STATUS_FAILED)
			msg = REASONLISTFAILED[reasonint - 1000];			
		
		return (msg);
	}	

//	private void query()
//	{
//		Cursor c = mgr.query(new DownloadManager.Query().setFilterById(1));
//	}
}
