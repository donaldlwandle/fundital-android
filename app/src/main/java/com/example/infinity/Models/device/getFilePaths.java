package com.example.infinity.Models.device;

import android.os.Environment;

public class getFilePaths {

    /* storage / emulated / o */
    private static String ROOT_DIR = Environment.getExternalStorageDirectory().getPath();
    public static String CAMERA = ROOT_DIR + "/DCIM/camera" ;
    public static String PICTURES = ROOT_DIR + "/Pictures" ;
    public static String FIREBASE_PROFILE_IMAGE_STORAGE = "photos/users/" ;
}
