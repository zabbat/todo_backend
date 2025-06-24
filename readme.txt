keytool -genkeypair -alias mysslkey -keyalg RSA -keysize 2048 -validity 3650 -storetype PKCS12 -keystore keystore.p12 -storepass devpass -keypass devpass -ext "SAN=IP:10.0.2.2"

keytool -export -alias mysslkey -keystore keystore.p12 -storetype PKCS12 -file devcert.cer



nginx cert:
openssl req -x509 -nodes -days 3650 -newkey rsa:2048 -keyout todo.key -out todo.crt -config openssl.cnf -extensions req_ext
openssl x509 -in todo.crt -outform DER -out todocert.cer
check: openssl x509 -in todo.crt -text -noout

pss: devpass

used for dev only

./gradlew clean bootJar  
docker-compose up --build
local:
docker-compose -f docker-compose.yml -f docker-compose.dev.yml up --build
prod:
docker-compose -f docker-compose.yml up --build -d
prod access
ssh -L 5432:localhost:5432 user@your-server-ip
Then in pgAdmin or DBeaver, connect to:

Host: localhost

Port: 5432

On Azure:
Go to your Virtual Machine or Container App in the Azure Portal.

Under "Networking", check Inbound port rules.

Make sure 5432 is NOT listed as allowed.

If it is, delete or restrict it to internal IPs only.

On AWS (EC2):
Open your Security Group settings.

Make sure there's no rule allowing 5432 from 0.0.0.0/0 (anyone).

Instead of opening 5432, just:

Open port 22 (SSH) in your firewall.

Then use SSH tunneling to access PostgreSQL (more secure).

Example:
ssh -L 5432:localhost:5432 user@your-server-ip
Now, locally on your machine, you can connect pgAdmin or DBeaver to:
Host: localhost
Port: 5432


for login:
https://www.youtube.com/watch?v=X7pGCmVxx10&ab_channel=AlanLands
https://spring.io/guides/gs/securing-web


# Todo Spring Boot Backend

Backend for the Android Todo app.
- Kotlin
- Spring Boot
- Firebase Auth (with JWT)
- Dev DB: H2
- Prod DB: PostgreSQL (via Docker)

## Dev Setup
./gradlew bootRunDev

## Production
./gradlew bootRunProd

## Tests
./gradlew test
