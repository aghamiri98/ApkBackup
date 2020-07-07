package com.aghamiri.apkbackup.data;

import android.Manifest;
import android.os.Environment;

import java.io.File;

public class Constant {

    private static final String BASE_PATH = Environment.getExternalStorageDirectory().toString() + File.separator;

    public static final String BACKUP_FOLDER = BASE_PATH + "ApkBackup" + File.separator;

    // for permission android M (6.0)
    public static String[] ALL_REQUIRED_PERMISSION = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

}
