Microservices Test Project
--------------------------

**prerequisites** 

* install Docker 1.12.x (Linux version or Docker for Mac/Windows)
* include and configure "lagom-service-locator-consul" Lagom-Consul integration (in each Lagom services)


## Project Setup

1. Build Java Docker image

        cd 1-java8
        ./build.sh
        cd ..

1. Build Service Docker images

        cd 2-service
        ./build.sh
        cd ..

1. Build Load-Balancer

        cd 3-deploy
        docker-compose build

1. Start and configure the Service-Locator/Load-Balancer

        # start the do ker stack
        docker-compose up -d
        # give consul services description
        ./add-consul-config.sh

        # to see logs
        docker-compose logs -f 


## Check config 

**Service-Locator** Status page is available here: [http://localhost:8500](http://localhost:8500/ui/#/dc1/nodes/consul)

**Load-Balancer** Statistics page is available here: [http://localhost:1936](http://admin:admin@localhost:1936) _(username : admin / password : admin)_


## Test

1. Test services

        curl localhost:9001/v1/hello

        curl localhost:9002/v1/greeting

1. Get metrics

        curl http://localhost:9001/_status/circuit-breaker/current

        curl http://localhost:9002/_status/circuit-breaker/current

1. Scale services

        # start a new hello service instance
        docker-compose scale backend-hello=2

        # start a new greeting service instance
        docker-compose scale backend-greeting=2

1. Load balancing

        # check load-balancing (1st level)
        curl localhost:9001/v1/hello
        {"hostname":"backend-hello","ip":"172.27.0.2","time":1481625558937}
        
        curl localhost:9001/v1/hello
        {"hostname":"backend-hello","ip":"172.27.0.7","time":1481625560678}

        # check load-balancing (2nd level)
        for i in {1..4}; do curl localhost:9002/v1/greeting; done
        
        {"hostname":"backend-greeting","ip":"172.27.0.4","remoteHostname":"backend-hello","remoteIp":"172.27.0.7","remoteTime":1481625777232}
        {"hostname":"backend-greeting","ip":"172.27.0.8","remoteHostname":"backend-hello","remoteIp":"172.27.0.7","remoteTime":1481625777314}
        {"hostname":"backend-greeting","ip":"172.27.0.4","remoteHostname":"backend-hello","remoteIp":"172.27.0.2","remoteTime":1481625777344}
        {"hostname":"backend-greeting","ip":"172.27.0.8","remoteHostname":"backend-hello","remoteIp":"172.27.0.2","remoteTime":1481625777446}


1. Stop all

        docker-compose stop
