1. The booking functions and remove booking functions are exactly the same in three hotel room file, which is repeating a lot.
2. To avoid repeating, I made Room an abstract class instead of interface so that Room is able to implement some functions. Then I created a constractor for Room. I moved function Book, toJOSN, removeBooking and changeBooking from three specific room files to Room.

Assumption: 
1. changeBooking returns updated booking if success, otherwise returns original booking.
2. overlaps will return true is the start day is after the end day. Otherwise it returns false.