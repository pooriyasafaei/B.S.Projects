import gym
import numpy as np
import time


class FrozenLake:
    def __init__(self, map, rewards, is_slippery=False):
        self.env = gym.make('FrozenLake-v1', is_slippery=is_slippery, desc=map, render_mode='human')
        self.num_states = self.env.observation_space.n
        self.num_actions = self.env.action_space.n
        self.env.reset()
        self.env.render()


        for i in range(self.num_states):
            for step in self.env.P[i].values():
                for i in range(len(step)):
                    move = step[i]
                    print(move)
                    step[i] = (move[0], move[1], rewards[move[1]], move[3])

    def policy_iteration(self, gamma=0.9, epsilon=1e-6):
        num_states = self.num_states
        num_actions = self.num_actions

        policy = np.random.randint(0, num_actions, size=num_states)
        iterations = 0
        while True:
            V = np.zeros(num_states)
            while True:
                delta = 0
                for s in range(num_states):
                    v = sum(
                        self.env.P[s][policy[s]][0][0] * (
                                self.env.P[s][policy[s]][0][2] + gamma * V[self.env.P[s][policy[s]][0][1]])
                        for s1 in range(len(self.env.P[s][policy[s]])))
                    delta = max(delta, np.abs(v - V[s]))
                    V[s] = v
                if delta < epsilon:
                    break
                iterations += 1

            policy_stable = True
            for s in range(num_states):
                old_action = policy[s]
                q_values = [sum(self.env.P[s][a][0][0] * (self.env.P[s][a][0][2] + gamma * V[self.env.P[s][a][0][1]])
                                for s1 in range(len(self.env.P[s][a]))) for a in range(num_actions)]
                policy[s] = np.argmax(q_values)
                if old_action != policy[s]:
                    policy_stable = False

            if policy_stable:
                break


        return policy, V, iterations

    def value_iteration(self, gamma=0.9, epsilon=1e-6):
        num_states = self.num_states
        num_actions = self.num_actions

        V = np.zeros(num_states)
        iterations = 0

        while True:
            delta = 0
            for s in range(num_states):
                v = max((self.env.P[s][a][0][0] * (self.env.P[s][a][0][2] + gamma * V[self.env.P[s][a][0][1]])
                         ) for a in range(num_actions))
                delta = max(delta, np.abs(v - V[s]))
                V[s] = v

            if delta < epsilon:
                break
            iterations += 1

        policy = np.zeros(num_states, dtype=int)
        for s in range(num_states):
            q_values = [self.env.P[s][a][0][0] * (self.env.P[s][a][0][2] + gamma * V[self.env.P[s][a][0][1]])
                        for a in range(num_actions)]
            policy[s] = np.argmax(q_values)

        return policy, V, iterations


map = ['SFFF', 'FFFH', 'FFFH', 'FHFF', 'FFFG']
map_reward = []

for row in map:
    for char in row:
        if char == 'S':
            map_reward.append(0)
        elif char == 'F':
            map_reward.append(0)
        elif char == 'H':
            map_reward.append(-1)
        elif char == 'G':
            map_reward.append(1)

custom_env = FrozenLake(map, map_reward)
print(custom_env.env.P[0][0])
# Run policy iteration
optimal_policy, optimal_value_function, iterations = custom_env.policy_iteration()

print("Value Iteration Algorithm Policy Number of Iterations:")
print(iterations)
print("\nOptimal Value Iteration Algorithm Policy:")
print(optimal_policy.reshape((5, 4)))
print("\nOptimal Value Iteration Algorithm Value Function:")
print(optimal_value_function.reshape((5, 4)))

# Run policy iteration
optimal_policy, optimal_value_function, iterations = custom_env.value_iteration()

print("\nPolicy Iteration Algorithm Policy Number of Iterations:")
print(iterations)
print("\nOptimal Policy Iteration Algorithm Policy:")
print(optimal_policy.reshape((5, 4)))
print("\nOptimal Policy Iteration Algorithm Value Function:")
print(optimal_value_function.reshape((5, 4)))