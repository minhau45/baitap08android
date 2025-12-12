package com.example.chatsocket;

import android.util.Log;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class SocketManager {

    private static Socket socket;

    public static Socket getSocket() {
        if (socket == null) {
            try {
                socket = IO.socket("http://server.example.com");
            } catch (URISyntaxException e) {
                Log.e("SOCKET", e.getMessage());
            }
        }
        return socket;
    }
}
