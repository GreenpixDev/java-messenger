# Spring Messenger
## Структура модулей
**Микросервисы:**
* messenger-gateway - прокси сервер
* messenger-user - микросервис пользователей и авторизации
* messenger-friends - микросервис друзей
* messenger-chat - микросервис чатов
* messenger-notifications - микросервис уведомлений
* messenger-file-storage - микросервис файлового хранилища

**Вспомогательные свои spring boot starter:**
* messenger-boot-jwt - модуль для подключения JWT конфигурации
* messenger-boot-integration - модуль для подключение защиты по API-KEY для интеграционных методов
* messenger-boot-common - базовый модуль с настройкой JWT и интеграций. Содержит много общих классов: DTO, Exception'ы и прочее.
* messenger-amqp-producer - модуль с конфигурациями MQRabbit для отправки сообщений сервису уведомлений

**Вспомогательные свои библиотеки:**
* messenger-amqp-common - базовые DTO для передачи через AMQP и некоторые интерфейсы сервисов

## Install
* Поднимите окружение `docker-compose up .`
* Запустите все микросервисы (Dockerfile не подготовил, поэтому придётся вручную)
* Если возникнут проблемы с docker-compose, можно попробовать поднять отдельно все `docker-compose.local.yaml` внутри модулей

## Что реализовано?
**Тестирование**:
* Покрытие сервисов unit тестами на 80%
* Полное покрытие api тестами сервиса messenger-user
* Частичное покрытие api тестами сервисов messenger-friends и messenger-chat

**Логгирование**
* Везде добавлено логгирование на разных уровнях (ERROR / INFO / DEBUG / TRACE)

**JavaDoc**
* Покрыта большая часть кода JavaDoc

## Остальное?
Остальное расписать не успел