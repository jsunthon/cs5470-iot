package logic;

import models.nodes.Summary;

import java.io.*;

class SummaryParser {

    private String  filename;
    private Summary socialSummary;
    private Summary decentralizedSummary;


    SummaryParser(String filename) {
        this.filename = filename;
        socialSummary = new Summary("S");
        decentralizedSummary = new Summary("D");
    }

    void parse() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));

        String currentLine;

        boolean skip = true;
        while ((currentLine = br.readLine()) != null) {
            String[] line = currentLine.split(",");

            // Skip the first line because it is the header;
            if (skip) {
                skip = false;
                continue;
            }

            // Type (Social or Decentralized)
            if (line[0].equals("S")) {
                updateSummary(socialSummary, line);
            } else if (line[0].equals("D")) {
                updateSummary(decentralizedSummary, line);
            }

        }

        System.out.println(socialSummary);
        System.out.println(decentralizedSummary);
    }


    private void updateSummary(Summary summary, String[] line) {
        // isSuccess Field
        if (line[5].equals("T")) {
            summary.numberOfSuccess++;
        }
        summary.totalNumber++;

        // bandwidth field / num_of_nodes_contacted
        summary.totalNodesContacted += Integer.valueOf(line[6]);

        // Latency Field
        summary.totalLatency += Integer.parseInt(line[8]);

        // Hops Field, "1", "2"
        if (summary.hopsCounter.containsKey(line[9])) {
            summary.hopsCounter.put(line[9], summary.hopsCounter.get(line[9]) + 1);
        } else {
            summary.hopsCounter.put(line[9], 1);
        }
    }
}
