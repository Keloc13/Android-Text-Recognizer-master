/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.android.gms.samples.vision.text.photo;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.InputStream;
import java.util.ArrayList;


public class PhotoViewerActivity extends Activity {
    private static final String TAG = "PhotoViewerActivity";
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private ArrayList<String> inputStrings;

    public PhotoViewerActivity()
    {
        inputStrings = new ArrayList<>();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Calling the super onCreate first
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_viewer);

        // This will take a picture from the resources raw in the APK and convert it to a bitmap
        // which the TextRecognizer can reads to create a SparseArray of TextBlock
        InputStream stream = getResources().openRawResource(R.raw.android_test_large);
        Bitmap bitmap = BitmapFactory.decodeStream(stream);
        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext())
                .build();

        // Create a frame from the bitmap and run text detection on the frame.
        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
        SparseArray<TextBlock> textBlockSparseArray = textRecognizer.detect(frame);

        if (!textRecognizer.isOperational()) {
            // Check for low storage.  If there is low storage, the native library will not be
            // downloaded, so detection will not become operational.
            IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
            boolean hasLowStorage = registerReceiver(null, lowstorageFilter) != null;

            if (hasLowStorage) {
                Toast.makeText(this, R.string.low_storage_error, Toast.LENGTH_LONG).show();
                Log.w(TAG, getString(R.string.low_storage_error));
            }
        }

        TextBlockView overlay = (TextBlockView) findViewById(R.id.faceView);
        overlay.setContent(bitmap, textBlockSparseArray);

        // Although detector may be used multiple times for different images, it should be released
        // when it is no longer needed in order to free native resources.
        textRecognizer.release();
        getStrings(textBlockSparseArray);

        // Start the intent to get a List of Strings from the image
        Intent i = new RetrieveList().newIntent(PhotoViewerActivity.this, inputStrings);
        startActivity(i);
    }

    // From SpareArray of TextBlock, add the value (String) of the the class inputStrings
    void getStrings(SparseArray<TextBlock> tester)
    {
        for(int i = 0; i < tester.size(); i++)
            inputStrings.add(tester.valueAt(i).getValue());
    }
}

