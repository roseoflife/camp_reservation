# camp_reservation
Campsite Reservation

Project basic requirements
1-The users will need to find out when the campsite is available
2-Provide an end point for reserving the campsite.
3-Provide appropriate end point(s) to allow
  modification/cancellation of an existing reservation


Non functional requirements
System should be able to handle large volume of requests for getting the campsite availability.
Demonstrate with appropriate test cases that the system can gracefully handle concurrent requests to reserve the campsite.


#Concurrency
Optimistic Locking in JPA
Database: H2

It can be reserved minimum 1 day(s) ahead of arrival and up to 1 month in advance.



#Run
 Run CampReservationApplication
LoadDatabase has some defaul values for availability. You can update the capacity in that class if you want to test.
 availabilityRepo.save(new AvailabilityEntity(r.nextLong(), LocalDate.now().plusDays(1), 2)));

API POST to do a reservation

http://localhost:8080/reservation
{
      "uid": "83b0fdc8-acf7-11eb-8529-0242ac130006",
      "fromDate": "2021-05-09",
      "toDate": "2021-05-10",
      "name": "Joe",
      "email": "joe@upgrade.com"
   }

API DELETE to cancel a reservation
   http://localhost:8080/cancel?id=d9c63f09-4490-4798-8c79-464ad4252ba6

API to find availabilities
   http://localhost:8080//availabilities?fromDate=2021-05-09&toDate=2021-05-10

API PUT to update a booking availabilities
http://localhost:8080/reservation
{
      "uid": "83b0fdc8-acf7-11eb-8529-0242ac130006",
      "fromDate": "2021-05-09",
      "toDate": "2021-05-10",
      "name": "Joe",
      "email": "joe@upgrade.com"
   }