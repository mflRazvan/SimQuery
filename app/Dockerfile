# Imagine de bază Python
FROM python:3.10-slim

# Setează directorul de lucru
WORKDIR /app

# Copiază toate fișierele proiectului în imagine
COPY . .

# Instalează dependențele (dacă există requirements.txt)
RUN pip install --no-cache-dir -r requirements.txt || true

# Specifică comanda implicită pentru a rula aplicația
CMD ["python", "app_flow.py"]
