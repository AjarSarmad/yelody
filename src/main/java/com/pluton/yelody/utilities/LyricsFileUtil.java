package com.pluton.yelody.utilities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

public class LyricsFileUtil {
	public static String saveTxtFile(String path, String fileName, MultipartFile multipartFile)
            throws IOException {          
		Path uploadPath = Paths.get(path);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        String extension = StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());
        if(!extension.equalsIgnoreCase("txt"))
            throw new IOException("Unsupported Extension: " + extension);

        Path filePath = uploadPath.resolve(fileName + "." + extension);
        try {
            Files.write(filePath, multipartFile.getBytes());
            System.out.println("\n\n"+filePath);
        } catch (IOException e) {
            throw new IOException("Could not save file: " + fileName, e);
        }
        return filePath.toString();
    }
	
	public static String saveXmlFile(String path, String fileName, MultipartFile multipartFile)
            throws IOException {          
		Path uploadPath = Paths.get(path);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        String extension = StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());
        if(!extension.equalsIgnoreCase("xml"))
            throw new IOException("Unsupported Extension: " + extension);

        Path filePath = uploadPath.resolve(fileName + "." + extension);
        try {
            Files.write(filePath, multipartFile.getBytes());
            System.out.println("\n\n"+filePath);
        } catch (IOException e) {
            throw new IOException("Could not save file: " + fileName, e);
        }
        return filePath.toString();
    }
}
