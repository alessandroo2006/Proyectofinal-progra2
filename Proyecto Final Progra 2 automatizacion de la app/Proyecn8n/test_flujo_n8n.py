#!/usr/bin/env python3
"""
Script para probar el flujo completo de n8n con las herramientas financieras
Este script simula exactamente lo que envía la app Android

Uso:
    python test_flujo_n8n.py

Requisitos:
    pip install requests
"""

import requests
import json
import time
from datetime import datetime

# URL del webhook de n8n
WEBHOOK_URL = "https://userfox.app.n8n.cloud/webhook/finanzas-webhook"

def print_header(text, emoji="🧪"):
    print("\n" + "="*80)
    print(f"{emoji} {text}")
    print("="*80)

def send_event(event_name, payload, expected_route):
    """Envía un evento y muestra el resultado"""
    print(f"\n📤 Enviando: {event_name}")
    print(f"🎯 Ruta esperada en n8n: {expected_route}")
    print(f"📦 Payload:")
    print(json.dumps(payload, indent=2))
    
    try:
        response = requests.post(WEBHOOK_URL, json=payload, timeout=30)
        
        print(f"\n✅ Status Code: {response.status_code}")
        
        if response.status_code == 200:
            try:
                response_data = response.json()
                print(f"📥 Respuesta: {json.dumps(response_data, indent=2)}")
            except:
                print(f"📥 Respuesta: {response.text[:200]}")
            
            print(f"✅ {event_name} → Enviado correctamente")
            print(f"💡 Ve a n8n → Executions para ver que tomó la ruta: {expected_route}")
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

def test_1_suscripciones():
    """Test 1: Rastreador de Suscripciones → Output 0"""
    print_header("TEST 1: Suscripciones", "🔔")
    
    payload = {
        "action": "subscription_tracker_accessed",
        "userId": "test_user_123",
        "timestamp": str(int(time.time() * 1000)),
        "data": {
            "event_description": "Usuario accedió al rastreador de suscripciones",
            "timestamp": str(int(time.time() * 1000))
        }
    }
    
    return send_event(
        "Suscripciones",
        payload,
        "Output 0 → Suscripciones - Guardar (append_sheet)"
    )

def test_2_clasificador():
    """Test 2: Clasificador de Gastos → Output 1"""
    print_header("TEST 2: Clasificador de Gastos", "💰")
    
    payload = {
        "action": "expense_classifier_accessed",
        "userId": "test_user_123",
        "timestamp": str(int(time.time() * 1000)),
        "data": {
            "event_description": "Usuario accedió al clasificador de gastos",
            "timestamp": str(int(time.time() * 1000))
        }
    }
    
    return send_event(
        "Clasificador de Gastos",
        payload,
        "Output 1 → Gastos - Guardar en Sheets (append_sheet)"
    )

def test_3_ofertas():
    """Test 3: Cazador de Ofertas → Output 2"""
    print_header("TEST 3: Cazador de Ofertas", "🎯")
    
    payload = {
        "action": "deal_hunter_accessed",
        "userId": "test_user_123",
        "timestamp": str(int(time.time() * 1000)),
        "data": {
            "event_description": "Usuario accedió al cazador de ofertas",
            "timestamp": str(int(time.time() * 1000))
        }
    }
    
    return send_event(
        "Cazador de Ofertas",
        payload,
        "Output 2 → Nuevo nodo para Ofertas"
    )

def test_4_voz_acceso():
    """Test 4: Asistente por Voz - Acceso → Output 3"""
    print_header("TEST 4: Asistente por Voz (Acceso)", "🎤")
    
    payload = {
        "action": "voice_assistant_accessed",
        "userId": "test_user_123",
        "timestamp": str(int(time.time() * 1000)),
        "data": {
            "event_description": "Usuario accedió al asistente por voz",
            "timestamp": str(int(time.time() * 1000))
        }
    }
    
    return send_event(
        "Asistente por Voz (Acceso)",
        payload,
        "Output 3 → Voz - Guardar en Sheets"
    )

def test_5_voz_gasto():
    """Test 5: Gasto por Voz Confirmado → Output 4 ⭐ IMPORTANTE"""
    print_header("TEST 5: Gasto por Voz (CON DATOS) ⭐", "💸")
    
    payload = {
        "action": "voice_expense_added",
        "userId": "test_user_123",
        "timestamp": str(int(time.time() * 1000)),
        "data": {
            "amount": "250.75",
            "merchant": "Restaurante La Esquina",
            "category": "Alimentación",
            "input_method": "voice"
        }
    }
    
    print("\n💡 Este evento incluye datos estructurados del gasto:")
    print("   - Monto: Q250.75")
    print("   - Comercio: Restaurante La Esquina")
    print("   - Categoría: Alimentación")
    print("   - Método: voice")
    
    return send_event(
        "Gasto por Voz (CON DATOS)",
        payload,
        "Output 4 → Voz - Guardar en Sheets (con datos del gasto)"
    )

def test_6_voz_cancelar():
    """Test 6: Voz Cancelada → Output 5"""
    print_header("TEST 6: Asistente por Voz (Cancelado)", "❌")
    
    payload = {
        "action": "voice_input_cancelled",
        "userId": "test_user_123",
        "timestamp": str(int(time.time() * 1000)),
        "data": {
            "event_description": "Usuario canceló la entrada de voz",
            "timestamp": str(int(time.time() * 1000))
        }
    }
    
    return send_event(
        "Voz Cancelada",
        payload,
        "Output 5 → Log o Analytics"
    )

def test_7_gamificacion():
    """Test 7: Gamificación y Retos → Output 6"""
    print_header("TEST 7: Gamificación y Retos", "🏆")
    
    payload = {
        "action": "gamification_accessed",
        "userId": "test_user_123",
        "timestamp": str(int(time.time() * 1000)),
        "data": {
            "event_description": "Usuario accedió a gamificación y retos",
            "timestamp": str(int(time.time() * 1000))
        }
    }
    
    return send_event(
        "Gamificación y Retos",
        payload,
        "Output 6 → Logros - Registrar en Sheets"
    )

def run_all_tests():
    """Ejecuta todos los tests en secuencia"""
    print("\n" + "🚀 PRUEBA COMPLETA DEL FLUJO N8N ".center(80, "="))
    print(f"URL: {WEBHOOK_URL}")
    print(f"Hora: {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}")
    print("="*80)
    
    print("\n📋 Este script probará las 7 rutas del Switch Node en n8n:")
    print("   Output 0: Suscripciones")
    print("   Output 1: Clasificador de Gastos")
    print("   Output 2: Cazador de Ofertas")
    print("   Output 3: Asistente por Voz (acceso)")
    print("   Output 4: Gasto por Voz (con datos) ⭐")
    print("   Output 5: Voz cancelada")
    print("   Output 6: Gamificación")
    
    results = []
    
    # Test 1
    results.append(("Suscripciones (Output 0)", test_1_suscripciones()))
    time.sleep(2)
    
    # Test 2
    results.append(("Clasificador (Output 1)", test_2_clasificador()))
    time.sleep(2)
    
    # Test 3
    results.append(("Ofertas (Output 2)", test_3_ofertas()))
    time.sleep(2)
    
    # Test 4
    results.append(("Voz - Acceso (Output 3)", test_4_voz_acceso()))
    time.sleep(2)
    
    # Test 5 - IMPORTANTE
    results.append(("Voz - Gasto ⭐ (Output 4)", test_5_voz_gasto()))
    time.sleep(2)
    
    # Test 6
    results.append(("Voz - Cancelar (Output 5)", test_6_voz_cancelar()))
    time.sleep(2)
    
    # Test 7
    results.append(("Gamificación (Output 6)", test_7_gamificacion()))
    
    # Resumen
    print("\n" + "="*80)
    print("📊 RESUMEN DE RESULTADOS")
    print("="*80)
    
    for test_name, result in results:
        status = "✅ PASÓ" if result else "❌ FALLÓ"
        print(f"{test_name:.<60} {status}")
    
    total_passed = sum(1 for _, result in results if result)
    total_tests = len(results)
    
    print("="*80)
    print(f"Total: {total_passed}/{total_tests} tests exitosos")
    print("="*80)
    
    if total_passed == total_tests:
        print("\n🎉 ¡TODOS LOS TESTS PASARON!")
        print("\n✅ Verificaciones:")
        print("   1. Ve a n8n → Executions")
        print("   2. Deberías ver 7 ejecuciones exitosas")
        print("   3. Cada una tomó su ruta correspondiente en el Switch")
        print("   4. Los datos se guardaron en Google Sheets")
        
        print("\n📋 Próximos pasos:")
        print("   1. Verifica que los nodos de Google Sheets estén activos")
        print("   2. Revisa que los datos aparezcan en tus hojas")
        print("   3. Configura el AI Agent si lo deseas")
        print("   4. Configura las notificaciones (Gmail/Telegram)")
        print("   5. Recompila e instala la app para probar en vivo")
        
    else:
        print(f"\n⚠️ {total_tests - total_passed} test(s) fallaron")
        print("\n💡 Posibles causas:")
        print("   1. El workflow en n8n no está activo")
        print("   2. El Switch Node no está configurado correctamente")
        print("   3. Los nodos de destino están desactivados")
        print("   4. Hay un error en las condiciones del Switch")
        
        print("\n🔧 Soluciones:")
        print("   1. Activa el workflow (toggle verde)")
        print("   2. Revisa la configuración del Switch Node")
        print("   3. Activa los nodos con (Deactivated)")
        print("   4. Consulta: CONFIGURACION_N8N_SWITCH.md")
    
    print("\n" + "="*80)

def test_single(test_number):
    """Ejecuta un solo test"""
    tests = {
        1: ("Suscripciones", test_1_suscripciones),
        2: ("Clasificador", test_2_clasificador),
        3: ("Ofertas", test_3_ofertas),
        4: ("Voz - Acceso", test_4_voz_acceso),
        5: ("Voz - Gasto ⭐", test_5_voz_gasto),
        6: ("Voz - Cancelar", test_6_voz_cancelar),
        7: ("Gamificación", test_7_gamificacion)
    }
    
    if test_number in tests:
        name, func = tests[test_number]
        print(f"\n🚀 Ejecutando Test {test_number}: {name}")
        func()
    else:
        print(f"❌ Test {test_number} no existe. Opciones: 1-7")

if __name__ == "__main__":
    import sys
    
    print("""
╔══════════════════════════════════════════════════════════════════════════╗
║         🧪 PRUEBA COMPLETA DEL FLUJO N8N                                ║
║                                                                          ║
║  Este script prueba las 7 rutas del Switch Node en n8n                  ║
║  Simula exactamente lo que envía la app Android                         ║
╚══════════════════════════════════════════════════════════════════════════╝
    """)
    
    if len(sys.argv) > 1:
        try:
            test_num = int(sys.argv[1])
            test_single(test_num)
        except ValueError:
            print("❌ Argumento inválido. Usa: python test_flujo_n8n.py [1-7]")
    else:
        # Ejecutar todos los tests
        run_all_tests()
    
    print("\n💡 Consejos:")
    print("   • Abre n8n → Executions para ver las ejecuciones")
    print("   • Verifica que cada evento tomó su ruta correcta")
    print("   • Revisa los datos en Google Sheets")
    print("   • Puedes ejecutar un test específico: python test_flujo_n8n.py [1-7]")
    print("   • Consulta CONFIGURACION_N8N_SWITCH.md para más detalles")
    print("\n✅ Script completado\n")

