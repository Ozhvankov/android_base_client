package com.test.test.Models;

import java.io.Serializable;

public class ExtraItemModel implements Serializable {
    public String supplier, client, items, articles, initial, status;

    public ExtraItemModel(String supplier,
            String client,
            String items,
            String articles,
            String initial,
            String status) {

        this.supplier = supplier;
        this.client = client;
        this.items = items;
        this.articles = articles;
        this.initial = initial;
        this.status = status;
    }
}
