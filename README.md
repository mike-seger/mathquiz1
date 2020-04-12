# mathquiz 1

An early (2012) experiment of a browser application (aka songle page
application) combined with a java backend. It uses XHTML, JSF and Javascript
for the frontend. The application is supposed to be kid friendly, usable on
a mobile device  and scales to any screen size.

![MathQuiz1 Game](doc/mathquiz1.png?raw=true "Screenshots")

## build
```
mvn clean package
```

## run
```
mvn jetty:run -Djetty.http.port=9999
```

## play
http://localhost:9999/
 
## run standalone
```
java -jar run/jetty-runner-*.jar --port 9999 target/mathquiz1.war
```
