package com.pluton.yelody.utilities;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.apache.commons.text.similarity.CosineSimilarity;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

public class ScoringAlgorithm {
	 public static int compareLyrics(String originalLyrics, String userLyrics) {
	        String processedOriginal = preprocessText(originalLyrics);
	        String processedUser = preprocessText(userLyrics);

	        int levenshteinScore = calculateLevenshteinScore(processedOriginal, processedUser);
	        double cosineScore = calculateCosineSimilarity(processedOriginal, processedUser);
	        double jaccardScore = calculateJaccardSimilarity(processedOriginal, processedUser);

	        double averageScore = (levenshteinScore + cosineScore + jaccardScore) / 3;
	        return (int) Math.round(averageScore);
	    }

	    private static String preprocessText(String text) {
	        return text.toLowerCase().replaceAll("[^a-zA-Z ]", "");
	    }

	    private static double calculateCosineSimilarity(String original, String user) {
	        Map<CharSequence, Integer> originalVector = toVector(original);
	        Map<CharSequence, Integer> userVector = toVector(user);
	        CosineSimilarity cosineSimilarity = new CosineSimilarity();
	        return cosineSimilarity.cosineSimilarity(originalVector, userVector) * 100;
	    }

	    private static double calculateJaccardSimilarity(String original, String user) {
	        Set<String> originalSet = new HashSet<>(Arrays.asList(original.split(" ")));
	        Set<String> userSet = new HashSet<>(Arrays.asList(user.split(" ")));
	        Set<String> intersection = new HashSet<>(originalSet);
	        intersection.retainAll(userSet);
	        Set<String> union = new HashSet<>(originalSet);
	        union.addAll(userSet);
	        return (double) (intersection.size() / union.size()) * 100;
	    }

	    private static Map<CharSequence, Integer> toVector(String text) {
	        String[] words = text.split(" ");
	        Map<CharSequence, Integer> vector = new HashMap<>();
	        for (String word : words) {
	            vector.put(word, vector.getOrDefault(word, 0) + 1);
	        }
	        return vector;
	    }

	    private static int calculateLevenshteinScore(String original, String user) {
	        LevenshteinDistance levenshteinDistance = new LevenshteinDistance();
	        int distance = levenshteinDistance.apply(original, user);
	        int maxPossibleDistance = Math.max(original.length(), user.length());
	        return 100 - distance * 100 / maxPossibleDistance;
	    }
}
