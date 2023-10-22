package com.pluton.yelody.utilities;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


public class ImageUtil {
	
	public static String saveFile(String path, String fileName, MultipartFile multipartFile)
            throws IOException {          
		Path uploadPath = Paths.get(path);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        String extension = StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());

        Path filePath = uploadPath.resolve(fileName + "." + extension);
        try {
            Files.write(filePath, multipartFile.getBytes());
            System.out.println("\n\n"+filePath);
        } catch (IOException e) {
            throw new IOException("Could not save file: " + fileName, e);
        }
        return filePath.toString();
    }
	
	public static byte[] compressImage(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[4*1024];
        while (!deflater.finished()) {
            int size = deflater.deflate(tmp);
            outputStream.write(tmp, 0, size);
        }
        try {
            outputStream.close();
        } catch (Exception ignored) {
        }
        return outputStream.toByteArray();
    }



    public static byte[] decompressImage(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[4*1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(tmp);
                outputStream.write(tmp, 0, count);
            }
            outputStream.close();
        } catch (Exception ignored) {
        }
        return outputStream.toByteArray();
    }
}