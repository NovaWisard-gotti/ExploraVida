# ExploraVida

**El gran laboratorio de la vida** — aplicacion Android educativa para ninos y ninas de 8 a 12 anos.

ExploraVida no ensena a memorizar organos. Ensena una idea: **los sistemas de un ser vivo trabajan juntos**.
El nino cuida a *Vita*, un organismo ilustrado que come, respira, se mueve y reacciona, y descubre por que
ninguna parte funciona sola.

---

## Que es

| | |
|---|---|
| Nombre | ExploraVida |
| Paquete | `com.educalab.exploravida` |
| Version | 1.0.0 |
| Publico | 8 a 12 anos |
| Idioma | Espanol |
| Area | Ciencias naturales / seres vivos |
| Conexion | **No necesita internet en ningun momento** |
| Permisos | **Ninguno declarado en el manifiesto** |

## Personajes

- **Vita** — el organismo del laboratorio. No es una lamina de anatomia: es un ser vivo ilustrado con
  zonas tocables, dibujado por completo con Compose Canvas.
- **Nora** — la exploradora que propone retos. Habla poco y solo cuando aporta algo.

## Que puede hacer el nino

- **Explorar a Vita**: tocar 30 zonas repartidas en 6 sistemas y descubrir que hace cada una.
- **Seguir 12 experiencias**: recorridos animados donde una particula viaja de sistema en sistema.
- **Resolver 20 actividades** de seis tipos distintos: arrastrar, ordenar, conectar, comparar, predecir y observar.
- **Conectar sistemas**: arrastrar cables entre los seis sistemas; el motor decide si la ayuda existe de verdad.
- **Ejecutar simulaciones**: lanzar dos pruebas con barras de energia reales y decidir cual aguanta mas.
- **Coleccionar 15 pegatinas** en el cuaderno del explorador y **12 insignias** en la sala de insignias.

## Estructura del proyecto

```
app/                    codigo Kotlin y recursos
  src/main/java/...     data/ (Room), domain/ (motores), ui/ (Compose)
  src/test/java/...     101 pruebas unitarias
database/               schema.sql y sample_data.sql
docs/                   memoria, manuales, informe de compilacion
docs/pdf/               los tres documentos en PDF
.github/workflows/      compilacion automatica del APK
```

## Privacidad

No se pide nombre real, correo, telefono, direccion ni ubicacion. No hay camara, microfono, contactos,
cuentas, publicidad ni analitica. El unico dato guardado es el alias elegido de una lista y un numero de
avatar, y vive solo en el dispositivo, dentro de `exploravida.db`.
