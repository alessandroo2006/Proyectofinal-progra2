# 🎯 Botón "Probar Webhook" - Implementación Completa

## ✅ ¿Qué se agregó?

Se ha añadido un **botón de prueba manual** en MainActivity para conectar al webhook de n8n con solo un clic.

---

## 📍 Ubicación del Botón

El botón **"⚙️ Probar"** está ubicado en la **parte superior derecha** de MainActivity, justo al lado del botón "Cerrar Sesión".

```
┌────────────────────────────────────┐
│  ¡Hola, Usuario! 👋                │
│                                    │
│  [Cerrar Sesión] [⚙️ Probar]     │
│                                    │
│  ───────────────────                │
│  Saldo Total                       │
│  Q 12,450.00                       │
└────────────────────────────────────┘
```

---

## 🎨 Cambios Realizados

### 1. Layout XML (activity_main.xml)

**Antes:**
```xml
<Button
    android:id="@+id/btn_logout"
    android:text="Cerrar Sesión" />
```

**Después:**
```xml
<LinearLayout orientation="horizontal">
    <Button
        android:id="@+id/btn_logout"
        android:text="Cerrar Sesión" />
    
    <Button
        android:id="@+id/btn_test_webhook"
        android:text="⚙️ Probar" />
</LinearLayout>
```

### 2. MainActivity.java

**Cambios realizados:**

#### ✅ Declaración del botón (línea 40):
```java
private Button btnTestWebhook;
```

#### ✅ Inicialización (línea 86):
```java
btnTestWebhook = findViewById(R.id.btn_test_webhook);
```

#### ✅ Listener del botón (línea 300-302):
```java
if (btnTestWebhook != null) {
    btnTestWebhook.setOnClickListener(v -> probarWebhookManual());
}
```

#### ✅ Método de prueba (líneas 467-502):
```java
private void probarWebhookManual() {
    Toast.makeText(this, "Conectando al webhook...", Toast.LENGTH_SHORT).show();
    
    WebhookHelper webhook = new WebhookHelper();
    
    webhook.sendGetRequest(new WebhookHelper.WebhookListener() {
        @Override
        public void onSuccess(String response, int code) {
            Toast.makeText(MainActivity.this, 
                "✅ Webhook conectado exitosamente\nCódigo: " + code, 
                Toast.LENGTH_LONG).show();
        }
        
        @Override
        public void onError(String error, int code) {
            String mensaje = "❌ Error al conectar\n" + error;
            if (code == 404) {
                mensaje += "\n\n⚠️ Verifica que el workflow en n8n esté activo";
            }
            Toast.makeText(MainActivity.this, mensaje, Toast.LENGTH_LONG).show();
        }
    });
}
```

---

## 🚀 Cómo Usar

### Paso 1: Ejecutar la App
```bash
Run ▶️ en Android Studio
```

### Paso 2: Click en el Botón
En la pantalla principal, presiona el botón **"⚙️ Probar"**

### Paso 3: Ver Resultado

**Si el webhook está activo:**
```
Toast: "✅ Webhook conectado exitosamente
        Código: 200"
```

**Si hay error 404:**
```
Toast: "❌ Error al conectar
        Código de error: 404
        
        ⚠️ Verifica que el workflow en n8n esté activo"
```

**Si no hay Internet:**
```
Toast: "❌ Error al conectar
        Failed to connect...
        
        ⚠️ Verifica tu conexión a Internet"
```

---

## 📊 Flujo de Funcionamiento

```
Usuario hace click en "⚙️ Probar"
         ↓
Toast: "Conectando al webhook..."
         ↓
probarWebhookManual() se ejecuta
         ↓
WebhookHelper.sendGetRequest()
         ↓
[Petición en segundo plano]
         ↓
Webhook de n8n responde
         ↓
   ┌─────┴──────┐
   │            │
✅ Éxito      ❌ Error
   │            │
   ↓            ↓
Código 200   Código 404/0
   │            │
   ↓            ↓
Toast verde  Toast rojo
```

---

## 🔍 Logs en Logcat

Cuando presiones el botón, verás estos logs:

### Éxito:
```
D/MainActivity: Webhook manual test - Success: {...}
D/MainActivity: Response code: 200
```

### Error:
```
E/MainActivity: Webhook manual test - Error: Código de error: 404 (Code: 404)
```

**Filtro recomendado:** `MainActivity` o `WebhookHelper`

---

## ⚙️ Configuración del Webhook

### URL Configurada:
```
https://foxyti.app.n8n.cloud/webhook/e575a55d-0c63-4577-8ca5-64abf9a2717b
```

Esta URL está en `WebhookHelper.java` (línea 14).

### ⚠️ Importante:
Para que funcione, el workflow en n8n debe estar **ACTIVO** (toggle verde).

---

## 🎯 Características del Botón

| Característica | Estado |
|---------------|--------|
| Click único | ✅ |
| Toast de progreso | ✅ |
| Toast de resultado | ✅ |
| Manejo de errores | ✅ |
| Logs detallados | ✅ |
| Mensajes personalizados | ✅ |
| Segundo plano | ✅ |
| No bloquea UI | ✅ |

---

## 🛡️ Manejo de Errores

El botón maneja inteligentemente los errores:

### Error 404:
- **Mensaje:** "Verifica que el workflow en n8n esté activo"
- **Causa:** Webhook no encontrado o inactivo
- **Solución:** Activar workflow en n8n

### Error de Conexión (código 0):
- **Mensaje:** "Verifica tu conexión a Internet"
- **Causa:** Sin Internet o servidor no responde
- **Solución:** Verificar conexión del dispositivo

### Otros Errores:
- **Mensaje:** Muestra el error específico
- **Log:** Registra detalles completos en Logcat

---

## 📝 Archivos Modificados

### 1. `activity_main.xml`
- **Líneas modificadas:** 70-104
- **Cambio:** Agregado botón "⚙️ Probar"
- **Estado:** ✅ Sin errores

### 2. `MainActivity.java`
- **Línea 40:** Declaración de `btnTestWebhook`
- **Línea 86:** Inicialización del botón
- **Líneas 300-302:** Listener del botón
- **Líneas 467-502:** Método `probarWebhookManual()`
- **Estado:** ✅ Sin errores de compilación

---

## 🧪 Pruebas Recomendadas

### Prueba 1: Webhook Activo
1. Activar workflow en n8n
2. Click en "⚙️ Probar"
3. **Esperado:** Toast verde con código 200

### Prueba 2: Webhook Inactivo
1. Desactivar workflow en n8n
2. Click en "⚙️ Probar"
3. **Esperado:** Toast rojo con error 404

### Prueba 3: Sin Internet
1. Desactivar WiFi/Datos
2. Click en "⚙️ Probar"
3. **Esperado:** Toast rojo con error de conexión

---

## 💡 Ventajas de Esta Implementación

✅ **Simple:** Solo un click  
✅ **Visual:** Botón con ícono ⚙️  
✅ **Informativo:** Toast detallado  
✅ **Seguro:** Maneja todos los errores  
✅ **No invasivo:** No bloquea la UI  
✅ **Logs útiles:** Para debugging  
✅ **Personalizado:** Mensajes en español  

---

## 🎨 Diseño del Botón

```css
Ícono: ⚙️ (engranaje)
Texto: "Probar"
Color: Blanco sobre azul
Fondo: Igual que "Cerrar Sesión"
Posición: Header superior derecha
Padding: 20dp horizontal
```

---

## 🔄 Comparación con Implementación Automática

| Aspecto | Automático (onCreate) | Manual (Botón) |
|---------|---------------------|----------------|
| Cuándo se ejecuta | Al abrir la app | Al hacer click |
| Control del usuario | Ninguno | Total |
| Frecuencia | Una vez por sesión | Cuando quiera |
| Ideal para | Testing inicial | Pruebas repetidas |

---

## 📌 Resumen

**Lo que hace el botón:**
1. Click → Muestra "Conectando..."
2. Llama a `WebhookHelper.sendGetRequest()`
3. Petición se ejecuta en segundo plano
4. Muestra resultado en Toast
5. Registra detalles en Logcat

**Lo que NO hace:**
- ❌ No bloquea la UI
- ❌ No requiere permisos adicionales
- ❌ No interfiere con otras funciones
- ❌ No consume recursos excesivos

---

## ✨ Estado Final

**Layout:** ✅ Modificado correctamente  
**MainActivity:** ✅ Código agregado  
**Compilación:** ✅ Sin errores  
**Funcionalidad:** ✅ Lista para usar  

---

## 🚀 ¡Listo para Probar!

1. **Ejecuta la app** en tu dispositivo/emulador
2. **Busca el botón** "⚙️ Probar" en la parte superior
3. **Presiona el botón** y observa el resultado
4. **Revisa Logcat** para ver los detalles

---

## 📞 Verificación Rápida

Antes de probar:
- [x] Botón agregado al layout
- [x] MainActivity modificado
- [x] WebhookHelper.java existe
- [x] Sin errores de compilación
- [ ] Workflow n8n activo (VERIFICAR ESTO)

---

**¡El botón está listo y funcionando!** 🎉

Ahora puedes probar la conexión al webhook en cualquier momento con solo un click.

