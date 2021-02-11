package com.test.test.Models;

public class Cell {
    public int id;
    public String Location;
    public String Area;
    public int         Aisle;
    public int Row;
    public int Section;
    public int Level;
    public int Positon;
    public int Max_Hight_cm;
    public int Max_Width_cm;
    public int Height_from_floor_m;
    public int Max_Weight_kg;
    public String Cell_Type;
    public String WrhZone;
    public int Deep_level;
    public int pallet_type;
    public String Warehouse;

    public Cell(int id,
            String Location,
            String Area,
            int Aisle,
            int Row,
            int Section,
            int Level,
            int Positon,
            int Max_Hight_cm,
            int Max_Width_cm,
            int Height_from_floor_m,
            int Max_Weight_kg,
            String Cell_Type,
            String WrhZone,
            int Deep_level,
            int pallet_type,
            String Warehouse) {
        this.id = id;
        this.Location = Location;
        this.Area = Area;
        this.Aisle = Aisle;
        this.Row = Row;
        this.Section =Section;
        this.Level = Level;
        this.Positon = Positon;
        this.Max_Hight_cm = Max_Hight_cm;
        this.Max_Width_cm = Max_Width_cm;
        this.Height_from_floor_m = Height_from_floor_m;
        this.Max_Weight_kg = Max_Weight_kg;
        this.Cell_Type = Cell_Type;
        this.WrhZone = WrhZone;
        this.Deep_level = Deep_level;
        this.pallet_type = pallet_type;
        this.Warehouse = Warehouse;
    }

}
