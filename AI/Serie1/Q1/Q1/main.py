###########################################
###### no need to change any thing ########
###########################################

import pygame, time, csv
import numpy as np
from time import sleep
import heapq
from IPython.display import HTML


class Node:
    def __init__(self, pos, parent, cost=None):
        self.x = pos[0]
        self.y = pos[1]
        self.parent = parent
        self.cost = cost

    def position(self):
        return self.x, self.y


class Queue:
    def __init__(self):
        self.list = []

    def push(self, item):
        self.list.insert(0, item)

    def pop(self):
        return self.list.pop()

    def isEmpty(self):
        return len(self.list) == 0


class PriorityQueue:
    """ O(1) access to the lowest-priority item """

    def __init__(self):
        self.heap = []
        self.count = 0

    def push(self, item, priority):
        entry = (priority, self.count, item)
        heapq.heappush(self.heap, entry)
        self.count += 1

    def pop(self):
        (_, _, item) = heapq.heappop(self.heap)
        return item

    def isEmpty(self):
        return len(self.heap) == 0

    def update(self, item, priority):
        # If item already in priority queue with higher priority, update its priority and rebuild the heap.
        for index, (p, c, i) in enumerate(self.heap):
            if i == item:
                if p <= priority:
                    break
                del self.heap[index]
                self.heap.append((priority, c, item))
                heapq.heapify(self.heap)
                break
        else:
            self.push(item, priority)


class Stack:
    def __init__(self):
        self.list = []

    def push(self, item):
        self.list.append(item)

    def pop(self):
        return self.list.pop()

    def isEmpty(self):
        return len(self.list) == 0


###########################################
###### no need to change any thing ########
###########################################

def solve_maze(map_address, algorithm):
    grid = np.genfromtxt(map_address, delimiter=',', dtype=int)
    num_rows, num_columns = len(grid), len(grid[0])
    empty_block_count = np.count_nonzero(grid == 1)

    # Define start & goal positions
    start_pos = (0, 0)
    goal_pos = (num_rows - 1, num_columns - 1)

    grid[0, 0] = 2
    grid[-1, -1] = 3

    grid_dim = (num_rows - 1, num_columns - 1)

    black, white, green, red, grey, blue, magenta = (0, 0, 0), (255, 255, 255), (50, 205, 50), (255, 99, 71), (
        211, 211, 211), (153, 255, 255), (255, 0, 255)
    idx_to_color = [black, white, green, red, blue, magenta]

    height = 15
    width = height
    margin = 1

    pygame.init()

    WINDOW_SIZE = [660, 660]
    screen = pygame.display.set_mode(WINDOW_SIZE)

    pygame.display.set_caption(f"{algorithm} Pathfinder. Solving: {map_address}")

    done = False
    run = False
    close = False

    clock = pygame.time.Clock()

    digital_twin = None

    if algorithm == "BFS":
        digital_twin = BFS_Digital_Twin(start_pos=start_pos, goal_pos=goal_pos, grid_dim=grid_dim)
    elif algorithm == "DFS":
        digital_twin = DFS_Digital_Twin(start_pos=start_pos, goal_pos=goal_pos, grid_dim=grid_dim)
    elif algorithm == "IDS":
        digital_twin = IDS_Digital_Twin(start_pos=start_pos, goal_pos=goal_pos, grid_dim=grid_dim)
    elif algorithm == "A_Star":
        digital_twin = A_Star_Digital_Twin(start_pos=start_pos, goal_pos=goal_pos, grid_dim=grid_dim)
    else:
        return None

    while not done:
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                done = True

            elif event.type == pygame.KEYDOWN:
                run = True
                start_t0 = time.time()

        screen.fill(grey)

        for row in range(num_rows):
            for column in range(num_columns):
                color = idx_to_color[grid[row, column]]
                pygame.draw.rect(screen, color,
                                 [(margin + width) * column + margin, (margin + height) * row + margin, width, height])

        clock.tick(60)
        pygame.display.flip()

        if run == True:
            sleep(0.01)
            solution, done, grid = digital_twin.update(grid=grid)

        if done == True:
            print(f"Total empty block numbers: {empty_block_count}")
            print(f"Explored block numbers: {np.count_nonzero(grid == 4)}")
            for pos in solution:
                grid[pos[0], pos[1]] = 5

            screen.fill(grey)

            for row in range(num_rows):
                for column in range(num_columns):
                    color = idx_to_color[grid[row, column]]
                    pygame.draw.rect(screen, color,
                                     [(margin + width) * column + margin, (margin + height) * row + margin, width,
                                      height])

            clock.tick(60)
            pygame.display.flip()

    print(f"Your maze solved with {algorithm} algorithm.")
    print(f"--- finished {time.time() - start_t0:.3f} s---")
    while not close:
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                close = True

            elif event.type == pygame.KEYDOWN:
                close = True
    pygame.quit()


###########################################
###### no need to change any thing ########
###########################################

class DFS_Digital_Twin:
    def __init__(self, start_pos, goal_pos, grid_dim):
        self.start_pos = start_pos
        self.goal_pos = goal_pos
        self.grid_dim = grid_dim
        self.stack = Stack()
        self.stack.push(Node(pos=start_pos, parent=None))

    def get_successors(self, x, y):
        return [(x + 1, y), (x - 1, y), (x, y + 1), (x, y - 1)]

    def is_valid_cell(self, pos):
        return 0 <= pos[0] <= self.grid_dim[0] and 0 <= pos[1] <= self.grid_dim[1]

    def backtrack_solution(self, curr_node):
        return self._backtrack(curr_node)

    def _backtrack(self, curr_node):
        return [] if curr_node.parent is None else self._backtrack(curr_node.parent) + [curr_node.position()]

    def update(self, grid):
        curr_state = self.stack.pop()
        x, y = curr_state.position()
        done = False
        solution_path = []

        for step in self.get_successors(x, y):
            if self.is_valid_cell(step) and \
                    grid[step[0], step[1]] in [1, 3]:  # 1: empty cell has not explored yet, 3: goal cell
                self.stack.push(Node(pos=step, parent=curr_state))

                if step == self.goal_pos:
                    done = True
                    solution_path = self.backtrack_solution(curr_state)
                    break

            grid[x, y] = 4  # visited

        return solution_path, done, grid

########################################################
######### implement BFS algorithm (10 Points) ##########
########################################################

class BFS_Digital_Twin:
    def __init__(self, start_pos, goal_pos, grid_dim):
        self.start_pos = start_pos
        self.goal_pos = goal_pos
        self.grid_dim = grid_dim
        self.queue = Queue()
        self.queue.push(Node(pos=start_pos, parent=None))

    def get_successors(self, x, y):
        return [(x + 1, y), (x - 1, y), (x, y + 1), (x, y - 1)]

    def is_valid_cell(self, pos):
        return 0 <= pos[0] <= self.grid_dim[0] and 0 <= pos[1] <= self.grid_dim[1]

    def backtrack_solution(self, curr_node):
        return self._backtrack(curr_node)

    def _backtrack(self, curr_node):
        return [] if curr_node.parent is None else self._backtrack(curr_node.parent) + [curr_node.position()]

    def update(self, grid):
        curr_state = self.queue.pop()
        x, y = curr_state.position()
        done = False
        solution_path = []

        for step in self.get_successors(x, y):
            if self.is_valid_cell(step) and \
                    grid[step[0], step[1]] in [1, 3]:  # 1: empty cell has not explored yet, 3: goal cell
                self.queue.push(Node(pos=step, parent=curr_state))

                if step == self.goal_pos:
                    done = True
                    solution_path = self.backtrack_solution(curr_state)
                    break

            grid[x, y] = 4  # visited

        return solution_path, done, grid


########################################################
######### implement IDS algorithm (15 Points) ##########
########################################################

class IDS_Digital_Twin:
    def __init__(self, start_pos, goal_pos, grid_dim):
        self.start_pos = start_pos
        self.goal_pos = goal_pos
        self.grid_dim = grid_dim
        self.max_depth = 0  # Maximum depth for the current iteration
        self.solution_path = []
        self.stack = Stack()
        self.stack.push(Node(pos=start_pos, parent=None, cost=0))
        self.visited = []

    def get_successors(self, x, y):
        return [(x + 1, y), (x - 1, y), (x, y + 1), (x, y - 1)]

    def is_valid_cell(self, pos):
        return 0 <= pos[0] <= self.grid_dim[0] and 0 <= pos[1] <= self.grid_dim[1]

    def backtrack_solution(self, curr_node):
        return self._backtrack(curr_node)

    def _backtrack(self, curr_node):
        return [] if curr_node.parent is None else self._backtrack(curr_node.parent) + [curr_node.position()]

    def update(self, grid):
        if self.stack.isEmpty():
            self.max_depth += 1
            for state in self.visited:
                v_x, v_y = state.position()
                grid[v_x, v_y] = 1
            self.visited = []
            self.solution_path = []
            self.stack = Stack()
            self.stack.push(Node(pos=self.start_pos, parent=None, cost=0))

            return [], False, grid

        curr_state = self.stack.pop()
        x, y = curr_state.position()
        done = False
        solution_path = []

        if not curr_state.cost == self.max_depth:
            for step in self.get_successors(x, y):
                if self.is_valid_cell(step) and \
                        grid[step[0], step[1]] in [1, 3]:  # 1: empty cell has not explored yet, 3: goal cell
                    self.stack.push(Node(pos=step, parent=curr_state, cost=curr_state.cost + 1))

                    if step == self.goal_pos:
                        done = True
                        solution_path = self.backtrack_solution(curr_state)
                        break

                grid[x, y] = 4  # visited
                self.visited.append(curr_state)

        return solution_path, done, grid



########################################################
######### implement A* algorithm (50 Points) ###########
########################################################

class A_Star_Digital_Twin:
    def __init__(self, start_pos, goal_pos, grid_dim):
        self.start_pos = start_pos
        self.goal_pos = goal_pos
        self.grid_dim = grid_dim
        self.max_depth = 0  # Maximum depth for the current iteration
        self.solution_path = []
        self.queue = PriorityQueue()
        self.queue.push(Node(pos=start_pos, parent=None, cost=0), self.manhattan_distance(start_pos))
        self.visited = []

    def manhattan_distance(self, pos):
        return abs(pos[0] - self.goal_pos[0]) + abs(pos[1] - self.goal_pos[1])

    def get_successors(self, x, y):
        return [(x + 1, y), (x - 1, y), (x, y + 1), (x, y - 1)]

    def is_valid_cell(self, pos):
        return 0 <= pos[0] <= self.grid_dim[0] and 0 <= pos[1] <= self.grid_dim[1]

    def backtrack_solution(self, curr_node):
        return self._backtrack(curr_node)

    def _backtrack(self, curr_node):
        return [] if curr_node.parent is None else self._backtrack(curr_node.parent) + [curr_node.position()]

    def update(self, grid):
        curr_state = self.queue.pop()
        x, y = curr_state.position()
        done = False
        solution_path = []

        for step in self.get_successors(x, y):
            if self.is_valid_cell(step) and \
                    grid[step[0], step[1]] in [1, 3]:  # 1: empty cell has not explored yet, 3: goal cell
                self.queue.push(Node(pos=step, parent=curr_state, cost=curr_state.cost + 1),
                                self.manhattan_distance(step) + curr_state.cost + 1)

                if step == self.goal_pos:
                    done = True
                    solution_path = self.backtrack_solution(curr_state)
                    break

            grid[x, y] = 4  # visited
            self.visited.append(curr_state)

        return solution_path, done, grid

solve_maze(map_address="mazes/maze_1.csv", algorithm="A_Star")