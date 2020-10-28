# utm: Website Real-Time Monitoring Tool

## Technology stack

    Java 8
    Spring Boot 2.3.4
    Maven (to build)
    Embedded H2 (in memory database)
    Embedded Tomcat server
    
## Tools

    Postman (used to test APIs)
    
    
## Getting started

    $ cd uptime-monitor

    $ docker-compose build

    $ docker-compose up
    
## Database
    
    Once application is started, all monitoring statistics can be checked under -
    
    http://localhost:9090/utmdb
    
    You will see 2 tables -
    
        WEB_CHECK - Shows all the websites that tool is monitoring
        MONITOR - Shows stats for each website like, response time, status (down/up) etc.

## Call the APIs

    Postman or any other tool can be used to hit the APIs.
    
    By default, application will start at port 9090.
    
    All below requests will be prefixed with 'http://localhost:9090'


### POST /utm/webcheck/create

To create a webcheck for any website, say google.com

    POST: http://localhost:9090/utm/webcheck/create
    
    Payload:
        {
            "name": "Google",
            "url": "http://google.com",
            "frequency": 1,
            "unit": "M"
        }

### GET /utm/webchecks

To get all the webchecks that tool in monitoring

    GET: http://localhost:9090/utm/webchecks


### GET /utm/webchecks/name/{nameSearchKey}

To search webchecks by name

    GET: http://localhost:9090/utm/webchecks/name/Google


### GET /utm/webchecks/interval/{frequency}/{unit}

To search webcheks by interval. 
Unit can be M (for minute) or H (for hour) and frequency be accordingly, based of unit.

    GET: http://localhost:9090/utm/webchecks/interval/5/M  (All the checks running on 5 minutes interval)
    GET: http://localhost:9090/utm/webchecks/interval/1/H  (All the checks running on 1 hour interval)
    

#### GET /utm/webcheck/status/{url}

To check the status of particulas website

    GET: http://localhost:9090/utm/webcheck/status/http://google.com
    

### POST /utm/webcheck/{checkId}/activate/{value}

To activate or deactivate particulas webcheck.
Value should be true to activate particular check and to deactivate value should be false.
    
    POST: http://localhost:9090/utm/webcheck/1/activate/false  (To deactivate with check ID 1)
    POST: http://localhost:9090/utm/webcheck/1/activate/true  (To activate with check ID 1)
    
    
    