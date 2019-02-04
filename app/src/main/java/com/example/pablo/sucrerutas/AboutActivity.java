package com.example.pablo.sucrerutas;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AboutActivity extends AppCompatActivity {
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView tvGitHub = findViewById(R.id.github);
        tvGitHub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://github.com/PabloNoya/SucreRutas"));
                startActivity(intent);
            }
        });

        TextView tvNombre = findViewById(R.id.nombre);
        tvNombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AboutActivity.this, "Contáctame :D", Toast.LENGTH_SHORT).show();
            }
        });

        TextView tvMail = findViewById(R.id.mail);
        tvMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarMail();
            }
        });

        TextView tvCall = findViewById(R.id.llamada);
        tvCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel: 70414607"));
                startActivity(intent);
            }
        });

        RelativeLayout tvThanks = findViewById(R.id.thanks);
        tvThanks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AboutActivity.this, "¡Gracias!", Toast.LENGTH_SHORT).show();
            }
        });

        TextView tvFacebook = findViewById(R.id.facebook);
        tvFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://www.facebook.com/GDGAndroidBolivia"));
                startActivity(intent);
            }
        });

        TextView tvWeb = findViewById(R.id.web);
        tvWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://camp.androidbolivia.com"));
                startActivity(intent);
            }
        });
    }

    public void enviarMail(){
        String [] TO = { "pablonoya18@gmail.com" };
        String asunto = "Sucre Rutas";

        intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, TO);
        intent.putExtra(Intent.EXTRA_SUBJECT, asunto);
        startActivity(Intent.createChooser(intent, "Enviar sugerencia"));
    }
}
