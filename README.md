# Shipping-Rate-Service Application - Documentation

Shipping-rate-service application is a Spring Boot backend REST API application that calculates and fetch shipping rates from various shipping providers. Currently the application only supports City-Link Express implementations.
This application also use Redis for caching purposes with JWT-based mechanism integrated.


## Requirements to run
* Java 17
* Maven 3
* Redis
* Postman (for testing)


## Steps to run using local
1. After having pulled this repository to your local, open the project using your desired IDE
2. Have your Redis started up using **redis-server** command in the terminal of your choice to start it up
![image](https://github.com/user-attachments/assets/29eae493-c040-40cd-872d-56eb9862f1e6)

3. After opening the project, for example in Intellij, head over to edit configurations
![image](https://github.com/user-attachments/assets/c996ca7b-35ff-46c4-a590-8578b0958990)

4. Add a run configuration using the main class com.my.shippingrate.ShippingRateServiceApplication using Java 17 accordingly. Add `-Dspring.profiles.active=local` in the VM options.\
![image](https://github.com/user-attachments/assets/565dbd49-486b-4235-a7db-fdefdf84ce89)

5. Open up settings and go to **Build, Execution and Deployment -> Compiler -> Annotation Processors** and enable annotation processing for the project
![image](https://github.com/user-attachments/assets/62c2b3f1-09ce-463e-ada6-1b7d925b4327)

6. Click Run project

7. Inspect the logs that the application has been started up successfully with Redis as well
![image](https://github.com/user-attachments/assets/0389688a-8cf8-4616-917a-60f8bef3b27f)

>[!NOTE]
>Ensure that Redis properties is configured correctly in application-local.properties if you are running Redis in local\
>Example:\
>`spring.data.redis.host=localhost`\
>`spring.data.redis.port=6379`


## Steps to run using Docker
This section requires Docker to be installed and running in your local
1. Open with a terminal of your choice at project root folder as the Dockerfile and docker-compose.yml are located in the there
2. Run the command `docker-compose up --build`
![image](https://github.com/user-attachments/assets/24ffdfc8-a79d-48db-bd37-11fd0f711c5d)

3. Inspect whether application is starting up in the Docker container with app and redis image have also been created
![image](https://github.com/user-attachments/assets/00d87112-8a34-4c29-b393-640154ca5b15)

>[!NOTE]
>Ensure that all properties is properly configured correctly in application.properties before running `docker-compose up --build`\
>For example, user credentials have been injected in the application.properties correctly


## API documentation
Once you are able to run the application in your local or docker, depending on which port that you are running the app, you may visit Swagger UI API documentation at localhost:${YOUR_PORT}/swagger-ui/index.html#/

### API testing
1. To start testing the API, we would need to authenticate ourself first by login through login API by using Postman. Login credentials can be found in application.properties as this application injects the required login credentials to in-memory user details.\
   Once user has been logged in, a bearer token will be generated, this token will be use to authorize user to access other APIs. Example response after login:
   ![image](https://github.com/user-attachments/assets/76af2378-f5f8-4446-9d92-c7d34ec7d90c)

2. Use the bearer token that was generated to access other resources. For example, use the bearer token to access shipping-rate API
![image](https://github.com/user-attachments/assets/012d8886-e319-4e41-b71a-1792e5b29d45)

### Shipping-rate API testing
1. After being able to generate the token, we would be able to access the shipping-rate API. We may refer the Swagger documentation to further analyze the required fields to construct the request body for each type for shipping provider.

> [!IMPORTANT]
> For every request made to shipping rate API, we are required to provide the type of shipping provider in `provider` parameter in the request body.
   

