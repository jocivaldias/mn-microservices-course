## MN Pricing

Features:
* Kafka Produce / Consume
* GraalVM

#### Graal
1. `./gradlew clean assemble # or ./mvnw clean package if using Maven`
2. `docker build . -t mn-pricing`

As Kafka runs on the host machine the stack must be attached to the correct host network, so the access to kafka works.
3. `docker stack deploy -c app.stack.yml mn-pricing-stack`


## Feature kafka documentation

- [Micronaut Kafka Messaging documentation](https://micronaut-projects.github.io/micronaut-kafka/latest/guide/index.html)

## Feature http-client documentation

- [Micronaut Micronaut HTTP Client documentation](https://docs.micronaut.io/latest/guide/index.html#httpClient)

