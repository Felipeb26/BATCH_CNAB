# MYSQL CONFIGURATIONS

### Devem ser utilizadas após iniciar a aplicação mysql no docker

    mysql -u root -p "senha root"
    update mysql.user set host='%' where user ='root';
    flush privileges;
    exit