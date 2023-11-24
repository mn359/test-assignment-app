# Описание
Приложение для получения и сохранения курсов EUR, USD, внесения транзакций, и расчете транзакций в заданной валюте.
# Начало

Запустить тесты:

`./mvnw clean test`

Запустить приложение:

`./mvnw spring-boot:run`

## Тестирование вручную

### Пример #1
#### 1. Отправить транзакцию
- POST запрос
- utl: http://localhost:8080/api/v1/transaction
- тело запроса: 
  ```
  {
    "date": "2023-11-15",
    "description": "test",
    "value": "200.5"
  } 

- Ожидаемый ответ: 200 (Accepted)

#### 2. Отправить запрос на транзакции за заданное период и в заданной валюте:

- GET 
- http://localhost:8080/api/v1/transaction
- ```
  {
    "from": "2023-11-01",
    "to": "2023-11-16",
    "currency": "USD"
  }
- Ожидаемый ответ:
- ```
  [
    {
      "date": "2023-11-15",
      "description": "test",
      "value": "2.1970917299"
    }
  ]

### Пример #2

#### Отправка запроса для получния и сохранения USD и EUR курсов:

- POST
- http://localhost:8080/api/v1/exchange_rate/update
- Ожидаемый ответ: 200 (Accepted)

#Прочее

- Запланированное время для получения и сохранения курсов USD и EUR можно изменить в application.properties.
- Swagger-ui: http://localhost:8080/swagger-ui.html
