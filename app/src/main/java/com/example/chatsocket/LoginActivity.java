package com.example.chatsocket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import io.socket.client.Socket;

public class LoginActivity extends AppCompatActivity {

    EditText edtName;
    Button btnLogin;

    Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtName = findViewById(R.id.edtName);
        btnLogin = findViewById(R.id.btnLogin);

        socket = SocketManager.getSocket();
        socket.connect();

        btnLogin.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            if (name.isEmpty()) {
                Toast.makeText(this, "Nhập tên!", Toast.LENGTH_SHORT).show();
                return;
            }
            socket.emit("user_login", name);
        });

        socket.on("login_success", args -> {
            runOnUiThread(() -> {
                Intent i = new Intent(this, ChatActivity.class);
                i.putExtra("username", edtName.getText().toString());
                startActivity(i);
                finish();
            });
        });

        socket.on("login_failed", args -> {
            runOnUiThread(() ->
                    Toast.makeText(this, "Tên đã tồn tại!", Toast.LENGTH_SHORT).show()
            );
        });
    }
}
