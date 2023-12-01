import math

import numpy as np
import random
import matplotlib
import matplotlib.pyplot as plt

matplotlib.use('TkAgg')

plt.figure(figsize=(10, 6))


def generate_random_samples(q_a, variance, num_samples):
    samples = np.random.normal(q_a, np.sqrt(variance), num_samples)
    return samples


def epsilon_greedy(epsilons, action_means, steps, repetition):
    avg_r = np.zeros((len(epsilons), steps))

    for e, epsilon in enumerate(epsilons):
        for i in range(repetition):
            est_values = np.zeros(n)
            a_count = np.zeros(n)

            for step in range(steps):
                if random.random() < epsilon:
                    action = random.randint(0, n - 1)
                else:
                    action = np.argmax(est_values)

                q_a = action_means[action]
                r = generate_random_samples(q_a, 1, 1)[0]
                a_count[action] += 1
                est_values[action] += (1 / a_count[action]) * (
                        r - est_values[action])
                avg_r[e, step] += r

    avg_r /= repetition
    return avg_r


def softmax(T,  action_means, steps, repetition, T_decay=1,):
    avg_r = np.zeros(steps)
    init_T = T
    for i in range(repetition):
        est_values = np.zeros(n)
        T = init_T
        for step in range(steps):
            a_chance = np.exp(est_values / T) / np.sum(np.exp(est_values / T))
            a = np.random.choice(n, p=a_chance)
            q_a = action_means[a]
            reward = generate_random_samples(q_a, 1, 1)[0]
            est_values[a] += (1 / (step + 1)) * (reward - est_values[a])
            avg_r[step] += reward
            T *= T_decay

    avg_r /= repetition
    return avg_r


action_means = [0.25, -0.6, 1.5, 0.5, 1.25, -1.5, -0.2, -1.0, 0.9, -0.5]
n = 10
steps = 1000
repetition = 2000
eps = [0.01, 0.1, 0.0]
T = 1.0
decay = 0.995

eps_greedy_rewards = epsilon_greedy(eps, action_means, steps, repetition)
softmax_rewards = softmax(T, action_means, steps, repetition)
decay_softmax_rewards = softmax(T, action_means, steps, repetition, decay)

for e, ep in enumerate(eps):
    plt.plot(eps_greedy_rewards[e], label=f"Epsilon = {ep}")

plt.plot(softmax_rewards, label=f"SoftMax T = {T}")
plt.plot(decay_softmax_rewards, label=f"Decay SoftMax T = {T}")

plt.xlabel("Steps")
plt.ylabel("Avg. Reward")
plt.legend()
plt.title("Average Reward(Steps)")
plt.show()

