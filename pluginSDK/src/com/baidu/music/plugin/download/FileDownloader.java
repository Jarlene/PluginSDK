package com.baidu.music.plugin.download;

import android.content.Context;
import android.text.TextUtils;
import com.baidu.music.plugin.clientlog.LogUtil;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 下载文件组合信息
 * <p/>
 * Created by Jarlene on 5/21 0021.
 */
public class FileDownloader {
    private static final String TAG      = "FileDownloader";
    private static final long   PER_SIZE = 1024 * 1024L;
    private Context     mContext;
    private FileService fileService;
    private boolean     exited;
    private long downloadedSize = 0L;
    private long fileSize       = 0L;
    private DownloadThread[] threads;
    private File             saveFile;
    private Map<Integer, Integer> data = new ConcurrentHashMap<Integer, Integer>();
    private long   block;
    private String downloadUrl;

    public int getThreadSize() {
        return threads.length;
    }

    public void exit() {
        this.exited = true;
    }

    public boolean getExited() {
        return this.exited;
    }

    public long getFileSize() {
        return fileSize;
    }

    protected synchronized void append(int size) {
        downloadedSize += size;
    }

    protected synchronized void update(int threadId, int pos) {
        this.data.put(threadId, pos);
        this.fileService.update(this.downloadUrl, threadId, pos);

    }

    public FileDownloader(Context context, String downloadUrl, File fileSaveDir) {
        try {
            this.mContext = context;
            this.downloadUrl = downloadUrl;
            fileService = new FileService(this.mContext);
            URL url = new URL(this.downloadUrl);
            if (!fileSaveDir.exists()) {
                fileSaveDir.mkdirs();
            }

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5 * 1000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept",
                    "image/gif, image/jpeg, image/pjpeg, image/pjpeg, "
                            + "application/x-shockwave-flash, application/xaml+xml, "
                            + "application/vnd.ms-xpsdocument, application/x-ms-xbap, "
                            + "application/x-ms-application, application/vnd.ms-excel, "
                            + "application/vnd.ms-powerpoint, application/msword, */*");
            conn.setRequestProperty("Accept-Language", "zh-CN");
            conn.setRequestProperty("Referer", downloadUrl);
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("User-Agent",
                    "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1"
                            + ".4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0"
                            + ".4506.2152; .NET CLR 3.5.30729)");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.connect();
            printResponseHeader(conn);

            if (conn.getResponseCode() == 200) {
                this.fileSize = conn.getContentLength();
                if (this.fileSize <= 0) {
                    throw new RuntimeException("Unknown file size ");
                }
                int threadNum = (int) (fileSize / PER_SIZE);
                this.threads = new DownloadThread[threadNum];

                String filename = getFileName(conn);
                this.saveFile = new File(fileSaveDir, filename);
                Map<Integer, Integer> logdata = fileService.getData(downloadUrl);

                if (logdata.size() > 0) {
                    for (Map.Entry<Integer, Integer> entry : logdata.entrySet()) {
                        data.put(entry.getKey(), entry.getValue());
                    }
                }

                if (this.data.size() == this.threads.length) {
                    for (int i = 0; i < this.threads.length; i++) {
                        this.downloadedSize += this.data.get(i + 1);
                    }
                    print("download size is " + this.downloadedSize + " byte");
                }

                this.block =
                        (this.fileSize % this.threads.length) == 0 ? this.fileSize / this.threads
                                .length : this.fileSize / this.threads.length + 1;
            } else {
                print("service error: " + conn.getResponseCode() + conn.getResponseMessage());
                throw new RuntimeException("server response error ");
            }
        } catch (Exception e) {
            print(e.toString());
            throw new RuntimeException("Can't connection this url");
        }
    }

    private String getFileName(HttpURLConnection conn) {
        String filename = this.downloadUrl.substring(this.downloadUrl.lastIndexOf('/') + 1);

        if (TextUtils.isEmpty(filename)) {
            for (int i = 0; ; i++) {
                String mine = conn.getHeaderField(i);
                if (mine == null) {
                    break;
                }
                if ("content-disposition".equals(conn.getHeaderFieldKey(i).toLowerCase())) {
                    Matcher m = Pattern.compile(".*filename=(.*)").matcher(
                            mine.toLowerCase(Locale.getDefault()));
                    if (m.find()) {
                        return m.group(1);
                    }
                }
            }
            filename = UUID.randomUUID() + ".tmp";
        }
        return filename;
    }

    public long download(DownloadProgressListener listener) throws Exception {
        try {
            RandomAccessFile randOut = new RandomAccessFile(this.saveFile, "rwd");
            if (this.fileSize > 0) {
                randOut.setLength(this.fileSize);
            }
            randOut.close();
            URL url = new URL(this.downloadUrl);
            int thLength = threads.length;
            if (this.data.size() != thLength) {
                this.data.clear();
                for (int i = 0; i < thLength; i++) {
                    this.data.put(i + 1, 0);
                }
                this.downloadedSize = 0;
            }
            for (int i = 0; i < thLength; i++) {
                int downloadedLength = this.data.get(i + 1);
                if (downloadedLength < this.block && this.downloadedSize < this.fileSize) {
                    this.threads[i] = new DownloadThread(this, url, this.saveFile, this.block,
                            this.data.get(i + 1), i + 1);
                    this.threads[i].setPriority(7);
                    this.threads[i].start();
                } else {
                    this.threads[i] = null;
                }
            }
            this.fileService.delete(this.downloadUrl);
            this.fileService.save(this.downloadUrl, this.data);
            boolean notFinished = true;
            while (notFinished) {
                notFinished = false;
                for (int i = 0; i < thLength; i++) {
                    if (this.threads[i] != null && !this.threads[i].isFinished()) {
                        notFinished = true;
                        if (this.threads[i].getDownloadedLength() == -1) {
                            this.threads[i] =
                                    new DownloadThread(this, url, this.saveFile, this.block,
                                            this.data.get(i + 1), i + 1);
                            this.threads[i].start();
                        }
                    }
                }
                if (listener != null) {
                    listener.onDownloadSize(this.downloadedSize,
                            (int) (downloadedSize * 1.0f / fileSize * 100));
                }
            }
            if (downloadedSize == this.fileSize) {
                this.fileService.delete(this.downloadUrl);
            }
        } catch (Exception e) {
            print(e.toString());
            throw new Exception("File downloads error");
        }
        return this.downloadedSize;
    }

    public static Map<String, String> getHttpResponseHeader(HttpURLConnection http) {
        Map<String, String> header = new LinkedHashMap<String, String>();
        for (int i = 0; ; i++) {
            String mine = http.getHeaderField(i);
            if (mine == null) {
                break;
            }
            header.put(http.getHeaderFieldKey(i), mine);
        }
        return header;
    }

    public static void printResponseHeader(HttpURLConnection http) {
        Map<String, String> header = getHttpResponseHeader(http);
        for (Map.Entry<String, String> entry : header.entrySet()) {
            String key = entry.getKey() != null ? entry.getKey() + ":" : "";
            print(key + entry.getValue());
        }
    }

    private static void print(String msg) {
        LogUtil.i(TAG, msg);
    }
}
