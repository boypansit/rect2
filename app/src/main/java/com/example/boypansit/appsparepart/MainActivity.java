package com.example.boypansit.appsparepart;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    private Button button;
    private TextView txtResult;
    private Button Search;
    private String  link;
    private String  valueName;
    private TextView Searchresult;
    private TextView ed1;
    private TextView ed2;
    private TextView ed3;
    private String contentss;
    private Button del;
    private Button regis;
    private Button change;
    private EditText plain_text_input;
    private TextView demoValue;
    public static final String TAG = "AndroidDrawing";

//    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference rootRef,demoRef;
//    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
//    private DatabaseReference mMovieRef;
    private DatabaseReference mRef ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button)findViewById(R.id.button);
        Search = (Button) findViewById(R.id.Search);
        txtResult = (TextView)findViewById(R.id.txtResult);
        Searchresult = (TextView) findViewById(R.id.Searchresult);
        ed1 = (TextView) findViewById(R.id.editText1);
        ed2 = (TextView) findViewById(R.id.editText2);
        ed3 = (TextView) findViewById(R.id.editText3);
        del = (Button) findViewById(R.id.del);
        regis = (Button) findViewById(R.id.regis);
        change = (Button) findViewById(R.id.change);
        plain_text_input = (EditText) findViewById(R.id.plain_text_input);
        //กำหนด Event เมื่อทำการกดปุ่ม btnScan
        del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference Spares = FirebaseDatabase.getInstance().getReference("store");
                    DatabaseReference zone1Ref = Spares.child(link);
                    DatabaseReference zone1NameRef = zone1Ref.child("Site");
                    DatabaseReference zone1stuRef = zone1Ref.child("Status");
//                    zone1stuRef.setValue("Delete");
                    zone1Ref.removeValue();
                    ed1.setText("Delete complete");
                    ed2.setText("");
                    ed3.setText("");
                }
        }
        );
        regis.setOnClickListener(new View.OnClickListener() {
                                   @RequiresApi(api = Build.VERSION_CODES.N)
                                   @Override
                                   public void onClick(View v) {
                                       DatabaseReference Spares = FirebaseDatabase.getInstance().getReference("store");
                                       DatabaseReference zone1Ref = Spares.child(link);
                                       DatabaseReference zone1NameRef = zone1Ref.child("Site");
                                       DatabaseReference zone1stuRef = zone1Ref.child("Status");
                                       DatabaseReference zone1regisRef = zone1Ref.child("Register");
                                       DatabaseReference zone1barcoderef = zone1Ref.child("Barcode");
                                       Date currentTime = Calendar.getInstance().getTime();
                                       Calendar c = Calendar.getInstance();
                                       SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                                       String formattedDate = df.format(c.getTime());
                                       zone1regisRef.setValue("Register on "+formattedDate);
                                       zone1stuRef.setValue("Register");
                                       zone1barcoderef.setValue(link);
                                   }
                               }
        );
        change.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View v) {
                                         DatabaseReference Spares = FirebaseDatabase.getInstance().getReference("store");
                                         DatabaseReference zone1Ref = Spares.child(link);
                                         DatabaseReference zone1NameRef = zone1Ref.child("Site");
                                         DatabaseReference zone1stuRef = zone1Ref.child("Status");
                                         String input = plain_text_input.getText().toString();
                                         zone1NameRef.setValue(input);
                                         zone1stuRef.setValue("On-Site");
                                     }
                                 }
        );
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                try {
                    //กำหนด intent ในการเรียกใช้ Barcode Scanner
                    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                    //ส่ง Mode ในการ Scan ให้กับ โปรแกรม Barcode Scanner
                    intent.putExtra("com.google.zxing.client.android.SCAN.SCAN_MODE", "QR_CODE_MODE");
                    //เริ่ม Activity จาก intent ที่กำหนด โดยกำหนด requestCode เป็น 0
                    startActivityForResult(intent, 0);
                } catch (Exception e) {
                    // TODO: handle exception
                    //ถ้าไม่ได้ลงโปรแกรม Barcode Scanner ไว้จะแสดงข้อความ Please Install Barcode Scanner
                    Toast.makeText(getBaseContext(),"Please Install Barcode Scanner",Toast.LENGTH_SHORT).show();
                }
            }
        });
        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed1.setText("");
                ed2.setText("");
                ed3.setText("");

                DatabaseReference Spares = FirebaseDatabase.getInstance().getReference("store");
                DatabaseReference zone1Ref = Spares.child(link);
                DatabaseReference zone1NameRef = zone1Ref.child("Site");
                DatabaseReference zone1stuRef = zone1Ref.child("Status");
                DatabaseReference zone1regisRef = zone1Ref.child("Register");

                zone1NameRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        valueName = dataSnapshot.getValue(String.class);
//                        Log.i(TAG,dataSnapshot.getValue(String.class));
                        if(valueName == null){
//                           ed1.setText("QR Register");
                        }
                        else{
                            ed1.setText("Site "+ valueName);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
//                        Log.w(TAG, "onCancelled", databaseError.toException());
                        Searchresult.setText("Result Site : No");
                    }
                });
                zone1stuRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        Log.i(TAG,dataSnapshot.getValue(String.class));

                        String value = dataSnapshot.getValue(String.class);
//                        Log.i(TAG,dataSnapshot.getValue(String.class));
                        if(value == null){
//                            ed2.setText("Result : DB mismatch");
                        }
                        else{
                            ed2.setText("Status "+ value);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
//                        Log.w(TAG, "onCancelled", databaseError.toException());
                    }
                });
                zone1regisRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String value = dataSnapshot.getValue(String.class);
//                        Log.i(TAG,dataSnapshot.getValue(String.class));
                        if(value == null){
                            ed1.setText("QR not Register");
                            regis.setVisibility(View.VISIBLE);
                            del.setVisibility(View.INVISIBLE);
                            change.setVisibility(View.INVISIBLE);
                            plain_text_input.setVisibility(View.INVISIBLE);
                        }
                        else{
//                            ed1.setText("QR Register");
                            if(valueName==null){
                                ed1.setText("QR Register");
                            }
                            regis.setVisibility(View.INVISIBLE);
                            del.setVisibility(View.VISIBLE);
                            change.setVisibility(View.VISIBLE);
                            plain_text_input.setVisibility(View.VISIBLE);
                            ed3.setText(value);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
//                        Log.w(TAG, "onCancelled", databaseError.toException());
                        Searchresult.setText("Result Site : No");
                    }
                });
            }
        });

    }
    //เมื่อทำการสแกนเสร็จแล้ว จะมาเรียก function onActivityResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // TODO Auto-generated method stub

        if (requestCode == 0) //ทำการตรวจสอบว่า requestCode ตรงกับที่ Barcode Scanner คืนค่ามาหรือไม่
        {
            if (resultCode == RESULT_OK) //ถ้า Barcode Scanner ทำงานสมบูรณ์
            {
                //รับข้อมูลจาก Barcode Scanner ที่ได้จากการสแกน
                String contents = intent.getStringExtra("SCAN_RESULT");
                //รับรูปแบบจาก Barcode Scanner ที่ได้จากการสแกน ว่าเป็นชนิดใด
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                //ทำการแสดงผลลัพธ์จากการแสกนใน txtResult
                txtResult.setText("QR CODE : " + contents);
                this.link = contents;
                contentss = contents;
                Search.setVisibility(View.VISIBLE);
                Searchresult.setVisibility(View.VISIBLE);
            }
        }
    }
}
