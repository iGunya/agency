# Приложение агенство недвижимости

### Запуск модуля ageny-server с выбором транспорта
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