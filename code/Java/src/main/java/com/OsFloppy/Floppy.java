package com.OsFloppy;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 模拟 软盘
 */
public class Floppy {
    /**
     * 两个磁头
     */
    enum MAGNETIC_HEAD {
        MAGNETIC_HEAD_0,
        MAGNETIC_HEAD_1
    };

    public int SECTOR_SIZE = 512; // 扇区大小 512Byte
    private int CYLINDER_COUNT = 80;// 磁道、柱面
    private int SECTOR_COUNT = 18; // 每个磁道18个扇区
    private MAGNETIC_HEAD magneticHead = MAGNETIC_HEAD.MAGNETIC_HEAD_0;
    private int current_cylinder = 0;
    private int current_sector = 0;

    private Map<Integer, ArrayList<ArrayList<byte[]>>> floppy = new HashMap<>();
    // 磁道--->扇区

    public Floppy() {
        initFloppy();
    }

    private void initFloppy() {
        //一个磁盘有两个盘面
        floppy.put(MAGNETIC_HEAD.MAGNETIC_HEAD_0.ordinal(), initFloppyDisk());
        floppy.put(MAGNETIC_HEAD.MAGNETIC_HEAD_1.ordinal(), initFloppyDisk());
    }

    private ArrayList<ArrayList<byte[]>> initFloppyDisk() {
        ArrayList<ArrayList<byte[]>> floppyDisk = new ArrayList<>();
        // 一个盘面有80个磁道/柱面
        for (int i = 0;i<CYLINDER_COUNT;++i){
            floppyDisk.add(initCylinder());
        }
        return floppyDisk;
    }

    private ArrayList<byte[]> initCylinder() {
        // 一个 柱面 有18个扇区
        ArrayList<byte[]> cylinder = new ArrayList<>();
        for (int i = 0;i<SECTOR_COUNT;++i) {
            byte[] sector = new byte[SECTOR_SIZE];
            cylinder.add(sector);
        }
        return cylinder;
    }

    /**
     * 确定磁头
     * @param head
     */
    public void setMagneticHead(MAGNETIC_HEAD head) {
        magneticHead = head;
    }

    /**
     * 确定磁道
     */
    public void setCylinder(int cylinder) {
        if ( cylinder < 0 ) {
            this.current_cylinder = 0;
        } else if ( cylinder >= 80 ) {
            this.current_cylinder = 79;
        } else {
            this.current_cylinder = cylinder;
        }
    }

    /**
     * 确定扇区
     */
    public void setSector(int sector) {
        // sector 编号从1到18
        if (sector < 1) {
            this.current_sector = 0;
        } else if (sector > 18) {
            this.current_sector = 18 - 1;
        } else {
            this.current_sector = sector - 1;
        }
    }

    /**
     * 读扇区
     * @param head
     * @param cylinder_num
     * @param sector_num
     * @return
     */
    public byte[] readFloppy(MAGNETIC_HEAD head, int cylinder_num, int sector_num) {
        setMagneticHead(head);
        setCylinder(cylinder_num);
        setSector(sector_num);

        ArrayList<ArrayList<byte[]>> disk = floppy.get(this.magneticHead.ordinal());
        ArrayList<byte[]> cylinder = disk.get(this.current_cylinder);
        byte[] sector = cylinder.get(this.current_sector);

        return sector;
    }

    /**
     * 写入扇区
     * @param head
     * @param cylinder_num
     * @param sector_num
     * @param buf
     */
    public void writeFloppy(MAGNETIC_HEAD head, int cylinder_num, int sector_num, byte[] buf) {
        setMagneticHead(head);
        setCylinder(cylinder_num);
        setSector(sector_num);

        ArrayList<ArrayList<byte[]>> disk = floppy.get(this.magneticHead.ordinal());
        ArrayList<byte[]> cylinder = disk.get(this.current_cylinder);
        byte[] cur_buf = cylinder.get(this.current_sector);

        System.arraycopy(buf, 0, cur_buf, 0, buf.length);
    }

    /**
     * 制作软盘
     * @param fileName
     */
    public void makeFloppy(String fileName) {
        /*
        512*18
        盘面0  柱面0
        盘面1  柱面0
        盘面0  柱面1
        盘面1  柱面1
         */
        try {
            DataOutputStream out = new DataOutputStream(new FileOutputStream(fileName));
            for (int cylinder = 0; cylinder < CYLINDER_COUNT; cylinder++) {
                for (int head = 0; head <= MAGNETIC_HEAD.MAGNETIC_HEAD_1.ordinal(); head++) {
                    for (int sector = 1; sector <= SECTOR_COUNT; sector++) {
                        byte[] buf = readFloppy(MAGNETIC_HEAD.values()[head], cylinder, sector);
                        out.write(buf);
                    }
                }
            }
//            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println(2*18*80*512.0/1000.0/1024.0 + " MB");
    }

}
