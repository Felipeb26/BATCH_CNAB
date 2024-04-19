# #!/bin/bash
clear

echo -e "\033[1;31m Starting Zipkin  \033[0m"
docker run -d --cpus="0.1" --name zipkin -p 9411:9411 openzipkin/zipkin

echo -e "\033[1;32m Starting RabbitMQ \033[0m"
docker run -d --cpus="0.1" --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3.13-management

echo -e "\033[1;34m Starting MSSQlServer 'SqlServer' \033[0m"
docker run -d --name sqlserver  --cpus="0.5" -e "ACCEPT_EULA=Y" -e "SA_PASSWORD=root@123" -p 1433:1433 mcr.microsoft.com/mssql/server:2019-latest

echo -e "\033[1;31m Starting SonarQube \033[0m"
docker run -d --name sonar --cpus="0.1" -p 9000:9000  -v sonarqube_extensions:/opt/sonarqube/extensions -d sonarqube:lts-community

for i in {5..0}; do
    sleep 1
    echo "Cleaning screen in $i sec"
done

clear
