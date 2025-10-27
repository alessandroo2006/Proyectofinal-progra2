# 🔧 Solución Error 404 HTTP

## ❌ Error Actual
```
HTTP Error: 404
```

## 🎯 Causa Principal
El error 404 significa que **el webhook de n8n no está disponible**. Las causas más comunes:

1. ⚠️ **El workflow en n8n NO está activo** (causa más común - 90%)
2. ⚠️ **El workflow no existe en n8n** 
3. ⚠️ **La URL del webhook es incorrecta**
4. ⚠️ **n8n está caído o no disponible**

---

## ✅ SOLUCIÓN PASO A PASO

### PASO 1: Verificar n8n

#### A) Accede a tu instancia de n8n
```
https://userfox.app.n8n.cloud/
```

#### B) Verifica si tienes un workflow creado
- Ve a **Workflows** en el menú izquierdo
- ¿Ves algún workflow relacionado con "finanzas" o "webhook"?

**❓ ¿NO tienes ningún workflow?**  
→ **Necesitas CREAR uno primero** (ve a "PASO 2: Crear Workflow")

**✅ ¿SÍ tienes un workflow?**  
→ Continúa con el siguiente paso

#### C) Verifica que el workflow esté ACTIVO
- Abre tu workflow
- En la esquina superior derecha, busca el **toggle/switch**
- **Debe estar en VERDE** (activo)

```
❌ GRIS/APAGADO = El webhook NO responderá (ERROR 404)
✅ VERDE/ENCENDIDO = El webhook SÍ responderá
```

**SI ESTÁ GRIS:** Haz click para activarlo → Debe cambiar a VERDE

---

### PASO 2: Crear Workflow en n8n (Si no lo tienes)

#### Opción A: Importar el Workflow Automáticamente

1. **En n8n**, click en el menú (tres líneas) → **Import from File**
2. Selecciona el archivo: `EJEMPLO_WORKFLOW_N8N.json`
3. Click en **Import**
4. Verifica que se haya importado correctamente
5. **ACTIVA** el workflow (toggle en verde)
6. **Guarda** el workflow (Ctrl+S)

#### Opción B: Crear Manualmente (Mínimo)

1. **En n8n**, click en **"Add workflow"**
2. En el workflow vacío, click en **"Add first step"**
3. Busca y selecciona: **"Webhook"**
4. Configura el nodo Webhook:
   ```
   HTTP Method: POST
   Path: webhook-test/1536943f-27e3-4934-bd8c-1b3a4cc89b93
   Response Mode: "Last Node" o "Response Node"
   ```
5. (Opcional) Agrega un nodo **"Respond to Webhook"**:
   ```
   Response Mode: JSON
   Response Body: {"status": "success", "message": "OK"}
   ```
6. **Conecta** el nodo Webhook al nodo Respond (si agregaste el paso 5)
7. **Guarda** el workflow (Ctrl+S o botón Save)
8. **ACTIVA** el workflow (toggle en verde - MUY IMPORTANTE)

**📸 Referencia Visual:**
```
┌─────────────┐         ┌──────────────────────┐
│  Webhook    │────────►│ Respond to Webhook   │
│  (POST)     │         │  (JSON: success)     │
└─────────────┘         └──────────────────────┘
```

---

### PASO 3: Verificar la URL en la App

1. Abre: `PRUEBA221/app/src/main/java/com/example/prueba/MainActivity.java`
2. Ve a la **línea 31**
3. Verifica que la URL sea exactamente:
   ```java
   private static final String FINANCIAL_TOOLS_WEBHOOK_URL = 
       "https://userfox.app.n8n.cloud/webhook-test/1536943f-27e3-4934-bd8c-1b3a4cc89b93";
   ```

4. También verifica la **línea 17** de `N8nWebhookClient.java`:
   ```java
   private static final String FINANCIAL_DATA_WEBHOOK_URL = 
       "https://userfox.app.n8n.cloud/webhook-test/1536943f-27e3-4934-bd8c-1b3a4cc89b93";
   ```

**⚠️ Importante:** 
- No debe tener espacios
- Debe ser EXACTAMENTE la misma URL que configuraste en n8n
- Debe empezar con `https://`

---

### PASO 4: Probar el Webhook ANTES de usar la App

#### Método A: Usando el Script Bat (Windows)

1. Ve a la carpeta `PRUEBA221`
2. Doble click en: `test_webhook_simple.bat`
3. Observa el resultado:
   - ✅ Si ves `"status":"success"` → **FUNCIONA**
   - ❌ Si ves error 404 → **El workflow no está activo**

#### Método B: Usando curl (Terminal)

```bash
curl -X POST https://userfox.app.n8n.cloud/webhook-test/1536943f-27e3-4934-bd8c-1b3a4cc89b93 \
  -H "Content-Type: application/json" \
  -d '{"action":"test","userId":"test","timestamp":"1234567890","data":"test"}'
```

**Resultados esperados:**
```bash
# ✅ CORRECTO (Status 200):
{"status":"success","message":"Data received successfully"}

# ❌ ERROR 404:
<!DOCTYPE html>...Page not found...
```

#### Método C: Usando Python

```bash
python test_webhook.py
```

---

### PASO 5: Probar desde la App

**Solo después de que el webhook responda correctamente en las pruebas anteriores:**

1. Compila la app (si hiciste cambios)
2. Instálala en tu dispositivo/emulador
3. Abre la app
4. Inicia sesión
5. Observa las notificaciones:
   - ✅ "Conexión exitosa con n8n" → **FUNCIONA**
   - ❌ "Error al conectar con n8n: HTTP Error: 404" → **Vuelve al PASO 1**

---

## 🔍 Diagnóstico Avanzado

### Verificar el Path del Webhook en n8n

El path debe ser EXACTAMENTE:
```
webhook-test/1536943f-27e3-4934-bd8c-1b3a4cc89b93
```

**NO debe ser:**
- ❌ `/webhook-test/1536943f-27e3-4934-bd8c-1b3a4cc89b93` (con slash inicial)
- ❌ `webhook-test/finanzas-webhook` (path diferente)
- ❌ Cualquier otra variación

### Verificar el Modo de Respuesta

En el nodo Webhook de n8n:
- **Response Mode:** Debe ser "Last Node" o "Response Node"
- **NO debe ser:** "When Last Node Finishes" si no tienes más nodos después

### Verificar Logs de n8n

1. En n8n, ve a **Executions** (panel izquierdo)
2. ¿Aparecen ejecuciones cuando pruebas desde la app?
   - **SÍ aparecen:** El webhook está recibiendo peticiones ✅
   - **NO aparecen:** El workflow no está activo o la URL es incorrecta ❌

---

## 📊 Tabla de Diagnóstico Rápido

| Síntoma | Causa | Solución |
|---------|-------|----------|
| Error 404 en app | Workflow no activo | Activar workflow en n8n |
| Error 404 en curl | Workflow no existe | Crear workflow en n8n |
| Sin ejecuciones en n8n | URL incorrecta | Verificar URL en MainActivity.java |
| Workflow gris en n8n | No activado | Click en toggle para activar |
| "Page not found" | Path incorrecto | Verificar path en webhook node |

---

## 🎯 Checklist de Verificación

Marca cada item conforme lo verifiques:

### En n8n:
- [ ] Tengo acceso a https://userfox.app.n8n.cloud/
- [ ] Tengo un workflow creado
- [ ] El workflow contiene un nodo Webhook
- [ ] El path del webhook es: `webhook-test/1536943f-27e3-4934-bd8c-1b3a4cc89b93`
- [ ] El HTTP Method es: POST
- [ ] El workflow está GUARDADO (Ctrl+S)
- [ ] El workflow está ACTIVO (toggle verde) ← **MUY IMPORTANTE**

### En la App:
- [ ] La URL en MainActivity.java línea 31 es correcta
- [ ] La URL en N8nWebhookClient.java línea 17 es correcta
- [ ] No hay espacios ni caracteres extra en las URLs
- [ ] La app está compilada con los cambios actuales

### Pruebas:
- [ ] El script `test_webhook_simple.bat` responde correctamente
- [ ] O el comando curl responde 200 OK
- [ ] O el script Python pasa los tests
- [ ] La app muestra "Conexión exitosa con n8n"

---

## 💡 Solución Más Común (90% de casos)

**El workflow en n8n NO está activo.**

### Pasos rápidos:
1. Abre n8n
2. Ve a Workflows
3. Abre tu workflow
4. Click en el **toggle** (esquina superior derecha) para activarlo
5. Debe cambiar a **VERDE**
6. Guarda (Ctrl+S)
7. Prueba nuevamente desde la app

---

## 🆘 Si Aún No Funciona

### Opción 1: Crear un Webhook de Prueba Simple

En n8n, crea un workflow nuevo MUY SIMPLE:

1. Workflow nuevo
2. Solo un nodo Webhook:
   ```
   Method: POST
   Path: test-simple
   Response: {"ok": true}
   ```
3. Actívalo
4. Prueba con curl:
   ```bash
   curl -X POST https://userfox.app.n8n.cloud/webhook/test-simple
   ```
5. Si esto funciona → El problema es el path largo
6. Si esto NO funciona → Problema con tu instancia de n8n

### Opción 2: Verificar Estado de n8n

Visita en tu navegador:
```
https://userfox.app.n8n.cloud/
```

- ✅ Si carga la página de login → n8n está funcionando
- ❌ Si no carga → n8n está caído o la URL es incorrecta

### Opción 3: Revisar Logs de la App

En Android Studio:
1. Abre Logcat
2. Filtra por: `N8nWebhookClient`
3. Busca el mensaje de error completo
4. Comparte ese error para ayuda más específica

---

## 📞 Necesitas Más Ayuda?

Si después de seguir todos estos pasos aún tienes error 404:

1. **Verifica:**
   - ¿El workflow está activo? (toggle verde)
   - ¿La URL es correcta?
   - ¿El path en n8n coincide con la app?

2. **Comparte:**
   - Screenshot del workflow en n8n (mostrando que está activo)
   - Screenshot del nodo Webhook (mostrando el path)
   - El log completo del error en Android Studio

---

## ✅ Una Vez que Funcione

Cuando veas este mensaje en la app:
```
✅ "Conexión exitosa con n8n"
```

¡Felicidades! La integración está funcionando. Puedes:
- Tocar el botón de herramientas para enviar eventos
- Ver las ejecuciones en n8n
- Personalizar el workflow según tus necesidades

---

**Última actualización:** Octubre 2025  
**Problema:** Error 404 HTTP  
**Solución principal:** Activar workflow en n8n

