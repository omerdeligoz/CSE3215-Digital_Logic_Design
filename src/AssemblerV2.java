import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class AssemblerV2 {
    final static Map<String, String> OPCODES = new HashMap<>();
    final static Map<String, String> REGISTERS = new HashMap<>();
    static String output = "v2.0 raw\n";

    static {
        OPCODES.put("ADD", "000");
        OPCODES.put("ADDI", "000");
        OPCODES.put("AND", "001");
        OPCODES.put("ANDI", "001");
        OPCODES.put("NAND", "010");
        OPCODES.put("NOR", "011");
        OPCODES.put("LD", "100");
        OPCODES.put("ST", "101");
        OPCODES.put("CMP", "110");
        OPCODES.put("JUMP", "111");
        OPCODES.put("JE", "111");
        OPCODES.put("JA", "111");
        OPCODES.put("JB", "111");
        OPCODES.put("JAE", "111");
        OPCODES.put("JBE", "111");


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
        File file = new File("input.txt");
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
//                System.out.printf("%s\n", line);
                line = line.replaceAll(",", "");
                String[] tokens = line.split(" ");
                String opcode = OPCODES.get(tokens[0]);
                String instruction = tokens[0];
                String REG, DR, SR, SR1, SR2, OP1, OP2, IMM, ADDR, binaryADDR;
                String binaryString, hexString;
                switch (instruction) {
                    case "ADD":
                    case "AND":
                    case "NAND":
                    case "NOR":
                        DR = tokens[1];
                        SR1 = tokens[2];
                        SR2 = tokens[3];

                        binaryString = opcode + REGISTERS.get(DR) + REGISTERS.get(SR1) + "000" + REGISTERS.get(SR2);
                        hexString = binaryToHex(binaryString);
                        output += hexString + " ";
                        System.out.printf("%s%s%s000%s\n", opcode, REGISTERS.get(DR), REGISTERS.get(SR1), REGISTERS.get(SR2));
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

                        binaryString = opcode + REGISTERS.get(DR) + REGISTERS.get(SR) + "1" + binaryIMM;
                        hexString = binaryToHex(binaryString);
                        output += hexString + " ";
                        System.out.printf("%s%s%s1%s\n", opcode, REGISTERS.get(DR), REGISTERS.get(SR), binaryIMM);
                        break;
                    case "LD":
                    case "ST":
                        REG = tokens[1];
                        ADDR = tokens[2];
                        binaryADDR = Integer.toBinaryString(Integer.parseInt(ADDR));
                        if (Integer.parseInt(ADDR) > 2047) {
                            throw new IllegalArgumentException("Address out of range!");
                        }
                        binaryADDR = String.format("%011d", Integer.parseInt(binaryADDR));

                        binaryString = opcode + REGISTERS.get(REG) + binaryADDR;
                        hexString = binaryToHex(binaryString);
                        output += hexString + " ";
                        System.out.printf("%s%s%s\n", opcode, REGISTERS.get(REG), binaryADDR);
                        break;
                    case "CMP":
                        OP1 = tokens[1];
                        OP2 = tokens[2];
                        binaryString = opcode + REGISTERS.get(OP1) + REGISTERS.get(OP2) + "0000000";
                        hexString = binaryToHex(binaryString);
                        output += hexString + " ";
                        System.out.printf("%s%s%s0000000\n", opcode, REGISTERS.get(OP1), REGISTERS.get(OP2));
                        break;
                    case "JUMP":
                    case "JE":
                    case "JA":
                    case "JB":
                    case "JAE":
                    case "JBE":
                        ADDR = tokens[1];
                        binaryADDR = Integer.toBinaryString(Integer.parseInt(ADDR));
                        if (Integer.parseInt(ADDR) > 2047) {
                            throw new IllegalArgumentException("Address out of range!");
                        }
                        binaryADDR = String.format("%011d", Integer.parseInt(binaryADDR));
                        switch (instruction) {
                            case "JUMP":
                                binaryString = opcode + "0000" + binaryADDR;
                                hexString = binaryToHex(binaryString);
                                output += hexString + " ";
                                System.out.printf("%s0000%s\n", opcode, binaryADDR);
                                break;
                            case "JE":
                                binaryString = opcode + "0001" + binaryADDR;
                                hexString = binaryToHex(binaryString);
                                output += hexString + " ";
                                System.out.printf("%s0001%s\n", opcode, binaryADDR);
                                break;
                            case "JA":
                                binaryString = opcode + "0010" + binaryADDR;
                                hexString = binaryToHex(binaryString);
                                output += hexString + " ";
                                System.out.printf("%s0010%s\n", opcode, binaryADDR);
                                break;
                            case "JB":
                                binaryString = opcode + "0011" + binaryADDR;
                                hexString = binaryToHex(binaryString);
                                output += hexString + " ";
                                System.out.printf("%s0011%s\n", opcode, binaryADDR);
                                break;
                            case "JAE":
                                binaryString = opcode + "0100" + binaryADDR;
                                hexString = binaryToHex(binaryString);
                                output += hexString + " ";
                                System.out.printf("%s0100%s\n", opcode, binaryADDR);
                                break;
                            case "JBE":
                                binaryString = opcode + "0101" + binaryADDR;
                                hexString = binaryToHex(binaryString);
                                output += hexString + " ";
                                System.out.printf("%s0101%s\n", opcode, binaryADDR);
                                break;
                        }
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        System.out.println(output);

    }

    public static String binaryToHex(String binaryString) {
        // Check if the binary input.txt length is exactly 18 bits
        if (binaryString.length() != 18) {
            throw new IllegalArgumentException("Input must be an 18-bit binary string");
        }

        int decimalValue = Integer.parseInt(binaryString, 2);
        String hexOutput = Integer.toHexString(decimalValue);
        while (hexOutput.length() < 5) {
            hexOutput = "0" + hexOutput;
        }
        return hexOutput.toUpperCase();
    }
}


/*

                 BINARY PRINTS IN SEPERATED FORMAT


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class AssemblerV2 {
    final static Map<String, String> OPCODES = new HashMap<>();
    final static Map<String, String> REGISTERS = new HashMap<>();
    static String output = "";

    static {
        OPCODES.put("ADD", "000");
        OPCODES.put("ADDI", "000");
        OPCODES.put("AND", "001");
        OPCODES.put("ANDI", "001");
        OPCODES.put("NAND", "010");
        OPCODES.put("NOR", "011");
        OPCODES.put("LD", "100");
        OPCODES.put("ST", "101");
        OPCODES.put("CMP", "110");
        OPCODES.put("JUMP", "111");
        OPCODES.put("JE", "111");
        OPCODES.put("JA", "111");
        OPCODES.put("JB", "111");
        OPCODES.put("JAE", "111");
        OPCODES.put("JBE", "111");


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
        File file = new File("input.txt");
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
//                System.out.printf("%s\n", line);
                line = line.replaceAll(",", "");
                String[] tokens = line.split(" ");
                String opcode = OPCODES.get(tokens[0]);
                String instruction = tokens[0];
                String REG, DR, SR, SR1, SR2, OP1, OP2, IMM, ADDR, binaryADDR;
                String binaryString, hexString;
                switch (instruction) {
                    case "ADD":
                    case "AND":
                    case "NAND":
                    case "NOR":
                        DR = tokens[1];
                        SR1 = tokens[2];
                        SR2 = tokens[3];

                        binaryString = opcode + REGISTERS.get(DR) + REGISTERS.get(SR1) + "000" + REGISTERS.get(SR2);
                        hexString = binaryToHex(binaryString);
                        output += hexString + " ";
                        System.out.printf("%s %s %s 0 00 %s\n", opcode, REGISTERS.get(DR), REGISTERS.get(SR1), REGISTERS.get(SR2));
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

                        binaryString = opcode + REGISTERS.get(DR) + REGISTERS.get(SR) + "1" + binaryIMM;
                        hexString = binaryToHex(binaryString);
                        output += hexString + " ";
                        System.out.printf("%s %s %s 1 %s\n", opcode, REGISTERS.get(DR), REGISTERS.get(SR), binaryIMM);
                        break;
                    case "LD":
                    case "ST":
                        REG = tokens[1];
                        ADDR = tokens[2];
                        binaryADDR = Integer.toBinaryString(Integer.parseInt(ADDR));
                        if (Integer.parseInt(ADDR) > 2047) {
                            throw new IllegalArgumentException("Address out of range!");
                        }
                        binaryADDR = String.format("%011d", Integer.parseInt(binaryADDR));

                        binaryString = opcode + REGISTERS.get(REG) + binaryADDR;
                        hexString = binaryToHex(binaryString);
                        output += hexString + " ";
                        System.out.printf("%s %s %s\n", opcode, REGISTERS.get(REG), binaryADDR);
                        break;
                    case "CMP":
                        OP1 = tokens[1];
                        OP2 = tokens[2];
                        binaryString = opcode + REGISTERS.get(OP1) + REGISTERS.get(OP2) + "0000000";
                        hexString = binaryToHex(binaryString);
                        output += hexString + " ";
                        System.out.printf("%s %s %s 0000000\n", opcode, REGISTERS.get(OP1), REGISTERS.get(OP2));
                        break;
                    case "JUMP":
                    case "JE":
                    case "JA":
                    case "JB":
                    case "JAE":
                    case "JBE":
                        ADDR = tokens[1];
                        binaryADDR = Integer.toBinaryString(Integer.parseInt(ADDR));
                        if (Integer.parseInt(ADDR) > 2047) {
                            throw new IllegalArgumentException("Address out of range!");
                        }
                        binaryADDR = String.format("%011d", Integer.parseInt(binaryADDR));
                        switch (instruction) {
                            case "JUMP":
                                binaryString = opcode + "0000" + binaryADDR;
                                hexString = binaryToHex(binaryString);
                                output += hexString + " ";
                                System.out.printf("%s 0000 %s\n", opcode, binaryADDR);
                                break;
                            case "JE":
                                binaryString = opcode + "0001" + binaryADDR;
                                hexString = binaryToHex(binaryString);
                                output += hexString + " ";
                                System.out.printf("%s 0001 %s\n", opcode, binaryADDR);
                                break;
                            case "JA":
                                binaryString = opcode + "0010" + binaryADDR;
                                hexString = binaryToHex(binaryString);
                                output += hexString + " ";
                                System.out.printf("%s 0010 %s\n", opcode, binaryADDR);
                                break;
                            case "JB":
                                binaryString = opcode + "0011" + binaryADDR;
                                hexString = binaryToHex(binaryString);
                                output += hexString + " ";
                                System.out.printf("%s 0011 %s\n", opcode, binaryADDR);
                                break;
                            case "JAE":
                                binaryString = opcode + "0100" + binaryADDR;
                                hexString = binaryToHex(binaryString);
                                output += hexString + " ";
                                System.out.printf("%s 0100 %s\n", opcode, binaryADDR);
                                break;
                            case "JBE":
                                binaryString = opcode + "0101" + binaryADDR;
                                hexString = binaryToHex(binaryString);
                                output += hexString + " ";
                                System.out.printf("%s 0101 %s\n", opcode, binaryADDR);
                                break;
                        }
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        System.out.println(output);

    }

    public static String binaryToHex(String binaryString) {
        // Check if the binary input.txt length is exactly 18 bits
        if (binaryString.length() != 18) {
            throw new IllegalArgumentException("Input must be an 18-bit binary string");
        }

        int decimalValue = Integer.parseInt(binaryString, 2);
        String hexOutput = Integer.toHexString(decimalValue);
        while (hexOutput.length() < 5) {
            hexOutput = "0" + hexOutput;
        }
        return hexOutput.toUpperCase();
    }
}
 */