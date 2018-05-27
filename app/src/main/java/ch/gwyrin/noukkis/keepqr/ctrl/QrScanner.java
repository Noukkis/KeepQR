/*
 * The MIT License
 *
 * Copyright (c) 2018 Noukkis.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package ch.gwyrin.noukkis.keepqr.ctrl;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ch.gwyrin.noukkis.keepqr.model.CallAPI;
import ch.gwyrin.noukkis.keepqr.model.Crypto;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by Noukkis on 26.05.2018.
 */

public class QrScanner extends AppCompatActivity implements ZXingScannerView.ResultHandler{


    private final static int CAMERA_PERMISSION = 1;

    private ZXingScannerView mScannerView;
    private List<BarcodeFormat> barcodeFormat;
    private Crypto crypto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        barcodeFormat = new ArrayList<>();
        barcodeFormat.add(BarcodeFormat.QR_CODE);
        crypto = new Crypto();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION);
        } else {
            start();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CAMERA_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    start();
                } else {
                    Toast.makeText(this, "Camera is necessary", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }
    }

    private void start() {
        mScannerView = new ZXingScannerView(this);
        mScannerView.setAspectTolerance(0.5f);
        mScannerView.setFormats(barcodeFormat);
        setContentView(mScannerView);
    }

    private void send(String url, String keepass, String id) {
        HashMap<String, String> params = new HashMap<>();
        params.put("keepass", keepass);
        params.put("id", id);
        CallAPI.post(url, params, this);
    }

    @Override
    public void handleResult(Result result) {
        Bundle b = getIntent().getExtras();
        String keepass = b.getString("keepass");
        Toast toast = Toast.makeText(this, "Unexpected error", Toast.LENGTH_LONG);
        if(keepass != null) {
            try {
                JsonParser parser = new JsonParser();
                JsonObject root = parser.parse(result.getText()).getAsJsonObject();
                String url = root.get("url").getAsString();
                String key = root.get("key").getAsString();
                String iv = root.get("iv").getAsString();
                String id = root.get("id").getAsString();
                String encrypted = crypto.encrypt(keepass, key, iv);
                send(url, encrypted, id);
                toast.setText("Success");
                toast.setDuration(Toast.LENGTH_SHORT);
            } catch (Exception e) {
                toast.setText("Error: " + e.getMessage());
            }
        }
        toast.show();
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }
}
