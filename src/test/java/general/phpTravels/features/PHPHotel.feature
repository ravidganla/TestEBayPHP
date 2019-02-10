Feature: PHP Hotel, Flight & Tour Booking 
Scenario: Book a hotel in Sydney
Given Open "https://www.phptravels.net" site Hotels module on "firefox" browser
When Book a hotel in "Sydney" city and between days "19/02/2019" and "22/02/2019"
Then Get Booking Confirmation

Scenario: Book a flight from Sydney
Given Open "https://www.phptravels.net" site Flights module on "firefox" browser
When Book ticket from "Sydney Kingsford Smith Arpt (SYD)" to "Melbourne Regional (MLB)" on date "19-02-2019"
Then Get Flight Booking Confirmation

Scenario: Book a tour
Given Open "https://www.phptravels.net" site Tours module on "firefox" browser
When Book "Sydney and Bondi Beach Explorer" tour on date "19/02/2019"
Then Get Tour Booking Confirmation