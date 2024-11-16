
# Проект: Система обработки заказов с использованием Apache Kafka

## Описание изменений для реализации многопоточной обработки и балансировки нагрузки

В рамках проекта была улучшена система обработки заказов с использованием Apache Kafka. Внесенные изменения касаются оптимизации производительности за счет внедрения механизма разделения на несколько партиций, многопоточной обработки сообщений и автоматической балансировки нагрузки.

### Основные изменения

1. **Добавление партиций для топиков Kafka**:
   - Для улучшения производительности было реализовано разделение темы Kafka на несколько партиций. Это позволяет обрабатывать данные о заказах параллельно и увеличивает общую производительность системы при высоких нагрузках.
   - Количество партиций для каждого топика можно настроить с помощью параметра конфигурации.

2. **Многопоточная обработка сообщений**:
   - Внесены изменения в потребитель Kafka, чтобы обеспечить многопоточную обработку сообщений. Используется параметр `concurrency` в аннотации `@KafkaListener`, что позволяет запустить несколько потоков для обработки сообщений из различных партиций.
   - Механизм повторной попытки обработки сообщений (с помощью аннотации `@Retryable`) позволяет избежать потери данных при возникновении ошибок в процессе обработки.

3. **Автоматическая балансировка нагрузки**:
   - Каждому потоку в рамках многопоточной обработки назначаются сообщения из различных партиций автоматически с использованием стратегии round-robin. Это обеспечивает равномерное распределение нагрузки между потоками и партициями.

### Скрипт для создания и изменения партиций в Kafka

Для автоматизации процесса изменения количества партиций в топиках Kafka был добавлен скрипт `add-partitions.sh`. Скрипт позволяет легко изменять количество партиций в топиках Kafka, что полезно для масштабирования системы.
 - Однако уменьшить количество партиций невозможно в Apache Kafka. Это ограничение существует, потому что при уменьшении количества партиций могут возникнуть проблемы с потерей данных, а также с обеспечением последовательности сообщений.

Пример использования скрипта:
```bash
./add-partitions.sh <topic-name> <number-of-partitions>
```

### Docker Compose для настройки Kafka и Zookeeper

Добавлены параметры в `docker-compose.yml` для настройки Kafka и Zookeeper с поддержкой нескольких партиций:

```yaml
version: "3.9"
services:
  zookeeper:
    image: confluentinc/cp-zookeeper:latest
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
      ZOOKEEPER_TICK_TIME: 2000
    ports:
      - 22181:2181

  kafka:
    image: confluentinc/cp-kafka:latest
    depends_on:
      - zookeeper
    ports:
      - 29092:29092
    hostname: kafka
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://host.docker.internal:29092
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT
      KAFKA_INTER_BROKER_LISTENER_NAME: PLAINTEXT
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
      KAFKA_LISTENER_PORT: 29092

  create-topic:
    image: confluentinc/cp-kafka:latest
    depends_on:
      - kafka
    command: >
      sh -c "
      while ! nc -z kafka 29092; do sleep 1; done;
      kafka-topics --create --topic new_orders --partitions 4 --replication-factor 1 --if-not-exists --bootstrap-server kafka:29092 && 
      kafka-topics --create --topic payed_orders --partitions 4 --replication-factor 1 --if-not-exists --bootstrap-server kafka:29092 && 
      kafka-topics --create --topic sent_orders --partitions 4 --replication-factor 1 --if-not-exists --bootstrap-server kafka:29092"
```

### Реализация многопоточной обработки в сервисе Kafka

В сервисе `KafkaListenerService` была добавлена поддержка многопоточной обработки сообщений с использованием аннотации `@KafkaListener`. Конфигурация `concurrency` позволяет настроить количество потоков для обработки сообщений из различных партиций:

```java
@Service
public class KafkaListenerService {

    private final OrderRepository orderRepository;

    @Autowired
    public KafkaListenerService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Retryable(
            retryFor = {Exception.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    @KafkaListener(
            topics = "${spring.kafka.consumer.topic}",
            groupId = "${spring.kafka.consumer.group-id}",
            concurrency = "${spring.kafka.listener.concurrency}"  // Число потоков для обработки сообщений
    )
    public void listenNewOrder(String message, Acknowledgment acknowledgment) throws Exception {
        // Десериализуем сообщение в DTO
        OrderDTO orderDTO = new ObjectMapper().readValue(message, OrderDTO.class);

        // Проверяем, существует ли уже заказ с таким идентификатором
        if (orderRepository.existsById(orderDTO.getOrderId())) {
            acknowledgment.acknowledge();
            return;
        }

        // Преобразуем DTO в сущность Order и сохраняем его в базу данных
        Order order = orderDTO.toOrder();
        orderRepository.save(order);

        acknowledgment.acknowledge();
    }
}
```

### Заключение

В результате этих изменений была значительно улучшена производительность системы обработки заказов за счет параллельной обработки сообщений и автоматической балансировки нагрузки. Система теперь может эффективно масштабироваться, обрабатывая большое количество заказов и сообщений без потери данных и с минимальной задержкой.

Для дальнейшего улучшения рекомендуется:
- Реализовать более детальный мониторинг системы с использованием таких инструментов, как Prometheus и Grafana.
- Добавить аутентификацию и авторизацию для более безопасного взаимодействия с сервисами.

