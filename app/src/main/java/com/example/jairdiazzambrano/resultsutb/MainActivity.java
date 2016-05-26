package com.example.jairdiazzambrano.resultsutb;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnListarE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnListarE = (Button) findViewById(R.id.btnListarEquipos);
        btnListarE.setOnClickListener(this);

    }

    @Override
    public void onClick (View boton){
if (boton.getId() == findViewById(R.id.btnListarEquipos).getId()){
    Intent obtnListarEquipos = new Intent (this, EquiposActivity.class);
    startActivity(obtnListarEquipos);

}
    }
}
