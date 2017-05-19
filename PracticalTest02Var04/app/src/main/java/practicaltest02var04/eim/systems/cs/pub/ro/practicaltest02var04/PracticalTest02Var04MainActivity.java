package practicaltest02var04.eim.systems.cs.pub.ro.practicaltest02var04;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PracticalTest02Var04MainActivity extends AppCompatActivity {

    private Button start, get;
    private EditText port, url;
    private TextView html;
    private ServerThread serverThread;
    private ClientThread clientThread;

    private StartButtonClickListener startButtonClickListener = new StartButtonClickListener();

    private class StartButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String serverPort = port.getText().toString();
            if (serverPort == null || serverPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Server port should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            serverThread = new ServerThread(Integer.parseInt(serverPort));
            if (serverThread.getServerSocket() == null) {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not create server thread!");
                return;
            }
            serverThread.start();
        }

    }

    private GetHTMLButtonClickListener getHTMLButtonClickListener = new GetHTMLButtonClickListener();

    private class GetHTMLButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String clientAddress = "localhost";
            String clientPort = port.getText().toString();
            String link = url.getText().toString();
            if (clientAddress == null || clientAddress.isEmpty()
                    || clientPort == null || clientPort.isEmpty()
                    || link == null || link.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Client connection parameters should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (serverThread == null || !serverThread.isAlive()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                return;
            }


            clientThread = new ClientThread(
                    clientAddress, Integer.parseInt(clientPort), link, html
            );
            clientThread.start();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_var04_main);

        start = (Button) findViewById(R.id.start);
        get = (Button) findViewById(R.id.get);

        port = (EditText) findViewById(R.id.port);
        url = (EditText) findViewById(R.id.url);

        html = (TextView) findViewById(R.id.html);

        start.setOnClickListener(startButtonClickListener);
        get.setOnClickListener(getHTMLButtonClickListener);

    }

    @Override
    protected void onDestroy() {
        Log.i(Constants.TAG, "[MAIN ACTIVITY] onDestroy() callback method has been invoked");
        if (serverThread != null) {
            serverThread.stopThread();
        }
        super.onDestroy();

    }
}
