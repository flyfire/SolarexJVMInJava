package ch01;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

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
    public static void main(String[] args) {
        Map<Integer, Instruction> instructionMap = new HashMap<>();
        instructionMap.put(0, new IConst1Inst());
        instructionMap.put(1, new IStore0Inst());
        instructionMap.put(2, new ILoad0Inst());
        instructionMap.put(3, new IReturnInst());

        Frame frame = new Frame();
        Interpreter.run(frame, instructionMap);
    }
}
