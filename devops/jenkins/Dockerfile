FROM jenkins/jenkins:lts-jdk17

USER root

# ═══════════════════════════════════════════════════════════
# ÉTAPE 1 : Installer Maven + outils de base
# ═══════════════════════════════════════════════════════════
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
        apt-transport-https \
        ca-certificates \
        curl \
        gnupg \
        lsb-release \
        maven && \
    rm -rf /var/lib/apt/lists/*

# ═══════════════════════════════════════════════════════════
# ÉTAPE 2 : Ajouter le dépôt Docker officiel
# ═══════════════════════════════════════════════════════════
RUN mkdir -p /etc/apt/keyrings && \
    curl -fsSL https://download.docker.com/linux/debian/gpg | \
    gpg --dearmor -o /etc/apt/keyrings/docker.gpg && \
    echo "deb [arch=amd64 signed-by=/etc/apt/keyrings/docker.gpg] \
        https://download.docker.com/linux/debian bookworm stable" | \
    tee /etc/apt/sources.list.d/docker.list > /dev/null

# ═══════════════════════════════════════════════════════════
# ÉTAPE 3 : Installer Docker CLI
# ═══════════════════════════════════════════════════════════
RUN apt-get update && \
    apt-get install -y --no-install-recommends docker-ce-cli && \
    rm -rf /var/lib/apt/lists/*

# ═══════════════════════════════════════════════════════════
# ÉTAPE 4 : Vérifier
# ═══════════════════════════════════════════════════════════
RUN echo "=== Maven ===" && mvn --version && \
    echo "=== Docker ===" && docker --version

USER jenkins