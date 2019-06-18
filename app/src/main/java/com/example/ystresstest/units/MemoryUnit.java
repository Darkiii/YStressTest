package com.example.ystresstest.units;

import com.example.ystresstest.utils.SuCommand;

public class MemoryUnit {
    private int memorySize;
    private int times;

    public MemoryUnit(int size, int times) {
        this.memorySize = size;
        this.times = times;
    }

    public void startTest() {
        SuCommand command = new SuCommand();
        command.execRootCmdSilent("memtester " + memorySize + "M " + times);
    }
}
