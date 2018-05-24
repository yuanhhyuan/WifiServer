package com.hy.wifihotspot;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.flystudio.wifihotspot.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

/**
采用TCP协议的socket，在wifi形成的局域网内，创建服务端
 */
public class ServerActivity  extends ActionBarActivity {
    String TAG = "060_ServerActivity";
    private TextView tvClientMsg, tvServerIP, tvServerPort;
    private final int SERVER_PORT = 8080;
    private String Server_Name = "Kingspp";
    Button clear;
    Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        tvClientMsg = (TextView) findViewById(R.id.textViewClientMessage);
        tvServerIP = (TextView) findViewById(R.id.textViewServerIP);
        tvServerPort = (TextView) findViewById(R.id.textViewServerPort);
        tvServerPort.setText(Integer.toString(SERVER_PORT));
        getDeviceIpAddress();

        clear = (Button)findViewById(R.id.button1);
        clear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                tvClientMsg.setText("");

            }
        });



        new Thread(new Runnable() {

            @Override
            public void run() {

                try {
                    ServerSocket socServer = new ServerSocket(SERVER_PORT);
                    Socket socClient = null;
                    while (true) {
                        socClient = socServer.accept();
                        ServerAsyncTask serverAsyncTask = new ServerAsyncTask();
                        serverAsyncTask.execute(new Socket[] { socClient });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * Get ip address of the device
     */
    public void getDeviceIpAddress() {
        try {

            for (Enumeration<NetworkInterface> enumeration = NetworkInterface
                    .getNetworkInterfaces(); enumeration.hasMoreElements();) {
                NetworkInterface networkInterface = enumeration.nextElement();
                for (Enumeration<InetAddress> enumerationIpAddr = networkInterface
                        .getInetAddresses(); enumerationIpAddr
                             .hasMoreElements();) {
                    InetAddress inetAddress = enumerationIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            && inetAddress.getAddress().length == 4) {
                        tvServerIP.setText(inetAddress.getHostAddress());
                    }
                }
            }
        } catch (SocketException e) {
            Log.e("ERROR:", e.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * AsyncTask which handles the commiunication with clients
     *
     * 双工
     */
    class ServerAsyncTask extends AsyncTask<Socket, Void, String> {

        //在新的工作线程中执行与client数据交互任务
        @Override
        protected String doInBackground(Socket... params) {
            String result = null;
            Socket mySocket = params[0];
            try {
                Log.e(TAG, "ServerAsyncTask doInBackground");
                //向client传输数据
                PrintWriter out = new PrintWriter(mySocket.getOutputStream(),
                        true);
                out.println("Welcome to \""+Server_Name+"\" Server");

                //获取client传输过来的数据。通过IO流（字符流）的形式传输。
                InputStream is = mySocket.getInputStream();
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(is));
                result = br.readLine();

                //关闭
                mySocket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        //在UI线程中获得doInBackground读取到的client传输过来的数据
        @Override
        protected void onPostExecute(String s) {

            Log.e(TAG, "ServerAsyncTask onPostExecute，client传输过来的数据 s : " + s);
//            tvClientMsg.append(s+"\n");
            tvClientMsg.setText(s+"\n");
        }
    }


}
