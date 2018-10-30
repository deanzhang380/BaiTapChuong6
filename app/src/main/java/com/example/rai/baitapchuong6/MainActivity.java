package com.example.rai.baitapchuong6;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import static com.example.rai.baitapchuong6.R.id.radio_button1;

public class MainActivity extends AppCompatActivity {
    Button bt_change, bt_save;
    EditText editText_name, editText_mail, editText_PhoneNumber;
    Intent intent;
    ImageView img;
    private static final String FILENAME = "myFile.txt";
    int Request_Code = 123;
    RadioGroup RG;
    RadioButton rb;
    String flag = "0";
    String value="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt_change = findViewById(R.id.button_change);
        bt_save = findViewById(R.id.button_save);
        editText_name = findViewById(R.id.ed1);
        editText_mail = findViewById(R.id.ed2);
        editText_PhoneNumber = findViewById(R.id.ed3);
        img = findViewById(R.id.image1);
        RG = findViewById(R.id.RG);

        int selectedId = RG.getCheckedRadioButtonId();
        rb = (RadioButton) findViewById(selectedId);
        bt_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, Request_Code);
            }
        });
        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BitmapDrawable drawable = (BitmapDrawable) img.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                if (editText_name == null) {
                    Toast.makeText(MainActivity.this, "Name is empty", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        WriteData(editText_name.getText().toString(), editText_mail.getText().toString(), editText_PhoneNumber.getText().toString(),bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        ReadData(FILENAME);
    }

    public void WriteData(String name, String mail, String PhoneNumber, Bitmap img) {
        try {
            FileOutputStream fileOutputStream = openFileOutput(FILENAME, MODE_PRIVATE);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            outputStreamWriter.write(name+"\n");
            outputStreamWriter.write(mail+"\n");
            outputStreamWriter.write(PhoneNumber+"\n");
            outputStreamWriter.write(rb.getText().toString()+"\n");
            outputStreamWriter.write(BitmapToString(img));
            outputStreamWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void ReadData(String FILENAME){
        try {
            FileInputStream fileIn=openFileInput(FILENAME);
            InputStreamReader InputRead= new InputStreamReader(fileIn);

            char[] inputBuffer= new char[1000];
            String s="";
            int charRead;

            while ((charRead=InputRead.read(inputBuffer))>0) {
                // char to string conversion
                String readstring=String.copyValueOf(inputBuffer,0,charRead);
                s +=readstring;
            }
            String[] items=s.split("\n");
            List<String> list= new ArrayList<String>();
            for(int i=0;i<items.length;i++){
                list.add(items[i]);
            }
            editText_name.setText(items[0]);
            editText_mail.setText(items[1]);
            editText_PhoneNumber.setText(items[2]);
            if(items[3]=="Male"){
                rb = (RadioButton) findViewById(R.id.radio_button1);
                rb.setChecked(true);
            }if(items[3]=="Famale"){
                rb = (RadioButton) findViewById(R.id.radio_button2);
                rb.setChecked(true);
            }
            img.setImageBitmap(StringToBitmap(items[4]));
            InputRead.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == Request_Code && resultCode == RESULT_OK && data != null) {
            Bitmap bm = (Bitmap) data.getExtras().get("data");
            img.setImageBitmap(bm);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static String BitmapToString(Bitmap bitmap) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            String temp = Base64.encodeToString(b, Base64.DEFAULT);
            return temp;
        } catch (NullPointerException e) {
            return null;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }

    public static Bitmap StringToBitmap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (NullPointerException e) {
            e.getMessage();
            return null;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }

}
