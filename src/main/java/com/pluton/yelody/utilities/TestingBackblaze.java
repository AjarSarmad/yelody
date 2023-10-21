//package com.example.b2client;
//import java.net.URI;
//import java.util.List;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import com.amazonaws.auth.profile.ProfileCredentialsProvider;
//import com.amazonaws.regions.Region;
//
//
//public class App
//{
//    // Change this to the endpoint from your bucket details, prefixed with "https://"
//    private static String ENDPOINT_URL = "https://<your endpoint>";
//
//   public static void main( String[] args )
//           throws Exception
//    {
//        // Extract the region from the endpoint URL
//        Matcher matcher = Pattern.compile("https://s3\\.([a-z0-9-]+)\\.backblazeb2\\.com").matcher(ENDPOINT_URL);
//        if (!matcher.find()) {
//            System.err.println("Can't find a region in the endpoint URL: " + ENDPOINT_URL);
//        }
//        String region = matcher.group(1);
//
//        // Create a client. The try-with-resources pattern ensures the client is cleaned up when we're done with it
//       try (S3Client b2 = S3Client.builder()
//               .region(Region.of(region))
//                .credentialsProvider(ProfileCredentialsProvider.create("b2tutorial"))
//                .endpointOverride(new URI(ENDPOINT_URL)).build()) {
//
//            // Get the list of buckets
//           List<Bucket> buckets = b2.listBuckets().buckets();
//
//           // Iterate through list, printing each bucket's name
//            System.out.println("Buckets in account:");
//            for (Bucket bucket : buckets) {
//                System.out.println(bucket.name());
//            }
//        }
//    }
//}