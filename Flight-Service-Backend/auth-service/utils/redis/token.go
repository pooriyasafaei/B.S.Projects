package redis

import "fmt"

func CreateUnauthorizedRedisKey(token string) string {
	return fmt.Sprintf(
		"auth:unauthorized-token:%s",
		token,
	)
}
