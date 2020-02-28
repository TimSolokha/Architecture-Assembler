package assembler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class Assembler {

    private HashMap<String, String> symbols = new HashMap<String, String>();

    public void firstPassAssemble(String filename) {
        // Keeps track of addresses for labels
        int count = 0;
        String filePath = filename;

        File readFileFirst = new File(filePath);
        if(readFileFirst.canRead()) {
            try {
                Scanner symbolScan = new Scanner(readFileFirst);
                // Makes a new file to put all of the labels and addresses
                File symbolFile = new File("FirstPass1.sym");
                // Writes to a text file
                FileWriter fileFirstPass = new FileWriter(symbolFile);

                // Searches the line for a label
                while(symbolScan.hasNextLine()) {
                    String nextLabel = symbolScan.nextLine();
                    // Looks for ORG of the program, and count is set to that value
                    if(nextLabel.substring(5,8).equals("ORG")) {
                        count = Integer.parseInt(nextLabel.substring(9, 12), 16);
                    }
                    else if(!nextLabel.substring(0, 3).equals("   ")) {
                        // Label is added to written file without the comma along with the hex address
                        String str = nextLabel.substring(0, 3);
                        String incHex = Integer.toHexString(count);
                        fileFirstPass.write(str + " " + incHex + "\n");
                        count++;
                        // Label is put into a Hash Map at incHex address
                        symbols.put(str, incHex);
                    }
                    else if(nextLabel.substring(5, 8).equals("END")) {
                        break;
                    }
                    else {
                        count++;
                    }
                }
                symbolScan.close();
                fileFirstPass.close();
            }
            // Needed to handle file exceptions
            catch (IOException ioexception) {
                System.out.println("IOException in First Pass");
            }
        }
    }

    public void secondPassAssemble(String filename) {
        // Keeps track of addresses for each line
        int count = 0;

        HashMap<String, Integer> instructionSet = new HashMap<>();
        instructionSet.put("AND", 0x0000);
        instructionSet.put("ADD", 0x1000);
        instructionSet.put("LDA", 0x2000);
        instructionSet.put("STA", 0x3000);
        instructionSet.put("BUN", 0x4000);
        instructionSet.put("BSA", 0x5000);
        instructionSet.put("ISZ", 0x6000);
        instructionSet.put("CLA", 0x7800);
        instructionSet.put("CLE", 0x7400);
        instructionSet.put("CMA", 0x7200);
        instructionSet.put("CME", 0x7100);
        instructionSet.put("CIR", 0x7080);
        instructionSet.put("CIL", 0x7040);
        instructionSet.put("INC", 0x7020);
        instructionSet.put("SPA", 0x7010);
        instructionSet.put("SNA", 0x7008);
        instructionSet.put("SZA", 0x7004);
        instructionSet.put("SZE", 0x7002);
        instructionSet.put("HLT", 0x7001);
        instructionSet.put("INP", 0xF800);
        instructionSet.put("OUT", 0xF400);
        instructionSet.put("SKI", 0xF200);
        instructionSet.put("SKO", 0xF100);
        instructionSet.put("ION", 0xF080);
        instructionSet.put("IOF", 0xF040);

        File readFileSecond = new File(filename);
        if(readFileSecond.canRead()) {
            try {
                int digit = 0;
                int instructionLocation = 0;
                int indirectAddress = 0;
                String label = "";
                int hexLabel = 0;

                Scanner binScan = new Scanner(readFileSecond);
                File binFile = new File("SecondPass1.txt");
                FileWriter fileSecondPass = new FileWriter(binFile);

                while(binScan.hasNext()) {
                    String current = binScan.nextLine();
                    if(current.length() <= 12) {
                        current = current + " 000 0";
                    }

                    if(symbols.containsKey(current.substring(9, 12))) {
                        label = symbols.get(current.substring(9, 12));
                        label.replaceAll(current.substring(9, 12), label);
                        hexLabel = Integer.parseInt(label, 16);
                    }

                    if(current.substring(5, 8).equals("ORG")) {
                        count = Integer.parseInt(current.substring(9, 12), 16);
                    }
                    else {
                        if(current.substring(5, 8).equals("END")) {
                            break;
                        }
                        else if(current.substring(5, 8).equals("DEC")) {
                            if(current.charAt(11) == ' ') {
                                digit = Integer.parseInt(current.substring(9,11));
                            }
                            else {
                                digit = Integer.parseInt(current.substring(9, 12));
                            }
                        }
                        else if(current.substring(5, 8).equals("HEX")) {
                            if(current.charAt(10) == ' ' || current.charAt(10) == '\n') {
                                digit = Integer.parseInt(current.substring(9, 10), 16);
                            }
                            else if(current.contains("AA")) {
                                digit = 0x00AA;
                            }
                            else {
                                digit = Integer.parseInt(current.substring(9, 12), 16);
                            }
                        }
                        else if(instructionSet.containsKey(current.substring(5, 8))) {
                            instructionLocation = instructionSet.get(current.substring(5, 8));
                            digit = instructionLocation;

                            if(current.substring(13, 14).equalsIgnoreCase("i")) {
                                indirectAddress = 32768;
                                // Sets "digit" to the indirect address
                                digit = instructionLocation + indirectAddress;
                            }
                        }
                        String temp = Integer.toHexString(count);
                        temp = temp.toUpperCase();
                        // Writes the correct address
                        String assembleAddress = ("000" + temp).substring(temp.length());

                        temp = Integer.toHexString(digit + hexLabel);
                        temp = temp.toUpperCase();
                        // Writes the correct assembly instruction
                        String assembleInstruction = ("0000" + temp).substring(temp.length());
                        // Reset values of hexLabel and digit
                        digit = 0;
                        hexLabel = 0;
                        indirectAddress = 0;
                        // The format for the output file
                        String writeInstruction = assembleAddress + ":   " + assembleInstruction;
                        fileSecondPass.write(writeInstruction);
                        fileSecondPass.write("\n");
                        // Move to the next line
                        count++;
                    }
                }
                fileSecondPass.close();
                binScan.close();
            }
            catch (IOException ioexception) {
                System.out.println("IOException in Second Pass.");
            }
        }
    }

    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        System.out.print("Enter a filename: ");
        String str = scan.nextLine();

        File file1 = new File(str);
        String testPath1 = file1.getAbsolutePath();


        Assembler testAssembler1 = new Assembler();
        testAssembler1.firstPassAssemble(testPath1);
        testAssembler1.secondPassAssemble(testPath1);
    }

}
