package android.serialport;

public class MyFunc {
  public static String Byte2Hex(Byte paramByte) {
        return String.format("%02x", new Object[] { paramByte }).toUpperCase();
    }

  public static String ByteArrToHex(byte[] paramArrayOfByte) {
    StringBuilder localStringBuilder = new StringBuilder();
    int j = paramArrayOfByte.length;
    int i = 0;
    for (;;) {
      if (i >= j) {
        return localStringBuilder.toString();
      }
      localStringBuilder.append(Byte2Hex(Byte.valueOf(paramArrayOfByte[i])));
      localStringBuilder.append(" ");
      i += 1;
    }
  }

  public static String ByteArrToHex(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
    StringBuilder localStringBuilder = new StringBuilder();
    for (;;) {
      if (paramInt1 >= paramInt2) {
        return localStringBuilder.toString();
      }
      localStringBuilder.append(Byte2Hex(Byte.valueOf(paramArrayOfByte[paramInt1])));
      paramInt1 += 1;
    }
  }

  public static byte HexToByte(String paramString) {
    return (byte)Integer.parseInt(paramString, 16);
  }

  public static byte[] HexToByteArr(String paramString) {
    int i = paramString.length();
    byte[] arrayOfByte = null;
    int k = 0;
    int j = 0;
    if (isOdd(i) == 1) {
      i += 1;
      arrayOfByte = new byte[i / 2];
      paramString = "0" + paramString;
      k = 0;
      j = 0;
    }
    for (;;) {
      if (j >= i) {
        return arrayOfByte;
//        arrayOfByte = new byte[i / 2];
//        break;
      }
      arrayOfByte[k] = HexToByte(paramString.substring(j, j + 2));
      k += 1;
      j += 2;
    }
  }

  public static int HexToInt(String paramString) {
    return Integer.parseInt(paramString, 16);
  }

  public static int isOdd(int paramInt) {
    return paramInt & 0x1;
  }
}


/* Location:              C:\Users\JP\Desktop\安卓开发必备\安卓反编译\代码\dex2jar-2.0\classes-dex2jar.jar!\com\bjw\ComAssistant\MyFunc.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */