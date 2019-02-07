package br.edu.ifpb.serviceapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import br.edu.ifpb.serviceapp.service.HttpService;

public class MainActivity extends AppCompatActivity {

    private BroadcastReceiver bReceiver;
    private Button btn;
    private View.OnClickListener onClickAcessar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Iniciando botao Acessar
        btn = (Button) findViewById(R.id.mainactivity_btn_Acessar);
        onClickAcessar = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, HttpService.class);
                TextView email = (TextView) findViewById(R.id.mainactivity_inp_email);
                TextView pass = (TextView) findViewById(R.id.mainactivity_inp_pass);
                i.putExtra("email", email.getText().toString());
                i.putExtra("password", pass.getText().toString());
                startService(i);
                desativarBotao();
            }
        };
        ativarBotao();

        // Iniciando BroadcasReceiver
        bReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean success = intent.getExtras().getBoolean("success");
                toastResult(success);
                ativarBotao();
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Registrando BroadcastReceiver
        this.registerReceiver(bReceiver, new IntentFilter("android.intent.action.MAIN"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Desregistrando BroadcasReceiver para liberar recurso
        this.unregisterReceiver(bReceiver);
    }

    private void ativarBotao(){
        btn.setOnClickListener(onClickAcessar);
        btn.setText("Acessar");
        btn.setActivated(true);
    }

    private void desativarBotao(){
        btn.setOnClickListener(null);
        btn.setText("Aguarde ...");
        btn.setActivated(false);
    }

    private void toastResult(boolean success){
        String mensagem = success ? "Sucesso" : "Request Fracassada";
        Toast.makeText(getApplicationContext(), mensagem, Toast.LENGTH_LONG).show();
    }

}
