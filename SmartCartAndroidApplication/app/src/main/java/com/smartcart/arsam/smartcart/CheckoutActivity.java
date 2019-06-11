package com.smartcart.arsam.smartcart;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.smartcart.arsam.smartcart.HomeFragments.CartFragment;
import com.smartcart.arsam.smartcart.Utility.SharedPref;

public class CheckoutActivity extends AppCompatActivity {

    ImageView imageView;
    String stringqrcode;
    Thread thread;
    public final static int QRcodeWidth = 500;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Checkout");
        imageView = findViewById(R.id.imageViewqrcode_checkout);

        stringqrcode = "";
        //create string of recycler

        //add user id in start
        SharedPref sp = new SharedPref(this);
        stringqrcode = stringqrcode + sp.getIntPref("user_id",0) + ";";

        for (int i = 0;i<MainActivity.cartItemsModels.size();i++)
        {
            stringqrcode = stringqrcode + MainActivity.cartItemsModels.get(i).getId() + ";" +
                    MainActivity.cartItemsModels.get(i).getName()+ ";" +
                    MainActivity.cartItemsModels.get(i).getQuantity() + ";" +
                    MainActivity.cartItemsModels.get(i).getPrice() + ";";
        }

        //create qr code
        try {
            //Log.d("string val",stringqrcode);
            bitmap = TextToImageEncode(stringqrcode);
            imageView.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }

    }

    Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.QRCodeBlackColor):getResources().getColor(R.color.QRCodeWhiteColor);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                stringqrcode = null;
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}
