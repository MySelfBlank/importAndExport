package services.export;

import enums.ConstantDict;
import onegis.common.utils.FileUtils;
import onegis.exception.BaseException;
import org.ini4j.Wini;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 保存元数据
 */
public class ExportMetadata {

    private Wini ini;

    public ExportMetadata(String outputDir, String fileName) {

        FileUtils.isExistDir(outputDir);
        try {
            valideFile(new File(outputDir + File.separator + fileName));
            this.ini = new Wini(new File(outputDir + File.separator + fileName));
        } catch (IOException | BaseException e) {
            e.printStackTrace();
        }
    }

    public void put(String key, String value) {
        this.ini.put("Multi-granularity Spatio-temporal Object Data File", key, value);
    }


    public static void writeMetaData(String path) {

        ExportMetadata metadata = new ExportMetadata(path, ConstantDict.META_DATA_FILE_NAME.getName());
        metadata.put("version", "1.0");
        metadata.put("fileFormat", "JSON");
        metadata.put("characterset", "utf-8");
        metadata.put("stime", "1546272000000");
        metadata.put("etime", "1548663977000");
        metadata.put("geoBox", "[114.287379,9.681363,0.0][114.291466,9.71332,0.0]");
        metadata.put("authority", "OneGIS");
        metadata.put("generatetime", String.valueOf(System.currentTimeMillis()));
        try {
            metadata.write();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void write() throws Exception {
        File file = ini.getFile();
        if (this.ini == null || this.ini.isEmpty()) {
            throw new BaseException("配置项为空");
        }
        FileOutputStream outputStream = new FileOutputStream(file, false);
        try {
            ini.getConfig().setStrictOperator(true);
            ini.store(outputStream);
        } finally {
            outputStream.close();
        }
    }

    private void valideFile(File file) throws BaseException, IOException {

        if (file.isDirectory()) {
            throw new BaseException("这不是一个文件，而是一个文件夹");
        }
        if (!file.exists()) {
            boolean newFile = file.createNewFile();
            if (newFile) {
            } else {
                throw new BaseException("文件创建失败");
            }
        } else {
        }
    }

}
