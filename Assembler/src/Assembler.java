import java.io.*;
import java.util.*;

public class Assembler {

    private static HashMap<String, int[]> RType = new HashMap<>(12);
    private static HashMap<String, Integer> IType = new HashMap<>(18);
    private static HashMap<String, Integer> JType = new HashMap<>(3);
    //private static String[][] table = new String[30][3];
    public static void main(String[] args) {

        Scanner fileName = new Scanner(System.in);
        System.out.println("Enter the absolute path");
        String name = fileName.nextLine();
        constructMaps();

        try (BufferedReader buffer = new BufferedReader(new FileReader(name))){
            int instNum = 0;
            //int n = 0;
            StringBuilder InstSet = new StringBuilder();
            while (buffer.ready()){
                String[] terms = buffer.readLine().replace("\t", "").split(" ");
                String machineCode = "";
                int regNum = 0;
                if (RType.containsKey(terms[0].toUpperCase())){
                    System.out.println(Arrays.toString(terms));
                    instNum++;
                    //n++;
                    int[] OPwFUN = RType.get(terms[0].toUpperCase());

                    String op = Integer.toBinaryString(OPwFUN[0]);
                    op = String.format("%5s", op).replace(' ', '0');

                    String fun = Integer.toBinaryString(OPwFUN[1]);
                    fun = String.format("%2s", fun).replace(' ', '0');

                    regNum = terms[1].charAt(terms[1].length() - 1) - 48;
                    String Rd = Integer.toBinaryString(regNum);
                    Rd = String.format("%3s", Rd).replace(' ', '0');

                    regNum = terms[2].charAt(terms[2].length() - 1) - 48;
                    String Rs = Integer.toBinaryString(regNum);
                    Rs = String.format("%3s", Rs).replace(' ', '0');

                    regNum = terms[3].charAt(terms[3].length() - 1) - 48;
                    String Rt = Integer.toBinaryString(regNum);
                    Rt = String.format("%3s", Rt).replace(' ', '0');

                    machineCode = fun + Rd + Rt + Rs + op;
                    //table[n][1] = Arrays.toString(terms);
                    //table[n][2] = machineCode;
                }
                else if (IType.containsKey(terms[0].toUpperCase())){
                    System.out.println(Arrays.toString(terms));
                    instNum++;
                    //n++;
                    int OP = IType.get(terms[0].toUpperCase());

                    String op = Integer.toBinaryString(OP);
                    op = String.format("%5s", op).replace(' ', '0');

                    regNum = terms[1].charAt(terms[1].length() - 1) - 48;
                    String Rt = Integer.toBinaryString(regNum);
                    Rt = String.format("%3s", Rt).replace(' ', '0');

                    regNum = terms[2].charAt(terms[2].length() - 1) - 48;
                    String Rs = Integer.toBinaryString(regNum);
                    Rs = String.format("%3s", Rs).replace(' ', '0');

                    String imm5 = Integer.toBinaryString(Integer.parseInt(terms[3]));
                    if (imm5.length() < 5)
                        imm5 = String.format("%5s", imm5).replace(' ', '0');
                    else
                        imm5 = imm5.substring(imm5.length()-5);

                    String sa = Integer.toBinaryString(Integer.parseInt(terms[3]));
                    sa = String.format("%5s", sa).replace(' ', '0');

                    if ((OP <= 15) && (OP >= 12))
                        machineCode = sa + Rt + Rs + op;
                    else
                        machineCode = imm5 + Rt + Rs + op;

                    //table[n][1] = Arrays.toString(terms);
                    //table[n][2] = machineCode;
                }
                else if (JType.containsKey(terms[0].toUpperCase())){
                    System.out.println(Arrays.toString(terms));
                    instNum++;
                    //n++;
                    int OP = JType.get(terms[0].toUpperCase());

                    String op = Integer.toBinaryString(OP);
                    op = String.format("%5s", op).replace(' ', '0');

                    String imm11 = Integer.toBinaryString(Integer.parseInt(terms[1]));
                    if (imm11.length() < 11)
                        imm11 = String.format("%5s", imm11).replace(' ', '0');
                    else
                        imm11 = imm11.substring(imm11.length()-11);

                    machineCode = imm11 + op;

                    //table[n][1] = Arrays.toString(terms);
                    //table[n][2] = machineCode;
                }
                else if (terms[0].isBlank() || terms[0].charAt(0) == '#') {
                    //Skip comment;
                }
                if (!machineCode.isEmpty()){
                    String spacer = (instNum == 1) ? "" : " ";
                    String newLine = (instNum == 16) ? "\n" : "";
                    String HexMachineCode = Integer.toHexString(Integer.parseInt(machineCode,2));
                    InstSet.append(spacer).append(String.format("%4s", HexMachineCode).replace(' ', '0')).append(newLine);
                    instNum = (instNum == 16) ? 0 : instNum;
                }
            }
            PrintWriter assembledCode = new PrintWriter(new FileWriter("src\\machine code.txt"));
            assembledCode.print("v2.0 raw\n");
            assembledCode.print(InstSet);
            assembledCode.close();
            //System.out.println(Arrays.deepToString(table));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void constructMaps(){
        RType.put("ADD", new int[]{0, 0});
        RType.put("SUB", new int[]{0, 1});
        RType.put("SLT", new int[]{0, 2});
        RType.put("SLTU", new int[]{0, 3});
        RType.put("XOR", new int[]{1, 0});
        RType.put("OR", new int[]{1, 1});
        RType.put("AND", new int[]{1, 2});
        RType.put("NOR", new int[]{1, 3});
        RType.put("SLL", new int[]{2, 0});
        RType.put("SRL", new int[]{2, 1});
        RType.put("SRA", new int[]{2, 2});
        RType.put("ROR", new int[]{2, 3});
        //
        IType.put("ADDI", 4);
        IType.put("SLTI", 6);
        IType.put("SLTIU", 7);
        IType.put("XORI", 8);
        IType.put("ORI", 9);
        IType.put("ANDI", 10);
        IType.put("NORI", 11);
        IType.put("SLLI", 12);
        IType.put("SRLI", 13);
        IType.put("SRAI", 14);
        IType.put("RORI", 15);
        IType.put("LW", 16);
        IType.put("SW", 17);
        IType.put("BEQ", 18);
        IType.put("BNE", 19);
        IType.put("BLT", 20);
        IType.put("BGE", 21);
        IType.put("JALR", 22);
        //
        JType.put("LUI", 23);
        JType.put("J", 24);
        JType.put("JAL", 25);

    }
}