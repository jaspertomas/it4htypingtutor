package utils;

import models.Apps;
import models.BusinessUnits;
import models.ContentSets;
import models.Contents;
import models.CustomTrackContents;
import models.CustomTracks;
import models.DoctorCustomTracks;
import models.DoctorSessionDetails;
import models.DoctorSessions;
import models.Employees;
import models.Notes;
import models.Users;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "unilab_edetailing";
	private static final int latestVersion=25;
	private static MyDatabaseHelper instance;
	public static void initialize(Context context)
	{
		instance=new MyDatabaseHelper(context);
	}
	public static MyDatabaseHelper getInstance()
	{
		return instance;
	}

	private MyDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, latestVersion);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		// CHARACTER SET utf8 COLLATE utf8_general_ci
		database.execSQL("CREATE TABLE IF NOT EXISTS log_entries (`id` INTEGER PRIMARY KEY" +
				",`action` varchar(20) " +
				",`entity` varchar(20) " +
				",`entity_uuid` varchar(50) " +
				",`new_or_updated_attributes` text );");
		database.execSQL("CREATE TABLE IF NOT EXISTS videos (`id` varchar(40) PRIMARY KEY" +
				",`name` varchar(255) " +
				",`description` varchar(255) " +
				",`file_path_url` varchar(255) " +
				",`asset_file_name` varchar(40) " +
				",`asset_content_type` varchar(30)" +
				",`asset_file_size` long" +
				",`asset_fingerprint` varchar(32)" +  //added Sep 5
				",`download_id` long" +
				",`status` varchar(10) );");
		database.execSQL("CREATE TABLE IF NOT EXISTS settings (`id` int PRIMARY KEY" +
				",`name` varchar(100) " +
				",`value` varchar(255) );");
//		database.execSQL("CREATE TABLE IF NOT EXISTS track_memberships (`id` varchar(40)  PRIMARY KEY" +
//				",`track_id` varchar(40)  " +
//				",`doctor_id` varchar(40) " +
//				",`last_watched_video_id` varchar(40) );");		
		database.execSQL("CREATE TABLE IF NOT EXISTS tracks (`id` varchar(40)  PRIMARY KEY" +
				",`name` varchar(255)  " +
				",`description` varchar(255) "+
				",`video_id` varchar(40) " + //added Sep 5
				",`image_file_name` varchar(40) " + //added Sep 5
				",`image_content_type` varchar(30) " + //added Sep 5
				",`image_file_size` long" + //added Sep 5
				",`image_file_url` varchar(255)" + //added Sep 5
				",`image_fingerprint` varchar(32)" +  //added Sep 5
				" );");	
		database.execSQL("CREATE TABLE IF NOT EXISTS doctors (`id` varchar(40)  PRIMARY KEY" +
				",`name` varchar(255)  " +
				",`specialization` varchar(255)" +	
				",`track_ids` varchar(255) "+//added Sep 5//!!!this will have the effect of placing the limit of tracks to 255/36
				" );");	  
		database.execSQL("CREATE TABLE IF NOT EXISTS word_clouds (`id` varchar(40)  PRIMARY KEY" +
				",`name` varchar(255)  " +
				",`keyword` varchar(50) " + 
				",`image_file_name` varchar(40) " + 
				",`image_content_type` varchar(30) " + 
				",`image_file_size` long" + 
				",`image_file_url` varchar(255)" + 
				",`image_fingerprint` varchar(32)" +  
				" );");	  
		database.execSQL("CREATE TABLE IF NOT EXISTS tags (`id` varchar(40)  PRIMARY KEY" +
				",`name` varchar(255)  " +
				",`video_ids` varchar(255) "+//!!!this will have the effect of placing the limit of videos to 255/36
				" );");	  
		database.execSQL("CREATE TABLE IF NOT EXISTS puzzle_pieces (`id` varchar(40)  PRIMARY KEY" +
				",`name` varchar(255)  " +//added Sep 5
				",`track_id` varchar(40) " + 
				",`line` integer " + //added Sep 5
				",`word` varchar(255) " +                 //removed Sep 5
				",`brand_video_id` varchar(40) " + 

				",`brochure_file_name` varchar(40) " + 
				",`brochure_content_type` varchar(30) " + 
				",`brochure_file_size` long" + 
				",`brochure_path_url` varchar(255)" + //added Sep 5
				",`brochure_fingerprint` varchar(32)" +  //added Sep 5
				
				",`image_file_name` varchar(40) " + //added Sep 5
				",`image_content_type` varchar(30) " + //added Sep 5
				",`image_file_size` long" + //added Sep 5
				",`image_file_url` varchar(255)" + //added Sep 5
				",`image_fingerprint` varchar(32)" +  //added Sep 5
				
				",`advocacy_video_id` varchar(40) " + 
				",`ordinality` integer " + 
				",`coordinates` varchar(20) " + 
				" );");	  
//		database.execSQL("CREATE TABLE IF NOT EXISTS content_sets (" +
//				"`id` varchar(40)  PRIMARY KEY" +
//				",`track_id` varchar(40)  " +
//				",`brand_video_id` varchar(40) " +
//				",`word` varchar(255) " +                 //removed Sep 5
//				",`brochure_file_name` varchar(40) " +
//				",`brochure_content_type` varchar(30) " +
//				",`brochure_file_size` long" +
//				",`advocacy_video_id` varchar(40)  " +
//				",`ordinality` int);");	
		database.execSQL("CREATE TABLE IF NOT EXISTS views (`id` varchar(40)  PRIMARY KEY" +
				",`video_id` varchar(40) " + 
				",`doctor_id` varchar(40) " + 
				",`time_stopped` varchar(255)  " +
				",`word` varchar(255) "+
				",`signature_path` varchar(255) "+
				" );");	  
		database.execSQL("CREATE TABLE IF NOT EXISTS downloads (`id` integer PRIMARY KEY" +
				",`filename` varchar(50) " + 
				",`url` varchar(255)  " +
				",`download_id` long "+
				",`checksum` varchar(40) "+
				" );");	  
		database.execSQL("CREATE TABLE IF NOT EXISTS errors (`id` integer PRIMARY KEY" +
				",`location` varchar(50) " + 
				",`description` varchar(255)  " +
				" );");	  
		database.execSQL("CREATE TABLE IF NOT EXISTS doctor_videos (`id` integer PRIMARY KEY" +
			",doctor_id varchar(40) " +
			",video_id varchar(40) " +
			",position integer " +
			",completed tinyint " +
			" );");
		database.execSQL("CREATE TABLE IF NOT EXISTS doctor_puzzle_pieces (`id` integer PRIMARY KEY" +
			",doctor_id varchar(40) " +
			",puzzle_piece_id varchar(40) " +
			",opened tinyint " +
			",brandvideocompleted tinyint " +
			",advocacyvideoscompleted integer " +
			",brochurecompleted tinyint " +
			",wordcloudcompleted tinyint " +
			",completed tinyint " +
			",wordchoice varchar(30) " +
			" );");
		database.execSQL("CREATE TABLE IF NOT EXISTS doctor_tracks (`id` integer PRIMARY KEY" +
			",doctor_id varchar(40) " +
			",track_id varchar(40) " +
			",completed tinyint " +
			" );");		
		database.execSQL("CREATE TABLE IF NOT EXISTS view_updates (`id` integer PRIMARY KEY" +//auto_increment 
			",video_id varchar(40) " +
			",doctor_id varchar(40) " +
			",word varchar(20) " +
			",time_stopped integer " +
			" );");
		onUpgrade( database,  1,  latestVersion);


/*
DoctorVideoData
doctor_id varchar(40)
video_id varchar(40)
position integer
complete boolean ?

DoctorPuzzlePieceData
doctor_id varchar(40)
puzzle_piece_id varchar(40)
opened boolean
brandvideocomplete boolean
advocacyvideocomplete boolean
brochurecomplete boolean
complete boolean

DoctorTrackData
doctor_id varchar(40)
track_id varchar(40)
completed boolean
 * */		
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
//		database.execSQL("DROP TABLE IF EXISTS log_entries");
//		database.execSQL("DROP TABLE IF EXISTS videos");
//		database.execSQL("DROP TABLE IF EXISTS settings");
//		//database.execSQL("DROP TABLE IF EXISTS content_sets");
//		//database.execSQL("DROP TABLE IF EXISTS track_memberships");
//		database.execSQL("DROP TABLE IF EXISTS tracks");
//		database.execSQL("DROP TABLE IF EXISTS doctors");
//		database.execSQL("DROP TABLE IF EXISTS word_clouds");
//		database.execSQL("DROP TABLE IF EXISTS tags");
//		database.execSQL("DROP TABLE IF EXISTS puzzle_pieces");
//		database.execSQL("DROP TABLE IF EXISTS views");
//		database.execSQL("DROP TABLE IF EXISTS downloads");
//		database.execSQL("DROP TABLE IF EXISTS errors");
//		database.execSQL("DROP TABLE IF EXISTS doctor_videos");
//		database.execSQL("DROP TABLE IF EXISTS doctor_puzzle_pieces"); 
//		database.execSQL("DROP TABLE IF EXISTS doctor_tracks"); 
//		database.execSQL("DROP TABLE IF EXISTS view_updates");
//		database.execSQL("DROP TABLE IF EXISTS custom_tracks");
//		database.execSQL("DROP TABLE IF EXISTS custom_track_details");
//			onCreate(database);

		
		if(oldVersion<8)
		{
//			database.execSQL("CREATE TABLE IF NOT EXISTS custom_tracks (`id` integer PRIMARY KEY" +
//					",name varchar(100) " +
//					" );");		
			database.execSQL(CustomTracks.createTable());		
//			database.execSQL("CREATE TABLE IF NOT EXISTS custom_track_contents (`id` integer auto_increment PRIMARY KEY" +
//				",custom_track_id integer " +
//				",type varchar(40) " +
//				",uuid varchar(40) " +
//				",order integer " +
//				" );");			
			database.execSQL(CustomTrackContents.createTable());		
		}
		
		if(oldVersion<9)
		{
			database.execSQL(DoctorCustomTracks.createTable());		
	    }		
		if(oldVersion<10)
		{
//			database.execSQL("ALTER TABLE "+DoctorCustomTracks.dbname+" ADD COLUMN viewed_content_id text ");
	    }		
		if(oldVersion<11)
		{
			database.execSQL(DoctorSessions.createTable());		
			database.execSQL(DoctorSessionDetails.createTable());		
		}	
		if(oldVersion<13)
		{
			database.execSQL(Apps.createTable());		
		}		
		if(oldVersion<15)
		{
			database.execSQL(Users.createTable());		
		}	
		if(oldVersion<17)
		{
			database.execSQL(Notes.createTable());		
		}	
		if(oldVersion<20)
		{
			database.execSQL(Contents.createTable());		
		}	
		if(oldVersion<21)
		{
			database.execSQL(BusinessUnits.createTable());		
		}	
		if(oldVersion<22)
		{
			database.execSQL("ALTER TABLE doctors ADD COLUMN `bu_ids` varchar(255)  ");
	    }		
		if(oldVersion<23)
		{
//			database.execSQL("ALTER TABLE "+Contents.dbname+" ADD COLUMN `bu_ids` varchar(255)  ");
	    }		
		if(oldVersion<24)
		{
			database.execSQL(ContentSets.createTable());		
	    }		
		if(oldVersion<25)
		{
			database.execSQL(Employees.createTable());		
	    }		
	}
	public void emptyDatabase() {
		SQLiteDatabase database=instance.getWritableDatabase();
//		

		
//		database.execSQL("delete from doctors;");
//		database.execSQL("delete from videos;");
//		database.execSQL("delete from tracks;");
//		database.execSQL("delete from puzzle_pieces;");
//		database.execSQL("delete from log_entries;");
//		database.execSQL("delete from tags;");
//		database.execSQL("delete from word_clouds;");
//		database.execSQL("delete from views;");
//		database.execSQL("delete from downloads;");
//		database.execSQL("delete from errors;");
//		database.execSQL("delete from doctor_videos;");
//		database.execSQL("delete from doctor_puzzle_pieces;"); 
//		database.execSQL("delete from doctor_tracks;"); 
//		database.execSQL("delete from view_updates;");
//		database.execSQL("delete from custom_tracks;");
//		database.execSQL("delete from custom_track_contents;");
//		database.execSQL("delete from doctor_custom_tracks;");
//		database.execSQL("delete from doctor_sessions;");
//		database.execSQL("delete from doctor_session_details;");
//		database.execSQL("delete from apps;");
////		database.execSQL("delete from users;");
//		database.execSQL("delete from notes;");
//		database.execSQL("delete from contents;");
		
//		Doctors.reset();
//		Videos.reset();
//		Tracks.reset();
//		PuzzlePieces.reset();
////		LogEntries.reset();
////		Tags.reset();
//		WordClouds.reset();
////		Views.reset();
////		Downloads.reset();
////		Errors.reset();
//		DoctorVideos.reset();
//		DoctorPuzzlePieces.reset();
//		DoctorTracks.reset();
//		ViewUpdates.reset();
//		CustomTracks.reset();
//		CustomTrackContents.reset();
//		DoctorCustomTracks.reset();
//		DoctorSessions.reset();
//		DoctorSessionDetails.reset();
//		Apps.reset();
////		Users.reset();
//		Notes.reset();
//		Contents.reset();

			database.close();
		
		
//		for(DoctorCustomTrack dt:DoctorCustomTracks.getItems().values())
//			dt.delete();
//		DoctorCustomTracks.reset();

//		database.execSQL("update puzzle_pieces set brochure_file_name=\"amiabel_e_brochure.pdf\"");

//		for(DoctorCustomTrack track:DoctorCustomTracks.getItems().values())
//		{
//			track.setViewed_content_id("53bb13af-d440-4857-8970-82aba7c5f16b");
//			track.save();
//		}


//		database.execSQL(DoctorSessions.deleteTable());		
//		database.execSQL(DoctorSessionDetails.deleteTable());		
//		database.execSQL(DoctorSessions.createTable());		
//		database.execSQL(DoctorSessionDetails.createTable());		

//		database.execSQL("delete from log_entries;");
//		
//		database.execSQL(Notes.deleteTable());		
//		database.execSQL(Notes.createTable());		
//		database.close();

		
//		Log.e("",String.valueOf(Apps.getItems().size()));
//		App app=new App();
//		app.setName("dummy");
//		app.setId("yo");
//		app.setComponentname("baabaablacksheep");
//		app.save();
//		Apps.reset();
//		Log.e("",String.valueOf(Apps.getItems().size()));
//		app=Apps.getByName("dummy");
//		Log.e("",app.getComponentname());
//		app.setComponentname("haveyouanywool");
//		app.save();
//		Apps.reset();
//		app=Apps.getByName("dummy");
//		Log.e("",app.getComponentname());
//		app.delete();
	}
}