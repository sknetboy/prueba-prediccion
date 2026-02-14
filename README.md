# ChurnInsight MVP

Plataforma de predicción de churn con arquitectura hexagonal en tres capas: DS (FastAPI + scikit-learn), Backend (Spring Boot) y Frontend (React + Vite).

## Arquitectura
- **DS service**: dominio (`Cliente`), caso de uso (`PredecirChurnUseCase`), repositorio de modelo y endpoint `/predict`.
- **Backend**: puertos (`ChurnPredictionPort`, `PrediccionRepositoryPort`), casos de uso (`Predecir`, `ObtenerEstadisticas`) y REST `/api/v1`.
- **Frontend**: arquitectura por features, hooks personalizados y capa Axios.

## Contrato de integración
Se define en `integration-contract.json` y se comparte entre equipos.

## Matriz de versiones objetivo (tu entorno local)
Configurado para tu setup de Windows 11:
- **Java**: Oracle/OpenJDK **21.0.8 LTS**
- **Maven**: **3.9.12**
- **Python**: **3.14.2**
- **Node.js**: **20.17.0**
- **npm**: **10.8.2**

> Nota: el backend ahora está configurado con `java.version=21` en `pom.xml`.

## Checklist de instalación por sistema operativo

### Ubuntu 22.04 / 24.04
- [ ] Actualizar índices de paquetes:
  ```bash
  sudo apt update
  ```
- [ ] Instalar Java 17, Python 3.11, pip y utilidades:
  ```bash
  sudo apt install -y openjdk-17-jdk python3.11 python3.11-venv python3-pip curl git
  ```
- [ ] Instalar Node.js 20.17.0 con nvm:
  ```bash
  curl -fsSL https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.7/install.sh | bash
  source ~/.nvm/nvm.sh
  nvm install 20.17.0
  nvm use 20.17.0
  npm i -g npm@10.8.2
  ```
- [ ] Instalar Maven 3.9.9 (opción recomendada: wrapper o paquete del sistema si cumple versión).
- [ ] Verificar versiones:
  ```bash
  java -version
  python3.11 --version
  node -v
  npm -v
  mvn -v
  ```

### Windows 11 (recomendado para tu caso)
- [ ] Instalar **Oracle/OpenJDK 21.0.8 LTS** y validar `JAVA_HOME`.
- [ ] Instalar **Apache Maven 3.9.12** y validar `MAVEN_HOME` + `PATH`.
- [ ] Instalar **Python 3.14.2** (activar “Add Python to PATH”).
- [ ] Instalar **NVM for Windows** y luego Node 20.17.0:
  ```powershell
  nvm install 20.17.0
  nvm use 20.17.0
  npm i -g npm@10.8.2
  ```
- [ ] Instalar **Maven 3.9.12** y configurar `MAVEN_HOME` + `PATH`.
- [ ] Verificar versiones:
  ```powershell
  java -version
  python --version
  node -v
  npm -v
  mvn -v
  ```
- [ ] Salida esperada mínima:
  - `java --version` -> `21.0.8 LTS`
  - `mvn -v` -> `Apache Maven 3.9.12`
  - `python --version` -> `Python 3.14.2`

### macOS (Ventura/Sonoma)
- [ ] Instalar Homebrew (si no existe).
- [ ] Instalar Java 17, Python 3.11 y Maven:
  ```bash
  brew update
  brew install openjdk@17 python@3.11 maven
  ```
- [ ] Usar Node 20.17.0 con nvm:
  ```bash
  curl -fsSL https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.7/install.sh | bash
  source ~/.nvm/nvm.sh
  nvm install 20.17.0
  nvm use 20.17.0
  npm i -g npm@10.8.2
  ```
- [ ] Verificar versiones:
  ```bash
  java -version
  python3.11 --version
  node -v
  npm -v
  mvn -v
  ```

## Ejecución local
1. DS service:
   ```bash
   cd ds-service
   python -m venv .venv
   source .venv/bin/activate  # En Windows: .venv\\Scripts\\activate
   python -m pip install --upgrade pip setuptools wheel
   pip install --prefer-binary -r requirements.txt
   uvicorn app.api.main:app --reload
   ```
2. Backend:
   ```bash
   cd backend && mvn spring-boot:run
   ```
3. Frontend:
   ```bash
   cd frontend && npm install && npm run dev
   ```

## Ejecución local en Windows (PowerShell)
```powershell
# 1) DS service
cd ds-service
python -m venv .venv
.\.venv\Scripts\Activate.ps1
python -m pip install --upgrade pip setuptools wheel
pip install --prefer-binary -r requirements.txt
uvicorn app.api.main:app --host 0.0.0.0 --port 8000

# 2) Backend (otra consola)
cd backend
mvn spring-boot:run

# 3) Frontend (otra consola)
cd frontend
npm install
npm run dev
```


## Solución de errores comunes (Python 3.14 en Windows)
Si ves errores como `Failed to build pandas` o `ConnectionResetError(10054)` al instalar dependencias:

1. Fuerza instalación por ruedas binarias y aumenta reintentos:
   ```powershell
   pip install --upgrade pip setuptools wheel
   pip config set global.timeout 120
   pip config set global.retries 10
   pip install --prefer-binary -r requirements.txt
   ```
2. Si tu red corporativa corta descargas, usa mirror de PyPI:
   ```powershell
   pip install -r requirements.txt -i https://pypi.org/simple
   ```
3. Este proyecto ya no requiere `pandas` para entrenar el modelo DS, reduciendo compilaciones pesadas en Windows/Python 3.14.

## Docker Compose
```bash
docker compose up --build
```

## Ejemplos curl
```bash
curl -X POST http://localhost:8000/predict -H 'Content-Type: application/json' -d '{"tiempo_contrato_meses":12,"retrasos_pago":2,"uso_mensual":14.5,"plan":"Premium"}'

curl -X POST http://localhost:8080/api/v1/predict -H 'Content-Type: application/json' -d '{"tiempoContratoMeses":12,"retrasosPago":2,"usoMensual":14.5,"plan":"Premium"}'

curl http://localhost:8080/api/v1/stats
```
