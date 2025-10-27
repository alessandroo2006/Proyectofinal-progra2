# Script de prueba del webhook de n8n (foxyti)
# Este script prueba si el webhook de n8n está funcionando correctamente

Write-Host "================================================" -ForegroundColor Cyan
Write-Host "  PRUEBA DE WEBHOOK N8N - FOXYTI" -ForegroundColor Cyan
Write-Host "================================================" -ForegroundColor Cyan
Write-Host ""

$webhookUrl = "https://foxyti.app.n8n.cloud/webhook-test/942559e5-d4e4-4ad6-a384-9fa8f4b0f2f6"

Write-Host "🔗 URL del Webhook:" -ForegroundColor Yellow
Write-Host "   $webhookUrl" -ForegroundColor White
Write-Host ""

# Preparar datos de prueba
$testData = @{
    action = "test_connection"
    userId = "test_user"
    timestamp = [int][double]::Parse((Get-Date -UFormat %s))
    data = @{
        test = $true
        message = "Test from PowerShell script"
    }
} | ConvertTo-Json -Depth 5

Write-Host "📦 Datos a enviar:" -ForegroundColor Yellow
Write-Host $testData -ForegroundColor Gray
Write-Host ""

Write-Host "🚀 Enviando petición..." -ForegroundColor Yellow
Write-Host ""

try {
    # Intentar enviar la petición
    $response = Invoke-WebRequest -Uri $webhookUrl `
        -Method POST `
        -ContentType "application/json" `
        -Body $testData `
        -TimeoutSec 10 `
        -ErrorAction Stop
    
    Write-Host "================================================" -ForegroundColor Green
    Write-Host "  ✅ ÉXITO - WEBHOOK FUNCIONANDO" -ForegroundColor Green
    Write-Host "================================================" -ForegroundColor Green
    Write-Host ""
    Write-Host "📊 Código de Estado: $($response.StatusCode)" -ForegroundColor Green
    Write-Host "📊 Descripción: $($response.StatusDescription)" -ForegroundColor Green
    Write-Host ""
    Write-Host "📄 Respuesta del Servidor:" -ForegroundColor Cyan
    Write-Host $response.Content -ForegroundColor White
    Write-Host ""
    Write-Host "✅ El webhook está ACTIVO y respondiendo correctamente." -ForegroundColor Green
    Write-Host "✅ Tu app Android debería poder conectarse sin problemas." -ForegroundColor Green
    
} catch {
    Write-Host "================================================" -ForegroundColor Red
    Write-Host "  ❌ ERROR - WEBHOOK NO DISPONIBLE" -ForegroundColor Red
    Write-Host "================================================" -ForegroundColor Red
    Write-Host ""
    
    if ($_.Exception.Response) {
        $statusCode = [int]$_.Exception.Response.StatusCode
        Write-Host "📊 Código de Error: $statusCode" -ForegroundColor Red
        
        switch ($statusCode) {
            404 {
                Write-Host "🔴 Error 404: Not Found" -ForegroundColor Red
                Write-Host ""
                Write-Host "CAUSA MÁS PROBABLE:" -ForegroundColor Yellow
                Write-Host "  1. El workflow en n8n NO está activo (toggle apagado)" -ForegroundColor Yellow
                Write-Host "  2. El workflow NO existe en n8n" -ForegroundColor Yellow
                Write-Host "  3. La URL del webhook es incorrecta" -ForegroundColor Yellow
                Write-Host ""
                Write-Host "SOLUCIÓN:" -ForegroundColor Cyan
                Write-Host "  1. Abre: https://foxyti.app.n8n.cloud/" -ForegroundColor White
                Write-Host "  2. Ve a Workflows" -ForegroundColor White
                Write-Host "  3. Abre tu workflow" -ForegroundColor White
                Write-Host "  4. Verifica que el toggle esté en VERDE (activo)" -ForegroundColor White
                Write-Host "  5. Si está GRIS, haz click para activarlo" -ForegroundColor White
                Write-Host "  6. Guarda el workflow (Ctrl+S)" -ForegroundColor White
                Write-Host "  7. Ejecuta este script nuevamente" -ForegroundColor White
            }
            500 {
                Write-Host "🔴 Error 500: Internal Server Error" -ForegroundColor Red
                Write-Host ""
                Write-Host "El webhook existe pero hay un error en el workflow de n8n." -ForegroundColor Yellow
                Write-Host "Revisa los logs de ejecución en n8n." -ForegroundColor Yellow
            }
            401 {
                Write-Host "🔴 Error 401: Unauthorized" -ForegroundColor Red
                Write-Host ""
                Write-Host "El webhook requiere autenticación." -ForegroundColor Yellow
            }
            403 {
                Write-Host "🔴 Error 403: Forbidden" -ForegroundColor Red
                Write-Host ""
                Write-Host "No tienes permiso para acceder a este webhook." -ForegroundColor Yellow
            }
            default {
                Write-Host "🔴 Error $statusCode" -ForegroundColor Red
                Write-Host ""
                Write-Host "Revisa la configuración del webhook en n8n." -ForegroundColor Yellow
            }
        }
    } else {
        Write-Host "📊 Error: $($_.Exception.Message)" -ForegroundColor Red
        Write-Host ""
        Write-Host "POSIBLES CAUSAS:" -ForegroundColor Yellow
        Write-Host "  1. No hay conexión a Internet" -ForegroundColor Yellow
        Write-Host "  2. El servidor de n8n está caído" -ForegroundColor Yellow
        Write-Host "  3. La URL es incorrecta" -ForegroundColor Yellow
        Write-Host ""
        Write-Host "SOLUCIÓN:" -ForegroundColor Cyan
        Write-Host "  1. Verifica tu conexión a Internet" -ForegroundColor White
        Write-Host "  2. Intenta abrir https://foxyti.app.n8n.cloud/ en el navegador" -ForegroundColor White
        Write-Host "  3. Si funciona en el navegador, espera unos minutos e intenta de nuevo" -ForegroundColor White
    }
    
    Write-Host ""
    Write-Host "❌ Tu app Android NO podrá conectarse hasta que esto se solucione." -ForegroundColor Red
}

Write-Host ""
Write-Host "================================================" -ForegroundColor Cyan
Write-Host "  FIN DE LA PRUEBA" -ForegroundColor Cyan
Write-Host "================================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "📚 Para más información, consulta: DIAGNOSTICO_ERROR_404.md" -ForegroundColor Gray
Write-Host ""

Read-Host "Presiona Enter para salir"

