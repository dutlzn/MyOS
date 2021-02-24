package com.OsFloppy;

import java.io.FileInputStream;
import java.io.InputStream;

public class FloppyTest {
    private Floppy floppyDisk = new Floppy();

    
    public FloppyTest(String s){
        writeToFloppy(s);
    }

    private void writeToFloppy(String file) {
        InputStream in;
        try {
            in = new FileInputStream(file);
            byte[] buf = new byte[512];
            buf[510] = 0x55;
            buf[511] = (byte) 0xaa;
            if (in.read(buf) != -1) {
                // 将内核读入 盘面0 柱面0  第一个扇区
                floppyDisk.writeFloppy(Floppy.MAGNETIC_HEAD.MAGNETIC_HEAD_0,
                        0,
                        1,
                        buf);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ;
        }
    }

    public void makeFloppy() {
        String s = "This is a text from cylinder 1 and sector 2";
        floppyDisk.writeFloppy(Floppy.MAGNETIC_HEAD.MAGNETIC_HEAD_0, 1, 2, s.getBytes());

        floppyDisk.makeFloppy("system.img");
    }

    public static void main(String[] args) {
        FloppyTest ft = new FloppyTest("D:\\MyOS\\code\\ASM\\1\\boot2.bat");
        ft.makeFloppy();
    }
}
