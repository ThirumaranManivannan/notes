# Notes Application

This repository hosts a straightforward Notes application designed for creating, retrieving, updating, stats and deleting notes.

The application exposes a RESTful API with diverse endpoints tailored for efficient note management.

## How to Use

1. **Clone the Repository**
    ```bash
    git clone https://github.com/ThirumaranManivannan/notes.git
    cd notes
    ```

2. **Set Up the Environment**

    Ensure that you have Java and Maven installed on your machine.

3. **Build and Run the Application(Maven)**
    ```bash
    mvn clean install
    java -jar target/notes-0.0.1-SNAPSHOT.jar
    ```
    The application will initiate, and you can access it at [http://localhost:8090](http://localhost:8090).

4. **Build Pipeline(GitHub)**

    CI pipeline configured, If any push to the branch, Pipeline will start and push docker image to docker Hub.

    ```bash
    docker pull maran786/notes
    docker run --name notes-app -p 127.0.0.1:8090:8090 maran786/notes
    ```
    docker pull - to get the latest image from Hub.

    docker run will create a container named notes-app and bind to local port(which you specified).

    The application will initiate, and you can access it at [http://localhost:8090](http://localhost:8090).

5. **Build Pipeline(Docker)**

    Install docker by following this guide at [How to install docker](https://docs.docker.com/desktop/install/mac-install/).

    Then from the project root run the below commands. 

    ```bash
    docker build -t notes .
    docker run --name notes-app -p 127.0.0.1:8090:8090 notes
    ```
    docker build - creates an image named notes.

    docker run will create a container named notes-app and bind to local port(which you specified).

    The application will initiate, and you can access it at [http://localhost:8090](http://localhost:8090).


## API Endpoints

1. **Create a Note**

   This API will create a new note with provided title, text and tags(optional).

   - **Endpoint:** `POST /notes/v1`
   - **Request Body:**
     ```json
     {
       "title": "Your Note Title",
       "text": "Your Note Content",
       "tags": ["TAG1", "TAG2"]
     }
     ```
   - **Response**
     - Response Http Codes ```200 success, 400 validation error, 500 internal server error```
        ```json
        {
          "id": "Id of the note",
          "title": "Title of the note",
          "createdAt": "Note created time"
        }
        ```

2. **Get All Notes**
   - **Endpoint:** `GET /notes/v1`
   - **Parameters:**
     - `tags` (optional): Filter notes by tags (comma-separated).
     - `page` (optional, default: 1): Page number for paginated results.
     - `size` (optional, default: 10): Number of notes per page.
     - **Response**
       - Response Http Codes ```200 success, 400 validation error, 500 internal server error, 404 No notes found```
         ```json
         {
          "notes": [{
            "id": "Id of the note",
            "title": "Title of the note",
            "createdAt": "Note created time"
          }],
           "totalNotes": "Total notes available",
           "totalPages": "Total pages",
           "currentPage": "Current page"
         }
         ```

3. **Get a Specific Note Text**
   - **Endpoint:** `GET /notes/v1/{noteId}`
   - **Path Variable:**
     - `noteId`: The unique identifier of the note.
   - **Response**
     - Response Http Codes ```200 success, 500 internal server error, 404 No notes found```
         ```json
         {
          "text": "Text(notes) of the note"
         }
         ```
4. **Update a Note**
   - **Endpoint:** `PUT /notes/v1/{noteId}`
   - **Path Variable:**
     - `noteId`: The unique identifier of the note to be updated.
   - **Request Body:** Similar to the one used for creating a note. But all values are optional.
   - **Response**
     - Response Http Codes ```200 success, 400 validation error, 500 internal server error, 404 No notes found```
     ```json
      {
        "id": "Id of the note",
        "title": "Title of the note",
        "createdAt": "Note created time"
      }
      ```

5. **Delete a Note**
   - **Endpoint:** `DELETE /notes/v1/{noteId}`
   - **Path Variable:**
     - `noteId`: The unique identifier of the note to be deleted.
   - **Response**
     - Response Http Codes ```200 success, 400 validation error, 500 internal server error, 404 No notes found```

6. **Get Note Statistics**
   - **Endpoint:** `GET /notes/v1/{noteId}/stats`
   - **Path Variable:**
     - `noteId`: The unique identifier of the note.
   - **Response**
     - Response Http Codes ```200 success, 400 validation error, 500 internal server error, 404 No notes found```
       ```json
       {
         "unique_word": "No of occurrences of the word"
       }
       ```

## Errors Handling

The application robustly manages various errors, including validation errors, field errors, and generic errors. It guarantees a consistent and informative response for different scenarios.
