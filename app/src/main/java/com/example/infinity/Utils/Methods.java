package com.example.infinity.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.example.infinity.Models.database.UserData;
import com.example.infinity.Models.database.UserPrivateData;
import com.example.infinity.Models.database.UserProfileData;
import com.example.infinity.Models.database.Votes;
import com.example.infinity.Models.device.getFilePaths;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class Methods {
    private static final String TAG = "Methods";
    private static double mPhotoUploadProgress = 0;


    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    /* METHODS FOR VERIFYING PERMISSIONS */

    /*method for looping on a permissions array*/
    public static boolean checkingPermissions(String[] permissions , Context mContext){
        Log.d(TAG, "checkingPermissions: checking permissions for :" + Arrays.toString(permissions));

        for (int i = 0 ; i < permissions.length ; i ++){
            String check = permissions[i];
            if (!checkPermission(check , mContext)){
                return false ;
            }
        }
        return true ;
    }

    /*checking if a permission is granted or not **/
    public static boolean checkPermission(String permission, Context mContext) {
        Log.d(TAG, "checkPermission: checking permission for : " + permission);

        int permissionRequest = ActivityCompat.checkSelfPermission(mContext , permission);

        if (permissionRequest != PackageManager.PERMISSION_GRANTED){
            return false;
        }
        return true ;
    }

    /*method for verifying permissions*/
    public static void verifyPermissions(String[] permissions , Activity mContext , int verifyPermissions){
        Log.d(TAG, "verifyPermissions: verifying permissions for: " + Arrays.toString(permissions));

        ActivityCompat.requestPermissions(mContext , permissions , verifyPermissions);


    }

    /***************************************************** METHODS FOR SEARCHING THE DEVICE DIRECTORIES ************************************************/

    /* this method will return an array list of folders from the given directory*/
    public static ArrayList<String> getFolders(String directory){
        ArrayList<String> foldersList = new ArrayList<>();
        File file = new File(directory);
        File[] listFiles = file.listFiles();

        for (int i = 0 ; i < listFiles.length ; i ++){
            if (listFiles[i].isDirectory() && listFiles[i].length() != 0){
                foldersList.add(listFiles[i].getAbsolutePath());
            }
        }
        return foldersList;
    }

    /*this method will return all files from the given folder*/
    public static ArrayList<String> getFiles(String folder){

        ArrayList<String> filesList = new ArrayList<>();
        File file = new File(folder);
        File[] files = file.listFiles();

        for (int i = 0 ; i < files.length ; i ++ ){

            if (files[i].isFile()){
                filesList.add(files[i].getAbsolutePath());
            }

        }

        return filesList ;

    }

    public static Boolean isDownloadsEmpty(Context mContext){
        return new File(mContext.getExternalFilesDir(null), "amajilanjilanwebemrhetjha").length() != 0;
    }





    /********************************************************** STRING MANIPULATIONS *************************************************************/

    /**
     * checking if string is null or not
     * return true or false
     * */
    public static boolean isStringNull(String string){
        return string.equals("");
    }

    /*
    *expanding string method
    * take a string with a period and replace it with a blank space
    * */
    public static String expandString(String string){
        return string.replace(".", " ");
    }

    /*
     *condensing  string method
     * take a string with a blank space and replace it with a period
     * */
    public static String condensingString(String string){
        return string.replace(" ", ".");
    }

    /*
     * Getting TimeStamp Difference Method
     * This Method will take the date created from discussion or comment database and return number of days
     * Input String Return String
     * */

    public static String getTimeStampDifference(final String dateCreated){

        String difference = "";
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("Africa/Johannesburg"));

        Date today = c.getTime();
        sdf.format(today);
        Date timeStamp;

        try {
            timeStamp = sdf.parse(dateCreated);
            difference = String.valueOf(Math.round((today.getTime() - timeStamp.getTime()) / 1000 / 60 /60 /24));
        }catch (ParseException e){
            Log.e(TAG, "getTimeStampDifference: ParseException : Parsing date went wrong :"+ e.getMessage() );
            difference = "0";
        }

        return difference ;

    }

    /*
     * Getting TimeStamp  Method
     * This Method will  return number current date
     * Input nothing Return String
     * */
    public static String getTimeStamp() {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Africa/Johannesburg"));
        return simpleDateFormat.format(new Date());

    }


    /*public static String upVotesText (List<Votes> votesList ){
        // update votes text
        if (votesList.size() == 1){
            return votesList.get(0).getUsername()+ " upvoted";

        }else if (votesList.size() == 2){

            return votesList.get(0).getUsername() +" and " +votesList.get(1).getUsername()+ " upvoted" ;

        }else if (votesList.size() == 3){

            return votesList.get(0).getUsername() + " , " + votesList.get(1).getUsername() + " and " +votesList.get(2).getUsername()+ " upvoted";

        }else if(votesList.size() >= 4){

            return  votesList.get(0).getUsername() + " , " + votesList.get(1).getUsername() + " , " +votesList.get(2).getUsername()+ " and " + (votesList.size() - 3) + " others upvoted";

        }

        return "support this discussion? give it an upvote!";
    }*/




    /*################################################################### FIREBASE METHODS##############################################################*/



    /**
     *check if the user is signed in or out
     * return true or false
     * */
    public static boolean checkCurrentUser( FirebaseUser firebaseUser){
        return firebaseUser != null;
    }


    /**
     *check if the user liked a discussion or not
     * return true or false
     * */
    public static Boolean isLikedByCurrentUser(DataSnapshot mDataSnapshot){

        for (DataSnapshot singleSnapShot : mDataSnapshot.getChildren()){

            // Case 1 : The User Already up voted the Photo
            if (singleSnapShot.getValue(Votes.class).getUser_id().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                return true ;

            }



        }

        return false ;
    }





    /*
     * Add New User to the Data-Base
     * inserting the user data to the data-base( email, full_name , password, date of birth etc.)
     * */

    public static void addUserToDataBase(DatabaseReference databaseReference ,String userID , String username , String email , String fullName , String profilePhoto){

        UserData user = new UserData(userID , username , profilePhoto ,"Profession");
        databaseReference.child("users")
                .child(userID)
                .setValue(user);

        UserPrivateData userPrivateData = new UserPrivateData(fullName ,email , "" , "" , "" , "");

        databaseReference.child("users_private_data")
                .child(userID)
                .setValue(userPrivateData);


    }



    /*
     * Method for retrieving user profile data from firebase
     * This method will get user stored data from the Users Node
     * */

    public static UserData getUserProfileData(DataSnapshot snapshot , FirebaseAuth mAuth ){

        Log.d(TAG, "getUserProfileData: attempting to get user data from fire base");

        UserData userData = new UserData();

        for (DataSnapshot ds : snapshot.getChildren() ){

            /*getting user data from firebase users node*/
            if (ds.getKey().equals(mAuth.getUid())){
                Log.d(TAG, "getUserProfileData: dataSnapshot" + ds);

                try {


                    userData.setUsername(
                            ds.getValue(UserData.class)
                                    .getUsername());

                    userData.setProfile_photo(
                            ds.getValue(UserData.class)
                                    .getProfile_photo());

                    userData.setProfession(
                            ds.getValue(UserData.class)
                                    .getProfession());

                    Log.d(TAG, "getUserProfileData: retrieved user profile data" + userData.toString());

                }catch (NullPointerException e){
                    Log.e(TAG, "getUserProfileData: NullPointerException " + e.getMessage() );
                }

            }




        }

        return userData ;

    }



    /*
     * Method for retrieving user profile data from firebase
     * This method will get user stored data from the Users Node
     * */

    public static UserPrivateData getUserPrivateData(DataSnapshot snapshot , FirebaseAuth mAuth ){

        Log.d(TAG, "getUserProfileData: attempting to get user data from fire base");

        UserPrivateData userPrivateData = new UserPrivateData();

        for (DataSnapshot ds : snapshot.getChildren() ){

            /*getting user data from firebase users node*/
            if (ds.getKey().equals(mAuth.getUid())){
                Log.d(TAG, "getUserProfileData: dataSnapshot" + ds);

                try {


                    userPrivateData.setGender(
                            ds.getValue(UserPrivateData.class)
                                    .getGender());

                    userPrivateData.setDate_of_birth(
                            ds.getValue(UserPrivateData.class)
                                    .getDate_of_birth());



                    Log.d(TAG, "getUserProfileData: retrieved user profile data" + userPrivateData.toString());

                }catch (NullPointerException e){
                    Log.e(TAG, "getUserProfileData: NullPointerException " + e.getMessage() );
                }

            }




        }

        return userPrivateData ;

    }






    /*update username */
    public static void updateUsername(String mUsername ,String mUserID , DatabaseReference mRef){
        Log.d(TAG, "updateUsername: updating username to " + mUsername);
        mRef.child("users")
                .child(mUserID)
                .child("username")
                .setValue(mUsername);
    }

    /*update username */
    public static void updateChaptersDownloadedVideos(String downloadedPositions ,String mUserID ,String courseID , String chapterID, DatabaseReference mRef){
        Log.d(TAG, "updateUsername: updating username to " + downloadedPositions);
        mRef.child("users_private_data")
                .child(mUserID)
                .child("downloaded_courses")
                .child(courseID)
                .child("chapters")
                .child(chapterID)
                .child("downloaded_media")
                .setValue(downloadedPositions);
    }

    /*update profile details */
    public static void updateProfileDetails(String mProfession ,String mDateOfBirth ,String mGender ,String mUserID ,String mProfileImg,DatabaseReference mRef){

        if (mProfession != null){
            Log.d(TAG, "updateUsername: updating username to " + mProfession);
            mRef.child("users")
                    .child(mUserID)
                    .child("profession")
                    .setValue(mProfession);
        }
        if (mProfileImg != null){
            Log.d(TAG, "updateUsername: updating username to " + mProfileImg);
            mRef.child("users")
                    .child(mUserID)
                    .child("profile_photo")
                    .setValue(mProfileImg);
        }


        if (mDateOfBirth!= null){
            Log.d(TAG, "updateUsername: updating username to " + mDateOfBirth);
            mRef.child("users_private_data")
                    .child(mUserID)
                    .child("date_of_birth")
                    .setValue(mDateOfBirth);
        }


        if (mGender != null){
            Log.d(TAG, "updateUsername: updating username to " + mGender);
            mRef.child("users_private_data")
                    .child(mUserID)
                    .child("gender")
                    .setValue(mGender);
        }




    }

    /*
    * Method for uploading users profile pictures to firebase storage
    * */

    public static void uploadUsersProfilePic(Activity mContext ,StorageReference mStorageRef , DatabaseReference DBRef,String userID , String imageUrl ){

        //profile image storage ref

        StorageReference profileImageRef = mStorageRef.child(getFilePaths.FIREBASE_PROFILE_IMAGE_STORAGE + userID + "/profile_image");

        // convert image memory url to bitmap
        Bitmap bm = imageUrlToBitmap(imageUrl);

        //convert Bitmap to BYteArray
        byte[] bytes = getBytesFromBitmap(bm ,50);

        //upload task
        UploadTask uploadTask = null ;
        uploadTask = profileImageRef.putBytes(bytes);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                profileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //add photo url to database, users node , profile photo property
                        updateProfileDetails(null , null ,null , userID , uri.toString() , DBRef);
                        Toast.makeText(mContext ,"image upload Success" , Toast.LENGTH_SHORT).show();

                        mContext.onBackPressed();
                    }
                });



            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(mContext ,"image upload failed" , Toast.LENGTH_SHORT).show();

            }

        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount() ;

                if (progress - 15 > mPhotoUploadProgress){

                    Toast.makeText(mContext ,"image upload progress : " + String.format("%.0f" , progress) +"%" , Toast.LENGTH_SHORT).show();
                    mPhotoUploadProgress = progress ;
                }

            }

        });


    }

    /*############################################################################# DIALOGS ####################################################################*/

    public static void genderAlertDialog(final TextInputEditText editText , Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("select your gender");

        final String[] gender = {"Female" , "Male" , "LGBTQ" ,"Prefer not to say"};
        builder.setItems(gender, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position) {
                if (position == 0 ){

                    editText.setText(gender[0]);

                }else if(position == 1){

                    editText.setText(gender[1]);

                }else if (position == 2){

                    editText.setText(gender[2]);

                }else {

                    editText.setText(gender[3]);

                }
            }
        });

        /*create and show dialog*/
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    /*############################################################################# IMAGE MANIPULATIONS ####################################################################*/

    /*method for converting image url from the phone memory to bitmap*/
    public static Bitmap imageUrlToBitmap(String imageUrl){
        File imageFile = new File(imageUrl);
        FileInputStream fis = null ;
        Bitmap bitmap = null ;

        try {

            fis = new FileInputStream(imageFile);
            bitmap = BitmapFactory.decodeStream(fis);

        }catch (FileNotFoundException e){
            Log.d(TAG, "imageUrlToBitmap: FileNotFoundException: " + e.getMessage());
        } finally {

            try {
                fis.close();
            }catch (IOException e){
                Log.d(TAG, "imageUrlToBitmap: IOException: " + e.getMessage());
            }

        }

        return bitmap ;

    }

    /*method for converting Bitmap to byte array
    * quality is between 0 to 100*/
    public static byte[] getBytesFromBitmap(Bitmap bitmap , int quality){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG , quality ,byteArrayOutputStream);

        return byteArrayOutputStream.toByteArray();
    }


}
