package android.serialport;

import android.serialport.bean.ComBean;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;

public abstract class SerialHelper {
    private byte[] _bLoopData = { 48 };
    private boolean _isOpen = false;
    private int iBaudRate = 9600;
    private int iDelay = 500;
    private InputStream mInputStream;
    private OutputStream mOutputStream;
    private ReadThread mReadThread;
    private SendThread mSendThread;
    private SerialPort mSerialPort;
    private String sPort = "/dev/s3c2410_serial0";

    public SerialHelper() {
        this("/dev/s3c2410_serial0", 9600);
    }

    public SerialHelper(String paramString) {
        this(paramString, 9600);
    }

  public SerialHelper(String paramString, int paramInt) {
    this.sPort = paramString;
    this.iBaudRate = paramInt;
  }

  public SerialHelper(String paramString1, String paramString2) {
    this(paramString1, Integer.parseInt(paramString2));
  }

  public void close() {
    if (this.mReadThread != null) {
      this.mReadThread.interrupt();
    }
    if (this.mSerialPort != null) {
      this.mSerialPort.close();
      this.mSerialPort = null;
    }
    this._isOpen = false;
  }

  public int getBaudRate() {
    return this.iBaudRate;
  }

  public String getPort() {
    return this.sPort;
  }

  public byte[] getbLoopData() {
    return this._bLoopData;
  }

  public int getiDelay() {
    return this.iDelay;
  }

  public boolean isOpen() {
    return this._isOpen;
  }

  protected abstract void onDataReceived(ComBean paramComBean);

  public void open() throws SecurityException, IOException, InvalidParameterException {
    mSerialPort = new SerialPort(new File(this.sPort), this.iBaudRate, 0);
    mOutputStream = this.mSerialPort.getOutputStream();
    mInputStream = this.mSerialPort.getInputStream();
    mReadThread = new ReadThread();
    mReadThread.start();
    mSendThread = new SendThread();
    mSendThread.setSuspendFlag();
    mSendThread.start();
    _isOpen = true;
  }

  public void send(byte[] paramArrayOfByte) {
    try {
      this.mOutputStream.write(paramArrayOfByte);
      return;
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void sendHex(String paramString) {
    send(MyFunc.HexToByteArr(paramString));
  }

  public void sendTxt(String paramString) {
    send(paramString.getBytes());
  }

  public boolean setBaudRate(int paramInt) {
    if (this._isOpen) {
      return false;
    }
    this.iBaudRate = paramInt;
    return true;
  }

  public boolean setBaudRate(String paramString) {
    return setBaudRate(Integer.parseInt(paramString));
  }

  public void setHexLoopData(String paramString) {
    this._bLoopData = MyFunc.HexToByteArr(paramString);
  }

  public boolean setPort(String paramString) {
    if (this._isOpen) {
      return false;
    }
    this.sPort = paramString;
    return true;
  }

  public void setTxtLoopData(String paramString) {
    this._bLoopData = paramString.getBytes();
  }

  public void setbLoopData(byte[] paramArrayOfByte) {
    this._bLoopData = paramArrayOfByte;
  }

  public void setiDelay(int paramInt) {
    this.iDelay = paramInt;
  }

  public void startSend() {
    if (this.mSendThread != null) {
      this.mSendThread.setResume();
    }
  }

  public void stopSend() {
    if (this.mSendThread != null) {
      this.mSendThread.setSuspendFlag();
    }
  }

  private class ReadThread extends Thread {
    private ReadThread() {}

    public void run() {
      super.run();
      for (;;) {
        if (isInterrupted()) {
          return;
        }
        try {
          if (SerialHelper.this.mInputStream == null) {
            break ;
          }
          Object localObject = new byte['Ȁ'];
          int i = SerialHelper.this.mInputStream.read((byte[])localObject);
          if (i > 0) {
            localObject = new ComBean(SerialHelper.this.sPort, (byte[])localObject, i);
            SerialHelper.this.onDataReceived((ComBean)localObject);
          }
          try {
            Thread.sleep(50L);
          }
          catch (InterruptedException localInterruptedException) {
            localInterruptedException.printStackTrace();
          }
        }
        catch (Throwable localThrowable) {
          localThrowable.printStackTrace();
        }
      }
    }
  }

  private class SendThread extends Thread {
    public boolean suspendFlag = true;

    /* Error */
    public void run() {
      // Byte code:
      //   0: aload_0
      //   1: invokespecial 29	java/lang/Thread:run	()V
      //   4: aload_0
      //   5: invokevirtual 33	com/bjw/ComAssistant/SerialHelper$SendThread:isInterrupted	()Z
      //   8: ifeq +4 -> 12
      //   11: return
      //   12: aload_0
      //   13: monitorenter
      //   14: aload_0
      //   15: getfield 20	com/bjw/ComAssistant/SerialHelper$SendThread:suspendFlag	Z
      //   18: ifne +41 -> 59
      //   21: aload_0
      //   22: monitorexit
      //   23: aload_0
      //   24: getfield 15	com/bjw/ComAssistant/SerialHelper$SendThread:this$0	Lcom/bjw/ComAssistant/SerialHelper;
      //   27: aload_0
      //   28: getfield 15	com/bjw/ComAssistant/SerialHelper$SendThread:this$0	Lcom/bjw/ComAssistant/SerialHelper;
      //   31: invokevirtual 37	com/bjw/ComAssistant/SerialHelper:getbLoopData	()[B
      //   34: invokevirtual 41	com/bjw/ComAssistant/SerialHelper:send	([B)V
      //   37: aload_0
      //   38: getfield 15	com/bjw/ComAssistant/SerialHelper$SendThread:this$0	Lcom/bjw/ComAssistant/SerialHelper;
      //   41: invokestatic 45	com/bjw/ComAssistant/SerialHelper:access$2	(Lcom/bjw/ComAssistant/SerialHelper;)I
      //   44: i2l
      //   45: invokestatic 49	java/lang/Thread:sleep	(J)V
      //   48: goto -44 -> 4
      //   51: astore_1
      //   52: aload_1
      //   53: invokevirtual 52	java/lang/InterruptedException:printStackTrace	()V
      //   56: goto -52 -> 4
      //   59: aload_0
      //   60: invokevirtual 57	java/lang/Object:wait	()V
      //   63: goto -49 -> 14
      //   66: astore_1
      //   67: aload_1
      //   68: invokevirtual 52	java/lang/InterruptedException:printStackTrace	()V
      //   71: goto -57 -> 14
      //   74: astore_1
      //   75: aload_0
      //   76: monitorexit
      //   77: aload_1
      //   78: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	79	0	this	SendThread
      //   51	2	1	localInterruptedException1	InterruptedException
      //   66	2	1	localInterruptedException2	InterruptedException
      //   74	4	1	localObject	Object
      // Exception table:
      //   from	to	target	type
      //   37	48	51	java/lang/InterruptedException
      //   59	63	66	java/lang/InterruptedException
      //   14	23	74	finally
      //   59	63	74	finally
      //   67	71	74	finally
      //   75	77	74	finally
    }

    public void setResume() {
        this.suspendFlag = false;
    }

    public void setSuspendFlag() {
      this.suspendFlag = true;
    }
  }

}