package practicaltest02var04.eim.systems.cs.pub.ro.practicaltest02var04;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;


public class ClientThread extends Thread {

    private String address;
    private int port;
    private String url;
    TextView htmlView;

    private Socket socket;

    public ClientThread(String address, int port, String url, TextView htmlView) {
        this.address = address;
        this.port = port;
        this.url = url;
        this.htmlView = htmlView;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(address, port);
            if (socket == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Could not create socket!");
                return;
            }
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            if (bufferedReader == null || printWriter == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Buffered Reader / Print Writer are null!");
                return;
            }
            printWriter.println(url);
            printWriter.flush();

            String html = "", line;
            while ((line = bufferedReader.readLine()) != null) {
                html = html + line + "\n";
            }

            final String finalHtml = html;
            htmlView.post(new Runnable() {
                @Override
                public void run() {
                   
                    htmlView.setText(finalHtml);
                }
            });

        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
                    if (Constants.DEBUG) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }

}