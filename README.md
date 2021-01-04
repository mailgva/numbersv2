# The web-server that provides interesting facts about numbers.
Requirements:
1. Server must accept an integer as input.
2. Server must use external Numbers service http://numbersapi.com/ to retrieve facts about accepted number.
3. Provide API to submit a number and retrieve result (text info about the number). Recently requested numbers should be stored in cache.
4. Provide metrics API to retrieve the following usage information: 10 most popular numbers (the ones that get requested most often), average latency of the Numbers service, average success rate of the Numbers service.
   Ideal solution would use Spring, Postgres and provide basic documentation.

# Application realized as web-server using REST (JSON). 
Technologies:
- Java 11
- Gradle  
- Spring boot 2.4.1
- PostgreSQL 42.2.18

App not contains:
- tests
- docker image

# Tutorial
1. Get from git: **git clone https://github.com/mailgva/numbersv2**
2. You need installed **PostgreSQL** (default port) and database called **numbers**
   - login **user**
   - password **password**
3. Start app: execute  **gradlew bootRun**.  
4. Enjoy app :)

#Docker way
1. Get from git: **git clone https://github.com/mailgva/numbersv2**
2. cd numbersv2
3. run **docker-compose up --build**

# Available URLs:
1. **/api/v1/fact/{number}** - to get number fact 
2. **/api/v1/fact/{number}/{type}** - to get number fact by type ("trivia", "math", "date", "year")
3. **/api/v1/cachefact/{number}** - to get number fact with using cache
4. **/api/v1/cachefact/{number}/{type}** - to get number fact by type using cache ("trivia", "math", "date", "year")
5. **/api/v1/mostpopular/** - to get 10 most popular numbers
6. **/api/v1/avglatency/** - to get average average latency of the Numbers service
7. **/api/v1/successrate/** - to get average success rate of the Numbers service

# Example:
1. curl http://localhost:8080/numbers/api/v1/fact/11 -H "Accept: application/json"
	* result:{"fact":"11 is the number of incarnations of The Doctor in BBC sci-fi series Doctor Who."}
2. curl http://localhost:8080/numbers/api/v1/fact/1970/year -H "Accept: application/json"
	* result:{"fact":"1970 is the year that U.S. President Richard Nixon signs into law the Occupational Safety and Health Act on December 29th."}
3. curl http://localhost:8080/numbers/api/v1/mostpopular/ -H "Accept: application/json"
	* result:{"mostpopular":{"1":2,"2":1,"1970":1,"5":2,"7":1,"9":2,"11":1}}
4. curl http://localhost:8080/numbers/api/v1/avglatency/ -H "Accept: application/json"
	* result:{"avglatency":"147.4666666666666667"}
5. curl http://localhost:8080/numbers/api/v1/successrate/ -H "Accept: application/json"
	* result:{"successrate":"100.0000000000000000"}
