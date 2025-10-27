# 📊 Eventos de Herramientas Financieras → n8n

## ✅ Integración Completada

Todas las funciones de **Herramientas Financieras** ahora envían eventos a tu webhook de n8n:

```
https://userfox.app.n8n.cloud/webhook-test/finanzas-webhook
```

---

## 🎯 Eventos Implementados

### 1️⃣ **Rastreador de Suscripciones**

**Cuándo se dispara:** Usuario toca "Notificador de Suscripciones" 🔔

**Payload enviado a n8n:**
```json
{
  "action": "subscription_tracker_accessed",
  "userId": "123",
  "timestamp": "1730000000000",
  "data": {
    "event_description": "Usuario accedió al rastreador de suscripciones",
    "timestamp": "1730000000000"
  }
}
```

**Uso en n8n:**
- Registrar uso de la función
- Generar recordatorios de suscripciones
- Análisis de suscripciones más consultadas

---

### 2️⃣ **Clasificador de Gastos**

**Cuándo se dispara:** Usuario toca "Clasificador Automático de Gastos" 💰

**Payload enviado a n8n:**
```json
{
  "action": "expense_classifier_accessed",
  "userId": "123",
  "timestamp": "1730000000000",
  "data": {
    "event_description": "Usuario accedió al clasificador de gastos",
    "timestamp": "1730000000000"
  }
}
```

**Uso en n8n:**
- Activar clasificación automática de emails
- Sincronizar con servicios de categorización
- Generar reportes de gastos

---

### 3️⃣ **Cazador de Ofertas**

**Cuándo se dispara:** Usuario toca "Cazador de Ofertas" 🎯

**Payload enviado a n8n:**
```json
{
  "action": "deal_hunter_accessed",
  "userId": "123",
  "timestamp": "1730000000000",
  "data": {
    "event_description": "Usuario accedió al cazador de ofertas",
    "timestamp": "1730000000000"
  }
}
```

**Uso en n8n:**
- Buscar ofertas en tiempo real
- Notificar sobre descuentos
- Comparar precios en diferentes tiendas

---

### 4️⃣ **Asistente por Voz**

#### 4A. Acceso al Asistente

**Cuándo se dispara:** Usuario toca "Asistente por Voz" 🎤

**Payload enviado a n8n:**
```json
{
  "action": "voice_assistant_accessed",
  "userId": "123",
  "timestamp": "1730000000000",
  "data": {
    "event_description": "Usuario accedió al asistente por voz",
    "timestamp": "1730000000000"
  }
}
```

#### 4B. Gasto Agregado por Voz

**Cuándo se dispara:** Usuario confirma un gasto usando voz

**Payload enviado a n8n:**
```json
{
  "action": "voice_expense_added",
  "userId": "123",
  "timestamp": "1730000000000",
  "data": {
    "amount": "150.50",
    "merchant": "Super Mercado",
    "category": "Alimentación",
    "input_method": "voice"
  }
}
```

**Uso en n8n:**
- Guardar gasto en base de datos
- Actualizar presupuesto automáticamente
- Enviar confirmación por email/SMS
- Analizar patrones de gasto
- Generar alertas si excede presupuesto

#### 4C. Cancelación de Entrada por Voz

**Cuándo se dispara:** Usuario cancela la entrada de voz

**Payload enviado a n8n:**
```json
{
  "action": "voice_input_cancelled",
  "userId": "123",
  "timestamp": "1730000000000",
  "data": {
    "event_description": "Usuario canceló la entrada de voz",
    "timestamp": "1730000000000"
  }
}
```

**Uso en n8n:**
- Analizar tasa de cancelación
- Mejorar UX del asistente por voz

---

### 5️⃣ **Gamificación y Retos**

**Cuándo se dispara:** Usuario toca "Gamificación y Retos" 🏆

**Payload enviado a n8n:**
```json
{
  "action": "gamification_accessed",
  "userId": "123",
  "timestamp": "1730000000000",
  "data": {
    "event_description": "Usuario accedió a gamificación y retos",
    "timestamp": "1730000000000"
  }
}
```

**Uso en n8n:**
- Actualizar progreso de retos
- Desbloquear logros
- Enviar notificaciones de nuevos retos
- Generar tabla de clasificación

---

## 📈 Resumen de Eventos

| Evento | Action | Tipo de Datos |
|--------|--------|---------------|
| Suscripciones | `subscription_tracker_accessed` | Evento simple |
| Clasificador | `expense_classifier_accessed` | Evento simple |
| Ofertas | `deal_hunter_accessed` | Evento simple |
| Voz - Acceso | `voice_assistant_accessed` | Evento simple |
| Voz - Gasto | `voice_expense_added` | Datos estructurados |
| Voz - Cancelar | `voice_input_cancelled` | Evento simple |
| Gamificación | `gamification_accessed` | Evento simple |

**Total:** 7 eventos diferentes

---

## 🎨 Flujo de Usuario

```
Usuario en MainActivity
    │
    └──► Toca botón 🛠️ "Herramientas Financieras"
         │
         └──► Se envía evento: "financial_tools_accessed"
              │
              └──► Abre FinancialToolsActivity
                   │
                   ├──► Toca "Suscripciones" → evento: subscription_tracker_accessed
                   │
                   ├──► Toca "Clasificador" → evento: expense_classifier_accessed
                   │
                   ├──► Toca "Ofertas" → evento: deal_hunter_accessed
                   │
                   ├──► Toca "Asistente por Voz"
                   │    │
                   │    ├──► Evento: voice_assistant_accessed
                   │    │
                   │    └──► Confirma gasto
                   │         │
                   │         └──► Evento: voice_expense_added (con datos)
                   │
                   └──► Toca "Gamificación" → evento: gamification_accessed
```

---

## 🔧 Configuración del Workflow en n8n

### Estructura Recomendada

```
┌──────────────┐
│   Webhook    │ (Recibe todos los eventos)
│   (POST)     │
└──────┬───────┘
       │
       ▼
┌──────────────┐
│ Switch Node  │ (Filtra por "action")
└──────┬───────┘
       │
       ├──► subscription_tracker_accessed → [Lógica de suscripciones]
       │
       ├──► expense_classifier_accessed → [Activar clasificador]
       │
       ├──► deal_hunter_accessed → [Buscar ofertas]
       │
       ├──► voice_assistant_accessed → [Log de uso]
       │
       ├──► voice_expense_added → [Guardar gasto + Actualizar presupuesto]
       │
       ├──► voice_input_cancelled → [Analytics]
       │
       └──► gamification_accessed → [Actualizar progreso]
```

### Nodos Sugeridos para cada Evento

#### Para `voice_expense_added` (El más importante):

1. **Switch Node** - Filtra por action === "voice_expense_added"
2. **Set Node** - Extrae datos del gasto:
   ```
   amount: {{$json.body.data.amount}}
   merchant: {{$json.body.data.merchant}}
   category: {{$json.body.data.category}}
   userId: {{$json.body.userId}}
   ```
3. **PostgreSQL/MySQL** - Guarda en base de datos
4. **HTTP Request** - Actualiza presupuesto en API
5. **Send Email** - Confirmación al usuario
6. **Respond to Webhook** - Respuesta exitosa

#### Para eventos de acceso (analytics):

1. **Switch Node** - Filtra por action
2. **Google Sheets** - Registra en hoja de analytics
3. **Respond to Webhook** - Respuesta simple

---

## 🧪 Probar los Eventos

### Desde la App:

1. Compila e instala la app
2. Abre la app e inicia sesión
3. Ve a "Herramientas Financieras"
4. Toca cada función para disparar los eventos
5. Ve a n8n → Executions para ver los eventos

### Desde Scripts:

Puedes probar cada evento individualmente:

```bash
# Test: Suscripciones
curl -X POST https://userfox.app.n8n.cloud/webhook-test/finanzas-webhook \
  -H "Content-Type: application/json" \
  -d '{"action":"subscription_tracker_accessed","userId":"test","timestamp":"123","data":{"event_description":"Test"}}'

# Test: Gasto por voz
curl -X POST https://userfox.app.n8n.cloud/webhook-test/finanzas-webhook \
  -H "Content-Type: application/json" \
  -d '{"action":"voice_expense_added","userId":"test","timestamp":"123","data":{"amount":"50","merchant":"Tienda","category":"Comida","input_method":"voice"}}'
```

---

## 📊 Datos que Recibirás en n8n

### Evento Simple (Ejemplo):
```json
{
  "headers": {...},
  "params": {},
  "query": {},
  "body": {
    "action": "subscription_tracker_accessed",
    "userId": "123",
    "timestamp": "1730000000000",
    "data": {
      "event_description": "Usuario accedió al rastreador de suscripciones",
      "timestamp": "1730000000000"
    }
  }
}
```

### Evento con Datos (Gasto por Voz):
```json
{
  "headers": {...},
  "params": {},
  "query": {},
  "body": {
    "action": "voice_expense_added",
    "userId": "123",
    "timestamp": "1730000000000",
    "data": {
      "amount": "150.50",
      "merchant": "Super Mercado",
      "category": "Alimentación",
      "input_method": "voice"
    }
  }
}
```

---

## 💡 Ideas de Automatizaciones en n8n

### 1. Notificaciones Inteligentes
- Cuando se agrega un gasto por voz > Q100, enviar email
- Cuando se accede a ofertas, buscar descuentos relevantes
- Cuando se completa un reto, enviar notificación push

### 2. Analytics
- Guardar todos los eventos en Google Sheets
- Crear dashboard de uso de funciones
- Generar reportes semanales

### 3. Integración con Otros Servicios
- Sincronizar gastos con Google Calendar
- Enviar datos a CRM
- Conectar con Notion para tracking
- Integrar con Slack para notificaciones del equipo

### 4. Machine Learning
- Analizar patrones de gasto
- Predecir gastos futuros
- Sugerir categorías automáticamente

---

## ✅ Checklist de Implementación

- [x] Integración de eventos en FinancialToolsActivity
- [x] Webhook client configurado
- [x] URL correcta: finanzas-webhook
- [x] 7 eventos diferentes implementados
- [x] Manejo de errores
- [x] Logging para debugging
- [ ] Recompilar la app
- [ ] Probar todos los eventos
- [ ] Configurar workflow en n8n
- [ ] Verificar que llegan los datos

---

## 🎉 Resultado Final

Ahora **cada acción del usuario** en Herramientas Financieras se registra en n8n, permitiéndote:

✅ Rastrear el uso de cada función  
✅ Automatizar procesos basados en acciones del usuario  
✅ Guardar gastos agregados por voz  
✅ Generar analytics en tiempo real  
✅ Crear flujos personalizados para cada evento  

**¡Tu app está completamente integrada con n8n!** 🚀

---

**Última actualización:** Octubre 2025  
**Archivo modificado:** `FinancialToolsActivity.java`  
**Eventos implementados:** 7

