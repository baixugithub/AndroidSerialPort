package android.serialport;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MyserialActivity extends Activity {
	EditText mReception;
	FileOutputStream mOutputStream;
	FileInputStream mInputStream;
    SerialPort sp;
    ReadThread  mReadThread;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final Button buttonOpenSerial = (Button)findViewById(R.id.bt_serial_open);
        buttonOpenSerial.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v) {
            mReception = (EditText) findViewById(R.id.EditTextEmission);

            try {
			    sp=new SerialPort(new File("/dev/ttyAMA0"), 115200, 0);
			} catch (SecurityException | IOException e) {
				e.printStackTrace();
			}

            // mReadThread = new ReadThread();
			//	mReadThread.start();
		      
		      mInputStream=(FileInputStream) sp.getInputStream();
			
		     Toast.makeText(getApplicationContext(), "串口打开成功",
		    		     Toast.LENGTH_SHORT).show();
			
		}
	    });
    

        final Button buttonsend= (Button)findViewById(R.id.bt_serial_send);
        buttonsend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                try {
                    mOutputStream=(FileOutputStream) sp.getOutputStream();

                    mOutputStream.write(mReception.getText().toString().getBytes());
                    mOutputStream.write('\n');
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Toast.makeText(MyserialActivity.this, "发送数据 " + mReception + " 成功", Toast.LENGTH_SHORT).show();
            }
        });

        final Button buttonrec= (Button)findViewById(R.id.bt_serial_receive);
        buttonrec.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int size;
                try {
                    byte[] buffer = new byte[64];
                    if (mInputStream == null) return;
                    size = mInputStream.read(buffer);
                    if (size > 0) {
                        onDataReceived(buffer, size);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    
    
    private class ReadThread extends Thread {

		@Override
		public void run() {
			super.run();
			while(!isInterrupted()) {
				int size;
				/*try {
					byte[] buffer = new byte[64];
					if (mInputStream == null) return;
					size = mInputStream.read(buffer);
					if (size > 0) {
						onDataReceived(buffer, size);
						
					}
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}*/
				//mReception.append(new String("recive"));
			}
		}
	}

    void onDataReceived(final byte[] buffer, final int size) {
		runOnUiThread(new Runnable() {
			public void run() {
				if (mReception != null) {
					mReception.append(new String(buffer, 0, size));
				}
			}
		});
	}

}