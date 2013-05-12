package imagemagick.sample;

import helper.TempStorageHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import magick.CompositeOperator;
import magick.DrawInfo;
import magick.GravityType;
import magick.ImageInfo;
import magick.MagickException;
import magick.MagickImage;
import magick.PixelPacket;
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
			tempStorageHelper.copyAsset(this, "OpenSans-Semibold.ttf");
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        try {
        	ImageInfo canvasInfo = new ImageInfo(tempStorageHelper.getTempFilePath("bg.jpg"));
        	MagickImage canvasImage = new MagickImage(canvasInfo);
        	
        	// add image
        	MagickImage image = new MagickImage(new ImageInfo(tempStorageHelper.getTempFilePath("android.png")));
        	canvasImage.compositeImage(CompositeOperator.AtopCompositeOp, image, 266, 538);
        	
        	// add text
			ImageInfo textImage = new ImageInfo();
			DrawInfo aInfo = new DrawInfo(textImage);
			aInfo.setFill(PixelPacket.queryColorDatabase("black"));
			aInfo.setOpacity(0);
			aInfo.setPointsize(50);
			aInfo.setFont(tempStorageHelper.getTempFilePath("OpenSans-Semibold.ttf"));
			aInfo.setTextAntialias(true);
			aInfo.setText("This is Text!!");
		    aInfo.setGeometry("730x70+0+900");
		    aInfo.setGravity(GravityType.CenterGravity);
		    canvasImage.annotateImage(aInfo);
        			    
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