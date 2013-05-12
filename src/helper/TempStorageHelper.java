package helper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;

public class TempStorageHelper {
	private String tempDirectory;
	private ArrayList<String> fileList = new ArrayList<String>();
	
	public TempStorageHelper() {
		this("temp");
	}
	
	public TempStorageHelper(String tempDirectory) {
		this.tempDirectory = Environment.getExternalStorageDirectory() + "/" + tempDirectory;
		
		init();
	}
	
	private void init() {
		File dir = new File(tempDirectory);
    	if(!dir.exists()) {
    		dir.mkdir();
        }
	}
	
	public void copyAsset(Context context, String path) throws IOException {
		AssetManager assetManager = context.getAssets();
		InputStream in = null;
		OutputStream out = null;
		String fileName = path;
		
		if(path.lastIndexOf("/") > -1) {
			fileName = path.substring(path.lastIndexOf("/"));
		}
		
		try {
	        in = assetManager.open(path);
	        out = new FileOutputStream(tempDirectory + "/" + fileName);
	        copyFile(in, out);
	        fileList.add(fileName);
		} catch(IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			in.close();
	        in = null;
	        out.flush();
	        out.close();
	        out = null;
		}
	}
	
	public String getTempFilePath(String fileName) {
		return tempDirectory + "/" + fileName;
	}
	
	public void removeTempfiles() {
		for(int i=0; i<fileList.size(); i++) {
			File f = new File(tempDirectory + "/" + fileList.get(i));
			f.delete();
		}
	}
	
	private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
          out.write(buffer, 0, read);
        }
    }
}
