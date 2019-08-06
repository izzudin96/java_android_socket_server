# Java Android Socket Server

Neither the Android nor the server weren't submitted for grading, I thought it would be a great idea to make this repository public.

- This server is taking advantage of `Java socket` (of course, client and server), as part of the assessment requirements.
- Sending data from Native Java application to Android application is quite tricky. In this case, I'm sending and receiving to and from both end using ObjectOutputStream, and ObjectInputStream.
- Every object that is sent through this socket is serialized.
- I tried to imitate a real HTTP request and response. So, there are `Response` and `Request` class available.
- On the `Request` object, you can assign action and parameters.
- On the `Response` object, you can assign message and the returned object.
- `DataStore` is a Singleton class, it stores information about the `RaidRooms` and `Users`

Hope this helps.
