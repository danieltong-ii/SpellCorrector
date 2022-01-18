package spell;

import java.io.IOException;
import java.io.*;
import java.util.*;


public class SpellCorrector implements ISpellCorrector {
    //create our ITrie data structure for this program, which we can use for both creating the
    //dictionary (TRIEMAP) and later searching for stuff.

    ITrie myTrie = new Trie();
    int editDistanceNumber = 0;

    @Override
    public void useDictionary(String dictionaryFileName) throws IOException {
        myTrie = new Trie();
        editDistanceNumber = 0;

        //convert dictionaryFileName to File Object
        File srcFile = new File(dictionaryFileName);
        //input dictionary File Object into scanner
        Scanner scanner = new Scanner(srcFile);
        // while there are still text in the file, add to myTrie.
        while (scanner.hasNext()) {
            myTrie.add(scanner.next());
        }
    }

    public ArrayList<String> FourEditDistance(String inputWord) {
        ArrayList<String> EDOList;
        EDOList = new ArrayList<>();

        // DELETION
        for (int i = 0; i < inputWord.length(); i++) {
            StringBuilder sb = new StringBuilder(inputWord);
            String resultString = sb.deleteCharAt(i).toString();
            EDOList.add(resultString);
        }
        //TRANSPOSE
        for (int i = 0; i < inputWord.length() - 1; i++) {
            char[] charArray = inputWord.toCharArray();
            char temp = charArray[i];
            charArray[i] = charArray[i + 1];
            charArray[i + 1] = temp;
            //Convert Char Array back to String and Add to Edit Distance One List
            String charToString = new String(charArray);
            EDOList.add(charToString);
        }
        //ALTERATION
        for (int i = 0; i < inputWord.length(); i++) {
            char[] charArray = inputWord.toCharArray();
            for (char alpha = 'a'; alpha <= 'z'; alpha++) {
                charArray[i] = alpha;
                String charToString = new String(charArray);
                EDOList.add(charToString);
            }
        }
        //INSERTION

        for (int i = 0; i <= inputWord.length(); i++) {
            for (char alpha = 'a'; alpha <= 'z'; alpha++) {
                StringBuilder sb = new StringBuilder(inputWord);
                String new_string = sb.insert(i, alpha).toString();
                EDOList.add(new_string);
            }
        }


        return EDOList;
    }

    @Override
    public String suggestSimilarWord(String inputWord) {
        inputWord = inputWord.toLowerCase();
        if (inputWord.isEmpty() == false) {
            INode search_result = myTrie.find(inputWord);

            if (search_result != null) {
                return inputWord;
            }
            else {
                ArrayList<String> EditDistanceOnelist = FourEditDistance(inputWord);
                return FindMostSimilar(EditDistanceOnelist);
            }
        }
        else {
            return null;
        }
    }



    public String FindMostSimilar(ArrayList<String> EDOList) {

        //ClOSEST EDIT DISTANCE
        int match_counter = 0;
        editDistanceNumber++;
        ArrayList<String> match_criteria_one = new ArrayList<>();
        for (int i = 0; i < EDOList.size(); i++) {
            INode eval_node = myTrie.find(EDOList.get(i));
            if (eval_node != null) {
                match_counter++;
                match_criteria_one.add((EDOList.get(i)));
            }
        }
        if ((match_criteria_one.isEmpty() == true) && (editDistanceNumber < 2)) {
            //NO EDIT DISTANCE ONE RESULTS, GO TO EDIT DISTANCE TWO
            ArrayList<String> MergedList = new ArrayList<>();
            for (int i = 0; i < EDOList.size(); i++) {
                ArrayList<String> Temp_List = FourEditDistance(EDOList.get(i));
                MergedList.addAll(Temp_List);
            }
            //CALL FindMostSimilar
            return FindMostSimilar(MergedList);
        }
        //we have an array of strings in match critiera one
        if (match_counter > 1) {
            //FOUND THE MOST TIMES IN DICTIONARY
            ArrayList<String> match_criteria_two = new ArrayList<>();
            int highest_frequency = 0;
            int multiple_same_highest_frequency = 0;
            String word_highest_frequency;
            for (int i = 0; i < match_criteria_one.size();i++) {
                INode eval_node = myTrie.find(match_criteria_one.get(i));


                if (eval_node.getValue() > highest_frequency) {
                    highest_frequency =  eval_node.getValue();
                }
            }
            // now we know what's the highest frequency, we can check to see if there are multiple
            for (int i = 0; i < match_criteria_one.size();i++) {
                INode eval_node = myTrie.find(match_criteria_one.get(i));
                if (eval_node.getValue() == highest_frequency) {
                    //add to match both close and high frequency and
                    match_criteria_two.add(match_criteria_one.get(i));
                    multiple_same_highest_frequency++;
                }
            }
            // Now we have a arraylist of the edit distance one that has highest frequency
            if (multiple_same_highest_frequency > 1) {
                Collections.sort(match_criteria_two);
                return match_criteria_two.get(0).toString();
            }
            else {
                return match_criteria_two.get(0);
            }
        }
        else if (match_counter == 1){
            return match_criteria_one.get(0);
        }
        return null;
    }
}
