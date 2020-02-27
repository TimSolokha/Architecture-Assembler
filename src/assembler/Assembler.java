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

        File readFileFirst = new File(filename);
        if(readFileFirst.canRead()) {
            try {
                Scanner symbolScan = new Scanner(readFileFirst);
                String symbolFilename = filename.substring(0, filename.length() - 4) + ".sym";
                // Makes a new file to put all of the labels and addresses
                File symbolFile = new File(symbolFilename);
                // Allows for manual appending of values into the new file
                FileWriter fileFirstPass = new FileWriter(symbolFile, false);

                // Looks for ORG of the program, and count is set to that value
                if(symbolScan.hasNextLine() && symbolScan.hasNext("ORG") == true) {
                    count = symbolScan.nextInt(16);
                }
                while(symbolScan.hasNextLine() && symbolScan.next() != "END") {
                    String nextLabel = symbolScan.nextLine();
                    // Searches the line for a label
                    if(nextLabel.substring(nextLabel.lastIndexOf(",")).equals(3)) {
                        // Label is added to written file without the comma along with the hex address
                        nextLabel.replace(",","");
                        fileFirstPass.write(nextLabel);
                        count++;
                        String incHex = Integer.toHexString(count);
                        fileFirstPass.write(" " + incHex);
                        // Label is put into a Hash Map at incHex address
                        symbols.put(nextLabel, incHex);
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
        // Keeps track of addresses for labels
        int count = 0;

        File readFileSecond = new File(filename);
        if(readFileSecond.canRead()) {
            try {
                int digit = 0;
                int memoryLocation = 0;
                int indirectAddress = 0;

                Scanner binScan = new Scanner(readFileSecond);
                String assemblerFilename = filename.substring(0, filename.length() - 4) + ".sym";
                // Makes a new file to put all of the instructions
                File binFile = new File(assemblerFilename);
                // Allows for manual appending of values into the new file
                FileWriter fileSecondPass = new FileWriter(binFile, false);

                while(binScan.hasNext()) {
                    // Find a certain instruction based on where the scanner is
                    String current = binScan.next();
                    int opCode = assemblerSet.findInstruction(current);

                    if(current.equals("ORG")) {
                        count = binScan.nextInt(16);
                        continue;
                    }
                    if(current.equals("END")) {
                        break;
                    }
                    else if(current.equals("DEC")) {
                        digit = binScan.nextInt();
                    }
                    else if(current.equals("HEX")) {
                        digit = binScan.nextInt(16);
                    }
                    else if(opCode == -1 && symbols.containsKey(current)) {
                        if(binScan.hasNext()) {
                            String symCurrent = binScan.next();
                            if (symCurrent.equals("DEC")) {
                                digit = binScan.nextInt();
                            } else if (symCurrent.equals("HEX")) {
                                digit = binScan.nextInt(16);
                            } else {
                                opCode = assemblerSet.findInstruction(symCurrent);
                                if ((opCode & 0xFFF) != 0) {
                                    digit = opCode;
                                } else {
                                    if (binScan.hasNextInt()) {
                                        memoryLocation = binScan.nextInt(16);
                                    } else if (binScan.hasNext()) {
                                        symCurrent = binScan.next();
                                        if (this.symbols.containsKey(symCurrent)) {
                                            memoryLocation = ((Integer) this.symbols.get(symCurrent)).intValue();
                                        } else {
                                            binScan.close();
                                            fileSecondPass.close();
                                            System.out.println("Invalid instruction at line: " + Integer.toHexString(count));
                                        }
                                    } else {
                                        binScan.close();
                                        fileSecondPass.close();
                                        System.out.println("Invalid line at: " + Integer.toHexString(count));
                                    }
                                    if(binScan.hasNext() && binScan.next().equalsIgnoreCase("i")) {
                                        indirectAddress = 32768;
                                        digit = opCode + indirectAddress + memoryLocation;
                                    }
                                }
                            }
                        }
                    }
                    else if((opCode & 0xFFF) != 0) {
                        digit = opCode;
                    }
                    else if((opCode & 0xFFF) != 1) {
                        memoryLocation = ((Integer) symbols.get(binScan.next())).intValue();
                        if(binScan.hasNext() && binScan.next().equalsIgnoreCase("i")) {
                            indirectAddress = 32768;
                            digit = opCode + indirectAddress + memoryLocation;
                        }
                    } else {
                        binScan.close();
                        fileSecondPass.close();
                        System.out.println("Invalid line at: " + Integer.toHexString(count));
                    }
                    String temp = Integer.toHexString(count);
                    temp = temp.toUpperCase();
                    String assembleAddress = ("000" + temp).substring(temp.length());
                    temp = Integer.toHexString(digit & 0xFFFF);
                    temp = temp.toUpperCase();
                    String assembleInstruction = ("0000" + temp).substring(temp.length());
                    String writeInstruction = assembleAddress + ":   " + assembleInstruction;
                    fileSecondPass.append(writeInstruction);
                    fileSecondPass.append("\n");
                    binScan.close();
                    count++;
                }
                fileSecondPass.close();
            }
            catch (IOException ioexception) {
                System.out.println("IOException in Second Pass");
            }
        } else {
            System.out.println("File can't be read.");
        }
    }

    public static void main(String[] args) {

    }

}
