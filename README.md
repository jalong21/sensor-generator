# Get Started With Docker

- install Docker Desktop 
- Set up network for container-to-container communication:
  - ```docker network create redis_server --driver bridge```
- run redis connected to previously made network and listening to default port. "redis-stack" is the name of server on bridge. "-p 8001:8001" is the port for redis insights, which you can browse to in the browser.
  - ```docker run -d --name redis-stack -p 0.0.0.0:6379:6379 -p 8001:8001 --network redis_server redis/redis-stack:latest```
- run this SBT project in docker (sbt has built in docker plugin)
  - ```sbt docker:publishLocal```
  - ```docker run --rm -p 9000:9000 --network redis_server toptal-project:0.1```#   s c a l a - t e m p l a t e 
#   s e n s o r - g e n e r a t o r  
 