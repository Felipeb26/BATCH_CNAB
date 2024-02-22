# SQLSERVER CONFIGURATIONS

### Devem ser utilizadas após iniciar a aplicação sqlserver no docker

    docker run --name sqlserver  -e "ACCEPT_EULA=Y" -e "SA_PASSWORD=root@123" -p 1433:1433 -d mcr.microsoft.com/mssql/server:2019-latest  