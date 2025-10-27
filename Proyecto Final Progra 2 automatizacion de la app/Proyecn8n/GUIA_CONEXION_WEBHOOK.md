# 📱 Guía Rápida: Conexión al Webhook N8N

## ✅ Ya está todo configurado

Tu proyecto ya tiene:
- ✓ Permisos de Internet en `AndroidManifest.xml`
- ✓ Dependencia OkHttp en `build.gradle.kts`
- ✓ Clase `WebhookHelper.java` para conectar al webhook

---

## 🚀 Cómo Usar (3 Pasos)

### 1️⃣ Crear instancia del WebhookHelper

```java
WebhookHelper webhook = new WebhookHelper();
```

### 2️⃣ Hacer petición GET o POST

**Opción A - Petición GET:**
```java
webhook.sendGetRequest(new WebhookHelper.WebhookListener() {
    @Override
    public void onSuccess(String response, int code) {
        Log.d("Webhook", "Conexión exitosa: " + code);
    }
    
    @Override
    public void onError(String error, int code) {
        Log.e("Webhook", "Error: " + error);
    }
});
```

**Opción B - Petición POST:**
```java
String datos = "{\"mensaje\": \"Hola desde Android\"}";

webhook.sendPostRequest(datos, new WebhookHelper.WebhookListener() {
    @Override
    public void onSuccess(String response, int code) {
        Log.d("Webhook", "Datos enviados: " + code);
    }
    
    @Override
    public void onError(String error, int code) {
        Log.e("Webhook", "Error: " + error);
    }
});
```

### 3️⃣ Listo! 🎉

La petición se ejecuta automáticamente en **segundo plano** y los callbacks se ejecutan en el **hilo principal**.

---

## 📝 Ejemplo Completo en una Activity

```java
public class MainActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Llamar al webhook
        conectarWebhook();
    }
    
    private void conectarWebhook() {
        WebhookHelper webhook = new WebhookHelper();
        
        webhook.sendGetRequest(new WebhookHelper.WebhookListener() {
            @Override
            public void onSuccess(String response, int code) {
                Toast.makeText(MainActivity.this, 
                    "Webhook conectado ✓", 
                    Toast.LENGTH_SHORT).show();
            }
            
            @Override
            public void onError(String error, int code) {
                Toast.makeText(MainActivity.this, 
                    "Error: " + error, 
                    Toast.LENGTH_SHORT).show();
            }
        });
    }
}
```

---

## 🔍 Códigos de Respuesta

| Código | Significado |
|--------|-------------|
| 200    | ✅ Conexión exitosa |
| 404    | ❌ URL incorrecta o webhook no existe |
| 500    | ❌ Error en el servidor n8n |
| 0      | ❌ Sin conexión a Internet |

---

## 🛠️ Solución de Problemas

### Error 404
- Verifica que la URL del webhook sea correcta
- Verifica que el workflow de n8n esté activo
- Prueba la URL en un navegador o Postman primero

### Error "No conectado"
- Verifica que el dispositivo tenga Internet
- Verifica los permisos en AndroidManifest.xml

### NetworkOnMainThreadException
- ❌ NO usar HttpURLConnection directamente
- ✅ Usar `WebhookHelper` (ya maneja hilos automáticamente)

---

## 📌 URL del Webhook

```
https://foxyti.app.n8n.cloud/webhook/e575a55d-0c63-4577-8ca5-64abf9a2717b
```

La URL está configurada en `WebhookHelper.java`. Para cambiarla, edita la constante `WEBHOOK_URL`.

---

## ⚠️ Importante

- Las peticiones se ejecutan **automáticamente en segundo plano**
- Los callbacks se ejecutan en el **hilo principal (UI Thread)**
- NO es necesario usar AsyncTask, Thread, o Executor manualmente
- OkHttp maneja todo el trabajo de red

---

## 🎯 Código Más Simple Posible

Para probar la conexión rápidamente, copia esto en cualquier Activity:

```java
new WebhookHelper().sendGetRequest(new WebhookHelper.WebhookListener() {
    public void onSuccess(String response, int code) {
        Toast.makeText(this, "✓ Conectado", Toast.LENGTH_SHORT).show();
    }
    public void onError(String error, int code) {
        Toast.makeText(this, "✗ Error: " + error, Toast.LENGTH_SHORT).show();
    }
});
```

¡Ya está! Sin errores, sin complicaciones. 🚀

