package com.example.chatsocket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

import java.util.ArrayList;

import io.socket.client.Socket;

public class ChatActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    EditText edtMessage;
    Button btnSend;

    ChatAdapter adapter;
    ArrayList<String> messages = new ArrayList<>();

    Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        socket = SocketManager.getSocket();

        recyclerView = findViewById(R.id.recyclerChat);
        edtMessage = findViewById(R.id.edtMessage);
        btnSend = findViewById(R.id.btnSend);

        adapter = new ChatAdapter(messages);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        btnSend.setOnClickListener(v -> {
            String msg = edtMessage.getText().toString().trim();
            if (!msg.isEmpty()) {
                socket.emit("send_message", msg);
                edtMessage.setText("");
            }
        });

        socket.on("receive_message", args -> {
            runOnUiThread(() -> {
                try {
                    JSONObject obj = (JSONObject) args[0];
                    String user = obj.getString("user");
                    String msg = obj.getString("message");

                    adapter.addMessage(user + ": " + msg);

                    recyclerView.scrollToPosition(messages.size() - 1);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        socket.off("receive_message");
    }
}
