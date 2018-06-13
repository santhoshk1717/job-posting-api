Search API: http://localhost:8080/api/search/:searchkeyword

Example: http://localhost:8080/api/search/Java

The following is the result set obtained

[
    {
        "availability": "fulltime",
        "compensation": "$20/hr",
        "description": "1 year experience",
        "location": "New York,NY",
        "posted_date": "2018-06-12 00:00:00",
        "replyRate": "70%",
        "role": "Jr. Developer",
        "skills": "Java, Vertx,netty, Kafka",
        "id": "0"
    },
    {
        "availability": "fulltime",
        "compensation": "$40/hr",
        "description": "2 year experience",
        "location": "New York,NY",
        "posted_date": "2018-06-12 00:00:00",
        "replyRate": "70%",
        "role": "Software Developer",
        "skills": "Java, Vertx,netty, Kafka",
        "id": "4"
    },
    {
        "availability": "fulltime",
        "compensation": "$40/hr",
        "description": "2 year experience",
        "location": "Manhattan,NY",
        "posted_date": "2018-06-12 00:00:00",
        "replyRate": "70%",
        "role": "Software Engineer",
        "skills": "Java, Vertx,netty, Kafka",
		"id": "2"
	}
]


Job Post API: http://localhost:8080/api/job

Send the following object in body for post api:

 {
        "role": "Software Engineer",
        "compensation": "$40/hr",
        "location": "Manhattan,NY",
        "description": "2 year experience",
        "replyRate": "70%",
        "skills": "Java, Vertx,netty, Kafka",
        "posted_date": "2018-06-12",
        "availability": "fulltime"

  }
  
Kafka Configarations: Installed Kafka broker on localhost:9092
Kafka Version: 1.1.0 	
Vertx-Kafka-client Version:  3.5.2


Integrated AWS RDS for the application : santhosh.crzbyoydhnfc.us-east-2.rds.amazonaws.com:3306/JobPostings

Exection:

				maven clean package

				java -jar target/job-postings-api-0.0.1-SNAPSHOT-fat.jar

				Exection must be done inside the project folder.
				

	