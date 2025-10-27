# 🔧 Solución Error 404 - Guía Paso a Paso

## ❌ Problema Confirmado

El webhook de n8n está dando **Error 404**, lo que significa:
- ❌ El workflow **NO está activo** en n8n
- ❌ O el webhook **NO está configurado correctamente**

---

## ✅ Solución en 5 Pasos

### **PASO 1: Acceder a n8n** 🌐

1. Abre tu navegador
2. Ve a: **https://userfox.app.n8n.cloud/**
3. Inicia sesión con tu cuenta

---

### **PASO 2: Verificar si Tienes un Workflow** 📋

Una vez dentro de n8n, mira el panel izquierdo:

**¿Ves algún workflow en la lista?**

```
┌─────────────────────────┐
│  n8n                    │
├─────────────────────────┤
│  📊 Workflows           │ ← Mira aquí
│    - Mi Workflow 1      │
│    - Mi Workflow 2      │
│                         │
│  📈 Executions          │
│  ⚙️  Settings           │
└─────────────────────────┘
```

#### **SI tienes workflows:**
Ve al **PASO 3A**

#### **NO tienes workflows:**
Ve al **PASO 3B**

---

### **PASO 3A: Configurar Workflow Existente** ⚙️

Si ya tienes un workflow:

1. **Abre el workflow** (click en él)

2. **Busca el nodo "Webhook"**
   - Debe haber un nodo con icono de webhook
   - Si no lo tiene, agrégalo (click en + → busca "Webhook")

3. **Click en el nodo Webhook** para configurarlo

4. **Verifica la configuración:**
   ```
   ┌──────────────────────────────────┐
   │  Webhook                         │
   ├──────────────────────────────────┤
   │  HTTP Method: POST               │ ← Debe ser POST
   │  Path: webhook/finanzas-webhook  │ ← Exactamente así
   │  Authentication: None            │
   │  Response Mode: Last Node        │
   └──────────────────────────────────┘
   ```

   ⚠️ **MUY IMPORTANTE:**
   - Path debe ser: `webhook/finanzas-webhook`
   - **SIN** barra inicial `/`
   - **NO:** `/webhook/finanzas-webhook`
   - **SÍ:** `webhook/finanzas-webhook`

5. **Guardar:**
   - Click en **"Save"** (esquina superior derecha)
   - O presiona **Ctrl+S**

6. **ACTIVAR el workflow:** ⭐ **CRÍTICO**
   ```
   ┌──────────────────────────────────────┐
   │  Mi Workflow        [Inactive] ❌    │ ← Está inactivo
   └──────────────────────────────────────┘
   
   Haz click en el toggle para activarlo:
   
   ┌──────────────────────────────────────┐
   │  Mi Workflow        [Active] ✅      │ ← Ahora está activo
   └──────────────────────────────────────┘
   ```

7. **Verifica la URL:**
   - En el nodo Webhook, busca "Webhook URLs"
   - Deberías ver:
     ```
     Production URL:
     https://userfox.app.n8n.cloud/webhook/finanzas-webhook
     ```
   - **Copia esta URL** y compárala con tu app

8. **Ve al PASO 4**

---

### **PASO 3B: Crear Workflow Nuevo** 🆕

Si NO tienes workflows:

1. **Click en "Add workflow"** o el botón **"+"**

2. **Se abrirá un canvas vacío**

3. **Agregar nodo Webhook:**
   - Click en el botón **"+"** grande en el centro
   - En el buscador, escribe: **"webhook"**
   - Click en **"Webhook"**

4. **Configurar el Webhook:**
   - Se abrirá el panel de configuración
   - Configura así:
   ```
   HTTP Method: POST
   Path: webhook/finanzas-webhook
   Authentication: None
   Response Mode: Last Node
   ```

5. **(Opcional) Agregar nodo de respuesta:**
   - Pasa el mouse sobre el nodo Webhook
   - Click en el **"+"** que aparece a la derecha
   - Busca: **"Respond to Webhook"**
   - Click en ese nodo
   - Configura:
     ```
     Response Mode: Using 'Respond to Webhook' Node
     Response Code: 200
     Response Body: {"status":"success","message":"OK"}
     ```

6. **Guardar el workflow:**
   - Click en **"Save"** (esquina superior derecha)
   - Dale un nombre: **"Finanzas App Webhook"**

7. **ACTIVAR el workflow:** ⭐ **CRÍTICO**
   - Busca el **toggle** en la esquina superior derecha
   - Debe decir "Inactive" o "Active"
   - **Haz click** para que se ponga **VERDE**
   - Debe cambiar a **"Active"**

8. **Ve al PASO 4**

---

### **PASO 4: Probar el Webhook** 🧪

Ahora que el workflow está activo, prueba el webhook:

#### **Opción A: Desde Windows (PowerShell)**

1. Abre PowerShell en la carpeta del proyecto
2. Ejecuta:
   ```powershell
   cd PRUEBA221
   .\test_webhook_simple.ps1
   ```

**Resultado esperado:**
```
✅ ÉXITO!
Status: 200 OK
Respuesta: {"status":"success","message":"OK"}
```

#### **Opción B: Desde Python**

```bash
cd PRUEBA221
python test_flujo_n8n.py
```

**Resultado esperado:**
```
✅ 7/7 tests exitosos
```

#### **Opción C: Desde el Navegador (Test Button)**

1. En n8n, en el nodo Webhook
2. Click en **"Listen for Test Event"**
3. Ejecuta el script de prueba
4. n8n capturará la petición automáticamente

---

### **PASO 5: Probar desde la App** 📱

Si el test anterior funcionó (✅ 200 OK):

1. **Recompila la app:**
   ```
   Build → Rebuild Project
   ```

2. **Ejecuta la app:**
   ```
   Run → Run 'app'
   ```

3. **Prueba una herramienta:**
   - Abre la app
   - Ve a "Herramientas Financieras"
   - Toca cualquier herramienta (ej. Suscripciones)

4. **Verifica en n8n:**
   - Ve a n8n → **Executions** (panel izquierdo)
   - Deberías ver una ejecución nueva
   - Status: **Success** ✅

5. **Verifica en la app:**
   - Abre Android Studio → Logcat
   - Filtro: `FinancialToolsActivity`
   - Deberías ver: `Evento enviado a n8n: subscription_tracker_accessed`

---

## 🔍 Diagnóstico de Problemas

### ❌ Sigue dando Error 404

**Posibles causas:**

1. **El workflow no está activo**
   - Solución: Verifica que el toggle esté en VERDE

2. **El Path es incorrecto**
   - Incorrecto: `/webhook/finanzas-webhook` (con `/` inicial)
   - Correcto: `webhook/finanzas-webhook` (sin `/` inicial)

3. **No guardaste el workflow**
   - Solución: Ctrl+S o click en "Save"

4. **El workflow está en otra cuenta**
   - Solución: Verifica que estás en la cuenta correcta

5. **La URL en la app es diferente**
   - Solución: Copia la URL exacta del nodo Webhook en n8n

---

### ❌ Error de Timeout

**Causa:** n8n está tardando mucho en responder

**Solución:**
- Verifica tu conexión a Internet
- Verifica que n8n.cloud esté funcionando
- Intenta más tarde

---

### ❌ Error de Conexión

**Causa:** No hay conexión a Internet

**Solución:**
- Verifica tu WiFi/datos móviles
- Prueba abrir https://userfox.app.n8n.cloud/ en el navegador

---

## 📊 Checklist de Verificación

Marca cada item conforme lo verifiques:

### En n8n:
- [ ] Tengo acceso a https://userfox.app.n8n.cloud/
- [ ] Tengo un workflow creado
- [ ] El workflow tiene un nodo "Webhook"
- [ ] El Path del webhook es: `webhook/finanzas-webhook` (sin `/` inicial)
- [ ] El HTTP Method es: POST
- [ ] El workflow está GUARDADO (Ctrl+S)
- [ ] El workflow está ACTIVO (toggle verde) ← **MUY IMPORTANTE**
- [ ] La URL del webhook es: `https://userfox.app.n8n.cloud/webhook/finanzas-webhook`

### Pruebas:
- [ ] El script PowerShell responde 200 OK
- [ ] O el script Python pasa los tests
- [ ] Veo ejecuciones en n8n → Executions

### En la App:
- [ ] La URL en el código es correcta
- [ ] La app está recompilada
- [ ] La app está instalada
- [ ] Los logs muestran "Evento enviado a n8n"

---

## 🎯 Resumen Visual

```
❌ ANTES (Error 404):
App → POST → n8n (workflow inactivo) → 404 Error

✅ DESPUÉS (Funcionando):
App → POST → n8n (workflow activo) → 200 OK → Ejecución exitosa
```

---

## 💡 Tip Final

**El problema MÁS COMÚN (95% de casos):**

El workflow en n8n **NO está activo** (toggle en gris).

**Solución rápida:**
1. Abre n8n
2. Abre tu workflow
3. Click en el toggle (esquina superior derecha)
4. Debe ponerse VERDE
5. Guarda (Ctrl+S)
6. Prueba nuevamente

---

## 📞 Si Aún No Funciona

Si después de seguir TODOS estos pasos aún tienes error 404:

1. **Toma un screenshot** del nodo Webhook en n8n mostrando:
   - La configuración del Path
   - El toggle activo (verde)
   - La URL del webhook

2. **Copia el error exacto** que aparece en:
   - Los logs de Android Studio
   - La respuesta del script de prueba

3. **Verifica** que la URL en n8n sea **exactamente**:
   ```
   https://userfox.app.n8n.cloud/webhook/finanzas-webhook
   ```

---

**Última actualización:** Octubre 2025  
**Problema:** Error 404 HTTP  
**Causa principal:** Workflow no activo en n8n  
**Solución:** Activar el workflow (toggle verde)

