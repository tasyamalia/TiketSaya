package com.tasya.tiketsaya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Random;

public class TicketCheckoutAct extends AppCompatActivity {

    Button btn_pay_ticket, btnMinus, btnPlus;
    LinearLayout btn_back;
    TextView textJumlahTiket, textTotalHarga, textMyBalance, nama_wisata, lokasi, ketentuan;
    ImageView notice_uang;
    Integer valueJumlahTiket = 1;
    Integer myBalance  = 0;
    Integer valueTotalHarga = 0;
    Integer valueHargaTiket = 0;
    Integer sisa_balance = 0;

    String date_wisata="";
    String time_wisata="";

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    //generate nomor integer secara random
    //karena kita ingin membuat transaksi secara unik
    Integer nomor_transaksi = new Random().nextInt();

    DatabaseReference reference, reference2, reference3, reference4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_checkout);

        getUsernameLocal();
        //Mengambil data dari intent
        Bundle bundle = getIntent().getExtras();
        final String jenis_tiket_baru = bundle.getString("jenis_tiket");

        btnMinus = findViewById(R.id.btnMinus);
        btnPlus = findViewById(R.id.btnPlus);
        textJumlahTiket = findViewById(R.id.textJumlahTiket);
        textTotalHarga = findViewById(R.id.textTotalHarga);
        textMyBalance = findViewById(R.id.textMyBalance);
        btn_pay_ticket= findViewById(R.id.btn_pay_ticket);
        notice_uang = findViewById(R.id.notice_uang);
        nama_wisata = findViewById(R.id.nama_wisata);
        lokasi= findViewById(R.id.lokasi);
        ketentuan = findViewById(R.id.ketentuan);
        btn_back = findViewById(R.id.btn_back);

        //Setting value baru untuk beberapa komponen
        textJumlahTiket.setText(valueJumlahTiket.toString());


        //secara default hide btn awal
        btnMinus.animate().alpha(0).setDuration(300).start();
        btnMinus.setEnabled(false);
        notice_uang.setVisibility(View.GONE);

        //Mengambil data user dari firebase
        reference2 = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myBalance= Integer.valueOf(dataSnapshot.child("user_balance").getValue().toString());
                textMyBalance.setText("US$ " +myBalance+ "");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Mengambil data dari firebase berdasarkan intent
        reference = FirebaseDatabase.getInstance().getReference().child("Wisata").child(jenis_tiket_baru);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //menimpa data yang ada dengan data yang baru
                nama_wisata.setText(dataSnapshot.child("nama_wisata").getValue().toString());
                lokasi.setText(dataSnapshot.child("lokasi").getValue().toString());
                ketentuan.setText(dataSnapshot.child("ketentuan").getValue().toString());
                date_wisata = dataSnapshot.child("date_wisata").getValue().toString();
                time_wisata = dataSnapshot.child("time_wisata").getValue().toString();
                valueHargaTiket= Integer.valueOf(dataSnapshot.child("harga_tiket").getValue().toString());
                valueTotalHarga = valueHargaTiket * valueJumlahTiket;
                textTotalHarga.setText("US$ "+ valueTotalHarga+"");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valueJumlahTiket-=1;
                textJumlahTiket.setText(valueJumlahTiket.toString());
                if(valueJumlahTiket<2){
                    btnMinus.animate().alpha(0).setDuration(300).start();
                    btnMinus.setEnabled(false);
                }
                valueTotalHarga = valueHargaTiket * valueJumlahTiket;
                textTotalHarga.setText("US$ "+ valueTotalHarga+"");
                if(valueTotalHarga < myBalance){
                    btn_pay_ticket.animate().translationY(0).alpha(1).setDuration(350).start();
                    btn_pay_ticket.setEnabled(true);
                    textMyBalance.setTextColor(Color.parseColor("#203DD1"));
                    notice_uang.setVisibility(View.GONE);
                }
            }
        });
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valueJumlahTiket+=1;
                textJumlahTiket.setText(valueJumlahTiket.toString());
                if(valueJumlahTiket>1){
                    btnMinus.animate().alpha(1).setDuration(300).start();
                    btnMinus.setEnabled(true);
                }
                valueTotalHarga = valueHargaTiket * valueJumlahTiket;
                textTotalHarga.setText("US$ "+ valueTotalHarga+"");
                if(valueTotalHarga > myBalance){
                    btn_pay_ticket.animate().translationY(250).alpha(0).setDuration(350).start();
                    btn_pay_ticket.setEnabled(false);
                    textMyBalance.setTextColor(Color.parseColor("#D1206B"));
                    notice_uang.setVisibility(View.VISIBLE);
                }
            }
        });

        btn_pay_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Menyimpan data user kepad firebase dan membuat table baru "MyTickets"
                reference3 = FirebaseDatabase.getInstance().getReference()
                        .child("MyTickets").child(username_key_new).child(nama_wisata.getText().toString()+ nomor_transaksi);
                reference3.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        reference3.getRef().child("id_tiket").setValue(nama_wisata.getText().toString()+ nomor_transaksi);
                        reference3.getRef().child("nama_wisata").setValue(nama_wisata.getText().toString());
                        reference3.getRef().child("lokasi").setValue(lokasi.getText().toString());
                        reference3.getRef().child("ketentuan").setValue(ketentuan.getText().toString());
                        reference3.getRef().child("jumlah_tiket").setValue(valueJumlahTiket.toString());
                        reference3.getRef().child("date_wisata").setValue(date_wisata);
                        reference3.getRef().child("time_wisata").setValue(time_wisata);


                        Intent gotosuccesspay = new Intent(TicketCheckoutAct.this, SuccessBuyTicketAct.class);
                        startActivity(gotosuccesspay);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                //Update data balance kepada users (yang saat ini login)
                //Mengambil data user dari firebase
                reference4 = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
                reference4.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        sisa_balance = myBalance - valueTotalHarga;
                        reference4.getRef().child("user_balance").setValue(sisa_balance);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key,"");
    }
}
