package com.test.test.Models;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class ItemModel implements Serializable {


    public int  item_weight,
            item_box,
            id, item_id, wrh_zone,
            plan_item_weight,
            plan_item_box, cell_id, fact_item_box, shelf_life_days, Pallet_Type ;
    public String Initial_PRINTED_LPN,
            Inbound_shipment_number,
            item_article,Lot_number_batch, Transport_Equipment_Number,
            footprint_name,
            inventory_status,
            Implementation_period,
            name,
            fact_weight_empty_pallet,
            production_date,
            number_party,
            Manufacturing_Date,
            pallet_name,
            inbound_date;
    public double fact_item_weight, fact_weight_empty_box, netto;

    public ItemModel(int id, String Initial_PRINTED_LPN) {
        this.Initial_PRINTED_LPN = Initial_PRINTED_LPN;
        this.id = id;
    }

    public ItemModel(
            String Initial_PRINTED_LPN,
            String Inbound_shipment_number,
            String item_article,
            String footprint_name,
            String inventory_status,
            String name,
            int shelf_life_days,
            String Implementation_period,
            int item_weight,
            int item_box,
            int id,
            double fact_item_weight,
            int fact_item_box,
            double fact_weight_empty_box,
            String fact_weight_empty_pallet,
            String production_date,
            String number_party,
            int item_id,
            int wrh_zone,
            String Manufacturing_Date,
            String inbound_date,
            String Lot_number_batch,
            String Transport_Equipment_Number,
            int Pallet_Type,
            String pallet_name,
            int plan_item_weight,
            int plan_item_box, int cell_id, double netto) {

        this.Initial_PRINTED_LPN = Initial_PRINTED_LPN;
        this.Inbound_shipment_number = Inbound_shipment_number;
        this.item_article = item_article;
        this.footprint_name = footprint_name;
        this.inventory_status = inventory_status;
        this.name = name;
        this.shelf_life_days = shelf_life_days;
        this.Implementation_period = Implementation_period;
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
        this.Manufacturing_Date = Manufacturing_Date;
        this.inbound_date = inbound_date;
        this.Lot_number_batch = Lot_number_batch;
        this.Transport_Equipment_Number = Transport_Equipment_Number;
        this.Pallet_Type = Pallet_Type;
        this.pallet_name = pallet_name;
        this.plan_item_weight = plan_item_weight;
        this.plan_item_box = plan_item_box;
        this.cell_id = cell_id;
        this.netto = netto;
    }

    @NonNull
    @Override
    public String toString() {
        return Initial_PRINTED_LPN;
    }
}
