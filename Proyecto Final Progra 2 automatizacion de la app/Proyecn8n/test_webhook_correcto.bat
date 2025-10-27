@echo off
REM Script para probar el webhook CORRECTO de n8n
REM Tu webhook: https://userfox.app.n8n.cloud/webhook-test/finanzas-webhook

echo ========================================
echo Probando TU Webhook Real de n8n
echo ========================================
echo.
echo URL: https://userfox.app.n8n.cloud/webhook/finanzas-webhook
echo.

curl -X POST https://userfox.app.n8n.cloud/webhook/finanzas-webhook ^
  -H "Content-Type: application/json" ^
  -d "{\"action\":\"test_connection\",\"userId\":\"test_user\",\"timestamp\":\"1234567890\",\"data\":\"Testing from Windows\"}"

echo.
echo.
echo ========================================
echo Resultado esperado:
echo {"message":"Workflow was started"}
echo ========================================
echo.
pause

