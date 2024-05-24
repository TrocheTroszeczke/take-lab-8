package lab.app;

import com.google.gson.*;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lab.dto.ComplaintDTO;

public class Main {

    private static Gson gson;
    private static Client client;
    private static String baseUrl = "http://localhost:8080/Server-1.0-SNAPSHOT/api/complaints/";

    public static void main(String[] args) {
        client = ClientBuilder.newClient();
        gson = new GsonBuilder().create();
        printAll();
        String body = client.target(baseUrl + "?status=open").request(MediaType.APPLICATION_JSON)
                .get(String.class);
        MyComplaintDTO[] complaints = gson.fromJson(body, MyComplaintDTO[].class);

        body = client.target(baseUrl + complaints[0].getId()).request(MediaType.APPLICATION_JSON)
                .get(String.class);
        MyComplaintDTO complain = gson.fromJson(body, MyComplaintDTO.class);
        complain.setStatus("closed");
        Response response = client.target(baseUrl + complain.getId()).request(MediaType.APPLICATION_JSON)
                .put(Entity.json(gson.toJson(complain)));
        System.out.println("STATUS:" + response.getStatus());
        printAll();
        client.close();
    }

    private static void printAll() {
        String body = client.target(baseUrl).request(MediaType.APPLICATION_JSON)
                .get(String.class);
        MyComplaintDTO[] complaints = gson.fromJson(body, MyComplaintDTO[].class);
        for (MyComplaintDTO complaint : complaints) {
            System.out.println(complaint.getId());
        }
    }
// LocalDate has private access in LocalDate, error in json parsing
    static class MyComplaintDTO extends ComplaintDTO {
        protected String complaintDate;
    }
}
