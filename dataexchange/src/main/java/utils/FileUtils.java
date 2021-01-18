package utils;

import java.io.*;
import java.util.ArrayList;

/**
 * 读取文件工具
 */
public class FileUtils {

    /**
     * 获取文件夹下所有文件
     * @param path
     * @return
     * @throws Exception
     */
    public static ArrayList<File> getFiles(String path) {
        //目标集合fileList
        ArrayList<File> fileList = new ArrayList<>();
        File file = new File(path);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File fileIndex : files) {
                //如果这个文件是目录，则进行递归搜索
                if (fileIndex.isDirectory()) {
                    getFiles(fileIndex.getPath());
                } else {
                    //如果文件是普通文件，则将文件句柄放入集合中
                    fileList.add(fileIndex);
                }
            }
        }
        return fileList;
    }

    /**
     * 读取文件
     *
     * @param fileName
     * @return
     */
    public static String readFile(String fileName) {
        StringBuffer result = new StringBuffer();
        try (FileReader reader = new FileReader(fileName);
             BufferedReader br = new BufferedReader(reader)
        ) {
            String line;
            while ((line = br.readLine()) != null) {
                // 一次读入一行数据
                result.append(line);
                result.append("\r\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }


    /**
     * 读取文件字节流
     * @param filePath
     * @return
     * @throws IOException
     */
    public static byte[] InputStream2ByteArray(String filePath) throws IOException {

        InputStream in = new FileInputStream(filePath);
        byte[] data = toByteArray(in);
        in.close();

        return data;
    }
    private static byte[] toByteArray(InputStream in) throws IOException {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
       // byte[] buffer = new byte[1024 * 4];
        byte[] buffer = new byte[in.available()];
        int n = 0;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        return out.toByteArray();
    }
}
