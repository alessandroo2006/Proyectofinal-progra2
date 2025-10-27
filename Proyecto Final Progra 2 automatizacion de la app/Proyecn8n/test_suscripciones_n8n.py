#!/usr/bin/env python3
"""
Script para probar la integración de suscripciones con n8n

Uso:
    python test_suscripciones_n8n.py

Requisitos:
    pip install requests
"""

import requests
import json
import time
from datetime import datetime, timedelta

# URL del webhook específico para suscripciones
WEBHOOK_URL = "https://userfox.app.n8n.cloud/webhook-test/Android-sync"

def print_header(text):
    print("\n" + "="*70)
    print(f"  {text}")
    print("="*70)

def test_subscription_added():
    """Test: Agregar suscripción"""
    print_header("🧪 TEST 1: Agregar Suscripción (subscription_added)")
    
    # Calcular fecha de renovación (30 días desde hoy)
    renewal_date = datetime.now() + timedelta(days=30)
    renewal_timestamp = int(renewal_date.timestamp() * 1000)
    renewal_formatted = renewal_date.strftime("%d/%m/%Y")
    
    payload = {
        "action": "subscription_added",
        "userId": "test_user_123",
        "timestamp": str(int(time.time() * 1000)),
        "data": {
            "subscription_name": "Netflix Premium",
            "amount": 99.99,
            "currency": "Q",
            "frequency": "MENSUAL",
            "renewal_date": renewal_timestamp,
            "renewal_date_formatted": renewal_formatted,
            "notification_days": 7,
            "action_type": "subscription_added",
            "timestamp": str(int(time.time() * 1000))
        }
    }
    
    print(f"\n📦 Payload:")
    print(json.dumps(payload, indent=2))
    print(f"\n📤 Enviando a: {WEBHOOK_URL}")
    
    try:
        response = requests.post(WEBHOOK_URL, json=payload, timeout=30)
        print(f"\n✅ Status Code: {response.status_code}")
        
        if response.status_code == 200:
            try:
                response_data = response.json()
                print(f"📥 Respuesta: {json.dumps(response_data, indent=2)}")
            except:
                print(f"📥 Respuesta: {response.text[:200]}")
            
            print("\n✅ Suscripción agregada enviada correctamente")
            print("💡 Ve a n8n → Executions para ver los datos")
            return True
        else:
            print(f"⚠️ Error con código: {response.status_code}")
            print(f"Respuesta: {response.text}")
            return False
            
    except requests.exceptions.Timeout:
        print("❌ Timeout después de 30 segundos")
        return False
    except Exception as e:
        print(f"❌ Error: {e}")
        return False

def test_subscription_deleted():
    """Test: Eliminar suscripción"""
    print_header("🧪 TEST 2: Eliminar Suscripción (subscription_deleted)")
    
    payload = {
        "action": "subscription_deleted",
        "userId": "test_user_123",
        "timestamp": str(int(time.time() * 1000)),
        "data": {
            "subscription_name": "Spotify",
            "amount": 49.99,
            "currency": "Q",
            "frequency": "MENSUAL",
            "action_type": "subscription_deleted",
            "timestamp": str(int(time.time() * 1000))
        }
    }
    
    print(f"\n📦 Payload:")
    print(json.dumps(payload, indent=2))
    print(f"\n📤 Enviando a: {WEBHOOK_URL}")
    
    try:
        response = requests.post(WEBHOOK_URL, json=payload, timeout=30)
        print(f"\n✅ Status Code: {response.status_code}")
        
        if response.status_code == 200:
            try:
                response_data = response.json()
                print(f"📥 Respuesta: {json.dumps(response_data, indent=2)}")
            except:
                print(f"📥 Respuesta: {response.text[:200]}")
            
            print("\n✅ Suscripción eliminada enviada correctamente")
            print("💡 Ve a n8n → Executions para ver el evento")
            return True
        else:
            print(f"⚠️ Error con código: {response.status_code}")
            print(f"Respuesta: {response.text}")
            return False
            
    except requests.exceptions.Timeout:
        print("❌ Timeout después de 30 segundos")
        return False
    except Exception as e:
        print(f"❌ Error: {e}")
        return False

def test_multiple_subscriptions():
    """Test: Agregar múltiples suscripciones"""
    print_header("🧪 TEST 3: Múltiples Suscripciones")
    
    subscriptions = [
        {"name": "Netflix", "amount": 99.99, "frequency": "MENSUAL"},
        {"name": "Spotify", "amount": 49.99, "frequency": "MENSUAL"},
        {"name": "Amazon Prime", "amount": 149.99, "frequency": "ANUAL"},
        {"name": "Disney+", "amount": 79.99, "frequency": "MENSUAL"},
    ]
    
    results = []
    
    for sub in subscriptions:
        renewal_date = datetime.now() + timedelta(days=30)
        renewal_timestamp = int(renewal_date.timestamp() * 1000)
        renewal_formatted = renewal_date.strftime("%d/%m/%Y")
        
        payload = {
            "action": "subscription_added",
            "userId": "test_user_123",
            "timestamp": str(int(time.time() * 1000)),
            "data": {
                "subscription_name": sub["name"],
                "amount": sub["amount"],
                "currency": "Q",
                "frequency": sub["frequency"],
                "renewal_date": renewal_timestamp,
                "renewal_date_formatted": renewal_formatted,
                "notification_days": 7,
                "action_type": "subscription_added",
                "timestamp": str(int(time.time() * 1000))
            }
        }
        
        print(f"\n📤 Enviando: {sub['name']} - Q{sub['amount']} ({sub['frequency']})")
        
        try:
            response = requests.post(WEBHOOK_URL, json=payload, timeout=30)
            if response.status_code == 200:
                print(f"   ✅ Enviado correctamente")
                results.append(True)
            else:
                print(f"   ❌ Error: {response.status_code}")
                results.append(False)
        except Exception as e:
            print(f"   ❌ Error: {e}")
            results.append(False)
        
        time.sleep(1)  # Pausa entre peticiones
    
    print(f"\n📊 Resultado: {sum(results)}/{len(results)} suscripciones enviadas correctamente")
    return all(results)

def run_all_tests():
    """Ejecuta todos los tests"""
    print("\n" + "🚀 PRUEBAS DE INTEGRACIÓN DE SUSCRIPCIONES CON N8N ".center(70, "="))
    print(f"URL: {WEBHOOK_URL}")
    print(f"Hora: {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}")
    print("="*70)
    
    results = []
    
    # Test 1: Agregar suscripción
    results.append(("Agregar Suscripción", test_subscription_added()))
    time.sleep(2)
    
    # Test 2: Eliminar suscripción
    results.append(("Eliminar Suscripción", test_subscription_deleted()))
    time.sleep(2)
    
    # Test 3: Múltiples suscripciones
    results.append(("Múltiples Suscripciones", test_multiple_subscriptions()))
    
    # Resumen
    print("\n" + "="*70)
    print("📊 RESUMEN DE RESULTADOS")
    print("="*70)
    
    for test_name, result in results:
        status = "✅ PASÓ" if result else "❌ FALLÓ"
        print(f"{test_name:.<50} {status}")
    
    total_passed = sum(1 for _, result in results if result)
    total_tests = len(results)
    
    print("="*70)
    print(f"Total: {total_passed}/{total_tests} tests exitosos")
    print("="*70)
    
    if total_passed == total_tests:
        print("\n🎉 ¡TODOS LOS TESTS PASARON!")
        print("\n✅ Verificaciones:")
        print("   1. Ve a n8n → Executions")
        print("   2. Deberías ver varias ejecuciones con:")
        print("      - action: subscription_added")
        print("      - action: subscription_deleted")
        print("   3. Los datos completos de cada suscripción")
        
        print("\n📋 Próximos pasos:")
        print("   1. Configura el Switch Node en n8n para filtrar por action")
        print("   2. Conecta a Google Sheets para guardar las suscripciones")
        print("   3. Configura recordatorios automáticos")
        print("   4. Recompila la app y prueba desde el dispositivo")
        
    else:
        print(f"\n⚠️ {total_tests - total_passed} test(s) fallaron")
        print("\n💡 Posibles causas:")
        print("   1. El workflow en n8n no está activo")
        print("   2. La URL del webhook es incorrecta")
        print("   3. El path en n8n no coincide: webhook-test/Android-sync")
        
        print("\n🔧 Soluciones:")
        print("   1. Ve a https://userfox.app.n8n.cloud/")
        print("   2. Verifica que el workflow esté activo (toggle verde)")
        print("   3. Verifica el path del webhook")
        print("   4. Consulta: INTEGRACION_SUSCRIPCIONES_N8N.md")
    
    print("\n" + "="*70)

if __name__ == "__main__":
    import sys
    
    print("""
╔══════════════════════════════════════════════════════════════════════╗
║     🧪 PRUEBAS DE SUSCRIPCIONES → N8N                               ║
║                                                                      ║
║  Este script prueba la integración de suscripciones con n8n         ║
╚══════════════════════════════════════════════════════════════════════╝
    """)
    
    if len(sys.argv) > 1:
        test_num = sys.argv[1]
        if test_num == "1":
            test_subscription_added()
        elif test_num == "2":
            test_subscription_deleted()
        elif test_num == "3":
            test_multiple_subscriptions()
        else:
            print("❌ Test inválido. Opciones: 1, 2, 3")
    else:
        # Ejecutar todos los tests
        run_all_tests()
    
    print("\n💡 Consejos:")
    print("   • Revisa las ejecuciones en n8n → Executions")
    print("   • Verifica que los datos de las suscripciones sean correctos")
    print("   • Puedes ejecutar un test específico: python test_suscripciones_n8n.py [1-3]")
    print("   • Consulta INTEGRACION_SUSCRIPCIONES_N8N.md para más detalles")
    print("\n✅ Script completado\n")

