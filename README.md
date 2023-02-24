# reactive-context-payload

## You can read further here
[Overcoming Challenges in Quarkus RESTEasy Reactive](https://medium.com/@jchristou/de0fe935d8e3)

## Packaging and running the application

The application can be packaged using:
```shell script
mvn clean package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
mvn quarkus:dev
```
> **_NOTE:_**  In order to execute the service locally, you need to mock the request to the AuthorizeClient. One way to do this is to import to your Mockoon the following json:
>
> {
"uuid": "76b07da0-55a5-4b8d-9053-a228bab3ce7e",
"lastMigration": 24,
"name": "Reactive Context Payload",
"endpointPrefix": "",
"latency": 0,
"port": 8085,
"hostname": "0.0.0.0",
"routes": [
{
"uuid": "6b5bdebe-b759-437f-a70b-52440abb2050",
"documentation": "",
"method": "get",
"endpoint": "authorize",
"responses": [
{
"uuid": "b6026bed-8973-4872-9f9b-b97f5e46553e",
"body": "This is my username",
"latency": 0,
"statusCode": 200,
"label": "",
"headers": [],
"bodyType": "INLINE",
"filePath": "",
"databucketID": "",
"sendFileAsBody": false,
"rules": [],
"rulesOperator": "OR",
"disableTemplating": false,
"fallbackTo404": false,
"default": true
}
],
"enabled": true,
"responseMode": null
}
],
"proxyMode": false,
"proxyHost": "",
"proxyRemovePrefix": false,
"tlsOptions": {
"enabled": false,
"type": "CERT",
"pfxPath": "",
"certPath": "",
"keyPath": "",
"caPath": "",
"passphrase": ""
},
"cors": true,
"headers": [
{
"key": "Content-Type",
"value": "application/json"
}
],
"proxyReqHeaders": [
{
"key": "",
"value": ""
}
],
"proxyResHeaders": [
{
"key": "",
"value": ""
}
],
"data": []
}

The application is up, and we are ready to trigger it

If you want to inspect the approach where we try to read the HTTP payload inside jax-rs filters and the request hangs, please trigger this curl:
```shell script
curl --location 'http://localhost:8080/example/filter' \
--header 'Content-Type: application/json' \
--data '{
    "phoneNumber": "6900000000"
}'
```

If you want to inspect the final solution with the interceptor, please try it out with this curl:
```shell script
curl --location 'http://localhost:8080/example/interceptor' \
--header 'Content-Type: application/json' \
--data '{
    "phoneNumber": "6900000000"
}'
```
