package com.konon.farm.product;

public class ProductThread implements Runnable{

    private String msg;

    private Product product;

    public ProductThread(String msg, Product product) {
        this.msg = msg;
        this.product = product;
    }

    @Override
    public void run() {
        ProductCRUD productCRUD = new ProductCRUD();
        switch (msg) {
            case "insert" -> {
                productCRUD.insert(product);
            }
            case "update" -> {
                productCRUD.update(product);
            }
            case "delete" -> {
                productCRUD.delete(product.getId());
            }
        }
    }
}
