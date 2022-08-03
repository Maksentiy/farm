package com.konon.farm.product;

public class HarvestThread implements Runnable{

    private String msg;

    private Harvest harvest;

    public HarvestThread(String msg, Harvest harvest) {
        this.msg = msg;
        this.harvest = harvest;
    }

    @Override
    public void run() {
        HarvestCRUD harvestCRUD = new HarvestCRUD();
        switch (msg) {
            case "insert" -> {
                harvestCRUD.insert(harvest);
            }
            case "update" -> {
                harvestCRUD.update(harvest);
            }
            case "delete" -> {
                harvestCRUD.delete(harvest.getId());
            }
        }
    }
}
