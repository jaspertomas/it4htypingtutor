package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.os.Environment;
import android.os.StatFs;

public class MyFileHelper {
	public static Double calcFreeSpaceMB()
	{
		//calculate free space
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
		double sdAvailSize = (double)stat.getAvailableBlocks()
		                  * (double)stat.getBlockSize();
		//free size in megabytes
		return sdAvailSize / (1024*1024);			
	}
	public static Long getFileSize(String filename)
	{
		return new File(MyDownloadHelper.DOWNLOADDIR+"/"+filename).length(); 
	}
	
//	public static String getMD5EncryptedString(String filename){
//        MessageDigest mdEnc = null;
//        try {
//            mdEnc = MessageDigest.getInstance("MD5");
//        } catch (NoSuchAlgorithmException e) {
//            System.out.println("Exception while encrypting to md5");
//            e.printStackTrace();
//        } 
//        
////        // Encryption algorithm
////        mdEnc.update(encTarget.getBytes(), 0, encTarget.length());
////        String md5 = new BigInteger(1, mdEnc.digest()).toString(16) ;
//
//		try {
//	        InputStream is;
//			is = new FileInputStream(filename);
//	        is = new DigestInputStream(is, mdEnc);
//	        is.close();
//	        byte[] digest = mdEnc.digest();        
//	        return String.valueOf(digest);
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		}
//
//    }
	public static String getMD5(String file){
	    String md5 = "";

	    try {
	        MessageDigest md = MessageDigest.getInstance("MD5");
	        InputStream is;
			is = new FileInputStream(file);

	        DigestInputStream dis = new DigestInputStream(is, md);
	        byte data[] = new byte[1024];
	        @SuppressWarnings("unused")
	        int count;
	        while ((count = dis.read(data)) != -1) {

	        }
	        byte[] digest = md.digest();

	        for (int i=0; i < digest.length; i++) {
	            md5 += Integer.toString( ( digest[i] & 0xff ) + 0x100, 16).substring( 1 );
	        }
	        return md5;
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return md5;
	}
}