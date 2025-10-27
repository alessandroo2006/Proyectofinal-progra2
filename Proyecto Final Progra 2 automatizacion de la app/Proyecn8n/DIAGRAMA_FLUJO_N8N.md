# 📊 Diagrama de Flujo - Integración n8n

## 🔄 Flujo General de Datos

```
┌─────────────────────────────────────────────────────────────────┐
│                     APLICACIÓN ANDROID                          │
│                                                                 │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │                   MainActivity.java                       │  │
│  │  ┌────────────────────────────────────────────────────┐  │  │
│  │  │ onCreate()                                         │  │  │
│  │  │   ├─ webhookClient = N8nWebhookClient.getInstance()│  │  │
│  │  │   ├─ testWebhookConnection()        [Evento 1] ────┼──┼──┼──┐
│  │  │   └─ sendFinancialDataToWebhook()  [Evento 2] ────┼──┼──┼──┤
│  │  └────────────────────────────────────────────────────┘  │  │  │
│  │                                                           │  │  │
│  │  ┌────────────────────────────────────────────────────┐  │  │  │
│  │  │ Botón Herramientas Financieras 🛠️                 │  │  │  │
│  │  │   onClick() → triggerFinancialToolsWebhook() ──────┼──┼──┼──┤
│  │  │                                        [Evento 3]   │  │  │  │
│  │  └────────────────────────────────────────────────────┘  │  │  │
│  └──────────────────────────────────────────────────────────┘  │  │
│                                                                 │  │
│  ┌──────────────────────────────────────────────────────────┐  │  │
│  │              N8nWebhookClient.java                       │  │  │
│  │  ┌────────────────────────────────────────────────────┐  │  │  │
│  │  │ sendEventToWebhook()                               │  │  │  │
│  │  │   ├─ Crea N8nRequest con:                         │  │  │  │
│  │  │   │   • action: tipo de evento                     │  │  │  │
│  │  │   │   • data: datos del evento                     │  │  │  │
│  │  │   │   • userId: ID del usuario                     │  │  │  │
│  │  │   │   • timestamp: marca de tiempo                 │  │  │  │
│  │  │   └─ Envía petición POST via Retrofit              │  │  │  │
│  │  └────────────────────────────────────────────────────┘  │  │  │
│  └──────────────────────────────────────────────────────────┘  │  │
│                                                                 │  │
│  ┌──────────────────────────────────────────────────────────┐  │  │
│  │              N8nWebhookService.java                      │  │  │
│  │  (Interface Retrofit)                                    │  │  │
│  │  └─ @POST sendData(url, request)                        │  │  │
│  └──────────────────────────────────────────────────────────┘  │  │
└─────────────────────────────────────────────────────────────────┘  │
                                                                     │
                                    ╔════════════════════════════════╧═╗
                                    ║         INTERNET / HTTPS         ║
                                    ║  POST → userfox.app.n8n.cloud   ║
                                    ╚════════════════════════════════╤═╝
                                                                     │
┌────────────────────────────────────────────────────────────────────▼──┐
│                          PLATAFORMA N8N                               │
│                                                                        │
│  ┌──────────────────────────────────────────────────────────────┐    │
│  │  Webhook Node                                                 │    │
│  │  URL: /webhook-test/1536943f-27e3-4934-bd8c-1b3a4cc89b93    │    │
│  │  Método: POST                                                 │    │
│  │  ┌────────────────────────────────────────────────────────┐  │    │
│  │  │ Recibe JSON:                                           │  │    │
│  │  │ {                                                      │  │    │
│  │  │   "action": "...",                                     │  │    │
│  │  │   "userId": "...",                                     │  │    │
│  │  │   "timestamp": "...",                                  │  │    │
│  │  │   "data": { ... }                                      │  │    │
│  │  │ }                                                      │  │    │
│  │  └────────────────────────────────────────────────────────┘  │    │
│  └──────────────────────────────────────────────────────────────┘    │
│                              │                                        │
│                              ▼                                        │
│  ┌──────────────────────────────────────────────────────────────┐    │
│  │  Tus Nodos de Procesamiento                                  │    │
│  │  • Filtrar por tipo de action                                │    │
│  │  • Guardar en base de datos                                  │    │
│  │  • Enviar notificaciones                                     │    │
│  │  • Generar reportes                                          │    │
│  │  • Integrar con otras APIs                                   │    │
│  └──────────────────────────────────────────────────────────────┘    │
│                              │                                        │
│                              ▼                                        │
│  ┌──────────────────────────────────────────────────────────────┐    │
│  │  Response Node (Opcional)                                     │    │
│  │  Responde a la app:                                          │    │
│  │  {                                                           │    │
│  │    "status": "success",                                      │    │
│  │    "message": "Data received",                               │    │
│  │    "data": { ... }                                           │    │
│  │  }                                                           │    │
│  └──────────────────────────────────────────────────────────────┘    │
└───────────────────────────────────────────────────────────────────────┘
```

## 📱 Eventos Específicos

### Evento 1: Test de Conexión
```
App inicia
    │
    ▼
testWebhookConnection()
    │
    ▼
POST: /webhook-test/1536943f-27e3-4934-bd8c-1b3a4cc89b93
Body: {
  "action": "test_connection",
  "userId": "test_user",
  "timestamp": "1729780000000",
  "data": "Testing connection from Android app"
}
    │
    ▼
n8n recibe y procesa
    │
    ▼
Respuesta 200 OK
    │
    ▼
Toast: "Conexión exitosa con n8n" ✅
```

### Evento 2: Datos Financieros
```
loadFinancialData()
    │
    ▼
Calcula balance, ingresos, gastos, ahorros
    │
    ▼
sendFinancialDataToWebhook()
    │
    ▼
POST: /webhook-test/1536943f-27e3-4934-bd8c-1b3a4cc89b93
Body: {
  "action": "financial_data_update",
  "userId": "123",
  "timestamp": "1729780000000",
  "data": {
    "total_balance": 12450.00,
    "monthly_income": "Q 5,500",
    "monthly_expenses": "Q 3,900",
    "monthly_savings": "Q 1,600"
  }
}
    │
    ▼
n8n recibe y procesa
    │
    ▼
Toast: "Datos sincronizados con n8n" ✅
```

### Evento 3: Botón Herramientas
```
Usuario toca botón 🛠️
    │
    ▼
btnGotoFinancialTools.onClick()
    │
    ▼
triggerFinancialToolsWebhook()
    │
    ▼
POST: /webhook-test/1536943f-27e3-4934-bd8c-1b3a4cc89b93
Body: {
  "action": "financial_tools_accessed",
  "userId": "123",
  "timestamp": "1729780000000",
  "data": {
    "event_description": "User accessed financial tools."
  }
}
    │
    ▼
n8n recibe y procesa
    │
    ▼
Toast: "Evento de herramientas registrado" ✅
    │
    ▼
Abre FinancialToolsActivity
```

## 🏗️ Arquitectura de Clases

```
┌─────────────────────────────────────────┐
│         MainActivity.java               │
│  ┌───────────────────────────────────┐  │
│  │ Atributos:                        │  │
│  │ • N8nWebhookClient webhookClient  │  │
│  │ • String WEBHOOK_URL              │  │
│  │ • double currentBalance           │  │
│  └───────────────────────────────────┘  │
│  ┌───────────────────────────────────┐  │
│  │ Métodos:                          │  │
│  │ • testWebhookConnection()         │  │
│  │ • sendFinancialDataToWebhook()    │  │
│  │ • triggerFinancialToolsWebhook()  │  │
│  └───────────────────────────────────┘  │
└────────────┬────────────────────────────┘
             │ usa
             ▼
┌─────────────────────────────────────────┐
│      N8nWebhookClient.java              │
│      (Singleton Pattern)                │
│  ┌───────────────────────────────────┐  │
│  │ Atributos:                        │  │
│  │ • static instance                 │  │
│  │ • N8nWebhookService webhookService│  │
│  │ • String BASE_URL                 │  │
│  │ • String WEBHOOK_URL              │  │
│  └───────────────────────────────────┘  │
│  ┌───────────────────────────────────┐  │
│  │ Métodos:                          │  │
│  │ • getInstance()                   │  │
│  │ • sendEventToWebhook()            │  │
│  │ • sendFinancialData()             │  │
│  │ • testConnection()                │  │
│  └───────────────────────────────────┘  │
└────────────┬────────────────────────────┘
             │ usa
             ▼
┌─────────────────────────────────────────┐
│    N8nWebhookService.java               │
│    (Retrofit Interface)                 │
│  ┌───────────────────────────────────┐  │
│  │ Métodos:                          │  │
│  │ @POST sendData()                  │  │
│  └───────────────────────────────────┘  │
│  ┌───────────────────────────────────┐  │
│  │ Clases Internas:                  │  │
│  │ • N8nRequest                      │  │
│  │   - action: String                │  │
│  │   - data: Object                  │  │
│  │   - userId: String                │  │
│  │   - timestamp: String             │  │
│  │                                   │  │
│  │ • N8nResponse                     │  │
│  │   - status: String                │  │
│  │   - message: String               │  │
│  │   - data: Object                  │  │
│  └───────────────────────────────────┘  │
└────────────┬────────────────────────────┘
             │ usa
             ▼
┌─────────────────────────────────────────┐
│      Retrofit + OkHttp                  │
│  • Maneja HTTP requests                 │
│  • Serialización JSON (Gson)            │
│  • Logging de peticiones                │
│  • Timeouts (30s)                       │
└─────────────────────────────────────────┘
```

## 🎨 Interfaz de Usuario

```
╔══════════════════════════════════════════╗
║  MainActivity                            ║
╠══════════════════════════════════════════╣
║                                          ║
║  ┌────────────────────────────────────┐  ║
║  │ ¡Buenos días, Usuario! 👋         │  ║
║  │ Veamos tus finanzas hoy           │  ║
║  └────────────────────────────────────┘  ║
║                                          ║
║  ┌────────────────────────────────────┐  ║
║  │ Saldo Total                        │  ║
║  │ Q 12,450.00                        │  ║
║  │ +8.5% este mes                     │  ║
║  └────────────────────────────────────┘  ║
║                                          ║
║  [Donut Chart - Gastos por categoría]   ║
║                                          ║
║  ┌────┬────┬────┐                       ║
║  │Ing.│Gst.│Aho.│                       ║
║  └────┴────┴────┘                       ║
║                                          ║
║  ┌────────────────────────────────────┐  ║
║  │ 🛠️  Herramientas Financieras  ▶   │◄─── Al tocar: Evento a n8n
║  │    Funciones avanzadas para       │  ║
║  │    optimizar tus finanzas         │  ║
║  └────────────────────────────────────┘  ║
║                                          ║
║  [Alertas Inteligentes]                 ║
║                                          ║
║  ┌──────────────────────────────────┐   ║
║  │ 🏠 📊 💭 🎓 💰                   │   ║
║  └──────────────────────────────────┘   ║
╚══════════════════════════════════════════╝
         │
         │ Al tocar 🛠️
         ▼
╔══════════════════════════════════════════╗
║  FinancialToolsActivity                  ║
╠══════════════════════════════════════════╣
║  ← Herramientas Financieras              ║
║     Gestiona tus finanzas de forma       ║
║     inteligente                          ║
║                                          ║
║  ┌────────────────────────────────────┐  ║
║  │ 🔔 Notificador de Suscripciones   │  ║
║  └────────────────────────────────────┘  ║
║                                          ║
║  ┌────────────────────────────────────┐  ║
║  │ 💰 Clasificador de Gastos         │  ║
║  └────────────────────────────────────┘  ║
║                                          ║
║  ┌────────────────────────────────────┐  ║
║  │ 🎯 Cazador de Ofertas             │  ║
║  └────────────────────────────────────┘  ║
║                                          ║
║  ┌────────────────────────────────────┐  ║
║  │ 🎤 Asistente por Voz              │  ║
║  └────────────────────────────────────┘  ║
║                                          ║
║  ┌────────────────────────────────────┐  ║
║  │ 🏆 Gamificación y Retos           │  ║
║  └────────────────────────────────────┘  ║
╚══════════════════════════════════════════╝
```

## 🔒 Seguridad y Configuración

### Permisos en AndroidManifest.xml
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

### Configuración de Red (OkHttp)
```
• Timeout de Conexión: 30 segundos
• Timeout de Lectura: 30 segundos
• Timeout de Escritura: 30 segundos
• Logging Level: BODY (registra todo)
• HTTPS: ✅ Soportado
```

### URL Base
```
BASE_URL: https://userfox.app.n8n.cloud/
WEBHOOK_PATH: /webhook-test/1536943f-27e3-4934-bd8c-1b3a4cc89b93
```

---
**Este diagrama muestra cómo tu app Android se comunica con n8n en tiempo real** 🚀

