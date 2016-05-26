package com.example.jairdiazzambrano.resultsutb;
import android.net.LocalSocketAddress;
import android.os.AsyncTask;
import android.os.Bundle;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.app.ProgressDialog;
import android.content.Intent;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by jairdiazzambrano on 26/05/16.
 */
public class EquiposActivity extends Activity implements View.OnClickListener{

    GridView gvequipos;
    Button button;
    private ProgressDialog Dialogo;
    private static String SOAP_ACTION = "http://tempuri.org/ListadoEquipos";
    private static String NAMESPACE = "http://tempuri.org/";
    private static String METHOD_NAME = "ListadoEquipos";
    private static String URL = "http://10.0.2.2/Web/ServicioClientes.asmx?WSDL";
    private String [] cargardatos;

    @Override
    protected void onCreate (Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipo);

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);

        gvequipos = (GridView) findViewById(R.id.gvEquipos);
        new asynequipos().execute();

    }

    //------ conectamos con la WS DB
    public Boolean invocarWS(){
        Boolean bandera = true;
        try{
            SoapObject respuesta = new SoapObject(NAMESPACE , METHOD_NAME);
            SoapSerializationEnvelope sobre = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            sobre.dotNet= true;
            sobre.setOutputSoapObject(respuesta);
            HttpTransportSE transporte = new HttpTransportSE(URL);
            transporte.call(SOAP_ACTION , sobre);

            SoapObject response = (SoapObject) sobre.getResponse();
          SoapObject ArrayOfTeam = (SoapObject) response.getProperty("ArrayOfTeam");
            SoapObject Team = (SoapObject) ArrayOfTeam.getProperty("Team");



            Log.e("valor de response", response.toString());
            Log.e("valor de ArrayofTeam", ArrayOfTeam.toString());
            Log.e("valor de newdataset", Team.toString());

            cargardatos = new String [Team.getPropertyCount() * 4 ];
            int fila = 0;
            for (int i = 0 ; i< Team.getPropertyCount(); i++ ){
                SoapObject datosxml = (SoapObject) Team.getProperty(i);
                Log.e("Valor de nombre", datosxml.getProperty(1).toString());
                Log.e("Valor de codigo", datosxml.getProperty(0).toString());
                Log.e("Valor de email", datosxml.getProperty(3).toString());
                Log.e("Valor de telefono", datosxml.getProperty(2).toString());

                cargardatos[fila]= datosxml.getProperty(1).toString();
                cargardatos[fila+1]= datosxml.getProperty(0).toString();
                cargardatos[fila+2]= datosxml.getProperty(3).toString();
                cargardatos[fila+3]= datosxml.getProperty(2).toString();

                fila+=4;

            }



        }catch(IOException e){

            e.printStackTrace();
            bandera= false;
        } catch (XmlPullParserException e){

            e.printStackTrace();;
            bandera = false;
        }

        return bandera;
    }

    //---- cargar datos en el Gridview

    public void cargar_elementos(){

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,cargardatos);
        gvequipos.setAdapter(adaptador);
    }
    //----- clase para sincronizar tareas

    class asynequipos extends AsyncTask <String, String, String>{

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            Dialogo = new ProgressDialog(EquiposActivity.this);
            Dialogo.setMessage("Cargando Datos");
            Dialogo.setIndeterminate(false);
            Dialogo.setCancelable(false);
            Dialogo.show();
        }
        @Override
        protected String doInBackground (String...params){
            if (invocarWS()){
                return "ok";

            }else{
                return "err";
            }
        }

@Override
        protected  void onPostExecute (String result){

    Dialogo.dismiss();
    if (result.equals("ok")){
        //cargar datos de grid
        cargar_elementos();

    }

}





    }

    @Override
    public void onClick (View boton){

        if (boton.getId() == findViewById(R.id.button).getId()){
            Intent buttonoprincipal = new Intent(this, MainActivity.class);
            startActivity(buttonoprincipal);
            finish();

        }
    }
}
