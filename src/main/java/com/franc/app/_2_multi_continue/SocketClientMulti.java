package com.franc.app._2_multi_continue;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

public class SocketClientMulti {
    private static final String SERVER_IP = "127.0.0.1";   // Server IP
    private static final int SERVER_PORT = 53001;          // Server PORT

    public static void main(String[] args) {
        Socket socket = null;
        InputStream request = null;
        OutputStream response = null;

        try {
            // 1. 소켓 생성
            socket = new Socket();

            // 2. 소켓 주소 할당 및 서버로 연결요청
            socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));
            System.out.println("[Socket] Connect Success!!! - " + socket.getInetAddress());


            // 3. 서버와 데이터 송수신
            request = socket.getInputStream();
            response = socket.getOutputStream();

            BufferedReader chat = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                // 3-1. 서버에 데이터 송신
                String chatMessage = chat.readLine();
                if("".equals(chatMessage)) break;
                response.write(chatMessage.getBytes(StandardCharsets.UTF_8));
                response.flush();

                // 3-2. 서버의 데이터 수신
                byte[] data = new byte[16];
                int dataIndex = request.read(data);
                if(0 >= dataIndex) break;
                String requestData = new String(data, 0, dataIndex);
                System.out.println(requestData);
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            // 4. 통신 종료
            try {
                if (request != null) request.close();
                if (response != null) response.close();
                if (socket != null && !socket.isClosed()) socket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
