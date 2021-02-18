package com.OsFloppy;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class Floppy512 {
    private Floppy floppyDisk = new Floppy();
    private int MAX_SECTOR_NUM = 18;

    private void writeFileToFloppy(String fileName, boolean bootable, int cylinder, int beginSec) {
        File file = new File(fileName);
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            byte[] buf = new byte[512];
            if (bootable) {
                buf[510] = 0x55;
                buf[511] = (byte)0xaa;
            }

            while (in.read(buf) != -1 ) {
                // 将内核读入到磁盘 盘面0 柱面0 扇区1
                floppyDisk.writeFloppy(Floppy.MAGNETIC_HEAD.MAGNETIC_HEAD_0, cylinder, beginSec, buf);
                beginSec++;
                if (beginSec > MAX_SECTOR_NUM) {
                    beginSec = 1;
                    cylinder++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ;
        }
    }

    public Floppy512(String s) {
        writeFileToFloppy(s, true, 0, 1);
    }

    public void makeFloppy() {
        writeFileToFloppy("D:\\MyOS\\code\\ASM\\5\\kernel.bat", false, 1, 2);

        floppyDisk.makeFloppy("system.img");
    }

    public static void main(String[] args) {
        Floppy512 ft = new Floppy512("D:\\MyOS\\code\\ASM\\5\\boot.bat");
        ft.makeFloppy();
    }
}
