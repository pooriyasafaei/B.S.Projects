value_function[s] = dynamic[s][policy[s]][0] * (
            dynamic[s][policy[s]][2] + gamma * value_function[dynamic[s][policy[s]][1]])
