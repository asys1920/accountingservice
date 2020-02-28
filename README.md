# Accounting Service
## Table of Contents

- [Description](#description)
- [Documentation](#documentation)
- [Features](#features)
- [Requirements](#requirements)
- [Quick Start / Setup](#quick-start--setup)
- [API](#api)

## Description
[![GitHub release (latest by date)](https://img.shields.io/github/v/release/asys1920/accountingservice)](https://github.com/asys1920/accountingservice/releases/tag/v1.0.0)

This microservice is part of the car-rental project which was built
by the Asys course 19/20 at the TH Bingen.

It manages the Bills of the car-rental and keeps track of all Bills
the car-rental has to offer. You can create and get Bills. Furthermore you can get the balance of the Car-Rental.

The Microservice can be monitored by Prometheus.

Logs can be sent to Elasticsearch/Logstash using Filebeat.

## Documentation
See [Management project](https://github.com/asys1920/management) for a documentation of the whole Car-Rental project.
## Features
This microservice can create, update and read bills from a local H2 Database and show the current balance of the Car-Rental
by aggregating bills in a given time-frame. Furthermore, it exposes health,
info and shutdown endpoints using Spring Boot Actuator. By exposing a special /actuator/prometheus endpoint it can
be monitored using Prometheus. By using Filebeat the logs the microservice generates are sent to Elasticsearch/Logstash.

## Requirements
A JDK with at least Java Version 11.

### Local
### Docker
## Quick Start / Setup
### Run Local
### Run Docker

## API
To see a full documentation view the swagger documentation while running the microservice. You can
find the Swagger Documentation at `http://<host>:<port>/swagger-ui.html` 

Method | Endpoint | Parameters | Request Body | Description
--- | --- | ---  | --- | ---
GET | /bills | N/A | N/A | Get all existing bills
GET | /bills | /{id} | N/A | Get an existing bill
POST | /bills | N/A | Bill in JSON Format | Create a new bill
PATCH | /bills | N/A | Bill in JSON Format | Updates a specific Bill
 | | | |
GET | /balance | ?start={start}&end={end} | N/A | Get the balance for a specific timeframe
