import java.util.HashMap;
import java.util.Map;

public class Assembler {
    final static Map<String,String> OPCODES = new HashMap<>();
    final static Map<String,String> REGISTERS = new HashMap<>();

    static {
        OPCODES.put("ADD", "0000");
        OPCODES.put("ADDI", "0001");
        OPCODES.put("AND", "0010");
        OPCODES.put("ANDI", "0011");
        OPCODES.put("NAND", "0100");
        OPCODES.put("NOR", "0101");
        OPCODES.put("LD", "0110");
        OPCODES.put("ST", "0111");
        OPCODES.put("CMP", "1000");
        OPCODES.put("JUMP", "1001");
        OPCODES.put("JE", "1010");
        OPCODES.put("JA", "1011");
        OPCODES.put("JB", "1100");
        OPCODES.put("JAE", "1101");
        OPCODES.put("JBE", "1110");

        REGISTERS.put("R0", "0000");
        REGISTERS.put("R1", "0001");
        REGISTERS.put("R2", "0010");
        REGISTERS.put("R3", "0011");
        REGISTERS.put("R4", "0100");
        REGISTERS.put("R5", "0101");
        REGISTERS.put("R6", "0110");
        REGISTERS.put("R7", "0111");
        REGISTERS.put("R8", "1000");
        REGISTERS.put("R9", "1001");
        REGISTERS.put("R10", "1010");
        REGISTERS.put("R11", "1011");
        REGISTERS.put("R12", "1100");
        REGISTERS.put("R13", "1101");
        REGISTERS.put("R14", "1110");
        REGISTERS.put("R15", "1111");
    }
    public static void main(String[] args) {
        System.out.println("Hello World!");
    }
}