package com.baidu.music.plugin.utils;


import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by Jarlene on 5/21 0021
 */
public class ZipUtils {

    private static void decompressZipFile(ZipInputStream zipInputStream, ZipEntry entry,
            String destPath) throws IOException {
        String entryName = entry.getName();
        String fileName = entryName.substring(entryName.lastIndexOf("/") + 1);

        File outFile = new File(destPath, fileName);
        if (!outFile.getParentFile().exists()) {
            outFile.getParentFile().mkdirs();
        }
        if (outFile.exists()) {
            outFile.delete();
        }
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outFile));
        int count = 0;
        byte buffer[] = new byte[1024];
        while ((count = zipInputStream.read(buffer)) > 0) {
            bos.write(buffer, 0, count);
        }
        bos.flush();
        bos.close();
    }

    /**
     * 将Apk中so文件拷贝到指定位置。
     *
     * @param apkPath
     * @param libPath
     */
    public static void extractLibraries(String apkPath, String libPath) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File(apkPath));
            ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
            ZipEntry entry = zis.getNextEntry();
            while (entry != null) {
                if (entry.getName().endsWith(".so")) {
                    decompressZipFile(zis, entry, libPath);
                }
                entry = zis.getNextEntry();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
