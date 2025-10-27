# 🚀 Crear Workflow Mínimo en n8n (5 minutos)

## ⚡ Configuración Rápida

### PASO 1: Crear Nuevo Workflow
1. Abre: https://userfox.app.n8n.cloud/
2. Inicia sesión
3. Click en **"Add workflow"** o el botón **"+"**
4. Se abrirá un canvas vacío

### PASO 2: Agregar Nodo Webhook
1. Click en el botón **"+" (Add node)**
2. En el buscador escribe: **webhook**
3. Selecciona: **"Webhook"** (el básico, no Webhook Trigger)
4. Se abrirá el panel de configuración

### PASO 3: Configurar el Webhook
En el panel de configuración del Webhook, ingresa:

```
Authentication: None
HTTP Method: POST
Path: webhook-test/1536943f-27e3-4934-bd8c-1b3a4cc89b93
Response Mode: Last Node
```

**⚠️ IMPORTANTE:** El Path debe ser **EXACTAMENTE**:
```
webhook-test/1536943f-27e3-4934-bd8c-1b3a4cc89b93
```

Sin espacios, sin slash inicial, sin comillas.

### PASO 4: Agregar Nodo de Respuesta (Opcional pero recomendado)
1. Hover sobre el nodo Webhook
2. Click en el **"+"** que aparece a la derecha
3. Busca: **"Respond to Webhook"**
4. Selecciona ese nodo
5. Configura:
   ```
   Response Mode: Using 'Respond to Webhook' Node
   Response Code: 200
   Response Body:
   {
     "status": "success",
     "message": "Data received successfully"
   }
   ```

### PASO 5: Guardar y Activar
1. **Guarda el workflow:**
   - Click en **"Save"** en la esquina superior derecha
   - O presiona **Ctrl+S**
   - Dale un nombre como: "Finanzas App Webhook"

2. **ACTIVA el workflow:**
   - Busca el **TOGGLE/SWITCH** en la esquina superior derecha
   - Debe decir "Inactive" o "Active"
   - **CLICK PARA ACTIVARLO** → Debe cambiar a **VERDE**
   - Debe decir "**Active**"

**📸 Visual:**
```
┌────────────────────────────────────────┐
│  Finanzas App Webhook      [Active] ✓  │ ← Este toggle debe estar verde
└────────────────────────────────────────┘
```

### PASO 6: Verificar la URL
1. Click en el nodo **Webhook**
2. En el panel de configuración, busca la sección **"Webhook URL"**
3. Debes ver algo como:
   ```
   Production URL:
   https://userfox.app.n8n.cloud/webhook-test/1536943f-27e3-4934-bd8c-1b3a4cc89b93
   ```
4. **Copia esta URL** para verificar

---

## ✅ Verificar que Funciona

### Método 1: Test en n8n
1. En el nodo Webhook, click en **"Listen for Test Event"**
2. Se activará un listener temporal
3. Abre tu app Android
4. La app enviará una petición
5. n8n la capturará automáticamente
6. Click en **"Execute Workflow"**

### Método 2: Test con Script
1. En Windows, abre PowerShell
2. Ejecuta:
```powershell
curl -X POST https://userfox.app.n8n.cloud/webhook-test/1536943f-27e3-4934-bd8c-1b3a4cc89b93 -H "Content-Type: application/json" -d '{\"action\":\"test\",\"userId\":\"test\",\"timestamp\":\"123\",\"data\":\"test\"}'
```

**Resultado esperado:**
```json
{"status":"success","message":"Data received successfully"}
```

---

## 🎯 Estructura Final del Workflow

```
┌──────────────┐         ┌─────────────────────┐
│   Webhook    │────────►│ Respond to Webhook  │
│   (POST)     │         │   (JSON: success)   │
└──────────────┘         └─────────────────────┘
     ▲
     │
     └─ Path: webhook-test/1536943f-27e3-4934-bd8c-1b3a4cc89b93
```

---

## ⚠️ CHECKLIST Final

Antes de probar la app, verifica:

- [ ] Workflow creado en n8n
- [ ] Nodo Webhook agregado
- [ ] Path es: `webhook-test/1536943f-27e3-4934-bd8c-1b3a4cc89b93`
- [ ] HTTP Method es: POST
- [ ] Nodo "Respond to Webhook" agregado (opcional)
- [ ] Workflow GUARDADO (Ctrl+S)
- [ ] Workflow **ACTIVO** (toggle verde) ← **MUY IMPORTANTE**

---

## 🎉 Probar la App

Una vez que el workflow esté activo:

1. Abre tu app Android
2. Inicia sesión
3. Debes ver:
   ```
   ✅ "Conexión exitosa con n8n"
   ✅ "Datos sincronizados con n8n"
   ```

4. Toca el botón 🛠️ "Herramientas Financieras"
5. Debes ver:
   ```
   ✅ "Evento de herramientas registrado"
   ```

6. Ve a n8n → **Executions** (panel izquierdo)
7. Verás las ejecuciones del workflow con los datos recibidos

---

## 🆘 Si No Funciona

### Error: "Workflow no encontrado"
→ Asegúrate de guardar el workflow (Ctrl+S)

### Error: Sigue apareciendo 404
→ Verifica que el toggle esté **VERDE** (activo)

### Error: "Path no coincide"
→ El path debe ser exactamente: `webhook-test/1536943f-27e3-4934-bd8c-1b3a4cc89b93`

### No aparecen ejecuciones en n8n
→ El workflow no está activo o la URL en la app es diferente

---

**Tiempo estimado:** 5 minutos  
**Dificultad:** Fácil  
**Siguiente paso:** Probar la app

