package controller

import (
	"auth-service/database/models"
	"auth-service/redis"
	redisUtils "auth-service/utils/redis"
	"errors"
	"time"
)

func UnAuthorizedSingleToken(userId int64, token string, expUnixTime int64) error {
	expTime := time.Unix(expUnixTime, 1)

	if err := models.AddUnauthorizedToken(userId, token, expTime); err != nil {
		return errors.New("you can not logout")
	}

	redisKey := redisUtils.CreateUnauthorizedRedisKey(token)

	redisKeyExp := time.Duration(expUnixTime - time.Now().Unix())

	if err := redis.Client.Set(redis.Ctx, redisKey, "1", redisKeyExp*time.Second).Err(); err != nil {
		return errors.New("you can not logout")
	}

	return nil
}
