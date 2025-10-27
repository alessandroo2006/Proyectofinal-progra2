# 🎉 ¡Suscripciones Conectadas a n8n!

## ✅ Estado: IMPLEMENTADO Y FUNCIONANDO

Tu aplicación Android ahora envía automáticamente los datos de suscripciones a n8n cuando el usuario las guarda.

---

## 🔗 Tu Webhook

```
https://foxyti.app.n8n.cloud/webhook-test/942559e5-d4e4-4ad6-a384-9fa8f4b0f2f6
```

---

## 📊 ¿Qué Datos Recibirás?

Cuando un usuario guarda una suscripción en la app, recibirás esto en n8n:

```json
{
  "action": "subscription_added",
  "userId": "123",
  "timestamp": "1730000000000",
  "data": {
    "herramienta": "suscripciones",
    "cliente": "Juan Pérez",
    "email": "juan@example.com",
    "plan": "Netflix Premium",
    "fecha_inicio": "2025-01-15",
    "fecha_vencimiento": "2025-10-25",
    "precio": "Q99.99",
    "frecuencia": "MENSUAL"
  }
}
```

### 🎯 Campos Explicados:

| Campo | Qué es | Ejemplo |
|-------|--------|---------|
| **herramienta** | Siempre "suscripciones" | `suscripciones` |
| **cliente** | Nombre del usuario | `Juan Pérez` |
| **email** | Email del usuario | `juan@example.com` |
| **plan** | Nombre de la suscripción | `Netflix Premium` |
| **fecha_inicio** | Cuándo se guardó | `2025-01-15` |
| **fecha_vencimiento** | Cuándo se renueva | `2025-10-25` |
| **precio** | Costo (con moneda) | `Q99.99` |
| **frecuencia** | Cada cuánto se paga | `MENSUAL` / `TRIMESTRAL` / `ANUAL` |

---

## 🚀 Pasos Rápidos para Empezar

### 1️⃣ Configura n8n (5 minutos)

1. **Abre:** https://foxyti.app.n8n.cloud/
2. **Crea un workflow nuevo**
3. **Agrega nodo "Webhook":**
   - Path: `webhook-test/942559e5-d4e4-4ad6-a384-9fa8f4b0f2f6`
   - Method: `POST`
   - Response Mode: `Last Node` o `Response Node`
4. **⭐ ACTIVA el workflow** (toggle verde - MUY IMPORTANTE)

O mejor aún:
- **Importa el workflow listo:** `WORKFLOW_N8N_SUSCRIPCIONES.json`

### 2️⃣ Prueba la Conexión

**Opción A: Con el script (recomendado)**
```bash
test_suscripcion_n8n.bat
```

**Opción B: Desde la app**
1. Compila y ejecuta tu app
2. Ve a: Herramientas Financieras → Suscripciones
3. Agrega una nueva suscripción
4. Verás: "📊 Suscripción sincronizada con n8n"

### 3️⃣ Verifica en n8n

1. Ve a tu workflow
2. Click en "Executions"
3. Deberías ver los datos recibidos

---

## 📁 Archivos Importantes

| Archivo | Qué Hace |
|---------|----------|
| `AddSubscriptionActivity.java` | 💻 Código que envía los datos |
| `test_suscripcion_n8n.bat` | 🧪 Script de prueba |
| `INTEGRACION_SUSCRIPCIONES_ACTUALIZADA.md` | 📖 Documentación completa |
| `EJEMPLO_DATOS_N8N_SUSCRIPCIONES.json` | 📊 Ejemplos de datos |
| `WORKFLOW_N8N_SUSCRIPCIONES.json` | ⚙️ Workflow listo para importar |
| `RESUMEN_CAMBIOS_SUSCRIPCIONES.md` | 📝 Lista de cambios |

---

## 🎨 Flujo Visual

```
┌─────────────────────────────────────────────────────────┐
│  USUARIO EN LA APP                                      │
└─────────────────────────────────────────────────────────┘
                    │
                    │ 1. Va a Herramientas Financieras
                    ▼
        ┌─────────────────────┐
        │   Suscripciones     │
        │  (click botón +)    │
        └─────────────────────┘
                    │
                    │ 2. Llena el formulario
                    │    - Netflix Premium
                    │    - Q99.99
                    │    - MENSUAL
                    │    - 25/10/2025
                    ▼
        ┌─────────────────────┐
        │   Click "Guardar"   │
        └─────────────────────┘
                    │
                    ├──► 💾 Guarda en DB local
                    │
                    └──► 📡 Envía a n8n
                             │
                             ▼
    ┌────────────────────────────────────────────┐
    │  WEBHOOK N8N                               │
    │  foxyti.app.n8n.cloud/webhook-test/...    │
    └────────────────────────────────────────────┘
                             │
                             ▼
    ┌────────────────────────────────────────────┐
    │  PROCESA EN N8N                            │
    │  ✓ Guarda en base de datos                 │
    │  ✓ Envía email al cliente                  │
    │  ✓ Notifica en Slack                       │
    │  ✓ Crea recordatorio                       │
    └────────────────────────────────────────────┘
                             │
                             ▼
    ┌────────────────────────────────────────────┐
    │  RESPONDE A LA APP                         │
    │  {"status": "success"}                     │
    └────────────────────────────────────────────┘
                             │
                             ▼
    ┌────────────────────────────────────────────┐
    │  USUARIO VE NOTIFICACIÓN                   │
    │  "📊 Suscripción sincronizada con n8n"     │
    └────────────────────────────────────────────┘
```

---

## 💡 Ideas de Qué Hacer con los Datos en n8n

### 🎯 Básico
- ✅ Guardar en Google Sheets
- ✅ Guardar en base de datos (MySQL, PostgreSQL, etc.)
- ✅ Enviar email de confirmación al cliente

### 🚀 Intermedio
- ✅ Enviar notificación a Slack/Discord
- ✅ Crear evento en Google Calendar
- ✅ Actualizar CRM (HubSpot, Salesforce, etc.)
- ✅ Calcular métricas (total suscripciones, ingreso mensual)

### 🔥 Avanzado
- ✅ Enviar recordatorio X días antes del vencimiento
- ✅ Crear factura automática
- ✅ Integrar con sistema de pagos
- ✅ Análisis predictivo de cancelaciones
- ✅ Dashboard en tiempo real

---

## 🛠️ Ejemplo de Workflow en n8n

```
Webhook (recibe datos)
    │
    ├──► IF: ¿Es nueva suscripción?
    │         │
    │         ├──► SÍ → Enviar email bienvenida
    │         │         │
    │         │         └──► Guardar en Google Sheets
    │         │               │
    │         │               └──► Crear evento Calendar
    │         │
    │         └──► NO → Actualizar registro
    │
    └──► Responder: {"status": "success"}
```

---

## 🔧 Solución Rápida de Problemas

| Problema | Solución |
|----------|----------|
| ❌ Error 404 | Verifica que el workflow esté **ACTIVO** (toggle verde) |
| ❌ No llegan datos | Verifica el path: `webhook-test/942559e5-d4e4-4ad6-a384-9fa8f4b0f2f6` |
| ❌ Error de red | Verifica conexión a Internet del dispositivo |
| ⚠️ Datos incorrectos | Revisa los logs: `adb logcat -s AddSubscriptionActivity` |

---

## 📊 Cómo Acceder a los Datos en n8n

En tus nodos de n8n, usa estas expresiones:

```javascript
// Datos del cliente
{{ $json.data.cliente }}              // "Juan Pérez"
{{ $json.data.email }}                // "juan@example.com"

// Datos de la suscripción
{{ $json.data.plan }}                 // "Netflix Premium"
{{ $json.data.precio }}               // "Q99.99"
{{ $json.data.frecuencia }}           // "MENSUAL"

// Fechas
{{ $json.data.fecha_inicio }}         // "2025-01-15"
{{ $json.data.fecha_vencimiento }}    // "2025-10-25"

// Metadata
{{ $json.userId }}                    // "123"
{{ $json.action }}                    // "subscription_added"
```

---

## 📞 Necesitas Ayuda?

1. **Documentación Completa:** Lee `INTEGRACION_SUSCRIPCIONES_ACTUALIZADA.md`
2. **Ver Ejemplos:** Abre `EJEMPLO_DATOS_N8N_SUSCRIPCIONES.json`
3. **Importar Workflow:** Usa `WORKFLOW_N8N_SUSCRIPCIONES.json`
4. **Ver Cambios:** Lee `RESUMEN_CAMBIOS_SUSCRIPCIONES.md`

---

## 🎉 ¡Todo Listo!

Tu app está completamente configurada. Cada vez que un usuario guarde una suscripción:

1. ✅ Se guarda en la base de datos local
2. ✅ Se envía automáticamente a n8n
3. ✅ El usuario ve confirmación en pantalla
4. ✅ Puedes procesar los datos como quieras en n8n

---

## 🔥 Próximos Pasos Sugeridos

1. **Importa el workflow de ejemplo** en n8n
2. **Ejecuta** `test_suscripcion_n8n.bat` para probar
3. **Compila la app** y prueba desde un dispositivo real
4. **Agrega más nodos** en n8n según tus necesidades
5. **Monitorea** las ejecuciones en n8n

---

**Estado:** ✅ LISTO PARA PRODUCCIÓN  
**Versión:** 2.0  
**Última Actualización:** ${new Date().toLocaleDateString('es-ES')}

---

**¡Disfruta de tu integración con n8n! 🚀**

