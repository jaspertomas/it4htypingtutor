package utils;

import java.io.File;

import models.Content;
import models.Contents;
import models.PuzzlePiece;
import models.PuzzlePieces;
import models.Track;
import models.Tracks;
import models.Video;
import models.Videos;
import models.WordCloud;
import models.WordClouds;
import android.app.DownloadManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.intelimina.biofemme.Constants;


public class FileInspector {
	public String getStatusString()
	{
//		ArrayList<String> list=new ArrayList<String> ();
		String string="Downloads:";
		if(started>0)string+="\n"+started+" started";//
		if(running>0)string+="\n"+running+" running";//status+=running+" running, ";
		if(paused>0)string+="\n"+paused+" paused";//status+=paused+" paused, ";
		if(pending>0)string+="\n"+pending+" pending";//status+=pending+" pending, ";
		if(successful>0)string+="\n"+successful+" successful";//status+=successful+" successful, ";
		if(failed>0)string+="\n"+failed+" failed";//status+=failed+" failed, ";
		if(redownloading>0)string+="\n"+redownloading+" redownloading, ";//status+=redownloading+" redownloading, ";
		if(missingnameoruri>0)string+="\n"+missingnameoruri+" missingnameoruri, ";//status+=missingnameoruri+" missingnameoruri, ";
		//reset values for next session 
		started=running=paused=pending=successful=failed=redownloading=missingnameoruri=0;

		return string;
	}
//	public ArrayList<String> getStatusString()
//	{
//		ArrayList<String> list=new ArrayList<String> ();
//		if(started>0)list.add(started+" started, ");//
//		if(running>0)list.add(running+" running, ");//status+=running+" running, ";
//		if(paused>0)list.add(paused+" paused, ");//status+=paused+" paused, ";
//		if(pending>0)list.add(pending+" pending, ");//status+=pending+" pending, ";
//		if(successful>0)list.add(successful+" successful, ");//status+=successful+" successful, ";
//		if(failed>0)list.add(failed+" failed, ");//status+=failed+" failed, ";
//		if(redownloading>0)list.add(redownloading+" redownloading, ");//status+=redownloading+" redownloading, ";
//		if(missingnameoruri>0)list.add(missingnameoruri+" missingnameoruri, ");//status+=missingnameoruri+" missingnameoruri, ";
//		//reset values for next session 
//		started=running=paused=pending=successful=failed=redownloading=missingnameoruri=0;
//
//		return list;
//	}
	private Integer started=0, running=0, paused=0, pending=0, successful=0, failed=0, redownloading=0, missingnameoruri=0;

	private static FileInspector instance=null;
//	private FileInspector() {
//	}
	public static FileInspector getInstance()
	{
		if(instance==null)instance=new FileInspector();
		return instance;
	}
	public boolean inspectAll()
	{
		boolean allok=true;
		boolean ok=true;
		
		//inspect tracks
		//in biofemme, tracks don't have file assets
//		for(Track track:Tracks.getTracks().values())
//		{
//			ok=inspectTrack(track);
//			if(ok==false)allok=false;
//		}

		//inspect wordclouds
		ok=inspectWordClouds();
		ok=inspectContents();
		if(ok==false)allok=false;

		return allok;
	}
	public boolean inspectWordClouds()
	{
		boolean allok=true;
		boolean ok=true;

		//DOWNLOAD WORD CLOUD ASSETS-----------------------
		for(WordCloud cloud:WordClouds.getWordClouds().values())
		{

			//inspect all related files
			ok=inspectFile(
					Constants.SERVER_URL+"/"+cloud.getImage_file_url(),
					cloud.getImage_file_name(),
					"Image for "+cloud.getName()+" wordcloud",
					cloud.getImage_fingerprint(),
					cloud.getImage_file_size()
					);
			if(ok==false)allok=false;
			
		}
		return allok;
	}

	public boolean inspectContents()
	{
		boolean allok=true;
		boolean ok=true;

		//DOWNLOAD CONTENT ASSETS-----------------------
		for(Content content:Contents.getItems().values())
		{

			//inspect all related files
			ok=inspectFile(
					Constants.SERVER_URL+"/"+content.getFile_url(),
					content.getFile_name(),
					content.getName(),
					content.getFile_fingerprint(),
					Long.valueOf(content.getFile_size())
					);
			//inspectFile(String uristring, String filename, String description, String checksum, Long filesize)
			if(ok==false)allok=false;
			
		}
		return allok;
	}	
	public boolean inspectTrack(Track track)
	{
//		SQLiteDatabase db=MyDatabaseHelper.getInstance().getWritableDatabase();
//		Cursor c = db.rawQuery("SELECT * FROM puzzle_pieces where track_id='"+track.getId()+"'", null);

//		int nameindex=c.getColumnIndex("name");
		String name=null;
		
		boolean allok=true;
		boolean ok=true;


		ok=inspectFile(
				Constants.SERVER_URL+"/"+track.getImage_file_url(),
				track.getImage_file_name(),
				"Image for "+track.getName(),
				track.getImage_fingerprint(),
				track.getImage_file_size()
				);
		if(ok==false)allok=false;
		
		
		
		//DOWNLOAD PUZZLE PIECE ASSETS-----------------------
		for(PuzzlePiece piece:PuzzlePieces.getByTrackId(track.getId()))
		{

			Video video;

			//inspect all related files
			ok=inspectFile(
					Constants.SERVER_URL+"/"+piece.getBrochure_path_url(),
					piece.getBrochure_file_name(),
					"Brochure for "+piece.getName()+" puzzle piece",
					piece.getBrochure_fingerprint(),
					piece.getBrochure_file_size()
					);
			if(ok==false)allok=false;
			
			ok=inspectFile(
					Constants.SERVER_URL+"/"+piece.getImage_file_url(),
					piece.getImage_file_name(),
					"Image for "+piece.getName()+" puzzle piece",
					piece.getImage_fingerprint(),
					piece.getImage_file_size()
					);
			if(ok==false)allok=false;
			
			video=Videos.getById(piece.getBrand_video_id());
			ok=inspectFile(
					Constants.SERVER_URL+"/"+video.getFile_path_url(),
					video.getAsset_file_name(),
					"Brand Video: "+video.getName(),
					"",//!!!!!!NO FINGERPRINT AVAILABLE
					video.getAsset_file_size()
					);
			if(ok==false)allok=false;
			
			for(String video_id:piece.getAdvocacy_video_ids())
			{
				video=Videos.getById(video_id);
				if(video==null)
				{
					Log.e("FileInspector: ","Video with id not found: "+video_id);
					break;
				}
				ok=inspectFile(
						Constants.SERVER_URL+"/"+video.getFile_path_url(),
						video.getAsset_file_name(),
						"Advocacy Video: "+video.getName(),
						"",//!!!!!!NO FINGERPRINT AVAILABLE
						video.getAsset_file_size()
						);
				if(ok==false)allok=false;
			}
		}
		
		return allok;
	}

	public boolean inspectFile(String uristring, String filename, String description, String checksum, Long filesize)
	{
		/*if filename is null or uri is null, complain to logcat and do nothing*/
		if(filename==null||filename.isEmpty())
		{
			Log.e("MyDownloadHelper: Download Error","Empty filename for url: "+uristring);
			missingnameoruri++;
			return false;
		}
		else if(uristring==null||uristring.isEmpty())
		{
			Log.e("MyDownloadHelper: Download Error","Empty url for file: "+filename);
			missingnameoruri++;
			return false;
		}
		
		
		Cursor c=null;
		SQLiteDatabase db=MyDatabaseHelper.getInstance().getWritableDatabase();
		String error;

		//get download_id
//		Long download_id=MyDownloadHelper.getInstance().queryDownloadIdByFilename(filename);
		//check if file requested is already being downloaded, using download_id
//		if(download_id!=null)
//			c = MyDownloadHelper.getInstance().getMgr().query(new DownloadManager.Query().setFilterById(download_id));

		c=MyDownloadHelper.getInstance().queryDownloadCursorByFilename(filename);
		
		//if download not in DownloadManager
		if(c==null||c.getCount()==0)
		{
			//look for file in download folder
			File file = new File(MyDownloadHelper.DOWNLOADDIR+"/"+filename);
			//if found
			if(file.exists()) 
			{
				//do checksum
				//if checksum fail
				if(!finalCheck(file.getAbsolutePath(),checksum,filesize))
				{
					//erase file and
					//start download
					MyDownloadHelper.getInstance().redownload(uristring, filename, description);
					Log.e("MyDownloadHelper",": File corrupt, redownloading: "+filename);
					redownloading++;
					return false;
				}
				//else file inspection successful
				else
				{
					Log.e("MyDownloadHelper",": File exists: "+filename);
					successful++;
					return true;
				}
			}
			//else if not found, start download
			else
			{
				//MyDownloadHelper.getInstance().download(uristring, filename, description, checksum, filesize);
				MyDownloadHelper.getInstance().download(uristring, filename, description);
				Log.e("MyDownloadHelper",": Download starting: "+filename);
				started++;
				return false;
			}
		}
		//else act according to status
		else
		{
			c.moveToFirst();
			
			switch (MyDownloadHelper.statusInt(c)) 
			{ 
				case DownloadManager.STATUS_FAILED:
					failed++;
					//report reason failed
					Log.e("MyDownloadHelper",": Download failed: "+filename+": "+MyDownloadHelper.reasonString(c));
					error="Download failed: "+filename+": "+MyDownloadHelper.reasonString(c);
					try{
						db.execSQL("INSERT INTO errors (id, location, description) VALUES (NULL, NULL, '"+error.replace("'", "''")+"');");
					} catch (Exception e) {
						Log.e("MyDownloadHelper: Database Error","Cannot insert to error table: "+error);
			        }	
					return false;
	
				case DownloadManager.STATUS_PAUSED:
					paused++;
					//report reason paused
					Log.e("MyDownloadHelper",": Download paused: "+filename+": "+MyDownloadHelper.reasonString(c));
					error="Download failed: "+filename+": "+MyDownloadHelper.reasonString(c);
					try{
						db.execSQL("INSERT INTO errors (id, location, description) VALUES (NULL, NULL, '"+error.replace("'", "''")+"');");
					} catch (Exception e) {
						Log.e("MyDownloadHelper: Database Error","Cannot insert to error table: "+error);
			        }	
					return false;
	
				case DownloadManager.STATUS_PENDING:
					pending++;
					//do nothing
					Log.e("MyDownloadHelper",": Download pending: "+filename);
					return false;
				case DownloadManager.STATUS_RUNNING:
					running++;
					//do nothing
					Log.e("MyDownloadHelper",": Download running: "+filename);
					return false;
				case DownloadManager.STATUS_SUCCESSFUL:
					//do final check
					boolean result=finalCheck(MyDownloadHelper.DOWNLOADDIR+"/"+filename, checksum, filesize);
					if(result==true)
					{
						Log.e("MyDownloadHelper",": Download complete: "+filename);
						successful++;
						return true;
					}
					else 
					{
						Log.e("MyDownloadHelper",": Download corrupt, redownloading: "+filename);
						MyDownloadHelper.getInstance().redownload(uristring, filename, description);
						redownloading++;
						return false;
					}
				default: 
					//this will not happen
					Log.e("MyDownloadHelper",": Download unknown status: "+filename);
					return false;
			}
		}
	}
	private boolean finalCheck(String filename, String checksum, Long filesize)
	{
//		return checksum(filename, checksum);
		return checksize(filename, filesize);
	}
	private boolean checksize(String filename, Long filesize)
	{
		File file=new File(filename);
		return file.length()==filesize; 
	}
	private boolean checksum(String filename, String checksum)
	{
		//checksum file
		String calc_checksum=MyFileHelper.getMD5(filename);
		return calc_checksum.contentEquals(checksum);
	}

	//tracks:			
//	",`name` varchar(255)  " +
//	",`description` varchar(255) "+
//	",`video_id` varchar(40) " + //added Sep 5
//	",`image_file_name` varchar(40) " + //added Sep 5
//	",`image_content_type` varchar(30) " + //added Sep 5
//	",`image_file_size` long" + //added Sep 5
//	",`image_file_url` varchar(255)" + //added Sep 5
//	",`image_fingerprint` varchar(32)" +  //added Sep 5
	
//puzzles
//	",`name` varchar(255)  " +//added Sep 5
//	",`track_id` varchar(40) " + 
//	",`line` integer " + //added Sep 5
//	",`word` varchar(255) " +                 //removed Sep 5
//	",`brand_video_id` varchar(40) " + 
//
//	",`brochure_file_name` varchar(40) " + 
//	",`brochure_content_type` varchar(30) " + 
//	",`brochure_file_size` long" + 
//	",`brochure_path_url` varchar(255)" + //added Sep 5
//	",`brochure_fingerprint` varchar(32)" +  //added Sep 5
//	
//	",`image_file_name` varchar(40) " + //added Sep 5
//	",`image_content_type` varchar(30) " + //added Sep 5
//	",`image_file_size` long" + //added Sep 5
//	",`image_file_url` varchar(255)" + //added Sep 5
//	",`image_fingerprint` varchar(32)" +  //added Sep 5
//	
//	",`advocacy_video_id` varchar(40) " + 
//	",`ordinality` integer " + 
}
