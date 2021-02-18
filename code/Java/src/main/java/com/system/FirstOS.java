package com.system;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FirstOS {
    // 先定义着，后面会覆盖
    private int[] imgContent = new int[]{0,0,0,0,0, 0,0,0,0,0};
    private List<Integer> imgByteToWriter = new ArrayList<>();

    private void readKernelFromFile(String fileName) {
        File f = new File(fileName);
        InputStream input = null;

        try {
            input = new FileInputStream(f);
            int bit;
            while ((bit = input.read()) != -1) {
                imgByteToWriter.add(bit);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ;
        }

        imgByteToWriter.add(0x55);
        imgByteToWriter.add(0xaa);
    }

    public FirstOS(String s){
        readKernelFromFile("D:\\MyOS\\code\\ASM\\boot.bat");
        //https://blog.csdn.net/sxt102400/article/details/9073359
        int len  = 0x168000;
        int curSize = imgByteToWriter.size();
        for (int l = 0;l<len-curSize;l++){
            imgByteToWriter.add(0);
        }
    }

    public void makeFllopy() {
        try {
            DataOutputStream output = new DataOutputStream(new FileOutputStream("system.img"));
            for (int i = 0;i<imgByteToWriter.size();i++){
                output.writeByte(imgByteToWriter.get(i).byteValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ;
        }
    }
    public static void main(String[] args) {
        FirstOS os = new FirstOS("test");
        os.makeFllopy();
    }
}
