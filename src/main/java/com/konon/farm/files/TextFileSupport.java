package com.konon.farm.files;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TextFileSupport<T> {

    public static File findFile() throws IOException {
        Calendar instance = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String date = formatter.format(instance.getTime()) + ".txt";
        String fileName = "D:\\java-stuff\\Last-one\\farm\\logs\\" + date;
        File log = new File(fileName);
        boolean newFile = log.createNewFile();
        if (newFile) {
            System.out.println();
        }
        return log;
    }

    public  void newEntry(String msg, T t) throws IOException{
        File log = findFile();
        Calendar instance = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String date = formatter.format(instance.getTime());
        String obj = t.toString();
        FileWriter writer = new FileWriter(log, true);
        writer.write(date + " " + msg + " " + obj + "\n");
        writer.flush();
        writer.close();
    }

    public static List<String> getAllLogFilesName() throws IOException{
        File directory = new File("D:\\java-stuff\\Last-one\\farm\\logs\\");
        File[] files = directory.listFiles();
        if (files == null) {
            return null;
        }
        List<String> filesName = new ArrayList<>();
        for (File file:files) {
            filesName.add(file.getName());
        }
        return filesName;
    }
}
