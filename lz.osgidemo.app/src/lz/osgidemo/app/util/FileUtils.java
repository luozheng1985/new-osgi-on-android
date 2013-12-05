package lz.osgidemo.app.util;

import android.os.Environment;

public class FileUtils {

	public String getSdcardRoot(){
		return Environment.getExternalStorageDirectory().getAbsolutePath();
	}
}
