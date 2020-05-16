# Java Android Socket Server

Project with: https://github.com/TheCelVanBi

Both the `Android App` and the `Server` were not required for submission, so, I thought it would be a great idea to share this repository.
- This server is taking advantage of `Java socket` (of course, both `Client` and `Server`), as part of the assessment requirements.
- Sending data from Native `Java` application to `Android` application is quite tricky. In this case, I'm sending and receiving to and from both ends using `ObjectOutputStream`, and `ObjectInputStream`.
- Every object that is sent through this socket is serialized (implement java `Serialization`).
- I tried to imitate a real HTTP request and response. So, there are `Response` and `Request` class available.
- The `Request` object, you can assign action and parameters.
- The `Response` object, you can assign a message and the returned object.
- `DataStore` is a Singleton class, it stores information about the `RaidRooms` and `Users`.
- The `Client` class is used to seed data to `DataStore`.

Hope this helps.
