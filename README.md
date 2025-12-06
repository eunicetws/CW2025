# CW2025 - TetrisJFX
___
## GitHub Link: https://github.com/eunicetws/CW2025
___
## Getting Started
This section will guide you through setting up and running the project on your local machine.

## Compilation Instructions
### 1.1 Prerequisites
Ensure that you have the following installed:
- Java Development Kit (JDK) - Version 23 or later 
  - check using 
    - ``java -version ``
    - `` javac -version``
- Maven - Version 3.8 or newer
  - check the version using ``mvn -v``
- JavaFX Library - Version 21.0.6 or newer
  - this will be downloaded automatically by Maven
- JUnit 5 (if testing is required)
  - this will be downloaded automatically by Maven
### 1.2 Cloning the Project

- To clone the repositiory using git, use the following commands in your terminal:
   ` bash
    git clone https://github.com/eunicetws/CW2025.git
   `

- To download the project via a ZIP file:
    1. On GitHub, make sure the branch is set to `master`.
    2. Click the green **Code** button.
    3. Select **Download ZIP** from the dropdown menu.
    4. Extract the ZIP file.

### 1.3 Building and Running the Project
#### Using terminal:

1. Navigate into the project directory
   ```bash
    cd <project location>
   ```

2. Build the project using Maven
   ```bash
    mvn clean install
   ```

3. Run the project using the JavaFX Maven plugin
   ```bash
    mvn javafx:run
   ```
#### Using IntelliJ
1. Open the project in IntelliJ
    1. Select "Open" / "Import"
   2. Browse to your project location
   3. choose Project's root directory and confirm
2. Import Maven dependencies
    - When you open the project, pom.xml will automatically import all Maven dependencies. 
    - If not, you can manually refresh Maven by clicking the “M” icon on the right side of IntelliJ.
    - Click on they sync symbol to reload all maven project
    - If necessary, manually update or download Maven dependencies and reload the project.
   
3. Build the project
   - In the Maven tool window, under Lifecycle, click:
     - clean → removes previous builds
     - install → compiles and installs the project locally
     
4. Run the project using JavaFX plugin
   - In the Maven tool window, go to Plugins → javafx → javafx:run 
   - Double-click javafx:run to launch the application.

#### Using VS Code
1. Open the project in VS Code
   1. Select "Open Folder"
   2. Browse to your project location
   3. Choose Project's root directory and confirm
2. Open a VS Code terminal
   - Go to Terminal → New Terminal.
   - Check if the terminal path is inside your project directory.
   - If it is not Navigate into the project directory
     ```bash
     cd <project location>
     ```
3. Build the project using Maven
   ```bash
    mvn clean install
   ```

4. Run the project using the JavaFX Maven plugin
   ```bash
    mvn javafx:run
   ```

## 3. Implemented and Working Properly
This section explains all the features that has been implemented into the game.

### Hard Drop
- Allows the player to instantly drop the current piece to the bottom of the grid
- Speeds up the gameplay
### Ghost Piece
- Display a piece with lower opacity on where the current piece will 
- Help players plan their moves more accurately
### Next Brick 
- Shows the upcoming piece in a panel so players can plan ahead
- Creates a more strategic gameplay
### Hold Brick
- Allows players to temporarily store a piece for later use
- Creates a more strategic gameplay

### Score
- Allow the user to get competitive by reaching for the highest score possible
### Lines Cleared
- Allows the user to track how many lines they have successfully cleared during gameplay
- Used in determining level progression
- Shows the player their progress throughout the game
### Levels
- The game speeds up as the level rises.
- At the first level,
    - The brick start out by falling every 400ms
    - the player must clear 10 lines.
- To reach the next level, the player must clear an additional 20 lines (lines cleared for previous levels are not counted)
- For the following levels, the required number of lines to level up continues to increase incrementally
- The maximum level is level 20 where the brick falls every 90ms.
- Is used to create a more progressive difficulty where the game speeds up as the player clears more lines
### Timer
- Allows the player to challenge themselves with a time limit
- The player can set the timer to 5 minutes, 10 minutes, 15 minutes, 20 minutes or none
### Display Keyboard Shortcuts while Playing
- To allow the user to see the keyboard shortcuts without needing to navigate to the Settings
### BGM and SFX
- Background Music is used to make the player feel more excited
- To enhance the gaming experience
### 7-Bag Randomization
- Ensure all seven Tetris pieces appear once per cycle
- To prevent situations where a single type of brick does not appear for a long time
- To improve randomness

### Home Menu
- Allow user to start the game themselves instead of dropping the user into the game immediately
- Allows the user to change the settings before playing. 
- Allow user to set the timer.
- #### Display HighScore
  - To show the user their current best HighScore
  - Sets a goal for user to beat
- #### Change Timer
  - Allow user to set the timer before starting the game
- #### Open Settings
  - Allow user to customise their setting
- #### Play the Game
  - Used to start playing the game
- #### Exit
  - Closes the application

### Pause Menu
- Gives the user the ability to modify the settings, resume or return home without resetting the entire game
- #### Pause
  - Allow user to stop the game without resetting the entire game
- #### Resume
  - Allow player to start playing the game starting from the time it was paused
- #### Restart
  - Resets the playing game 
- #### Open Settings
  - Open the settings to allow modification mid-game
- #### Return Home
  - Stops the game to returns to the Home Menu

### Settings
- #### Toggle Displays
  - This will allow the user to choose whether they want to see the Next Brick, Hold Brick, Ghost Piece and display the keyboard shortcuts
  - Enables players to increase the difficulty by hiding the Next Brick, Hold Brick, or Ghost Piece, creating a more challenging and unpredictable gameplay experience.
- #### Change Sound Volume
  - Adjusts the volume of background music and sound effects independently
  - Improves overall user experience by letting players customize audio levels to their preference
- #### Customise Keyboard Bindings
  - Allows players to assign their preferred keys for all game actions to improve accessibility and comfort.

## 4. Implemented but Not Working
This section explains all the features that has been implemented but were not working properly in the game.
### Change SFX sound
- The function to save the desired SFX sound type was implemented in `SaveData` and in `SoundData`
- Due to time constraints, the logic responsible for loading and applying the saved SFX sound type during gameplay was not fully completed.
- As a result, although the sound type is correctly stored in the save file, the sound system does not have the function to change the type of sound

## 5. Features Not Implemented
This section explains all the features that has been implemented but were not working properly in the game.
### Changing the starting level
- The idea to implement this feature was only occur towards the end of the deadline of the project, leaving limited time for proper planning and integration.
- Due to time constraints, implementing this feature became more challenging because 
  - it required updates to multiple components of the system, including the Level, SaveData, and HomeController classes 
  - requires a design that fits the current Home Menu

## 6. New Java Classes
These are the java classes that are newly added to the game categorised by their respective packages
### com.comp2042.controllers 
| Class                   | Description                                                                                                                                                                                                                                                                                                      |
|-------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| HomeController.java     | The HomeController is responsible for: <ul><li>handling the UI and interface of the Home Menu</li><li>allowing user to set a timer before the game begins</li><ul>                                                                                                                                               |
| SettingsController.java | The SettingsController is responsible for: <ul><li>handling the UI and interface of the Settings Menu</li><li>change the volume of the BGM and SFX</li><li>toggle the display for Next brick, Hold Brick, Ghost Piece and Keyboard Shortcut Display</li><li>allows user to change the keyboard shortcut</li><ul> |

### com.comp2042.data
| Class             | Description                                      |
|-------------------|--------------------------------------------------|
| Level.java        | Records and calculates the current level         |
| LinesCleared.java | Records the total number of line cleared         |
| SaveData.java     | Read and write the save file                     |
| SoundData.java    | Contains the SFX information                     |

### com.comp2042.logic
| Class             | Description                                      |
|-------------------|--------------------------------------------------|
| Timer.java        | Manages the timer when the player plays the game |

### com.comp2042.media
| Class     | Description                                                                      |
|-----------|----------------------------------------------------------------------------------|
| Bgm.java  | Manages the background music                                                     |
| Sfx.java  | Manages the sound effect for when a button is clicked and when a line is cleared |

## 6. Modified Java Classes
### GameController
Moved to package com.comp2042.controllers

| Changes                                                   | Reason                                                                                          |
|-----------------------------------------------------------|-------------------------------------------------------------------------------------------------|
| modify `onDownEvent(MoveEvent event, boolean isHardDrop)` | modified to allow both hard drop and soft drop                                                  |
| added `board.moveGhostPiece()` to each function           | to update the ghost piece's position each time the brick's position changes                     |
| new function `onHoldEvent(MoveEvent event)`               | calls the board for the hold brick logic when the GuiController detects the keybinding for hold |

### GuiController
- Moved to package com.comp2042.controllers
- #### Added Display Score, Cleared Lines, Level, Key Controls
    | Changes                                                               | Reason                                                                    |
    |-----------------------------------------------------------------------|---------------------------------------------------------------------------|
    | new function `bindScore(IntegerProperty integerProperty)`             | To allow the GUI to update the score dynamically.                         |
    | new function `bindLevel(IntegerProperty integerProperty)`             | To allow the GUI to update the level dynamically.                         |
    | new function `bindTotalClearedLines(IntegerProperty integerProperty)` | To allow the GUI to update the total number of cleared lines dynamically. |
    | new function `updateKeyLabels()`                                      | To allow the GUI to display the currently set keyboard shortcuts.         |

- #### Brick Display
  - Update the way bricks will be displayed

      | Changes                                                             | Reason                                                                                                                                                                                                  |
      |---------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
      | modify function `getFillColor(int i)`                               | <ul><li>Updated the color mapping for each brick type to improve the design.</li><li>Simplified the code. </li></ul>                                                                                    |
      | added function`getBorderColour(int i)`                              | Created a border around each pixel of the brick for better visualisation.                                                                                                                               |
      | modify function `refreshBrick(ViewData brick)`                      | <ul><li>Change the layoutX and layoutY of the Brick Pane so it stays in the correct position</li><li>Added `refreshGhostPiece()` so the ghost piece would always be in the correct position. </li></ul> |
      | modify function `setRectangleData(int color, Rectangle rectangle)`  | Created a border around each pixel of the brick for better visualisation.                                                                                                                               |

  - Displays the Next Brick

      | Changes                                          | Reason                                                                        |
      |--------------------------------------------------|-------------------------------------------------------------------------------|
      | new variable `rectanglesNextBrick`               | Create a matrix showing the next upcoming brick.                              |
      | new function `refreshNextBrick(ViewData brick)`  | Used to update the display of the next brick whenever the next brick changes. |

  - Display the Hold Brick

      | Changes                                         | Reason                                                                                 |
      |-------------------------------------------------|----------------------------------------------------------------------------------------|
      | new variable `rectanglesHoldBrick`              | Create a matrix showing the currently held brick, if any.                              |
      | new variable `isHoldOn`                         | Used to indicates whether the player has used the hold action during the current turn. |
      | new function `refreshHoldBrick(ViewData brick)` | Used to update the display of the hold brick whenever the held brick changes.          |
      | new function `resetHoldBrickDisplay()`          | Used to reset the hold brick's display when creating a new game.                       |

  - Display the Ghost Piece
    
      | Changes                                           | Reason                                                                                                                         |
      |---------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------|
      | new variable `rectanglesGhostPiece`               | Create a matrix used to display the ghost piece's projected landing position.                                                  |
      | added function`refreshGhostPiece(ViewData brick)` | Update the Ghost Piece alongside the falling brick to ensure it is always in the correct falling position of the current brick |

  - #### Created a Hard Drop Feature
    | Changes                                  | Reason                                                                                                         |
    |------------------------------------------|----------------------------------------------------------------------------------------------------------------|
    | new function `HardDrop(MoveEvent event)` | Handles the hard-drop action in which the the active brick is instantly dropped to the lowest valid position.  |
    
  - #### Display GameOver when the timer ends
    | Changes                                  | Reason                                                                                                      |
    |------------------------------------------|-------------------------------------------------------------------------------------------------------------|
    | new function `moveDown(MoveEvent event)` | Checks if the timer has reached 0 each time the brick moves down. If yes, the game over panel is displayed. |

  - #### Change the speed of the game
    | Changes                             | Reason                        |
    |-------------------------------------|-------------------------------|
    | new function `setSpeed(int speed)`  | Changes the speed of the game |

  - #### Allow user to modify their shortcuts & displays
    | Changes                                                           | Reason                                                            |
    |-------------------------------------------------------------------|-------------------------------------------------------------------|
    | new function `getKeyCode(int saveDataLine)`                       | Read the user set keyboard shortcut from the saved data.          |
    | new function `checkToggles()`                                     | Used to check whether a Label or Pane should be displayed or not. |
    | new function `setEventListener(InputEventListener eventListener)` | Used to check for keyboard events                                 |

  - #### Added the Pause Feature
    - Allow the player to pause mid-game

    | Changes                      | Reason                                                     |
    |------------------------------|------------------------------------------------------------|
    | new variable `isPause`       | Used to represents whether the game is currently paused.   |
    | new variable `settingsPane`  | Used as a root container for the settings panel UI.        |
    | new function `pauseGame()`   | Allows user to pause the game and displays the pause menu. |
    | new function `returnHome()`  | Allows user to return home when they want to stop playing. |

  - #### Remade Game Over Menu
    
    | Changes                      | Reason                                                 |
    |------------------------------|--------------------------------------------------------|
    | modify function `gameOver()` | Changed to allow the redesigned Game Over Menu to show |

### Score 
- Moved to package com.comp2042.data
    
    | Changes                               | Reason                                                                                            |
    |---------------------------------------|---------------------------------------------------------------------------------------------------|
    | new feature `saveScore()`             | used to save the highest score to the appropriate line in the save file based on the Timer chosen |
    | added `saveScore()` to every function | to save the score each time a higher score is detected when the score changes                     |

### BrickRotator
- Moved to package com.comp2042.logic
- Allow the brick rotator to store the data of the currently held brick

    | Changes                              | Reason                                                                              |
    |--------------------------------------|-------------------------------------------------------------------------------------|
    | new feature `RandomBrickGenerator()` | creates the first set of random bricks                                              |
    | new feature `refillBag()`            | adds a new set of the all brick types randomly shuffled into the current brick list |
    | modify feature `getBrick()`          | checks if the brick list needs to be refilled                                       |

### RandomBrickGenerator
- Modified to allow 7-bag randomization

  | Changes                        | Reason                                                                              |
  |--------------------------------|-------------------------------------------------------------------------------------|
  | new feature `getHoldBrick()`   | to return the brick that is currently held                                          |
  | new feature `setHoldBrick()`   | to change the hold brick to the active brick and the active brick to the held brick |
  | new feature `resetHoldBrick()` | when a new game is called, set the held brick to null                               |

### Simple Board
Moved to package com.comp2042.logic
- #### Added View Data for Hold Brick data
  - Allows the `GuiController` to correctly display the held piece.
    
      | Changes                               | Reason                                                                   |
      |---------------------------------------|--------------------------------------------------------------------------|
      | `brickData` is no longer final        | Allows `brickData` to be updated when the player swaps or holds a brick. |
      | new variable `HoldBrickData`          | Stores the brick data of the held piece.                                 |
      | new function `getHoldBrickData() `    | Returns the held brick’s data to other classes                           |
      | new function `refreshHoldBrickData()` | Updates the held brick's data when it is held or swapped.                |

- #### Added View Data for Next Piece data
  - Allows the `GuiController` to correctly display the next piece.

      | Changes                              | Reason                                         |
      |--------------------------------------|------------------------------------------------|
      | new variable `nextBrickData`         | Stores the brick data of the held piece.       |
      | new function `getNextBrickData()`    | Returns the next brick’s data to other classes |

- #### Added Position for Ghost Piece
  - Allows the `GuiController` to correctly display the ghost piece position.
    
      | Changes                                 | Reason                                                                        |
      |-----------------------------------------|-------------------------------------------------------------------------------|
      | new variable `ghostPieceOffset`         | Stores the current x and y position of the Ghost Brick                        |
      | new function `getGhostPieceXPosition()` | Returns the position of the ghost piece based on the x-axis to other classes  |
      | new function `getGhostPieceYPosition()` | Returns the position of the ghost piece based on the y-axis to other classes  |

### Main
| Changes                       | Reason                                                                                                                                                                  |
|-------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Added a Font Loader           | <ul><li>Centralizes the font-loading logic for easier maintenance and updates.</li><li>Maintains consistent font across the entire application.</li></ul>               |
| Changed Window Size           | <ul><li>Provides a better user experience, making the game more visually appealing.</li><li>Displays game elements more clearly.</li></ul>                              |
| Starts up the Home Controller | <ul><li>Does not drop the user into the game immediately.</li><li>Allows the user to change the settings before playing.</li><li>Allow user to set the timer.</li></ul> |

### Refactoring the Classes
- grouped classes into packages for better organization and maintainability.
```declarative
java/
└── com/
    └── comp2042/
        ├── controllers/
        │   ├── GameController.java
        │   ├── GuiController.java
        │   ├── HomeController.java
        │   └── SettingsController.java
        │
        ├── data/
        │   ├── bricks/
        │   │   ├── BrickI.java
        │   │   ├── BrickJ.java
        │   │   ├── BrickL.java
        │   │   ├── BrickO.java
        │   │   ├── BrickS.java
        │   │   ├── BrickT.java
        │   │   ├── BrickZ.java
        │   │   └── RandomBrickGenerator.java
        │   ├── ClearRow.java
        │   ├── DownData.java
        │   ├── Level.java
        │   ├── LinesCleared.java
        │   ├── MoveEvent.java
        │   ├── NextShapeInfo.java
        │   ├── SaveData.java
        │   ├── Score.java
        │   └── SoundData.java
        │
        ├── enums/
        │   ├── EventSource.java
        │   ├── EventType.java
        │   ├── KeyEventType.java
        │   └── SaveDataType.java
        │
        ├── interfaces/
        │   ├── Board.java
        │   ├── Brick.java
        │   ├── BrickGenerator.java
        │   └── InputEventListener.java
        │
        ├── logic/
        │   ├── BrickRotator.java
        │   ├── MatrixOperations.java
        │   ├── SimpleBoard.java
        │   └── Timer.java
        │
        ├── media/
        │   ├── Bgm.java
        │   └── Sfx.java
        │
        ├── view/
        │   ├── NotificationPanel.java
        │   └── ViewData.java
        │
        └── Main.java

```

## 7. Unexpected Problems
### Difficulties handling JUnit tests
- Many classes in the project are closely related to each other.
- This makes it challenging to write JUnit tests because most classes contain private functions and variables.
- Testing internal logic often requires using reflection or design changes to make certain methods/package-private for easier testing.

### Time Management
- This project required a significant amount of time to complete.
- Delays were caused by scheduling conflicts and lack of prior experience.
- Managing time was difficult due to other projects and other ongoing commitments.
- However, working on this project has greatly improved my time management skills.

### Inexperience in Handling Git
- My limited experience with Git caused occasional messy merges.
- Sometimes, I would accidentally perform the merge at the wrong time.
- At times, I would accidentally commit changes to the wrong branch.
- However, I gradually became more experienced and confident in using Git.