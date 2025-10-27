# 🚨 Solución Rápida - Error 404

## ⚡ Problema
Tu app da error **HTTP 404** al intentar conectar con n8n.

---

## ✅ Solución en 3 Pasos

### 📍 PASO 1: Accede a n8n
```
https://foxyti.app.n8n.cloud/
```

### 📍 PASO 2: Activa tu Workflow
1. Click en **"Workflows"** (menú izquierdo)
2. Abre tu workflow
3. En la esquina superior derecha, busca el **toggle** (interruptor)
4. **Click para activarlo** → Debe cambiar a **VERDE**
5. **Guarda** el workflow (Ctrl+S o botón Save)

### 📍 PASO 3: Prueba la Conexión

#### Opción A: Desde Windows (Recomendado)
1. Ve a la carpeta del proyecto
2. Doble click en: **`test_webhook_foxyti.bat`**
3. Observa el resultado:
   - ✅ **"Código de Estado: 200"** → Funciona
   - ❌ **"404"** → El workflow NO está activo, vuelve al Paso 2

#### Opción B: Desde PowerShell
```powershell
Invoke-WebRequest -Uri "https://foxyti.app.n8n.cloud/webhook-test/942559e5-d4e4-4ad6-a384-9fa8f4b0f2f6" -Method POST -Body '{"test":"data"}' -ContentType "application/json"
```

**Resultado esperado:**
```
StatusCode: 200
Content: {"status":"success"...}
```

#### Opción C: Ejecutar PowerShell Script
1. Ve a la carpeta del proyecto
2. Click derecho en: **`test_webhook_foxyti.ps1`** → **"Ejecutar con PowerShell"**
3. Sigue las instrucciones en pantalla

---

## 🎯 ¿No tienes un Workflow?

Si no tienes ningún workflow creado en n8n:

### Crear Workflow Rápido

1. En n8n, click **"Add workflow"**
2. Click **"Add first step"** → Busca **"Webhook"**
3. Configura el Webhook:
   ```
   HTTP Method: POST
   Path: webhook-test/942559e5-d4e4-4ad6-a384-9fa8f4b0f2f6
   Respond: Immediately
   Response Code: 200
   ```
4. En "Response Body", escribe:
   ```json
   {
     "status": "success",
     "message": "Data received from Android app"
   }
   ```
5. **Guarda** el workflow (Ctrl+S)
6. **Activa** el workflow (toggle verde)
7. Copia la "Production URL" y verifica que coincida con la de la app

---

## 🔄 Cambios Hechos en la App

He unificado todas las URLs del webhook para que usen la misma:

**Archivo modificado:**
- `SubscriptionTrackerActivity.java` (línea 26)

**URL anterior (userfox):**
```
❌ https://userfox.app.n8n.cloud/webhook-test/Android-sync
```

**URL nueva (foxyti):**
```
✅ https://foxyti.app.n8n.cloud/webhook-test/942559e5-d4e4-4ad6-a384-9fa8f4b0f2f6
```

**Beneficio:** Ahora solo necesitas un workflow en una instancia de n8n.

---

## 📱 Probar desde la App

Una vez que el webhook responda con código 200:

1. **Recompila la app** (si hiciste cambios)
2. **Ejecuta la app** en tu dispositivo/emulador
3. **Observa los Logs** en Android Studio → Logcat:
   - Filtra por: `N8nWebhookClient`
4. **Busca:**
   - ✅ `"Webhook call to ... successful"` → Funciona
   - ❌ `"HTTP Error: 404"` → Vuelve al Paso 1

---

## 📊 Checklist Rápido

- [ ] ✅ Puedo acceder a https://foxyti.app.n8n.cloud/
- [ ] ✅ Tengo un workflow creado
- [ ] ✅ El workflow está GUARDADO
- [ ] ✅ El workflow está ACTIVO (toggle verde)
- [ ] ✅ El script `test_webhook_foxyti.bat` responde con código 200
- [ ] ✅ La app ya no muestra error 404

---

## 🆘 ¿Sigue sin funcionar?

Consulta el diagnóstico completo en:
- **`DIAGNOSTICO_ERROR_404.md`** - Diagnóstico detallado con todas las soluciones

---

## 📝 Resumen Visual

```
🔴 Error 404
    ↓
Abrir n8n
    ↓
¿Hay workflow?
    NO → Crear workflow (ver arriba)
    SÍ → ¿Está activo (verde)?
         NO → Activar toggle
         SÍ → Guardar (Ctrl+S)
              ↓
         Ejecutar test_webhook_foxyti.bat
              ↓
         ¿Código 200?
              NO → Revisar path del webhook
              SÍ → ✅ Ejecutar app
```

---

**En el 90% de los casos, el problema es simplemente que el workflow no está activo.**

¡Actívalo y todo debería funcionar! 🚀

