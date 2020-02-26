package assembler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class Assembler {
    private InstructionSet assemblerSet;

    private HashMap symbols;

    public void firstPassAssemble(String filename) {
        // Keeps track of addresses for labels
        int count = 0;

        File readFile = new File(filename);
        if(readFile.canRead()) {
            try {
                Scanner input = new Scanner(readFile);
                String symbolFilename = filename.substring(0, filename.length() - 4) + ".sym";
                // Makes a new file to put all of the labels and addresses
                File symbolFile = new File(symbolFilename);
                // Allows for manual appending of values into the new file
                FileWriter fileFirstPass = new FileWriter(symbolFile, false);

                // Looks for ORG of the program, and count is set to that value
                if(input.hasNextLine() && input.hasNext("ORG") == true) {
                    count = input.nextInt(16);
                }
                while(input.hasNextLine() && input.next() != "END") {
                    String nextLabel = input.nextLine();
                    // Searches the line for a label
                    if(nextLabel.substring(nextLabel.lastIndexOf(",")).equals(4)) {
                        // Label is added to written file, along with the hex address
                        fileFirstPass.write(nextLabel);
                        count++;
                        String incHex = Integer.toHexString(count);
                        fileFirstPass.write(" " + incHex);
                        // Label is put into a Hash Map at incHex address
                        symbols.put(nextLabel, incHex);
                    }
                }
                fileFirstPass.close();
            }
            // Needed to handle file exceptions
            catch (IOException ioexception) {
                System.out.println("IOException in First Pass");
            }
        }
    }
    public static void main(String[] args) {

    }

}
