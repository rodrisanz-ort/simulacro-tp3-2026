# simulacro-tp3-2026

## API de frases

La pantalla de frases consulta `https://api.api-ninjas.com/v2/randomquotes`.
Para habilitar la carga localmente, copiá `.env.example` como `.env` y agregá tu clave:

```dotenv
API_NINJAS_KEY=tu_clave
```

La clave se envía como `X-Api-Key`. El archivo `.env` está ignorado por Git y no debe subirse.
También se acepta `API_NINJAS_KEY` en `local.properties`.