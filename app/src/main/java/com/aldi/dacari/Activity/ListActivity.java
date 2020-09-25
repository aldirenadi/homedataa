package com.aldi.dacari.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.aldi.dacari.Adapter.ExpandableListAdapter;
import com.aldi.dacari.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private ExpandableListAdapter listAdapter;
    private ExpandableListView listView;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listHashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        listView = (ExpandableListView)findViewById(R.id.lvExp);
        initData();
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listHashMap);
        listView.setAdapter(listAdapter);
    }

    private void initData() {
        listDataHeader = new ArrayList<>();
        listHashMap = new HashMap<>();

        listDataHeader.add("Pengguna");
        listDataHeader.add("Isi Postingan");
//        listDataHeader.add("IOS");
//        listDataHeader.add("Google MAP");


        List<String> list_text_1 = new ArrayList<>();
        list_text_1.add("(1) Homeda tidak memiliki tanggung jawab atas kegiatan transaksi keuangan yang dilakukan antara penyedia jasa dan pencari jasa");
        list_text_1.add("(2) Homeda tidak bertanggung jawab atas interaksi antara anda dengan pihak ketiga ataupun di luar Homeda");
        list_text_1.add("(3) Homeda tidak menjamin posting yang dilakukan benar-benar dapat membuat penyedia jasa memiliki pekerjaan");
        list_text_1.add("(4) Pengguna diwajibkan merahasian password kepada siapapun. Homeda tidak bertanggung jawab atas kerugian apabila terjadi kelalaian oleh pengguna dalam menjaga kerahasiaan passwordnya");
        list_text_1.add("(5) Dilarang keras mencemarkan nama baik Homeda baik di dalam aplikasi ataupun di luar aplikasi");
        list_text_1.add("(6) Penyedia Jasa wajib merespon dari pencari jasa, baik itu berupa telepon maupun chatting");
        list_text_1.add("(7) Disarankan untuk bertemu langsung apabila merasa cocok dengan pencari jasa untuk melakukan obrolan selanjutnya");
        list_text_1.add("(8) Dengan melakukan aktivitas melalui Homeda, pengguna menyatakan setuju untuk tunduk dan terikat pada syarat dan ketentuan umum yang berlaku");
        list_text_1.add("(9) Pengguna diharapkan untuk memeriksa halaman ini dari waktu ke waktu untuk memperhatikan setiap perubahan, karena hal tersebut mengikat anda sebagai pengguna");

        List<String> list_text_2 = new ArrayList<>();
        list_text_2.add("(1) Wajib memasukkan nama jasa Anda, contoh : Jasa potong rumput harian");
        list_text_2.add("(2) Wajib memasukkan kategori yang tersedia");
        list_text_2.add("(3) Wajib mendeskripsikan secara detail jasa yang Anda posting");
        list_text_2.add("(4) Wajib memasukkan Kisaran bayaran");
        list_text_2.add("(5) Wajib memasukkan Berapa tahun pengalaman anda di jasa tersebut");
        list_text_2.add("(6) Jika anda memliki layanan jasa, silahkan tambahkan sebagai point tambahan jasa Anda");
        list_text_2.add("(7) Wajib memasukkan Gambar Pamflet/promosi yang mendukung jasa Anda");
        list_text_2.add("(8) Jika point di atas tidak terpenuhi, maka postingan Anda tidak berhasil terupload");

//        List<String> xamarin = new ArrayList<>();
//        xamarin.add("xamarin Expandable ListView");
//        xamarin.add("xamarin Google MAP");
//        xamarin.add("xamarin This  is IOS");
//        xamarin.add("xamarin Application");
//
//        List<String> uwp = new ArrayList<>();
//        uwp.add("uwp Expandable ListView");
//        uwp.add("uwp Google MAP");
//        uwp.add("uwp This  is IOS");
//        uwp.add("uwp Application");

        listHashMap.put(listDataHeader.get(0), list_text_1);
        listHashMap.put(listDataHeader.get(1), list_text_2);
//        listHashMap.put(listDataHeader.get(2), xamarin);
//        listHashMap.put(listDataHeader.get(3), uwp);

        ImageView back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
