#!/bin/bash

# Скрипт для изменения количества партиций в топике Kafka.
# Рекомендуется использовать для запуска на том же хосте, где работает Docker контейнер с Kafka.

# Использование:
# 1. Убедитесь, что порт Kafka проброшен наружу и доступен с хоста (например, 29092).
# 2. Запустите скрипт с аргументами: ./add-partitions.sh <topic-name> <number-of-partitions>.

# Проверяем наличие аргументов
if [ "$#" -ne 2 ]; then
    echo "Использование: $0 <topic-name> <number-of-partitions>"
    exit 1
fi

# Название топика и количество партиций
TOPIC_NAME=$1
PARTITIONS=$2

# Добавляем партиции в топик
# Параметр --bootstrap-server указывает на порт Kafka, проброшенный контейнером наружу.
kafka-topics --alter --topic "$TOPIC_NAME" --partitions "$PARTITIONS" --bootstrap-server kafka:29092

# Проверка успешности выполнения команды
if [ $? -eq 0 ]; then
    echo "Количество партиций в топике '$TOPIC_NAME' успешно изменено на $PARTITIONS"
else
    echo "Ошибка при добавлении партиций в топик '$TOPIC_NAME'"
fi

# Рекомендации по запуску для Docker контейнера:
# - Убедитесь, что контейнер Kafka доступен через порт 29092 (или другой, проброшенный наружу).
# - Если требуется выполнить скрипт внутри контейнера, используйте:
#   docker exec -it <kafka-container-name> bash -c "/path/to/add-partitions.sh <topic-name> <number-of-partitions>"
