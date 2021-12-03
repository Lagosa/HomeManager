# HomeManager

## Description
The home manager is a system that facilitates interaction between house members when they are not together. It was conceived to have connected to it the phones of the family members through an app, and a central tablet. The app helps to manage chores within the house: to make a list of them, assign them to members, keep track (make a report) of how many tasks did individuals do. Besides these, it helps decide on the dishes to make by having a list of all the recipes the family had made, by choosing a random dish, or by planning a dish for every day of the week. The central tablet has the role of displaying information about the status of the house. Such information can be who is at home, display message for the person that arrives home the earliest, display information about the day’s dish, the chores that need to be done, mementos, upcoming events for the family.


## Database design, tables
  - **users:** UUID, nickname, family id, role (roles: id), birth date<br>
  - **centralTablets:** UUID, family id, ip?<br>
  - **arrivals:** id, family (families: id), user (users: id), status (left, arrived), date, hour<br> 
  - **families:** UUID, e-mail, password, identification code<br>
- **chores:** id, family (families: UUID), status, submittedBy (users: id), doneBy (users: id), submittingDate, deadline, type (choreTypes: id), description, title, done date<br>
- **dishes:** id, familyId, dishName, type (dishTypes: id), recipe, visibility, submitter (users: id)<br>
- **ingredients:** id, ingredient, measurementUnit<br>
- **dish_ingredients:** id, dish(dishes: id), ingredient (ingredient: id), quantity<br>
- **polls:** id, family (families: id), dish (dish: id), forCount, againstCount<br>
- **dishPlans:** id, family (families: id), dish (dishes: id), weekStartDate, weekEndDate, dayToPrepareIt, status<br>
- **notifications:** id, family (families: id), sender (users: id) [for system and central tablet a special code], receiver(users: id), message, status (seen, not seen)<br>
- **mementos:** id, family (families: id), title, date, hour<br>
- **roles:** id, role (child, parent)<br>
- **choreTypes:** id, type<br>
- **dishTypes:** id, type<br>
## Detailed functionalities
### Individual phones:
####  Registration process:
 - create a family account
 - join the family by identification code
-set personal data: name, role (child, parent, central tablet), birth date
- get data about a user by id
- log into the family account
#### chores:
- create a chore (chore name, deadline, short description, category)
- delete a chore
- take up a chore
- mark a chore as done
- get the list of all undone chores (sort by deadlines)
- get reports of individuals’ done chores grouped by weeks
- (send a notification to urge a chore assigned to a given person, done only by parents) - mobile version
- get chore types
- change deadline
- get took up chores
#### dish planner:
- add a new dish (name, type (breakfast, lunch, dinner), ingredients, recipe, photo)
- list the dishes based on the type, how many times they were made
- create a poll on what to cook
- add to the poll recipes
- vote in a poll
- get poll results
- get a random dish based on a type
- make a weekly dish plan - add for each day of the week at least 1 dish
- view weekly dish plan
- mark a dish as cooked
- generate a list of ingredients needed for the dishes for a given day (today, tomorrow, or any day of the week)
### Interaction with other family members:
- send a message in form of a notification to a given person or to the whole family
- get a list of who is at home
- get a list of when did family members arrived and left from home (only parents)
- send a message to the central tablet
- create an upcoming event/memento 
### Central tablet:
- set who arrived home
- get a list of who is home
- get a list of today’s dish plan
- get notifications sent to the central tablet
- get a list of upcoming events/memento
## Further improvemenets:
### shopping list:
- create a shopping list
- add items (everyone from the family)
- remove items
- mark as bought
