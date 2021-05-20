import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    static void filesIndex(int[] N, int V, int[] startIndex, int[] endIndex) {
        for (int i = 0; i < N.length; i++) {
            startIndex[i] = N[i] / 50 * (V - 1);
            endIndex[i] = N[i] / 50 * V;
        }
    }

    static void searchIndex(String testWord, HashMap<String, ArrayList<String>> dictionary) {
        ArrayList<String> array = null;
        if (dictionary.containsKey(testWord)) {
            array = dictionary.get(testWord);
            for (String w : array)
                System.out.println(w);
        }
    }

    public static void main(String[] args) {
        int V = 5;
        int[] startIndex = new int[5];
        int[] endIndex = new int[5];
        int[] N = {12500, 12500, 12500, 12500, 50000};
        File[] directions = {
                new File("aclImdb//test//neg"),
                new File("aclImdb//test//pos"),
                new File("aclImdb//train//neg"),
                new File("aclImdb//train//pos"),
                new File("aclImdb//train//unsup")
        };

        HashMap<String, ArrayList<String>> dictionary = new HashMap<>();

        filesIndex(N, V, startIndex, endIndex);

        for (int j = 0; j < directions.length; j++) {
            File dir = directions[j];
            if (dir.isDirectory()) {
                File[] arrayFiles = dir.listFiles();

                if (arrayFiles != null)
                    for (File file : arrayFiles) {
                        String path = dir.getParent() + "\\" + dir.getName() + "\\" + file.getName();

                        int nameInt = Integer.parseInt(file.getName()
                                .replaceAll("_+\\d+.txt", ""));

                        if (nameInt >= startIndex[j] && nameInt < endIndex[j]) {

                            try (BufferedReader bufReader = new BufferedReader(new FileReader(file))) {
                                String line;

                                while ((line = bufReader.readLine()) != null) {
                                    line = line.replaceAll("<br /><br />", " ")
                                            .replaceAll("[^A-Za-z0-9']", " ");

                                    String[] tokens = line.split("\\s*(\\s)\\s*");

                                    for (String token : tokens) {
                                        dictionary.putIfAbsent(token, new ArrayList<String>());
                                        if (!dictionary.get(token).contains(path))
                                            dictionary.get(token).add(path);
                                    }
                                }
                            } catch (IOException exc) {
                                System.out.println("File read error");
                            }
                        }
                    }
            }
        }

        searchIndex("The", dictionary);

        System.out.println("debugger"); // check dictionary by debugger
    }
}