# 🔴 Diagnóstico Error HTTP 404

## 📍 Problema Detectado

La app está haciendo llamadas a **4 webhooks diferentes** de n8n, y algunos están devolviendo **Error 404 (Not Found)**.

---

## 🌐 URLs de Webhook en la App

### URL Principal (usada en 3 lugares):
```
https://foxyti.app.n8n.cloud/webhook-test/942559e5-d4e4-4ad6-a384-9fa8f4b0f2f6
```

**Se usa en:**
1. ✅ `MainActivity.java` (línea 33) - Al iniciar la app
2. ✅ `N8nWebhookClient.java` (línea 18) - Cliente base
3. ✅ `AddSubscriptionActivity.java` (línea 30) - Al agregar suscripción
4. ✅ `FinancialToolsActivity.java` (línea 302) - Al guardar gasto por voz

### URL Diferente (usada en 1 lugar):
```
https://userfox.app.n8n.cloud/webhook-test/Android-sync
```

**Se usa en:**
1. ⚠️ `SubscriptionTrackerActivity.java` (línea 26) - Al eliminar suscripción

---

## 🔍 Cuándo se Producen los Errores 404

| Momento | Archivo | Acción | Webhook |
|---------|---------|--------|---------|
| Al abrir la app | MainActivity.java | Test connection | foxyti.app.n8n.cloud |
| Al abrir Herramientas | MainActivity.java | Send financial data | foxyti.app.n8n.cloud |
| Al guardar gasto por voz | FinancialToolsActivity.java | voice_expense_saved | foxyti.app.n8n.cloud |
| Al agregar suscripción | AddSubscriptionActivity.java | subscription_added | foxyti.app.n8n.cloud |
| Al eliminar suscripción | SubscriptionTrackerActivity.java | subscription_deleted | **userfox.app.n8n.cloud** |

---

## ❌ Causas del Error 404

Un error 404 significa que **el endpoint no existe**. Las causas más comunes:

### 1. ⚠️ **El workflow en n8n NO está activo** (90% de casos)
   - El workflow existe pero está desactivado (toggle gris)
   - Solución: Activar el workflow en n8n

### 2. ⚠️ **El workflow NO existe**
   - No hay ningún workflow creado con esa URL
   - Solución: Crear el workflow en n8n

### 3. ⚠️ **La URL del webhook es incorrecta**
   - El path no coincide con el configurado en n8n
   - Solución: Copiar la URL correcta desde n8n

### 4. ⚠️ **Estás usando la cuenta incorrecta de n8n**
   - Tienes dos instancias: `foxyti.app.n8n.cloud` y `userfox.app.n8n.cloud`
   - Solución: Verificar en cuál cuenta están los workflows

---

## ✅ SOLUCIÓN PASO A PASO

### 🎯 PASO 1: Decidir qué URL usar

Tienes **2 instancias diferentes** de n8n:
- `foxyti.app.n8n.cloud` (usada en la mayoría de lugares)
- `userfox.app.n8n.cloud` (solo en SubscriptionTrackerActivity)

**¿Cuál usar?** 
- ✅ **Recomendado:** Usar solo `foxyti.app.n8n.cloud` para TODO
- ⚠️ O mantener ambas, pero necesitas workflows en ambas instancias

---

### 🎯 PASO 2: Crear/Activar Workflow en n8n

#### A) Accede a tu n8n
```
https://foxyti.app.n8n.cloud/
```

#### B) Verifica si tienes workflows
1. Click en **"Workflows"** (menú izquierdo)
2. ¿Ves algún workflow creado?

**❌ No hay workflows:**
- Ve al **PASO 3** para crear uno

**✅ Sí hay workflows:**
- Continúa con la verificación

#### C) Verifica que el workflow esté ACTIVO
1. Abre el workflow
2. En la esquina superior derecha, busca el **toggle/switch**
3. **Debe estar en VERDE** (activo)

```
❌ GRIS/APAGADO = 404 Error
✅ VERDE/ACTIVO = Funciona
```

#### D) Verifica la URL del Webhook
1. Click en el nodo "Webhook" en tu workflow
2. Copia la URL que aparece (ej: `Production URL`)
3. Compárala con la URL en la app

**¿La URL es diferente?**
- Ve al **PASO 4** para actualizar la app

---

### 🎯 PASO 3: Crear Workflow Mínimo en n8n

Si no tienes un workflow, créalo:

#### Crear Workflow Nuevo
1. En n8n, click **"Add workflow"**
2. Click **"Add first step"** → Busca **"Webhook"**
3. Configura el nodo Webhook:
   ```
   Method: POST
   Path: webhook-test/942559e5-d4e4-4ad6-a384-9fa8f4b0f2f6
   Respond: Immediately
   Response Code: 200
   Response Body: {"status":"success","message":"Data received"}
   ```
4. **Guarda** el workflow (Ctrl+S)
5. **Activa** el workflow (toggle verde)

#### Copiar la URL del Webhook
1. Click en el nodo Webhook
2. Copia la **"Production URL"**
3. Debe ser algo como: `https://foxyti.app.n8n.cloud/webhook-test/942559e5-d4e4-4ad6-a384-9fa8f4b0f2f6`

---

### 🎯 PASO 4: Verificar URLs en la App

Compara las URLs de la app con las de n8n:

#### Archivos a verificar:
1. **N8nWebhookClient.java** (línea 17-18)
2. **MainActivity.java** (línea 33)
3. **AddSubscriptionActivity.java** (línea 30)
4. **FinancialToolsActivity.java** (línea 302)
5. **SubscriptionTrackerActivity.java** (línea 26)

**¿Las URLs coinciden con n8n?**
- ✅ Sí → Continúa al PASO 5
- ❌ No → Actualiza las URLs (ve a **PASO 5B**)

---

### 🎯 PASO 5: Probar la Conexión

#### A) Prueba desde Terminal (Windows PowerShell)

```powershell
Invoke-WebRequest -Uri "https://foxyti.app.n8n.cloud/webhook-test/942559e5-d4e4-4ad6-a384-9fa8f4b0f2f6" `
  -Method POST `
  -ContentType "application/json" `
  -Body '{"action":"test","data":"test"}'
```

**Resultado esperado:**
```
StatusCode        : 200
StatusDescription : OK
Content           : {"status":"success","message":"Data received"}
```

**Si da error 404:**
```
404 : Page Not Found
```
→ El workflow NO está activo o NO existe. Vuelve al PASO 2.

#### B) Prueba desde la App

1. **Abre Logcat** en Android Studio
2. **Filtra por:** `N8nWebhookClient`
3. **Ejecuta la app**
4. **Observa los logs:**

**Logs de éxito:**
```
✅ Webhook call to ... successful. Response: ...
```

**Logs de error 404:**
```
❌ Webhook call to ... failed. HTTP Error: 404
```

---

## 🛠️ Soluciones Rápidas

### ✅ Solución 1: Activar Workflow (Más Común)

1. Abre n8n → Workflows
2. Abre tu workflow
3. Click en el **toggle** (esquina superior derecha)
4. Debe cambiar a **VERDE**
5. Guarda (Ctrl+S)
6. Prueba la app nuevamente

---

### ✅ Solución 2: Unificar URLs (Recomendado)

Cambia `SubscriptionTrackerActivity.java` para usar la misma URL que todos:

**Archivo:** `app/src/main/java/com/example/prueba/SubscriptionTrackerActivity.java`

**Línea 26:**
```java
// ❌ ANTES (URL diferente):
private static final String N8N_SUBSCRIPTION_WEBHOOK_URL = "https://userfox.app.n8n.cloud/webhook-test/Android-sync";

// ✅ DESPUÉS (URL unificada):
private static final String N8N_SUBSCRIPTION_WEBHOOK_URL = "https://foxyti.app.n8n.cloud/webhook-test/942559e5-d4e4-4ad6-a384-9fa8f4b0f2f6";
```

Esto evita tener que mantener dos instancias de n8n.

---

### ✅ Solución 3: Deshabilitar Webhooks Temporalmente

Si no necesitas n8n ahora, puedes deshabilitar las llamadas:

**Comenta las llamadas al webhook:**

En `MainActivity.java` (línea 111):
```java
// sendFinancialDataToWebhook(); // Deshabilitado temporalmente
```

En `MainActivity.java` (línea 432):
```java
// N8nWebhookClient.getInstance().testConnection(...); // Deshabilitado temporalmente
```

**⚠️ Esto desactiva la integración con n8n.**

---

## 📊 Checklist de Verificación

Marca cada item conforme lo verifiques:

### En n8n (https://foxyti.app.n8n.cloud):
- [ ] Puedo acceder a n8n
- [ ] Tengo un workflow creado
- [ ] El workflow tiene un nodo "Webhook"
- [ ] El path del webhook es: `webhook-test/942559e5-d4e4-4ad6-a384-9fa8f4b0f2f6`
- [ ] El método HTTP es: `POST`
- [ ] El workflow está **GUARDADO** (Ctrl+S)
- [ ] El workflow está **ACTIVO** (toggle verde) ← **MUY IMPORTANTE**

### En la App:
- [ ] Las URLs en todos los archivos Java apuntan a `foxyti.app.n8n.cloud`
- [ ] No hay URL a `userfox.app.n8n.cloud` (o está configurada correctamente)
- [ ] La app está recompilada con los últimos cambios

### Pruebas:
- [ ] El comando PowerShell responde con código 200
- [ ] O la prueba desde el navegador funciona
- [ ] Los logs en Logcat muestran "successful"
- [ ] La app NO muestra toasts con error 404

---

## 📱 Monitorear Errores en Tiempo Real

### En Android Studio (Logcat):

**Filtro 1: Ver todas las llamadas al webhook**
```
tag:N8nWebhookClient
```

**Filtro 2: Ver solo errores**
```
tag:N8nWebhookClient level:error
```

**Filtro 3: Ver errores 404 específicamente**
```
tag:N8nWebhookClient 404
```

---

## 🆘 Si Sigue Sin Funcionar

### Verifica los Logs Completos

En Android Studio → Logcat, busca:
```
HTTP Error: 404
```

Identifica **qué URL** está fallando y **cuándo** se llama.

### Prueba con un Webhook de Prueba Público

Usa un servicio como **webhook.site** para probar:

1. Ve a: https://webhook.site/
2. Copia tu URL única (ej: `https://webhook.site/abc123`)
3. Cambia temporalmente la URL en `N8nWebhookClient.java`:
   ```java
   private static final String BASE_URL = "https://webhook.site/";
   private static final String FINANCIAL_DATA_WEBHOOK_URL = "https://webhook.site/abc123";
   ```
4. Ejecuta la app
5. Ve a webhook.site y verifica que lleguen las peticiones

**Si funciona con webhook.site pero no con n8n:**
→ El problema está en la configuración de n8n.

**Si tampoco funciona con webhook.site:**
→ El problema está en la app (permisos de Internet, etc.)

---

## 📝 Resumen Visual

```
🔴 Error 404
    ↓
    ¿Workflow existe en n8n?
    ↓
    NO → Crear workflow (PASO 3)
    SÍ → ¿Está activo (verde)?
         ↓
         NO → Activar workflow (PASO 2)
         SÍ → ¿URL coincide con app?
              ↓
              NO → Actualizar URL (PASO 4)
              SÍ → ¿Responde con 200?
                   ↓
                   NO → Revisar logs n8n
                   SÍ → ✅ Todo funcionando
```

---

## 🎯 Acción Inmediata Recomendada

**1. Unificar URL** (más simple):
   - Cambiar `SubscriptionTrackerActivity.java` línea 26 para usar `foxyti.app.n8n.cloud`

**2. Crear/Activar workflow** en `foxyti.app.n8n.cloud`:
   - Path: `webhook-test/942559e5-d4e4-4ad6-a384-9fa8f4b0f2f6`
   - Method: POST
   - Activo: ✅ VERDE

**3. Probar** con PowerShell:
   ```powershell
   Invoke-WebRequest -Uri "https://foxyti.app.n8n.cloud/webhook-test/942559e5-d4e4-4ad6-a384-9fa8f4b0f2f6" -Method POST -Body '{"test":"data"}' -ContentType "application/json"
   ```

**4. Ejecutar app** y verificar Logcat.

---

¡Con esto deberías poder diagnosticar y solucionar el error 404! 🚀

