# ✅ Webhook de n8n Actualizado

## 🔗 Nueva URL del Webhook
```
https://foxyti.app.n8n.cloud/webhook-test/942559e5-d4e4-4ad6-a384-9fa8f4b0f2f6
```

## 📝 Archivos Actualizados

### 1. MainActivity.java
- **Línea 33:** `FINANCIAL_TOOLS_WEBHOOK_URL`
- **Función:** Envía datos financieros cuando la app se inicia

### 2. N8nWebhookClient.java
- **Línea 18:** `FINANCIAL_DATA_WEBHOOK_URL`
- **Función:** Cliente principal para todas las conexiones con n8n

### 3. WebhookHelper.java
- **Línea 24:** `WEBHOOK_URL`
- **Función:** Helper para pruebas manuales del webhook

## 🚀 ¿Qué envía la app?

### Al iniciar la aplicación:
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

### Al probar la conexión:
```json
{
  "action": "test_connection",
  "userId": "test_user",
  "timestamp": "1234567890",
  "data": "Testing connection from Android app"
}
```

### Al acceder a Herramientas Financieras:
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

## 🧪 Cómo Probar la Conexión

### Opción 1: Desde la App
1. Compila y ejecuta la app en tu dispositivo/emulador
2. La app intentará conectarse automáticamente al iniciar
3. Verás un Toast (notificación) indicando el estado de la conexión
4. También puedes usar el botón de prueba de webhook en los ajustes

### Opción 2: Script de Prueba
1. Ejecuta `test_nuevo_webhook.bat`
2. Observa la respuesta del servidor

### Opción 3: Prueba Manual con cURL
```bash
curl -X POST \
  "https://foxyti.app.n8n.cloud/webhook-test/942559e5-d4e4-4ad6-a384-9fa8f4b0f2f6" \
  -H "Content-Type: application/json" \
  -d '{"action":"test","userId":"manual_test","data":"Hello from cURL"}'
```

## ⚙️ Configuración Requerida en n8n

Para que el webhook funcione, asegúrate de que en n8n:

1. **El workflow esté ACTIVO** (toggle en verde)
2. **Nodo Webhook configurado:**
   - HTTP Method: `POST`
   - Path: `webhook-test/942559e5-d4e4-4ad6-a384-9fa8f4b0f2f6`
   - Response Mode: `Last Node` o `Response Node`
   - Authentication: `None` (o según tu configuración)

3. **Respuesta esperada:**
   ```json
   {
     "status": "success",
     "message": "Data received"
   }
   ```

## 📱 Eventos que Activan el Webhook

| Evento | Cuándo se envía | Archivo |
|--------|-----------------|---------|
| Inicio de App | Al abrir MainActivity | MainActivity.java |
| Test de Conexión | Al iniciar la app | MainActivity.java |
| Botón Probar Webhook | Click en ajustes | MainActivity.java |
| Herramientas Financieras | Al navegar a herramientas | FinancialToolsActivity |

## 🔧 Solución de Problemas

### Error 404 - Not Found
- ✅ Verifica que el workflow en n8n esté **activo**
- ✅ Confirma que el path sea exactamente: `webhook-test/942559e5-d4e4-4ad6-a384-9fa8f4b0f2f6`

### Error 0 - Network Error
- ✅ Verifica tu conexión a Internet
- ✅ Confirma que la URL sea accesible desde tu dispositivo

### Error 500 - Internal Server Error
- ✅ Revisa la configuración del workflow en n8n
- ✅ Verifica que el nodo Webhook esté correctamente conectado

## 📊 Monitoreo

Para ver los datos que llegan a n8n:
1. Abre tu workflow en n8n
2. Haz clic en el nodo Webhook
3. Ve a la pestaña "Executions" para ver las peticiones recibidas

---

**Fecha de actualización:** ${new Date().toLocaleDateString('es-ES')}
**Estado:** ✅ Configurado y Listo

