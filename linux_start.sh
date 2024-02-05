docker-compose down

docker build -t batch-cnab  .

docker-compose up --build --force-recreate --remove-orphans