package com.test.test.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class EditableItem implements Parcelable {
    /*
    Modify
https://wms2.madrocket.agency/engineapi/2461?module=lpnmodify&
inbound_date=2021-02-05&
Manufacturing_Date=2021-02-05&
Transport_Equipment_Number=312312&
Pallet_Type=1&
Item_inventory_status=2&
cell_id=1272&
Lot_number_batch=test


Inventory
https://wms2.madrocket.agency/engineapi/2461?
kg_current=1000&
box_current=100&
module=lpninventoryadjustment
     */

    public int id;
 /*   int Planned_LPN;
    String item_article;
    String item_group;
    String item_subgroup;
    String item_neighborhood;
    String item_neighborhood_sign;
    String item_neighborhood_value;
    String wrh_zone;
    String shelf_life_days;
    int Implementation_period;
    String shipment_type;
    int Footprint;
    String Supplier;
    String Client;
    int gross_wight;
    String Item_inventory_status;
    int Item_No;
    String Inbound_shipment_number;
    int Pallet_wight;
    int Footprint_max_box_quantity;
    int Gross_weight_of_single_box;
    int Nett_weight_of_single_box;
    int Height_of_a_single_box;
    String Pallet_Type;
    String System_LPN;*/
                                public String Manufacturing_Date;
/*    int Transport_Equipment_Number;
    String WH_Operator_labeling_pallet;
    int Lot_number_batch;
    String Initial_PRINTED_LPN;
    String updated_at;
    int item_id;
    int Planned_number_of_pallets;
    int unit_id;
    int list_id;
    int item_weight;
    int tem_box;*/
public String inbound_date;
    public int kg_current;
  /*  int kg_avaible;
    int kg_reserved;*/
  public int box_current;
   /* int box_avaible;
    int box_reserved;
    int global_status;
    int fact_item_weight;
    int fact_item_box;
    int fact_weight_empty_box;
    int fact_weight_empty_pallet;
    int production_date;
    int number_party;
    int cell_id;
    int plan_item_weight;
    int plan_item_box;
    int lpn_id_partial;
    int lot_id;*/

    public EditableItem(Parcel in) {
        id = in.readInt();
        Manufacturing_Date = in.readString();
        inbound_date = in.readString();
        kg_current = in.readInt();
        box_current = in.readInt();
    }

    public EditableItem() {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(Manufacturing_Date);
        dest.writeString(inbound_date);
        dest.writeInt(kg_current);
        dest.writeInt(box_current);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<EditableItem> CREATOR = new Creator<EditableItem>() {
        @Override
        public EditableItem createFromParcel(Parcel in) {
            return new EditableItem(in);
        }

        @Override
        public EditableItem[] newArray(int size) {
            return new EditableItem[size];
        }
    };
}
