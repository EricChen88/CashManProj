## Project description

Emulates a cash dispensing application for use in an ATM or similar device.  There is no need to request authorisation or availability of funds. The application should assume that all requests are legitimate; there will be other components of the system that will do things such as communicating with bank accounts and authorising withdrawals.

## Key design principals and features
* The application supports $50 and $20 notes by default. However using object orientated programming techniques, the underlying code has been designed to be generic to support all possible currency denominations.
* Invented lazy searching recursive algorithm to determine the best combination of currency types for higher efficiency
* More customisable and flexible

## Instructions

* To build the application
gradlew build
* To build and run the application
gradlew run
* To run unit test
gradlew test

## Detailed requirements

*The device will have a supply of cash (physical bank notes) available. 

*It must know how many of each type of bank note it has. It should be able to report back how much of each note it has. 

*It should be possible to tell it that it has so many of each type of note during initialisation. After initialisation, it is only possible to add or remove notes. 

*It must support $20 and $50 Australian denominations. 

*It should be able to dispense legal combinations of notes. For example, a request for $100 can be satisfied by either five $20 notes or 2 $50 notes. It is not required to present a list of options. 

*If a request cannot be satisfied due to failure to find a suitable combination of notes, it should report an error condition in some fashion. For example, in an ATM with only $20 and $50 notes, it is not possible to dispense $30. 

*Dispensing money should reduce the amount of available cash in the machine. 

*Failure to dispense money due to an error should not reduce the amount of available cash in the machine. 

For an ATM-style of machine (with $20 and $50 notes), the following dispensed amounts are of particular interest: 

*$20 

*$40 

*$50 

*$70

*$80 

*$100 

*$150 

*$60 

*$110 

*$200, when there is only 3x$50 notes and 8x$20 notes available.

