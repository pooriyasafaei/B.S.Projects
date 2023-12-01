# import libraries here
import random
import copy
import numpy as np
# import cv2
# from google.colab.patches import cv2_imshow
# from google.colab import output
import time
import os, sys
import time
import pygame

# from google.colab import drive
# drive.mount('/content/drive')
# import sys
# sys.path.insert(0,'/content/drive/My Drive/Colab Notebooks')

from gui import *
from map import *
from tqdm.auto import tqdm
import matplotlib.pyplot as plt

"""# Q2: Adversarial Search (100 Points)

<font size=4>
Author: Mohammad Ali Banayeean Zade
<br/>
<font color=red>
Please run all the cells.
</font>
</font>
<br/>
</div>

In this problem you must implement a minimax agent to play a game known as Alquerque.

The game has already been implemented for you. You should implement these things:  
Complete the three minimax players (minmax, alpha-beta pruning) and random player classes. For the first minmax player you must create the minimax tree until you reach a desired depth or a cutoff state. Then you must use an evaluation function in `Game` class to score the nodes and find the best next move. For the second one, you must do all the above but using alpha beta pruning algorithm.

*** Note that, the result from the game is a part of your score and a small portion of your score is evaluated based on the quality of your code.

# Game rules:
You can play Alquerque <a href="https://omerkel.github.io/Alquerque/html5/src/">here</a>.
Before starting, each player places their twelve pieces in the two rows closest to them and in the two rightmost spaces in the center row. The game is played in turns, with one player taking red and the other blue.
<ul>
<li> A piece can move from its point to any empty adjacent point that is <b>  connected by a line. </b> </li>
<li> A piece can jump over an opposing piece and remove it from the game, if that opposing piece is adjacent and the point beyond it is empty.</li>
</ul>

<img  
src="https://imageupload.io/ib/Sb359POEfCjUpAL_1698232288.png" alt="demo.png"/>
</br>

*** The goal of the game is to get more score. Players play for 100 round, Finally each player score come from below equation:


$$\displaystyle score=(remain \; pieces)+5×(pieces \; taken \; from \; opponent)$$


The player with higher score win the game.

</br>

# Agents

## Player

Player is base class for game-playing agent. Other agents must inherit this class.
"""


class Player:
    """
    Base class for a game-playing agent.
    You must implement the next_move() method to complete this class.

    Parameters
    ----------
    number : int
        The number of the player. If 1 then this player goes first. Otherwise this
        player goes second.
    """

    def __init__(self, number: int, maxdepth=2):
        self.number = number
        self.maxdepth = maxdepth

    def next_move(self, game):
        raise NotImplementedError("This should be implemented")


"""## Random Player (5 points)
This player, randomly select a move from the available possible moves.
"""


class Random_Player(Player):
    def next_move(self, game):
        """
        Randomly select a move from the available possible moves.

        Parameters
        ----------
        game : `game.Game`
            An instance of `game.Game` encoding the current state of the
            game.

        Returns
        ----------
        (int, int) or None
            A randomly selected possible move.

        Useful functions:
        ----------
        game.get_possible_moves
        """
        all_moves = game.get_possible_moves()

        if all_moves:
            # If there are possible moves, select one randomly
            random_move = random.choice(all_moves)
            return random_move
        else:
            # If there are no possible moves, return None
            return None


"""## Min-Max Player (30 pints)
In this part, You should implement orginal min max algorithm for `Min_Max_Player` class
"""


class Min_Max_Player(Player):
    def next_move(self, game):
        """
        Parameters
        ----------
        game : `game.Game`
        An instance of `game.Game` encoding the current state of the
        game.

        Returns
        ----------
        (int, int) or None
        A possible move. The first int is the moving piece's location id, and the second int is the target location id.

        If None is returned when possible moves are available, it is an automatic forfeit, and the player will lose the match.

        Useful functions:
        ----------
        game.get_possible_moves
        game.apply
        game.get_player_score
        """
        best_move = self.minimax(game, self.maxdepth)[0]
        return best_move

    def minimax(self, game, depth):
        possible_moves = game.get_possible_moves()
        if depth == 0 or not possible_moves:
            return None, game.get_player_score(self.number)

        if game.turn % 2 == self.number % 2:
            best_move = None
            best_score = -float('inf')
            for move in possible_moves:
                next_game = game.copy()
                next_game.apply(move)
                _, score = self.minimax(next_game, depth - 1)
                if score > best_score:
                    best_score = score
                    best_move = move
            return best_move, best_score

        else:
            best_move = None
            best_score = float('inf')
            for move in possible_moves:
                next_game = game.copy()
                next_game.apply(move)
                _, score = self.minimax(next_game, depth - 1)
                if score < best_score:
                    best_score = score
                    best_move = move
            return best_move, best_score


"""## Alpha-Beta Pruning (40 points)
In this part, You should implement alpha beta pruning algorithm.
"""


class Alpha_Beta_Pruning_Player(Player):
    def next_move(self, game):
        """
        Parameters
        ----------
        game : `game.Game`
        An instance of `game.Game` encoding the current state of the
        game.

        Returns
        ----------
        (int, int) or None
        A possible move. The first int is the moving piece's location id, and the second int is the target location id.

        If None is returned when possible moves are available, it is an automatic forfeit, and the player will lose the match.

        Useful functions:
        ----------
        game.get_possible_moves
        game.apply
        game.get_player_score
        """
        best_move = self.pruning(game, self.maxdepth, -float('inf'), float('inf'))[0]
        return best_move

    def pruning(self, game, depth, alpha, beta):
        possible_moves = game.get_possible_moves()
        if depth == 0 or not possible_moves:
            return None, game.get_player_score(self.number)

        if game.turn % 2 == self.number % 2:
            best_move = None
            best_score = -float('inf')
            for move in possible_moves:
                next_game = game.copy()
                next_game.apply(move)
                _, score = self.pruning(next_game, depth - 1, alpha, beta)
                if score > best_score:
                    best_score = score
                    best_move = move

                if best_score >= beta:
                    return move, best_score
                alpha = max(alpha, best_score)

            return best_move, best_score

        else:
            best_move = None
            best_score = float('inf')
            for move in possible_moves:
                next_game = game.copy()
                next_game.apply(move)
                _, score = self.pruning(next_game, depth - 1, alpha, beta)
                if score < best_score:
                    best_score = score
                    best_move = move

                if best_score <= alpha:
                    return move, best_score
                beta = min(beta, best_score)
            return best_move, best_score


"""# Game
All the game actions happen in this class. Don't change this codes. You can read the code to unserstand what it exactly does.

*** Notice: You should implement `get_player_score()` function here.
"""


class Game:
    def __init__(self, player_1, player_2):
        """
        Parameters:
        ----------
        player_1: It is first player. From Player classes

        player_2: It is second player. From Player classes
        """
        self.map = OriginalMap(220, 140, 200, 200, 4, 4)

        self.player1 = player_1
        self.player2 = player_2

        self.turn = 0

        self.player1_pieces = [i for i in range(12)]
        for i in self.player1_pieces:
            self.map.assign_point(i, PointStatus.player_1)

        self.player2_pieces = [i for i in range(13, 25)]
        for i in self.player2_pieces:
            self.map.assign_point(i, PointStatus.player_2)

    def get_active_player(self):
        return self.player1 if self.turn % 2 == 1 else self.player2

    def copy(self):
        new_game = Game(self.player1, self.player2)
        new_game.turn = self.turn
        new_game.player1_pieces = copy.deepcopy(self.player1_pieces)
        new_game.player2_pieces = copy.deepcopy(self.player2_pieces)
        self.map.copy(new_game.map)
        return new_game

    def tick(self):
        self.turn += 1
        game_copy = copy.deepcopy(self)
        player = self.get_active_player()
        move = player.next_move(game_copy)

        if self.is_move_valid(move):
            self.apply(move)

    def is_move_valid(self, move):
        if move is not None:
            if move[0] in self.get_player_pieces(self.turn % 2):
                return True
            else:
                return False

    def get_possible_moves(self):
        possible_moves = []
        pieces_available = self.player1_pieces if self.turn % 2 == 1 else self.player2_pieces
        for piece_id in pieces_available:
            piece = self.map.id_to_point(piece_id)

            for i in range(-2, 3):
                for j in range(-2, 3):
                    dest_row = piece[0] + i
                    dest_column = piece[1] + j
                    if self.map.is_point_valid(dest_row, dest_column):
                        dest_id = self.map.point_to_id(dest_row, dest_column)
                        if self.map.can_move(piece_id, dest_id):
                            possible_moves.append((piece_id, dest_id))

        return possible_moves

    def apply(self, move):
        if move is not None:
            outcomes = self.map.apply_move(move)

            moving_player_pieces = self.player1_pieces if self.turn % 2 == 1 else self.player2_pieces
            waiting_player_pieces = self.player2_pieces if self.turn % 2 == 1 else self.player1_pieces

            for outcome in outcomes:
                # Some piece died
                if outcome[1] == -1:
                    assert outcome[0] in waiting_player_pieces
                    waiting_player_pieces.remove(outcome[0])
                # A piece moved
                else:
                    assert outcome[0] in moving_player_pieces
                    moving_player_pieces.remove(outcome[0])
                    moving_player_pieces.append(outcome[1])

    def get_player_pieces(self, number):
        if number == 1:
            return self.player1_pieces
        else:
            return self.player2_pieces

    def get_player_score(self, number):
        '''
        Calculate the heuristic value of a game state from the point of view of the given player.
        Parameters:
        ----------
        number: show given player number.

        Useful functions:
        ----------
        game.get_player_pieces
        '''
        if number == 1:
            return len(self.player1_pieces) + 5 * (12 - len(self.player2_pieces))
        else:
            return len(self.player2_pieces) + 5 * (12 - len(self.player1_pieces))

    def render(self, surface):
        self.map.render(surface)


"""# Test agents (25 points)

"""

# Don't change this cell

import matplotlib.pyplot as plt


def visualize(game, display, screen):
    game.render(screen)
    display.flip()
    view = pygame.surfarray.array3d(screen)
    view = view.transpose([1, 0, 2])
    # img_bgr = cv2.cvtColor(view, cv2.COLOR_RGB2BGR)
    # cv2_imshow(img_bgr)


# Don't change this cell
# By calling this function, the game start and you can track the game by using graphics = True

SCREEN_WIDTH = 640
SCREEN_HEIGHT = 480


def start_game(game: Game, graphics=True):
    '''
    Parameters:
    ----------
    game: instance of `Game` class.
    graphics: If True, show graphic of the game.
  '''

    pygame.init()
    screen = pygame.display.set_mode((SCREEN_WIDTH, SCREEN_HEIGHT))
    display = pygame.display
    done = False

    # White background
    background_color = (255, 255, 255)
    counter = 0

    while not done:
        counter += 1
        screen.fill(background_color)
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                done = True

        if counter >= 1:
            game.tick()

        if counter >= 101:
            break

        if graphics:
            # can change this to have better visualizion
            visualize(game, display=display, screen=screen)

    player1_score = game.get_player_score(1)
    player2_score = game.get_player_score(2)
    if player1_score > player2_score:
        print("Player 1 wins")
        return 1
    elif player2_score > player1_score:
        print("Player 2 wins")
        return 2
    else:
        print("It's a draw")
        return 0


"""### Two random player"""

# example 1: player1 = red, player2 = blue
# with graphics=True you can follow the steps and see the game.
player1 = Random_Player(1)
player2 = Random_Player(2)
game = Game(player1, player2)
start_game(game, graphics=True)

"""You can see playing of two random player for 10 time in below cell. It is obvious that first player wining is approximately equal to second one."""

# example 1: player1 = red, player2 = blue
num = 10
win_1 = 0
win_2 = 0
draw = 0
for _ in range(num):
    player1 = Random_Player(1)
    player2 = Random_Player(2)
    game = Game(player1, player2)
    result = start_game(game, graphics=False)
    if result == 1:
        win_1 += 1
    elif result == 0:
        draw += 1
    else:
        win_2 += 1
print(f'win_1 = {win_1}, win_2 = {win_2}, draw = {draw}')

"""### Random player vs Min Max player"""

# example 2:
seed = 42
random.seed(seed)
player_1 = Min_Max_Player(1, maxdepth=3)
player_2 = Random_Player(2)
game = Game(player_1, player_2)
start_game(game, graphics=True)

"""### Random player vs Alpha-Beta player"""

# example 3:
seed = 42
random.seed(seed)
player1 = Alpha_Beta_Pruning_Player(1, maxdepth=3)
player2 = Random_Player(2)
game = Game(player1, player2)
start_game(game, graphics=True)

"""### Time performance

In this part, we want to check time performance of alpha beta pruning vs minmax. It may take a little time to run :)
"""

seed = 42
random.seed(seed)
player_2 = Random_Player(2)
minimax_time = []
alpha_beta_time = []
for i in tqdm(range(4)):
    start = time.time()
    player_1 = Min_Max_Player(1, maxdepth=i + 1)
    game = Game(player_1, player_2)
    start_game(game, graphics=False)
    minimax_time.append(time.time() - start)

    start = time.time()
    player_1 = Alpha_Beta_Pruning_Player(1, maxdepth=i + 1)
    game = Game(player_1, player_2)
    start_game(game, graphics=False)
    alpha_beta_time.append(time.time() - start)

plt.plot(list(range(1, len(minimax_time) + 1)), np.array(minimax_time) / 60, label="minimax time")
plt.plot(list(range(1, len(alpha_beta_time) + 1)), np.array(alpha_beta_time) / 60, label="alpha_beta time")
plt.legend(loc="upper right")
plt.xlabel('depth')
plt.ylabel('time (m)')
plt.show()

"""Now you can see how much alpha beta pruning has much better time performance (especially after depth = 3).

### Play for many times

Now we want to test alpha beta and minmax in 10 games and get number of wining. your score should so close to 20.
"""


def get_game_result(player_1, player_2, num_game):
    win, lose, draw = 0, 0, 0
    start = time.time()
    for i in tqdm(range(num_game)):
        random.seed()
        game = Game(player_1, player_2)
        result = start_game(game, graphics=False)
        if result == 1:
            win += 1
        elif result == 2:
            lose += 1
        else:
            draw += 1
    return win, lose, draw, (time.time() - start) / 60


def mark():
    player1 = Min_Max_Player(1, maxdepth=3)
    player2 = Random_Player(2)
    win1, lose1, draw1, t1 = get_game_result(player1, player2, 10)

    player1 = Alpha_Beta_Pruning_Player(1, maxdepth=3)
    player2 = Random_Player(2)
    win2, lose2, draw2, t2 = get_game_result(player1, player2, 10)
    print(f'minimax player vs random player win={win1}, lose={lose1}, draw={draw1}, time = {t1} m')
    print(f'alpha beta vs random player win={win2}, lose={lose2}, draw={draw2}, time = {t2} m')
    print(f'score: {win1 + win2}')


mark()

player1 = Random_Player(1)
player2 = Random_Player(2)
win2, lose2, draw2, t2 = get_game_result(player1, player2, 20)
print(f'random player win={win2}, lose={lose2}, draw={draw2}, time = {t2} m')

"""# Extra score * (20 points)

## Expectimax
In previous sections, you consider your opponent play **min** for each step or action. In this part, you should consider your opponent that play random and implement expectimax algorithm.
"""


class Expectimax_Player(Player):
    def next_move(self, game):
        """
        Parameters
        ----------
        game : `game.Game`
        An instance of `game.Game` encoding the current state of the
        game.

        Returns
        ----------
        (int, int) or None
        A possible move. The first int is the moving piece's location id, and the second int is the target location id.

        If None is returned when possible moves are available, it is an automatic forfeit, and the player will lose the match.

        Useful functions:
        ----------
        game.get_possible_moves
        game.apply
        game.get_player_score
        """
        best_move = self.expectimax(game, self.maxdepth)[0]
        return best_move

    def expectimax(self, game, depth):
        possible_moves = game.get_possible_moves()
        if depth == 0 or not possible_moves:
            return None, game.get_player_score(self.number)

        if game.turn % 2 == self.number % 2:
            best_move = None
            best_score = -float('inf')
            for move in possible_moves:
                next_game = game.copy()
                next_game.apply(move)
                _, score = self.expectimax(next_game, depth - 1)
                if score > best_score:
                    best_score = score
                    best_move = move
            return best_move, best_score

        else:
            best_move = None
            sum = 0
            for move in possible_moves:
                next_game = game.copy()
                next_game.apply(move)
                _, score = self.expectimax(next_game, depth - 1)
                sum += score
            return best_move, sum/len(possible_moves)

"""Now you can see the results by running below cell. We excpect that the results approximatly to be similar below image.
<img
    src="https://gcdnb.pbrd.co/images/OgejM08bTJet.png?o=1"
/>

## Test
"""


def mark():
    player1 = Alpha_Beta_Pruning_Player(1, maxdepth=3)
    player2 = Alpha_Beta_Pruning_Player(2, maxdepth=1)
    win1, _, _, _ = get_game_result(player1, player2, 5)

    player1 = Alpha_Beta_Pruning_Player(1, maxdepth=3)
    player2 = Random_Player(2)
    win2, _, _, _ = get_game_result(player1, player2, 5)

    player1 = Expectimax_Player(1, maxdepth=3)
    player2 = Alpha_Beta_Pruning_Player(2, maxdepth=3)
    win3, _, _, _ = get_game_result(player1, player2, 5)

    player1 = Expectimax_Player(1, maxdepth=3)
    player2 = Random_Player(2)
    win4, _, _, _ = get_game_result(player1, player2, 5)

    return win1, win2, win3, win4


result1, result2, result3, result4 = mark()

from prettytable import PrettyTable

myTab = PrettyTable([" ", "Adversarial", "Random"])
myTab.add_row(["Minimax", f'{result1} / 5', f'{result2} / 5'])
myTab.add_row(["Expectimax", f'{result3} / 5', f'{result4} / 5'])
print(myTab)
