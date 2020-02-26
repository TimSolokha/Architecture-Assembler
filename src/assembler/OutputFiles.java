package assembler;

import java.io.File;

public class OutputFiles {

    private File binFile;
    private File symbolTable;

    public OutputFiles(File bin, File symbol) {
        this.binFile = bin;
        this.symbolTable = symbol;
    }

    public File getBinFile() {
        return this.binFile;
    }

    public File getSymbolTable() {
        return this.symbolTable;
    }

    public String printBinFile() {
        //TODO: Add parsing of file after finishing Assembler.java
        return "";
    }

    public String printSymbolTable() {
        //TODO: Add parsing of file after finishing Assembler.java
        return "";
    }
}
