# 📊 Integración de Suscripciones con n8n

## ✅ Implementación Completada

La herramienta de **Suscripciones** ahora envía todos los datos a n8n automáticamente.

---

## 🔗 URL del Webhook Específico

```
https://userfox.app.n8n.cloud/webhook-test/Android-sync
```

Este webhook es **diferente** al webhook general de herramientas financieras. Es específico para sincronizar datos de suscripciones.

---

## 📊 Eventos que se Envían

### 1️⃣ **Suscripción Agregada** (`subscription_added`)

**Cuándo se dispara:** Cuando el usuario guarda una nueva suscripción

**Payload enviado a n8n:**
```json
{
  "action": "subscription_added",
  "userId": "123",
  "timestamp": "1730000000000",
  "data": {
    "subscription_name": "Netflix",
    "amount": 99.99,
    "currency": "Q",
    "frequency": "MENSUAL",
    "renewal_date": 1730000000000,
    "renewal_date_formatted": "25/10/2025",
    "notification_days": 7,
    "action_type": "subscription_added",
    "timestamp": "1730000000000"
  }
}
```

**Datos incluidos:**
- ✅ Nombre de la suscripción
- ✅ Monto
- ✅ Moneda (Q = Quetzales)
- ✅ Frecuencia (MENSUAL, TRIMESTRAL, ANUAL)
- ✅ Fecha de renovación (timestamp)
- ✅ Fecha de renovación formateada (DD/MM/YYYY)
- ✅ Días de notificación antes del vencimiento

---

### 2️⃣ **Suscripción Eliminada** (`subscription_deleted`)

**Cuándo se dispara:** Cuando el usuario mantiene presionada una suscripción para eliminarla

**Payload enviado a n8n:**
```json
{
  "action": "subscription_deleted",
  "userId": "123",
  "timestamp": "1730000000000",
  "data": {
    "subscription_name": "Netflix",
    "amount": 99.99,
    "currency": "Q",
    "frequency": "MENSUAL",
    "action_type": "subscription_deleted",
    "timestamp": "1730000000000"
  }
}
```

---

## 🎯 Flujo Completo en la App

### **Agregar Suscripción:**

```
Usuario en la App
    │
    └──► Toca "Herramientas Financieras"
         │
         └──► Toca "Notificador de Suscripciones"
              │
              └──► Toca botón "+" (Agregar)
                   │
                   └──► Llena el formulario:
                        - Nombre: Netflix
                        - Monto: 99.99
                        - Frecuencia: MENSUAL
                        - Fecha de renovación: 25/10/2025
                        │
                        └──► Toca "Guardar"
                             │
                             ├──► Se guarda en base de datos local ✅
                             │
                             └──► Se envía a n8n ✅
                                  POST: https://userfox.app.n8n.cloud/webhook-test/Android-sync
                                  │
                                  └──► Usuario ve: "📊 Suscripción sincronizada con n8n"
```

### **Eliminar Suscripción:**

```
Usuario en la App
    │
    └──► En la lista de suscripciones
         │
         └──► Mantiene presionada una suscripción
              │
              ├──► Se elimina de la base de datos local ✅
              │
              └──► Se envía evento de eliminación a n8n ✅
                   POST: https://userfox.app.n8n.cloud/webhook-test/Android-sync
                   Action: subscription_deleted
```

---

## 🔧 Archivos Modificados

### 1. **AddSubscriptionActivity.java**

**Cambios realizados:**
- ✅ Agregado `N8nWebhookClient`
- ✅ Agregada constante `N8N_SUBSCRIPTION_WEBHOOK_URL`
- ✅ Método `sendSubscriptionToN8n()` que envía datos completos
- ✅ Se llama automáticamente después de guardar

**Líneas clave:**
- Línea 30: URL del webhook
- Línea 38: Cliente webhook
- Línea 174: Llamada a `sendSubscriptionToN8n()`
- Línea 187-227: Método que envía datos a n8n

### 2. **SubscriptionTrackerActivity.java**

**Cambios realizados:**
- ✅ Agregado `N8nWebhookClient`
- ✅ Agregada constante `N8N_SUBSCRIPTION_WEBHOOK_URL`
- ✅ Método `sendSubscriptionDeletedToN8n()` para eliminaciones
- ✅ Se llama automáticamente al eliminar

**Líneas clave:**
- Línea 26: URL del webhook
- Línea 32: Cliente webhook
- Línea 85: Llamada a `sendSubscriptionDeletedToN8n()`
- Línea 118-151: Método que envía eliminación a n8n

---

## 🎨 Configuración del Workflow en n8n

### **PASO 1: Crear Webhook en n8n**

1. Abre: https://userfox.app.n8n.cloud/
2. Crea o abre un workflow
3. Agrega nodo "Webhook"
4. Configura:
   ```
   HTTP Method: POST
   Path: webhook-test/Android-sync
   Response Mode: Last Node
   ```

### **PASO 2: Agregar Switch Node**

Agrega un nodo "Switch" para filtrar por tipo de acción:

```javascript
// Ruta 0 - Suscripción Agregada
{{$json.body.action}} equals "subscription_added"

// Ruta 1 - Suscripción Eliminada
{{$json.body.action}} equals "subscription_deleted"
```

### **PASO 3: Procesar Suscripción Agregada**

Conecta la Ruta 0 a estos nodos:

**A) Set Node - Extraer Datos**
```javascript
subscription_name: {{$json.body.data.subscription_name}}
amount: {{$json.body.data.amount}}
currency: {{$json.body.data.currency}}
frequency: {{$json.body.data.frequency}}
renewal_date: {{$json.body.data.renewal_date_formatted}}
userId: {{$json.body.userId}}
```

**B) Google Sheets - Guardar**
```
Operation: Append
Sheet: Suscripciones
Columns:
  - userId
  - subscription_name
  - amount
  - currency
  - frequency
  - renewal_date
  - notification_days
  - created_at
```

**C) Send Email - Confirmación (Opcional)**
```
To: usuario@email.com
Subject: Nueva suscripción agregada: {{$json.body.data.subscription_name}}
Body:
  Se agregó una nueva suscripción:
  - Nombre: {{$json.body.data.subscription_name}}
  - Monto: {{$json.body.data.currency}} {{$json.body.data.amount}}
  - Frecuencia: {{$json.body.data.frequency}}
  - Próxima renovación: {{$json.body.data.renewal_date_formatted}}
```

**D) Calcular Próximas Renovaciones (Opcional)**
```javascript
// Función para calcular fechas futuras
const renewalDate = new Date({{$json.body.data.renewal_date}});
const frequency = "{{$json.body.data.frequency}}";

// Agregar a calendario o sistema de recordatorios
```

### **PASO 4: Procesar Suscripción Eliminada**

Conecta la Ruta 1 a estos nodos:

**A) Google Sheets - Actualizar Estado**
```
Operation: Update
Sheet: Suscripciones
Find by: subscription_name
Set: status = "deleted"
```

**B) Send Notification (Opcional)**
```
Notificar que se eliminó la suscripción
```

---

## 🧪 Probar la Integración

### **Método 1: Desde la App**

1. **Recompila la app:**
   ```
   Build → Rebuild Project
   ```

2. **Ejecuta la app**

3. **Agrega una suscripción:**
   - Ve a Herramientas Financieras
   - Toca "Notificador de Suscripciones"
   - Toca el botón "+" (agregar)
   - Llena los datos:
     - Nombre: Netflix
     - Monto: 99.99
     - Frecuencia: MENSUAL
     - Fecha: Selecciona una fecha futura
   - Toca "Guardar"
   - Deberías ver: "📊 Suscripción sincronizada con n8n"

4. **Verifica en n8n:**
   - Abre n8n → Executions
   - Verás una ejecución con action: "subscription_added"
   - Datos completos de la suscripción

5. **Elimina una suscripción:**
   - Mantén presionada una suscripción
   - Se eliminará
   - Verifica en n8n que llegó el evento "subscription_deleted"

### **Método 2: Script de Prueba**

Crea un archivo `test_suscripciones.py`:

```python
import requests
import json
import time

WEBHOOK_URL = "https://userfox.app.n8n.cloud/webhook-test/Android-sync"

# Test 1: Agregar suscripción
payload_add = {
    "action": "subscription_added",
    "userId": "test_user_123",
    "timestamp": str(int(time.time() * 1000)),
    "data": {
        "subscription_name": "Netflix",
        "amount": 99.99,
        "currency": "Q",
        "frequency": "MENSUAL",
        "renewal_date": int(time.time() * 1000),
        "renewal_date_formatted": "25/10/2025",
        "notification_days": 7,
        "action_type": "subscription_added",
        "timestamp": str(int(time.time() * 1000))
    }
}

print("📤 Enviando suscripción agregada...")
response = requests.post(WEBHOOK_URL, json=payload_add)
print(f"✅ Status: {response.status_code}")
print(f"📥 Respuesta: {response.text}")

time.sleep(2)

# Test 2: Eliminar suscripción
payload_delete = {
    "action": "subscription_deleted",
    "userId": "test_user_123",
    "timestamp": str(int(time.time() * 1000)),
    "data": {
        "subscription_name": "Netflix",
        "amount": 99.99,
        "currency": "Q",
        "frequency": "MENSUAL",
        "action_type": "subscription_deleted",
        "timestamp": str(int(time.time() * 1000))
    }
}

print("\n📤 Enviando suscripción eliminada...")
response = requests.post(WEBHOOK_URL, json=payload_delete)
print(f"✅ Status: {response.status_code}")
print(f"📥 Respuesta: {response.text}")
```

---

## 📊 Casos de Uso en n8n

### 1. **Recordatorios Automáticos**

```
Suscripción agregada
    ↓
Calcular fecha de renovación - 7 días
    ↓
Crear evento en Google Calendar
    ↓
Configurar recordatorio por email/SMS
```

### 2. **Control de Gastos**

```
Suscripción agregada
    ↓
Sumar al total de gastos mensuales
    ↓
Si excede presupuesto → Enviar alerta
    ↓
Actualizar dashboard de gastos
```

### 3. **Análisis de Suscripciones**

```
Cada suscripción agregada/eliminada
    ↓
Guardar en Google Sheets
    ↓
Generar reporte mensual:
  - Total gastado en suscripciones
  - Suscripciones más caras
  - Suscripciones canceladas
  - Tendencias de gasto
```

### 4. **Comparación de Precios**

```
Suscripción agregada
    ↓
Buscar en API de precios
    ↓
Si hay mejor oferta → Notificar usuario
```

---

## 🎯 Ventajas de esta Integración

✅ **Sincronización Automática:** Cada suscripción se envía a n8n sin intervención manual  
✅ **Datos Completos:** Incluye toda la información relevante  
✅ **Eventos Separados:** Agregar y eliminar son eventos distintos  
✅ **Webhook Dedicado:** URL específica para suscripciones  
✅ **Notificaciones en la App:** El usuario sabe cuando se sincroniza  
✅ **Manejo de Errores:** Si falla, se registra en logs pero no afecta la app  

---

## 📝 Logs para Debugging

### En Android Studio:

```
Logcat → Filtro: "AddSubscriptionActivity" o "SubscriptionTrackerActivity"

Logs esperados:
D/AddSubscriptionActivity: ✅ Suscripción enviada a n8n exitosamente
D/SubscriptionTrackerActivity: ✅ Eliminación de suscripción enviada a n8n
```

### En n8n:

```
Executions → Ver ejecución → Datos de entrada:
{
  "action": "subscription_added",
  "data": {
    "subscription_name": "Netflix",
    ...
  }
}
```

---

## ✅ Checklist de Verificación

### Código:
- [x] AddSubscriptionActivity.java modificado
- [x] SubscriptionTrackerActivity.java modificado
- [x] URL del webhook configurada
- [x] N8nWebhookClient integrado
- [x] Sin errores de compilación

### n8n:
- [ ] Webhook creado: `/webhook-test/Android-sync`
- [ ] Workflow activo (toggle verde)
- [ ] Switch Node configurado
- [ ] Nodos de procesamiento conectados
- [ ] Google Sheets configurado (opcional)

### Pruebas:
- [ ] App recompilada
- [ ] Suscripción agregada desde la app
- [ ] Notificación "Sincronizada con n8n" aparece
- [ ] Evento visible en n8n Executions
- [ ] Suscripción eliminada desde la app
- [ ] Evento de eliminación en n8n

---

## 🎉 Resultado Final

Ahora cada vez que un usuario:
- ✅ **Agrega una suscripción** → Se envía a n8n con todos los datos
- ✅ **Elimina una suscripción** → Se envía evento de eliminación a n8n

**n8n puede:**
- 📊 Guardar en base de datos
- 📧 Enviar confirmaciones
- 📅 Crear recordatorios
- 💰 Calcular gastos totales
- 🔔 Enviar alertas antes de renovaciones
- 📈 Generar reportes y análisis

---

**URL del Webhook:** https://userfox.app.n8n.cloud/webhook-test/Android-sync  
**Eventos:** 2 (subscription_added, subscription_deleted)  
**Estado:** ✅ Completamente Implementado  
**Última actualización:** Octubre 2025

