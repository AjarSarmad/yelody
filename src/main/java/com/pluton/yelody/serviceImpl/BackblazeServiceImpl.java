package com.pluton.yelody.serviceImpl;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.backblaze.b2.client.B2ClientConfig;
import com.backblaze.b2.client.B2StorageClient;
import com.backblaze.b2.client.B2StorageClientFactory;
import com.backblaze.b2.client.contentHandlers.B2ContentMemoryWriter;
import com.backblaze.b2.client.contentSources.B2FileContentSource;
import com.backblaze.b2.client.exceptions.B2Exception;
import com.backblaze.b2.client.structures.B2DeleteFileVersionRequest;
import com.backblaze.b2.client.structures.B2DownloadByNameRequest;
import com.backblaze.b2.client.structures.B2FileVersion;
import com.backblaze.b2.client.structures.B2UploadFileRequest;
import com.pluton.yelody.config.BackblazeConfigProperties;
import com.pluton.yelody.services.BackblazeService;

@Service
public class BackblazeServiceImpl implements BackblazeService {
	@Autowired
	BackblazeConfigProperties backblazeProperties;
    
	B2ClientConfig config = null;
	
	B2StorageClient b2StorageClient = null;
		
	public void initBackblaze() {
		try {
	    	config = B2ClientConfig.builder(backblazeProperties.getApplicationKeyId(), backblazeProperties.getApplicationKey(), "yelody").build();
	    	b2StorageClient = B2StorageClientFactory.createDefaultFactory().create(config);
		} catch (Exception e) {
            System.err.println("ERROR INITIALIZING BACKBLAZE SERVICE" + e.getMessage());
            e.printStackTrace();
        }
	}
	
	
	@Override
	public boolean uploadSong(boolean isUserRecordedSong, String songId, MultipartFile mp3File) {
		String bucketId;
		 try {
			 initBackblaze();
			 if(isUserRecordedSong)
				 bucketId = backblazeProperties.getUserBucketId();
			 else
				 bucketId = backblazeProperties.getKaraokeBucketId();

			 	
			 	songId += ".mp3";
			    File file = File.createTempFile(songId, ".mp3");
			    mp3File.transferTo(file);
		        
		        B2FileContentSource contentSource = B2FileContentSource.builder(file)
		                .build();
		      
		        B2UploadFileRequest request = B2UploadFileRequest.builder(bucketId, songId, "audio/mpeg", contentSource).setCustomField(songId, bucketId)
		            .build();

		        B2FileVersion  response = b2StorageClient.uploadSmallFile(request);
		        
		        if(response!=null)
		        	return true;

		    } catch (IOException e) {
		        System.err.println("Error uploading song: " + e.getMessage());
		    } catch (B2Exception e) {
		        System.err.println("Error uploading song: " + e.getMessage());
		    }
		return false;
	}


	@Override
	public String getSongById(boolean isUserRecordedSong, String songId) {
		B2DownloadByNameRequest request = null;
		 try {
			 	initBackblaze();
			 	String fileName = songId + ".mp3";
	            // Create a B2DownloadFileByNameRequest based on Bucket Category
			 	if(isUserRecordedSong)
				 	 request = B2DownloadByNameRequest.builder(backblazeProperties.getUserBucketName(),fileName ).build();
			 	else
			 		request = B2DownloadByNameRequest.builder(backblazeProperties.getKaraokeBucketName(),fileName ).build();
			 	
			 	B2ContentMemoryWriter fileContent = B2ContentMemoryWriter.builder().setVerifySha1ByRereadingFromDestination(false).build();

			 	// Download the file from Backblaze B2
			 	b2StorageClient.downloadByName(request, fileContent);
			 	
			 	byte[] songBytes = fileContent.getBytes();
			 	
	            // Encode the byte array to base64
	            String base64EncodedSong = Base64.getEncoder().encodeToString(songBytes);

	            //FOR DIRECT DOWNLOAD
//			 	ByteArrayResource songBytes = new ByteArrayResource(fileContent.getBytes());
//	            String contentType = "application/octet-stream";
//	            String headerValue = "attachment; filename=\"" + songBytes.getFilename() + "\"";
//	            return ResponseEntity.ok()
//	                    .contentType(MediaType.parseMediaType(contentType))
//	                    .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
//	                    .body(songBytes);	  
	            
	            return base64EncodedSong;
		 } catch ( B2Exception e) {
	            System.err.println("Error downloading song: " + e.getMessage());
	            return null;
        }	
	 }


	@Override
	public boolean deleteSongById(boolean isUserRecordedSong, String songId) {
		String bucketName;
		String fileName;
		try {
			initBackblaze();
			
			fileName = songId + ".mp3";
			if(isUserRecordedSong)
				bucketName = backblazeProperties.getUserBucketName();
			else
				bucketName = backblazeProperties.getKaraokeBucketName();

			B2FileVersion fileInfoRequest = b2StorageClient.getFileInfoByName(bucketName, fileName);
					
			B2DeleteFileVersionRequest deleteRequest = B2DeleteFileVersionRequest.builder(fileInfoRequest.getFileName(), fileInfoRequest.getFileId()).build();
			b2StorageClient.deleteFileVersion(deleteRequest);
			
			if(deleteRequest!=null)
				return true;
			else
				return false;
		} catch ( B2Exception e) {
            System.err.println("Error downloading song: " + e.getMessage());
            return false;
    }	
	}
	
   
}