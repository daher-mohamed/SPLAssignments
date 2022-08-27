package bgu.spl.net.api.bidi;

import bgu.spl.net.api.MessageEncoderDecoder;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class EncoderDecoderMessage<T> implements MessageEncoderDecoder<T> {
    private byte[] bytes = new byte[1 << 10]; //start with 1k
    private byte[] opcodeBytes = new byte[2];
    byte[] numberArray = new byte[2];
    private int len = 0;
  //  private int zeroBytesSoFarAfterTheFirstTwoBytes = 0;
    private int firstTwoBytes = 0;
    private short opcode = -1;


    @Override
    public T decodeNextByte(byte nextByte) {
        String res = "";
        if (firstTwoBytes < 2) {
            opcodeBytes[firstTwoBytes++] = nextByte;
        }
        if (firstTwoBytes == 2) {
            opcode = this.bytesToShort(opcodeBytes);
            firstTwoBytes = firstTwoBytes + 1;
        }
        else if (opcode != -1) {
                if (nextByte == '\0') {
                    nextByte = ' ';
                    pushByte(nextByte);
                }
                else if(nextByte==';')//Return the message;
                {
                    res = popString();
                    reset();
                    return (T)res;
                }
                else
                    pushByte(nextByte);
            }

        return null;
        }

        //THis encoder work just for ERROR and ACK
    @Override
    public byte[] encode(T message) {

      String str = (String) message;
      String strArray[]=str.split(" ");
      short Opcode=(short) Integer.parseInt(strArray[1]);
       byte[] b1=shortToByte(Opcode);
       byte[] b2=shortToByte((short) Integer.parseInt(strArray[0]));
       byte[] b3=new byte[b2.length+2];
       int i;
      for( i=0;i<b2.length;i++){
           b3[i]=b2[i];
      }
      b3[i]=b1[0];
      b3[i+1]=b1[1];

      //if THE MESSAGE IS A FOLLOW MESSAGE******
      if(Opcode==4&&Integer.parseInt(strArray[0])==10)
      {
          System.out.println("i for ack 4");
          System.out.println("the message is "+str);
          int global=0;
         strArray[2]=strArray[2]+";";
          byte[] bf=strArray[2].getBytes(StandardCharsets.UTF_8);
          byte[] b4=new byte[b3.length+bf.length];
          System.out.println("b4.length"+b4.length);
          System.out.println("b3.length"+b3.length);
          System.out.println("bf.length"+bf.length);
          for(int j=0;j<b4.length;j=j+1){
              if(j<b3.length){
                  b4[j]=b3[j];
                  System.out.println("b3"+(j-b3.length));
                  System.out.println(j);

              }
              else if(j<bf.length+b3.length){
                  b4[j]=bf[j-b3.length];
                  System.out.println("bf"+(j-b3.length));
                  System.out.println(j);
              }

          }
          return b4;
      }
      else if((short) Integer.parseInt(strArray[1])==22){
          String content="";
          for(int j=2;j<strArray.length;j=j+1)
          {
              content=content+strArray[j]+'\0';
          }
        content+=";";
          byte[] b22=content.getBytes(StandardCharsets.UTF_8);
          byte[]b23=new byte[b22.length+4];
          b23[0]=b2[0];
          b23[1]=b2[1];
          b23[2]=b1[0];
          b23[3]=b1[1];
          for(int j=4;j<b23.length;j=j+1){
              b23[j]=b22[j-4];
          }
          return b23;
      }
     //************THIS IS A NOTIFICATION MESSAGE
       else if((short) Integer.parseInt(strArray[0])==9)
       {
           String content="";
           for(int j=2;j<strArray.length;j=j+1){
               content=content+strArray[j]+'\0';
           }
           content=content+";";
           byte[] st=content.getBytes(StandardCharsets.UTF_8);
           byte[] b5=new byte[st.length+2+1];
           b5[0]=b3[0];
           b5[1]=b3[1];
           System.out.println(strArray[1]);
           if(strArray[1].equals("0")){
           b5[2]=0;
               System.out.println("strArray[1]");

           }
           else b5[2]=1;
           for(int j=3;j<b5.length;j=j+1)
               b5[j]=st[j-3];

           return b5;
       }
       //LogSTAT message
       else if(Opcode==7&&Integer.parseInt(strArray[0])==10)
       {
            byte[] b7=new byte[12];
            int d=0;
           for(int j=0;j<strArray.length;j+=1)
           {
               short s=(short)Integer.parseInt(strArray[j]);
               byte[] m=shortToByte(s);
               b7[d]=m[0];
               d++;
               b7[d]=m[1];
               d++;
           }


           return b7;
        }



       return b3;


    }

    private String popString() {
        String result = new String(bytes, 0, len, StandardCharsets.UTF_8);
        len = 0;
      String resul2= opcode+" "+ result;
      System.out.println(resul2);
      return resul2;
    }

    private short bytesToShort(byte[] byteARR) {
        short result = (short) ((byteARR[0] & 0xff) << 8);
        result += (short) (byteARR[1] & 0xff);
        return result;
    }

    private void pushByte(byte nextByte) {
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }
        bytes[len++] = nextByte;
    }

    private void reset() {
        //zeroBytesSoFarAfterTheFirstTwoBytes = 0;
        opcode = -1;
        firstTwoBytes = 0;
    }

    private byte[] shortToByte(short num) {
        byte[] byteArr = new byte[2];
        byteArr[0] = (byte) ((num >> 8) & 0xFF);
        byteArr[1] = (byte) (num & 0xFF);
        return byteArr;
    }

}
