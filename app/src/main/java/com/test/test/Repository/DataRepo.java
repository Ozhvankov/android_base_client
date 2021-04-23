package com.test.test.Repository;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.bosphere.filelogger.FL;
import com.fxn.stash.Stash;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import okhttp3.Credentials;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

public class DataRepo {

    public interface onDataListener {
        void returnData(String data);
    }

    public static class getData extends AsyncTask<String, String, String> {

        private final boolean isLogger;
        onDataListener listener;
        private HttpUrl url;
        Request request;
        String cred = Credentials.basic(Stash.getString("email"), Stash.getString("api_key"));


        public getData(onDataListener listener) {
            this.listener = listener;
            isLogger = Stash.getBoolean("logger");
        }

        public void deletePallet(int id) {
            url = HttpUrl.parse(Stash.getString("domain") + "/engineapi")
                    .newBuilder()
                    .addPathSegment(String.valueOf(id))
                    .addQueryParameter("module", "inboundshipment")
                    .build();
            Log.d("asdasd", url.toString());
            request = new Request.Builder()
                    .url(url).delete(new RequestBody() {
                        @Nullable
                        @Override
                        public MediaType contentType() {
                            return null;
                        }

                        @Override
                        public void writeTo(@NotNull BufferedSink bufferedSink) throws IOException {

                        }
                    })
                    .header("User-Agent", System.getProperty("http.agent"))
                    .header("Authorization", Credentials.basic(Stash.getString("email"), Stash.getString("api_key")))
                    .build();

        }

        public void getFootprint(int id) {
            url = HttpUrl.parse(Stash.getString("domain") + "/engineapi")
                    .newBuilder()
                    .addQueryParameter("module", "footprint")
                    .addQueryParameter("search",
                            "Item:equal:" + id)
                    .build();
            Log.d("asdasd", url.toString());
            request = new Request.Builder()
                    .url(url).get()
                    .header("User-Agent", System.getProperty("http.agent"))
                    .header("Authorization", Credentials.basic(Stash.getString("email"), Stash.getString("api_key")))
                    .build();

        }

        public void setPalleteInCell(int list_id, int pallete_id, int cell_id) {
            // https://wms2.madrocket.agency/engineapi/53?module=inboundshipmentlist&action=putaway&data[2461][cell_id]=1271
            url = HttpUrl.parse(Stash.getString("domain") + "/engineapi")
                    .newBuilder()
                    .addPathSegment(String.valueOf(list_id))
                    .addQueryParameter("module", "inboundshipmentlist")
                    .addQueryParameter("action",
                            "putaway")
                    .addQueryParameter("data[" + pallete_id + "][cell_id]",
                            String.valueOf(cell_id))
                    .build();
            Log.d("asdasd", url.toString());
            request = new Request.Builder()
                    .url(url).put(new RequestBody() {
                        @Nullable
                        @Override
                        public MediaType contentType() {
                            return null;
                        }

                        @Override
                        public void writeTo(@NotNull BufferedSink bufferedSink) throws IOException {

                        }
                    })
                    .header("User-Agent", System.getProperty("http.agent"))
                    .header("Authorization", Credentials.basic(Stash.getString("email"), Stash.getString("api_key")))
                    .build();
        }

        public void getPalletType() {
            url = HttpUrl.parse(Stash.getString("domain") + "/engineapi")
                    .newBuilder()
                    .addQueryParameter("module", "locationspallettype")
                    .build();
            Log.d("asdasd", url.toString());
            request = new Request.Builder()
                    .url(url).get()
                    .header("User-Agent", System.getProperty("http.agent"))
                    .header("Authorization", Credentials.basic(Stash.getString("email"), Stash.getString("api_key")))
                    .build();

        }
        public void getItemInventoryStatus() {
            url = HttpUrl.parse(Stash.getString("domain") + "/engineapi")
                    .newBuilder()
                    .addQueryParameter("module", "iteminventorystatus")
                    .build();
            Log.d("asdasd", url.toString());
            request = new Request.Builder()
                    .url(url).get()
                    .header("User-Agent", System.getProperty("http.agent"))
                    .header("Authorization", Credentials.basic(Stash.getString("email"), Stash.getString("api_key")))
                    .build();
        }

        public void getItemShipmentUnitType() {
            url = HttpUrl.parse(Stash.getString("domain") + "/engineapi")
                    .newBuilder()
                    .addQueryParameter("module", "itemshipmentunittype")
                    .build();
            Log.d("asdasd", url.toString());
            request = new Request.Builder()
                    .url(url).get()
                    .header("User-Agent", System.getProperty("http.agent"))
                    .header("Authorization", Credentials.basic(Stash.getString("email"), Stash.getString("api_key")))
                    .build();

        }

        public void getWrZone(int id){
            url = HttpUrl.parse(Stash.getString("domain") + "/engineapi")
                    .newBuilder()
                    .addPathSegment(String.valueOf(id))
                    .addQueryParameter("module", "items")
                    .build();
            Log.d("asdasd", url.toString());
            request = new Request.Builder()
                    .url(url).get()
                    .header("User-Agent", System.getProperty("http.agent"))
                    .header("Authorization", Credentials.basic(Stash.getString("email"), Stash.getString("api_key")))
                    .build();
        }


        public void getCells(int Status,  int WrhZone, int Warehouse, int Cell_Type, int pallet_type, int Max_Weight_kg) {
            //https://wms2.madrocket.agency/engineapi?module=locationscells&search=Status:equal:1|WrhZone:equal:1|Warehouse:equal:1|Cell_Type:equal:3|pallet_type:equal:1|Max_Weight_kg:bigger_equal:5000
            String s = String.format("Status:equal:%s|WrhZone:equal:%s|Warehouse:equal:%s|Cell_Type:equal:%s|pallet_type:equal:%s|Max_Weight_kg:bigger_equal:%s",
                    String.valueOf(Status),
                    String.valueOf(WrhZone),
                    String.valueOf(Warehouse),
                    String.valueOf(Cell_Type),
                    String.valueOf(pallet_type),
                    String.valueOf(Max_Weight_kg));

            url = HttpUrl.parse(
                    Stash.getString("domain") + "/engineapi")
                    .newBuilder()
                    .addQueryParameter("module", "locationscells")
                    .addQueryParameter("search", s)
                    .build();

            request = new Request.Builder()
                    .url(url)
                    .get()
                    .header("User-Agent", System.getProperty("http.agent"))
                    .header("Authorization", Credentials.basic(Stash.getString("email"), Stash.getString("api_key")))
                    .build();
        }

        public void getCells(int Status, int WrhZone, int Cell_Type, int Warehouse, String Location, int Max_Weight_kg) {

            //https://wms2.madrocket.agency/engineapi?module=locationscells&search=Status:equal:1|WrhZone:equal:1|Cell_Type:equal:2|Warehouse:equal:1|Location:like:07|&limit=all
            String s = String.format("Status:equal:%s|WrhZone:equal:%s|Cell_Type:equal:%s|Warehouse:equal:%s|Location:like:%s|Max_Weight_kg:bigger_equal:%s", String.valueOf(Status), String.valueOf(WrhZone), String.valueOf(Cell_Type), String.valueOf(Warehouse), Location.toUpperCase(), String.valueOf(Max_Weight_kg));

            url = HttpUrl.parse(Stash.getString("domain") + "/engineapi")
                    .newBuilder()
                    .addQueryParameter("module", "locationscells")
                    //.addEncodedQueryParameter("search", s)
                    .addQueryParameter("search", s)
                    .addQueryParameter("limit", "all")
                    .build();
            // https://wms2.madrocket.agency/engineapi?module=locationscells&search=Status%3Aequal%3A1%7CWrhZone%3Aequal%3A1%7CCell_Type%3Aequal%3A2%7CWarehouse%3Aequal%3A0%7CLocation%3Alike%3A07%7C&limit=all

            //https://wms2.madrocket.agency/engineapi?module=locationscells&search=Status:equal:1%7CWrhZone:equal:1%7CCell_Type:equal:2%7CWarehouse:equal:1%7CLocation:like:07%7C&limit=all
            // https://wms2.madrocket.agency/engineapi?module=locationscells&search=Status:equal:1|WrhZone:equal:1|Cell_Type:equal:2|Warehouse:equal:1|Location:like:07|&limit=all

            // https://wms2.madrocket.agency/engineapi?module=locationscells&search=Status:equal:1|WrhZone:equal:1|Cell_Type:equal:2|Warehouse:equal:0|Location:like:07|&limit=all
            Log.d("asdasd", url.toString());
            request = new Request.Builder()
                    .url(url).get()
                    .header("User-Agent", System.getProperty("http.agent"))
                    .header("Authorization", Credentials.basic(Stash.getString("email"), Stash.getString("api_key")))
                    .build();

        }

        public void addPallete(String item_id, String footprint_id, String unit_id, String gross, String pallet_wight
                , String packaging_wight, String list_id, String lot_id) {
            url = HttpUrl.parse(Stash.getString("domain") + "/engineapi")
                    .newBuilder()
                    .addQueryParameter("module", "inboundshipment")
                    .addQueryParameter("item_id", item_id)
                    .addQueryParameter("footprint_id", footprint_id)
                    .addQueryParameter("unit_id", unit_id)
                    .addQueryParameter("gross", gross)
                    .addQueryParameter("pallet_wight", pallet_wight)
                    .addQueryParameter("packaging_wight", packaging_wight)
                    .addQueryParameter("list_id", list_id)
                    .addQueryParameter("lot_id", lot_id)
                    .build();
            Log.d("asdasd", url.toString());
            request = new Request.Builder()
                    .url(url).post(new RequestBody() {
                        @Nullable
                        @Override
                        public MediaType contentType() {
                            return null;
                        }

                        @Override
                        public void writeTo(@NotNull BufferedSink bufferedSink) throws IOException {

                        }
                    })
                    .header("User-Agent", System.getProperty("http.agent"))
                    .header("Authorization", Credentials.basic(Stash.getString("email"), Stash.getString("api_key")))
                    .build();

        }

        public void modifyPallete(int id,
                                  String inbound_date,
                                  String Manufacturing_Date,
                                  String Transport_Equipment_Number,
                                  int Pallet_Type,
                                  int Item_inventory_status,
                                  String Lot_number_batch,
                                  int cell_id) {
            //https://wms2.madrocket.agency/engineapi/2461?module=lpnmodify&inbound_date=2021-02-05&Manufacturing_Date=2021-02-05&Transport_Equipment_Number=312312&Pallet_Type=1&Item_inventory_status=2&cell_id=1272&Lot_number_batch=test
            url = HttpUrl.parse(Stash.getString("domain") + "/engineapi")
                    .newBuilder()
                    .addPathSegment(String.valueOf(id))
                    .addQueryParameter("module", "lpnmodify")
                    .addQueryParameter("inbound_date", inbound_date)
                    .addQueryParameter("Manufacturing_Date", Manufacturing_Date)
                    .addQueryParameter("Transport_Equipment_Number", Transport_Equipment_Number)
                    .addQueryParameter("Pallet_Type", String.valueOf(Pallet_Type))
                    .addQueryParameter("Item_inventory_status", String.valueOf(Item_inventory_status))
                    .addQueryParameter("Lot_number_batch", Lot_number_batch)
                    .addQueryParameter("cell_id", String.valueOf(cell_id))
                    .build();
            Log.d("asdasd", url.toString());
            request = new Request.Builder()
                    .url(url).put(new RequestBody() {
                        @Nullable
                        @Override
                        public MediaType contentType() {
                            return null;
                        }

                        @Override
                        public void writeTo(@NotNull BufferedSink bufferedSink) throws IOException {

                        }
                    })
                    .header("User-Agent", System.getProperty("http.agent"))
                    .header("Authorization", Credentials.basic(Stash.getString("email"), Stash.getString("api_key")))
                    .build();

        }

//https://wms2.madrocket.agency/engineapi/2461?kg_current=1000&box_current=100&module=lpninventoryadjustment
        public void inventaryPallete(int id,
                          int kg_current,
                          int box_current) {
            url = HttpUrl.parse(Stash.getString("domain") + "/engineapi")
                    .newBuilder()
                    .addPathSegment(String.valueOf(id))
                    .addQueryParameter("module", "lpninventoryadjustment")
                    .addQueryParameter("kg_current", String.valueOf(kg_current))
                    .addQueryParameter("box_current", String.valueOf(box_current))
                    .build();
            Log.d("asdasd", url.toString());
            request = new Request.Builder()
                    .url(url).put(new RequestBody() {
                        @Nullable
                        @Override
                        public MediaType contentType() {
                            return null;
                        }

                        @Override
                        public void writeTo(@NotNull BufferedSink bufferedSink) throws IOException {

                        }
                    })
                    .header("User-Agent", System.getProperty("http.agent"))
                    .header("Authorization", Credentials.basic(Stash.getString("email"), Stash.getString("api_key")))
                    .build();
        }

        public void setWarehouse(String warehouse, String user_id) {
            url = HttpUrl.parse(Stash.getString("domain") + "/engineapi")
                    .newBuilder()
                    .addPathSegment(user_id)
                    .addQueryParameter("module", "profile")
                    .addQueryParameter("warehouse", warehouse)
                    .build();
            Log.d("asdasd", url.toString());
            request = new Request.Builder()
                    .url(url)
                    .put(new RequestBody() {
                        @Nullable
                        @Override
                        public MediaType contentType() {
                            return null;
                        }

                        @Override
                        public void writeTo(@NotNull BufferedSink bufferedSink) throws IOException {

                        }
                    })
                    .header("User-Agent", System.getProperty("http.agent"))
                    .header("Authorization", Credentials.basic(Stash.getString("email"), Stash.getString("api_key")))
                    .build();
        }

        public void auth(String username, String pass) {
            url = HttpUrl.parse(Stash.getString("domain") + "/engineapi")
                    .newBuilder()
                    .addQueryParameter("module", "profile")
                    .addQueryParameter("search",
                            "email:equal:" + username + "|" + "password:equal:" + pass)
                    .build();

            request = new Request.Builder()
                    .url(url)
                    .get()
                    .header("Authorization", Credentials.basic(username, "94FdL2-pnZTY-ynmgjr-89jA8"))
                    .header("User-Agent", System.getProperty("http.agent"))
                    .build();
        }

        public void saveStagingTask(String id, String cellId, String lpnId, String lpnIdPartial) {
            url = HttpUrl.parse(Stash.getString("domain") + "/engineapi")
                    .newBuilder()
                    .addPathSegment(id)
                    .addQueryParameter("module", "outbounditemtasks")
                    .addQueryParameter("task_type_id", "4")
                    .addQueryParameter("data[cell_id]", cellId)
                    .addQueryParameter("lpn_id", lpnId)
                    .addQueryParameter("lpn_id_partial", lpnIdPartial)
                    .build();

            request = new Request.Builder()
                    .url(url)
                    .put(new RequestBody() {
                        @Nullable
                        @Override
                        public MediaType contentType() {
                            return null;
                        }

                        @Override
                        public void writeTo(@NotNull BufferedSink bufferedSink) throws IOException {

                        }
                    })
                    .header("User-Agent", System.getProperty("http.agent"))
                    .header("Authorization", Credentials.basic(Stash.getString("email"), Stash.getString("api_key")))
                    .build();
        }

        public void saveRefillTask(String id, String cellId, String lpnId) {
            url = HttpUrl.parse(Stash.getString("domain") + "/engineapi")
                    .newBuilder()
                    .addPathSegment(id)
                    .addQueryParameter("module", "outbounditemtasks")
                    .addQueryParameter("task_type_id", "1")
                    .addQueryParameter("data[cell_id]", cellId)
                    .addQueryParameter("lpn_id", lpnId)
                    .build();

            request = new Request.Builder()
                    .url(url)
                    .put(new RequestBody() {
                        @Nullable
                        @Override
                        public MediaType contentType() {
                            return null;
                        }

                        @Override
                        public void writeTo(@NotNull BufferedSink bufferedSink) throws IOException {

                        }
                    })
                    .header("User-Agent", System.getProperty("http.agent"))
                    .header("Authorization", Credentials.basic(Stash.getString("email"), Stash.getString("api_key")))
                    .build();
        }

        public void saveReturnTask(String id, String cellId, String lpnId) {
            url = HttpUrl.parse(Stash.getString("domain") + "/engineapi")
                    .newBuilder()
                    .addPathSegment(id)
                    .addQueryParameter("module", "outbounditemtasks")
                    .addQueryParameter("task_type_id", "3")
                    .addQueryParameter("data[cell_id]", cellId)
                    .addQueryParameter("lpn_id", lpnId)
                    .build();

            request = new Request.Builder()
                    .url(url)
                    .put(new RequestBody() {
                        @Nullable
                        @Override
                        public MediaType contentType() {
                            return null;
                        }

                        @Override
                        public void writeTo(@NotNull BufferedSink bufferedSink) throws IOException {

                        }
                    })
                    .header("User-Agent", System.getProperty("http.agent"))
                    .header("Authorization", Credentials.basic(Stash.getString("email"), Stash.getString("api_key")))
                    .build();
        }

        public void savePartialTask(String id, String cellId, String lpnId, String lpnIdPartial) {
            url = HttpUrl.parse(Stash.getString("domain") + "/engineapi")
                    .newBuilder()
                    .addPathSegment(id)
                    .addQueryParameter("module", "outbounditemtasks")
                    .addQueryParameter("task_type_id", "2")
                    .addQueryParameter("data[cell_id]", cellId)
                    .addQueryParameter("lpn_id", lpnId)
                    .addQueryParameter("data[lpn_id_partial]", lpnIdPartial)
                    .build();

            request = new Request.Builder()
                    .url(url)
                    .put(new RequestBody() {
                        @Nullable
                        @Override
                        public MediaType contentType() {
                            return null;
                        }

                        @Override
                        public void writeTo(@NotNull BufferedSink bufferedSink) throws IOException {

                        }
                    })
                    .header("User-Agent", System.getProperty("http.agent"))
                    .header("Authorization", Credentials.basic(Stash.getString("email"), Stash.getString("api_key")))
                    .build();
        }

        public void getLpnList() {
            //https://wms2.madrocket.agency/engineapi?module=lpnlist&search=type_id:equal:1|used:is_null&limit=10
            //https://wms2.madrocket.agency/engineapi?module=lpnlist&search=type_id:equal:1|used:is_null&limit=10

            url = HttpUrl.parse(Stash.getString("domain")
                    + "/engineapi")
                    .newBuilder()
                    .addQueryParameter("module", "lpnlist")
                    .addQueryParameter("search", "type_id:equal:1|used:is_null")
                    .addQueryParameter("limit", "10")
                    .build();

            request = new Request.Builder()
                    .url(url)
                    .get()
                    .header("User-Agent", System.getProperty("http.agent"))
                    .header("Authorization", Credentials.basic(Stash.getString("email"), Stash.getString("api_key")))
                    .build();
        }

        public void getTask(String id) {
            url = HttpUrl.parse(Stash.getString("domain") + "/engineapi")
                    .newBuilder()
                    .addPathSegment(id)
                    .addQueryParameter("module", "outbounditemtasks")
                    .build();

            request = new Request.Builder()
                    .url(url)
                    .get()
                    .header("User-Agent", System.getProperty("http.agent"))
                    .header("Authorization", cred)
                    .build();
        }

        public void getOutboundData(int id, String filter) {
            url = HttpUrl.parse(Stash.getString("domain") + "/engineapi")
                    .newBuilder()
                    .addPathSegment(String.valueOf(id))
                    .addQueryParameter("module", "outboundshipment")
                    .addQueryParameter("filter", filter)
                    .build();

            request = new Request.Builder()
                    .url(url)
                    .get()
                    .header("User-Agent", System.getProperty("http.agent"))
                    .header("Authorization", Credentials.basic(Stash.getString("email"), Stash.getString("api_key")))
                    .build();
        }

        public void getOutbound(int id) {
            url = HttpUrl.parse(Stash.getString("domain") + "/engineapi")
                    .newBuilder()
                    .addPathSegment(String.valueOf(id))
                    .addQueryParameter("module", "outboundshipment")
                    .build();

            request = new Request.Builder()
                    .url(url)
                    .get()
                    .header("User-Agent", System.getProperty("http.agent"))
                    .header("Authorization", Credentials.basic(Stash.getString("email"), Stash.getString("api_key")))
                    .build();
        }

        public void getOutboundList() {
            url = HttpUrl.parse(Stash.getString("domain") + "/engineapi")
                    .newBuilder().addQueryParameter("module", "outboundshipment")
                    .addQueryParameter("search", "status_id:equal:1")
                    .build();
            request = new Request.Builder()
                    .url(url)
                    .get()
                    .header("User-Agent", System.getProperty("http.agent"))
                    .header("Authorization", cred)
                    .build();
        }

        public void setInventory(int id, HashMap<String, String> hashMap) {
            url = HttpUrl.parse(Stash.getString("domain") + "/frameworkapi")
                    .newBuilder()
                    .addPathSegment(String.valueOf(id))
                    .addQueryParameter("module", "lpninventoryadjustment")
                    .build();
            FormBody.Builder requestBody = new FormBody.Builder();
            for (Map.Entry<String, String> entry : hashMap.entrySet()) {
                requestBody.add(entry.getKey(), entry.getValue());
            }

            request = new Request.Builder()
                    .url(url)
                    .header("User-Agent", System.getProperty("http.agent"))
                    .header("Authorization", cred)
                    .put(requestBody.build())
                    .build();
        }

        public void setModify(int id, HashMap<String, String> hashMap) {
            HttpUrl.Builder h = HttpUrl.parse(Stash.getString("domain") + "/engineapi")
                    .newBuilder()
                    .addPathSegment(String.valueOf(id))
                    .addQueryParameter("module", "inboundshipmentlist")
                    .addQueryParameter("action", "fact");
            for (Map.Entry<String, String> entry : hashMap.entrySet()) {
                h.addQueryParameter(entry.getKey(), entry.getValue());
            }

            url =h.build();

            request = new Request.Builder()
                    .url(url)
                    .header("User-Agent", System.getProperty("http.agent"))
                    .header("Authorization", cred)
                    .put(new RequestBody() {
                        @Nullable
                        @Override
                        public MediaType contentType() {
                            return null;
                        }

                        @Override
                        public void writeTo(@NotNull BufferedSink bufferedSink) throws IOException {

                        }
                    })
                    .build();
        }

        public void getStockLpnList() {
            url = HttpUrl.parse(Stash.getString("domain") + "/engineapi")
                    .newBuilder()
                    .addQueryParameter("module", "inboundshipmentnewlist")
                    .addQueryParameter("limit", String.valueOf(Integer.MAX_VALUE))
                    .build();
            request = new Request.Builder()
                    .url(url)
                    .header("User-Agent", System.getProperty("http.agent"))
                    .header("Authorization", cred)
                    .get()
                    .build();
        }

        public void getStock(String lpn) {
            url = HttpUrl.parse(Stash.getString("domain") +
                    "/engineapi/?module=inboundshipmentnew&search=Initial_PRINTED_LPN:equal:"
                    + lpn + "|cell_id:bigger:0")
                    .newBuilder()
                    .build();
            request = new Request.Builder()
                    .url(url)
                    .header("User-Agent", System.getProperty("http.agent"))
                    //.header("Authorization", "Basic
                    // c3VwZXJhZG1pbkBtYWRyb2NrZXQuc3R1ZGlvOnVLZXBwci1XdUdMVC1TUjJ0RWUtZFdQYlU=")
                    .get()
                    .build();
        }

        public void getList() {
            url = HttpUrl.parse(Stash.getString("domain") + "/engineapi")
                    .newBuilder()
                    .addQueryParameter("module", "inboundshipmentlist")
                    .build();

            request = new Request.Builder()
                    .url(url)
                    .get()
                    .header("User-Agent", System.getProperty("http.agent"))
                    .header("Authorization", cred)
                    .build();
        }



        public void getListById(String list_id) {
            url = HttpUrl.parse(Stash.getString("domain") + "/engineapi")
                    .newBuilder()
                    .addPathSegment(list_id)
                    .addQueryParameter("module", "inboundshipmentlist")
                    .build();

            request = new Request.Builder()
                    .url(url)
                    .get()
                    .header("User-Agent", System.getProperty("http.agent"))
                    .header("Authorization", cred)
                    .build();
        }

        public void getListByStatus(int status_id, int page, int pageLimit) {
            url = HttpUrl.parse(Stash.getString("domain") + "/engineapi")
                    .newBuilder()
                    .addQueryParameter("module", "inboundshipmentlist")
                    .addQueryParameter("search", "status_id:equal:" + status_id)
                    .addQueryParameter("page", String.valueOf(page))
                    .addQueryParameter("limit", String.valueOf(pageLimit))
                    .build();

            request = new Request.Builder()
                    .url(url)
                    .get()
                    .header("User-Agent", System.getProperty("http.agent"))
                    .header("Authorization", cred)
                    .build();
        }

        public void getList(int page) {
            url = HttpUrl.parse(Stash.getString("domain") + "/engineapi")
                    .newBuilder()
                    .addQueryParameter("module", "inboundshipmentlist")
                    .addQueryParameter("page", String.valueOf(page))
                    .build();

            request = new Request.Builder()
                    .url(url)
                    .get()
                    .header("User-Agent", System.getProperty("http.agent"))
                    .header("Authorization", cred)
                    .build();
        }

        public void getItem(int id) {
            url = HttpUrl.parse(Stash.getString("domain") + "/engineapi")
                    .newBuilder()
                    .addPathSegment(String.valueOf(id))
                    .addQueryParameter("module", "inboundshipment")
                    .build();

            request = new Request.Builder()
                    .url(url)
                    .get()
                    .header("User-Agent", System.getProperty("http.agent"))
                    .header("Authorization", cred)
                    .build();
        }

        public void updateItem(HashMap<String, String> hashMap, String list_id, String action) {
            url = HttpUrl.parse(Stash.getString("domain") + "/frameworkapi")
                    .newBuilder()
                    .addPathSegment(String.valueOf(list_id))
                    .addQueryParameter("module", "inboundshipmentlist")
                    .build();
            FormBody.Builder requestBody = new FormBody.Builder()
                    .add("action", action);
            for (Map.Entry<String, String> entry : hashMap.entrySet()) {
                requestBody.add(entry.getKey(), entry.getValue());
            }

            request = new Request.Builder()
                    .url(url)
                    .header("User-Agent", System.getProperty("http.agent"))
                    .header("Authorization", cred)
                    .put(requestBody.build())
                    .build();
        }

        public void start() {
            this.execute();
        }

        public void release() {
            if (this.getStatus() == Status.RUNNING) {
                this.cancel(true);
            }
        }


        @Override
        protected String doInBackground(String... strings) {

            OkHttpClient client = new OkHttpClient();

            try {
                if(isLogger) {
                    FL.d(request.toString());
                }
                Response response = client.newCall(request).execute();
                String r  =response.body().string();
                if(isLogger) {
                    FL.d(r);
                }
                return r;
            } catch (IOException e) {
                if(isLogger) {
                    FL.d(e.toString());
                }
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s == null) {
                listener.returnData("");
            } else {
                listener.returnData(s);
            }
        }
    }
}
