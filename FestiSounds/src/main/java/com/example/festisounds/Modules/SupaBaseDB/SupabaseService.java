package com.example.festisounds.Modules.SupaBaseDB;

import com.example.festisounds.Modules.Festival.Entities.Festival;
import com.example.festisounds.Modules.Festival.Repository.FestivalRepo;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;


@Service
public class SupabaseService {
    private static final String SUPABASE_URL = System.getenv("supabaseUrl");
    private static final String SUPABASE_KEY = System.getenv("supabaseKey");

    public CloseableHttpClient createHttpClient() {
        return HttpClients.createDefault();
    }

    FestivalRepo festivalRepo;

    public SupabaseService(FestivalRepo festivalRepo) {
        this.festivalRepo = festivalRepo;
    }

    public String fetchDataFromSupabaseTable(String tableName) throws Exception {
        CloseableHttpClient httpClient = createHttpClient();
        String endpoint = SUPABASE_URL + tableName;

        HttpGet httpGet = new HttpGet(endpoint);
        httpGet.setHeader("apikey", SUPABASE_KEY);

        CloseableHttpResponse response = httpClient.execute(httpGet);

        int statusCode = response.getStatusLine().getStatusCode();

        try {
            return EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            throw new RuntimeException("Request failed with status code: " + statusCode);
        }
    }

    public void saveFestivalToDataBase(Festival festival) throws Exception {
        CloseableHttpClient httpClient = createHttpClient();
        String endpoint = SUPABASE_URL + "festivals";

        HttpPost httpPost = new HttpPost(endpoint);
        httpPost.setHeader("apikey", SUPABASE_KEY);
//        httpPost.setHeader("Content-Type", "application/json");
        festivalRepo.save(festival);

        StringEntity requestEntity = new StringEntity(String.valueOf(festival), ContentType.APPLICATION_JSON);
        httpPost.setEntity((HttpEntity) festival);
    }
}
