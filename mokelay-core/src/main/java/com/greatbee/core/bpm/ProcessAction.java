package com.greatbee.core.bpm;

public enum ProcessAction {
    //同意
    Agree("agree"),
    //拒绝
    Refuse("refuse"),
    //转签
    Transfer("transfer"),
    //打回
    Repulse("repulse");
    private String action;

    ProcessAction(String action) {
        this.action = action;
    }
}