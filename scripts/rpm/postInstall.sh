#!/bin/bash

echo "===================== SETUP DATABASE ====================="

#sudo -u postgres psql -U postgres -c "drop database junidb"
sudo -u postgres psql -U postgres -tc "SELECT 1 FROM pg_user WHERE usename = 'juniterrien'" | grep -q 1 || sudo -u postgres psql -U postgres -c "CREATE USER juniterrien PASSWORD 'junipass';"
sudo -u postgres psql -U postgres -c "CREATE SCHEMA IF NOT EXISTS junidb;"
sudo -u postgres psql -U postgres -tc "SELECT 1 FROM pg_database WHERE datname = 'junidb'" | grep -q 1 || sudo -u postgres psql -U postgres -c "CREATE DATABASE junidb;"
sudo -u postgres psql -U postgres -c "GRANT ALL ON SCHEMA junidb TO juniterrien;"


echo "===================== CREATE THE SSL CERTIFICATE ====================="
keytool -delete -alias juniter -keystore keystore.p12
keytool -genkey -alias juniter -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore keystore.p12 -validity 3650

