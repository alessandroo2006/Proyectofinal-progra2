package com.example.prueba;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface N8nWebhookService {

    @POST
    Call<N8nResponse> sendData(@Url String url, @Body N8nRequest request);

    // Clase para la respuesta del webhook
    class N8nResponse {
        private String status;
        private String message;
        private Object data;

        // Getters y setters
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }

        public Object getData() { return data; }
        public void setData(Object data) { this.data = data; }
    }

    // Clase para la petición al webhook
    class N8nRequest {
        private String action;
        private Object data;
        private String timestamp;
        private String userId;

        public N8nRequest() {}

        public N8nRequest(String action, Object data, String userId) {
            this.action = action;
            this.data = data;
            this.userId = userId;
            this.timestamp = String.valueOf(System.currentTimeMillis());
        }

        // Getters y setters
        public String getAction() { return action; }
        public void setAction(String action) { this.action = action; }

        public Object getData() { return data; }
        public void setData(Object data) { this.data = data; }

        public String getTimestamp() { return timestamp; }
        public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
    }
}
