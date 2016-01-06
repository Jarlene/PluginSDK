package com.baidu.music.plugin.utils;

import android.content.Context;
import android.text.TextUtils;

import java.io.*;


/**
 * Created by Jarlene on 5/21 0021
 */
public class FileUtils {

    /**
     * 流拷贝
     *
     * @param is
     * @param os
     * @throws java.io.IOException
     */
    public static void copyStream(InputStream is, OutputStream os) throws IOException {
        byte buffer[] = new byte[1024];
        int len = -1;
        while ((len = is.read(buffer, 0, 1024)) != -1) {
            os.write(buffer, 0, len);
        }
    }

    /**
     * 文件copy
     *
     * @param src
     * @param dest
     * @return
     * @throws java.io.IOException
     */
    public static File copyFile(String src, String dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            File destFile = new File(dest);
            if (!destFile.getParentFile().exists()) {
                destFile.getParentFile().mkdirs();
            }
            if (destFile.exists()) {
                destFile.delete();
            }
            is = new FileInputStream(src);
            os = new FileOutputStream(destFile);
            FileUtils.copyStream(is, os);
            return destFile;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 拷贝assets目录文件到目标文件
     *
     * @param context
     * @param assetsPath
     * @param dest
     */
    public static File copyAssets(Context context, String assetsPath, String dest)
            throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            File destFile = new File(dest);
            if (!destFile.getParentFile().exists()) {
                destFile.getParentFile().mkdirs();
            }

            if (destFile.exists()) {
                destFile.delete();
            }

            is = context.getAssets().open(assetsPath);
            os = new FileOutputStream(destFile);
            FileUtils.copyStream(is, os);
            return destFile;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获得路径的文件名
     *
     * @param path
     * @return
     */
    public static String getFileName(String path) {
        File file = new File(path);
        String fileName = file.getName();
        return fileName.substring(0, fileName.indexOf("."));
    }

    public static void deletelDir(String dir) throws IOException {
        if (TextUtils.isEmpty(dir)) {
            return;
        }
        File f = new File(dir);// 定义文件路径
        if (f.exists() && f.isDirectory()) {// 判断是文件还是目录
            File[] delFile = f.listFiles();
            if (delFile == null || delFile.length == 0) {// 若目录下没有文件则直接删除
                f.delete();
            } else {// 若有则把文件放进数组，并判断是否有下级目录
                int i = delFile.length;
                for (int j = 0; j < i; j++) {
                    if (delFile[j].isDirectory()) {
                        deletelDir(delFile[j].getAbsolutePath()); // 递归调用del方法并取得子目录路径
                    }
                    delFile[j].delete(); // 删除文件
                }
                f.delete();
            }
        }
    }


}
