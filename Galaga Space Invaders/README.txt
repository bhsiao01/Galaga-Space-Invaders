=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 120 Game Project README
PennKey: bhsiao
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. 2-D Arrays
  I used a 2-D array to store the space invaders. Since they need to be arranged in rows and columns, 
  a 2-D array makes the most sense to store them. Also, since I shift the invaders when they reach a
  border, it makes sense to shift all of the invaders if any of the rows have hit the border.
  
  At first, I was going to store all the invaders as a LinkedList and a 2D array, and since that's
  not a good implementation of a 2D array, I modified it such that the space invaders were stored in
  a 2D array.
  
  Also, I was originally going to make a grid that was a 2D array and store what was currently at
  that position. However, I felt that was redundant since all of my objects had position fields
  so I could just directly check their locations by calling their getPx() and getPy() methods.

  2. Collections
  I used a LinkedList to store the galaga invaders. Since I positioned invaders based on their 
  indices, using a LinkedList makes the most sense. Since a set is unordered, ordering the invaders
  would have been more difficult. I also didn't want to pair any of the invaders with another value,
  so using a map does not make sense. In addition, I added a new row of invaders each time 
  every galaga invader had died, and a normal array does not allow one to change the size of the 
  array, so again, a LinkedList made the most sense.
  
  I also used a LinkedList to store the bombs because I added them when a random number was less
  than 5 and removed them when they hit the bottom border.
  
  (I discussed the feedback I received for this in #1)
  
  3. Inheritance/Subtyping
  I had two types of invaders. All of them employ the same move() method but attack differently. One
  type will be the traditional kind from Space Invaders and just drop bombs. The other type will be 
  similar to the Galaga kind and swoop down to attack the player. Since the invaders will share 
  draw() and move() methods and have unique attack() methods, it makes sense to make an Invader 
  abstract class so that I only have to implement the draw() and move() once and can just implement 
  different attack() methods.

  4. Testing
  I tested the various methods I had in GameCourt and all my classes. The states of the Invader, 
  Bomb, Bullet, and Player objects should change based on their interactions. For example, if an 
  Invader touches a Player, then the game should end. Also, if a Bullet hits an Invader, then the 
  Invader should die. 


=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.
  Invader:
  Invader was my abstract class for the SpaceInvader and GalagaInvader classes. I used it to store
  the shared fields and methods of the SpaceInvader and GalagaInvader classes, which included
  their states of being dead and attacking. Also, I implemented the draw and move methods here
  because the two types of invaders were drawn the same (with the exception of their color) and 
  moved the same. 
  
  SpaceInvader:
  This is the class for the white invaders. Its main function was to make the space invaders attack
  by dropping bombs. It had a few static fields to enable me to easily check how many space invaders
  were currently attacking.
  
  GalagaInvader:
  This is the class for the red invaders. Its main function was to make the galaga invaders attack 
  by dropping down. It also had a static field to keep track of when the last invader had dropped
  and let me make sure that they had at least 1 second in between new drops.
  
  Bullet:
  This is the class for the bullets that the player shot and the bombs that the space invaders 
  dropped. Since the bullets and bombs behaved the same except for which direction they went, I 
  made them both be under the same class. Its main function was to move the bullets/bombs.
  
  Player:
  This is the class for the player. Its main function was to allow the user to interact with the 
  program. The only class that the player can directly interact with is the Player class. By
  using the arrow keys and space bar, the user could move the player and shoot at the invaders.
  
  Game:
  This is the class that runs the game. Its main function is to set up the actual window for the
  game and display the state. It displays the state through having a label that prints the player's
  current score. 
  
  GameCourt:
  This is the class that dictates what happens in the court. Its main functions are to draw all of
  the objects in the game and invoke all of their methods. This includes moving the objects
  and modifying their states when different objects interact with each other.
  
  GameObj:
  This is the parent of all of the classes I created. It contains low-level methods such as 
  setting the object's positions and velocities. Its main function is to reduce the need for 
  redundant code by allowing all of my created objects to share common functions. For example,
  I didn't need to store every objects' individual locations and velocities because I could just
  call this.getPx() to get the x-coordinate of any of my objects.

- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?
  I had a hard time figuring out how to get the invaders to move and shift down each time they hit
  a wall. I was stuck for a couple days trying to get the invaders to move correctly, and for a 
  while, they would get out of alignment because I was trying to implement this functionality all 
  in one for loop. It took a lot of debugging for me to figure out that I was not always updating
  all of the invaders' positions.
  
  Another difficulty was figuring out how to modify the states of different objects within other 
  classes. For example, I needed to set the bullets' positions to the player's position, which was
  constantly changing. It took careful planning and trial and error for me to figure out that I 
  could pass in the player as an argument to the bullets' methods.


- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?
  I think that my design has a fairly good separation of functionality. I don't have much 
  redundancy in my code, and each class has a separate function. However, I don't think I have the
  best private state encapsulation. In order to get the space invaders to drop bombs, I pass the 
  bombs LinkedList to the invaders' attack method. Instead of using a copy and preserving 
  encapsulation, I pass the actual list of bombs. If I had time, I would try to figure out a better
  way of getting the invaders to drop bombs without breaking encapsulation.


========================
=: External Resources :=
========================

- Cite any external resources (libraries, images, tutorials, etc.) that you may
  have used while implementing your game.
