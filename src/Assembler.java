import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class Assembler {
    final static Map<String, String> OPCODES = new HashMap<>();
    final static Map<String, String> REGISTERS = new HashMap<>();

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
        File file = new File("input");
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.printf("%s\n", line);
                line = line.replaceAll(",", "");
                String[] tokens = line.split(" ");
                String opcode = OPCODES.get(tokens[0]);
                String instruction = tokens[0];
                String REG, DR, SR, SR1, SR2, OP1, OP2, IMM, ADDR, binaryADDR;


                switch (instruction) {
                    case "ADD":
                    case "AND":
                    case "NAND":
                    case "NOR":
                        DR = tokens[1];
                        SR1 = tokens[2];
                        SR2 = tokens[3];
                        System.out.printf("%s %s %s %s 00\n", opcode, REGISTERS.get(DR), REGISTERS.get(SR1), REGISTERS.get(SR2));
                        break;
                    case "ADDI":
                    case "ANDI":
                        DR = tokens[1];
                        SR = tokens[2];
                        IMM = tokens[3];
                        String binaryIMM = Integer.toBinaryString(Integer.parseInt(IMM));
                        if (Integer.parseInt(IMM) < -32 || Integer.parseInt(IMM) > 31) {
                            throw new IllegalArgumentException("Immediate value out of range!");
                        }
                        if (Integer.parseInt(IMM) < 0) {
                            binaryIMM = binaryIMM.substring(binaryIMM.length() - 6);
                        }
                        binaryIMM = String.format("%06d", Integer.parseInt(binaryIMM));

                        System.out.printf("%s %s %s %s\n", opcode, REGISTERS.get(DR), REGISTERS.get(SR), binaryIMM);
                        break;
                    case "LD":
                    case "ST":
                        REG = tokens[1];
                        ADDR = tokens[2];
                        binaryADDR = Integer.toBinaryString(Integer.parseInt(ADDR));
                        if (Integer.parseInt(ADDR) > 1023) {
                            throw new IllegalArgumentException("Address out of range!");
                        }
                        binaryADDR = String.format("%010d", Integer.parseInt(binaryADDR));
                        System.out.printf("%s %s %s\n", opcode, REGISTERS.get(REG), binaryADDR);
                        break;
                    case "CMP":
                        OP1 = tokens[1];
                        OP2 = tokens[2];
                        System.out.printf("%s %s %s 000000\n", opcode, REGISTERS.get(OP1), REGISTERS.get(OP2));
                        break;
                    case "JUMP":
                    case "JE":
                    case "JA":
                    case "JB":
                    case "JAE":
                    case "JBE":
                        ADDR = tokens[1];
                        binaryADDR = Integer.toBinaryString(Integer.parseInt(ADDR));
                        if (Integer.parseInt(ADDR) > 1023) {
                            throw new IllegalArgumentException("Address out of range!");
                        }
                        binaryADDR = String.format("%010d", Integer.parseInt(binaryADDR));
                        System.out.printf("%s 0000 %s\n", opcode, binaryADDR);
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}