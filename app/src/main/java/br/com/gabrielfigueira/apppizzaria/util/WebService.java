package br.com.gabrielfigueira.apppizzaria.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class WebService extends AsyncTask<String, String, String> {
    private ProgressDialog dlg;

    private Context contexto;

    public WebService (Context contexto){
        this.contexto = contexto;
    }

    @Override
    protected void onPreExecute() {
        dlg = ProgressDialog.show(contexto, "Aguarde...", "Conectando ao servidor...");
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            URL url = new URL(params[1]);

//            http://pizzariaapi.herokuapp.com/api/clientes/salvar
//            if (params.length == 1){
//                url = new URL("http://172.25.3.128:8080/LivroREST/livro/" + params[0]);
//            }else{
//                url = new URL("http://172.25.3.128:8080/LivroREST/livro/" + params[0] + "/" + params[1]);
//            }

            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            OutputStreamWriter streamWriter = new OutputStreamWriter(con.getOutputStream());
            streamWriter.write(params[2].toString());
            streamWriter.flush();
            StringBuilder stringBuilder = new StringBuilder();
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStreamReader streamReader = new InputStreamReader(con.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(streamReader);
                String response = null;
                while ((response = bufferedReader.readLine()) != null) {
                    stringBuilder.append(response + "\n");
                }
                bufferedReader.close();

                Log.d("test", stringBuilder.toString());
                return stringBuilder.toString();
            } else {
                Log.e("test", con.getResponseMessage());
                return null;
            }
        } catch (Exception e) {
            Log.e("ERRO", e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        dlg.dismiss();
    }
}