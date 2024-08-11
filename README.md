#  Loan Application

****

### High level design -
<img width="1269" alt="Screenshot 2024-08-12 at 1 09 47 AM" src="https://github.com/user-attachments/assets/52ed069b-fb16-4fe1-9305-026c87b09ceb">


### API documentation -

1. Create Loan

Url - **api/v1/loan**
Method - POST


Request -
<!--DOCS_START-->
    {
        "term": 3,
        "amountRequired": 1021.12,
        "customerId":"1",
        "paymentFrequency":"weekly"
    }
<!--DOCS_END-->

Response
<!--DOCS_START-->
    {
        "success": true,
        "message": "Loan requested",
        "data": {
            "amount": 1021.12,
            "loanTerm": 3,
            "customerId": "1",
            "paymentFrequency": "weekly",
            "loanStatus": "pending",
            "paymentSchedules": [
                {
                    "amount": 340.37333333333333,
                    "paymentDueDate": "2024-08-07",
                    "paymentStatus": "pending"
                },
                {
                    "amount": 340.37333333333333,
                    "paymentDueDate": "2024-08-14",
                    "paymentStatus": "pending"
                },
                {
                    "amount": 340.37333333333333,
                    "paymentDueDate": "2024-08-21",
                    "paymentStatus": "pending"
                }
            ],
            "id": "67735103119449"
        }
    }
<!--DOCS_END-->

2. Fetch Account

Url - **api/v1/admin/evaluateLoan**
Method - POST

Request -
<!--DOCS_START-->
    {
        "loanId": "67735103119449",
        "loanStatus": "approved"
     }
<!--DOCS_END-->

Response
<!--DOCS_START-->
    {
        "success": true,
        "message": "Loan approved"
    }
<!--DOCS_END-->

3. Loan Re-payment


Url - **api/v1/payment**
Method - POST

Request
<!--DOCS_START-->
    {
        "loanId": "67735103119449",
        "amount": 341
    }
<!--DOCS_END-->

Response
<!--DOCS_START-->
    {
        "success": true,
        "message": "Transaction done successfully",
        "data": {
            "amount": 1021.12,
            "loanTerm": 3,
            "customerId": "1",
            "paymentFrequency": "weekly",
            "loanStatus": "pending",
            "paymentSchedules": [
                {
                    "paymentStatus": "paid"
                },
                {
                    "amount": 340.37333333333333,
                    "paymentDueDate": "2024-08-14",
                    "paymentStatus": "pending"
                },
                {
                    "amount": 340.37333333333333,
                    "paymentDueDate": "2024-08-21",
                    "paymentStatus": "pending"
                }
            ],
            "id": "67735103119449"
        }
    }
<!--DOCS_END-->


### Local setup steps

#### Kafka Setup
0. install docker desktop and enable kubernetes from settings.
1. `kubectl create namespace kafka`
2. `kubectl create -f 'https://strimzi.io/install/latest?namespace=kafka' -n kafka`
   wait for sometime to get this pod running (verify via running kubectl get pods -n kafka)
3. create kafka.yaml & kafka-ui.yaml as above
4. `kubectl apply -f kafka.yaml -n kafka`
5. `kubectl apply -f kafka-ui.yaml -n kafka`
6. `kubectl wait kafka/my-cluster --for=condition=Ready --timeout=300s -n kafka`
7. `kubectl get svc -n kafka`
    1. get port of service my-cluster-kafka-server-bootstrap
    2. telnet localhost port
8. use above port in application.properties file. 

#### SQL setup

0. Install mysql
1. Create database loan_app 
2. Update application.properties 

#### Run appliction locally

1. Either use any IDE and directly run MainApplication.java as  java application.
2. Using maven command \
   2.1 Open a terminal/command prompt. \
   2.2 Navigate to the root directory of your Spring Boot project (where the pom.xml file is located). \
   2.3 Run the following command:

    `mvn spring-boot:run` \
This will compile project and start the Spring Boot application.




