package com.example.easyexchangeapp.ImageCompressor;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Compressor {
    private final Uri imagePath;
    private final Context context;

    public Compressor(Uri imagePath, Context context) {
        this.imagePath = imagePath;
        this.context = context;
    }


    public byte[] compressImage() throws IOException {
        Bitmap bmp = MediaStore.Images.Media.getBitmap(context.getContentResolver(),imagePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG,25,baos);
        byte[] data = baos.toByteArray();
        return data;
    }
}
