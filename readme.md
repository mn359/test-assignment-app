# Описание
Приложение для получения и сохранения курсов EUR, USD, внесения транзакций, и расчете транзакций в заданной валюте.

Написано на Java 17.

# Начало

Запустить тесты:

`.\mvnw clean test`

Запустить приложение:

`.\mvnw spring-boot:run`

## Тестирование вручную

Понадобится REST API клиент (например Thunder Client, Postman, и т.д) для отправки  HTTP запросов.

#### 1. Отправить запрос для получния и сохранения USD и EUR курсов

- POST
- http://localhost:8080/api/v1/exchange_rate/update
- Ожидаемый ответ: 200 (Accepted)


#### 2. Отправить транзакцию
(указать текущюю дату)
- POST запрос
- utl: http://localhost:8080/api/v1/transaction
- тело запроса: 
  ```
  {
    "date": "2023-11-27",
    "description": "test",
    "value": "200.5"
  } 

- Ожидаемый ответ: 200 (Accepted)

#### 3. Отправить запрос на транзакции за заданное период и в заданной валюте:

- GET 
- http://localhost:8080/api/v1/transaction
- ```
  {
    "from": "2023-11-01",
    "to": "2023-12-31",
    "currency": "USD"
  }
- Примерный ожидаемый ответ:
- ```
  [
    {
      "date": "2023-11-27",
      "description": "test",
      "value": "2.1970917299"
    }
  ]

# Прочее

- Запланированное время для получения и сохранения курсов USD и EUR можно изменить в application.properties.
- Swagger-ui: http://localhost:8080/swagger-ui.html
