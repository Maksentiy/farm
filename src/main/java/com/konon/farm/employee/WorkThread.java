package com.konon.farm.employee;

public class WorkThread implements Runnable{

    private String msg;

    private Work work;

    public WorkThread(String msg, Work work) {
        this.msg = msg;
        this.work = work;
    }

    @Override
    public void run() {
        WorkCRUD workCRUD = new WorkCRUD();
        switch (msg) {
            case "insert" -> {
                workCRUD.insert(work);
            }
            case "update" -> {
                workCRUD.update(work);
            }
            case "delete" -> {
                workCRUD.delete(work.getId());
            }
        }
    }
}
