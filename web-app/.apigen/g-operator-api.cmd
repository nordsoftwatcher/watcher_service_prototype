java -jar openapi-generator-cli.jar generate ^
    -c ./apigen-config.json ^
    -i http://192.168.88.33:8580/siwatch_operator_api/v2/api-docs ^
    -g typescript-fetch ^
    -o ../src/api/operator-api