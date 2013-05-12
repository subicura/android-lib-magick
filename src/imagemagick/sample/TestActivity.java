package imagemagick.sample;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import magick.CompositeOperator;
import magick.ImageInfo;
import magick.MagickException;
import magick.MagickImage;

import helper.TempStorageHelper;
import teste.ndk.R;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

public class TestActivity extends Activity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        TempStorageHelper tempStorageHelper = new TempStorageHelper();
        try {
			tempStorageHelper.copyAsset(this, "bg.jpg");
			tempStorageHelper.copyAsset(this, "android.png");
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        try {
        	ImageInfo canvasInfo = new ImageInfo(tempStorageHelper.getTempFilePath("bg.jpg"));
        	MagickImage canvasImage = new MagickImage(canvasInfo);
        	
        	MagickImage image = new MagickImage(new ImageInfo(tempStorageHelper.getTempFilePath("android.png")));
        	canvasImage.compositeImage(CompositeOperator.AtopCompositeOp, image, 266, 538);
        	
        	try {
    			byte blob[] = canvasImage.imageToBlob(canvasInfo);
    			FileOutputStream fos = new FileOutputStream(new File(Environment.getExternalStorageDirectory() + "/Download/test.jpg"));
    			fos.write(blob);
    			fos.close();
    			
    			this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
    		}
    		catch (IOException e) {
    			e.printStackTrace();
    		}
		} catch (MagickException e) {
			e.printStackTrace();
		}
        
        tempStorageHelper.removeTempfiles();
    }
}