
JavaFX Desktop Email Client
==================
### Proof of concept of email-client with desktop GUI
---------------	
This is NOT a secure app, and I used a dummy Gmail in order to test the program. DO NOT USE IT ON YOUR REAL GMAIL! (Although Google blocks restricted apps from using it anyway).

JavaFX does not have many good examples across the web. I believe this Udemy course is one of the best source for a tutorial to JavaFX with more advanced features like connecting into email account, multiple stages, CSS etc.

### Concepts I learned throughout the project
---------------	
Throughout this project I perceived few main concepts:
- How JavaFX works
- MVC Architecture
- The importance of Abstract Class and Polymorphism
- Services in JavaFX (which act as multithreading)
- Compose a full functional app
- Working with modules
- Merge a CSS into a JavaFX application
- That OOP bloats your code like crazy
- That Java bloats your code like crazy (I believe it was half the length of code in any not OOP language)

### Todo
---------------	
- The persistence currently using Binary64 **Encoding** which means this app is anything but secure - swap the encoding to AES that based on user's "key" (or password).
- TLS / Secure access to Gmail server.
- "About" window.
- Let the user have more than one host (currently it configures only for Gmail).

### Thanks
---------------	
Alex Horea: https://www.udemy.com/course/advanced-programming-with-javafx-build-an-email-client/
The project based on his tutorial and guidance
