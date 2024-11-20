package se.ki.education.nkcx.util;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

/* Created by: Binay Singh */

public final class FileUtil {

    private static final String LOCAL_FILE_DIRECTORY = System.getProperty("catalina.home") + File.separator + "saved_file";

    public static String saveFile(String encodedString, String directory, String fileName) throws IOException {
        String savedAbsolutePath = saveFileLocally(encodedString, ConfigUtil.INSTANCE.getFileSavingDirectory().substring(8) + directory, fileName).getAbsolutePath();
        return savedAbsolutePath.substring(savedAbsolutePath.indexOf("saved_file") - 1);
    }

    private static File saveFileLocally(String encodedString, String directory, String fileName) throws IOException {
        String fileType = encodedString.split(";")[0].split("/")[1];
        Path path = Paths.get(directory);
        if (Files.notExists(path)) {
            Files.createDirectories(path);
        }
        File file = new File(directory, fileName + "." + fileType);
        //decode the string and save the image
        decodeToImage(encodedString, file);
        return file;
    }

    private static boolean deleteLocalFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }

    public static void deleteFile(String filePath) {
        deleteLocalFile(filePath);
    }

    private static void decodeToImage(String encodedString, File file) throws IOException {
        int start = encodedString.indexOf(",");
        encodedString = encodedString.substring(start + 1);//remove data:image/png;base64
        byte[] btDataFile = Base64.getDecoder().decode(encodedString);
        try (FileOutputStream osf = new FileOutputStream(file.getAbsolutePath())) {
            osf.write(btDataFile);
        }
    }

    public static String getAbsolutePathFromFileUrl(String fileUrl) {
        //Remove "file://"
        return ConfigUtil.INSTANCE.getFileSavingDirectory().substring(8) + fileUrl.substring(fileUrl.indexOf("saved_file") + 10);
    }

    public static String getMultipartFileExtension(MultipartFile multipartFile) {
        if (multipartFile != null && !multipartFile.isEmpty() && multipartFile.getOriginalFilename() != null) {
            return multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".") + 1);
        }
        return null;
    }

    public static String saveMultipartFile(MultipartFile multipartFile, String fileName, String directory) throws IOException {
        Path path = Paths.get(LOCAL_FILE_DIRECTORY + File.separator + directory);
        if (Files.notExists(path)) {
            Files.createDirectories(path);
        }
        File file = new File(LOCAL_FILE_DIRECTORY + File.separator + directory, fileName + "." + getMultipartFileExtension(multipartFile));
        byte[] btDataFile = multipartFile.getBytes();
        try (FileOutputStream osf = new FileOutputStream(file.getAbsolutePath())) {
            osf.write(btDataFile);
        }

        return file.getAbsolutePath().substring(file.getAbsolutePath().indexOf("saved_file") - 1);
    }

    public static String getLocalFileDirectory() {
        return LOCAL_FILE_DIRECTORY;
    }

    public static String saveExcelFileLocally(Workbook workbook, String directory, String fileName, String extension) throws IOException {
        directory = ConfigUtil.INSTANCE.getFileSavingDirectory().substring(8) + directory;
        Path path = Paths.get(directory);
        if (Files.notExists(path)) {
            Files.createDirectories(path);
        }
        File file = new File(directory, fileName + "." + extension);

        if (!file.exists()) {
            FileOutputStream outputStream = new FileOutputStream(file.getAbsolutePath());
            workbook.write(outputStream);
            workbook.close();
        }

        return file.getAbsolutePath().substring(file.getAbsolutePath().indexOf("saved_file") - 1);
    }
}
