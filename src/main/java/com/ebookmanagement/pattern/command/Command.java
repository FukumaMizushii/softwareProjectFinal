package com.ebookmanagement.pattern.command;


public interface Command {


    void execute();


    void undo();


    String describe();
}
