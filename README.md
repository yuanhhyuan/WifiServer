# WifiServer
1、设置WiFi热点，形成局域网，作为server端，与WiFi client端实现双向通信；2、连接到WiFi路由


一、Wifi热点 
package com.hy.wifihotspot

1、开启WiFi热点

   /**
     * 自定义wifi热点
     *
     * @param enabled 开启or关闭
     * @return
     */
    private boolean setWifiApEnabled(boolean enabled) {
        ......
    }

2、与wificlient双工通信
   通过ServerSocket，监听wificlient端接入进来的socket，实现双工通信。
   
   //监听wificlient
   ServerSocket socServer = new ServerSocket(SERVER_PORT);
   Socket socClient = null;                
   socClient = socServer.accept();

   
    /**
     * 双工通信
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
