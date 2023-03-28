# Rubric: Technology
**This rubric can be completed by Saturday the 25th.** 
- If you wish to do that as a team, please try to self reflect and rate yourself using the sections below. (Select the grade rating you believe you fit into and delete the rest)
- For each section, please also argument your choice with a small paragraph and some screenshots or links to Git commits.
- After that I will check how you completed and go through your codebase one more time. (**MY DECISION ON GRADES IS STILL FINAL**)
- If you do not wish to complete this rubric, you can still do that and I will complete it after Saturday. This means that each element of your codebase will be held to my own scrutiny.

#### The screenshots can be found here: https://imgur.com/a/XdP7mLG

### Dependency Injection

Application uses dependency injection to connect dependent components. No use of static fields in classes.

- *Good:* The application (client and server) uses dependency injection in multiple places to connect dependent components. The server makes use of Spring annotations to access path variables, parameters, and request bodies.

We use dependency injection for all of our scene controllers which have the @Inject annotation and we make use of path variables and request bodies annotations in our server controllers. Our use of dependency injection could be better, for example we are never using beans.


### Spring Boot

Application makes good use of the presented Spring built-in concepts to configure the server and maintain the lifecycle of the various server components.

- *Good:* The application contains example of @Controller, @RestController, and a JPA repository.

The application makes use of @RestController annotations for all of the server controllers and the database connection is guaranteed using the JPA repository. Our rating is just "good" because we removed all of the @Service annotations and service classes, because the app wasn't working as wanted.

### JavaFX

Application uses JavaFX for the client and makes good use of available features (use of buttons/images/lists/formatting/…). The connected JavaFX controllers are used with dependency injection.

- *Good:* The UI contains more than just buttons, text fields, or labels. The application contains images and a non-default layout.

We are using JavaFX for the client and all of the scenes have buttons, text fields and labels. We are also using custom elements for the list on the BoardView page and we are planning on adding images. The only place where we use dependency injection is in the CardListCellCtrl.


### Communication

Application uses communication via REST requests and Websockets. The code is leveraging the canonical Spring techniques for endpoints and websocket that have been introduced in the lectures. The client uses libraries to simplify access.

- *Excellent:* The server defines all REST and webservice endpoints through Spring and uses a client library like Jersey (REST) or Stomp (Webservice) to simplify the server requests.

We are using REST for all of the controller endpoints and we also synchronized the app for multiple users by means of websockets with Stomp.

### Data Transfer

Application defines meaningful data structures and uses Jackson to perform the de-/serialization of submitted data.

- *Excellent:* Jackson is used implicitly by Spring or the client library. No explicit Jackson calls are required in the application.

Every commons class is serialized through the Spring annotations and we do not have explicit Jackson calls except for a @JsonIgnore annotation.


