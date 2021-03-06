package com.example.michaelwang.capitaltogo;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.net.http.RequestQueue;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.toolbox.Volley;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class RequestCashActivity extends AppCompatActivity {
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;

    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {android.Manifest.permission.RECORD_AUDIO};
    AudioRecord ar = null;
    int buffsize = 0;
    int blockSize = 256;
    boolean isRecording = false;
    private Thread recordingThread = null;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_cash);

        Typeface medium =  Typeface.createFromAsset(getAssets(),  "fonts/Avenir-Medium.ttf");
        Typeface condensed =  Typeface.createFromAsset(getAssets(),  "fonts/AvenirNextCondensed-DemiBold.ttf");

        final TextView tvAmount = (TextView) findViewById(R.id.tvAmount);
        final TextView tvDollarSign = (TextView) findViewById(R.id.tvDollar);
        final EditText etAmount = (EditText) findViewById(R.id.etAmount);
        final Button bOne = (Button) findViewById(R.id.bOne);
        final Button bFive = (Button) findViewById(R.id.bFive);
        final Button bTen = (Button) findViewById(R.id.bTen);
        final Button bTwenty = (Button) findViewById(R.id.bTwenty);
        final Button bFifty = (Button) findViewById(R.id.bFifty);
        final Button bHundred = (Button) findViewById(R.id.bHundred);
        final Button bSubmit = (Button) findViewById(R.id.bSubmit);
        final ImageButton bTalk = (ImageButton) findViewById(R.id.bTalk);

		tvAmount.setTypeface(medium);
        tvDollarSign.setTypeface(medium);
        etAmount.setTypeface(medium);
        bOne.setTypeface(condensed);
        bFive.setTypeface(condensed);
        bTen.setTypeface(condensed);
        bTwenty.setTypeface(condensed);
        bFifty.setTypeface(condensed);
        bHundred.setTypeface(condensed);

        bOne.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(TextUtils.isEmpty(etAmount.getText().toString())){
                    etAmount.setText("1");
                }
                else {
                    etAmount.setText("" + (Integer.parseInt(etAmount.getText().toString()) + 1));
                }
                etAmount.setSelection(etAmount.getText().toString().length());
            }
        });
        bFive.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(TextUtils.isEmpty(etAmount.getText().toString())){
                    etAmount.setText("5");
                }
                else {
                    etAmount.setText("" + (Integer.parseInt(etAmount.getText().toString()) + 5));
                }
                etAmount.setSelection(etAmount.getText().toString().length());
            }
        });
        bTen.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(TextUtils.isEmpty(etAmount.getText().toString())){
                    etAmount.setText("10");
                }
                else {
                    etAmount.setText("" + (Integer.parseInt(etAmount.getText().toString()) + 10));
                }
                etAmount.setSelection(etAmount.getText().toString().length());
            }
        });
        bTwenty.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(TextUtils.isEmpty(etAmount.getText().toString())){
                    etAmount.setText("20");
                }
                else {
                    etAmount.setText("" + (Integer.parseInt(etAmount.getText().toString()) + 20));
                }
                etAmount.setSelection(etAmount.getText().toString().length());
            }
        });
        bFifty.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(TextUtils.isEmpty(etAmount.getText().toString())){
                    etAmount.setText("50");
                }
                else {
                    etAmount.setText("" + (Integer.parseInt(etAmount.getText().toString()) + 50));
                }
                etAmount.setSelection(etAmount.getText().toString().length());
            }
        });
        bHundred.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(TextUtils.isEmpty(etAmount.getText().toString())){
                    etAmount.setText("100");
                }
                else {
                    etAmount.setText("" + (Integer.parseInt(etAmount.getText().toString()) + 100));
                }
                etAmount.setSelection(etAmount.getText().toString().length());
            }
        });

        bSubmit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(!bSubmit.getText().toString().equals("Get $0")) {
                    Intent submitIntent = new Intent(RequestCashActivity.this, CodeActivity.class);
                    submitIntent.putExtra("amount", etAmount.getText().toString());
                    submitIntent.putExtra("aid", getIntent().getExtras().getString("aid"));
                    RequestCashActivity.this.startActivity(submitIntent);
                }
            }
        });

        bTalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRecording) {
                    baslat(v);
                } else {
                    durdur(v);
                    File f1 = new File("/sdcard/voice8K16bitmono.pcm"); // The location of your PCM file
                    File f2 = new File("/sdcard/voice8K16bitmono.wav"); // The location where you want your WAV file
                    try {
                        rawToWave(f1, f2);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    String url = "http://c2go-api-dev.us-east-1.elasticbeanstalk.com/api/parseaudio";

                    Log.d("RequestCash", f2.toString());
                    // i'll fix the Flask API later so I can just do addTextBody(key, value)
                    HttpEntity entity = MultipartEntityBuilder.create()
                            .addBinaryBody("recording", new File("/sdcard/voice8K16bitmono.wav"))
                            .build();

                    HttpPost request = new HttpPost(url);
                    request.setEntity(entity);

                    HttpClient client = new DefaultHttpClient();
                    try {
                        HttpResponse response = client.execute(request);
                        JSONObject data = new JSONObject(EntityUtils.toString(response.getEntity()));
                        String amount = data.getString("amount");
                        Log.d("HomeScreenActivity.java", amount);
                        Intent submitIntent = new Intent(RequestCashActivity.this, CodeActivity.class);
                        submitIntent.putExtra("aid", getIntent().getExtras().getString("aid"));
                        submitIntent.putExtra("amount", amount);
                        RequestCashActivity.this.startActivity(submitIntent);
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    	etAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(TextUtils.isEmpty(etAmount.getText().toString())){
                    bSubmit.setText("Get $0");
                }
                else {
                    bSubmit.setText("Get $"+etAmount.getText().toString());
                }

                if(TextUtils.isEmpty(etAmount.getText().toString())){
                    bSubmit.setBackgroundColor(Color.parseColor("#A1A1A1"));
                    bSubmit.setTextColor(Color.parseColor("#D1C9C9"));
                }
                else if(Integer.parseInt(etAmount.getText().toString())>0){
                    bSubmit.setBackgroundColor(Color.parseColor("#509FF7"));
                    bSubmit.setTextColor(Color.parseColor("#FFFFFF"));
                }
                else{
                    bSubmit.setBackgroundColor(Color.parseColor("#A1A1A1"));
                    bSubmit.setTextColor(Color.parseColor("#D1C9C9"));
                }
            }
        });

    }


    public void baslat(View v)
    {
        // when click to START
        buffsize = AudioRecord.getMinBufferSize(44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        ar = new AudioRecord(MediaRecorder.AudioSource.MIC, 44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, buffsize);

        ar.startRecording();

        isRecording = true;
        recordingThread = new Thread(new Runnable() {
            public void run() {
                writeAudioDataToFile();
            }
        }, "AudioRecorder Thread");
        recordingThread.start();
    }
    public void durdur(View v)
    {
        // When click to STOP
        ar.stop();
        isRecording = false;
    }

    private void writeAudioDataToFile() {
        // Write the output audio in byte

        String filePath = "/sdcard/voice8K16bitmono.pcm";
        short sData[] = new short[buffsize/2];

        FileOutputStream os = null;
        try {
            os = new FileOutputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (isRecording) {
            // gets the voice output from microphone to byte format

            ar.read(sData, 0, buffsize/2);
            Log.d("eray","Short wirting to file" + sData.toString());
            try {
                // // writes the data to file from buffer
                // // stores the voice buffer
                byte bData[] = short2byte(sData);
                os.write(bData, 0, buffsize);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] short2byte(short[] sData) {
        int shortArrsize = sData.length;
        byte[] bytes = new byte[shortArrsize * 2];
        for (int i = 0; i < shortArrsize; i++) {
            bytes[i * 2] = (byte) (sData[i] & 0x00FF);
            bytes[(i * 2) + 1] = (byte) (sData[i] >> 8);
            sData[i] = 0;
        }
        return bytes;

    }
    private void rawToWave(final File rawFile, final File waveFile) throws IOException {

        byte[] rawData = new byte[(int) rawFile.length()];
        DataInputStream input = null;
        try {
            input = new DataInputStream(new FileInputStream(rawFile));
            input.read(rawData);
        } finally {
            if (input != null) {
                input.close();
            }
        }

        DataOutputStream output = null;
        try {
            output = new DataOutputStream(new FileOutputStream(waveFile));
            // WAVE header
            // see http://ccrma.stanford.edu/courses/422/projects/WaveFormat/
            writeString(output, "RIFF"); // chunk id
            writeInt(output, 36 + rawData.length); // chunk size
            writeString(output, "WAVE"); // format
            writeString(output, "fmt "); // subchunk 1 id
            writeInt(output, 16); // subchunk 1 size
            writeShort(output, (short) 1); // audio format (1 = PCM)
            writeShort(output, (short) 1); // number of channels
            writeInt(output, 44100); // sample rate
            writeInt(output, 44100 * 2); // byte rate
            writeShort(output, (short) 2); // block align
            writeShort(output, (short) 16); // bits per sample
            writeString(output, "data"); // subchunk 2 id
            writeInt(output, rawData.length); // subchunk 2 size
            // Audio data (conversion big endian -> little endian)
            short[] shorts = new short[rawData.length / 2];
            ByteBuffer.wrap(rawData).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shorts);
            ByteBuffer bytes = ByteBuffer.allocate(shorts.length * 2);
            for (short s : shorts) {
                bytes.putShort(s);
            }

            output.write(fullyReadFileToBytes(rawFile));
        } finally {
            if (output != null) {
                output.close();
            }
        }
    }
    byte[] fullyReadFileToBytes(File f) throws IOException {
        int size = (int) f.length();
        byte bytes[] = new byte[size];
        byte tmpBuff[] = new byte[size];
        FileInputStream fis= new FileInputStream(f);
        try {

            int read = fis.read(bytes, 0, size);
            if (read < size) {
                int remain = size - read;
                while (remain > 0) {
                    read = fis.read(tmpBuff, 0, remain);
                    System.arraycopy(tmpBuff, 0, bytes, size - remain, read);
                    remain -= read;
                }
            }
        }  catch (IOException e){
            throw e;
        } finally {
            fis.close();
        }

        return bytes;
    }
    private void writeInt(final DataOutputStream output, final int value) throws IOException {
        output.write(value >> 0);
        output.write(value >> 8);
        output.write(value >> 16);
        output.write(value >> 24);
    }

    private void writeShort(final DataOutputStream output, final short value) throws IOException {
        output.write(value >> 0);
        output.write(value >> 8);
    }

    private void writeString(final DataOutputStream output, final String value) throws IOException {
        for (int i = 0; i < value.length(); i++) {
            output.write(value.charAt(i));
        }
    }

}
