# typicodeblog
The project to cover the blog API with unit tests with the help of RestAssured

This is a gradle project ready to be built in CircleCI as well. See the https://circleci.com/gh/NestyIvan/typicodeblog for the results.
To build the project locally you need to have grradle installed. Run the following command "./gradlew test".
The test report result can be found by the path: build/reports/tests/test

The default URI for the blog and the default user "Samantha" are defined in the gradle.properties and can be overiden by any other values.
The project tests the user's profile with the corresponding POJOs classes, however there were no intentions to cover schema validation in a normal sense (with ). It's assumed that the blog is under construction, therefore it's likely that the schema would change frequently.

The main test scenario is validating emails format of eeach comment left by all posts written the defined user. The email check is performed by a regex and validates the email abides following rules:
0) @ sign should be presented
1) A-Z characters allowed
2) a-z characters allowed
3) 0-9 numbers allowed
4) Dots(.), dashes(-) and underscores(_) are allowed
5) Rest all characters are not allowed
The final regex rule should be discussed in details with the rest team.
The test approach includes using multithreading in order to speed up the test.

The pagination wasn't implemented as it's probably not supported by the blog itself. Neither offset nor limit filtering worked for me. In case of test data grows to a production-like size (million of comments for example) that could become a problem. However, for the current data volumes (500 comments per post) it's not a problem.
