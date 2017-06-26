package com.google.android.gms.samples.vision.text.photo;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.util.ArrayList;

/**
 * Created by KevinVuNguyen on 6/25/17.
 */

public class PicToString {

    private static final String TAG = "PhotoViewerActivity";
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private static ArrayList<String> inputStrings;

    public PicToString()
    {
        inputStrings = new ArrayList<>();
    }

    /**
     *
     * @param bitmap
     * @param context
     * @param overlay --> retrieves the reference location of the TextBlockView
     * @return an array of Strings
     */
   public static String[] getPicToString(Bitmap bitmap, Context context, TextBlockView overlay)
   {
       TextRecognizer textRecognizer = new TextRecognizer.Builder(context)
               .build();

       // Create a frame from the bitmap and run text detection on the frame.
       Frame frame = new Frame.Builder().setBitmap(bitmap).build();
       SparseArray<TextBlock> textBlockSparseArray = textRecognizer.detect(frame);

       if (!textRecognizer.isOperational()) {
           // Check for low storage.  If there is low storage, the native library will not be
           // downloaded, so detection will not become operational.
           IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
           boolean hasLowStorage = context.registerReceiver(null, lowstorageFilter) != null;

           if (hasLowStorage) {
               Toast.makeText(context, R.string.low_storage_error, Toast.LENGTH_LONG).show();
               Log.w(TAG, context.getString(R.string.low_storage_error));
           }
       }

       overlay.setContent(bitmap, textBlockSparseArray);

       // Although detector may be used multiple times for different images, it should be released
       // when it is no longer needed in order to free native resources.
       textRecognizer.release();
       getStrings(textBlockSparseArray);

       // initializing the arrayString
       String[] arrayString = new String[inputStrings.size()];

       //converts the strings in the arraylist to an array
       inputStrings.toArray(arrayString);

       return arrayString;
   }

    /**
     * converts the TextBlocks to Strings and stores the strings in an ArrayList
     * @param tester
     */
    private static void getStrings(SparseArray<TextBlock> tester)
    {
        inputStrings = new ArrayList<>();
        for(int i = 0; i < tester.size(); i++)
            inputStrings.add(tester.valueAt(i).getValue());
    }
}
