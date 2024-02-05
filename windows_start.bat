@echo off

call docker-compose down

call docker build -t batch-cnab  .

call docker-compose up --build --force-recreate --remove-orphans