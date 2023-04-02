# Rubric: Testing

### Coverage

Testing is an integral part of the coding activities. Unit tests cover all parts of the application (client, server, commons). Excellent teams will also pay attention to the (unit test) code coverage of crucial system components (accordind to the build result over all components).

- Good: All conceptual parts of the application have several automated tests (client, server, commons).

Our current code coverage is around 75% for the commons and server packages, but 0% for the client. We are aiming for a higher percentage and if required, we can also implement tests for some methods in the client package. (Fig. 1, Fig. 3)

### Unit Testing

Classes are tested in isolation. Configurable *dependent-on-components* are passed to the *system-under-test* to avoid integration tests (for example, to avoid running a database or opening REST requests in each a test run).

- Excellent: Configurable subclasses are created to replace dependent-on-components in most of the tests.

We have configurable repositories which contain methods that override the ones in the real interfaces. Their only purpose is to help with the testing. (Fig. 2)

### Indirection

The project applies the test patterns that have been covered in the lecture on *Dependency Injection*. More specifically, the test suite includes tests for indirect input/output and behavior.

- Sufficient: The project contains at least one exemplary test that goes beyond asserting direct input/output of a system-under-test. For example, by asserting indirect input, indirect output, or behavior.

The only particular case we are testing is behavior, when a bad request response is sent by the server. (Fig. 4)

### Endpoint Testing

The REST API is tested through automated JUnit tests.

- Excellent: The project contains automated tests that cover regular and exceptional use of most endpoints.

Most of the endpoints are tested (93%) and most of the lines are covered (81%), so bad usage is also handled in the tests. (Fig. 3)

