### Запуск докер файла
#### В файле docker.yml установить путь монтирования (volume) бд
Для запуска с транспортом kafka:
```
docker-compose -f docker.yml --env-file kafka.env  --profile kafka up -d
```
Для запуска с транспортом artemis:
```
docker-compose -f docker.yml --env-file jms.env  --profile jms up -d
```
####Основное приложение отображается на порт 2203
####Просмотр лога на порт 2206
### Запуск модуля agecy-server с выбором транспорта
По дефолту выбран jms, но можно явно указать:
```
mvn -q spring-boot:run -Dspring-boot.run.arguments="--app.transport=jmsProducer"
```
Выбор Kafka:
```
mvn -q spring-boot:run -Dspring-boot.run.arguments="--app.transport=kafkaProducer"
```
### Запуск топика Kafka
```
--create --zookeeper localhost:2181 --replication-factor 1 --partitions 2 --topic MoveUsers
--create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic SaveLog
```