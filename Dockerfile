ARG TAG=23.0.6

FROM curlimages/curl AS DOWNLOADER

WORKDIR /app

RUN curl -H "Accept: application/zip" https://github.com/vymalo/keycloak-webhook/releases/download/v0.5.0/keycloak-webhook-0.5.0-all.jar -o /app/keycloak-webhook.jar -Li

FROM quay.io/keycloak/keycloak:${TAG}

ENV KEYCLOAK_DIR /opt/keycloak
ENV KC_PROXY edge

USER 0

COPY --from=DOWNLOADER /app/keycloak-webhook.jar /opt/keycloak/providers/

RUN $KEYCLOAK_DIR/bin/kc.sh build

USER 1000