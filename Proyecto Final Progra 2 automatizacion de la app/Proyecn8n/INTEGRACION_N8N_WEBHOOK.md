# Integración con n8n Webhook

## 📋 Descripción
La aplicación está completamente integrada con n8n mediante webhooks para sincronizar datos financieros y eventos del usuario.

## 🔗 URL del Webhook
```
https://userfox.app.n8n.cloud/webhook-test/1536943f-27e3-4934-bd8c-1b3a4cc89b93
```

## 🎯 Funcionalidades Implementadas

### 1. Conexión Automática al Iniciar
Al abrir la aplicación (MainActivity), automáticamente:
- ✅ Prueba la conexión con n8n
- ✅ Envía datos financieros del usuario
- ✅ Muestra notificaciones de éxito/error

### 2. Evento del Botón de Herramientas Financieras
Cuando el usuario presiona el botón "Herramientas Financieras":
- ✅ Se envía un evento `financial_tools_accessed` al webhook
- ✅ Incluye el ID del usuario y descripción del evento
- ✅ Abre la pantalla de herramientas financieras

### 3. Datos Enviados al Webhook

#### Al Iniciar la App
```json
{
  "action": "financial_data_update",
  "userId": "123",
  "timestamp": "1234567890",
  "data": {
    "total_balance": 12450.00,
    "monthly_income": "Q 5,500",
    "monthly_expenses": "Q 3,900",
    "monthly_savings": "Q 1,600"
  }
}
```

#### Al Presionar Botón de Herramientas
```json
{
  "action": "financial_tools_accessed",
  "userId": "123",
  "timestamp": "1234567890",
  "data": {
    "event_description": "User accessed financial tools."
  }
}
```

## 🛠️ Arquitectura Técnica

### Clases Principales

1. **N8nWebhookClient.java**
   - Singleton que maneja todas las conexiones con n8n
   - Métodos principales:
     - `sendEventToWebhook()`: Envía cualquier evento personalizado
     - `sendFinancialData()`: Envía datos financieros
     - `testConnection()`: Prueba la conexión

2. **N8nWebhookService.java**
   - Interface de Retrofit para las llamadas HTTP
   - Clases de datos: `N8nRequest` y `N8nResponse`

### Dependencias Utilizadas
```gradle
implementation("com.squareup.retrofit2:retrofit:2.9.0")
implementation("com.squareup.retrofit2:converter-gson:2.9.0")
implementation("com.squareup.okhttp3:okhttp:4.12.0")
implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
implementation("com.google.code.gson:gson:2.10.1")
```

## 📱 Puntos de Integración en la App

### MainActivity.java
```java
// Línea 31: URL del webhook definida como constante
private static final String FINANCIAL_TOOLS_WEBHOOK_URL = "...";

// Línea 61: Inicialización del cliente webhook
webhookClient = N8nWebhookClient.getInstance();

// Línea 67: Test de conexión al iniciar
testWebhookConnection();

// Línea 107: Envío de datos financieros
sendFinancialDataToWebhook();

// Línea 334-337: Click listener del botón de herramientas
btnGotoFinancialTools.setOnClickListener(v -> {
    triggerFinancialToolsWebhook();
    startActivity(new Intent(this, FinancialToolsActivity.class));
});
```

## 🔧 Configuración en n8n

Para que el webhook funcione correctamente, asegúrate de que tu flujo en n8n:

1. **Acepte peticiones POST** en la URL configurada
2. **Procese JSON** con la siguiente estructura:
   - `action`: Tipo de evento
   - `userId`: ID del usuario
   - `timestamp`: Marca de tiempo
   - `data`: Objeto con datos específicos del evento

3. **Responda con JSON** (opcional pero recomendado):
   ```json
   {
     "status": "success",
     "message": "Data received",
     "data": {}
   }
   ```

## 🐛 Debugging

### Ver Logs en Android Studio
```bash
# Filtrar por tag N8nWebhookClient
adb logcat -s N8nWebhookClient

# O en Logcat de Android Studio:
Tag: N8nWebhookClient
```

### Mensajes de Log Importantes
- ✅ "N8n webhook service initialized successfully"
- ✅ "Webhook call successful"
- ⚠️ "Webhook service not initialized"
- ❌ "HTTP Error: [código]"
- ❌ "Network error: [mensaje]"

## 📝 Notas Importantes

1. **Permisos**: El AndroidManifest.xml ya incluye `INTERNET` y `ACCESS_NETWORK_STATE`
2. **Timeout**: Las peticiones tienen un timeout de 30 segundos
3. **Logging**: El interceptor HTTP registra todo el cuerpo de las peticiones (útil para debug)
4. **Thread Safety**: Todas las respuestas se ejecutan en el hilo principal UI con `runOnUiThread()`

## 🚀 Cómo Extender la Integración

Para agregar más eventos a n8n:

```java
// En cualquier Activity
N8nWebhookClient webhookClient = N8nWebhookClient.getInstance();

Map<String, Object> eventData = new HashMap<>();
eventData.put("descripcion", "Evento personalizado");
eventData.put("valor", 100);

webhookClient.sendEventToWebhook(
    "https://userfox.app.n8n.cloud/webhook-test/1536943f-27e3-4934-bd8c-1b3a4cc89b93",
    "mi_evento_personalizado",
    eventData,
    userId,
    new N8nWebhookClient.WebhookCallback() {
        @Override
        public void onSuccess(N8nWebhookService.N8nResponse response) {
            // Manejar éxito
        }
        
        @Override
        public void onError(String error) {
            // Manejar error
        }
    }
);
```

## ✅ Estado de Implementación
- [x] Cliente webhook configurado
- [x] Conexión automática al iniciar
- [x] Envío de datos financieros
- [x] Evento de botón de herramientas
- [x] Manejo de errores
- [x] Logs para debugging
- [x] Notificaciones al usuario

---
**Última actualización**: Octubre 2025
**Autor**: Integración automática

