package com.aldi.dacari.Activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.aldi.dacari.R;

public class NavMapsListActivity extends AppCompatActivity {
    Button lokasi1, lokasi2, lokasi3, lokasi4, lokasi5;

    private Button callBTN_1, callBTN_2, callBTN_3, callBTN_4,
            callBTN_5, callBTN_6, callBTN_7, callBTN_8, callBTN_9,
            callBTN_10, callBTN_11, callBTN_12, callBTN_13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_maps_home_list);

        lokasi1=findViewById(R.id.lokasi_toko_1);
        lokasi2=findViewById(R.id.lokasi_toko_2);
        lokasi3=findViewById(R.id.lokasi_toko_3);
        lokasi4=findViewById(R.id.lokasi_toko_4);
        lokasi5=findViewById(R.id.lokasi_toko_5);

        // TELPHON
        callBTN_1 = (Button) findViewById(R.id.tel_toko_1);
        callBTN_2 = (Button) findViewById(R.id.tel_toko_2);
        callBTN_3 = (Button) findViewById(R.id.tel_toko_3);
        callBTN_4 = (Button) findViewById(R.id.tel_toko_4);

        callBTN_5 = (Button) findViewById(R.id.tel_toko_5);
        callBTN_6 = (Button) findViewById(R.id.tel_toko_6);
        callBTN_7 = (Button) findViewById(R.id.tel_toko_7);
        callBTN_8 = (Button) findViewById(R.id.tel_toko_8);

        callBTN_9 = (Button) findViewById(R.id.tel_toko_9);
        callBTN_10 = (Button) findViewById(R.id.tel_toko_10);
        callBTN_11 = (Button) findViewById(R.id.tel_toko_11);
        callBTN_12 = (Button) findViewById(R.id.tel_toko_12);
        callBTN_13 = (Button) findViewById(R.id.tel_toko_13);

        // Hubungi Nomor HP
        callBTN_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel: 081218916990"));
                startActivity(intent);
            }
        });

        callBTN_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel: (0561) 772717"));
                startActivity(intent);
            }
        });

        callBTN_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel: 081256467779"));
                startActivity(intent);
            }
        });

        callBTN_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel: 085252464488"));
                startActivity(intent);
            }
        });

        callBTN_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel: "));
                startActivity(intent);
            }
        });

        callBTN_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel: (0561) 734533"));
                startActivity(intent);
            }
        });

        callBTN_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel: (0561) 745866"));
                startActivity(intent);
            }
        });

        callBTN_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel: 082255093998"));
                startActivity(intent);
            }
        });

        callBTN_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel: 081345120000"));
                startActivity(intent);
            }
        });

        callBTN_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel: 085368888435"));
                startActivity(intent);
            }
        });

        callBTN_11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel: 0852-4656-7775"));
                startActivity(intent);
            }
        });

        callBTN_12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel: 081352334012"));
                startActivity(intent);
            }
        });

        callBTN_13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel: 082251217900"));
                startActivity(intent);
            }
        });
    }

    // CEK LOKASI
    public void onClick(View view){
        int id=view.getId();
        switch(id){
            // posisi di balik
            case R.id.lokasi_toko_1:
                String lng="109.328683";                            // Bangunan Sinar Karya
                String lat="-0.026245";
                Intent terasIntent=new Intent();
                terasIntent.putExtra("lng",lng);
                terasIntent.putExtra("lat",lat);
                setResult(Activity.RESULT_OK,terasIntent);
                finish();
                break;

            case R.id.lokasi_toko_2:
                //String name=etName.getText().toString();          // Toko Bangunan Ellin
                String lngsafran="109.312805";
                String latsafran="-0.021679";
                Intent safranIntent=new Intent();
                safranIntent.putExtra("lng",lngsafran);
                safranIntent.putExtra("lat",latsafran);
                setResult(Activity.RESULT_OK,safranIntent);
                finish();
                break;

            case R.id.lokasi_toko_3:
                //String name=etName.getText().toString();          // Toko Bangunan Bintang Terang
                String lngsafran_3="109.322362";
                String latsafran_3="-0.030818";
                Intent safranIntent_3=new Intent();
                safranIntent_3.putExtra("lng",lngsafran_3);
                safranIntent_3.putExtra("lat",latsafran_3);
                setResult(Activity.RESULT_OK,safranIntent_3);
                finish();
                break;

            case R.id.lokasi_toko_4:
                //String name=etName.getText().toString();          // Toko Bangunan Dan Showrom keramik
                String lngsafran_4="109.30588";
                String latsafran_4="-0.031586";
                Intent safranIntent_4=new Intent();
                safranIntent_4.putExtra("lng",lngsafran_4);
                safranIntent_4.putExtra("lat",latsafran_4);
                setResult(Activity.RESULT_OK,safranIntent_4);
                finish();
                break;

            case R.id.lokasi_toko_5:
                //String name=etName.getText().toString();          // Toko Bangunan Ace
                String lngsafran_5="109.335458";
                String latsafran_5="-0.032056";
                Intent safranIntent_5=new Intent();
                safranIntent_5.putExtra("lng",lngsafran_5);
                safranIntent_5.putExtra("lat",latsafran_5);
                setResult(Activity.RESULT_OK,safranIntent_5);
                finish();
                break;

            case R.id.lokasi_toko_6:                                // TOKO Bangunanan Gelora Bangunan
                //String name=etName.getText().toString();
                String lngsafran_6="109.331086";
                String latsafran_6="-0.025362";
                Intent safranIntent_6=new Intent();
                safranIntent_6.putExtra("lng",lngsafran_6);
                safranIntent_6.putExtra("lat",latsafran_6);
                setResult(Activity.RESULT_OK,safranIntent_6);
                finish();
                break;

            case R.id.lokasi_toko_7:                                 // Toko Multi Depo
                //String name=etName.getText().toString();
                String lngsafran_7="109.341480";
                String latsafran_7="-0.039248";
                Intent safranIntent_7=new Intent();
                safranIntent_7.putExtra("lng",lngsafran_7);
                safranIntent_7.putExtra("lat",latsafran_7);
                setResult(Activity.RESULT_OK,safranIntent_7);
                finish();
                break;

            case R.id.lokasi_toko_8:                                // Toko Elektronik Jaya
                //String name=etName.getText().toString();
                String lngsafran_8="109.317509";
                String latsafran_8="-0.048355";
                Intent safranIntent_8=new Intent();
                safranIntent_8.putExtra("lng",lngsafran_8);
                safranIntent_8.putExtra("lat",latsafran_8);
                setResult(Activity.RESULT_OK,safranIntent_8);
                finish();
                break;

            case R.id.lokasi_toko_9:                             // Toko Duta Eterna Electronic
                //String name=etName.getText().toString();
                String lngsafran_9="109.326187";
                String latsafran_9="-0.036608";
                Intent safranIntent_9=new Intent();
                safranIntent_9.putExtra("lng",lngsafran_9);
                safranIntent_9.putExtra("lat",latsafran_9);
                setResult(Activity.RESULT_OK,safranIntent_9);
                finish();
                break;

            case R.id.lokasi_toko_10:                               // Toko Jawi Elektronik
                //String name=etName.getText().toString();
                String lngsafran_10="109.321942";
                String latsafran_10="-0.019486";
                Intent safranIntent_10=new Intent();
                safranIntent_10.putExtra("lng",lngsafran_10);
                safranIntent_10.putExtra("lat",latsafran_10);
                setResult(Activity.RESULT_OK,safranIntent_10);
                finish();
                break;

            case R.id.lokasi_toko_11:                           // Toko Glaen Elektornik / Glaen Indo Niaga
                //String name=etName.getText().toString();
                String lngsafran_11="109.334092";
                String latsafran_11="-0.050434";
                Intent safranIntent_11=new Intent();
                safranIntent_11.putExtra("lng",lngsafran_11);
                safranIntent_11.putExtra("lat",latsafran_11);
                setResult(Activity.RESULT_OK,safranIntent_11);
                finish();
                break;

            case R.id.lokasi_toko_12:                            // Toko Ajun Elektronik
                //String name=etName.getText().toString();
                String lngsafran_12="109.321764";
                String latsafran_12="-0.039379";
                Intent safranIntent_12=new Intent();
                safranIntent_12.putExtra("lng",lngsafran_12);
                safranIntent_12.putExtra("lat",latsafran_12);
                setResult(Activity.RESULT_OK,safranIntent_12);
                finish();
                break;

            case R.id.lokasi_toko_13:                           // Toko Lina Perabotan
                //String name=etName.getText().toString();
                String lngsafran_13="109.366868";
                String latsafran_13="-0.027758";
                Intent safranIntent_13=new Intent();
                safranIntent_13.putExtra("lng",lngsafran_13);
                safranIntent_13.putExtra("lat",latsafran_13);
                setResult(Activity.RESULT_OK,safranIntent_13);
                finish();
                break;
        }
    }
}

