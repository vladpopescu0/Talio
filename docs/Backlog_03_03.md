                                                                                                OOPP 23, version: Mar 2, 2023

## Talio User Stories

### Stakeholders:

+ __User__: Any person that is able to use the app.
+ __Admin__: A person that manages the server. 

### Terminology:

+ __Card__: An object on a list with a title that defines a task.
+ __List__: A collection of cards of similar importance.
+ __Board__: A collection of lists, which can be created by anyone.
+ __Tags__: An attribute of cards that can be used to group them together.
+ __Main page__: A page where you can see a list of all boards.

### EPIC - Minimal Application

As a user, I want a simple app for organizing my tasks, which has a single board where I can add lists containing cards that represent different tasks. Other users can see, edit and delete changes added to cards.

As a user, I want …

+ to connect to the app without logging in, so I don’t have to remember my password.
    - When I enter the app I will see the main page.
+ to create a single board, so I can start adding my tasks that need organizing.
    - Have a single board on the front page.
    - Data about the board is stored in the database.
+ to be able to add lists, so I can organize my tasks more wisely.
    - On the board screen, there will be a button to add new lists.
    - This button will lead me to a list creation screen, and entering all the information will result in a new list on the board.
+ to create and add cards that consist solely of a title, so there would be only a short reminder of a task I have to do.
    - There will be a button to add new cards.
    - This button will lead to a card creation screen, and entering the required information will result in a new card being added to the desired list.
    - The card should be created and added to the database.
+ to be able to change the order of cards within a board by dragging, so I can prioritize the available tasks.
    - A card will be able to be dragged with the mouse to change its position in the list.
    - This change will be seen by all users.
+ to be able to make changes to a card that are visible to all users, so I can update my (or somebody else’s) tasks.
    - Every card should have an “Edit” button.
    - When clicked, a Card Edit screen will appear on the screen with different already completed fields that can be edited by the user who clicked on that card.
    - This screen should have a “Save changes” button.
    - When clicked, the user is redirected to a board where the card is located.
    - All changes are added to the database.
    - Every card should have a “Delete” button, clicking it will delete the card from the user’s interface and from the database.

As an admin, I want ...

+ to be able to restart the server without losing any boards or cards, to implement updates more easily.
    - The boards, lists, and cards will be saved to a database, which will be used to recover information on startup.
+ to be able to receive my admin privileges by entering a fixed password, so that I can use the system from any device.
    - The main page should have the “Login as admin” button.
    - When clicked, the person is asked for a password that is set in the program before starting the server.
    - If the password is correct, the person will be redirected to an admin page(?) and receive all the rights. 

### EPIC - Public Boards

As a user, I want to have access to all boards created by other users. With this feature, collaboration becomes much more efficient.

As a user, I want…
+ to be able to access public boards created by any user, so I can see what tasks me and other people have to do.
    - Each board has a “See board” button.
    - Clicking the button should redirect me to the specific board and its contents.

### EPIC - Multiple Boards

As a user, I want to be able to see and create multiple boards. Any board can be deleted by any user that can see the board.

As a user, I want …

+ to create multiple boards, so I can have more specific boards.
    - Boards are displayed vertically on a (scrollable) home page.
    - There is a “+New” button in the home page, which accesses the board creation page.
    - When clicking it, the user is directed to a board creation page.
    - After submitting the new board, the user is redirected to the main page, where the new board is displayed.
+ to be able to delete any boards I have access to, so that there will not be any unused boards.
    - There will be a “Delete board” button on every board.
    - When clicked, the board will be deleted from the application.


### EPIC - Merging Boards

As a user, I want to be able to merge two or more boards into one board, so that I can alter the organization of my tasks.

As a user, I want… 

+ to be able to join boards by name/ key, so I can use a single board if multiple boards have the same purpose.
    - Inside a board (called main board) there should be a button called “Merge with other boards”
    - When clicked, the user is directed to a window with a checklist of all boards, which they can select.
    - After clicking a “Merge” button, all the lists from the selected boards will be transferred to the main board.



### EPIC - Card customization

As a user, I want to be able to stylize the app and customize my cards and tags, so I can better distinguish them and find them more easily.

As a user, I want …

+ to add a description and a nested task list to a card, so I can achieve a clearer demonstration of my tasks.
    - When editing the details of a card, there is a multiline text field titled “Description”.
    - When editing the details of a card, there is a list called “Tasks”.
    - The “Tasks” list consists of a collection of vertically aligned pairs of a checkbox and a corresponding name of a task.
    - Below this list, there is the “+Add Task” button.
    - When clicking it, a new task is appended to the aforementioned list.
+ to attach and change tags to each card, so I can specify the importance/ category of it.
    - When editing the details of a card, there is a list called “Tags”.
    - The “Tags” list consists of a collection of horizontally aligned tags.
    - This list contains a trailing “Add Tag” button.
    - When clicking it, the user is prompted for the name of the tag to be created, after pressing a “Create” button, the new tag is added to the card.
    - Each element of the “Tags” list has a “X” button which removes the tag when it is clicked.
+ to customize the background color of a board or the color of a tag, so I can emphasize some cards and make the boards personalized.
    - The edit view of a board and a tag features a section called “Background Color”.
    - This section contains a list of buttons representing respective available colours.
    - When clicking one such button, the background colour of the board/tag is set to the respective colour of the button, e.g. clicking a red button makes the background colour of the element red.
    - The currently selected colour is indicated by a bold border of the button representing that colour.

### EPIC - Shortcuts

As a user, I want to be able to use shortcuts so I can make some repetitive tasks faster and easier.

As a user, I want …

+ to use shortcuts to access specific features so I can save time and use the application with ease.
    - Shortcuts help me perform basic operations, like deletion, insertion or editing of a specific element.
    - Shortcuts representing a specific operation should be invokable on every page that allows users to perform that operation through the User Interface.


### EPIC - Secure boards using Passwords

As a user, I want to protect my boards using passwords so only certain people can edit the content.

As a user, I want...

+ create password protected boards, so only users with the password can write on the board, while the rest of the users can only read it. 
    - When editing a board, there is a button called “Create Password”.
    - Pressing that, I should be able to create a password that would limit who can change the contents of the board.

As an admin, I want…

+ to be able to change the password of any board, so I can have access to it/remove a user’s access to it, if it violates any rules.
    - Have an option “change password” for every password protected board. 
+ to be able to delete any board, so I can remove them if they contain offensive content, or if I want to clear the space on the server.
    - Every board will have 2 buttons, “Delete board” and “Delete board from database”.
    - When clicked, the latter will completely delete a board from the database, after confirming the admin password.



