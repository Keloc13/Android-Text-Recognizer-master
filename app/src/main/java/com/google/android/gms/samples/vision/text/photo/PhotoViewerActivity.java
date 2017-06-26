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
        TextBlockView overlay = (TextBlockView) findViewById(R.id.faceView);

        // Start the intent to get a List of Strings from the image
        Intent i = new RetrieveList().newIntent(PhotoViewerActivity.this, PicToString.getPicToString(bitmap,this, overlay));
        startActivity(i);
    }
}

