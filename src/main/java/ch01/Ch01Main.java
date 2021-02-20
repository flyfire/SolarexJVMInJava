package ch01;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * Created by Solarex at 11:19 AM/2/20/21
 * Desc:
 */
class Frame {
    public int pc; // 程序计数器
    public final Map<Integer, Integer> localVars = new HashMap<>(); //本地变量表
    public final Stack<Integer> operandStack = new Stack<>(); // 操作数栈
}

interface Instruction {
    default int offset() {
        return 1;
    }

    void eval(Frame frame);
}

class IConst1Inst implements Instruction {
    @Override
    public void eval(Frame frame) {
        frame.operandStack.push(1);
        frame.pc += offset();
    }
}

class ILoad0Inst implements Instruction{
    @Override
    public void eval(Frame frame) {
        frame.operandStack.push(frame.localVars.get(0));
        frame.pc += offset();
    }
}

class IStore0Inst implements Instruction {
    @Override
    public void eval(Frame frame) {
        frame.localVars.put(0, frame.operandStack.pop());
        frame.pc += offset();
    }
}

class IReturnInst implements Instruction {
    @Override
    public void eval(Frame frame) {
        System.out.println(frame.operandStack.pop());
        frame.pc += offset();
    }
}

class Interpreter {
    public static void run(Frame frame, Map<Integer, Instruction> instructions) {
        do {
            Instruction instruction = instructions.get(frame.pc);
            instruction.eval(frame);
        } while (instructions.containsKey(frame.pc));
    }
}

public class Ch01Main {
    private static Map<Integer, Instruction> parse(String file) {
        List<String> rawLines;
        try {
            rawLines = Files.readAllLines(Paths.get(file));
        } catch (IOException e) {
            System.out.println("file not found");
            return null;
        }
        if (rawLines.isEmpty()) {
            System.out.println("empty file");
            return null;
        }
        List<String> lines = rawLines.stream()
                .map(String::trim)
                .map(it -> it.replaceAll(": ", " "))
                .map(it -> it.replaceAll(", ", " "))
                .map(it -> it.replaceAll(" +", " "))
                .filter(it -> it.length() > 0)
                .collect(Collectors.toList());

        Map<Integer, Instruction> instructionMap = new HashMap<>();
        for (int i = 0; i < lines.size(); i++) {
            String raw = lines.get(i);
            String[] terms = raw.split(" ");
            int pc = Integer.parseInt(terms[0]);
            String inst = terms[1];
            Instruction instruction = null;
            switch (inst.toLowerCase()) {
                case "iconst_1":
                    instruction = new IConst1Inst();
                    break;
                case "istore_0":
                    instruction = new IStore0Inst();
                    break;
                case "iload_0":
                    instruction = new ILoad0Inst();
                    break;
                case "ireturn":
                    instruction = new IReturnInst();
                    break;
                default:
                    break;
            }

            if (instruction == null) {
                System.out.println("parse file filed, raw: " + raw);
                return null;
            }
            instructionMap.put(pc, instruction);
        }
        return instructionMap;
    }
    public static void main(String[] args) {
        /*
        Map<Integer, Instruction> instructionMap = new HashMap<>();
        instructionMap.put(0, new IConst1Inst());
        instructionMap.put(1, new IStore0Inst());
        instructionMap.put(2, new ILoad0Inst());
        instructionMap.put(3, new IReturnInst());
         */
        Map<Integer, Instruction> instructionMap = parse("test.bc");
        Frame frame = new Frame();
        Interpreter.run(frame, instructionMap);
    }
}
