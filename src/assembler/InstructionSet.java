package assembler;

import java.util.HashMap;
import java.io.File;

public class InstructionSet {
    private HashMap instructionSet;

    public InstructionSet() {
        this.instructionSet = new HashMap();
        this.instructionSet.put("AND", 0x0000);
        this.instructionSet.put("ADD", 0x1000);
        this.instructionSet.put("LDA", 0x2000);
        this.instructionSet.put("STA", 0x3000);
        this.instructionSet.put("BUN", 0x4000);
        this.instructionSet.put("BSA", 0x5000);
        this.instructionSet.put("ISZ", 0x6000);
        this.instructionSet.put("CLA", 0x7800);
        this.instructionSet.put("CLE", 0x7400);
        this.instructionSet.put("CMA", 0x7200);
        this.instructionSet.put("CME", 0x7100);
        this.instructionSet.put("CIR", 0x7080);
        this.instructionSet.put("CIL", 0x7040);
        this.instructionSet.put("INC", 0x7020);
        this.instructionSet.put("SPA", 0x7010);
        this.instructionSet.put("SNA", 0x7008);
        this.instructionSet.put("SZA", 0x7004);
        this.instructionSet.put("SZE", 0x7002);
        this.instructionSet.put("HLT", 0x7001);
        this.instructionSet.put("INP", 0xF800);
        this.instructionSet.put("OUT", 0xF400);
        this.instructionSet.put("SKI", 0xF200);
        this.instructionSet.put("SKO", 0xF100);
        this.instructionSet.put("ION", 0xF080);
        this.instructionSet.put("IOF", 0xF040);

    }
}
