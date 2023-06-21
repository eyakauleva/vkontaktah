CREATE (liza:User {id: "10", login: "liza123", firstName: "Liza", lastName: "Ya", gender: "FEMALE", age: 20, version: 0})
CREATE (ivan:User {id: "11", login: "ivivi", firstName: "Ivan", lastName: "Ivanov", gender: "MALE", age: 35, version: 0})
CREATE (katya:User {id: "12", login: "kitty", firstName: "Katya", lastName: "Moto", gender: "FEMALE", age: 21, version: 0})

CREATE (k_post1:Post {id: "100", text: "this is my 1st post!", isEdited: false, created: localdatetime(), lastModified: localdatetime(), version: 0})
CREATE (k_post2:Post {id: "101", text: "I like dogs", isEdited: false, created: localdatetime(), lastModified: localdatetime(), version: 0})

CREATE (katya)-[:CREATED]->(k_post1)
CREATE (katya)-[:CREATED]->(k_post2)

CREATE (liza)-[:LIKED {value: "9"}]->(k_post1)
CREATE (ivan)-[:LIKED {value: "7.8"}]->(k_post1)
CREATE (ivan)-[:LIKED {value: "8.2"}]->(k_post2)
