# Get Started With Docker

- install Docker Desktop 
- Set up network for container-to-container communication:
  - ```docker network create sensor_server --driver bridge```
  - ```docker network inspect sensor_server```
- run redis connected to previously made network and listening to default port. "redis-stack" is the name of server on bridge. "-p 8001:8001" is the port for redis insights, which you can browse to in the browser.
  - ```docker run -d --name redis-stack -p 0.0.0.0:6379:6379 -p 8001:8001 --network redis_server redis/redis-stack:latest```
  - This isn't used by this project atm
- run this SBT project in docker (sbt has built in docker plugin)
  - ```sbt docker:publishLocal```
  - ```docker run --rm -p 9000:9000 --network sensor_server sensor-generator```
- now that we have a dockerfile, we can do
  - ```docker build -t sensor-generator .```
  - ```docker run --rm -p 9001:9000 --network sensor_server --name sensor-generator sensor-generator```
  - This will listen on port 9001, connect to the sensor_server network, and name the containor sensor-generator
- Run this and the sensor-service, then hit the health endpoint and this will start posting sensor events to the sensor-service