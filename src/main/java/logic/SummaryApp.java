package logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SummaryApp {

    public static void main(String[] args) {
        List<String> filenames = new ArrayList<>();
        for (String file : App.FILENAMES_20_K) {
            filenames.add(App.RESULT_DIRECTORY + file + ".csv");
        }

        for (String filename : filenames) {
            System.out.println(filename);
            SummaryParser sp = new SummaryParser(filename);
            try {
                sp.parse();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
