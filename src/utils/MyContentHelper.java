package utils;

import java.util.ArrayList;
import java.util.HashMap;

import models.App;
import models.Apps;
import models.Content;
import models.Contents;
import models.CustomTrackContent;
import models.CustomTrackContents;
import models.DoctorCustomTrack;
import models.PuzzlePiece;
import models.PuzzlePieces;
import models.Video;
import models.Videos;
import views.customlist.CustomListBaseAdapter;
import views.customlist.CustomListItem;

public class MyContentHelper {
	public static void reloadexistingtrack(ArrayList<CustomListItem> chosen, String track_id, DoctorCustomTrack dt)
	{
		//load an existing track

		ArrayList<CustomTrackContent> contents= CustomTrackContents.getByCustomTrackId(track_id);

		//stretch chosen list if necessary
//		chosen.ensureCapacity(contents.size());
		
		//arrange contents according to sort
		HashMap<Integer,CustomTrackContent> map=new HashMap<Integer,CustomTrackContent>(); 

		for(CustomTrackContent content:contents)
		{
			map.put(content.getSort(), content);
		}
		
		
		//fill the chosen list with this track's contents
		Video video;
		PuzzlePiece piece;
		CustomTrackContent content;
		CustomListItem item;
		for(Integer key:map.keySet())
		{
			content=map.get(key);
			piece=PuzzlePieces.getById(content.getPuzzle_piece_id());
			if(content.getType().contentEquals("brandvideo"))
			{
				video=Videos.getById(content.getUuid());
				item=newCustomListBrandVideo(piece,video);
				if(dt!=null && dt.getViewed_content_id().contains(content.getUuid()))
					item.setChecked(true);
				chosen.add(item); 
			}
			else
			if(content.getType().contentEquals("advocacyvideo"))
			{
				video=Videos.getById(content.getUuid());
				item=newCustomListAdvocacyVideo(piece,video);
				if(dt!=null && dt.getViewed_content_id().contains(content.getUuid()))
					item.setChecked(true);
				chosen.add(item); 
			}
			else
			if(content.getType().contentEquals("brochure"))
			{
				item=newCustomListBrochure(piece);
				if(dt!=null && dt.getViewed_content_id().contains(content.getUuid()))
					item.setChecked(true);
				chosen.add(item); 
			}
			else
			if(content.getType().contentEquals("app"))
			{
				item=newCustomListApp(Apps.getById(content.getPuzzle_piece_id()));
				if(dt!=null && dt.getViewed_content_id().contains(content.getUuid())) 
					item.setChecked(true);
				chosen.add(item); 
			}
			else
			if(
					content.getType().contentEquals("BrochureContent")
					||
					content.getType().contentEquals("ApkContent")
					||
					//videocontent backward compatibility
					content.getType().contentEquals("VideoContent")
					||
					content.getType().contentEquals("BrandVideoContent")
					||
					content.getType().contentEquals("AdvocacyVideoContent")
					||
					content.getType().contentEquals("ImageContent")
			)
			{
				item=newCustomListContent(Contents.getById(content.getUuid()));
				if(dt!=null && dt.getViewed_content_id().contains(content.getUuid())) 
					item.setChecked(true);
				chosen.add(item); 
			}				
//			else
//			if(content.getType().contentEquals("ApkContent"))
//			{
//				item=newCustomListApp(Apps.getById(content.getPuzzle_piece_id()));
//				if(dt!=null && dt.getViewed_content_id().contains(content.getUuid())) 
//					item.setChecked(true);
//				chosen.add(item); 
//			}				
//			else
//			if(content.getType().contentEquals("VideoContent"))
//			{
//				item=newCustomListApp(Apps.getById(content.getPuzzle_piece_id()));
//				if(dt!=null && dt.getViewed_content_id().contains(content.getUuid())) 
//					item.setChecked(true);
//				chosen.add(item); 
//			}				
//			else
//			if(content.getType().contentEquals("ImageContent"))
//			{
//				item=newCustomListApp(Apps.getById(content.getPuzzle_piece_id()));
//				if(dt!=null && dt.getViewed_content_id().contains(content.getUuid())) 
//					item.setChecked(true);
//				chosen.add(item); 
//			}				
		}
	}
	public static CustomListItem newCustomListContent(Content content) {
		// TODO Auto-generated method stub
		Integer icon=0;
		if(content.getType().contentEquals("ImageContent"))
			icon=CustomListBaseAdapter.IMAGE;
		else if(content.getType().contentEquals("BrochureContent"))
			icon=CustomListBaseAdapter.PDF;
		else if(content.getType().contentEquals("VideoContent"))
			icon=CustomListBaseAdapter.BRANDVIDEO;
		else if(content.getType().contentEquals("BrandVideoContent"))
			icon=CustomListBaseAdapter.BRANDVIDEO;
		else if(content.getType().contentEquals("AdvocacyVideoContent"))
			icon=CustomListBaseAdapter.ADVOCACYVIDEO;
		else if(content.getType().contentEquals("ApkContent"))
			icon=CustomListBaseAdapter.APP;
		return new CustomListItem(
				content.getType(), 
				"",
				content.getName(), 
				icon,
				content.getType(), 
				content.getId(),
				content.getId()
				//CustomListItem(String name, String itemDescription, String price,
			//int imageNumber, String type, String uuid, String puzzle_piece_id)
			);
	}
	public static CustomListItem newCustomListApp(App app) {
		// TODO Auto-generated method stub
		return new CustomListItem(
				"App", 
				"",
				app.getName(), 
				CustomListBaseAdapter.APP,
				"app", 
				app.getId(),
				app.getId()
			);
	}
	public static CustomListItem newCustomListBrandVideo(PuzzlePiece piece, Video video)
	{
		return new CustomListItem(
				"Brand Video", 
				video.getName(),
				piece.getName(), 
				CustomListBaseAdapter.BRANDVIDEO,
				"brandvideo", 
				video.getId(),
				piece.getId()
			);
	}
	public static CustomListItem newCustomListAdvocacyVideo(PuzzlePiece piece, Video video)
	{
		return new CustomListItem(
				"Advocacy Video", 
				video.getName(), 
				piece.getName(), 
				CustomListBaseAdapter.ADVOCACYVIDEO, 
				"advocacyvideo",
				video.getId(),
				piece.getId()
			);
	}
	public static CustomListItem newCustomListBrochure(PuzzlePiece piece)
	{
		return new CustomListItem(
				"Brochure", 
				"", 
				piece.getName(),
				CustomListBaseAdapter.PDF, 
				"brochure", 
				piece.getId(),
				piece.getId()
			);
		}

	public static String prettifySeconds(Integer seconds)
	{
	    Integer minutes=seconds/60;
	    seconds=seconds%60;
	    String s=minutes+":"+seconds;
//	    return padLeft(minutes.toString())+" minutes "+padLeft(seconds.toString())+" seconds" ;
	    return (minutes.toString())+" minutes "+(seconds.toString())+" seconds" ;
	}

	public static String padLeft(String s) {
	    return String.format("%2s", s).replace(' ', '0');
	}
}
