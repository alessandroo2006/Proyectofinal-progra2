# 🧪 Prueba con Webhook Temporal

## ⚡ Solución Rápida para Probar

Si quieres probar la app INMEDIATAMENTE mientras configuras tu n8n, puedes usar un webhook de prueba público.

### Opción A: Webhook.site (Recomendado para pruebas)

1. **Abre:** https://webhook.site/
2. **Copia la URL única** que te dan (algo como: `https://webhook.site/xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx`)
3. **Modifica MainActivity.java:**

```java
// Línea 31 - Cambia temporalmente la URL
private static final String FINANCIAL_TOOLS_WEBHOOK_URL = 
    "https://webhook.site/TU-UUID-AQUI";  // ← Pega tu URL de webhook.site
```

4. **Compila y ejecuta la app**
5. **Ve a webhook.site** para ver las peticiones que llegan

**Ventajas:**
- ✅ Funciona inmediatamente
- ✅ Ves las peticiones en tiempo real
- ✅ No necesitas configurar nada

**Desventajas:**
- ⚠️ Es temporal y público
- ⚠️ Solo para pruebas, NO para producción

### Opción B: RequestBin

1. **Abre:** https://requestbin.com/
2. **Crea un endpoint público**
3. **Copia la URL**
4. **Úsala en MainActivity.java** (línea 31)

---

## 🔄 Volver a tu Webhook de n8n

Una vez que configures tu workflow en n8n:

1. **Restaura la URL original** en MainActivity.java:
```java
private static final String FINANCIAL_TOOLS_WEBHOOK_URL = 
    "https://userfox.app.n8n.cloud/webhook-test/1536943f-27e3-4934-bd8c-1b3a4cc89b93";
```

2. **Recompila la app**
3. **Listo**

---

## ⚠️ Importante

Esta solución es SOLO para:
- ✅ Probar que la app funciona
- ✅ Ver qué datos se envían
- ✅ Debugging temporal

**NO usar en producción** porque:
- ❌ Los webhooks públicos son temporales
- ❌ Cualquiera puede ver los datos
- ❌ Se borran después de unas horas

---

## 🎯 Solución Definitiva

La solución real es configurar tu workflow en n8n como se explica en:
- `CREAR_WORKFLOW_MINIMO.md`
- `SOLUCION_ERROR_404.md`

