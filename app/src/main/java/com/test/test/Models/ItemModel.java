package com.test.test.Models;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class ItemModel implements Serializable {
    public String initial_PRINTED_LPN,
            Inbound_shipment_number,
            item_article,
            footprint_name,
            inventory_status,
            name,
            shelf_life_days,
            implementation_period,
            item_weight,
            item_box,
            id,
            fact_item_weight,
            fact_item_box,
            fact_weight_empty_box,
            fact_weight_empty_pallet,
            production_date,
            number_party,
            item_id,
            wrh_zone,
            manufacturing_Date,
            inbound_date,
            lot_number_batch,
            transport_Equipment_Number,
            pallet_Type,
            pallet_name,
            plan_item_weight,
            plan_item_box;

    public ItemModel(
            String Initial_PRINTED_LPN,
            String Inbound_shipment_number,
            String item_article,
            String footprint_name,
            String inventory_status,
            String name,
            String shelf_life_days,
            String Implementation_period,
            String item_weight,
            String item_box,
            String id,
            String fact_item_weight,
            String fact_item_box,
            String fact_weight_empty_box,
            String fact_weight_empty_pallet,
            String production_date,
            String number_party,
            String item_id,
            String wrh_zone,
            String manufacturing_Date,
            String inbound_date,
            String lot_number_batch,
            String transport_Equipment_Number,
            String pallet_Type,
            String pallet_name,
            String plan_item_weight,
            String plan_item_box) {

        this.initial_PRINTED_LPN = Initial_PRINTED_LPN;
        this.Inbound_shipment_number = Inbound_shipment_number;
        this.item_article = item_article;
        this.footprint_name = footprint_name;
        this.inventory_status = inventory_status;
        this.name = name;
        this.shelf_life_days = shelf_life_days;
        this.implementation_period = Implementation_period;
        this.item_weight = item_weight;
        this.item_box = item_box;
        this.id = id;
        this.fact_item_weight = fact_item_weight;
        this.fact_item_box = fact_item_box;
        this.fact_weight_empty_box = fact_weight_empty_box;
        this.fact_weight_empty_pallet = fact_weight_empty_pallet;
        this.production_date = production_date;
        this.number_party = number_party;
        this.item_id = item_id;
        this.wrh_zone = wrh_zone;
        this.manufacturing_Date = manufacturing_Date;
        this.inbound_date = inbound_date;
        this.lot_number_batch = lot_number_batch;
        this.transport_Equipment_Number = transport_Equipment_Number;
        this.pallet_Type = pallet_Type;
        this.pallet_name = pallet_name;
        this.plan_item_weight = plan_item_weight;
        this.plan_item_box = plan_item_box;
    }

    @NonNull
    @Override
    public String toString() {
        return initial_PRINTED_LPN;
    }
}
