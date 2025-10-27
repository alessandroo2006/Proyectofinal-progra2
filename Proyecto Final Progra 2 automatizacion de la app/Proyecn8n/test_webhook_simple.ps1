# Script PowerShell para probar el webhook de n8n
# Uso: .\test_webhook_simple.ps1

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Probando Webhook de n8n" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "URL: https://userfox.app.n8n.cloud/webhook/finanzas-webhook" -ForegroundColor Yellow
Write-Host ""

$body = @{
    action = "test_connection"
    userId = "test_user"
    timestamp = [DateTimeOffset]::UtcNow.ToUnixTimeMilliseconds().ToString()
    data = @{
        event_description = "Test desde PowerShell"
    }
} | ConvertTo-Json

Write-Host "Enviando petición..." -ForegroundColor Yellow

try {
    $response = Invoke-RestMethod -Uri "https://userfox.app.n8n.cloud/webhook/finanzas-webhook" `
        -Method Post `
        -ContentType "application/json" `
        -Body $body `
        -TimeoutSec 30
    
    Write-Host ""
    Write-Host "✅ ÉXITO!" -ForegroundColor Green
    Write-Host "Status: 200 OK" -ForegroundColor Green
    Write-Host "Respuesta:" -ForegroundColor Cyan
    $response | ConvertTo-Json -Depth 10
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Cyan
    Write-Host "El webhook está funcionando correctamente" -ForegroundColor Green
    Write-Host "Ahora puedes probar desde tu app Android" -ForegroundColor Green
    Write-Host "========================================" -ForegroundColor Cyan
}
catch {
    Write-Host ""
    Write-Host "❌ ERROR!" -ForegroundColor Red
    Write-Host "Mensaje: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host ""
    
    if ($_.Exception.Message -like "*404*") {
        Write-Host "⚠️ Error 404 - El webhook no existe o no está activo" -ForegroundColor Yellow
        Write-Host ""
        Write-Host "Soluciones:" -ForegroundColor Cyan
        Write-Host "1. Ve a https://userfox.app.n8n.cloud/" -ForegroundColor White
        Write-Host "2. Verifica que tengas un workflow creado" -ForegroundColor White
        Write-Host "3. Verifica que el workflow esté ACTIVO (toggle verde)" -ForegroundColor White
        Write-Host "4. Verifica que el Path del webhook sea: webhook/finanzas-webhook" -ForegroundColor White
        Write-Host "5. Guarda el workflow (Ctrl+S)" -ForegroundColor White
    }
    elseif ($_.Exception.Message -like "*timeout*") {
        Write-Host "⚠️ Timeout - n8n no respondió a tiempo" -ForegroundColor Yellow
    }
    else {
        Write-Host "⚠️ Error de conexión" -ForegroundColor Yellow
    }
    
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Cyan
}

Write-Host ""
Read-Host "Presiona Enter para salir"

