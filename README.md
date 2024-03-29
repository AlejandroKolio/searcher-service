### Searcher Service based on Spring 5/Project Reactor.

Is a service, that will accept a request with text parameter on input.

It will return maximum of 5 books and maximum of 5 albums (**by default if there is no any limit given explisitly**)
that are related to the input term. The response
elements will only contain title, authors(/artists) and information whether a book or an album
###### documentation can be found in resources/openapi.yml.

##### Response Example

###### http://localhost:8080/v1/api/search/artist?name=frank+sinatra&limit=2
````json5

[
  {
    "albumId": "6eN1RE8EceFCVdBnLe34pb",
    "albumTitle": "Frank Sinatra. I Did It My Way"
  },
  {
    "albumId": "4mciOfSihYjck74iQJqBAi",
    "albumTitle": "Nothing But the Best (Remastered)"
  }
][
  {
    "bookId": "7HM6PwiDE56k5dEfPnh6gH",
    "bookTitle": "Frank Sinatra"
  },
  {
    "bookId": "34FsYg96dOl28cXXV4eE63",
    "bookTitle": "Frank Sinatra, My Father"
  }
]
````

##### For albums please use the iTunes API: 
https://affiliate.itunes.apple.com/resources/documentation/itunes-store-web-service-search-api/#searching

##### For books please use Google Books API:
https://developers.google.com/books/docs/v1/reference/volumes/list 

- Sort the result by title alphabetically.
- Make sure the software is production-ready from resilience, stability and performance point of view.
The stability of the downstream service may not be affected by the stability of the upstream services.
Results originating from one upstream service (and its stability / performance) 
may not affect the results originating from the other upstream service. 
- Service needs to respond within 60 seconds. 
#### Requirements
- Java 11
- Maven 3.3.9 or greater
