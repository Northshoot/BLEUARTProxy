package com.redbear.chat;

import android.util.Log;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * Created by lauril on 9/3/14.
 */
final public class UARTServiceMessage {

    public static final short REQ_READ		= 0;
    public static final short REQ_SUBS		= 1;
    public static final short REQ_WRITE		= 2;

    public static final short RSP_OK		= 20;
    public static final short RSP_SRV_NA	= 21;
    public static final short RSP_SRV_AL	= 32;
    public static final short RSP_READ		= 33;
    public static final short RSP_WRT		= 34;
    public static final short RSP_SUBS	    = 35;

    private static final long serialVersionUID = 8887772353469343890L;
    private static final String TAG = "UARTServiceMessage";
    private int HEADER_UUID_FROM_UINT16T;
    private byte HEADER_TYPE_FROM_UINT8T;
    private byte PACKET_DATA_LEN_FROM_UINT8T;
    private byte[] DATA;
    private ByteBuffer INTERNAL_PACKET;

    public UARTServiceMessage(byte[] netPacket){
        Log.d(TAG, "Length " + String.valueOf(netPacket.length)
                + " string value "
                + new String(netPacket));
        INTERNAL_PACKET = ByteBuffer.wrap(netPacket);
        INTERNAL_PACKET.order(ByteOrder.LITTLE_ENDIAN);
        try {
            while (INTERNAL_PACKET.hasRemaining()) {
                byte[] b = new byte[2];
                INTERNAL_PACKET.get(b ,0,2);
                HEADER_UUID_FROM_UINT16T = unsignedIntFromByteArray(b);
                HEADER_TYPE_FROM_UINT8T = INTERNAL_PACKET.get();
                PACKET_DATA_LEN_FROM_UINT8T = INTERNAL_PACKET.get();
                //TODO: buffer need to be fixed here
                //but since we forwarding, we dont care
                DATA = new byte[PACKET_DATA_LEN_FROM_UINT8T];
                break;
            }
        } catch ( BufferOverflowException ex ) {
            ex.printStackTrace();
        } catch ( BufferUnderflowException ex ) {
            ex.printStackTrace();
        } catch ( Exception ex ){
            ex.printStackTrace();
        }
        Log.d(TAG, "HEADER UUID " + String.valueOf(HEADER_UUID_FROM_UINT16T));
        Log.d(TAG, "HEADER TYPE " + String.valueOf(HEADER_TYPE_FROM_UINT8T));
        Log.d(TAG, "DATA LENGTH " + String.valueOf(PACKET_DATA_LEN_FROM_UINT8T));

    }

    public byte[] getPacketByteArray(){
        return INTERNAL_PACKET.array();
    }

    public ByteBuffer getPaketByteBuffer(){
        return INTERNAL_PACKET;
    }

    public int getHEADER_UUID_FROM_UINT16T() {
        return HEADER_UUID_FROM_UINT16T;
    }

    public void setHEADER_UUID_FROM_UINT16T(int HEADER_UUID_FROM_UINT16T) {
        this.HEADER_UUID_FROM_UINT16T = HEADER_UUID_FROM_UINT16T;
    }

    public byte getHEADER_TYPE_FROM_UINT8T() {
        return HEADER_TYPE_FROM_UINT8T;
    }

    public void setHEADER_TYPE_FROM_UINT8T(byte HEADER_TYPE_FROM_UINT8T) {
        this.HEADER_TYPE_FROM_UINT8T = HEADER_TYPE_FROM_UINT8T;
    }

    public byte getPACKET_DATA_LEN_FROM_UINT8T() {
        return PACKET_DATA_LEN_FROM_UINT8T;
    }

    public void setPACKET_DATA_LEN_FROM_UINT8T(byte PACKET_DATA_LEN_FROM_UINT8T) {
        this.PACKET_DATA_LEN_FROM_UINT8T = PACKET_DATA_LEN_FROM_UINT8T;
    }

    public byte[] getDATA() {
        return DATA;
    }

    public void setDATA(byte[] DATA) {
        this.DATA = DATA;
    }


    /***
     * returns byte part of a byte array
     * @param bytes byte array
     * @param start starting index of byte array
     * @param end end index of byte array
     * @return byte[stop-end]
     */
    private byte[] getBytes(byte[] bytes, int start, int end){
        byte[] b = new byte[end-start+1];
        for(int i=0; start <= end;i++){
            b[i]=bytes[start++];
        }
        return b;
    }

    private int unsignedIntFromByteArray(byte[] bytes) {
        int res=0;
        for (int i=0;i<bytes.length;i++){
            res = res | ((bytes[i] & 0xff) << i*8);
        }
        return res;
    }


    private static int convertTwoBytesToInt (byte[] bytes)
    {
        if (bytes.length !=2) throw new AssertionError("Expected 2 bytes");
        return (bytes[1] << 8) | (bytes[0] & 0xFF);
    }

    private static int convertFourBytesToInt (byte[] bytes)
    {
        if (bytes.length !=4 ) throw new AssertionError("Expected 4 bytes");
        return (bytes[3] << 24) | (bytes[2] & 0xFF) << 16 | (bytes[1] & 0xFF) << 8 | (bytes[0] & 0xFF);
    }

    /***
     * Convert two unsigned bytes to int Litle Endian!
     * @param bytes
     * @return converted int
     */
    public static int convertTwoUnsignedBytesToInt (byte[] bytes)
    {
        if (bytes.length != 2) throw new AssertionError("Expected 2 bytes");
        return (bytes[1] & 0xFF) << 8 | (bytes[0] & 0xFF);
    }

    /***
     * convert 4 unsigned bytes to long
     * @param bytes
     * @return converted long
     */
    public static long convertFourUnsignedBytesToInt (byte[] bytes)
    {
        if (bytes.length !=4) throw new AssertionError("Expected 4 bytes");
        return (long) (bytes[0] & 0xFF) << 24 | (bytes[1] & 0xFF) << 16 | (bytes[2] & 0xFF) << 8 | (bytes[3] & 0xFF);
    }



    @Override
    public String toString() {
        return "UARTServiceMessage{" +
                "HEADER_UUID_FROM_UINT16T=" + HEADER_UUID_FROM_UINT16T +
                ", HEADER_TYPE_FROM_UINT8T=" + HEADER_TYPE_FROM_UINT8T +
                ", PACKET_DATA_LEN_FROM_UINT8T=" + PACKET_DATA_LEN_FROM_UINT8T +
                ", DATA=" + Arrays.toString(DATA) +
                '}';
    }
}
