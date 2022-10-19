# Vegan Test Project
RESTful Service for Vegan Solutions

## Deploy
You can deploy the project as Spring Boot application, or you can deploy it with Docker running the following commands

```
docker build -t {imageName}:{imageVersion} . 
docker run -d -p {containerPort}:{hostPost} {imageName}:{imageVersion}
```

## Documentation
Documentation was created with Open API 3.0, after deployment can be found at 
> {yourBaseUrl}/swagger-ui/index.html
