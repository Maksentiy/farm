package com.konon.farm.files;

import java.io.IOException;

public class TextFileThread<T> implements Runnable{

    private String msg;

    T t;

    public TextFileThread(String msg, T t) {
        this.msg = msg;
        this.t = t;
    }

    public void run() {
        TextFileSupport<T> textFileSupport = new TextFileSupport<>();
        try {
            textFileSupport.newEntry(msg, t);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
