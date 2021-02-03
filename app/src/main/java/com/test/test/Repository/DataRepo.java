package com.test.test.Repository;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

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

        onDataListener listener;
        private HttpUrl url;
        Request request;
        String cred = Credentials.basic(Stash.getString("email"), Stash.getString("api_key"));


        public getData(onDataListener listener) {
            this.listener = listener;
        }

        public getData(onDataListener listener, String query, int page) {
            this.listener = listener;
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

        public void saveStagingTask(String id, String taskType, String cellId, String lpnId, String lpnIdPartial) {
            url = HttpUrl.parse(Stash.getString("domain") + "/frameworkapi")
                    .newBuilder()
                    .addPathSegment(id)
                    .addQueryParameter("module", "outbounditemtasks")
                    .addQueryParameter("task_type_id", taskType)
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
                    .header("Authorization", cred)
                    .build();
        }

        public void saveTask(String id, String taskType, String cellId, String lpnId) {
            url = HttpUrl.parse(Stash.getString("domain") + "/frameworkapi")
                    .newBuilder()
                    .addPathSegment(id)
                    .addQueryParameter("module", "outbounditemtasks")
                    .addQueryParameter("task_type_id", taskType)
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
                    .header("Authorization", cred)
                    .build();
        }

        public void saveTask(String id, String taskType, String cellId, String lpnId, String lpnIdPartial) {
            url = HttpUrl.parse(Stash.getString("domain") + "/frameworkapi")
                    .newBuilder()
                    .addPathSegment(id)
                    .addQueryParameter("module", "outbounditemtasks")
                    .addQueryParameter("task_type_id", taskType)
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
                    .header("Authorization", cred)
                    .build();
        }

        public void getLpnList() {
            url = HttpUrl.parse(Stash.getString("domain")
                    + "/frameworkapi/?module=inboundshipmentnewlist&search=item_id:equal:1149%7Ccell_id:equal:0")
                    .newBuilder()
                    .build();

            request = new Request.Builder()
                    .url(url)
                    .get()
                    .header("User-Agent", System.getProperty("http.agent"))
                    .header("Authorization", cred)
                    .build();
        }

        public void getTask(String id) {
            url = HttpUrl.parse(Stash.getString("domain") + "/frameworkapi")
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

        public void getOutboundData(String id, String filter) {
            url = HttpUrl.parse(Stash.getString("domain") + "/frameworkapi")
                    .newBuilder()
                    .addPathSegment(id)
                    .addQueryParameter("module", "outboundshipment")
                    .addQueryParameter("filter", filter)
                    .build();

            request = new Request.Builder()
                    .url(url)
                    .get()
                    .header("User-Agent", System.getProperty("http.agent"))
                    .header("Authorization", cred)
                    .build();
        }

        public void getOutbound(String id) {
            url = HttpUrl.parse(Stash.getString("domain") + "/frameworkapi")
                    .newBuilder()
                    .addPathSegment(id)
                    .addQueryParameter("module", "outboundshipment")
                    .build();

            request = new Request.Builder()
                    .url(url)
                    .get()
                    .header("User-Agent", System.getProperty("http.agent"))
                    .header("Authorization", cred)
                    .build();
        }

        public void getOutboundList() {
            url = HttpUrl.parse(Stash.getString("domain") + "/frameworkapi/?module=outboundshipment")
                    .newBuilder()
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
            url = HttpUrl.parse(Stash.getString("domain") + "/frameworkapi")
                    .newBuilder()
                    .addPathSegment(String.valueOf(id))
                    .addQueryParameter("module", "lpnmodify")
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

        public void getStockLpnList() {
            url = HttpUrl.parse(Stash.getString("domain") + "/frameworkapi")
                    .newBuilder()
                    .addQueryParameter("module", "inboundshipmentnewlist")
                    .build();
            request = new Request.Builder()
                    .url(url)
                    .header("User-Agent", System.getProperty("http.agent"))
                    .header("Authorization",
                            "Basic c3VwZXJhZG1pbkBtYWRyb2NrZXQuc3R1ZGlvOnVLZXBwci1XdUdMVC1TUjJ0RWUtZFdQYlU=")
                    .get()
                    .build();
        }

        public void getStock(String lpn) {
            url = HttpUrl.parse(Stash.getString("domain") +
                    "/frameworkapi/?module=inboundshipmentnew&search=Initial_PRINTED_LPN:equal:"
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

        public void getCellsList(String wrh) {
            url = HttpUrl.parse(
                    Stash.getString("domain") + "/frameworkapi?module=selectCell&search=Status:equal:1|WrhZone:equal:"
                            + wrh)
                    .newBuilder()
                    //.addQueryParameter("module", "selectCell&search=Status:equal:2|WrhZone:equal:" + wrh)
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

        public void getListByStatus(int status_id) {
            url = HttpUrl.parse(Stash.getString("domain") + "/engineapi")
                    .newBuilder()
                    .addQueryParameter("module", "inboundshipmentlist")
                    .addQueryParameter("search", "status_id:equal:" + status_id)
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
            url = HttpUrl.parse(Stash.getString("domain") + "/frameworkapi")
                    .newBuilder()
                    .addPathSegment(String.valueOf(id))
                    .addQueryParameter("module", "inboundshipmentlist")
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
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s == null) {
                listener.returnData("");
            } else {
                Log.d("asd", s);
                listener.returnData(s);
            }
        }
    }
}
