package com.spamdetect;

import edu.stanford.nlp.pipeline.*;
import java.util.*;

public class App {
    // Our "Spammy" word list
    private static final Set<String> SPAM_WORDS = new HashSet<>(Arrays.asList(
        "winner", "free", "gift", "claim", "cash", "prize", "urgent", "link", "offer", "money"
    ));

    public static void main(String[] args) {
        // Initialize the AI Brain
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n*** SPAM DETECTOR READY ***");
        
        while (true) {
            System.out.print("\nEnter a message to check (or type 'exit'): ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) break;

            // Process text
            CoreDocument doc = new CoreDocument(input);
            pipeline.annotate(doc);

            int spamScore = 0;
            int totalWords = doc.tokens().size();

            for (var token : doc.tokens()) {
                String lemma = token.lemma().toLowerCase();
                if (SPAM_WORDS.contains(lemma)) {
                    spamScore++;
                }
            }

            // Decide the result
            double probability = (double) spamScore / totalWords;
            System.out.println("Analysis Result:");
            if (probability > 0.2) {
                System.out.println(">> [!] STATUS: SPAM DETECTED");
            } else {
                System.out.println(">> [✓] STATUS: HAM (CLEAN)");
            }
            System.out.println("Spam Probability: " + (int)(probability * 100) + "%");
        }
        scanner.close();
    }
}