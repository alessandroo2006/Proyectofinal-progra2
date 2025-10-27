# 🔧 Configuración del Switch en n8n para Herramientas Financieras

## ✅ Estado: App Completamente Conectada

Tu app Android **YA está enviando eventos** a n8n. Ahora solo necesitas configurar el Switch Node en tu workflow para que dirija cada evento al flujo correcto.

---

## 📊 Eventos que Envía la App

| Herramienta en App | Action (n8n) | Ruta en Switch |
|-------------------|--------------|----------------|
| 🔔 Notificador de Suscripciones | `subscription_tracker_accessed` | Output 0 |
| 💰 Clasificador de Gastos | `expense_classifier_accessed` | Output 1 |
| 🎯 Cazador de Ofertas | `deal_hunter_accessed` | Output 2 |
| 🎤 Asistente por Voz (abrir) | `voice_assistant_accessed` | Output 3 |
| 🎤 Asistente por Voz (gasto) | `voice_expense_added` | Output 4 ⭐ |
| 🎤 Asistente por Voz (cancelar) | `voice_input_cancelled` | Output 5 |
| 🏆 Gamificación y Retos | `gamification_accessed` | Output 6 |

---

## 🎯 Configuración del Switch Node en n8n

### Paso 1: Abrir el Switch Node

1. En tu workflow de n8n, click en el nodo **"Switch - Seleccionar Ruta"**
2. Verás la configuración del Switch

### Paso 2: Configurar las Rutas

**Mode:** Rules

**Configuración de cada ruta:**

#### **Output 0 - Suscripciones**
```
Conditions:
  - Field: {{$json.body.action}}
  - Operation: Equal
  - Value: subscription_tracker_accessed
```
**Conectar a:** Suscripciones - Guardar (append_sheet)

---

#### **Output 1 - Gastos (Clasificador)**
```
Conditions:
  - Field: {{$json.body.action}}
  - Operation: Equal
  - Value: expense_classifier_accessed
```
**Conectar a:** Gastos - Guardar en Sheets (append_sheet)

---

#### **Output 2 - Ofertas**
```
Conditions:
  - Field: {{$json.body.action}}
  - Operation: Equal
  - Value: deal_hunter_accessed
```
**Conectar a:** Nuevo nodo para Ofertas (append_sheet)

---

#### **Output 3 - Voz (Acceso)**
```
Conditions:
  - Field: {{$json.body.action}}
  - Operation: Equal
  - Value: voice_assistant_accessed
```
**Conectar a:** Voz - Guardar en Sheets (append_sheet)

---

#### **Output 4 - Voz (Gasto Confirmado)** ⭐ IMPORTANTE
```
Conditions:
  - Field: {{$json.body.action}}
  - Operation: Equal
  - Value: voice_expense_added
```
**Conectar a:** Voz - Guardar en Sheets (append_sheet)

**Este es el más importante porque incluye datos estructurados:**
- amount (monto)
- merchant (comercio)
- category (categoría)
- input_method (voice)

---

#### **Output 5 - Voz (Cancelado)**
```
Conditions:
  - Field: {{$json.body.action}}
  - Operation: Equal
  - Value: voice_input_cancelled
```
**Conectar a:** Log simple o analytics

---

#### **Output 6 - Gamificación**
```
Conditions:
  - Field: {{$json.body.action}}
  - Operation: Equal
  - Value: gamification_accessed
```
**Conectar a:** Logros - Registrar en Sheets (append_sheet)

---

## 📋 Estructura JSON que Recibe n8n

### Evento Simple (Ejemplo: Suscripciones)
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

### Evento con Datos (Gasto por Voz)
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

## 🔧 Configurar Nodos de Google Sheets

### Para Suscripciones (append_sheet)

```
Operation: Append
Authentication: OAuth2
Document: [Tu Google Sheet ID]
Sheet: Suscripciones

Columns to Send:
  - userId: {{$json.body.userId}}
  - timestamp: {{$json.body.timestamp}}
  - action: {{$json.body.action}}
  - description: {{$json.body.data.event_description}}
  - fecha: {{new Date().toISOString()}}
```

### Para Gastos - Clasificador (append_sheet)

```
Operation: Append
Authentication: OAuth2
Document: [Tu Google Sheet ID]
Sheet: Gastos

Columns to Send:
  - userId: {{$json.body.userId}}
  - timestamp: {{$json.body.timestamp}}
  - action: {{$json.body.action}}
  - description: {{$json.body.data.event_description}}
  - fecha: {{new Date().toISOString()}}
```

### Para Voz - Gasto Confirmado (append_sheet) ⭐

```
Operation: Append
Authentication: OAuth2
Document: [Tu Google Sheet ID]
Sheet: Gastos_Voz

Columns to Send:
  - userId: {{$json.body.userId}}
  - timestamp: {{$json.body.timestamp}}
  - amount: {{$json.body.data.amount}}
  - merchant: {{$json.body.data.merchant}}
  - category: {{$json.body.data.category}}
  - input_method: {{$json.body.data.input_method}}
  - fecha: {{new Date().toISOString()}}
```

### Para Logros (append_sheet)

```
Operation: Append
Authentication: OAuth2
Document: [Tu Google Sheet ID]
Sheet: Logros

Columns to Send:
  - userId: {{$json.body.userId}}
  - timestamp: {{$json.body.timestamp}}
  - action: {{$json.body.action}}
  - description: {{$json.body.data.event_description}}
  - fecha: {{new Date().toISOString()}}
```

---

## 🎨 Diagrama de Flujo Completo

```
App Android
    │
    └──► Usuario toca herramienta
         │
         ├──► POST a n8n webhook
         │    URL: /webhook-test/finanzas-webhook
         │    Body: {action, userId, timestamp, data}
         │
         └──► n8n Webhook recibe
              │
              └──► Switch Node filtra por "action"
                   │
                   ├──► Output 0: subscription_tracker_accessed
                   │    └──► Google Sheets (Suscripciones)
                   │         └──► AI Agent (opcional)
                   │              └──► Notificaciones (Gmail/Telegram)
                   │
                   ├──► Output 1: expense_classifier_accessed
                   │    └──► Google Sheets (Gastos)
                   │         └──► AI Agent (opcional)
                   │              └──► Notificaciones
                   │
                   ├──► Output 2: deal_hunter_accessed
                   │    └──► Google Sheets (Ofertas)
                   │         └──► Buscar ofertas en tiempo real
                   │
                   ├──► Output 4: voice_expense_added ⭐
                   │    └──► Google Sheets (Gastos_Voz)
                   │         ├──► Actualizar presupuesto
                   │         ├──► Enviar confirmación
                   │         └──► Verificar límites
                   │
                   └──► Output 6: gamification_accessed
                        └──► Google Sheets (Logros)
                             └──► Actualizar progreso
```

---

## 🧪 Probar la Configuración

### Método 1: Script Python

```bash
cd PRUEBA221
python test_herramientas_financieras.py
```

Esto enviará los 7 eventos a n8n y verás en tiempo real cómo cada uno toma su ruta.

### Método 2: Desde la App

1. **Recompila la app** (si no lo has hecho):
   ```
   Build → Rebuild Project
   ```

2. **Ejecuta la app**

3. **Ve a Herramientas Financieras**

4. **Toca cada herramienta:**
   - Suscripciones → Verás ejecución en n8n Output 0
   - Clasificador → Verás ejecución en n8n Output 1
   - Ofertas → Verás ejecución en n8n Output 2
   - Voz → Verás ejecución en n8n Output 3/4/5
   - Gamificación → Verás ejecución en n8n Output 6

5. **Verifica en n8n:**
   - Abre: https://userfox.app.n8n.cloud/
   - Ve a "Executions"
   - Click en cada ejecución para ver por qué ruta pasó

---

## ⚙️ Activar los Nodos Desactivados

En tu workflow veo que los nodos tienen "(Deactivated)". Para activarlos:

### Opción 1: Desde el Nodo
1. Click en el nodo
2. En el panel de propiedades, busca "Disabled"
3. Desmarca el checkbox "Disabled"

### Opción 2: Click Derecho
1. Click derecho en el nodo
2. Selecciona "Enable"

### Nodos que debes activar:
- ✅ Suscripciones - Guardar (append_sheet)
- ✅ Gastos - Guardar en Sheets (append_sheet)
- ✅ Voz - Guardar en Sheets (append_sheet)
- ✅ Logros - Registrar en Sheets (append_sheet)

---

## 🔍 Verificar que Funciona

### En n8n:

1. **Activa el workflow** (toggle verde)
2. **Ejecuta el script de prueba**
3. **Ve a Executions**
4. **Deberías ver:**
   - 7 ejecuciones exitosas
   - Cada una con su ruta correspondiente
   - Datos guardados en Google Sheets

### En Google Sheets:

Crea estas hojas en tu documento:
- **Suscripciones** - Para eventos de suscripciones
- **Gastos** - Para eventos del clasificador
- **Gastos_Voz** - Para gastos confirmados por voz
- **Ofertas** - Para eventos de ofertas
- **Logros** - Para eventos de gamificación

Columnas sugeridas:
```
userId | timestamp | action | description | fecha | [otros campos específicos]
```

---

## 📊 Ejemplo de Datos en Google Sheets

### Hoja: Gastos_Voz
| userId | timestamp | amount | merchant | category | input_method | fecha |
|--------|-----------|--------|----------|----------|--------------|-------|
| 123 | 1730000000000 | 150.50 | Super Mercado | Alimentación | voice | 2025-10-24 |
| 123 | 1730000001000 | 45.00 | Gasolinera | Transporte | voice | 2025-10-24 |

### Hoja: Suscripciones
| userId | timestamp | action | description | fecha |
|--------|-----------|--------|-------------|-------|
| 123 | 1730000000000 | subscription_tracker_accessed | Usuario accedió... | 2025-10-24 |

---

## 💡 Tips Importantes

### 1. Orden de las Rutas
El Switch evalúa las condiciones en orden. Asegúrate de que no haya conflictos.

### 2. Fallback Route
Agrega una ruta "Default" al final para capturar eventos desconocidos:
```
Default Output → Log Node (para debugging)
```

### 3. Testing
Usa el botón "Execute Workflow" en n8n con datos de prueba antes de probar desde la app.

### 4. Logs
Revisa los logs en:
- **n8n:** Executions → Click en ejecución → Ver detalles
- **App:** Android Studio → Logcat → Filtro: "FinancialToolsActivity"

---

## ✅ Checklist de Configuración

- [ ] Webhook activo en n8n
- [ ] Switch Node configurado con 7 rutas
- [ ] Cada ruta conectada a su nodo correspondiente
- [ ] Nodos de Google Sheets activados (sin "Deactivated")
- [ ] Credenciales de Google configuradas
- [ ] Google Sheets creado con las hojas necesarias
- [ ] Workflow guardado
- [ ] Workflow activo (toggle verde)
- [ ] Probado con script Python
- [ ] Probado desde la app
- [ ] Datos aparecen en Google Sheets

---

## 🎉 Resultado Esperado

Cuando todo esté configurado:

1. Usuario toca "Suscripciones" en la app
2. App envía evento a n8n
3. n8n recibe y Switch dirige a Output 0
4. Se guarda en Google Sheets (hoja Suscripciones)
5. Se ejecuta AI Agent (opcional)
6. Se envía notificación (opcional)
7. Usuario ve confirmación en la app

**¡Y lo mismo para cada herramienta!** 🚀

---

**URL del Webhook:** https://userfox.app.n8n.cloud/webhook-test/finanzas-webhook  
**Estado de la App:** ✅ Completamente conectada  
**Eventos implementados:** 7  
**Próximo paso:** Configurar Switch Node en n8n

