# Auxiliary functions
import copy


class Functions:
    def __init__(self, domains, edges, halls_count) -> None:
        self.domains = domains
        self.edges = edges
        self.halls_count = halls_count
        self.neighbors = self.get_neighbors()

    @staticmethod
    def parser(halls_count, prefered_halls, next_e_lines):
        domains = {i: [] for i in range(1, halls_count + 1)}
        for index, halls in enumerate(prefered_halls, start=1):
            for hall in halls:
                domains[hall].append(index)
        edges = []
        for edge in next_e_lines:
            edges.append((int(edge[0]), int(edge[1])))
        return Functions(domains, edges, halls_count)

    def get_neighbors(self):
        neighbours = {i: [] for i in range(1, self.halls_count + 1)}
        for edge in self.edges:
            neighbours[edge[0]].append(edge[1])
            neighbours[edge[1]].append(edge[0])
        return neighbours

    def is_satisfied(self, assignments):
        for e1, e2 in self.edges:
            if assignments[e1] == assignments[e2]:
                return False
        return True

    def get_unassigned(self, assignments):
        for i in range(1, self.halls_count + 1):
            if i not in assignments.keys():
                return i

    def domain_values(self, variable):
        return self.domains[variable]

    def clone(self):
        return copy.deepcopy(self)

############################################################ cell 2

def ac_3(csp_properties: Functions):
    #################################################################
    # (Point: 30% of total score obtained by tests)                 #
    # This function returns false                                   #
    # if an inconsistency is found and true otherwise.              #
    # Feel free to also implement a revise function in this cell. #
    #################################################################
    queue = csp_properties.edges[:]
    while queue:
        e1, e2 = queue.pop(0)
        if revise(csp_properties, e1, e2):
            if not csp_properties.domains[e1]:
                return False
            for e in csp_properties.neighbors[e1]:
                if e != e2:
                    queue.append((e1, e))
    return True

def revise(csp_properties, e1, e2):
    revised = False
    for i in csp_properties.domains[e1]:
        if len(csp_properties.domains[e2]) == 1 and csp_properties.domains[e2] == i:
            csp_properties.domains[e1].remove(i)
            revised = True
    return revised


############################################################ cell 3

def backtrack(csp_properties: Functions, assignments):
    #################################################################
    # (Point: 60% of total score obtained by tests)                 #
    # This function returns a solution if there is a complete       #
    # assignment or failure if there is not                         #
    #################################################################
    if len(assignments) == csp_properties.halls_count:
        if csp_properties.is_satisfied(assignments):
            return assignments
        return
    variable = csp_properties.get_unassigned(assignments)
    for value in csp_properties.domain_values(variable):
        assignments[variable] = value
        _csp_properties = csp_properties.clone()
        if ac_3(_csp_properties):
            result = backtrack(_csp_properties, assignments)
            if result:
                return result
        del assignments[variable]
    return

def backtracking_search(csp_properties):
    #################################################################
    #                          (Optional)                           #
    #   Just in case you need an auxiliary function for backtrack   #
    #################################################################
    assignments = backtrack(csp_properties, {})
    if not assignments:
        assignments_str = 'NO'
    else:
        assignments_ordered = []
        for i in range(1, csp_properties.halls_count + 1):
            assignments_ordered.append(assignments[i])
        assignments_str = ' '.join(map(str, assignments_ordered))
    return assignments_str

############################################################ cell 4

######################

import Helper_codes.csp_helper as csp
import time
import networkx as nx
import matplotlib
matplotlib.use('TkAgg')
import matplotlib.pyplot as plt
import Helper_codes.csp_helper as helper


def plot_test_case(result, n, next_e_lines,
                   test_num):  # Do not change this function. This is for plotting the assignment.
    if result == 'NO':
        return
    reult_list = result.split()
    reult_list = list(map(int, reult_list))
    if len(reult_list) >= 15:
        print(f'Too many colors to plot for test {test_num + 1}')
        return

    G = nx.Graph()
    G.add_nodes_from(range(1, n + 1))
    for edge in next_e_lines:
        G.add_edge(edge[0], edge[1])
    colors = ['red', 'green', 'blue', 'yellow', 'orange', 'purple', 'pink', 'brown', 'gray', 'black', 'cyan', 'magenta',
              'olive', 'teal']

    color_map = []
    for i in range(1, n + 1):
        color_map.append(colors[reult_list[i - 1] - 1])

    nx.draw(G, node_color=color_map, with_labels=True)
    plt.show()


TIME_LIMIT = 3

tests = csp.get_all_tests(prefix='csp_')
tests_passed = 0
for test_num, test in enumerate(tests):
    n, m, m_next_lines, e, next_e_lines = csp.scan_test_input(test)
    #################################################################
    # (Point: 10% of total score obtained by tests)                 #
    # Replace this comment section with a piece of code to          #
    # handle inputs.                                                #
    #################################################################
    csp_properties = Functions.parser(n, m_next_lines, next_e_lines)
    start_time = time.time()
    result = backtracking_search(csp_properties)
    delay = time.time() - start_time
    if helper.is_result_valid(test, result) and delay < TIME_LIMIT:
        tests_passed += 1
    else:
        print(f'test {test} failed. time elapsed= {delay}')
    plot_test_case(result, n, next_e_lines, test_num)
    print('----------------------------------------------------------')
# (Point: 50% of your total score)                                    #
print(f'Score = {tests_passed / len(tests) * 100}%')
